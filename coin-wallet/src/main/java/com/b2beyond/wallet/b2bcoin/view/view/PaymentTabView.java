package com.b2beyond.wallet.b2bcoin.view.view;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Transaction;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.TransactionItem;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.TransactionItems;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Transfer;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import com.b2beyond.wallet.b2bcoin.util.CoinUtil;
import com.b2beyond.wallet.b2bcoin.util.DateUtil;
import com.b2beyond.wallet.b2bcoin.view.model.JComboboxItem;
import com.b2beyond.wallet.b2bcoin.view.view.panel.AbstractAddressJPanel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;


public class PaymentTabView extends AbstractAddressJPanel implements Observer {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private JTable paymentsTable;
    private DefaultTableModel paymentsTableModel;


    private Addresses addressesList;


    public PaymentTabView() {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        this.setLayout(new BorderLayout());

        //construct components
        setAddresses(new JComboBox<JComboboxItem>());

        String[] columnNames = {"Address", "Date", "Amount"};
        paymentsTableModel = new DefaultTableModel(columnNames, 0);
        paymentsTable = new JTable(paymentsTableModel);
        paymentsTable.getColumnModel().getColumn(0).setPreferredWidth(675);
        paymentsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        paymentsTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        paymentsTable.setRowHeight(30);

        JScrollPane transactionsTableScrollPane = new JScrollPane(paymentsTable);
        transactionsTableScrollPane.setVisible(true);
        transactionsTableScrollPane.setColumnHeader(new JViewport() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 32;
                return d;
            }
        });

        JPanel paymentsTablePanel = new JPanel();
        paymentsTablePanel.setLayout(new BoxLayout(paymentsTablePanel, 1));
        paymentsTablePanel.add(paymentsTable.getTableHeader(), BorderLayout.NORTH);
        paymentsTablePanel.add(transactionsTableScrollPane, BorderLayout.CENTER);

        this.add(addresses, BorderLayout.NORTH);
        this.add(paymentsTablePanel, BorderLayout.CENTER);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof Addresses) {
            if (!data.equals(addressesList)) {
                addressesList = (Addresses) data;
                update(addressesList);
            }
        }
        if (data instanceof TransactionItems) {
            TransactionItems transactionItems = (TransactionItems) data;
            updateBalances(transactionItems);
            setFilterOnTable(paymentsTable, paymentsTableModel);
        }
    }

    private void updateBalances(TransactionItems transactionItems) {
        for (TransactionItem item : transactionItems.getItems()) {
            for (Transaction transaction : item.getTransactions()) {
                String dateStr = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT).format(new Date(transaction.getTimestamp() * 1000));
//                Date date = null;
//                try {
//                    date = DateUtil.parse(dateStr);
//                } catch (ParseException e) {
//                    LOGGER.error("Could not parse data", e);
//                }

                long amount = transaction.getAmount();

                if (transaction.getUnlockTime() != 0) {
                    LOGGER.trace("Unlock time : " + transaction.getUnlockTime());
                }

                if (amount < 0) {
                    String address = "";
                    for (Transfer transfer : transaction.getTransfers()) {
                        if (StringUtils.isNotBlank(transfer.getAddress())) {
                            if (addressesList.getAddresses().contains(transfer.getAddress())) {
                                address = transfer.getAddress();
                            }
                        }
                    }

                    final Object[] data = {address, dateStr, amount};
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (paymentsTable != null) {
                                paymentsTableModel.addRow(data);
                            }
                        }
                    });
                }
            }
        }
    }

}
