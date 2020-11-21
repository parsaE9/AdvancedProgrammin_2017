import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Png {

    private BufferedImage softwall;
    private BufferedImage hardwall;
    private BufferedImage damagedwall;
    private HashMap<String,BufferedImage> hash = new HashMap<>();

    public Png() {
        try {
            softwall = ImageIO.read(new File("pics\\softWall.png"));
            hash.put("softwall",softwall);
            damagedwall = ImageIO.read(new File("pics\\softWallDamaged.png"));
            hash.put("damagedwall",damagedwall);
            hardwall = ImageIO.read(new File("pics\\hardWall.png"));
            hash.put("hardwall",hardwall);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getPng(String type){
        return hash.get(type);
    }
}
