package com.b2beyond.wallet.rpc;

import com.b2beyond.wallet.rpc.model.coin.BlockCount;
import com.b2beyond.wallet.rpc.model.coin.BlockWrapper;
import com.b2beyond.wallet.rpc.model.coin.TransactionWrapper;


public interface CoinController {

    JsonRpcExecutor<BlockCount> getBlockCountExecutor();

    JsonRpcExecutor<BlockWrapper> getBlockWrapperExecutor();

    JsonRpcExecutor<TransactionWrapper> getTransactionWrapperExecutor();

}
