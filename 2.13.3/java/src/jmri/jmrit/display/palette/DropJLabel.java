package jmri.jmrit.display.palette;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.JLabel;

import jmri.jmrit.catalog.ImageIndexEditor;
import jmri.jmrit.catalog.NamedIcon;

/**
* This file is part of JMRI
* @author Pete Cressman  Copyright (c) 2011
*/


public class DropJLabel extends JLabel implements DropTargetListener {
    DataFlavor dataFlavor;
    protected Hashtable <String, NamedIcon>   _iconMap;
    
    
    DropJLabel (Icon icon) {
        super(icon);
        try {
            dataFlavor = new DataFlavor(ImageIndexEditor.IconDataFlavorMime);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        //if (log.isDebugEnabled()) log.debug("DropJLabel ctor");
    }
    DropJLabel (Icon icon,  Hashtable <String, NamedIcon> iconMap) {
    	this(icon);
    	_iconMap = iconMap;
    }

    public void dragExit(DropTargetEvent dte) {
        //if (log.isDebugEnabled()) log.debug("DropJLabel.dragExit ");
    }
    public void dragEnter(DropTargetDragEvent dtde) {
        //if (log.isDebugEnabled()) log.debug("DropJLabel.dragEnter ");
    }
    public void dragOver(DropTargetDragEvent dtde) {
        //if (log.isDebugEnabled()) log.debug("DropJLabel.dragOver ");
    }
    public void dropActionChanged(DropTargetDragEvent dtde) {
        //if (log.isDebugEnabled()) log.debug("DropJLabel.dropActionChanged ");
    }
    public void drop(DropTargetDropEvent e) {
        try {
            Transferable tr = e.getTransferable();
            if(e.isDataFlavorSupported(dataFlavor)) {
                NamedIcon newIcon = new NamedIcon((NamedIcon)tr.getTransferData(dataFlavor));
                accept(e, newIcon);
            } else if(e.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String)tr.getTransferData(DataFlavor.stringFlavor);
                if (log.isDebugEnabled()) log.debug("drop for stringFlavor "+text);
                NamedIcon newIcon = new NamedIcon(text, text);
                accept(e, newIcon);
            } else {
                if (log.isDebugEnabled()) log.debug("DropJLabel.drop REJECTED!");
                e.rejectDrop();
            }
        } catch(IOException ioe) {
            if (log.isDebugEnabled()) log.debug("DropPanel.drop REJECTED!");
            e.rejectDrop();
        } catch(UnsupportedFlavorException ufe) {
            if (log.isDebugEnabled()) log.debug("DropJLabel.drop REJECTED!");
            e.rejectDrop();
        }
    }
    private void accept(DropTargetDropEvent e, NamedIcon newIcon) {
        e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        DropTarget target = (DropTarget)e.getSource();
        DropJLabel label = (DropJLabel)target.getComponent();
        if (log.isDebugEnabled()) log.debug("accept drop for "+label.getName()+
                                             ", "+newIcon.getURL());
        if (newIcon==null || newIcon.getIconWidth()<1 || newIcon.getIconHeight()<1) {
            label.setText(ItemPalette.rbp.getString("invisibleIcon"));
            label.setForeground(Color.lightGray);
        } else {
            newIcon.reduceTo(100, 100, 0.2);
            label.setText(null);
        }
        label.setIcon(newIcon);
//        _catalog.setBackground(label);
        _iconMap.put(label.getName(), newIcon);
        e.dropComplete(true);
        if (log.isDebugEnabled()) log.debug("DropJLabel.drop COMPLETED for "+label.getName()+
                                             ", "+(newIcon!=null ? newIcon.getURL().toString():" newIcon==null "));
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DropJLabel.class.getName());
}
