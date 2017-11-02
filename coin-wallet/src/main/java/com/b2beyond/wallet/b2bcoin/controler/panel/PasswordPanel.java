package com.b2beyond.wallet.b2bcoin.controler.panel;

import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import com.b2beyond.wallet.b2bcoin.util.FileResourceExtractor;
import org.apache.log4j.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class PasswordPanel extends JPanel {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private JTextField passwordField;

    /**
     * Create the panel.
     */
    public PasswordPanel(String wallet) {
        setBorder(new EmptyBorder(10,10,10,10));
        setPreferredSize(new Dimension(300, 150));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 1 };
        gridBagLayout.rowHeights = new int[] { 1, 1, 1, 1 };
        gridBagLayout.columnWeights = new double[] { 1 };
        gridBagLayout.rowWeights = new double[]{ 0.02, 0.1, 0.02, 0.1};
        setLayout(gridBagLayout);

        //Create an array of the text and components to be displayed.
        String msgString1 = "<html><body>Enter wallet password"
                + "<br><br>" +
                "Wallet : " + wallet + "<br><br></body></html>";

        passwordField = new JPasswordField();

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
        add(passwordField, gbc);

    }

    public JTextField getPasswordField() {
        return passwordField;
    }
}
