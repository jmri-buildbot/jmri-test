// PricomTest.java

package jmri.jmrix.pricom;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the jmri.jmrix.pricom package.
 * @author      Bob Jacobsen  Copyright 2005
 * @version   $Revision: 1.1 $
 */
public class PricomTest extends TestCase {

    // from here down is testing infrastructure

    public PricomTest(String s) {
        super(s);
    }

    // Main entry point
    static public void main(String[] args) {
        String[] testCaseName = {PricomTest.class.getName()};
        junit.swingui.TestRunner.main(testCaseName);
    }

    // test suite from all defined tests
    public static Test suite() {
        apps.tests.AllTest.initLogging();
        TestSuite suite = new TestSuite("jmri.jmrix.pricom.PricomTest");
        suite.addTest(jmri.jmrix.pricom.pockettester.PocketTesterTest.suite());
        return suite;
    }

}
