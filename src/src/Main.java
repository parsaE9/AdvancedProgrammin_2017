import java.awt.EventQueue;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

public class Main {

    public static int gameStage = 1;
    public static GameFrame frame;
    public static void main(String[] args) {

        if(gameStage == 1){
            Menu menu = new Menu();
            menu.createMenu();
            new Sounds();

            do {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (menu.mapAddress.equals(""));

        }
        if(gameStage == 2){
            frame.dispose();
            Reward.rewards = new ArrayList<>();
            Wall.walls = new ArrayList<>();
            Tank.enemies = new ArrayList<>();
            new Sounds();
            Menu.mapAddress = "maps\\map2.txt";
        }



        // Initialize the global thread-pool
        ThreadPool.init();


        // After the player clicks 'PLAY' ...
        //creates the map of game
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = null;
                try (Scanner fileReader = new Scanner(new File(Menu.mapAddress))) {
                    int lineCounter = 0;
                    while (fileReader.hasNextLine() && lineCounter != 30) {
                        int charCounter = 0;
                        char[] chars = fileReader.nextLine().toCharArray();
                        while (charCounter != 30) {
                            switch (chars[charCounter]) {
                                case 'H':
                                    new HardWall(charCounter * 200, lineCounter * 200);
                                    break;
                                case 'S':
                                    new SoftWall(charCounter * 200, lineCounter * 200);
                                    break;
                                case 'A':
                                    new AwardWall(charCounter * 200, lineCounter * 200);
                                    break;
                                case 'T':
                                    new MovingType1(charCounter * 200, lineCounter * 200);
                                    break;
                                case 'Y' :
                                    new MovingType2(charCounter * 200, lineCounter * 200,10);
                                    break;
                                case 'G':
                                    new Mine(charCounter * 200, lineCounter * 200);
                                    break;
                                case 'F':
                                    new NonMovingTank(charCounter * 200, lineCounter * 200);
                                    break;
                                case 'U':
                                    new Upgrade(charCounter * 200, lineCounter * 200);
                                    break;
                                case 'R':
                                    new FixedTank(charCounter * 200, lineCounter * 200);
                                    break;
                                case 'C':
                                    new RefillCannon(charCounter * 200, lineCounter * 200);
                                    break;
                                case 'M':
                                    new RefillMachineGun(charCounter * 200, lineCounter * 200);
                                    break;
                                case 'E':
                                    new EndGame(charCounter * 200, lineCounter * 200);
                                    break;
                                case 'D':
                                    new Shield(charCounter * 200, lineCounter * 200);
                                    break;
                            }
                            charCounter++;
                        }
                        lineCounter++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                frame = new GameFrame("Normal Tanks");
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.initBufferStrategy();
                // Create and execute the game-loop
                GameLoop game = new GameLoop(frame);
                game.init();
                ThreadPool.execute(game);
                // and the game starts ...
            }
        });
    }
}
