import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GameFrame extends JFrame {

	public static final int GAME_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height; // 720p game resolution
	public static final int GAME_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;  // wide aspect ratio
	private boolean victory = false;
	private boolean defeat = false;
	private int loop = 0;
	private long lastRender;
	private ArrayList<Float> fpsHistory;
	private BufferedImage shooter;
	private BufferStrategy bufferStrategy;
	private Image background;
	private static Rectangle frameRectangle;


	public GameFrame(String title) {
		super(title);
		setResizable(false);
		setSize(GAME_WIDTH, GAME_HEIGHT);
		frameRectangle = new Rectangle();
		frameRectangle.setLocation(0, 0);
		frameRectangle.setSize(GAME_WIDTH, GAME_HEIGHT);
		Sounds.getGameSound();

		try {
			background = ImageIO.read(new File("pics\\background.png"));
			background = background.getScaledInstance(6000, 6000, java.awt.Image.SCALE_SMOOTH);
			shooter = ImageIO.read(new File("pics\\shooter.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lastRender = -1;
		fpsHistory = new ArrayList<>(100);

	}

	/**
	 * This must be called once after the JFrame is shown:
	 * frame.setVisible(true);
	 * and before any rendering is started.
	 */
	public void initBufferStrategy() {
		// Triple-buffering
		createBufferStrategy(3);
		bufferStrategy = getBufferStrategy();
	}


	/**
	 * Game rendering with triple-buffering using BufferStrategy.
	 */
	public void render(GameState state) {
		// Render single frame
		do {
			// The following loop ensures that the contents of the drawing buffer
			// are consistent in case the underlying surface was recreated
			do {
				// Get a new graphics context every time through the loop
				// to make sure the strategy is validated
				Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
				try {
					doRendering(graphics, state);
				} finally {
					// Dispose the graphics
					graphics.dispose();
				}
				// Repeat the rendering if the drawing buffer contents were restored
			} while (bufferStrategy.contentsRestored());

			// Display the buffer
			bufferStrategy.show();
			// Tell the system to do the drawing NOW;
			// otherwise it can take a few extra ms and will feel jerky!
			Toolkit.getDefaultToolkit().sync();

			// Repeat the rendering if the drawing buffer was lost
		} while (bufferStrategy.contentsLost());
	}

	/**
	 * Rendering all game elements based on the game state.
	 */
	private void doRendering(Graphics2D g2d, GameState state) {

		g2d.drawImage(background, 0, 0, null);
		g2d.drawImage(background,-MainTank.currentX,-MainTank.currentY,null);
		for (int i = 0; i < Reward.rewards.size(); i++) {
			if (Reward.rewards.get(i).mold.intersects(frameRectangle))
				Reward.rewards.get(i).doRendering(g2d);
		}
		Wall.doRendering(g2d);
		MainTank.createMainTank().doRendering(g2d);
		for (int i = 0; i < EnemyTank.enemies.size(); i++) {
			if (EnemyTank.enemies.get(i).mold.intersects(frameRectangle))
				EnemyTank.enemies.get(i).doRendering(g2d);
		}

		Bullet.doRendering(g2d);
		Cursor shooterCursor = Toolkit.getDefaultToolkit().createCustomCursor(shooter, new Point(0, 0), "shooter");

// Set the blank cursor to the JFrame.
		this.getContentPane().setCursor(shooterCursor);
		// Print FPS info
		long currentRender = System.currentTimeMillis();
		if (lastRender > 0) {
			fpsHistory.add(1000.0f / (currentRender - lastRender));
			if (fpsHistory.size() > 100) {
				fpsHistory.remove(0); // remove oldest
			}
			float avg = 0.0f;
			for (float fps : fpsHistory) {
				avg += fps;
			}
			avg /= fpsHistory.size();
			String str = String.format("Average FPS = %.1f , Last Interval = %d ms",
					avg, (currentRender - lastRender));
			g2d.setColor(Color.CYAN);
			g2d.setFont(g2d.getFont().deriveFont(18.0f));
			int strWidth = g2d.getFontMetrics().stringWidth(str);
			int strHeight = g2d.getFontMetrics().getHeight();
			g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, strHeight + 50);
		}
		lastRender = currentRender;
		// Print user guide
		String userGuide
				= "Use the MOUSE or ARROW KEYS to move the BALL. "
				+ "Press ESCAPE to end the game.";
		g2d.setFont(g2d.getFont().deriveFont(18.0f));
		g2d.drawString(userGuide, 10, GAME_HEIGHT - 10);
		// Draw GAME OVER
		if (state.gameOver) {
			String str = "GAME OVER";
			g2d.setColor(Color.WHITE);
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
			int strWidth = g2d.getFontMetrics().stringWidth(str);
			g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, GAME_HEIGHT / 2);
			}
		if(state.pauseGame){
			String str = "PAUSED";
			g2d.setColor(Color.WHITE);
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
			int strWidth = g2d.getFontMetrics().stringWidth(str);
			g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, GAME_HEIGHT / 2);
		}
		if(state.win){
			victory = true;
			state.win = false;
		}
		if(state.lose){
			defeat = true;
			state.lose = false;
		}
		if(defeat){
			loop++;
			String str = "GAME OVER ";
			g2d.setColor(Color.WHITE);
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
			int strWidth = g2d.getFontMetrics().stringWidth(str);
			g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, GAME_HEIGHT / 2);
			if(loop > 10){
				state.gameOver = true;
			}
		}
		if(victory){
			loop++;
			String str = "VICTORY";
			g2d.setColor(Color.WHITE);
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
			int strWidth = g2d.getFontMetrics().stringWidth(str);
			g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, GAME_HEIGHT / 2);
			if(loop > 10){
				state.gameOver = true;
				victory = false;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Main.gameStage++;
				Main.main(new String[]{"a"});
			}
		}
	}


	public static Rectangle getFrameRectangle() {
		return frameRectangle;
	}
}
