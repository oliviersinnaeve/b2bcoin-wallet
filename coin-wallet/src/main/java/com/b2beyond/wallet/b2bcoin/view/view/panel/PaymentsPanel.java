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

public class PaymentsPanel extends AbstractBorderlessJPanel {

    private JLabel numberOfPayments;
    private JLabel totalPaymentsAmount;
    private JLabel totalPaymentsLockedAmount;


    /**
     * Create the panel.
     */
    public PaymentsPanel() {
        setName("paymentsPanel");
        setForeground(SystemColor.textInactiveText);
        setBackground(B2BUtil.mainColor);
        setToolTipText("This panel gives you your spendable balance and your locked balance. The locked balance needs 10 blocks to be confirmed.");
//        setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
//        Border border = getBorder();
//        Border margin = new EmptyBorder(10,10,10,10);
//        setBorder(new CompoundBorder(border, margin));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{1, 1, 1, 1};
        gridBagLayout.rowHeights = new int[]{1, 1, 1, 1, 1, 1};
        gridBagLayout.columnWeights = new double[]{0.02, 0.245, 0.03, 0.75};
        gridBagLayout.rowWeights = new double[]{0.02, 0.48, 0.02, 0.48, 0.02, 0.48};
        setLayout(gridBagLayout);

        JLabel lblBalance = new JLabel("Number of payments :");
        lblBalance.setForeground(SystemColor.activeCaptionText);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(lblBalance, gbc);

        numberOfPayments = new JLabel("Loading ...");
        numberOfPayments.setName("numberOfPayments");
        gbc.gridx = 3;
        gbc.gridy = 1;
        add(numberOfPayments, gbc);

        JLabel lblLockedBalance = new JLabel("Total amount of payments :");
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(lblLockedBalance, gbc);

        totalPaymentsAmount = new JLabel("Loading ...");
        totalPaymentsAmount.setName("totalPaymentsAmount");
        gbc.gridx = 3;
        gbc.gridy = 3;
        add(totalPaymentsAmount, gbc);

        JLabel lblUnconfirmedLockedBalance = new JLabel("Total unconfirmed payments :");
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(lblUnconfirmedLockedBalance, gbc);

        totalPaymentsLockedAmount = new JLabel("Loading ...");
        totalPaymentsLockedAmount.setName("totalPaymentsLockedAmount");
        gbc.gridx = 3;
        gbc.gridy = 5;
        add(totalPaymentsLockedAmount, gbc);
    }

    public JLabel getNumberOfPayments() {
        return numberOfPayments;
    }

    public JLabel getTotalPaymentsAmount() {
        return totalPaymentsAmount;
    }

    public JLabel getTotalPaymentsLockedAmount() {
        return totalPaymentsLockedAmount;
    }
}



