/**
 * 
 */
package jmri.jmrit.turnoutoperations;

import java.awt.Component;
import javax.swing.JPanel;

import jmri.NoFeedbackTurnoutOperation;
import jmri.TurnoutOperation;

import java.lang.reflect.Constructor;

/**
 * configuration panel for TurnoutOperation class
 * Must be overridden to define specific panel details for class
 * Must have exactly one constructor like the one shown below
 * @author John Harper	Copyright 2005
 * @version $Revision: 1.1 $
 */
public abstract class TurnoutOperationConfig extends JPanel {

	TurnoutOperation myOperation;
	
	TurnoutOperationConfig(TurnoutOperation op) {
		myOperation = op;
	}
	
	TurnoutOperation getOperation() { return myOperation; };
	
	public abstract void endConfigure();
	
	/**
	 * Given an instance of a concrete subclass of
	 * the TurnoutOperation class, looks for a corresponding ...Config
	 * class and creates an instance of it. If anything goes wrong (no such
	 * class, wrong constructors, instantiation error, ....) just return null
	 * @param op	operation for which configurator is required
	 * @return	the configurator
	 */
	static public TurnoutOperationConfig getConfigPanel(TurnoutOperation op) {
		TurnoutOperationConfig config = null;
		String[] path = op.getClass().getName().split("\\.");
		String configName = "jmri.jmrit.turnoutoperations." + path[path.length-1] + "Config";
		try {
			Class configClass = Class.forName(configName);
			Constructor[] constrs = configClass.getConstructors();
			if (constrs.length==1) {
				try {
					config = (TurnoutOperationConfig)constrs[0].newInstance(new Object[]{op});
				} catch (Throwable e) { };		// too many to list!
			}
		} catch (ClassNotFoundException e) { };
		if (config==null) {
			log.debug("could not create configurator for "+op.getClass().getName()+" \""+op.getName()+"\"");
		}
		return config;
	}
    static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(TurnoutOperationConfig.class.getName());
}
