/**
 * EasyDccMonFrameTest.java
 *
 * Description:	    JUnit tests for the EasyDccProgrammer class
 * @author			Bob Jacobsen
 * @version
 */

package jmri.jmrix.easydcc.easydccmon;

import jmri.*;

import java.util.*;

import junit.framework.Test;
import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import jmri.jmrix.easydcc.*;

public class EasyDccMonFrameTest extends TestCase {

	public void testCreate() {
		EasyDccMonFrame f = new EasyDccMonFrame();
	}

	public void testMsg() {
		EasyDccMessage m = new EasyDccMessage(3);
		m.setOpCode('L');
		m.setElement(1, '0');
		m.setElement(2, 'A');

		EasyDccMonFrame f = new EasyDccMonFrame();

		f.message(m);

		Assert.assertEquals("length ", "cmd: \"L0A\"\n".length(), f.getFrameText().length());
		Assert.assertEquals("display", "cmd: \"L0A\"\n", f.getFrameText());
	}

	public void testReply() {
		EasyDccReply m = new EasyDccReply();
		m.setOpCode('C');
		m.setElement(1, 'o');
		m.setElement(2, ':');

		EasyDccMonFrame f = new EasyDccMonFrame();

		f.reply(m);

		Assert.assertEquals("display", "rep: \"Co:\"\n", f.getFrameText());
		Assert.assertEquals("length ", "rep: \"Co:\"\n".length(), f.getFrameText().length());
	}

	public void testWrite() throws JmriException {

		// infrastructure objects
		EasyDccInterfaceScaffold t = new EasyDccInterfaceScaffold();

	}

	// service internal class to handle transmit/receive for tests
	class EasyDccInterfaceScaffold extends EasyDccTrafficController {
		public EasyDccInterfaceScaffold() {
		}

		// override some EasyDccInterfaceController methods for test purposes

		public boolean status() { return true;
		}

		/**
	 	* record messages sent, provide access for making sure they are OK
	 	*/
		public Vector outbound = new Vector();  // public OK here, so long as this is a test class
		public void sendEasyDccMessage(EasyDccMessage m, EasyDccListener l) {
			if (log.isDebugEnabled()) log.debug("sendEasyDccMessage ["+m+"]");
			// save a copy
			outbound.addElement(m);
		}

		// test control member functions

		/**
		 * forward a message to the listeners, e.g. test receipt
		 */
		protected void sendTestMessage (EasyDccMessage m) {
			// forward a test message to Listeners
			if (log.isDebugEnabled()) log.debug("sendTestMessage    ["+m+"]");
			notifyMessage(m, null);
			return;
		}
		protected void sendTestReply (EasyDccReply m) {
			// forward a test message to Listeners
			if (log.isDebugEnabled()) log.debug("sendTestReply    ["+m+"]");
			notifyReply(m);
			return;
		}

		/*
		* Check number of listeners, used for testing dispose()
		*/
		public int numListeners() {
			return cmdListeners.size();
		}

	}

	// from here down is testing infrastructure

	public EasyDccMonFrameTest(String s) {
		super(s);
	}

	// Main entry point
	static public void main(String[] args) {
		String[] testCaseName = {EasyDccMonFrameTest.class.getName()};
		junit.swingui.TestRunner.main(testCaseName);
	}

	// test suite from all defined tests
	public static Test suite() {
		TestSuite suite = new TestSuite(EasyDccMonFrameTest.class);
		return suite;
	}

	static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(EasyDccMonFrameTest.class.getName());

}
