import javax.sound.sampled.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Sounds {

    private static AudioInputStream gameSound;
    private static AudioInputStream heavyGun;
    private static AudioInputStream lightGun;
    private static AudioInputStream destroyed;
    private static AudioInputStream mine;
    private static AudioInputStream emptyGun;
    private static AudioInputStream repair;
    private static AudioInputStream gameOver;
    public  static Clip gameMusic;
    private static Clip heavyGunSound;
    private static Clip lightGunSound;
    private static Clip destroyedSound;
    private static Clip mineSound;
    private static Clip emptyGunSound;
    private static Clip repairSound;
    private static Clip gameOverSound;

    public Sounds() {
        try {
            gameSound = AudioSystem.getAudioInputStream(new File("Sounds\\gameSound.wav"));
            heavyGun = AudioSystem.getAudioInputStream(new File("Sounds\\heavyGun.wav"));
            lightGun = AudioSystem.getAudioInputStream(new File("Sounds\\lightgun.wav"));
            destroyed = AudioSystem.getAudioInputStream(new File("Sounds\\destroyed.wav"));
            mine = AudioSystem.getAudioInputStream(new File("Sounds\\mine.wav"));
            emptyGun = AudioSystem.getAudioInputStream(new File("Sounds\\emptyGun.wav"));
            repair = AudioSystem.getAudioInputStream(new File("Sounds\\repair.wav"));
            gameOver = AudioSystem.getAudioInputStream(new File("Sounds\\gameOver.wav"));
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        try {
            heavyGunSound = AudioSystem.getClip();
            heavyGunSound.open(heavyGun);
            gameMusic = AudioSystem.getClip();
            gameMusic.open(gameSound);
            lightGunSound = AudioSystem.getClip();
            lightGunSound.open(lightGun);
            destroyedSound = AudioSystem.getClip();
            destroyedSound.open(destroyed);
            mineSound = AudioSystem.getClip();
            mineSound.open(mine);
            emptyGunSound = AudioSystem.getClip();
            emptyGunSound.open(emptyGun);
            repairSound = AudioSystem.getClip();
            repairSound.open(repair);
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(gameOver);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void getGameSound() {
        gameMusic.start();
        gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
        //  gameMusic.setFramePosition(0);
    }


    public static void getHeavyGun() {
        heavyGunSound.start();
        heavyGunSound.setFramePosition(0);
    }


    public static void getLightGun() {
        lightGunSound.start();
        lightGunSound.setFramePosition(0);
    }


    public static void getMine() {
        mineSound.start();
        mineSound.setFramePosition(0);
    }

    public static void getDestroyed() {
        destroyedSound.start();
        destroyedSound.setFramePosition(0);
    }

    public static void getEmptyGun(){
        emptyGunSound.start();
        emptyGunSound.setFramePosition(0);
    }


    public static void getRepair(){
        repairSound.start();
        repairSound.setFramePosition(0);
    }


    public static void getGameOverSound(){
        gameOverSound.start();
        gameOverSound.setFramePosition(0);
    }
}
