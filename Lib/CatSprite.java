package Lib;

import javax.swing.*;
import java.awt.*;

public class CatSprite extends JLabel {

    private final int size;
    private final ImageIcon[] idleFrames;     // ยืน
    private ImageIcon[] happyFrames;           // มีความสุข
    private ImageIcon[] annoyedFrames;          // โกรธ
    private ImageIcon[] bathFrames;             // ถูสบู่
    private ImageIcon[] rinseFrames;            // ล้างตัว
    private ImageIcon[] waterFrames;            // ท่ารำคาญน้ำ 
    private ImageIcon[] currentFrames;          
    private ImageIcon[] sleepFrames;            // นอน
    private ImageIcon[] eatFrames;
    private ImageIcon[] vomitFrames;            //อ้วก              
    private final int idleDelay;     
    private int happyDelay ; 
    private int annoyedDelay ;
    private int rinseDelay ;
    private int waterDelay ;
    private int sleepDelay ;
    private int eatDelay ;
    private int vomitDelay ;
    private boolean playingOnce = false;    // กดเล่นเเมว
    private boolean holding = false;      // ค้างเฟรม
    private boolean sleeping = false;      
    private Runnable onFinished;          // ทำต่อหลังรันอนิเมชันจบ
    private int index = 0;
    private final Timer timer;

    private int clickCount = 0;
    private long firstClickTime = 0;
    private static final int CLICK_LIMIT = 3;        // คลิกกี่ครั้งถึงจะรำคาญ
    private static final long CLICK_WINDOW = 5000;   // ภายในกี่ ms 1000 = 1วิ

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

    }public void setAnnoyedAnimation(String prefix, int frameCount, int delayMs) {
        annoyedFrames = loadFrames(prefix, frameCount);
        annoyedDelay = delayMs;
    }

    // ห้องนอน
    public void setSleepAnimation(String prefix, int frameCount, int delayMs) {
        sleepFrames = loadFrames(prefix, frameCount);
        sleepDelay = delayMs;
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public void sleep() {
        if (sleepFrames == null || playingOnce || sleeping) return;
        sleeping = true;
        currentFrames = sleepFrames;
        index = 0;
        setIcon(currentFrames[0]);
        setSpeed(sleepDelay);
    }

    public void wake() {
        if (!sleeping) return;
        sleeping = false;
        currentFrames = idleFrames;
        index = 0;
        setIcon(currentFrames[0]);
        setSpeed(idleDelay);
    }
    // ในห้องน้ำ
    public void setBathFrames(String prefix, int frameCount) {
        bathFrames = loadFrames(prefix, frameCount);
    }

    public void setRinseAnimation(String prefix, int frameCount, int delayMs) {
        rinseFrames = loadFrames(prefix, frameCount);
        rinseDelay = delayMs;
    }

    // อนิเมชันรอบเดียวไม่รับคลิกเพิ่ม
    public boolean isBusy() {
        return playingOnce;
    }

    // ค้างเฟรมท่าฟอง
    public void showBathLevel(int level) {
        if (bathFrames == null || playingOnce) return;
        holding = true;
        setIcon(bathFrames[Math.max(0, Math.min(level, bathFrames.length - 1))]);
    }

    public void playRinse(Runnable onDone) {
        holding = false;
        if (rinseFrames == null || playingOnce) {
            if (onDone != null) onDone.run();
            return;
        }
        onFinished = onDone;
        playOnce(rinseFrames, rinseDelay);
    }

    public void setWaterAnimation(String prefix, int frameCount, int delayMs) {
        waterFrames = loadFrames(prefix, frameCount);
        waterDelay = delayMs;
    }

    
    public void playWaterAnnoyed() {
        playOnce(waterFrames, waterDelay);
    }

    // ให้อาหาร
    public void setEatAnimation(String prefix, int frameCount, int delayMs) {
        eatFrames = loadFrames(prefix, frameCount);
        eatDelay = delayMs;
    }

    public void playEat(Runnable onDone) {
        if (eatFrames == null || playingOnce) {
            if (onDone != null) onDone.run();
            return;
        }
        onFinished = onDone;
        playOnce(eatFrames, eatDelay);
    }
    public void setVomitAnimation(String prefix, int frameCount, int delayMs) {
        vomitFrames = loadFrames(prefix, frameCount);
        vomitDelay = delayMs;
    }

    public void playVomit(Runnable onDone) {
        if (vomitFrames == null || playingOnce) {
            if (onDone != null) onDone.run();
            return;
        }
        onFinished = onDone;
        playOnce(vomitFrames, vomitDelay);
    }

    public void playHappy() {
        playOnce(happyFrames, happyDelay);
    }

    private void setSpeed(int ms) {
    timer.setDelay(ms);
    timer.setInitialDelay(ms);
    timer.restart();
    }

    public void onClicked() {
        if (playingOnce) return;   
        long now = System.currentTimeMillis();
        if (clickCount == 0 || now - firstClickTime > CLICK_WINDOW) {
            clickCount = 1;
            firstClickTime = now;
        } else {
            clickCount++;
        }

        if (clickCount >= CLICK_LIMIT && annoyedFrames != null) {
            clickCount = 0;
            playOnce(annoyedFrames, annoyedDelay);
        } else {
            playOnce(happyFrames, happyDelay);
        }
    }

    private void playOnce(ImageIcon[] frames, int delay) {
        if (frames == null || playingOnce) return;
        playingOnce = true;
        currentFrames = frames;
        index = 0;
        setIcon(currentFrames[0]);
        setSpeed(delay);
    }

    private void nextFrame() {
        if (holding) return;                      
        index++;
        Runnable done = null;
        if (index >= currentFrames.length) {
            if (playingOnce) {
                playingOnce = false;
                currentFrames = idleFrames;
                setSpeed(idleDelay);
                done = onFinished;
                onFinished = null;
            }
            index = 0;
        }
        setIcon(currentFrames[index]);
        if (done != null) done.run();             
    }

    public void start() { timer.start(); }
    public void stop()  { timer.stop(); }
}