package Lib;

import javax.swing.*;
import java.awt.*;

public class CatSprite extends JLabel {

    private final int size;
    private final ImageIcon[] idleFrames;     
    private ImageIcon[] happyFrames;          
    private ImageIcon[] currentFrames;
    private final int idleDelay;     
    private int happyDelay = 150;        
    private boolean playingOnce = false;
    private int index = 0;
    private final Timer timer;

    public CatSprite(String prefix, int frameCount, int size, int delayMs) {
        this.size = size;
        this.idleDelay = delayMs;
        idleFrames = loadFrames(prefix, frameCount);
        currentFrames = idleFrames;
        setIcon(currentFrames[0]);
        setSize(size, size);

        timer = new Timer(delayMs, e -> nextFrame());
    }

    private ImageIcon[] loadFrames(String prefix, int count) {
        ImageIcon[] result = new ImageIcon[count];
        for (int i = 0; i < count; i++) {
            Image img = new ImageIcon(
                getClass().getResource("/pic/cat/" + prefix + (i + 1) + ".png")).getImage();
            result[i] = new ImageIcon(img.getScaledInstance(size, size, Image.SCALE_REPLICATE));
        }
        return result;
    }

    public void setHappyAnimation(String prefix, int frameCount, int delayMs) {
        happyFrames = loadFrames(prefix, frameCount);
        happyDelay = delayMs;
    }

    private void setSpeed(int ms) {
    timer.setDelay(ms);
    timer.setInitialDelay(ms);
    timer.restart();
    }

    public void playHappy() {
        if (happyFrames == null || playingOnce) return;  
        playingOnce = true;
        currentFrames = happyFrames;
        index = 0;
        setIcon(currentFrames[0]);   
        setSpeed(happyDelay);           
    }

    private void nextFrame() {
        index++;
        if (index >= currentFrames.length) {      
            if (playingOnce) {                    
                playingOnce = false;
                currentFrames = idleFrames;
                setSpeed(idleDelay);
            }
            index = 0;                            
        }
        setIcon(currentFrames[index]);
    }

    public void start() { timer.start(); }
    public void stop()  { timer.stop(); }
}