package com.b2beyond.wallet.b2bcoin.view.view;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.view.controller.MiningController;
import com.b2beyond.wallet.b2bcoin.view.model.JComboboxItem;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


public class MiningTabView extends JPanel implements ActionListener, Observer {

    private MiningController miningController;

    private JComboBox<JComboboxItem> addresses;
    private JComboBox<JComboboxItem> numberOfProcessors;

    private JTextArea logging;
    private BufferedReader loggingBuffer;


    public MiningTabView(MiningController miningController) {
        this.miningController = miningController;
        this.setLayout(new BorderLayout());

        JLabel addressLabel = new JLabel("Mining for address : ");
        addresses = new JComboBox<>();
        JLabel numberOfProcessorsLabel = new JLabel("Use # Cpu Cores : ");
        numberOfProcessors = new JComboBox<>();

        fillNumberOfProcessorsCombobox();

        JPanel addPaymentPanel = new JPanel();
        addPaymentPanel.add(addressLabel);
        addPaymentPanel.add(addresses);
        addPaymentPanel.add(numberOfProcessorsLabel);
        addPaymentPanel.add(numberOfProcessors);

        JButton startMiningButton = new JButton("Start Mining");
        startMiningButton.addActionListener(this);
        JButton stopMiningButton = new JButton("Stop Mining");
        stopMiningButton.addActionListener(this);

        logging = new JTextArea();
        DefaultCaret caret = (DefaultCaret)logging.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane loggingScrollPane = new JScrollPane(logging);
        loggingScrollPane.setVisible(true);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startMiningButton);
        buttonPanel.add(stopMiningButton);

        this.add(addPaymentPanel, BorderLayout.NORTH);
        this.add(loggingScrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void fillNumberOfProcessorsCombobox() {
        int cores = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < cores; i++) {
            String value = String.valueOf(i + 1);
            JComboboxItem item = new JComboboxItem(value, value);
            numberOfProcessors.addItem(item);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        Addresses availableAddresses;
        if (data instanceof Addresses) {
            this.addresses.removeAllItems();
            availableAddresses = (Addresses) data;
            for (String address : availableAddresses.getAddresses()) {
                JComboboxItem item = new JComboboxItem(address, address);
                addresses.addItem(item);
            }
        }
    }

    public void setController(MiningController miningController) {
        this.miningController = miningController;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("Start Mining".equalsIgnoreCase(command)) {
            JComboboxItem address = (JComboboxItem) addresses.getSelectedItem();
            JComboboxItem numberOfProcessorsSelectedItem = (JComboboxItem) numberOfProcessors.getSelectedItem();
            miningController.startMining(address.getKey(), numberOfProcessorsSelectedItem.getKey());
            setLogging(miningController.getMiningOutput());
        }
        if ("Stop Mining".equalsIgnoreCase(command)) {
            miningController.stopMining();
        }
    }

    public void setLogging(BufferedReader loggingBufferedReader) {
        this.loggingBuffer = loggingBufferedReader;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (miningController.isMining()) {
                    try {
                        String line;
                        while ((line = loggingBuffer.readLine()) != null) {
                            logging.append(line + System.getProperty("line.separator"));
                        }

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        // TODO log accordingly
                    }
                }
            }
        }).start();
    }
}
