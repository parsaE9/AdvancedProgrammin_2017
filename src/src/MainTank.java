import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainTank extends Tank {
    private static MainTank instance = null;
    private boolean keyPressed;
    private boolean mousePress;
    private MainTank.KeyHandler keyHandler;
    private MainTank.MouseHandler mouseHandler;
    private BufferedImage machineGunCounterPng;
    private BufferedImage cannonCounterPng;
    private int cannonCounter;
    private int machineGunCounter;
    private BufferedImage heartPng;
    private BufferedImage shieldPng;
    private int mod;
    private int cheat;

    private MainTank() {
        delay = 0;
        side = 2;
        health = 5;
        moveSide = 2;
        cannonCounter = 50;
        machineGunCounter = 200;
        try {
            machineGunCounterPng = ImageIO.read(new File("pics\\machineGunCounter.png"));
            cannonCounterPng = ImageIO.read(new File("pics\\cannonCounter.png"));
            heartPng = ImageIO.read(new File("pics\\heart.png"));
            shieldPng = ImageIO.read(new File("pics\\shield2.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        locX = 300;
        locY = 200;

        keyPressed = false;

        mousePress = false;

        try {
            tankBody = ImageIO.read(new File("pics\\KV-2Body.png"));
            tankShooter = ImageIO.read(new File("pics\\KV-2Shooter.png"));
            mold = new Rectangle();
            mold.setSize(tankBody.getWidth(), tankBody.getHeight());
            tempBody = tankBody;
            tempShooter = tankShooter;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        rotateTime = 90;
        keyHandler = new MainTank.KeyHandler();
        mouseHandler = new MainTank.MouseHandler();
    }


    public void update() {
        if (keyPressed && rotateTime == 90) {
            switch (moveSide) {
                case 4:
                    if (side != 4) {
                        rotateTime = 0;
                        //side = 4;
                    } else if (!collision(tempMold(side))) {
                        if (212 > locY && locY >= 204) {
                            currentY -= 8;
                            for(Wall wall :Wall.getWalls()) {
                                wall.update();
                            }
                            for(Reward reward :Reward.rewards) {
                                reward.update();
                            }
                        } else {
                            locY -= 8;
                        }
                    }
                    break;
                case 2:
                    if (side != 2) {
                        //rotate(2 - side);
                        rotateTime = 0;
                        //side = 2;
                    } else if (!collision(tempMold(side))) {
                        if (GameFrame.GAME_HEIGHT - tankBody.getHeight() - 200 >= locY && locY > GameFrame.GAME_HEIGHT - tankBody.getHeight() - 208) {
                            currentY += 8;
                            for(Wall wall :Wall.getWalls()) {
                                wall.update();
                            }
                            for(Reward reward :Reward.rewards) {
                                reward.update();
                            }
                        } else {
                            locY += 8;
                        }
                    }
                    break;
                case 3:
                    if (side != 3) {
                        //rotate(3 - side);
                        rotateTime = 0;
                        //side = 3;
                    } else if (!collision(tempMold(side))) {
                        if (308 > locX && locX >= 300) {
                            currentX -= 8;
                            for(Wall wall :Wall.getWalls()) {
                                wall.update();
                            }
                            for(Reward reward :Reward.rewards) {
                                reward.update();
                            }
                        } else {
                            locX -= 8;
                        }
                    }
                    break;
                case 1:
                    if (side != 1) {
                        //  rotate(1 - side);
                        rotateTime = 0;
                        // side = 1;
                    } else if (!collision(tempMold(side))) {
                        if (GameFrame.GAME_WIDTH - tankBody.getHeight() - 300 >= locX && locX > GameFrame.GAME_WIDTH - tankBody.getHeight() - 308) {
                            currentX += 8;
                            for(Wall wall :Wall.getWalls()) {
                                wall.update();
                            }
                            for(Reward reward :Reward.rewards) {
                                reward.update();
                            }
                        } else {
                            locX += 8;
                        }
                    }
                    break;
            }
            locX = Math.max(locX, 300);
            locX = Math.min(locX, GameFrame.GAME_WIDTH - tankBody.getHeight() - 300);
            locY = Math.max(locY, 200);
            locY = Math.min(locY, GameFrame.GAME_HEIGHT - tankBody.getHeight() - 200);
        } else if (rotateTime != 90) {
            rotateTime += 8;
            if(rotateTime < 90 && rotateTime > 82){
                rotateTime = 90;
            }
            PointerInfo a = MouseInfo.getPointerInfo();
            rotate(((double) (moveSide - side) / 90) * rotateTime * Math.PI / 2);
            if (rotateTime == 90) {
                side = moveSide;
                tankBody = tempBody;
                // tankShooter = tempShooter;
            }
        }
        mold.setLocation(locX, locY);
        delay();
    }

    public KeyListener getKeyListener() {
        return keyHandler;
    }


    public MouseListener getMouseListener() {
        return mouseHandler;
    }


    public MouseMotionListener getMouseMotionListener() {
        return mouseHandler;
    }


    class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (rotateTime == 90) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        moveSide = 4;
                        keyPressed = true;
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        moveSide = 2;
                        keyPressed = true;
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        moveSide = 3;
                        keyPressed = true;
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        moveSide = 1;
                        keyPressed = true;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        break;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    if (moveSide == 4)
                        keyPressed = false;
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    if (moveSide == 2)
                        keyPressed = false;
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    if (moveSide == 3)
                        keyPressed = false;
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    if (moveSide == 1)
                        keyPressed = false;
                    break;
                case KeyEvent.VK_F:
                    cheat = 1;
                    break;
                case KeyEvent.VK_U:
                    if(cheat == 1)
                        cheat = 2;
                    else
                        cheat = 0;
                    break;
                case KeyEvent.VK_L:
                    if(cheat == 2)
                        cheat = 3;
                    else if(cheat == 3)
                        cheat = 4;
                    else
                        cheat = 0;
                    break;
                case KeyEvent.VK_H:
                    if(cheat == 4)
                        MainTank.createMainTank().fixedTank();
                    cheat = 0;
                    break;
                case KeyEvent.VK_C:
                    if(cheat == 4)
                        MainTank.createMainTank().setCannonCounter(50);
                    cheat = 0;
                    break;
                case KeyEvent.VK_M:
                    if(cheat == 4)
                        MainTank.createMainTank().setMachineGunCounter(200);
                    cheat = 0;
                    break;
                case KeyEvent.VK_ESCAPE:
                    GameState.setPauseGame();
                    break;
            }
        }

    }

    class MouseHandler extends MouseAdapter {

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3)
                mod = (mod + 1) % 2;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (delay == 0) {
                    if (mod == 0 && machineGunCounter != 0) {
                        delay = GameLoop.FPS / 8;
                        PointerInfo a = MouseInfo.getPointerInfo();
                        new MachineGun(Math.toDegrees(Math.atan2(a.getLocation().y - locY - tempShooter.getHeight() / 2, a.getLocation().x - locX - tempShooter.getWidth() / 2)), 12, MainTank.createMainTank());
                        Sounds.getLightGun();
                        machineGunCounter--;
                    } else if (mod == 1 && cannonCounter != 0) {
                        delay = GameLoop.FPS / (2 + Cannon.getDelay());
                        PointerInfo a = MouseInfo.getPointerInfo();
                        new Cannon(Math.toDegrees(Math.atan2(a.getLocation().y - locY - tempShooter.getHeight() / 2, a.getLocation().x - locX - tempShooter.getWidth() / 2)), 12, MainTank.createMainTank());
                        Sounds.getHeavyGun();
                        cannonCounter--;
                    }
                    else if(machineGunCounter == 0 || cannonCounter == 0)
                        Sounds.getEmptyGun();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            AffineTransform at = new AffineTransform();
            at.translate(tempShooter.getWidth() / 2, tempShooter.getHeight() / 2);
            PointerInfo a = MouseInfo.getPointerInfo();
            at.rotate(Math.toRadians(Math.toDegrees(Math.atan2(a.getLocation().y - locY - tempBody.getHeight() / 2, a.getLocation().x - locX - tempBody.getWidth() / 2))));
            at.translate(-tempShooter.getWidth() / 2, -tempShooter.getHeight() / 2);
            AffineTransformOp op;
            BufferedImage newImage;
            op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            newImage = new BufferedImage(tempShooter.getWidth(), tempShooter.getWidth(), tankShooter.getType());
            op.filter(tankShooter, newImage);
            tempShooter = newImage;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }
    }

    @Override
    public void doRendering(Graphics2D g2d) {
        g2d.drawImage(tempBody, locX, locY, null);
        g2d.drawImage(tempShooter, locX + (tempBody.getHeight() - tempShooter.getHeight()) / 2, locY + (tempBody.getHeight() - tempShooter.getHeight()) / 2, null);
        String str = String.format("%3d", machineGunCounter);
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
        g2d.drawImage(machineGunCounterPng, 100, 30, null);
        g2d.drawString(str, 180, 85);
        str = String.format("%3d", cannonCounter);
        g2d.drawImage(cannonCounterPng, 300, 30, null);
        g2d.drawString(str, 370, 85);
        for (int i = 0; i < health; i++) {
            g2d.drawImage(heartPng, Toolkit.getDefaultToolkit().getScreenSize().width - 400 + i * 70, 30, null);
        }
        for(int i = 0; i < shield; i++)
            g2d.drawImage(shieldPng, Toolkit.getDefaultToolkit().getScreenSize().width - 600 + i * 70, 30, null);

    }


    public static MainTank createMainTank() {
        if (instance == null) {
            instance = new MainTank();
        }
        return instance;
    }


    public void delay() {
        if (delay > 0)
            delay--;
    }


    public boolean collision(Rectangle tempMold) {
        for (EnemyTank enemyTank : EnemyTank.enemies) {
            if (tempMold.intersects(enemyTank.mold)) {
                if (enemyTank instanceof Mine) {
                    ((Mine) enemyTank).health = 0;
                    Sounds.getMine();
                    damage(5);
                }
                return true;
            }
        }

        for (Reward reward : Reward.rewards) {
            if (tempMold.intersects(reward.mold)) {
                if((mod == 0 && MachineGun.getUpgrade() < 2) || (mod == 1 && Cannon.getDelay() < 2)) {
                    reward.setReward();
                    Reward.rewards.remove(reward);
                    return true;
                }
            }
        }

        for (Wall wall : Wall.getWalls()) {
            if (tempMold.intersects(wall.mold)) {
                return true;
            }
        }

        return false;
    }

    /**
     * decreases health if tank took damage
     * @param damage damage taken
     */
    @Override
    public void damage(int damage) {
        if(shield > 0)
            shield--;
        else
            health--;
        Sounds.getDestroyed();
        MachineGun.setUpgrade(0);
        Cannon.setDelay(0);
        if(health <= 0){
            Sounds.gameMusic.close();
            GameState.lose = true;
            Sounds.getGameOverSound();
        }
    }


    public int getMod() {
        return mod;
    }


    public void setCannonCounter(int cannonCounter) {
        this.cannonCounter = cannonCounter;
    }


    public void setMachineGunCounter(int machineGunCounter) {
        this.machineGunCounter = machineGunCounter;
    }


    public void fixedTank() {
        health = 5;
    }

    public void setShield(){
        shield = 2;
    }
}
