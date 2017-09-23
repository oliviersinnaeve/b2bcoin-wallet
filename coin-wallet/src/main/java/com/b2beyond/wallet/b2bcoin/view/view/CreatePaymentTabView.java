package com.b2beyond.wallet.b2bcoin.view.view;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Payment;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.PaymentInput;
import com.b2beyond.wallet.b2bcoin.util.CoinUtil;
import com.b2beyond.wallet.b2bcoin.view.controller.PaymentController;
import com.b2beyond.wallet.b2bcoin.view.model.JComboboxItem;
import com.b2beyond.wallet.b2bcoin.view.view.panel.AbstractAddressJPanel;
import com.b2beyond.wallet.b2bcoin.view.view.panel.TransferPanel;
import org.apache.log4j.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


public class CreatePaymentTabView extends AbstractAddressJPanel implements ActionListener, Observer {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private PaymentController paymentController;

    private JPanel transferPanel;
    private List<TransferPanel> transfers = new ArrayList<>();

    private JTextField fee;


    public CreatePaymentTabView(PaymentController controller) {

        this.paymentController = controller;
        //construct preComponents
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[]  { 1, 1, 1, 1, 1 };
        gbl.rowHeights = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
        gbl.columnWeights = new double[]{0.01, 0.48, 0.02, 0.48, 0.01};
        gbl.rowWeights = new double[] { 0.02, 0.02, 0.02, 0.02, 0.02, 0.88, 0.02, 0.02, 0.02 };
        setLayout(gbl);

        //construct components
        JLabel addressFromLabel = new JLabel ("Adress from :");
        JLabel feeLabel = new JLabel ("Fee :");

        addresses = new JComboBox<>();

        NumberFormat amountFormat = NumberFormat.getNumberInstance();
        amountFormat.setMinimumFractionDigits(12);
        amountFormat.setMaximumFractionDigits(12);
        fee = new JFormattedTextField(amountFormat);
        fee.setColumns(35);
        fee.setText("0.000001000000");

        JButton addPaymentButton = new JButton ("Add payment");
        JButton createPaymentButton = new JButton ("Create payment(s)");

        //set components properties
        addressFromLabel.setToolTipText ("Address from");
        createPaymentButton.setToolTipText ("Create payment(s)");

        addPaymentButton.addActionListener(this);
        createPaymentButton.addActionListener(this);

        TransferPanel panel = new TransferPanel(true);
        transfers.add(panel);
        GridLayout layout = new GridLayout(0 ,1);
        transferPanel = new JPanel(layout);
        layout.setVgap(10);
        transferPanel.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(addressFromLabel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        add(addresses, gbc);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(feeLabel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 3;
        gbc.gridy = 3;
        add(fee, gbc);

        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(transferPanel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 8;
        add(addPaymentButton, gbc);

        gbc.gridx = 3;
        gbc.gridy = 8;
        add(createPaymentButton, gbc);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof Addresses) {
            if (!data.equals(addressesList)) {
                addressesList = (Addresses) data;
                update(addressesList);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Create payment(s)")) {
            PaymentInput input = new PaymentInput();
            input.setAddress(((JComboboxItem)addresses.getSelectedItem()).getValue());
            long feeAmount = CoinUtil.getLongForText(fee.getText());
            input.setFee(feeAmount);
            Map<String, Long> transferList = new HashMap<>();

            for (TransferPanel tmpTransfer : transfers) {
                long amount = CoinUtil.getLongForText(tmpTransfer.getAmount().getText());
                transferList.put(tmpTransfer.getAddress().getText(), amount);
            }
            input.setTransfers(transferList);

            Payment payment = paymentController.makePayment(input);
        }

        if (command.equals("Add payment")) {
            TransferPanel newPanel = new TransferPanel(false);
            transfers.add(newPanel);
            transferPanel.add(newPanel);
            newPanel.setActionListeners();
        }
    }

}
