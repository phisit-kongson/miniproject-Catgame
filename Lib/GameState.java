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
    private final java.util.HashMap<String, Integer> inventory = new java.util.HashMap<>();
    private final java.util.HashMap<String, Long> lastRewardTimes = new java.util.HashMap<>();
    private static final long COOLDOWN_MS = 30000; // 30 วินาที 
    
    private int feedCount = 0;
    private int playCount = 0;
    private int shopBuyCount = 0;
    

    private boolean feedClaimed = false;
    private boolean playClaimed = false;
    private boolean shopClaimed = false;
    
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

    // เมธอดเพิ่มไอเทมเข้ากระเป๋า
    public void addItem(String itemKey, int amount) {
        int current = inventory.getOrDefault(itemKey, 0);
        inventory.put(itemKey, current + amount);
        fireChanged();
    }
    public int getItemCount(String itemKey) {
        return inventory.getOrDefault(itemKey, 0);
    }

    public boolean useItem(String itemKey) {
        int current = getItemCount(itemKey);
        if (current <= 0) return false;      // ไม่มีของ
        inventory.put(itemKey, current - 1);
        fireChanged();
    return true;
    }

    // กลับเป็นค่าเริ่มต้น 
    public void reset() {
        coin = 100;
        energy = 0;
        hunger = 80;
        hygiene = 0;
        inventory.put("fish", 0);
        inventory.put("milk", 0);
        inventory.put("kibble", 0);
        inventory.put("snack cat", 0);
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
    public boolean tryClaimReward(String activityKey, int rewardCoin) {
        long now = System.currentTimeMillis();
        long lastTime = lastRewardTimes.getOrDefault(activityKey, 0L);

        // ถ้าเวลาปัจจุบันห่างจากรอบก่อนเกิน 30 วินาที
        if (now - lastTime >= COOLDOWN_MS) {
            addCoin(rewardCoin);                  // บวกเหรียญและอัปเดตหน้าจออัตโนมัติ
            lastRewardTimes.put(activityKey, now); // บันทึกเวลาที่ได้รับล่าสุด
            return true;
        }
        return false; // ยังติด Cooldown ไม่แจกเหรียญ
    }

    /**
     * คำนวณเวลา Cooldown ที่เหลือ (วินาที) ไว้แจ้งเตือนผู้เล่น
     */
    public long getRemainingCooldown(String activityKey) {
        long now = System.currentTimeMillis();
        long lastTime = lastRewardTimes.getOrDefault(activityKey, 0L);
        long diff = now - lastTime;
        if (diff >= COOLDOWN_MS) return 0;
        return (COOLDOWN_MS - diff) / 1000;
    }

        public int getFeedCount() {
        return feedCount;
    }
    public void recordFeedCount() {
        feedCount++;
        fireChanged();
    }

    public int getPlayCount() {
        return playCount;
    }
    public void recordPlayCount() {
        playCount++;
        fireChanged();
    }

    public int getShopBuyCount() { 
        return shopBuyCount;
    }
    public void recordShopBuy() { 
        recordShopBuy_internal();
    }
    private void recordShopBuy_internal() { 
        shopBuyCount++; 
        fireChanged(); 
    }

    public boolean doFeedClaimed(){
        return feedClaimed;
    }
    public void setFeedClaimed(boolean feedClaimed){
        this.feedClaimed = feedClaimed;
        fireChanged();
    }
    public boolean doPlayClaimed(){
        return playClaimed;
    }
    public void setPlayClaimed(boolean playClaimed){
        this.playClaimed = playClaimed;
        fireChanged();
    }

    public boolean doShopClaimed(){
        return shopClaimed;
    }
    public void setShopClaimed(boolean shopClaimed){
        this.shopClaimed = shopClaimed;
        fireChanged();
    }
}