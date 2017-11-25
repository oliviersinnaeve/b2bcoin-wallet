package com.b2beyond.wallet.b2bcoin.view.view.panel;


import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AbstractBorderlessJPanel extends JPanel {

    public AbstractBorderlessJPanel() {
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

}
