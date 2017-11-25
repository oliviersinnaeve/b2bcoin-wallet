package com.b2beyond.wallet.b2bcoin.view.view;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.AddressBalance;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.coin.BlockWrapper;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.SingleTransactionItem;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Status;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Transaction;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.TransactionItem;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.TransactionItems;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Transfer;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.UnconfirmedTransactionHashes;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.exception.KnownJsonRpcException;
import com.b2beyond.wallet.b2bcoin.util.CoinUtil;
import com.b2beyond.wallet.b2bcoin.view.controller.ActionController;
import com.b2beyond.wallet.b2bcoin.view.controller.AddressesController;
import com.b2beyond.wallet.b2bcoin.view.view.panel.BalancePanel;
import com.b2beyond.wallet.b2bcoin.view.view.panel.DonationPanel;
import com.b2beyond.wallet.b2bcoin.view.view.panel.PaymentsPanel;
import com.b2beyond.wallet.b2bcoin.view.view.panel.ServerPanel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;


public class StatusTabView extends JPanel implements Observer {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private ActionController actionController;
    private AddressesController addressController;
    private JsonRpcExecutor<SingleTransactionItem> transactionItemsJsonRpcExecutor;

    private String lastBlockHash;

    private BalancePanel balancePanel;
    private PaymentsPanel paymentsPanel;
    private ServerPanel serverPanel;

    private int fullNumberOfPayments = 0;
    private long fullPayedAmount = 0;


    public StatusTabView(final ActionController actionController,
                         final AddressesController addressController,
                         final JsonRpcExecutor<Void> resetExecutor,
                         final JsonRpcExecutor<SingleTransactionItem> transactionItemsJsonRpcExecutor) {
        this.actionController = actionController;
        this.addressController= addressController;
        this.transactionItemsJsonRpcExecutor = transactionItemsJsonRpcExecutor;

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{1, 1, 1, 1, 1};
        gridBagLayout.rowHeights = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
        gridBagLayout.columnWeights = new double[]{0.01, 0.48, 0.02, 0.48, 0.01};
        gridBagLayout.rowWeights = new double[]{0.01, 0.01, 0.01, 0.01, 0.93, 0.01, 0.01, 0.01};
        setLayout(gridBagLayout);

        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 1;

        balancePanel = new BalancePanel();
        add(balancePanel, gbc_panel);

        paymentsPanel = new PaymentsPanel();
        gbc_panel.gridx = 3;
        gbc_panel.gridy = 1;
        add(paymentsPanel, gbc_panel);

        serverPanel = new ServerPanel();
        gbc_panel.gridwidth = 3;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 3;
        add(serverPanel, gbc_panel);

        DonationPanel donationPanel = new DonationPanel();
        gbc_panel.gridwidth = 3;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 5;
        add(donationPanel, gbc_panel);

//        JButton resetButton = new JButton("Reset wallet");
//        resetButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Resetting wallet");
//                actionController.stopBackgroundProcessesBeforeReset();
//                try {
//                    resetExecutor.execute(JsonRpcExecutor.EMPTY_PARAMS);
//                } catch (KnownJsonRpcException e1) {
//                    e1.printStackTrace();
//                }
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//
//                actionController.startBackgroundProcessesAfterReset();
//            }
//        });
//
//        gbc_panel.gridx = 3;
//        gbc_panel.gridy = 7;
//        add(resetButton, gbc_panel);
    }


    public void update(Observable rpcPoller, Object data) {
        if (data instanceof Status) {
            Status viewData = (Status) data;
            if (!viewData.getLastBlockHash().equals(lastBlockHash)) {
                lastBlockHash = viewData.getLastBlockHash();
                BlockWrapper blockWrapper = actionController.getBlockWrapper(lastBlockHash);

                serverPanel.getPeers().setText("" + viewData.getPeerCount());
                serverPanel.getLastBlockHash().setText(viewData.getLastBlockHash());
                if (blockWrapper != null &&  blockWrapper.getBlock() != null) {
                    serverPanel.getBlockHeight().setText("" + blockWrapper.getBlock().getHeight());
                    serverPanel.getCoinsInNetwork().setText(CoinUtil.getTextForLong(blockWrapper.getBlock().getAlreadyGeneratedCoins()));
                    serverPanel.getBaseReward().setText(CoinUtil.getTextForLong(blockWrapper.getBlock().getBaseReward()));
                    serverPanel.getDifficulty().setText("" + blockWrapper.getBlock().getDifficulty());
                }
            }
        }
        if (data instanceof Addresses) {
            Addresses addresses = (Addresses) data;

            long fullAmount = 0;
            long fullLockedAmount = 0;

            for (String address: addresses.getAddresses()) {
                AddressBalance addressBalance = addressController.getBalance(address);

                if (addressBalance != null) {
                    fullAmount += addressBalance.getAvailableBalance();
                    fullLockedAmount += addressBalance.getLockedAmount();
                }
            }

            balancePanel.getAvailableBalance().setText(CoinUtil.getTextForLong(fullAmount));
            balancePanel.getLockedBalance().setText(CoinUtil.getTextForLong(fullLockedAmount));
        }
        if (data instanceof TransactionItems) {
            TransactionItems transactionItems = (TransactionItems) data;
            updateBalances(transactionItems);
        }
        if (data instanceof UnconfirmedTransactionHashes) {
            UnconfirmedTransactionHashes transactionItems = (UnconfirmedTransactionHashes) data;
            updateUnconfirmed(transactionItems);
        }
    }

    private void updateUnconfirmed(UnconfirmedTransactionHashes transactionItems) {
        TransactionItems transactions = new TransactionItems();

        for (String hash : transactionItems.getTransactionHashes()) {
            SingleTransactionItem transactionItem;
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

        long fullPayedUnconfirmedAmount = 0;
        for (TransactionItem item: transactions.getItems()) {
            for (Transaction transaction : item.getTransactions()) {
                for (Transfer transfer : transaction.getTransfers()) {
                    if (StringUtils.isNotBlank(transfer.getAddress())) {
                        float amount = transaction.getAmount();

                        if (amount < 0) {
                            fullPayedUnconfirmedAmount += amount;
                            break;
                        }
                    }
                }
            }
        }

        paymentsPanel.getTotalPaymentsLockedAmount().setText(CoinUtil.getTextForLong(fullPayedUnconfirmedAmount));
    }

    private void updateBalances(TransactionItems transactionItems) {
        for (TransactionItem item: transactionItems.getItems()) {
            for (Transaction transaction : item.getTransactions()) {
                long amount = transaction.getAmount();
                long unlockBlocks = transaction.getUnlockTime();

                if (unlockBlocks != 0) {
                    LOGGER.trace("Unlock time : " + unlockBlocks);
                }


                if (amount < 0) {
                    fullPayedAmount += amount;
                    fullNumberOfPayments += 1;
                }
            }
        }

        paymentsPanel.getNumberOfPayments().setText("" + fullNumberOfPayments);
        paymentsPanel.getTotalPaymentsAmount().setText(CoinUtil.getTextForLong(fullPayedAmount));
    }

}
