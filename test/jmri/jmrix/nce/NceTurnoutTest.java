/**
 * NceTurnoutTest.java
 *
 * Description:	    tests for the jmri.jmrix.nce.NceTurnout class
 * @author			Bob Jacobsen
 * @version			$Revision: 1.3 $
 */

package jmri.jmrix.nce;

import jmri.AbstractTurnoutTest;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

public class NceTurnoutTest extends AbstractTurnoutTest {

	private NceTrafficControlScaffold tcis = null;

	public void setUp() {
		// prepare an interface
		tcis = new NceTrafficControlScaffold();

		t = new NceTurnout(4);
	}

	public int numListeners() { return tcis.numListeners(); }

	public void checkThrownMsgSent() {
		Assert.assertTrue("message sent", tcis.outbound.size()>0);
		Assert.assertEquals("content", "S C02 81 fe 7f", tcis.outbound.elementAt(tcis.outbound.size()-1).toString());  // THROWN message
	}

	public void checkClosedMsgSent() {
		Assert.assertTrue("message sent", tcis.outbound.size()>0);
		Assert.assertEquals("content", "S C02 81 ff 7e", tcis.outbound.elementAt(tcis.outbound.size()-1).toString());  // CLOSED message
	}

	// from here down is testing infrastructure

	public NceTurnoutTest(String s) {
		super(s);
	}

	// Main entry point
	static public void main(String[] args) {
		String[] testCaseName = {NceTurnoutTest.class.getName()};
		junit.swingui.TestRunner.main(testCaseName);
	}

	// test suite from all defined tests
	public static Test suite() {
		TestSuite suite = new TestSuite(NceTurnoutTest.class);
		return suite;
	}

	 static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(NceTurnoutTest.class.getName());

}
