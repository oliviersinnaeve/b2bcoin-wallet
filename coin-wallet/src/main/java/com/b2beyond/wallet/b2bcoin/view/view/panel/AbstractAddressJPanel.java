package com.b2beyond.wallet.b2bcoin.view.view.panel;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.view.model.JComboboxItem;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by oliviersinnaeve on 14/09/17.
 */
public abstract class AbstractAddressJPanel extends JPanel implements ActionListener {

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
        sorter.setComparator(1, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                try {
                    Date date1 = writeFormat.parse(o1);
                    Date date2 = writeFormat.parse(o2);
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });
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
