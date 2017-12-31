package com.b2beyond.wallet.b2bcoin.view.view.panel;

import com.b2beyond.wallet.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.view.model.JComboboxItem;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public abstract class AbstractAddressJPanel extends AbstractBorderlessJPanel implements ActionListener {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    protected Addresses addressesList;

    protected JComboBox<JComboboxItem> addresses;

    private JTable table;


    protected void setFilterOnTable(JTable tableParameter, DefaultTableModel model) {
        this.table = tableParameter;
        LOGGER.info("Setting filter on table");
        RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
            public boolean include(Entry entry) {
                String address = (String) entry.getValue(0);
                JComboboxItem selectedItem = (JComboboxItem) addresses.getSelectedItem();

                if (StringUtils.isBlank(address) || selectedItem == null) {
                    return false;
                } else {
                    return address.equals(selectedItem.getKey());
                }
            }
        };

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>();
        sorter.setRowFilter(filter);
        sorter.setModel(model);
        table.setRowSorter(sorter);
    }

    public void setAddresses(JComboBox<JComboboxItem> addresses) {
        this.addresses = addresses;
        this.addresses.addActionListener(this);
    }

    public void update(Addresses addresses) {
        if (addresses != null && !addresses.getAddresses().isEmpty()){
            this.addresses.removeAllItems();
            for (String address : addresses.getAddresses()) {
                JComboboxItem item = new JComboboxItem(address, address);
                this.addresses.addItem(item);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("comboBoxChanged")) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (table != null && table.getRowSorter() != null) {
                        LOGGER.info("comboBoxChanged");
                        table.getRowSorter().allRowsChanged();
                    }
                }
            });
        }
    }

}
