package com.b2beyond.wallet.b2bcoin.view.view;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.AddressBalance;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Success;
import com.b2beyond.wallet.b2bcoin.util.CoinUtil;
import com.b2beyond.wallet.b2bcoin.view.controller.ActionController;
import com.b2beyond.wallet.b2bcoin.view.view.panel.AbstractBorderlessJPanel;
import com.b2beyond.wallet.b2bcoin.view.view.table.ButtonColumn;
import org.apache.log4j.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;


public class AddressesTabView extends AbstractBorderlessJPanel implements Observer {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private ActionController controller;

    private JTable addressesTable;
    private DefaultTableModel addressesTableModel;

    private JLabel totalAmountLabel;
    private JLabel totalAmountLockedLabel;


    String[] columnNames = { "Address", "Available", "Locked", "" };


    public AddressesTabView(ActionController controller) {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        this.controller = controller;
        this.setLayout(new BorderLayout());

        addressesTableModel = new DefaultTableModel(columnNames, 0);
        addressesTable = new JTable(addressesTableModel);
        addressesTable.getColumnModel().getColumn(0).setPreferredWidth(700);
        addressesTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        addressesTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        addressesTable.setRowHeight(30);

        JScrollPane addressesTableScrollPane = new JScrollPane(addressesTable);
        addressesTableScrollPane.setVisible(true);
        addressesTableScrollPane.setColumnHeader(new JViewport() {
            @Override public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 32;
                return d;
            }
        });


        totalAmountLabel = new JLabel("Loading ...");
        totalAmountLabel.setBackground(Color.white);
        totalAmountLockedLabel = new JLabel("Loading ...");
        totalAmountLockedLabel.setBackground(Color.white);
        JPanel amountPanel = new JPanel();
        amountPanel.add(totalAmountLabel);
        amountPanel.add(totalAmountLockedLabel);

        JTableHeader header = addressesTable.getTableHeader();
        this.add(header, BorderLayout.NORTH);
        this.add(addressesTableScrollPane, BorderLayout.CENTER);
        this.add(amountPanel, BorderLayout.SOUTH);

        setTotalBalance(0);
        setTotalLockedBalance(0);
    }

    @Override
    public void update(Observable observable, Object data) {
        addressesTableModel.setRowCount(0);

        Addresses addresses;
        if (data instanceof Addresses) {
            addresses = (Addresses) data;

            long fullAmount = 0;
            long fullLockedAmount = 0;

            for (String address: addresses.getAddresses()) {
                AddressBalance addressBalance = controller.getBalance(address);

                if (addressBalance != null) {
                    fullAmount += addressBalance.getAvailableBalance();
                    fullLockedAmount += addressBalance.getLockedAmount();

                    Action delete = new AbstractAction() {
                        public void actionPerformed(ActionEvent e)
                        {
                            JTable table = (JTable)e.getSource();
                            int modelRow = Integer.valueOf( e.getActionCommand() );
                            String address = (String)table.getModel().getValueAt(modelRow, 0);
                            AddressBalance balance = controller.getBalance(address);
                            if (balance != null && balance.getAvailableBalance() == 0 && balance.getLockedAmount() == 0) {
                                Success success = controller.deleteAddress(address);
                                JOptionPane.showMessageDialog(null,
                                        "The address has been deleted with response : " + success.getStatus(),
                                        "Address deleted",
                                        JOptionPane.INFORMATION_MESSAGE);
                                ((DefaultTableModel)table.getModel()).removeRow(modelRow);
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "There are still funds on the address, transfer them first.",
                                        "Address deletion error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    };

                    Object[] rowData = {
                            address,
                            CoinUtil.getTextForLong(addressBalance.getAvailableBalance()), CoinUtil.getTextForLong(addressBalance.getLockedAmount()),
                            "Delete"
                    };
                    addressesTableModel.addRow(rowData);

                    new ButtonColumn(addressesTable, delete, 3);
                }
            }

            totalAmountLabel.setText(CoinUtil.getTextForLong(fullAmount));
            totalAmountLockedLabel.setText(CoinUtil.getTextForLong(fullLockedAmount));
        }
    }

    public void setTotalBalance(long balance) {
        LOGGER.info("Updating total balance label");
        totalAmountLabel.setText(CoinUtil.getTextForLong(balance));
    }

    public void setTotalLockedBalance(long balance) {
        LOGGER.info("Updating total locked balance label");
        totalAmountLockedLabel.setText(CoinUtil.getTextForLong(balance));
    }

}
