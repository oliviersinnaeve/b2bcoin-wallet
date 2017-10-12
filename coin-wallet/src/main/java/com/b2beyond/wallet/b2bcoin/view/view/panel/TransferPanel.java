package com.b2beyond.wallet.b2bcoin.view.view.panel;

import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import com.b2beyond.wallet.b2bcoin.view.view.CreatePaymentTabView;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class TransferPanel extends JPanel {

    private CreatePaymentTabView parent;

    private JTextField address;
    private JTextField amount;

    private JButton deletePanelButton;

    /**
     * Create the panel.
     */
    public TransferPanel(boolean first, CreatePaymentTabView parent) {
        this.parent = parent;

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

        JLabel lblBalance = new JLabel("Address :");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(lblBalance, gbc);

        address = new JTextField();
        address.setColumns(75);
        gbc.gridx = 3;
        gbc.gridy = 1;
        add(address, gbc);

        JLabel lblLockedBalance = new JLabel("Amount :");
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(lblLockedBalance, gbc);


        NumberFormat amountFormat = NumberFormat.getNumberInstance();
        amountFormat.setMinimumFractionDigits(12);
        amountFormat.setMaximumFractionDigits(12);
        amount = new JFormattedTextField(amountFormat);
        amount.setColumns(35);
        gbc.gridx = 3;
        gbc.gridy = 3;
        add(amount, gbc);

        if (!first) {
            deletePanelButton = new JButton("Remove transfer");

            gbc.anchor = GridBagConstraints.NORTHEAST;
            gbc.gridx = 4;
            gbc.gridy = 3;
            add(deletePanelButton, gbc);
        }
    }

    public void setActionListeners() {
        deletePanelButton.addActionListener(new DeletePanelListener(parent, this));
    }

    public JTextField getAddress() {
        return address;
    }

    public JTextField getAmount() {
        return amount;
    }
}

class DeletePanelListener implements ActionListener {

    private CreatePaymentTabView parent;
    private TransferPanel panel;

    public DeletePanelListener(CreatePaymentTabView parent, TransferPanel panel) {
        this.parent = parent;
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.getParent().remove(panel);
        parent.removePanel(panel);

        parent.repaint();
    }

}
