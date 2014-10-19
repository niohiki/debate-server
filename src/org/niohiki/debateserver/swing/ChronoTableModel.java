package org.niohiki.debateserver.swing;

import java.util.HashMap;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;
import org.niohiki.debateserver.Locale;
import org.niohiki.debateserver.chronometer.Chronometer;

public class ChronoTableModel extends AbstractTableModel {

    private final HashMap<String, Chronometer> chronometers;
    private final Locale locale;

    public ChronoTableModel(Locale loc, HashMap<String, Chronometer> chronos) {
        chronometers = chronos;
        locale = loc;
    }

    @Override
    public int getRowCount() {
        return chronometers.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int i) {
        if (i == 0) {
            return locale.chronoInfo.chronoName;
        } else if (i == 1) {
            return locale.chronoInfo.chronoID;
        } else {
            return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String key = getKey(rowIndex, chronometers.keySet().iterator());
        if (columnIndex == 0) {
            return chronometers.get(key).toString();
        } else if (columnIndex == 1) {
            return key;
        } else {
            return "";
        }
    }

    private String getKey(int i, Iterator<String> it) {
        String key = it.next();
        if (i == 0) {
            return key;
        } else {
            return getKey(i - 1, it);
        }
    }
}
