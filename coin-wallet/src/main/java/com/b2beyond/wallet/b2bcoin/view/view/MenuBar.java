package com.b2beyond.wallet.b2bcoin.view.view;

import com.b2beyond.wallet.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.rpc.model.Address;
import com.b2beyond.wallet.rpc.model.Addresses;
import com.b2beyond.wallet.rpc.model.SpendKeys;
import com.b2beyond.wallet.rpc.exception.KnownJsonRpcException;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import com.b2beyond.wallet.b2bcoin.view.controller.ActionController;
import com.b2beyond.wallet.b2bcoin.view.view.panel.AboutPanel;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;


public class MenuBar extends JMenuBar {

    ActionController actionController;

    public MenuBar(final JFrame mainFrame, final PropertiesConfiguration walletProperties, ActionController controller) {
        this.actionController = controller;
        ImageIcon icon = new ImageIcon("exit.png");

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem exitMenuItem = new JMenuItem("Exit", icon);
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.setToolTipText("Exit application");
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                System.exit(0);
            }
        });

        JMenu wallet = new JMenu("Wallet");
        JMenu help = new JMenu("Help");

        // New address sub menu
        JMenuItem createNewAddressMenuItem = new JMenuItem("Create new address", icon);
        createNewAddressMenuItem.setToolTipText("Create new address");
        createNewAddressMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Address address = actionController.createAddress();
                JOptionPane.showMessageDialog(null,
                        address.getAddress(),
                        "New address - check address view",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Reset wallet sub menu
        JMenuItem resetWalletMenuItem = new JMenuItem("Reset wallet", icon);
        resetWalletMenuItem.setToolTipText("Reset wallet");
        resetWalletMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                actionController.resetWallet();
            }
        });

        // Backup wallet sub menu
        JMenuItem backupWalletMenuItem = new JMenuItem("Backup wallet", icon);
        backupWalletMenuItem.setToolTipText("Backup wallet");
        backupWalletMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                System.setProperty("apple.awt.fileDialogForDirectories", "true");

                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fc.showOpenDialog(mainFrame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    B2BUtil.backupWallet(file.getAbsolutePath(), walletProperties.getString("container-file"), null);

                    JOptionPane.showMessageDialog(null,
                            "You wallet has been backed up to directory : " + file.getAbsolutePath(),
                            "Address backed up",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Get spend keys sub menu
        JMenuItem spendKeysMenuItem = new JMenuItem("Spend keys", icon);
        spendKeysMenuItem.setToolTipText("Get your send keys");
        spendKeysMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Addresses addresses  = actionController.getWalletRpcController().getAddressesExecutor().execute(JsonRpcExecutor.EMPTY_PARAMS);

                    String html = "<html><body>";
                    for (String address: addresses.getAddresses()) {
                        SpendKeys keys = actionController.getSpendKeys(address);

                        html += "<h3>" + address + "</h3>";
                        html += "<textarea rows='2' cols='75'  style='overflow:auto'>" +
                                "Spend public key : " + keys.getSpendPublicKey() +
                                "&#13;&#10;" +
                                "Spend secret key : " + keys.getSpendSecretKey() +
                                "</textarea>";
                    }
                    html += "</body></html>";

                    JOptionPane.showMessageDialog(
                            mainFrame,
                            html,
                            "Overview of spending keys",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (KnownJsonRpcException e) {
                    e.printStackTrace();
                }
            }
        });

        // Help sub menu
        JMenuItem aboutMenuItem = new JMenuItem("About the wallet");
        aboutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JOptionPane.showMessageDialog(null,
                    new AboutPanel(),
                    "About B2B Coin wallet",
                    JOptionPane.INFORMATION_MESSAGE,
                    B2BUtil.getIcon());
            }
        });

        file.add(exitMenuItem);

        wallet.add(backupWalletMenuItem);
        wallet.add(spendKeysMenuItem);
        wallet.add(resetWalletMenuItem);
        wallet.add(createNewAddressMenuItem);

        help.add(aboutMenuItem);

        this.add(file);
        this.add(wallet);
        this.add(help);
    }



}
