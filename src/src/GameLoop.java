import java.util.Iterator;

/**
 * A very simple structure for the main game loop.
 * THIS IS NOT PERFECT, but works for most situations.
 * Note that to make this work, none of the 2 methods
 * in the while loop (update() and render()) should be
 * long running! Both must execute very quickly, without
 * any waiting and blocking!
 * <p>
 * Detailed discussion on different game loop design
 * patterns is available in the following link:
 * http://gameprogrammingpatterns.com/game-loop.html
 *
 * @author Seyed Mohammad Ghaffarian
 */
public class GameLoop implements Runnable {

	/**
	 * Frame Per Second.
	 * Higher is better, but any value above 24 is fine.
	 */
	public static final int FPS = 30;

	private GameFrame canvas;
	private GameState state;

	public GameLoop(GameFrame frame) {
		canvas = frame;
	}

	/**
	 * This must be called before the game loop starts.
	 */
	public void init() {
		state = new GameState();
		MainTank mainTank = MainTank.createMainTank();
		canvas.addKeyListener(mainTank.getKeyListener());
		canvas.addMouseListener(mainTank.getMouseListener());
		canvas.addMouseMotionListener(mainTank.getMouseMotionListener());
	}

	@Override
	public void run() {
		boolean gameOver = false;
		while (!gameOver) {
			try {
				long start = System.currentTimeMillis();
				//
				canvas.render(state);

				if(!GameState.pauseGame) {
					state.update();
					Bullet.update();
					//GameFrame.getFrameRectangle().setLocation(MainTank.currentX,MainTank.currentY);
					Iterator<EnemyTank> enemyTankIterator = EnemyTank.enemies.iterator();
					while (enemyTankIterator.hasNext()) {
						EnemyTank enemyTank = enemyTankIterator.next();
						if (enemyTank.getHealthy() > 0)
							enemyTank.update();

						else
							enemyTankIterator.remove();
					}
					gameOver = state.gameOver;
					//
				}
				long delay = (1000 / FPS) - (System.currentTimeMillis() - start);
				if (delay > 0)
					Thread.sleep(delay);
			} catch (InterruptedException ex) {
			}
		}
		canvas.render(state);
	}
}
