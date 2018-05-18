package com.b2beyond.wallet.rpc;

import com.b2beyond.wallet.rpc.model.coin.*;


public interface CoinController {

    JsonRpcExecutor<BlockCount> getBlockCountExecutor();

    JsonRpcExecutor<BlockHeaderWrapper> getBlockHeaderWrapperExecutor();

    JsonRpcExecutor<BlockWrapper> getBlockWrapperExecutor();

    JsonRpcExecutor<TransactionWrapper> getTransactionWrapperExecutor();

}
