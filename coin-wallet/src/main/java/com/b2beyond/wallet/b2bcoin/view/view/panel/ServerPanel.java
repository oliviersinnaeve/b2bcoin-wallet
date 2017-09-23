package com.b2beyond.wallet.b2bcoin.view.view.panel;

import com.b2beyond.wallet.b2bcoin.util.B2BUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;

public class ServerPanel extends JPanel {

    private JLabel peers;
    private JLabel lastBlockHash;

    /**
     * Create the panel.
     */
    public ServerPanel() {
        setBackground(B2BUtil.mainColor);
        setToolTipText("This panel gives you your spendable balance and your locked balance. The locked balance needs 10 blocks to be confirmed.");
        setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
        Border border = getBorder();
        Border margin = new EmptyBorder(10,10,10,10);
        setBorder(new CompoundBorder(border, margin));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{1, 1, 1, 1};
        gridBagLayout.rowHeights = new int[]{1, 1, 1, 1};
        gridBagLayout.columnWeights = new double[]{0.02, 0.245, 0.03, 0.75};
        gridBagLayout.rowWeights = new double[]{0.02, 0.48, 0.02, 0.48};
        setLayout(gridBagLayout);

        JLabel lblPeers = new JLabel("Number of peers :");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(lblPeers, gbc);

        peers = new JLabel("Loading ...");
        gbc.gridx = 3;
        gbc.gridy = 1;
        add(peers, gbc);

        JLabel lblLastBlockHash = new JLabel("Last block hash :");
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(lblLastBlockHash, gbc);

        lastBlockHash = new JLabel("Loading ...");
        gbc.gridx = 3;
        gbc.gridy = 3;
        add(lastBlockHash, gbc);
    }

    public JLabel getPeers() {
        return peers;
    }

    public JLabel getLastBlockHash() {
        return lastBlockHash;
    }
}
