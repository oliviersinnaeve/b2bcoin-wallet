package com.b2beyond.wallet.b2bcoin.view.view.panel;


import javax.swing.border.EmptyBorder;
import java.awt.LayoutManager;

public class AbstractBorderlessJPanel extends AbstractWhitePanel {

    public AbstractBorderlessJPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public AbstractBorderlessJPanel(LayoutManager layout) {
        super(layout);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public AbstractBorderlessJPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public AbstractBorderlessJPanel() {
        super();
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

}