package com.b2beyond.wallet.b2bcoin.view.controller;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Address;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.AddressBalance;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.exception.KnownJsonRpcException;
import org.apache.log4j.Logger;


public class AddressesController {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private JsonRpcExecutor<Address> createAddressExecutor;
    private JsonRpcExecutor<AddressBalance> balanceExecutor;


    public AddressesController(JsonRpcExecutor<Address> createAddressExecutor, JsonRpcExecutor<AddressBalance> balanceExecutor) {
        this.createAddressExecutor = createAddressExecutor;
        this.balanceExecutor = balanceExecutor;
    }

    public Address createAddress() {
        LOGGER.info("Create address");
        try {
            return this.createAddressExecutor.execute(JsonRpcExecutor.EMPTY_PARAMS);
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }

        return null;
    }

    public AddressBalance getBalance(String address) {
        LOGGER.info("Get address balance : " + address);

        try {
            return this.balanceExecutor.execute("\"params\": {" +
                "\"address\": \"" + address + "\"" +
            "}");
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }

        return null;
    }

}
