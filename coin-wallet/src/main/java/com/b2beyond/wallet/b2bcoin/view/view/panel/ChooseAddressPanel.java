package com.b2beyond.wallet.b2bcoin.view.view.panel;

import com.b2beyond.wallet.b2bcoin.util.CoinUtil;
import com.b2beyond.wallet.b2bcoin.util.ComponentFactory;
import com.b2beyond.wallet.b2bcoin.view.controller.ActionController;
import com.b2beyond.wallet.b2bcoin.view.controller.PaymentController;
import com.b2beyond.wallet.b2bcoin.view.model.JComboboxItem;
import com.b2beyond.wallet.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.rpc.exception.KnownJsonRpcException;
import com.b2beyond.wallet.rpc.model.Address;
import com.b2beyond.wallet.rpc.model.Addresses;
import com.b2beyond.wallet.rpc.model.Payment;
import com.b2beyond.wallet.rpc.model.PaymentInput;
import org.apache.log4j.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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


public class ChooseAddressPanel extends AbstractAddressJPanel {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private ActionController actionController;


    public ChooseAddressPanel(ActionController actionController) {

        this.actionController = actionController;
        //construct preComponents
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[]  { 1, 1, 1 };
        gbl.rowHeights = new int[] { 1, 1, 1 };
        gbl.columnWeights = new double[]{0.01, 0.48, 0.02};
        gbl.rowWeights = new double[] { 0.02, 0.02, 0.02 };
        setLayout(gbl);

        //construct components
        addresses = new JComboBox<>();


        GridLayout layout = new GridLayout(0 ,1);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(addresses, gbc);

        try {
            addressesList = actionController.getWalletRpcController().getAddressesExecutor().execute(JsonRpcExecutor.EMPTY_PARAMS);
            update(addressesList);
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }
    }

    public String getAddress() {
        return ((JComboboxItem)addresses.getSelectedItem()).getValue();
    }

}
