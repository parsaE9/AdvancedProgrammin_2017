import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static jdk.nashorn.internal.objects.NativeMath.random;

public abstract class Wall implements Serializable {
    public static ArrayList<Wall> walls = new ArrayList<>();
    protected int locX,locY;
    protected int health;
    protected String type;
    protected Rectangle mold;
    private static Png png = new Png();

    public Wall(int x,int y){
        locX = x;
        locY = y;
        health = 10;
        walls.add(this);
    }

    public void update(){
        mold.setLocation(locX - MainTank.currentX , locY - MainTank.currentY);
    }

    public static ArrayList<Wall> getWalls() {
        return walls;
    }


    public static void doRendering(Graphics2D g2d) {
        for (int i = 0; i < walls.size(); i++) {
            if(GameFrame.getFrameRectangle().intersects(walls.get(i).mold))
                g2d.drawImage(png.getPng(walls.get(i).type), walls.get(i).locX - MainTank.currentX, walls.get(i).locY - MainTank.currentY, null);
        }
    }

    public abstract void damage(int power);
}

class HardWall extends Wall{
    public HardWall(int x,int y){
        super(x,y);
        mold = new Rectangle();
        mold.setLocation(x,y);
        mold.setSize(200,200);
        type = "hardwall";
    }

    @Override
    public void damage(int power) {

    }
}

class SoftWall extends Wall{
    public SoftWall(int x,int y){
        super(x,y);
        mold = new Rectangle();
        mold.setLocation(x,y);
        mold.setSize(200,200);
        type = "softwall";
    }

    @Override
    public void damage(int power) {
        health -= power;
        if(health <= 5){
            type = "softwall";
        }
        if(health <= 0){
            getWalls().remove(this);
        }
    }

}

class AwardWall extends Wall{
    public AwardWall(int x,int y){
        super(x,y);
        mold = new Rectangle();
        mold.setLocation(x,y);
        mold.setSize(200,200);
        type = "softwall";
    }

    @Override
    public void damage(int power) {
        health -= power;
        if(health <= 5){
            type = "damagedwall";
        }
        if(health <= 0){
            getWalls().remove(this);
            Random random = new Random();
            int r = random.nextInt(3 + 1);
            if(r == 0)
                new Upgrade(locX,locY);
            else if(r == 1)
                new FixedTank(locX, locY);
            else if( r == 2)
                new RefillCannon(locX, locY);
            else if(r == 3)
                new RefillMachineGun(locX, locY);
        }
    }

}
