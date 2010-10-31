// OptionFrame.java

package jmri.jmrit.operations.setup;

import java.awt.GridBagLayout;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JPanel;

import jmri.jmrit.operations.OperationsFrame;


/**
 * Frame for user edit of setup options
 * 
 * @author Dan Boudreau Copyright (C) 2010
 * @version $Revision: 1.6 $
 */

public class OptionFrame extends OperationsFrame{

	static final ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrit.operations.setup.JmritOperationsSetupBundle");
	
	// labels

	// major buttons	
	JButton saveButton = new JButton(rb.getString("Save"));

	// radio buttons		
    
    // check boxes
	JCheckBox routerCheckBox = new JCheckBox(rb.getString("EnableCarRouting"));
	JCheckBox rfidCheckBox = new JCheckBox(rb.getString("EnableRfid"));
	JCheckBox carLoggerCheckBox = new JCheckBox(rb.getString("EnableCarLogging"));
	JCheckBox engineLoggerCheckBox = new JCheckBox(rb.getString("EnableEngineLogging"));
	
	JCheckBox localInterchangeCheckBox = new JCheckBox(rb.getString("AllowLocalInterchange"));
	JCheckBox localSidingCheckBox = new JCheckBox(rb.getString("AllowLocalSiding"));
	JCheckBox localYardCheckBox = new JCheckBox(rb.getString("AllowLocalYard"));
	JCheckBox trainIntoStagingCheckBox = new JCheckBox(rb.getString("TrainIntoStaging"));
	
	// text field
	
	// combo boxes

	public OptionFrame() {
		super(ResourceBundle.getBundle("jmri.jmrit.operations.setup.JmritOperationsSetupBundle").getString("TitleOptions"));
	}

	public void initComponents() {
		
		// the following code sets the frame's initial state

		// load checkboxes	
		rfidCheckBox.setSelected(Setup.isRfidEnabled());
		routerCheckBox.setSelected(Setup.isCarRoutingEnabled());
		carLoggerCheckBox.setSelected(Setup.isCarLoggerEnabled());
		engineLoggerCheckBox.setSelected(Setup.isEngineLoggerEnabled());
		localInterchangeCheckBox.setSelected(Setup.isLocalInterchangeMovesEnabled());
		localSidingCheckBox.setSelected(Setup.isLocalSidingMovesEnabled());
		localYardCheckBox.setSelected(Setup.isLocalYardMovesEnabled());
		trainIntoStagingCheckBox.setSelected(Setup.isTrainIntoStagingCheckEnabled());

		// add tool tips
		saveButton.setToolTipText(rb.getString("SaveToolTip"));
			
		getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		
		// Build Options panel
		JPanel pBuild = new JPanel();
		pBuild.setLayout(new GridBagLayout());
		pBuild.setBorder(BorderFactory.createTitledBorder(rb.getString("BorderLayoutBuildOptions")));	
		addItemLeft (pBuild, localInterchangeCheckBox, 1,0);
		addItemLeft (pBuild, localSidingCheckBox, 1,1);
		addItemLeft (pBuild, localYardCheckBox, 1,2);
		addItemLeft (pBuild, trainIntoStagingCheckBox, 1,3);
		
		// Router panel
		JPanel pRouter = new JPanel();
		pRouter.setLayout(new GridBagLayout());
		pRouter.setBorder(BorderFactory.createTitledBorder(rb.getString("BorderLayoutRouterOptions")));	
		addItemLeft (pRouter, routerCheckBox, 1,0);
		
		// Logger panel
		JPanel pLogger = new JPanel();
		pLogger.setLayout(new GridBagLayout());
		pLogger.setBorder(BorderFactory.createTitledBorder(rb.getString("BorderLayoutLoggerOptions")));		
		addItemLeft (pLogger, engineLoggerCheckBox, 1,0);
		addItemLeft (pLogger, carLoggerCheckBox, 1,1);
		
		JPanel pOption = new JPanel();
		pOption.setLayout(new GridBagLayout());
		pOption.setBorder(BorderFactory.createTitledBorder(rb.getString("BorderLayoutOptions")));		
		addItemLeft (pOption, rfidCheckBox, 1,0);
		
		// row 11
		JPanel pControl = new JPanel();
		pControl.setLayout(new GridBagLayout());
		addItem(pControl, saveButton, 3, 9);
		
		getContentPane().add(pBuild);
		getContentPane().add(pRouter);
		getContentPane().add(pLogger);
		getContentPane().add(pOption);
		getContentPane().add(pControl);

		// setup buttons
		addButtonAction(saveButton);

		//	build menu		
		addHelpMenu("package.jmri.jmrit.operations.Operations_Settings", true);

		pack();
		setSize(getWidth(), getHeight()+25);	// pad out a bit
		setVisible(true);
	}
	
	// Save button
	public void buttonActionPerformed(java.awt.event.ActionEvent ae) {
		if (ae.getSource() == saveButton){
			// Local moves?
			Setup.setLocalInterchangeMovesEnabled(localInterchangeCheckBox.isSelected());
			Setup.setLocalSidingMovesEnabled(localSidingCheckBox.isSelected());
			Setup.setLocalYardMovesEnabled(localYardCheckBox.isSelected());
			// Staging restriction?
			Setup.setTrainIntoStagingCheckEnabled(trainIntoStagingCheckBox.isSelected());
			// Car routing enabled?
			Setup.setCarRoutingEnabled(routerCheckBox.isSelected());
			// RFID enabled?
			Setup.setRfidEnabled(rfidCheckBox.isSelected());
			// Logging enabled?		
			Setup.setEngineLoggerEnabled(engineLoggerCheckBox.isSelected());
			Setup.setCarLoggerEnabled(carLoggerCheckBox.isSelected());
			OperationsSetupXml.instance().writeOperationsFile();
		}
	}

	static org.apache.log4j.Logger log = org.apache.log4j.Logger
	.getLogger(OptionFrame.class.getName());
}
