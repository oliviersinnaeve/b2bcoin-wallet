package com.b2beyond.wallet.rpc;

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


public interface WalletController {

    JsonRpcExecutor<ViewSecretKey> getViewSecretKeyExecutor();

    JsonRpcExecutor<SpendKeys> getSpendKeysExecutor();

    JsonRpcExecutor<Status> getStatusExecutor();

    JsonRpcExecutor<Address> getCreateAddressExecutor();

    JsonRpcExecutor<Success> getDeleteAddressExecutor();

    JsonRpcExecutor<AddressBalance> getBalanceExecutor();

    JsonRpcExecutor<Addresses> getAddressesExecutor();

    JsonRpcExecutor<Payment> getPaymentExecutor();

    JsonRpcExecutor<TransactionItems> getTransactionsExecutor();

    JsonRpcExecutor<SingleTransactionItem> getTransactionExecutor();

    JsonRpcExecutor<UnconfirmedTransactionHashes> getUnconfirmedTransactionHashesExecutor();

    JsonRpcExecutor<Void> getResetExecutor();

    JsonRpcExecutor<Void> getSaveExecutor();

}
