/** 
 * LocoNetInterfaceScaffold.java
 *
 * Description:	 	Test scaffold implementation of LocoNetInterface
 * @author			Bob Jacobsen Copyright (C) 2001
 * @version			
 */

package jmri.tests.jmrix.loconet;

import jmri.jmrix.loconet.LnTrafficController;
import jmri.jmrix.loconet.LocoNetListener;
import jmri.jmrix.loconet.LocoNetMessage;

import java.util.Vector;

public class LocoNetInterfaceScaffold extends LnTrafficController {

	public LocoNetInterfaceScaffold() {
		self = this;
	}

	// override some LnTrafficController methods for test purposes
	
	public boolean status() { return true; 
	}

	/**
	 * record LocoNet messages sent, provide access for making sure they are OK
	 */
	public Vector outbound = new Vector();  // public OK here, so long as this is a test class
	public void sendLocoNetMessage(LocoNetMessage m) {
		if (log.isDebugEnabled()) log.debug("sendLocoNetMessage ["+m+"]");
		// save a copy
		outbound.addElement(m);
		// forward as an echo
		notify(m);
	}

	// test control member functions
	
	/** 
	 * forward a message to the listeners, e.g. test receipt
	 */
	protected void sendTestMessage (LocoNetMessage m) {
		// forward a test message to LocoNetListeners
		if (log.isDebugEnabled()) log.debug("sendTestMessage    ["+m+"]");
		notify(m);
		return;
	}
	
	static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(LocoNetInterfaceScaffold.class.getName());

}


/* @(#)LocoNetInterfaceScaffold.java */
