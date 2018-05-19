package com.b2beyond.wallet.b2bcoin.view.view.panel;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChangeViewSizePanel extends JPanel {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private JTextField viewWidth;
    private JTextField viewHeight;

    /**
     * Create the panel.
     */
    public ChangeViewSizePanel(int width, int height) {

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(300, 200));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{1, 1, 1, 1, 1};
        gridBagLayout.rowHeights = new int[]{1, 1, 1, 1, 1};
        gridBagLayout.columnWeights = new double[]{0.01, 0.25, 0.2, 0.70, 0.01};
        gridBagLayout.rowWeights = new double[]{0.02, 0.25, 0.02, 0.25, 0.02};
        setLayout(gridBagLayout);


        //Create an array of the text and components to be displayed.
        String msgString1 = "<html><body>Change the minimum size of the gui</body></html>";

        viewWidth = new JTextField();
        viewWidth.setText(String.valueOf(width));
        viewHeight = new JTextField();
        viewHeight.setText(String.valueOf(height));

        String msgString2 = "Width  :";
        String msgString3 = "Height : ";

        JLabel helpMessageLabel = new JLabel(msgString1);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(helpMessageLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(new JLabel(msgString2), gbc);
        gbc.gridwidth = 3;
        gbc.gridx = 3;
        gbc.gridy = 3;
        add(viewWidth, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        add(new JLabel(msgString3), gbc);
        gbc.gridx = 3;
        gbc.gridy = 5;
        add(viewHeight, gbc);
    }

    public JTextField getViewWidth() {
        return viewWidth;
    }

    public JTextField getViewHeight() {
        return viewHeight;
    }
}
