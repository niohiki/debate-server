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
        return 5;
    }

    @Override
    public String getColumnName(int i) {
        if (i == 0) {
            return locale.chronoInfo.chronoName;
        } else if (i == 1) {
            return locale.chronoInfo.chronoID;
        } else if (i == 2) {
            return locale.chronoInfo.stance;
        } else if (i == 3) {
            return locale.chronoInfo.mainRunning;
        } else if (i == 4) {
            return locale.chronoInfo.secondaryRunning;
        } else {
            return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String key = getKey(rowIndex, chronometers.keySet().iterator());
        if (columnIndex == 0) {
            return chronometers.get(key).fullName();
        } else if (columnIndex == 1) {
            return key;
        } else if (columnIndex == 2) {
            return chronometers.get(key).stance();
        } else if (columnIndex == 3) {
            return chronometers.get(key).isMainRunning();
        } else if (columnIndex == 4) {
            return chronometers.get(key).isSecondaryRunning();
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
