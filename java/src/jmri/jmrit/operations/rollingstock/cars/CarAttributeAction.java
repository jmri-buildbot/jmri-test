// CarAttributeEditFrame.java
package jmri.jmrit.operations.rollingstock.cars;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Frame for adding and editing the car roster for operations.
 *
 * @author Daniel Boudreau Copyright (C) 2008, 2014
 * @version $Revision$
 */
final class CarAttributeAction extends AbstractAction {

    /**
     *
     */
    private static final long serialVersionUID = -369749063288544953L;

    public CarAttributeAction(String actionName, CarAttributeEditFrame caef) {
        super(actionName);
        this.caef = caef;
    }

    CarAttributeEditFrame caef;

    public void actionPerformed(ActionEvent ae) {
        log.debug("Show attribute quanity");
        caef.toggleShowQuanity();
    }

    static Logger log = LoggerFactory.getLogger(CarAttributeAction.class.getName());
}
