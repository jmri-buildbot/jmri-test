// AutomatTableFrame.java

package jmri.jmrit.automat.monitor;

import java.awt.Dimension;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.*;

import jmri.util.JTableUtil;

/**
 * Frame providing a table of Automat instances
 *
 * @author	Bob Jacobsen   Copyright (C) 2004
 * @version	$Revision: 1.6 $
 */
public class AutomatTableFrame extends jmri.util.JmriJFrame {

    AutomatTableDataModel	dataModel;
    JTable			dataTable;
    JScrollPane 		dataScroll;

    static ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrit.automat.monitor.AutomatTableBundle");

    public AutomatTableFrame(AutomatTableDataModel model) {

        super();
        dataModel 	= model;

        dataTable	= JTableUtil.sortableDataModel(dataModel);
        dataScroll	= new JScrollPane(dataTable);

        // configure items for GUI

        dataModel.configureTable(dataTable);

        // general GUI config
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // install items in GUI
        JPanel pane1 = new JPanel();
        getContentPane().add(dataScroll);
        pack();
        pane1.setMaximumSize(pane1.getSize());

		setTitle(rb.getString("TitleAutomatTable"));

        addHelpMenu("package.jmri.jmrit.automat.monitor.AutomatTableFrame", true);

        pack();
    }

    public void dispose() {
        dataModel.dispose();
        dataModel = null;
        dataTable = null;
        dataScroll = null;
        super.dispose();
    }
}
