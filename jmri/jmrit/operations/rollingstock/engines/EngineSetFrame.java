// EngineSetFrame.java

package jmri.jmrit.operations.rollingstock.engines;

import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

import jmri.jmrit.operations.rollingstock.RollingStock;
import jmri.jmrit.operations.rollingstock.RollingStockSetFrame;


/**
 * Frame for user to place engine on the layout
 * 
 * @author Dan Boudreau Copyright (C) 2008, 2010
 * @version $Revision: 1.12 $
 */

public class EngineSetFrame extends RollingStockSetFrame implements java.beans.PropertyChangeListener {

	protected static final ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrit.operations.rollingstock.engines.JmritOperationsEnginesBundle");
	
	EngineManager manager = EngineManager.instance();
	EngineManagerXml managerXml = EngineManagerXml.instance();
	
	Engine _engine;
		
	public EngineSetFrame() {
		super();
	}

	public void initComponents() {
		super.initComponents();

		// build menu
		addHelpMenu("package.jmri.jmrit.operations.Operations_Engines", true);
		
		// disable location unknown, return when empty, final destination fields
		locationUnknownCheckBox.setVisible(false);	
		pOptionalrwe.setVisible(false);
		pFinalDestination.setVisible(false);
			
		packFrame();
	}
	
	public void loadEngine(Engine engine){
		_engine = engine;
		load(engine);
	}
	
	protected ResourceBundle getRb(){
		return rb;
	}
	
	protected boolean save(){
		if (!super.save())
			return false;
		// is this engine part of a consist?
		if (_engine.getConsist() != null){
			if (JOptionPane.showConfirmDialog(this,
					rb.getString("engineInConsist"),
					rb.getString("enginePartConsist"),
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
				// convert cars list to rolling stock list
				List<RollingStock> list = _engine.getConsist().getGroup();
				if (!updateGroup(list))
					return false;
			}
		}
		managerXml.writeOperationsFile();
		return true;
	}

	static org.apache.log4j.Logger log = org.apache.log4j.Logger
	.getLogger(EngineSetFrame.class.getName());
}
