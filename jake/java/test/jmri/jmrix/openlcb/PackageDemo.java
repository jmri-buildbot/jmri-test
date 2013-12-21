// PackageDemo.java

package jmri.jmrix.openlcb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Demos for the jmri.jmrix.openlcb.swing package.
 * @author      Bob Jacobsen  Copyright 2012
 * @version   $Revision$
 */
public class PackageDemo extends TestCase {

    public void testDefinitions() {
    }
    
    // from here down is testing infrastructure

    public PackageDemo(String s) {
        super(s);
    }

    // Main entry point
    static public void main(String[] args) {
        apps.tests.AllTest.initLogging();
        String[] testCaseName = {"-noloading", PackageDemo.class.getName()};
        junit.swingui.TestRunner.main(testCaseName);
    }

    // test suite from all defined tests
    public static Test suite() {
        TestSuite suite = new TestSuite("jmri.jmrix.openlcb.PackageDemo");

        suite.addTest(jmri.jmrix.openlcb.swing.PackageDemo.suite());
        
        return suite;
    }

    // The minimal setup for log4J
    protected void setUp() { apps.tests.Log4JFixture.setUp(); }
    protected void tearDown() { apps.tests.Log4JFixture.tearDown(); }
}
