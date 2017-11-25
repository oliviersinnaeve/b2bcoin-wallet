package com.b2beyond.wallet.b2bcoin.util;

import javax.swing.JButton;
import java.awt.Dimension;

/**
 * Created by oliviersinnaeve on 25/11/17.
 */
public class ComponentFactory {

    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(B2BUtil.mainColor);
        button.setPreferredSize(new Dimension(300, 30));

        return button;
    }

    public static JButton createSubButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(B2BUtil.splashTextColor);
        button.setPreferredSize(new Dimension(300, 30));

        return button;
    }


}
