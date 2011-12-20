// InternalSensorManager.java

package jmri.jmrix.internal;

/**
 * Implementation of the InternalSensorManager interface.
 * @author			Bob Jacobsen Copyright (C) 2001, 2003, 2006
 * @version			$Revision$
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value="NM_SAME_SIMPLE_NAME_AS_SUPERCLASS", justification="name assigned historically")
public class InternalSensorManager extends jmri.managers.InternalSensorManager {

    public InternalSensorManager(String prefix){
        super();
        this.prefix = prefix;
    }
        
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InternalSensorManager.class.getName());
}

/* @(#)InternalSensorManager.java */
