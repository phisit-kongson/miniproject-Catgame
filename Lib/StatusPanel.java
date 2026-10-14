package Lib;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    private final GameState state = GameState.get();
    private final JLabel coinBox;
    private final JProgressBar energyBar, hungerBar, hygieneBar;
    private final Runnable refresher = this::refresh;

    public StatusPanel() {
        setLayout(null);
        setOpaque(false);
        setBounds(0, 0, 130, 140);

        UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);

        
        coinBox = new JLabel("", SwingConstants.CENTER);
        coinBox.setFont(new Font("", Font.BOLD, 16));
        coinBox.setBounds(15, 15, 100, 35);
        coinBox.setOpaque(true);
        coinBox.setBackground(Color.WHITE);
        coinBox.setForeground(Color.BLACK);
        coinBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(coinBox);

        energyBar  = makeBar(65,  new Color(36, 149, 255));
        hungerBar  = makeBar(90,  new Color(240, 129, 60));
        hygieneBar = makeBar(115, new Color(51, 222, 205));

        refresh();
    }

    private JProgressBar makeBar(int y, Color color) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);
        bar.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 14));
        bar.setForeground(color);
        bar.setBounds(20, y, 100, 15);
        add(bar);
        return bar;
    }

    private void refresh() {
        coinBox.setText("coin : " + state.getCoin());
        energyBar.setValue(state.getEnergy());
        hungerBar.setValue(state.getHunger());
        hygieneBar.setValue(state.getHygiene());
    }

    @Override
    public void addNotify() {
        super.addNotify();
        state.addListener(refresher);
        refresh();
    }

    @Override
    public void removeNotify() {
        state.removeListener(refresher);
        super.removeNotify();
    }
}
