import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Tank {

    protected int locX, locY;
    protected Rectangle mold;
    protected int side;
    protected int moveSide;
    protected int rotateTime;
    protected int delay;
    protected BufferedImage tankBody;
    protected BufferedImage tempBody;
    protected BufferedImage tankShooter;
    protected BufferedImage tempShooter;
    public static ArrayList<EnemyTank> enemies = new ArrayList<>();
    public static int currentX, currentY;
    protected int health;
    protected int shield = 0;


    public Tank() {

    }

    public void rotate(double bodyAngel) {
        AffineTransform at = new AffineTransform();

        at.translate(tankBody.getWidth() / 2, tankBody.getHeight() / 2);
        at.rotate(bodyAngel);
        at.translate(-tankBody.getWidth() / 2, -tankBody.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage newImage = new BufferedImage(tankBody.getWidth(), tankBody.getWidth(), tankBody.getType());
        op.filter(tankBody, newImage);
        tempBody = newImage;

    }

    public void doRendering(Graphics2D g2d) {
        g2d.drawImage(tempBody, locX - MainTank.currentX, locY - MainTank.currentY, null);
        g2d.drawImage(tempShooter, locX - MainTank.currentX + (tempBody.getHeight() - tempShooter.getHeight()) / 2, locY - MainTank.currentY + (tempBody.getHeight() - tempShooter.getHeight()) / 2, null);
    }


    public abstract void update();

    public int getCenterX() {
        return locX + tempBody.getWidth() / 2;
    }


    public int getCenterY() {
        return locY + tempBody.getHeight() / 2;
    }

    public void damage(int damage) {
        health -= damage;
        Sounds.getDestroyed();
    }


    public int getHealthy() {
        return health;
    }

    public Rectangle tempMold(int side) {
        Rectangle tempMold = new Rectangle(mold.x, mold.y, mold.width, mold.height);
        switch (side) {
            case 1:
                tempMold.setLocation(tempMold.x + 8, tempMold.y);
                break;
            case 2:
                tempMold.setLocation(tempMold.x, tempMold.y + 8);
                break;
            case 3:
                tempMold.setLocation(tempMold.x - 8, tempMold.y);
                break;
            case 4:
                tempMold.setLocation(tempMold.x, tempMold.y - 8);
                break;
        }
        return tempMold;
    }
}
