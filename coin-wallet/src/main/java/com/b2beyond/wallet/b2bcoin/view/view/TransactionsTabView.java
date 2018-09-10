package com.b2beyond.wallet.b2bcoin.view.view;

import com.b2beyond.wallet.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.rpc.model.Addresses;
import com.b2beyond.wallet.rpc.model.SingleTransactionItem;
import com.b2beyond.wallet.rpc.model.Transaction;
import com.b2beyond.wallet.rpc.model.TransactionItem;
import com.b2beyond.wallet.rpc.model.TransactionItems;
import com.b2beyond.wallet.rpc.model.Transfer;
import com.b2beyond.wallet.rpc.model.UnconfirmedTransactionHashes;
import com.b2beyond.wallet.rpc.exception.KnownJsonRpcException;
import com.b2beyond.wallet.b2bcoin.util.CoinUtil;
import com.b2beyond.wallet.b2bcoin.view.model.JComboboxItem;
import com.b2beyond.wallet.b2bcoin.view.view.panel.AbstractAddressJPanel;
import com.b2beyond.wallet.b2bcoin.view.view.renderer.DateTableCellRenderer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.DefaultRowSorter;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class TransactionsTabView extends AbstractAddressJPanel implements Observer {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private JsonRpcExecutor<SingleTransactionItem> transactionItemsJsonRpcExecutor;

    private JTable unconfirmedTransactionsTable;
    private DefaultTableModel unconfirmedTransactionsTableModel;

    private JTable transactionsTable;
    private DefaultTableModel transactionsTableModel;

    private String[] columnNames = {"Address", "Date", "Amount"};
    private String[] columnNamesUnconfirmed = {"Address", "Amount", ""};

    private boolean firstTableInitialization = true;


    public TransactionsTabView(JsonRpcExecutor<SingleTransactionItem> transactionItemsJsonRpcExecutor) {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        this.transactionItemsJsonRpcExecutor = transactionItemsJsonRpcExecutor;

        this.setLayout(new BorderLayout());

        // Preparing Unconfirmed Table !
        unconfirmedTransactionsTableModel = new DefaultTableModel(columnNamesUnconfirmed, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        unconfirmedTransactionsTable = new JTable(unconfirmedTransactionsTableModel);
        unconfirmedTransactionsTable.getColumnModel().getColumn(0).setPreferredWidth(700);
        unconfirmedTransactionsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        unconfirmedTransactionsTable.getColumnModel().getColumn(2).setPreferredWidth(75);

        unconfirmedTransactionsTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

        unconfirmedTransactionsTable.setRowHeight(30);

        // Preparing Confirmed Table !
        transactionsTableModel = new DefaultTableModel(columnNames, 0);
        transactionsTable = new JTable(transactionsTableModel);
        transactionsTable.getColumnModel().getColumn(0).setPreferredWidth(700);
        transactionsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        transactionsTable.getColumnModel().getColumn(1).setCellRenderer(new DateTableCellRenderer());
        transactionsTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        transactionsTable.setRowHeight(30);

        JScrollPane unconfirmedTransactionsTableScrollPane = new JScrollPane(unconfirmedTransactionsTable);
        unconfirmedTransactionsTableScrollPane.setVisible(true);
        unconfirmedTransactionsTableScrollPane.setColumnHeader(new JViewport() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 32;
                return d;
            }
        });

        JScrollPane transactionsTableScrollPane = new JScrollPane(transactionsTable);
        transactionsTableScrollPane.setVisible(true);
        transactionsTableScrollPane.setColumnHeader(new JViewport() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 32;
                return d;
            }
        });

        JPanel paymentsTablePanel = new JPanel(new BorderLayout());
        paymentsTablePanel.add(transactionsTable.getTableHeader(), BorderLayout.NORTH);
        paymentsTablePanel.add(unconfirmedTransactionsTableScrollPane, BorderLayout.CENTER);
        paymentsTablePanel.add(transactionsTableScrollPane, BorderLayout.SOUTH);

        JLabel addressLabel = new JLabel("Transactions for address : ");
        setAddresses(new JComboBox<JComboboxItem>());
        addresses.setMinimumSize(new Dimension(700, 30));

        JPanel addPaymentPanel = new JPanel();
        addPaymentPanel.add(addressLabel);
        addPaymentPanel.add(addresses);

        this.add(addPaymentPanel, BorderLayout.NORTH);
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
            update(transactionItems);
        }
        if (data instanceof UnconfirmedTransactionHashes) {
            UnconfirmedTransactionHashes unconfirmedHashes = (UnconfirmedTransactionHashes) data;
            updateUnconfirmed(unconfirmedHashes.getTransactionHashes());
        }
    }

    public void update(final TransactionItems transactions) {
        for (TransactionItem item : transactions.getItems()) {
            for (Transaction transaction : item.getTransactions()) {
                long amount = transaction.getAmount();

                if (amount < 0) {
                    String address = "";
                    for (Transfer transfer : transaction.getTransfers()) {
                        if (StringUtils.isNotBlank(transfer.getAddress())) {
                            System.out.println(transfer.getAddress());
                            if (addressesList.getAddresses().contains(transfer.getAddress())) {
                                address = transfer.getAddress();
                            }
                        }
                    }

                    final Object[] data = {address, transaction.getTimestamp() * 1000, CoinUtil.getTextForNumber(amount)};
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (transactionsTable != null) {
                                transactionsTableModel.addRow(data);
                            }
                        }
                    });
                } else {
                    for (Transfer transfer : transaction.getTransfers()) {
                        System.out.println(transfer.getAddress());
                        if (StringUtils.isNotBlank(transfer.getAddress())) {
                            if (transaction.getUnlockTime() != 0) {
                                LOGGER.trace("Unlock time : " + transaction.getUnlockTime());
                            }

                            final Object[] data = {transfer.getAddress(), transaction.getTimestamp() * 1000, CoinUtil.getTextForNumber(transfer.getAmount())};
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (transactionsTable != null) {
                                        transactionsTableModel.addRow(data);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }

        if (firstTableInitialization) {
            setFilterOnTable(transactionsTable, transactionsTableModel);

            LOGGER.info("Setting sort order on column 1");
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (transactionsTable != null) {
                        LOGGER.info("Filtering table");
                        List<RowSorter.SortKey> list = new ArrayList<>();
                        list.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
                        transactionsTable.getRowSorter().setSortKeys(list);
                        ((DefaultRowSorter) transactionsTable.getRowSorter()).sort();
                    }
                }
            });
            firstTableInitialization = false;
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                transactionsTableModel.fireTableDataChanged();
            }
        });

        transactionsTable.updateUI();
    }

    public void updateUnconfirmed(List<String> unconfirmedHashes) {
        URL splashScreenLocation = Thread.currentThread().getContextClassLoader().getResource("locked-icon.png");
        ImageIcon lockedIcon = new ImageIcon(splashScreenLocation);
        LOGGER.trace("Image loading status : " + splashScreenLocation + " : " + lockedIcon.getImageLoadStatus());
        unconfirmedTransactionsTableModel.setRowCount(0);

        TransactionItems transactions = new TransactionItems();

        for (String hash : unconfirmedHashes) {
            SingleTransactionItem transactionItem = null;
            try {
                transactionItem = transactionItemsJsonRpcExecutor.execute("\"params\":{  " +
                        "     \"transactionHash\":\"" + hash + "\"" +
                        "  }");
                TransactionItem item = new TransactionItem();
                item.getTransactions().add(transactionItem.getTransaction());
                transactions.getItems().add(item);
            } catch (KnownJsonRpcException e) {
                e.printStackTrace();
            }
        }

        for (TransactionItem item : transactions.getItems()) {
            for (Transaction transaction : item.getTransactions()) {
                for (Transfer transfer : transaction.getTransfers()) {
                    if (StringUtils.isNotBlank(transfer.getAddress())) {
                        long amount = transaction.getAmount();

                        if (amount < 0) {
                            String address = "";

                            for (Transfer innerTransfer : transaction.getTransfers()) {
                                if (StringUtils.isNotBlank(innerTransfer.getAddress())) {
                                    if (!addressesList.getAddresses().contains(innerTransfer.getAddress())) {
                                        address = innerTransfer.getAddress();
                                    }
                                }
                            }

                            final Object[] data = {address, CoinUtil.getTextForNumber(amount), lockedIcon};
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (unconfirmedTransactionsTable != null) {
                                        unconfirmedTransactionsTableModel.addRow(data);
                                    }
                                }
                            });
                            break;
                        } else {
                            final Object[] data = {transfer.getAddress(), CoinUtil.getTextForNumber(transfer.getAmount()), lockedIcon};
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (unconfirmedTransactionsTable != null) {
                                        unconfirmedTransactionsTableModel.addRow(data);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    }

}
