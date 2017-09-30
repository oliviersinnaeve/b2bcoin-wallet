package com.b2beyond.wallet.b2bcoin.controler;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.RpcPoller;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Address;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.AddressBalance;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Payment;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.SingleTransactionItem;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Status;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.TransactionItems;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.UnconfirmedTransactionHashes;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.ViewSecretKey;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class WalletRpcController {

    private JsonRpcExecutor<ViewSecretKey> viewSecretKeyExecutor;
    private JsonRpcExecutor<Status> statusExecutor;
    private JsonRpcExecutor<Address> createAddressExecutor;
    private JsonRpcExecutor<AddressBalance> balanceExecutor;
    private JsonRpcExecutor<Addresses> addressesExecutor;
    private JsonRpcExecutor<Payment> paymentExecutor;
    private JsonRpcExecutor<TransactionItems> transactionsExecutor;
    private JsonRpcExecutor<SingleTransactionItem> transactionExecutor;
    private JsonRpcExecutor<UnconfirmedTransactionHashes> unconfirmedTransactionHashesExecutor;

    private JsonRpcExecutor<Void> resetExecutor;
    private JsonRpcExecutor<Void> saveExecutor;

    private List<RpcPoller<?>> pollers = new ArrayList<>();

    public WalletRpcController(PropertiesConfiguration applicationProperties) {
        String baseUrl = applicationProperties.getString("wallet-daemon-base-url");

        viewSecretKeyExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getViewKey", ViewSecretKey.class);
        statusExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getStatus", Status.class);
        createAddressExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "createAddress", Address.class);
        balanceExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getBalance", AddressBalance.class);
        addressesExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getAddresses", Addresses.class);
        paymentExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "sendTransaction", Payment.class);
        transactionsExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getTransactions", TransactionItems.class);
        transactionExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getTransaction", SingleTransactionItem.class);
        unconfirmedTransactionHashesExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getUnconfirmedTransactionHashes", UnconfirmedTransactionHashes.class);

        resetExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "reset", Void.class);
        saveExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "save", Void.class);
    }

    public void addPollers(RpcPoller poller) {
        pollers.add(poller);
        new Thread(poller).start();
    }

    public void stop() {
        for (RpcPoller poller : pollers) {
            poller.stop();
        }
    }

    public void restart() {
        for (RpcPoller poller : pollers) {
            poller.start();
            new Thread(poller).start();
        }
    }

    public JsonRpcExecutor<ViewSecretKey> getViewSecretKeyExecutor() {
        return viewSecretKeyExecutor;
    }

    public JsonRpcExecutor<Status> getStatusExecutor() {
        return statusExecutor;
    }

    public JsonRpcExecutor<Address> getCreateAddressExecutor() {
        return createAddressExecutor;
    }

    public JsonRpcExecutor<AddressBalance> getBalanceExecutor() {
        return balanceExecutor;
    }

    public JsonRpcExecutor<Addresses> getAddressesExecutor() {
        return addressesExecutor;
    }

    public JsonRpcExecutor<Payment> getPaymentExecutor() {
        return paymentExecutor;
    }

    public JsonRpcExecutor<TransactionItems> getTransactionsExecutor() {
        return transactionsExecutor;
    }

    public JsonRpcExecutor<SingleTransactionItem> getTransactionExecutor() {
        return transactionExecutor;
    }

    public JsonRpcExecutor<UnconfirmedTransactionHashes> getUnconfirmedTransactionHashesExecutor() {
        return unconfirmedTransactionHashesExecutor;
    }

    public JsonRpcExecutor<Void> getResetExecutor() {
        return resetExecutor;
    }

    public JsonRpcExecutor<Void> getSaveExecutor() {
        return saveExecutor;
    }
}
