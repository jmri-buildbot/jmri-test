// LocationsTableModel.java
package jmri.jmrit.operations.locations;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import jmri.jmrit.operations.setup.Control;
import jmri.util.table.ButtonEditor;
import jmri.util.table.ButtonRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Table Model for edit of locations used by operations
 *
 * @author Daniel Boudreau Copyright (C) 2008, 2013
 * @version $Revision$
 */
public class LocationsTableModel extends javax.swing.table.AbstractTableModel implements PropertyChangeListener {

    /**
     *
     */
    private static final long serialVersionUID = 8385333181895879131L;

    LocationManager locationManager; // There is only one manager

    // Defines the columns
    public static final int IDCOLUMN = 0;
    public static final int NAMECOLUMN = 1;
    public static final int TRACKCOLUMN = 2;
    public static final int LENGTHCOLUMN = 3;
    public static final int USEDLENGTHCOLUMN = 4;
    public static final int ROLLINGSTOCK = 5;
    public static final int PICKUPS = 6;
    public static final int DROPS = 7;
    public static final int ACTIONCOLUMN = 8;
    public static final int EDITCOLUMN = 9;

    private static final int HIGHESTCOLUMN = EDITCOLUMN + 1;

    public LocationsTableModel() {
        super();
        locationManager = LocationManager.instance();
        locationManager.addPropertyChangeListener(this);
        updateList();
    }

    public final int SORTBYNAME = 1;
    public final int SORTBYID = 2;

    private int _sort = SORTBYNAME;

    public void setSort(int sort) {
        synchronized (this) {
            _sort = sort;
        }
        updateList();
        fireTableDataChanged();
    }

    private synchronized void updateList() {
        // first, remove listeners from the individual objects
        removePropertyChangeLocations();

        if (_sort == SORTBYID) {
            locationsList = locationManager.getLocationsByIdList();
        } else {
            locationsList = locationManager.getLocationsByNameList();
        }
        // and add them back in
        for (Location loc : locationsList) {
            loc.addPropertyChangeListener(this);
        }
    }

    List<Location> locationsList = null;

    void initTable(JTable table) {
        // Install the button handlers
        TableColumnModel tcm = table.getColumnModel();
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        TableCellEditor buttonEditor = new ButtonEditor(new javax.swing.JButton());
        tcm.getColumn(ACTIONCOLUMN).setCellRenderer(buttonRenderer);
        tcm.getColumn(ACTIONCOLUMN).setCellEditor(buttonEditor);
        tcm.getColumn(EDITCOLUMN).setCellRenderer(buttonRenderer);
        tcm.getColumn(EDITCOLUMN).setCellEditor(buttonEditor);
        // set column preferred widths
        table.getColumnModel().getColumn(IDCOLUMN).setPreferredWidth(40);
        table.getColumnModel().getColumn(NAMECOLUMN).setPreferredWidth(200);
        table.getColumnModel().getColumn(TRACKCOLUMN).setPreferredWidth(
                Math.max(60, new JLabel(Bundle.getMessage("Class/Interchange") + Bundle.getMessage("Spurs")
                        + Bundle.getMessage("Yards")).getPreferredSize().width + 20));
        table.getColumnModel().getColumn(LENGTHCOLUMN).setPreferredWidth(
                Math.max(60, new JLabel(getColumnName(LENGTHCOLUMN)).getPreferredSize().width + 10));
        table.getColumnModel().getColumn(USEDLENGTHCOLUMN).setPreferredWidth(60);
        table.getColumnModel().getColumn(ROLLINGSTOCK).setPreferredWidth(
                Math.max(80, new JLabel(getColumnName(ROLLINGSTOCK)).getPreferredSize().width + 10));
        table.getColumnModel().getColumn(PICKUPS).setPreferredWidth(
                Math.max(60, new JLabel(getColumnName(PICKUPS)).getPreferredSize().width + 10));
        table.getColumnModel().getColumn(DROPS).setPreferredWidth(
                Math.max(60, new JLabel(getColumnName(DROPS)).getPreferredSize().width + 10));
        table.getColumnModel().getColumn(ACTIONCOLUMN).setPreferredWidth(
                Math.max(80, new JLabel(Bundle.getMessage("Yardmaster")).getPreferredSize().width + 40));
        table.getColumnModel().getColumn(EDITCOLUMN).setPreferredWidth(80);
        // have to shut off autoResizeMode to get horizontal scroll to work (JavaSwing p 541)
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public synchronized int getRowCount() {
        return locationsList.size();
    }

    public int getColumnCount() {
        return HIGHESTCOLUMN;
    }

    public String getColumnName(int col) {
        switch (col) {
            case IDCOLUMN:
                return Bundle.getMessage("Id");
            case NAMECOLUMN:
                return Bundle.getMessage("Name");
            case TRACKCOLUMN:
                return Bundle.getMessage("Track");
            case LENGTHCOLUMN:
                return Bundle.getMessage("Length");
            case USEDLENGTHCOLUMN:
                return Bundle.getMessage("Used");
            case ROLLINGSTOCK:
                return Bundle.getMessage("RollingStock");
            case PICKUPS:
                return Bundle.getMessage("Pickups");
            case DROPS:
                return Bundle.getMessage("Drop");
            case ACTIONCOLUMN:
                return Bundle.getMessage("Action");
            case EDITCOLUMN:
                return Bundle.getMessage("Edit"); // edit column
            default:
                return "unknown"; // NOI18N
        }
    }

    public Class<?> getColumnClass(int col) {
        switch (col) {
            case IDCOLUMN:
                return String.class;
            case NAMECOLUMN:
                return String.class;
            case TRACKCOLUMN:
                return String.class;
            case LENGTHCOLUMN:
                return String.class;
            case USEDLENGTHCOLUMN:
                return String.class;
            case ROLLINGSTOCK:
                return String.class;
            case PICKUPS:
                return String.class;
            case DROPS:
                return String.class;
            case ACTIONCOLUMN:
                return JButton.class;
            case EDITCOLUMN:
                return JButton.class;
            default:
                return null;
        }
    }

    public boolean isCellEditable(int row, int col) {
        switch (col) {
            case EDITCOLUMN:
            case ACTIONCOLUMN:
                return true;
            default:
                return false;
        }
    }

    public synchronized Object getValueAt(int row, int col) {
        if (row >= getRowCount()) {
            return "ERROR row " + row; // NOI18N
        }
        Location l = locationsList.get(row);
        if (l == null) {
            return "ERROR location unknown " + row; // NOI18N
        }
        switch (col) {
            case IDCOLUMN:
                return l.getId();
            case NAMECOLUMN:
                return l.getName();
            case TRACKCOLUMN:
                return getTrackTypes(l);
            case LENGTHCOLUMN:
                return Integer.toString(l.getLength());
            case USEDLENGTHCOLUMN:
                return Integer.toString(l.getUsedLength());
            case ROLLINGSTOCK:
                return Integer.toString(l.getNumberRS());
            case PICKUPS:
                return Integer.toString(l.getPickupRS());
            case DROPS:
                return Integer.toString(l.getDropRS());
            case ACTIONCOLUMN:
                return Bundle.getMessage("Yardmaster");
            case EDITCOLUMN:
                return Bundle.getMessage("Edit");
            default:
                return "unknown " + col; // NOI18N
        }
    }

    private String getTrackTypes(Location location) {
        if (location.isStaging()) {
            return (Bundle.getMessage("Staging"));
        } else {
            StringBuffer sb = new StringBuffer();
            if (location.hasInterchanges()) {
                sb.append(Bundle.getMessage("Class/Interchange") + " ");
            }
            if (location.hasSpurs()) {
                sb.append(Bundle.getMessage("Spurs") + " ");
            }
            if (location.hasYards()) {
                sb.append(Bundle.getMessage("Yards"));
            }
            return sb.toString();
        }
    }

    public void setValueAt(Object value, int row, int col) {
        switch (col) {
            case ACTIONCOLUMN:
                launchYardmaster(row);
                break;
            case EDITCOLUMN:
                editLocation(row);
                break;
            default:
                break;
        }
    }

    List<LocationEditFrame> frameList = new ArrayList<LocationEditFrame>();

    private synchronized void editLocation(int row) {
        // use invokeLater so new window appears on top
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Location loc = locationsList.get(row);
                log.debug("Edit location ({})", loc.getName());
                for (LocationEditFrame lef : frameList) {
                    if (lef._location == loc) {
                        lef.dispose();
                        frameList.remove(lef);
                        break;
                    }
                }
                LocationEditFrame lef = new LocationEditFrame(loc);
                frameList.add(lef);
            }
        });
    }

    private synchronized void launchYardmaster(int row) {
        // use invokeLater so new window appears on top
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                log.debug("Yardmaster");
                Location loc = locationsList.get(row);
                new YardmasterFrame(loc);
            }
        });
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (Control.showProperty) {
            log.debug("Property change: ({}) old: ({}) new: ({})", e.getPropertyName(), e.getOldValue(), e
                    .getNewValue());
        }
        if (e.getPropertyName().equals(LocationManager.LISTLENGTH_CHANGED_PROPERTY)) {
            updateList();
            fireTableDataChanged();
        } else if (e.getSource().getClass().equals(Location.class)) {
            Location loc = (Location) e.getSource();
            int row = locationsList.indexOf(loc);
            if (Control.showProperty) {
                log.debug("Update location table row: {} name: {}", row, loc.getName());
            }
            if (row >= 0) {
                fireTableRowsUpdated(row, row);
            }
        }
    }

    private synchronized void removePropertyChangeLocations() {
        if (locationsList != null) {
            for (Location loc : locationsList) {
                loc.removePropertyChangeListener(this);
            }
        }
    }

    public void dispose() {
        if (log.isDebugEnabled()) {
            log.debug("dispose");
        }
        for (LocationEditFrame lef : frameList) {
            lef.dispose();
        }
        locationManager.removePropertyChangeListener(this);
        removePropertyChangeLocations();
    }

    static Logger log = LoggerFactory.getLogger(LocationsTableModel.class.getName());
}
