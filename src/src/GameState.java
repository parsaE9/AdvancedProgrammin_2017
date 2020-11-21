
/**
 * This class holds the state of game and all of its elements.
 * This class also handles user inputs, which affect the game state.
 *
 * @author Seyed Mohammad Ghaffarian
 */
public class GameState {

    public static boolean gameOver;
    public static boolean pauseGame;
    public static boolean win;
    public static boolean lose;

    public GameState() {
        win = false;
        lose = false;
        gameOver = false;
        pauseGame = false;
    }

    /**
     * The method which updates the game state.
     */
    public void update() {
        MainTank.createMainTank().update();
        Bullet.update();
    }

    /**
     * sets the status of game
     * @param over true if game is over , false if game is not over yet
     */
    public static void setGameOver(boolean over) {
        gameOver = over;
    }

    /**
     * sets the game in pause status
     */
    public static void setPauseGame(){
        if(pauseGame)
            pauseGame = false;
        else
            pauseGame = true;
    }
}
