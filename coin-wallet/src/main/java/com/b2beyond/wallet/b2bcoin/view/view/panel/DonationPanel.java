package com.b2beyond.wallet.b2bcoin.view.view.panel;

import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import com.b2beyond.wallet.b2bcoin.util.ComponentFactory;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DonationPanel extends AbstractBorderlessJPanel {

    /**
     * Create the panel.
     */
    public DonationPanel() {
        setBackground(B2BUtil.mainColor);
        setToolTipText("This panel gives you some wallet you can donate to. The is purely made on private funds.");
//        setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
//        Border border = getBorder();
//        Border margin = new EmptyBorder(10,10,10,10);
//        setBorder(new CompoundBorder(border, margin));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{1, 1, 1, 1, 1};
        gridBagLayout.rowHeights = new int[]{1, 1, 1, 1, 1};
        gridBagLayout.columnWeights = new double[]{0.02, 0.84, 0.02, 0,1, 0.02};
        gridBagLayout.rowWeights = new double[]{0.02, 0.48, 0.02, 0.48, 0.02};
        setLayout(gridBagLayout);

        JLabel btcDonationAddressLabel = new JLabel("Donation - BTC Address : 1E8eqLxrR9GtkZBNW2hwL6MjLBhCiZdoqS");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(btcDonationAddressLabel, gbc);

        JButton copyBtcAddress = ComponentFactory.createSubButton("Copy address");
        copyBtcAddress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String myString = "1E8eqLxrR9GtkZBNW2hwL6MjLBhCiZdoqS";
                StringSelection stringSelection = new StringSelection(myString);
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        });

        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.gridx = 3;
        gbc.gridy = 1;
        add(copyBtcAddress, gbc);

        JLabel litecoinDonationAddressLabel = new JLabel("Donation - LTC Address : LTxYayXRs9Cm6rjfJKFh862GF7DgXecgx8");
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(litecoinDonationAddressLabel, gbc);

        JButton copyLtcAddress = ComponentFactory.createSubButton("Copy address");
        copyLtcAddress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String myString = "LTxYayXRs9Cm6rjfJKFh862GF7DgXecgx8";
                StringSelection stringSelection = new StringSelection(myString);
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        });

        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.gridx = 3;
        gbc.gridy = 3;
        add(copyLtcAddress, gbc);
    }

}
