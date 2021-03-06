package jmri.jmrix.openlcb;

import jmri.DccLocoAddress;
import jmri.DccThrottle;
import jmri.LocoAddress;
import jmri.jmrix.AbstractThrottleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a ThrottleManager for OpenLCB
 * <P>
 * @author	Bob Jacobsen Copyright (C) 2003, 2005, 2012
 * @version $Revision$
 */
public class OlcbThrottleManager extends AbstractThrottleManager {

    public OlcbThrottleManager() {
        super();
    }

    /**
     * Constructor.
     */
    public OlcbThrottleManager(jmri.jmrix.SystemConnectionMemo memo, OlcbConfigurationManager mgr) {
        super(memo);
        this.mgr = mgr;
    }

    OlcbConfigurationManager mgr;

    public void requestThrottleSetup(LocoAddress a, boolean control) {
        // Immediately trigger the callback.
        DccLocoAddress address = (DccLocoAddress) a;
        log.debug("new debug throttle for " + address);
        notifyThrottleKnown(new OlcbThrottle(address, adapterMemo, mgr), a);
    }

    /**
     * Address 1 and above can be a long address
     *
     */
    public boolean canBeLongAddress(int address) {
        return (address >= 1);
    }

    /**
     * Address 127 and below can be a short address
     *
     */
    public boolean canBeShortAddress(int address) {
        return (address <= 127);
    }

    /**
     * Are there any ambiguous addresses (short vs long) on this system?
     */
    public boolean addressTypeUnique() {
        return false;
    }

    public LocoAddress getAddress(String value, LocoAddress.Protocol protocol) {
        // if OpenLCB handle here
        if (protocol == LocoAddress.Protocol.OPENLCB) {
            org.openlcb.NodeID node = new org.openlcb.NodeID(value);
            return new OpenLcbLocoAddress(node);
        } else {
            // otherwise defer up to DCC
            return super.getAddress(value, protocol);
        }
    }

    public String[] getAddressTypes() {
        return new String[]{LocoAddress.Protocol.DCC_SHORT.getPeopleName(),
            LocoAddress.Protocol.DCC_LONG.getPeopleName(),
            LocoAddress.Protocol.OPENLCB.getPeopleName()};
    }

    public LocoAddress.Protocol[] getAddressProtocolTypes() {
        return new LocoAddress.Protocol[]{LocoAddress.Protocol.DCC_SHORT,
            LocoAddress.Protocol.DCC_LONG,
            LocoAddress.Protocol.OPENLCB};
    }

    public boolean disposeThrottle(DccThrottle t, jmri.ThrottleListener l) {
        log.debug("disposeThrottle called for " + t);
        if (super.disposeThrottle(t, l)) {
            OlcbThrottle lnt = (OlcbThrottle) t;
            lnt.throttleDispose();
            return true;
        }
        return false;
    }

    static Logger log = LoggerFactory.getLogger(OlcbThrottleManager.class.getName());

}
