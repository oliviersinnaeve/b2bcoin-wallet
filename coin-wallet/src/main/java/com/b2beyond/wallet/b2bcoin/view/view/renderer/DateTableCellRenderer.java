package com.b2beyond.wallet.b2bcoin.view.view.renderer;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.text.NumberFormat;
import java.util.Date;


public class DateTableCellRenderer extends DefaultTableCellRenderer {

    private static final NumberFormat FORMAT = NumberFormat.getCurrencyInstance();

    @Override
    public final Component getTableCellRendererComponent(JTable table, Object value,
                                                         boolean isSelected, boolean hasFocus, int row, int column) {
        final Component result = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        if (value instanceof Long) {
            Long text = (Long) value;
            setText(new Date(text).toString());
        }

        return result;
    }

}
