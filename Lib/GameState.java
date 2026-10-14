package Lib;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.Timer;

public class GameState {

    private static final GameState INSTANCE = new GameState();
    private final java.util.HashMap<String, Integer> inventory = new java.util.HashMap<>();
    private final java.util.HashMap<String, Long> lastRewardTimes = new java.util.HashMap<>();
    private static final long COOLDOWN_MS = 30000;

    private static class UserQuestData {
        int feedCount = 0;
        int playCount = 0;
        int shopBuyCount = 0;

        boolean feedClaimed = false;
        boolean playClaimed = false;
        boolean shopClaimed = false;

        long feedClaimTime = 0;
        long playClaimTime = 0;
        long shopClaimTime = 0;
    }

    private static final String FILE_PATH = "./File/users.csv";
    private final HashMap<String, UserQuestData> userQuests = new HashMap<>();
    private String currentUsername = null;

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
        // ลดทุก 6 วิ
        decayTimer = new Timer(6000, e -> {
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

    public boolean trySpend(int cost) {
        if (cost > coin) return false;
        coin -= cost;
        fireChanged();
        return true;
    }

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
        if (current <= 0) return false;
        inventory.put(itemKey, current - 1);
        fireChanged();
        return true;
    }

    // กลับเป็นค่าเริ่มต้น
    public void reset() {
        coin = 50;
        energy = 60;
        hunger = 60;
        hygiene = 50;
        inventory.put("fish", 0);
        inventory.put("milk", 0);
        inventory.put("kibble", 0);
        inventory.put("snack cat", 0);
        fireChanged();
    }

    public void loadUser(String username) {
        this.currentUsername = null;
        reset();
        lastRewardTimes.clear();

        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine(); // ข้าม Header
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] arr = line.split(",");

                    if (arr.length > 2 && arr[1].trim().equals(username)) {
                        if (arr.length >= 11) {
                            try {
                                coin    = Math.max(0, Integer.parseInt(arr[3].trim()));
                                energy  = clamp(Integer.parseInt(arr[4].trim()));
                                hunger  = clamp(Integer.parseInt(arr[5].trim()));
                                hygiene = clamp(Integer.parseInt(arr[6].trim()));
                                inventory.put("fish",      Math.max(0, Integer.parseInt(arr[7].trim())));
                                inventory.put("milk",      Math.max(0, Integer.parseInt(arr[8].trim())));
                                inventory.put("kibble",    Math.max(0, Integer.parseInt(arr[9].trim())));
                                inventory.put("snack cat", Math.max(0, Integer.parseInt(arr[10].trim())));
                            } catch (NumberFormatException ignored) {}
                        }
                        break;
                    }
                }
            } catch (IOException ex) {
                System.err.println("Cannot load user data: " + ex.getMessage());
            }
        }

        this.currentUsername = username;

        String key = (username != null && !username.trim().isEmpty()) ? username.trim() : "default";
        if (!userQuests.containsKey(key)) {
            userQuests.put(key, new UserQuestData());
        }

        fireChanged();
    }

    private UserQuestData getQuestData() {
        String key = (currentUsername == null || currentUsername.trim().isEmpty()) ? "default" : currentUsername.trim();

        UserQuestData data = userQuests.get(key);
        if (data == null) {
            data = new UserQuestData();
            userQuests.put(key, data);
        }
        return data;
    }

    public void saveUser() {
        if (currentUsername == null || currentUsername.isEmpty()) return;

        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (isFirstLine) {
                    lines.add(line);
                    isFirstLine = false;
                    continue;
                }

                String[] arr = line.split(",");
                if (arr.length > 2 && arr[1].trim().equals(currentUsername)) {
                    String updatedLine = arr[0].trim() + ","
                            + arr[1].trim() + ","
                            + arr[2].trim() + ","
                            + coin + ","
                            + energy + ","
                            + hunger + ","
                            + hygiene + ","
                            + getItemCount("fish") + ","
                            + getItemCount("milk") + ","
                            + getItemCount("kibble") + ","
                            + getItemCount("snack cat");
                    lines.add(updatedLine);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException ex) {
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Cannot save user data: " + ex.getMessage());
        }
    }

    public void startDecay() {
        if (!decayTimer.isRunning()) decayTimer.start();
    }

    public void stopDecay() {
        decayTimer.stop();
        saveUser();
    }

    public void addListener(Runnable r) {
        if (!listeners.contains(r)) listeners.add(r);
    }

    public void removeListener(Runnable r) {
        listeners.remove(r);
    }

    private void fireChanged() {
        saveUser();
        for (Runnable r : new ArrayList<>(listeners)) r.run();
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(MAX, v));
    }

    public boolean tryClaimReward(String activityKey, int rewardCoin) {
        long now = System.currentTimeMillis();
        long lastTime = lastRewardTimes.getOrDefault(activityKey, 0L);

        if (now - lastTime >= COOLDOWN_MS) {
            addCoin(rewardCoin);
            lastRewardTimes.put(activityKey, now);
            return true;
        }
        return false;
    }

    public long getRemainingCooldown(String activityKey) {
        long now = System.currentTimeMillis();
        long lastTime = lastRewardTimes.getOrDefault(activityKey, 0L);
        long diff = now - lastTime;
        if (diff >= COOLDOWN_MS) return 0;
        return (COOLDOWN_MS - diff) / 1000;
    }

    // Quest 
    private static final long questCD = 28800000; // 8 ชม.
    public long getQuestCDRemaining(String questKey) {
        UserQuestData d = getQuestData();
        boolean claimed;
        long last;
        switch (questKey) {
            case "feed": claimed = d.feedClaimed; last = d.feedClaimTime; break;
            case "play": claimed = d.playClaimed; last = d.playClaimTime; break;
            case "shop": claimed = d.shopClaimed; last = d.shopClaimTime; break;
            default: return 0;
        }
        if (!claimed) return 0;
        long left = questCD - (System.currentTimeMillis() - last);
        return left <= 0 ? 0 : (left + 999) / 1000;
    }

    public boolean refreshQuests() {
        UserQuestData d = getQuestData();
        boolean changed = false;
        if (d.feedClaimed && getQuestCDRemaining("feed") == 0) {
            d.feedClaimed = false; d.feedCount = 0; changed = true;
        }
        if (d.playClaimed && getQuestCDRemaining("play") == 0) {
            d.playClaimed = false; d.playCount = 0; changed = true;
        }
        if (d.shopClaimed && getQuestCDRemaining("shop") == 0) {
            d.shopClaimed = false; d.shopBuyCount = 0; changed = true;
        }
        if (changed) fireChanged();
        return changed;
    }

    public int getFeedCount() {
        return getQuestData().feedCount;
    }
    public void recordFeedCount() {
        if (getQuestData().feedClaimed) return;   
        getQuestData().feedCount++;
        fireChanged();
    }

    public int getPlayCount() {
        return getQuestData().playCount;
    }
    public void recordPlayCount() {
        if (getQuestData().playClaimed) return;
        getQuestData().playCount++;
        fireChanged();
    }

    public int getShopBuyCount() {
        return getQuestData().shopBuyCount;
    }
    public void recordShopBuy() {
        if (getQuestData().shopClaimed) return;
        getQuestData().shopBuyCount++;
        fireChanged();
    }

    public boolean doFeedClaimed() {
        return getQuestData().feedClaimed;
    }
    public void setFeedClaimed(boolean v) {
        UserQuestData d = getQuestData();
        d.feedClaimed = v;
        if (v) d.feedClaimTime = System.currentTimeMillis();
        fireChanged();
    }

    public boolean doPlayClaimed() {
        return getQuestData().playClaimed;
    }
    public void setPlayClaimed(boolean v) {
        UserQuestData d = getQuestData();
        d.playClaimed = v;
        if (v) d.playClaimTime = System.currentTimeMillis();
        fireChanged();
    }

    public boolean doShopClaimed() {
        return getQuestData().shopClaimed;
    }
    public void setShopClaimed(boolean v) {
        UserQuestData d = getQuestData();
        d.shopClaimed = v;
        if (v) d.shopClaimTime = System.currentTimeMillis();
        fireChanged();
    }
}