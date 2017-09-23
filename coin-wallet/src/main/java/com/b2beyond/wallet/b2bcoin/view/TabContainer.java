package com.b2beyond.wallet.b2bcoin.view;

import com.b2beyond.wallet.b2bcoin.view.controller.ViewController;

import javax.swing.ImageIcon;
import javax.swing.JComponent;


public class TabContainer<T, C extends JComponent> {

    private int index;
    private boolean enabled;
    private String name;
    private JComponent view;
    private ImageIcon icon;
    private ViewController<T, C> controller;

    public TabContainer(int index, String name, JComponent view, boolean enabled) {
        this.index = index;
        this.name = name;
        this.enabled = enabled;
        this.view = view;
    }

    public TabContainer(int index, String name, JComponent view, boolean enabled, ImageIcon icon) {
        this.index = index;
        this.name = name;
        this.enabled = enabled;
        this.view = view;
        this.icon = icon;
    }

    public TabContainer(String name, ViewController<T, C> controller) {
        this.name = name;
        this.controller = controller;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public JComponent getView() {
        return view;
    }

    public void setView(JComponent view) {
        this.view = view;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public ViewController<T, C> getController() {
        return controller;
    }

    public void setController(ViewController<T, C> controller) {
        this.controller = controller;
    }
}

