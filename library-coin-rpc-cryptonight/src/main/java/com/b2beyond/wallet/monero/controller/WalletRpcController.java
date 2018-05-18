package com.b2beyond.wallet.monero.controller;

import com.b2beyond.wallet.rpc.model.Address;
import com.b2beyond.wallet.rpc.model.AddressBalance;
import com.b2beyond.wallet.rpc.model.Addresses;
import com.b2beyond.wallet.rpc.model.Payment;
import com.b2beyond.wallet.rpc.model.SingleTransactionItem;
import com.b2beyond.wallet.rpc.model.SpendKeys;
import com.b2beyond.wallet.rpc.model.Status;
import com.b2beyond.wallet.rpc.model.Success;
import com.b2beyond.wallet.rpc.model.TransactionItems;
import com.b2beyond.wallet.rpc.model.UnconfirmedTransactionHashes;
import com.b2beyond.wallet.rpc.model.ViewSecretKey;
import com.b2beyond.wallet.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.rpc.RpcPoller;
import com.b2beyond.wallet.rpc.WalletController;

import java.util.ArrayList;
import java.util.List;


public class WalletRpcController {
//        implements WalletController {

    private JsonRpcExecutor<ViewSecretKey> viewSecretKeyExecutor;
    private JsonRpcExecutor<SpendKeys> spendKeysExecutor;
    private JsonRpcExecutor<Status> statusExecutor;
    private JsonRpcExecutor<Address> createAddressExecutor;
//    private JsonRpcExecutor<Success> deleteAddressExecutor;
    private JsonRpcExecutor<AddressBalance> balanceExecutor;
    private JsonRpcExecutor<Addresses> addressesExecutor;
    private JsonRpcExecutor<Payment> paymentExecutor;
    private JsonRpcExecutor<TransactionItems> transactionsExecutor;
    private JsonRpcExecutor<SingleTransactionItem> transactionExecutor;
    private JsonRpcExecutor<UnconfirmedTransactionHashes> unconfirmedTransactionHashesExecutor;

    private JsonRpcExecutor<Void> resetExecutor;
    private JsonRpcExecutor<Void> saveExecutor;

    private List<RpcPoller<?>> pollers = new ArrayList<>();

    public WalletRpcController(String baseUrl) {
        viewSecretKeyExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getViewKey", ViewSecretKey.class);
        spendKeysExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getSpendKeys", SpendKeys.class);
        statusExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getheight", Status.class);
        createAddressExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "make_integrated_address", Address.class);
//        deleteAddressExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "deleteAddress", Success.class);
        balanceExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getBalance", AddressBalance.class);
        addressesExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getAddresses", Addresses.class);
        paymentExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "sendTransaction", Payment.class);
        transactionsExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getTransactions", TransactionItems.class);
        transactionExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getTransaction", SingleTransactionItem.class);
        unconfirmedTransactionHashesExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getUnconfirmedTransactionHashes", UnconfirmedTransactionHashes.class);

        resetExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "reset", Void.class);
        saveExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "update", Void.class);
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
            new Thread(poller).start();
        }
    }

    public JsonRpcExecutor<ViewSecretKey> getViewSecretKeyExecutor() {
        return viewSecretKeyExecutor;
    }

    public JsonRpcExecutor<SpendKeys> getSpendKeysExecutor() {
        return spendKeysExecutor;
    }

    public JsonRpcExecutor<Status> getStatusExecutor() {
        return statusExecutor;
    }

    public JsonRpcExecutor<Address> getCreateAddressExecutor() {
        return createAddressExecutor;
    }

    public JsonRpcExecutor<Success> getDeleteAddressExecutor() {
        throw new NoSuchMethodError();
//        return deleteAddressExecutor;
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
