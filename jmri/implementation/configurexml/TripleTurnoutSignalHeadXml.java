package jmri.implementation.configurexml;

import jmri.InstanceManager;
import jmri.SignalHead;
import jmri.implementation.TripleTurnoutSignalHead;
import jmri.Turnout;

import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Handle XML configuration for TripleTurnoutSignalHead objects.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2003, 2008
 * @version $Revision: 1.3 $
 */
public class TripleTurnoutSignalHeadXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public TripleTurnoutSignalHeadXml() {}

    /**
     * Default implementation for storing the contents of a
     * TripleTurnoutSignalHead
     * @param o Object to store, of type TripleTurnoutSignalHead
     * @return Element containing the complete info
     */
    public Element store(Object o) {
        TripleTurnoutSignalHead p = (TripleTurnoutSignalHead)o;

        Element element = new Element("signalhead");
        element.setAttribute("class", this.getClass().getName());

        // include contents
        element.setAttribute("systemName", p.getSystemName());

        storeCommon(p, element);
        
        element.addContent(addTurnoutElement(p.getGreen()));
        element.addContent(addTurnoutElement(p.getYellow()));
        element.addContent(addTurnoutElement(p.getRed()));

        return element;
    }

    Element addTurnoutElement(Turnout to) {
        String user = to.getUserName();
        String sys = to.getSystemName();

        Element el = new Element("turnout");
        el.setAttribute("systemName", sys);
        if (user!=null) el.setAttribute("userName", user);

        return el;
    }

    /**
     * Create a TripleTurnoutSignalHead
     * @param element Top level Element to unpack.
     * @return true if successful
     */
    @SuppressWarnings("unchecked")
	public boolean load(Element element) {
        List<Element> l = element.getChildren("turnout");
        Turnout green = loadTurnout(l.get(0));
        Turnout yellow = loadTurnout(l.get(1));
        Turnout red = loadTurnout(l.get(2));
        // put it together
        String sys = element.getAttribute("systemName").getValue();
        Attribute a = element.getAttribute("userName");
        SignalHead h;
        if (a == null)
            h = new TripleTurnoutSignalHead(sys, green, yellow, red);
        else
            h = new TripleTurnoutSignalHead(sys, a.getValue(), green, yellow, red);

        loadCommon(h, element);
        
        InstanceManager.signalHeadManagerInstance().register(h);
        return true;
    }

    Turnout loadTurnout(Object o) {
        Element e = (Element)o;

        // we don't create the Turnout, we just look it up.
        String sys = e.getAttribute("systemName").getValue();
        return InstanceManager.turnoutManagerInstance().getBySystemName(sys);
    }

    public void load(Element element, Object o) {
        log.error("Invalid method called");
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TripleTurnoutSignalHeadXml.class.getName());
}