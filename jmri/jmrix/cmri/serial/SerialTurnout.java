// SerialTurnout.java

package jmri.jmrix.cmri.serial;

import jmri.AbstractTurnout;
import jmri.Turnout;
import jmri.NmraPacket;

/**
 * SerialTurnout.java
 *
 *  This object doesn't listen to the C/MRI communications.  This is because
 *  it should be the only object that is sending messages for this turnout;
 *  more than one Turnout object pointing to a single device is not allowed.
 *
 * Description:		extend jmri.AbstractTurnout for C/MRI serial layouts
 * @author			Bob Jacobsen Copyright (C) 2003
 * @version			$Revision: 1.4 $
 */
public class SerialTurnout extends AbstractTurnout {

    /**
     * Create a Turnout object, with both system and user names.
     * <P>
     * 'systemName' was previously validated in SerialTurnoutManager
     */
    public SerialTurnout(String systemName, String userName) {
        super(systemName, userName);
        // Extract the Node from the name
        tNode = SerialAddress.getNodeFromSystemName(systemName);
        // Extract the Bit from the name
        tBit = SerialAddress.getBitFromSystemName(systemName);
    }

    /**
     * Handle a request to change state by sending a turnout command
     */
    protected void forwardCommandChangeToLayout(int s) {
        // implementing classes will typically have a function/listener to get
        // updates from the layout, which will then call
        //		public void firePropertyChange(String propertyName,
        //				                Object oldValue,
        //						Object newValue)
        // _once_ if anything has changed state (or set the commanded state directly)

        // sort out states
        if ( (s & Turnout.CLOSED) > 0) {
            // first look for the double case, which we can't handle
            if ( (s & Turnout.THROWN) > 0) {
                // this is the disaster case!
                log.error("Cannot command both CLOSED and THROWN "+s);
                return;
            } else {
                // send a CLOSED command
                sendMessage(true);
            }
        } else {
            // send a THROWN command
            sendMessage(false);
        }
    }

    public void dispose() {}  // no connections need to be broken

    // data members
    SerialNode tNode;  // Serial Node used to control turnout
    int tBit;          // bit number of turnout control in Serial node

    protected void sendMessage(boolean closed) {
        tNode.setOutputBit(tBit, closed);
    }

    static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(SerialTurnout.class.getName());
}

/* @(#)SerialTurnout.java */
