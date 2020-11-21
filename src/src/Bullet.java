import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Bullet {
    protected static ArrayList<Bullet> bullets = new ArrayList<>();
    protected Rectangle mold;
    protected double degree;
    protected int speed;
    protected int power;
    protected BufferedImage bulletPNG;
    protected int time = 3 * GameLoop.FPS;
    protected double locX, locY;
    protected Tank tank;


    public Bullet(double degree, int speed, Tank tank) {
        mold = new Rectangle();
        this.degree = degree;
        this.speed = speed;
        locX = tank.getCenterX();
        locY = tank.getCenterY();
        locX = (locX + (135 / 2) * Math.cos(Math.toRadians(degree)));
        locY = (locY + (135 / 2) * Math.sin(Math.toRadians(degree)));
        this.tank = tank;
    }


    public static void update() {
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).locX = bullets.get(i).locX + (bullets.get(i).speed * Math.cos(Math.toRadians(bullets.get(i).degree)));
            bullets.get(i).locY = bullets.get(i).locY + (bullets.get(i).speed * Math.sin(Math.toRadians(bullets.get(i).degree)));
            bullets.get(i).mold.setLocation((int) bullets.get(i).locX - Tank.currentX, (int) bullets.get(i).locY - Tank.currentY);
            bullets.get(i).decreaseTime();
            if (bullets.get(i).time <= 0 || bullets.get(i).hitCheck()) {
                bullets.remove(i);
                i--;
            }
        }
    }

    public static void doRendering(Graphics2D g2d) {
        for (int i = 0; i < bullets.size(); i++) {
            double rotationRequired = Math.toRadians(bullets.get(i).degree);
            double locationX = bullets.get(i).bulletPNG.getWidth() / 2;
            double locationY = bullets.get(i).bulletPNG.getHeight() / 2;
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);


            if (bullets.get(i).tank instanceof MainTank)
                g2d.drawImage(op.filter(bullets.get(i).bulletPNG, null), (int) bullets.get(i).locX, (int) bullets.get(i).locY, null);
            else
                g2d.drawImage(op.filter(bullets.get(i).bulletPNG, null), (int) bullets.get(i).locX - Tank.currentX, (int) bullets.get(i).locY - Tank.currentY, null);
        }
    }

    public void decreaseTime() {time--;}

    public boolean hitCheck() {
        if (tank instanceof MainTank) {
            Iterator<EnemyTank> tankIterator = EnemyTank.enemies.iterator();
            while (tankIterator.hasNext()) {
                Tank enemy = tankIterator.next();
                Rectangle tempMold = new Rectangle(enemy.mold.x - MainTank.currentX, enemy.mold.y - MainTank.currentY, enemy.mold.width, enemy.mold.height);
                if (this.mold.intersects(tempMold) && !(enemy instanceof Mine)) {
                    enemy.damage(power);
                    return true;
                }
            }
        } else {
            if (this.mold.intersects(MainTank.createMainTank().mold)) {
                MainTank.createMainTank().damage(5);

                return true;
            }
        }
        Iterator<Wall> wallIterator = Wall.getWalls().iterator();
        Rectangle tempMold;
        if(tank instanceof MainTank)
            tempMold = new Rectangle(mold.x + MainTank.currentX, mold.y + MainTank.currentY, mold.width, mold.height);
        else{
            tempMold = new Rectangle(mold.x, mold.y, mold.width, mold.height);
        }
        while (wallIterator.hasNext()) {
            Wall wall = wallIterator.next();
            if (tempMold.intersects(wall.mold)) {
                wall.damage(power);
                return true;
            }
        }

        return false;
    }
}

class MachineGun extends Bullet {
    private static int upgrade = 0;

    public MachineGun(double degree, int speed, Tank tank) {
        super(degree, speed, tank);
        power = 1 + upgrade;
        try {
            bulletPNG = ImageIO.read(new File("pics\\machineGun.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bullets.add(this);
        mold.setSize(bulletPNG.getWidth(), bulletPNG.getHeight());
    }

    public static boolean upgrade(){
        if(upgrade < 2) {
            upgrade++;
            for(Bullet bullet:bullets){
                if(bullet instanceof MachineGun){
                    bullet.power += upgrade;
                }
            }
            return true;
        }
        else
            return false;
    }


    public static int getUpgrade() {
        return upgrade;
    }


    public static void setUpgrade(int upgrade) {
        MachineGun.upgrade = upgrade;
    }
}

class Cannon extends Bullet {
    private static int delay;

    public Cannon(double degree, int speed, Tank tank) {
        super(degree, speed, tank);
        try {
            bulletPNG = ImageIO.read(new File("pics\\cannon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        power = 5;
        delay = 0;
        bullets.add(this);
        mold.setSize(bulletPNG.getWidth(), bulletPNG.getHeight());
    }

    public static boolean upgrade(){
        if(delay < 2) {
            delay++;
            return true;
        }
        else
            return false;
    }

    public static int getDelay() {
        return delay;
    }

    public static void setDelay(int delay) {
        Cannon.delay = delay;
    }
}