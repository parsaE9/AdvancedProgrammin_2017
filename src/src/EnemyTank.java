import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public abstract class EnemyTank extends Tank {


    public EnemyTank(int locX, int locY) {
        enemies.add(this);
        this.locX = locX;
        this.locY = locY;
    }


    @Override
    public void update() {

    }

    public boolean collision(Rectangle tempMold) {
        tempMold.setLocation(tempMold.x, tempMold.y);
        for (EnemyTank enemyTank : EnemyTank.enemies) {
            if (tempMold.intersects(enemyTank.mold) && enemyTank != this && !(enemyTank instanceof Mine))
                return true;
        }
        if (tempMold.intersects(MainTank.createMainTank().mold)) {
            if (this instanceof MovingType2) {
                ((MovingType2) this).health = 0;
                MainTank.createMainTank().damage(5);
            }
            return true;
        }
        for (Wall wall : Wall.getWalls()) {
            if (tempMold.intersects(wall.mold)) {
                return true;
            }
        }
        return false;
    }
}

class MovingType1 extends EnemyTank {
    private double degree;
    private Random rand = new Random();
    private int instanceTime;

    public MovingType1(int x, int y) {
        super(x, y);
        degree = 0;
        health = 5;
        try {
            tankBody = ImageIO.read(new File("pics\\M-6Body.png"));
            tankShooter = ImageIO.read(new File("pics\\M-6Shooter.png"));
            mold = new Rectangle();
            mold.setSize(tankBody.getWidth(), tankBody.getHeight());
            tempBody = tankBody;
            tempShooter = tankShooter;
        } catch (IOException e) {
            e.printStackTrace();
        }
        rotateTime = 90;
        instanceTime = (int) (GameLoop.FPS * (0.5 * (rand.nextInt(100) / 100) + 1));
        side = 2;
        moveSide = 2;
    }

    @Override
    public void update() {
        updateInstanceTime();
        updateShooter();
        if (rotateTime == 90 && moveSide != 0) {
            switch (moveSide) {
                case 4:
                    if (side != 4) {
                        if (collision(tempMold(side)))
                            rotateTime = 0;
                    } else if (!collision(tempMold(side)))
                        locY -= 8;
                    break;
                case 2:
                    if (side != 2) {
                        rotateTime = 0;
                    } else if (!collision(tempMold(side)))
                        locY += 8;
                    break;
                case 3:
                    if (side != 3) {
                        //rotate(3 - side);
                        rotateTime = 0;
                        //side = 3;
                    } else if (!collision(tempMold(side)))
                        locX -= 8;
                    break;
                case 1:
                    if (side != 1) {
                        //  rotate(1 - side);
                        rotateTime = 0;
                        // side = 1;
                    } else if (!collision(tempMold(side)))
                        locX += 8;
                    break;
            }
        } else if (rotateTime != 90) {
            rotateTime += 8;
            if (rotateTime > 82 && rotateTime < 90)
                rotateTime = 90;
            rotate(((double) (moveSide - side) / 90) * rotateTime * Math.PI / 2);
            if (rotateTime == 90) {
                side = moveSide;
                tankBody = tempBody;
                // tankShooter = tempShooter;
            }
        }
        mold.setLocation(locX - MainTank.currentX, locY - MainTank.currentY);
    }

    public void updateInstanceTime() {
        if (instanceTime == 0) {
            instanceTime = (GameLoop.FPS * ((rand.nextInt(100) / 100) + 2));
            moveSide = rand.nextInt(5);
        } else if (rotateTime == 90) {
            instanceTime--;
        }
    }

    public void updateShooter() {
        AffineTransform at = new AffineTransform();
        at.translate(tempShooter.getWidth() / 2, tempShooter.getHeight() / 2);

        MainTank mainTank = MainTank.createMainTank();
        double currentDegree = Math.toDegrees(Math.atan2(mainTank.locY + mainTank.tempBody.getHeight() / 2 - (locY + tempBody.getHeight() / 2 - MainTank.currentY), mainTank.locX + mainTank.tempBody.getWidth() / 2 - ((locX + tempBody.getWidth() / 2) - MainTank.currentX))) - 90;
        if (currentDegree > degree) {
            degree++;
        } else {
            degree--;
        }
        at.rotate(Math.toRadians(degree));
        at.translate(-tempShooter.getWidth() / 2, -tempShooter.getHeight() / 2);
        AffineTransformOp op;
        BufferedImage newImage;
        op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        newImage = new BufferedImage(tempShooter.getWidth(), tempShooter.getWidth(), tankShooter.getType());
        op.filter(tankShooter, newImage);
        tempShooter = newImage;
        if (delay == 0) {
            if(mold.intersects(GameFrame.getFrameRectangle()))
                Sounds.getHeavyGun();
            new Cannon(degree + 90, 12, this);
            delay = GameLoop.FPS + rand.nextInt(GameLoop.FPS);
        } else {
            delay--;
        }
    }
}
class MovingType2 extends EnemyTank {
    private double degree;
    private int speed;


    public MovingType2(int x, int y, int speed) {
        super(x, y);
        degree = 0;
        health = 1;
        this.speed = speed;
        try {
            tankBody = ImageIO.read(new File("pics\\enemyType2.png"));
            mold = new Rectangle();
            mold.setSize(tankBody.getWidth(), tankBody.getHeight());
            tempBody = tankBody;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        MainTank mainTank = MainTank.createMainTank();
        if (!collision(tempMold(new Point((int) (speed * Math.cos(Math.atan2(mainTank.getCenterY() + MainTank.currentY - locY - tankBody.getHeight() / 2, mainTank.getCenterX() + MainTank.currentX - locX - tankBody.getWidth() / 2))), (int) (speed * Math.sin(Math.atan2(mainTank.getCenterY() + MainTank.currentY - locY - tankBody.getHeight() / 2, mainTank.getCenterX() + MainTank.currentX - locX - tankBody.getWidth() / 2))))))) {
            locX = locX + (int) (speed * Math.cos(Math.atan2(mainTank.getCenterY() + MainTank.currentY - locY - tankBody.getHeight() / 2, mainTank.getCenterX() + MainTank.currentX - locX - tankBody.getWidth() / 2)));
            locY = locY + (int) (speed * Math.sin(Math.atan2(mainTank.getCenterY() + MainTank.currentY - locY - tankBody.getHeight() / 2, mainTank.getCenterX() + MainTank.currentX - locX - tankBody.getWidth() / 2)));
        }
        rotate(Math.atan2(mainTank.getCenterY() + MainTank.currentY - locY - tankBody.getHeight() / 2, mainTank.getCenterX() + MainTank.currentX - locX - tankBody.getWidth() / 2));
        mold.setLocation(locX - MainTank.currentX, locY - MainTank.currentY);
    }


    public void doRendering(Graphics2D g2d) {
        g2d.drawImage(tempBody, locX - MainTank.currentX, locY - MainTank.currentY, null);
    }
    public Rectangle tempMold(Point point) {
        Rectangle tempMold = new Rectangle(mold.x, mold.y, mold.width, mold.height);
        tempMold.setLocation(tempMold.x + point.x, tempMold.y + point.y);
        return tempMold;
    }
}


class NonMovingTank extends EnemyTank {
    private double degree;
    private Random rand = new Random();


    public NonMovingTank(int locX, int locY) {
        super(locX, locY);
        degree = 0;
        health = 5;
        try {
            tankBody = ImageIO.read(new File("pics\\nonMovingTank.png"));
            tankShooter = ImageIO.read(new File("pics\\nonMovingShooter.png"));
            mold = new Rectangle();
            mold.setSize(tankBody.getWidth(), tankBody.getHeight());
            tempBody = tankBody;
            tempShooter = tankShooter;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void update() {
        updateShooter();
        mold.setLocation(locX - MainTank.currentX, locY - MainTank.currentY);
    }

    public void updateShooter() {
        AffineTransform at = new AffineTransform();
        at.translate(tempShooter.getWidth() / 2, tempShooter.getHeight() / 2);

        MainTank mainTank = MainTank.createMainTank();
        double currentDegree = Math.toDegrees(Math.atan2(mainTank.locY + mainTank.tempBody.getHeight() / 2 - (locY + tempBody.getHeight() / 2 - MainTank.currentY), mainTank.locX + mainTank.tempBody.getWidth() / 2 - ((locX + tempBody.getWidth() / 2) - MainTank.currentX))) - 90;
        if (currentDegree > degree + 8) {
            degree += 8;
        } else if(currentDegree + 8 < degree) {
            degree -= 8;
        }
        at.rotate(Math.toRadians(degree));
        at.translate(-tempShooter.getWidth() / 2, -tempShooter.getHeight() / 2);
        AffineTransformOp op;
        BufferedImage newImage;
        op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        newImage = new BufferedImage(tempShooter.getWidth(), tempShooter.getWidth(), tankShooter.getType());
        op.filter(tankShooter, newImage);
        tempShooter = newImage;
        if (delay == 0) {
            if(mold.intersects(GameFrame.getFrameRectangle()))
                Sounds.getHeavyGun();
            new Cannon(degree + 90, 6, this);
            delay = GameLoop.FPS + rand.nextInt(GameLoop.FPS);
        } else {
            delay--;
        }
    }
}

class Mine extends EnemyTank {

    public Mine(int x, int y) {
        super(x + 75, y + 75);
        health = 1;
        try {
            tankBody = ImageIO.read(new File("pics\\mine.png"));
            mold = new Rectangle();
            mold.setSize(tankBody.getWidth(), tankBody.getHeight());
            mold.setLocation(locX, locY);
            tempBody = tankBody;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        mold.setLocation(locX - currentX, locY - currentY);
    }


    public void doRendering(Graphics2D g2d) {
        g2d.drawImage(tempBody, locX - MainTank.currentX, locY - MainTank.currentY, null);
    }
}