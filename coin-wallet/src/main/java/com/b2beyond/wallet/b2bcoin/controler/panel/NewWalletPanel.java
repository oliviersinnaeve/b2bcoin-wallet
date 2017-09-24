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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NewWalletPanel extends JPanel {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private JTextField walletNameField;
    private JTextField passwordField;

    /**
     * Create the panel.
     */
    public NewWalletPanel() {
        final String userHome = B2BUtil.getUserHome();
        setBorder(new EmptyBorder(10,10,10,10));
        setPreferredSize(new Dimension(600, 400));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 1, 1, 1, 1, 1 };
        gridBagLayout.rowHeights = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
        gridBagLayout.columnWeights = new double[] { 0.01, 0.25, 0.2, 0.70, 0.01  };
        gridBagLayout.rowWeights = new double[]{ 0.02, 0.25, 0.02, 0.15, 0.02, 0.10, 0.02, 0.10, 0.02, 0.10, 0.02};
        setLayout(gridBagLayout);

        JRadioButton newWalletButton = new JRadioButton("New wallet");
        newWalletButton.setActionCommand("New wallet");
        newWalletButton.setSelected(true);
        JRadioButton importWalletButton = new JRadioButton("Import simple wallet");
        importWalletButton.setActionCommand("Import simple wallet");

        ButtonGroup group = new ButtonGroup();
        group.add(newWalletButton);
        group.add(importWalletButton);

        //Create an array of the text and components to be displayed.
        String msgString1 = "<html><body>This is the first time you access the wallet :"
                + "<br><br>" +
                "1. please enter the name and password for your new wallet."
                + "<br>" +
                "2. or choose your simple wallet address file"
                + "<br><br></body></html>";

        walletNameField = new JTextField(50);
        passwordField = new JPasswordField();
        final JButton button = new JButton("Select file ...");
        button.setEnabled(false);
        final JFileChooser fileChooser = new JFileChooser();
        String msgString2 = "1. Wallet Name :";
        String msgString3 = "2. Select simple wallet : ";
        String msgString4 = "Password :";

        JLabel helpMessageLabel = new JLabel(msgString1);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(helpMessageLabel, gbc);

        JPanel radioPanel = new JPanel();
        radioPanel.add(newWalletButton);
        radioPanel.add(importWalletButton);
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(radioPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        add(new JLabel(msgString2), gbc);
        gbc.gridx = 3;
        gbc.gridy = 5;
        add(walletNameField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        add(new JLabel(msgString3), gbc);
        gbc.gridx = 3;
        gbc.gridy = 7;
        add(button, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        add(new JLabel(msgString4), gbc);
        gbc.gridx = 3;
        gbc.gridy = 9;
        add(passwordField, gbc);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    LOGGER.info("Opening: " + file.getName());

                    if (file.getAbsolutePath().equalsIgnoreCase(userHome + file.getName())) {
                        LOGGER.info("File already located in b2bcoin home folder");
                    } else {
                        LOGGER.info("Copy wallet file to b2bcoin home folder from " + file.getAbsolutePath());
                        try {
                            FileResourceExtractor.copyFromFileSystem(file.getAbsolutePath(), userHome + file.getName());
//                            long fileLength = Files.copy(Paths.get(file.toURI()), new FileOutputStream());
                            LOGGER.debug("File copied");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                    walletNameField.setText(file.getName());
                } else {
                    LOGGER.info("Open command cancelled by user.");
                }
            }
        });

        newWalletButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                walletNameField.setEnabled(true);
                button.setEnabled(false);
            }
        });

        importWalletButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                walletNameField.setEnabled(false);
                button.setEnabled(true);
            }
        });
    }

    public JTextField getWalletNameField() {
        return walletNameField;
    }

    public JTextField getPasswordField() {
        return passwordField;
    }
}
