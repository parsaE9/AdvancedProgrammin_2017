import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Reward {
    protected int locX,locY;
    protected BufferedImage img;
    protected Rectangle mold;
    public static ArrayList<Reward> rewards = new ArrayList<>();


    public Reward(int x,int y){
        locX = x;
        locY = y;
        rewards.add(this);
    }


    public void update(){
        mold.setLocation(locX - MainTank.currentX ,locY - MainTank.currentY);
    }


    public void doRendering(Graphics2D g2d){
        g2d.drawImage(img, locX - MainTank.currentX, locY - MainTank.currentY, null);
    }


    public abstract void setReward();
}


class Upgrade extends Reward{

    public Upgrade(int x, int y) {
        super(x, y);
        try {
            img = ImageIO.read(new File("pics\\upgrade.png"));
            mold = new Rectangle(img.getWidth(),img.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setReward() {
        if(MainTank.createMainTank().getMod() == 0){
            MachineGun.upgrade();
        }else
            Cannon.upgrade();
    }
}

class RefillMachineGun extends Reward{

    public RefillMachineGun(int x, int y) {
        super(x, y);
        try {
            img = ImageIO.read(new File("pics\\machineGunRefill.png"));
            mold = new Rectangle(img.getWidth(),img.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setReward() {
        MainTank.createMainTank().setMachineGunCounter(200);
    }
}

class RefillCannon extends Reward{

    public RefillCannon(int x, int y) {
        super(x, y);
        try {
            img = ImageIO.read(new File("pics\\cannonRefill.png"));
            mold = new Rectangle(img.getWidth(),img.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setReward() {
        MainTank.createMainTank().setCannonCounter(50);
    }
}

class FixedTank extends Reward{

    public FixedTank(int x, int y) {
        super(x, y);
        try {
            img = ImageIO.read(new File("pics\\fixedTank.png"));
            mold = new Rectangle(img.getWidth(),img.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setReward() {
        Sounds.getRepair();
        MainTank.createMainTank().fixedTank();
    }
}
class EndGame extends Reward{

    public EndGame(int x, int y) {
        super(x, y);
        try {
            img = ImageIO.read(new File("pics\\end.png"));
            mold = new Rectangle(img.getWidth(),img.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void setReward() {
        Sounds.gameMusic.close();
        Sounds.getGameOverSound();
        GameState.win = true;
    }
}

class Shield extends Reward{

    public Shield(int x, int y) {
        super(x, y);
        try {
            img = ImageIO.read(new File("pics\\shield.png"));
            mold = new Rectangle(img.getWidth(),img.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setReward() {
        Sounds.getRepair();
        MainTank.createMainTank().setShield();
    }
}
