package com.b2beyond.wallet.b2bcoin.view.view;

import com.b2beyond.wallet.b2bcoin.util.CoinUtil;
import com.b2beyond.wallet.b2bcoin.view.controller.ActionController;
import com.b2beyond.wallet.b2bcoin.view.view.panel.*;
import com.b2beyond.wallet.rpc.exception.KnownJsonRpcException;
import com.b2beyond.wallet.rpc.model.*;
import com.b2beyond.wallet.rpc.model.coin.BlockWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;


public class StatusTabView extends AbstractWhitePanel implements Observer {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private ActionController actionController;

    private String lastBlockHash;

    private BalancePanel balancePanel;
    private PaymentsPanel paymentsPanel;
    private ServerPanel serverPanel;

    private int fullNumberOfPayments = 0;
    private long fullPayedAmount = 0;


    public StatusTabView(final ActionController actionController) {
        this.actionController = actionController;

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
    }


    public void update(Observable rpcPoller, Object data) {
        if (data instanceof Status) {
            Status viewData = (Status) data;
            lastBlockHash = viewData.getLastBlockHash();
            BlockWrapper blockWrapper = actionController.getBlockWrapper(lastBlockHash);

            serverPanel.getPeers().setText("" + viewData.getPeerCount());
            serverPanel.getLastBlockHash().setText(viewData.getLastBlockHash());
            if (blockWrapper != null && blockWrapper.getBlock() != null) {
                serverPanel.getBlockHeight().setText("" + blockWrapper.getBlock().getHeight());
                serverPanel.getCoinsInNetwork().setText(CoinUtil.getTextForNumber(blockWrapper.getBlock().getAlreadyGeneratedCoins().floatValue()));
                serverPanel.getBaseReward().setText(CoinUtil.getTextForNumber(blockWrapper.getBlock().getBaseReward()));
                serverPanel.getDifficulty().setText("" + blockWrapper.getBlock().getDifficulty());
            }
        }
        if (data instanceof Addresses) {
            Addresses addresses = (Addresses) data;

            long fullAmount = 0;
            long fullLockedAmount = 0;

            for (String address : addresses.getAddresses()) {
                AddressBalance addressBalance = actionController.getBalance(address);

                if (addressBalance != null) {
                    fullAmount += addressBalance.getAvailableBalance();
                    fullLockedAmount += addressBalance.getLockedAmount();
                }
            }

            balancePanel.getAvailableBalance().setText(CoinUtil.getTextForNumber(fullAmount));
            balancePanel.getLockedBalance().setText(CoinUtil.getTextForNumber(fullLockedAmount));
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
                transactionItem = actionController.getWalletRpcController().getTransactionExecutor().execute("\"params\":{  " +
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
        for (TransactionItem item : transactions.getItems()) {
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

        paymentsPanel.getTotalPaymentsLockedAmount().setText(CoinUtil.getTextForNumber(fullPayedUnconfirmedAmount));
    }

    private void updateBalances(TransactionItems transactionItems) {
        for (TransactionItem item : transactionItems.getItems()) {
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
        paymentsPanel.getTotalPaymentsAmount().setText(CoinUtil.getTextForNumber(fullPayedAmount));
    }

}
