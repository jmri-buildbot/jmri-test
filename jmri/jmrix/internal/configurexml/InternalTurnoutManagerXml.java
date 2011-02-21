package jmri.jmrix.internal.configurexml;

import org.jdom.Element;

/**
 * Provides load and store functionality for
 * configuring InternalTurnoutManagers.
 * <P>
 * Uses the store method from the abstract base class, but
 * provides a load method here.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2006
 * @version $Revision: 1.1 $
 */
public class InternalTurnoutManagerXml extends jmri.managers.configurexml.InternalTurnoutManagerXml {

    public InternalTurnoutManagerXml() {
        super();
    }

    public void setStoreElementClass(Element turnouts) {
        turnouts.setAttribute("class",this.getClass().getName());
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InternalTurnoutManagerXml.class.getName());

}