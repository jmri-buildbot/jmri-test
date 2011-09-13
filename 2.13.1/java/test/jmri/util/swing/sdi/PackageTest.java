// PackageTest.java

package jmri.util.swing.sdi;

import junit.framework.*;
import javax.swing.*;
import jmri.util.*;
import jmri.util.swing.*;

/**
 * Invokes complete set of tests in the jmri.util.swing.sdi tree
 *
 * @author	    Bob Jacobsen  Copyright 2010
 * @version         $Revision$
 */
public class PackageTest extends TestCase {
    
    public void testAction() {
        JmriJFrame f = new JmriJFrame("SDI test");
        JButton b = new JButton(new ButtonTestAction(
                        "new frame", new jmri.util.swing.sdi.JmriJFrameInterface()));
        f.add(b);
        f.pack();
        f.setVisible(true);
    }
    
    // from here down is testing infrastructure
    public PackageTest(String s) {
        super(s);
    }

    // Main entry point
    static public void main(String[] args) {
        String[] testCaseName = {"-noloading", PackageTest.class.getName()};
        junit.swingui.TestRunner.main(testCaseName);
    }

    // test suite from all defined tests
    public static Test suite() {
        TestSuite suite = new TestSuite(PackageTest.class); 

       suite.addTest(SdiJfcUnitTests.suite());

        return suite;
    }

    // The minimal setup for log4J
    protected void setUp() { apps.tests.Log4JFixture.setUp(); }
    protected void tearDown() { apps.tests.Log4JFixture.tearDown(); }

}
