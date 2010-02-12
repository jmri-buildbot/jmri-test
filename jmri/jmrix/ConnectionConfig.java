// ConnectionConfig.java

package jmri.jmrix;

import javax.swing.JPanel;

/**
 * Definition of objects to handle configuring a layout connection.
 *
 * @author      Bob Jacobsen   Copyright (C) 2001, 2003
 * @version	$Revision: 1.4 $
 * @see JmrixConfigPane
 * @see AbstractPortController
 */
public interface ConnectionConfig  {

    public String name();
    public void loadDetails(JPanel details);
    public String getInfo();
    
    public String getManufacturer();
    public void setManufacturer(String Manufacturer);

}

