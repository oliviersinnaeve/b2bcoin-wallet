package com.b2beyond.wallet.b2bcoin.view.view;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Address;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.AddressBalance;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.util.CoinUtil;
import com.b2beyond.wallet.b2bcoin.view.controller.AddressesController;
import org.apache.log4j.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;


public class AddressesTabView extends JPanel implements Observer {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private AddressesController controller;

    private JTable addressesTable;
    private DefaultTableModel addressesTableModel;

    private JLabel totalAmountLabel;
    private JLabel totalAmountLockedLabel;


    String[] columnNames = { "Address", "Available", "Locked" };


    public AddressesTabView(AddressesController controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());

        addressesTableModel = new DefaultTableModel(columnNames, 0);
        addressesTable = new JTable(addressesTableModel);
        addressesTable.getColumnModel().getColumn(0).setPreferredWidth(700);
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

//        JButton createNewAddress = new JButton("Create Address");
//        createNewAddress.addActionListener(this);

        totalAmountLabel = new JLabel("Loading ...");
        totalAmountLockedLabel = new JLabel("Loading ...");
        JPanel amountPanel = new JPanel();
        amountPanel.add(totalAmountLabel);
        amountPanel.add(totalAmountLockedLabel);
//        amountPanel.add(createNewAddress);

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
                    Object[] rowData = {address, CoinUtil.getTextForLong(addressBalance.getAvailableBalance()), CoinUtil.getTextForLong(addressBalance.getLockedAmount())};
                    addressesTableModel.addRow(rowData);
                }
            }

            totalAmountLabel.setText(CoinUtil.getTextForLong(fullAmount));
            totalAmountLockedLabel.setText(CoinUtil.getTextForLong(fullLockedAmount));
        }
    }

    public void addAddress(Address address) {
        Object[] data = {address.getAddress()};
        addressesTableModel.addRow(data);
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        String command = e.getActionCommand();
//
//        if (command.equals("Create Address")) {
//            Address address = controller.createAddress();
//            addAddress(address);
//        }
//    }

    public void setTotalBalance(long balance) {
        LOGGER.info("Updating total balance label");
        totalAmountLabel.setText(CoinUtil.getTextForLong(balance));
    }

    public void setTotalLockedBalance(long balance) {
        LOGGER.info("Updating total locked balance label");
        totalAmountLockedLabel.setText(CoinUtil.getTextForLong(balance));
    }

}
