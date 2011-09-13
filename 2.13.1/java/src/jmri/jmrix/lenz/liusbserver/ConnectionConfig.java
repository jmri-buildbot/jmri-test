// ConnectionConfig.java

package jmri.jmrix.lenz.liusbserver;

import javax.swing.JPanel;
import javax.swing.JTextField;

import jmri.jmrix.JmrixConfigPane;


/**
 * Handle configuring an XPressNet layout connection
 * via a LIUSB Server.
 * <P>
 * This uses the {@link LIUSBServerAdapter} class to do the actual
 * connection.
 *
 * @author	Paul Bender Copyright (C) 2009
 * @version	$Revision$
 *
 * @see LIUSBServerAdapter
 */
public class ConnectionConfig  extends jmri.jmrix.AbstractNetworkConnectionConfig {

    javax.swing.JComboBox portBox = new javax.swing.JComboBox();



    /**
     * Ctor for an object being created during load process;
     * Swing init is deferred.
     */
    public ConnectionConfig(jmri.jmrix.NetworkPortAdapter p){
        super(p);

    }
    /**
     * Ctor for a functional Swing object with no prexisting adapter
     */
    public ConnectionConfig() {
	super();
    }

    public String name() { return "Lenz LIUSB Server"; }

    /**
     * Load the adapter with an appropriate object
     * <i>unless</i> it has already been set.
     */
    protected void setInstance() { if(adapter==null) adapter = new LIUSBServerAdapter(); }

    public String getInfo() {
        String t = (String)portBox.getSelectedItem();
        if (t!=null) return t;
        else return JmrixConfigPane.NONE;
    }

    public void loadDetails(JPanel details) {
     	super.loadDetails(details);
        hostNameField.setText(LIUSBServerAdapter.DEFAULT_IP_ADDRESS);
	hostNameField.setEnabled(false); // we can't change this now.
	portFieldLabel.setText("Communication Port");
	portField.setText(String.valueOf(LIUSBServerAdapter.COMMUNICATION_TCP_PORT));
	portField.setEnabled(false); // we can't change this now.
	opt1Box.setEnabled(false); // we can't change this now.
	
    }

        protected JTextField bcastPortField = new JTextField(String.valueOf(LIUSBServerAdapter.BROADCAST_TCP_PORT));

}
