/* XNetProgrammerManager.java */

package jmri.jmrix.lenz;

import jmri.managers.DefaultProgrammerManager;
import jmri.Programmer;

/**
 * Extend DefaultProgrammerManager to provide ops mode programmers on XPressNet
 *
 * @see         jmri.ProgrammerManager
 * @author	Paul Bender Copyright (C) 2003
 * @version	$Revision: 2.7 $
 */
public class XNetProgrammerManager  extends DefaultProgrammerManager {

   protected XNetTrafficController tc = null;

    public XNetProgrammerManager(Programmer pProgrammer) {
        super(pProgrammer);
        tc = XNetTrafficController.instance();
    }

    /**
     * XPressNet command station does provide Ops Mode
     * We should make this return false based on what command station 
     * we're using but for now, we'll return true
     */
    public boolean isAddressedModePossible() {
        return tc.getCommandStation().isOpsModePossible();
    }

    public Programmer getAddressedProgrammer(boolean pLongAddress, int pAddress) {
        return new XNetOpsModeProgrammer(pAddress);
    }

    public Programmer reserveAddressedProgrammer(boolean pLongAddress, int pAddress) {
        return null;
    }
}

/* @(#)DefaultProgrammerManager.java */
