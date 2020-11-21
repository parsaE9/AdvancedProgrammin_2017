import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class Menu {

    public static String mapAddress = "";
    private JFrame menuFrame;
    private ArrayList<JButton> menuBtns,difficultyBtns;
    private Container container;


    public void createMenu(){
        menuFrame = new JFrame("Normal Tanks");
        menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuFrame.setSize(GameFrame.GAME_WIDTH, GameFrame.GAME_HEIGHT);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setBackground(Color.BLACK);

        container = new Container();

        menuBtns = new ArrayList<>();
        menuBtns.add(new JButton("ONE PLAYER      "));
        menuBtns.add(new JButton("TWO PLAYER      "));
        menuBtns.add(new JButton("EDIT MAP ONE    "));
        menuBtns.add(new JButton("EDIT MAP TWO    "));

        difficultyBtns = new ArrayList<>();
        difficultyBtns.add(new JButton("EASY      "));
        difficultyBtns.add(new JButton("MEDIUM    "));
        difficultyBtns.add(new JButton("HARD      "));
        difficultyBtns.add(new JButton("EDITED MAP"));

        Action action = new Action();

        int height = 300;

        for(JButton btn : menuBtns){
            height += 80;
            btn.setSize(250,60);
            btn.setLocation(150,height);
            btn.setBackground(Color.GRAY);
            btn.setBorderPainted(false);
            btn.setFont(new Font("", Font.ITALIC, 20));
            btn.revalidate();
            container.add(btn);
            btn.addActionListener(action);
        }

        height = 300;

        for(JButton btn : difficultyBtns){
            height += 80;
            btn.setSize(250,60);
            btn.setLocation(150,height);
            btn.setBackground(Color.GRAY);
            btn.setBorderPainted(false);
            btn.setFont(new Font("", Font.ITALIC, 20));
            btn.revalidate();
            btn.addActionListener(action);
        }


        JLabel normalTanks = new JLabel(getImage("pics\\normaltanks.png",1000,200));
        normalTanks.setSize(1000, 300);
        normalTanks.setLocation(400, 20);
        normalTanks.setBackground(Color.BLACK);
        normalTanks.setOpaque(true);

        JLabel tank = new JLabel(getImage("pics\\tank.png",600,400));
        tank.setSize(600,500);
        tank.setLocation(600,300);
        tank.setBackground(Color.BLACK);
        tank.setOpaque(true);


        container.add(normalTanks);
        container.add(tank);
        menuFrame.setContentPane(container);
        menuFrame.setVisible(true);
    }

    private void nextMenu(){
        for(JButton btn : menuBtns)
            container.remove(btn);

        for(JButton btn : difficultyBtns)
            container.add(btn);
    }

    private ImageIcon getImage(String address,int width,int height){
        ImageIcon icon = new ImageIcon(address);
        Image image = icon.getImage();
        Image newimage = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimage);
        return icon;
    }


    private class Action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(menuBtns.get(0))){
                nextMenu();
            }
            if(e.getSource().equals(menuBtns.get(1))){
                nextMenu();
            }
            if(e.getSource().equals(menuBtns.get(2))){
                //open map1edited.txt
                String s = "rundll32 url.dll,FileProtocolHandler " + "maps\\map1edited.txt";
                Process p = null;
                try {p = Runtime.getRuntime().exec(s);} catch (IOException e1) {e1.printStackTrace();}
                try {p.waitFor();} catch (InterruptedException e1) {e1.printStackTrace();}
            }
            if(e.getSource().equals(menuBtns.get(3))){
                //open map2.txt
                String s = "rundll32 url.dll,FileProtocolHandler " + "maps\\map2.txt";
                Process p = null;
                try {p = Runtime.getRuntime().exec(s);} catch (IOException e1) {e1.printStackTrace();}
                try {p.waitFor();} catch (InterruptedException e1) {e1.printStackTrace();}

            }
            if(e.getSource().equals(difficultyBtns.get(0))){
                mapAddress = "maps\\map1easy.txt";
                menuFrame.dispose();
            }
            if(e.getSource().equals(difficultyBtns.get(1))){
                mapAddress = "maps\\map1medium.txt";
                menuFrame.dispose();
            }
            if(e.getSource().equals(difficultyBtns.get(2))){
                mapAddress = "maps\\map1hard.txt";
                menuFrame.dispose();
            }
            if(e.getSource().equals(difficultyBtns.get(3))){
                mapAddress = "maps\\map1edited.txt";
                menuFrame.dispose();

            }

            menuFrame.revalidate();
            menuFrame.validate();
            menuFrame.repaint();
        }
    }
}