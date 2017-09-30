package com.b2beyond.wallet.b2bcoin.view.view;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Address;
import com.b2beyond.wallet.b2bcoin.view.controller.ActionController;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


public class MenuBar extends JMenuBar {

    ActionController actionController;

    public MenuBar(ActionController controller) {
        this.actionController = controller;
        ImageIcon icon = new ImageIcon("exit.png");

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit", icon);
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                System.exit(0);
            }
        });

        JMenu wallet = new JMenu("Wallet");
        file.setMnemonic(KeyEvent.VK_W);

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


//        JMenuItem importSimpleWallet = new JMenuItem("Import simple wallet", icon);
//        eMenuItem.setMnemonic(KeyEvent.VK_I);
//        eMenuItem.setToolTipText("Import simple wallet data");
//        eMenuItem.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent ev) {
////                actionController.showImportScreen();
//            }
//        });


        file.add(eMenuItem);
        wallet.add(createNewAddressMenuItem);

        this.add(file);
        this.add(wallet);
    }

}
