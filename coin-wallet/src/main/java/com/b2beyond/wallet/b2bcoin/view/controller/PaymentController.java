package com.b2beyond.wallet.b2bcoin.view.controller;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Address;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Payment;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.PaymentInput;
import org.apache.log4j.Logger;


public class PaymentController {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private JsonRpcExecutor<Payment> paymentExecutor;


    public PaymentController(JsonRpcExecutor<Payment> paymentExecutor) {
        this.paymentExecutor = paymentExecutor;
    }

    public Payment makePayment(PaymentInput input) {
        LOGGER.info("Create payment");

        String params = "\"params\":{\n" +
                "    \"anonymity\":" + input.getAnonymity() + ",\n" +
                "    \"fee\":" + input.getFee() + ",\n" +
                "    \"unlockTime\":" + input.getUnlockTime() + ",\n";
//                "    \"paymentId\":\"" + input.getPaymentId() + "\",\n";


        int index = 0;
        if(input.getAddresses() != null && !input.getAddresses().isEmpty()) {
            params += "    \"addresses\":[\n";

            for (Address key : input.getAddresses()) {
                String address = key.getAddress();
                params += "        \"" + address + "\",\n";

                if (index < input.getAddresses().size() - 1) {
                    params += ",";
                }
                index += 1;
            }

//                "        \"27eJo2S9eVo5N2G9zyjkqNBZPR6d2qvVD122vQMGAhcrjZjLu8nsMqk3c4KQ9iMJ4AV4fpBMccmjfJ4cu7uprKLNFX4qWNh\",\n" +
//                "        \"24JtjYsLdSJKNNDCPGdMco5NbMBLqVWZ5ZiW5vzjXQUrLpMs1MRnfTQ3c4KQ9iMJ4AV4fpBMccmjfJ4cu7uprKLNFXHARwn\",\n" +
//                "        \"21fYPCpaM3ochSSyLnhDAhgw1yV5WPb5c1BfyX5eidbMGyEPgnbSgJW3c4KQ9iMJ4AV4fpBMccmjfJ4cu7uprKLNFX8VQMv\"\n" +
            params += "    ],\n";
        }

        params += "    \"transfers\":[\n";

        index = 0;
        for (String key : input.getTransfers().keySet()) {
            Long amount = input.getTransfers().get(key);
            params += "        {\n" +
                "            \"amount\":" + amount + ",\n" +
                "            \"address\":\"" + key + "\"\n" +
                "        }\n";

            if (index < input.getTransfers().size() - 1) {
                params += ",";
            }
            index += 1;
        }

//                "        {\n" +
//                "            \"amount\":123456,\n" +
//                "            \"address\":\"27eJo2S9eVo5N2G9zyjkqNBZPR6d2qvVD122vQMGAhcrjZjLu8nsMqk3c4KQ9iMJ4AV4fpBMccmjfJ4cu7uprKLNFX4qWNh\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"amount\":234567,\n" +
//                "            \"address\":\"278g3wNw5W48DeGbjwxkW3XauBip64uYKS9eFveUHBfdRAG3dYHPZvqXy5BWbfuKEtWZ86PJZdRacAgr1x3gtP5nLyGcVt8\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"amount\":345678,\n" +
//                "            \"address\":\"2AtjUXGmhP6CmbRxCtBESR4MjSGiWCQUTPCdsDpw72Co2pwzZT7rjnaBNRCSFCEygjNo5oe8mHyXU4Eip8szu4ZnAFyPW1a\"\n" +
//                "        }\n" +
        params += "    ],\n" +
                "    \"changeAddress\":\"" + input.getAddress() +"\"\n" +
                "}";

        this.paymentExecutor.setParams(params);

        return this.paymentExecutor.execute();
    }

}
