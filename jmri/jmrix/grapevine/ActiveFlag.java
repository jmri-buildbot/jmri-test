// ActiveFlag.java

package jmri.jmrix.grapevine;

/**
 * Provide a flag to indicate that the system provided by
 * this package is active.
 * <P>
 * This is a very light-weight class, carrying only the flag,
 * so as to limit the number of unneeded class loadings.
 *
 * @author		Bob Jacobsen  Copyright (C) 2003, 2006, 2007
 * @version             $Revision: 1.1 $
 */
abstract public class ActiveFlag {

    static private boolean flag = false;
    static public void setActive() {
        flag = true;
    }
    static public boolean isActive() {
        return flag;
    }
}


/* @(#)AbstractMRReply.java */
