package com.b2beyond.wallet.b2bcoin.view.view.panel;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.LayoutManager;


public class AbstractWhitePanel extends JPanel {

    public AbstractWhitePanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        setBackground(Color.WHITE);
    }

    public AbstractWhitePanel(LayoutManager layout) {
        super(layout);
        setBackground(Color.WHITE);
    }

    public AbstractWhitePanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        setBackground(Color.WHITE);
    }

    public AbstractWhitePanel() {
        setBackground(Color.WHITE);
    }
}
