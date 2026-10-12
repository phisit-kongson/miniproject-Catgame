package Lib;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

/**
 * ค่าสถานะกลางของเกม (coin, energy, hunger, hygiene)
 * ทุกห้องเรียกใช้ตัวเดียวกันผ่าน GameState.get() เลยเห็นค่าตรงกันเสมอ
 * ทำงานบน Swing thread (Timer ของ javax.swing) จึงไม่ต้องล็อกอะไรเพิ่ม
 */
public class GameState {

    private static final GameState INSTANCE = new GameState();

    public static GameState get() {
        return INSTANCE;
    }

    public static final int MAX = 100;

    private int coin;
    private int energy;
    private int hunger;
    private int hygiene;

    private final List<Runnable> listeners = new ArrayList<>();
    private final Timer decayTimer;

    private GameState() {
        reset();
        // ลดทุก 5 วิ
        decayTimer = new Timer(5000, e -> {
            energy  = clamp(energy  - 2);
            hunger  = clamp(hunger  - 3);
            hygiene = clamp(hygiene - 2);
            fireChanged();
        });
    }

    public int getCoin()    { return coin; }
    public int getEnergy()  { return energy; }
    public int getHunger()  { return hunger; }
    public int getHygiene() { return hygiene; }

    public void addCoin(int n)    { coin    = Math.max(0, coin + n); fireChanged(); }
    public void addEnergy(int n)  { energy  = clamp(energy  + n);    fireChanged(); }
    public void addHunger(int n)  { hunger  = clamp(hunger  + n);    fireChanged(); }
    public void addHygiene(int n) { hygiene = clamp(hygiene + n);    fireChanged(); }

    // จ่ายเหรียญ ถ้าเหรียญไม่พอจะไม่หักและคืน false 
    public boolean trySpend(int cost) {
        if (cost > coin) return false;
        coin -= cost;
        fireChanged();
        return true;
    }

    // กลับเป็นค่าเริ่มต้น 
    public void reset() {
        coin = 100;
        energy = 0;
        hunger = 80;
        hygiene = 0;
        fireChanged();
    }

    // ตัวลดค่าอัตโนมัติ 
    public void startDecay() {
        if (!decayTimer.isRunning()) decayTimer.start();
    }

    public void stopDecay() {
        decayTimer.stop();
    }

    // ตัวฟังการเปลี่ยนแปลง  StatusPanel ใช้
    public void addListener(Runnable r) {
        if (!listeners.contains(r)) listeners.add(r);
    }

    public void removeListener(Runnable r) {
        listeners.remove(r);
    }

    private void fireChanged() {
        for (Runnable r : new ArrayList<>(listeners)) r.run();
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(MAX, v));
    }
}