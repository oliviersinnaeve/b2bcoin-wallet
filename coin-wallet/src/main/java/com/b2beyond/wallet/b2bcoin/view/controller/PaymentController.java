package com.b2beyond.wallet.b2bcoin.view.controller;

import com.b2beyond.wallet.rpc.model.AddressBalance;
import com.b2beyond.wallet.rpc.model.Payment;
import com.b2beyond.wallet.rpc.model.PaymentInput;
import com.b2beyond.wallet.rpc.exception.KnownJsonRpcException;
import com.b2beyond.wallet.b2bcoin.util.CoinUtil;
import org.apache.log4j.Logger;

import javax.swing.JOptionPane;


public class PaymentController {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private ActionController controller;


    public PaymentController(ActionController controller) {
        this.controller = controller;
    }

    public Payment makePayment(PaymentInput input) throws KnownJsonRpcException {
        LOGGER.info("Create payment");

        String address = input.getAddress();
        AddressBalance balance = controller.getBalance(address);
        Long totalAMount = 0l;
        for (Long amount : input.getTransfers().values()) {
            totalAMount += amount;
        }

        if (balance.getAvailableBalance() <= totalAMount) {
            JOptionPane.showMessageDialog(null,
                "You can only send " + CoinUtil.getTextForNumber(totalAMount - input.getFee()) + ". You must substract the fee from the final amount.",
                "Address deletion error",
                JOptionPane.ERROR_MESSAGE);

            // TODO add message and deduct fee automatically !
        } else {
            this.controller.getWalletRpcController().getPaymentExecutor().setReadTimeout(300000);
            try {
                Payment payment = this.controller.getWalletRpcController().getPaymentExecutor().execute(input.getParams());

                if (payment == null) {
                    JOptionPane.showMessageDialog(null,
                            "Failed to execute payment, retry later ...",
                            "Fatal error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Payment was successfully executed.",
                            "Payment success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (KnownJsonRpcException e) {
                LOGGER.error("Creating payment failed : " + e.getError().getMessage());
                throw e;
            }
        }

        return null;
    }

}
