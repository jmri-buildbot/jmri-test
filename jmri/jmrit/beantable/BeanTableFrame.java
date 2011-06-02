// BeanTableFrame.java

package jmri.jmrit.beantable;

import java.util.ResourceBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;

import jmri.util.com.sun.TableSorter;



/**
 * Provide a JFrame to display a table of NamedBeans.
 * <P>
 * This frame includes the table itself at the top,
 * plus a "bottom area" for things like an Add... button
 * and checkboxes that control display options.
 * <p>
 * The usual menus are also provided here.
 * <p>
 * Specific uses are customized via the BeanTableDataModel
 * implementation they provide, and by 
 * providing a {@link #extras} implementation
 * that can in turn invoke {@link #addToBottomBox} as needed.
 * 
 * @author	Bob Jacobsen   Copyright (C) 2003
 * @version	$Revision: 1.32 $
 */
public class BeanTableFrame extends jmri.util.JmriJFrame {

    BeanTableDataModel		dataModel;
    JTable			dataTable;
    JScrollPane 		dataScroll;
    Box bottomBox;		// panel at bottom for extra buttons etc
    int bottomBoxIndex;	// index to insert extra stuff
    static final int bottomStrutWidth = 20;

    ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrit.beantable.BeanTableBundle");
    ResourceBundle rbapps = ResourceBundle.getBundle("apps.AppsBundle");
    
    public BeanTableFrame(){
        super();
    }

    public BeanTableFrame(String s){
        super(s);
    }
    
    public BeanTableFrame(BeanTableDataModel model, String helpTarget, JTable dataTab) {

        super();
        dataModel 	= model;
        this.dataTable = dataTab;

        dataScroll	= new JScrollPane(dataTable);

        // give system name column as smarter sorter and use it initially
        try {
            TableSorter tmodel = ((TableSorter)dataTable.getModel());
            tmodel.setColumnComparator(String.class, new jmri.util.SystemNameComparator());
            tmodel.setSortingStatus(BeanTableDataModel.SYSNAMECOL, TableSorter.ASCENDING);
        } catch (java.lang.ClassCastException e) {}  // happens if not sortable table
        
        // configure items for GUI
        dataModel.configureTable(dataTable);

        // general GUI config
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // add save menu item
        JMenuBar menuBar = new JMenuBar();        
        JMenu fileMenu = new JMenu(rbapps.getString("MenuFile"));
        menuBar.add(fileMenu);
        fileMenu.add(new jmri.configurexml.SaveMenu());
        
        JMenuItem printItem = new JMenuItem(rbapps.getString("PrintTable"));
        fileMenu.add(printItem);
        printItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // MessageFormat headerFormat = new MessageFormat(getTitle());  // not used below
                        MessageFormat footerFormat = new MessageFormat(getTitle()+" page {0,number}");
                        dataTable.print(JTable.PrintMode.FIT_WIDTH , null, footerFormat);
                    } catch (java.awt.print.PrinterException e1) {
                        log.warn("error printing: "+e1,e1);
                    }
                }
        });

        setJMenuBar(menuBar);

        addHelpMenu(helpTarget,true);

        // install items in GUI
        getContentPane().add(dataScroll);
        bottomBox = Box.createHorizontalBox();
        bottomBox.add(Box.createHorizontalGlue());	// stays at end of box
        bottomBoxIndex = 0;
        
        getContentPane().add(bottomBox);
        
        // add extras, if desired by subclass
        extras();

        // set Viewport preferred size from size of table
        java.awt.Dimension dataTableSize = dataTable.getPreferredSize();
        // width is right, but if table is empty, it's not high
        // enough to reserve much space.
        dataTableSize.height = Math.max(dataTableSize.height, 400);
        dataScroll.getViewport().setPreferredSize(dataTableSize);
 	    
        // set preferred scrolling options
        dataScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        dataScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    }

    /**
     * Hook to allow sub-types to install more items in GUI
     */
	void extras() {}
	    
    /**
     * Hook to allow sub-typing of JTable created
     */
/*	protected JTable makeJTable(TableSorter sorter) {
	    return new JTable(sorter)  {
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                boolean res = super.editCellAt(row, column, e);
                java.awt.Component c = this.getEditorComponent();
                if (c instanceof javax.swing.JTextField) {
                    ( (JTextField) c).selectAll();
                }            
                return res;
            }
        };
    }*/
	    
    protected Box getBottomBox() { return bottomBox; }
    /**
     * Add a component to the bottom box. Takes care of organising glue, struts etc
     * @param comp
     * @param c
     */
    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value="UUF_UNUSED_FIELD", 
          justification="param c is required in the listedtableframe")
    protected void addToBottomBox(Component comp, String c) {
    	bottomBox.add(Box.createHorizontalStrut(bottomStrutWidth), bottomBoxIndex);
    	++bottomBoxIndex;
    	bottomBox.add(comp, bottomBoxIndex);
    	++bottomBoxIndex;
    }
    
    public void dispose() {
        if (dataModel != null)
            dataModel.dispose();
        dataModel = null;
        dataTable = null;
        dataScroll = null;
        super.dispose();
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BeanTableFrame.class.getName());
}
