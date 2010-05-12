//OperationsSetupGuiTest.java

package jmri.jmrit.operations.setup;

import jmri.jmrit.operations.locations.LocationManagerXml;
import jmri.jmrit.operations.rollingstock.engines.EngineManagerXml;
import jmri.jmrit.operations.rollingstock.cars.CarManagerXml;
import jmri.jmrit.operations.routes.RouteManagerXml;
import jmri.jmrit.operations.setup.OperationsXml;
import jmri.jmrit.operations.trains.TrainManagerXml;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.extensions.jfcunit.eventdata.*;
import jmri.jmrit.display.LocoIcon;

import java.io.File;

/**
 * Tests for the Operations Setup GUI class
 *  
 * @author	Dan Boudreau Copyright (C) 2009
 * @version $Revision: 1.1 $
 */
public class OperationsSetupGuiTest extends jmri.util.SwingTestCase {
	
	public void testDirectionCheckBoxes(){
		OperationsSetupFrame f = new OperationsSetupFrame();
		f.initComponents();
		
		//both east/west and north/south checkboxes should be set	
		Assert.assertTrue("North selected", f.northCheckBox.isSelected());
		Assert.assertTrue("East selected", f.eastCheckBox.isSelected());
		
		getHelper().enterClickAndLeave( new MouseEventData( this, f.northCheckBox ) );	
		Assert.assertFalse("North deselected", f.northCheckBox.isSelected());
		Assert.assertTrue("East selected", f.eastCheckBox.isSelected());
		
		getHelper().enterClickAndLeave( new MouseEventData( this, f.eastCheckBox ) );		
		Assert.assertTrue("North selected", f.northCheckBox.isSelected());
		Assert.assertFalse("East deselected", f.eastCheckBox.isSelected());
		
		getHelper().enterClickAndLeave( new MouseEventData( this, f.eastCheckBox ) );
		Assert.assertTrue("North selected", f.northCheckBox.isSelected());
		Assert.assertTrue("East selected", f.eastCheckBox.isSelected());
		
		//done
		f.dispose();
	}
	
	public void testSetupFrameWrite(){
		OperationsSetupFrame f = new OperationsSetupFrame();
		f.initComponents();
		
		f.setVisible(true);
		f.railroadNameTextField.setText("Test Railroad Name");
		f.maxLengthTextField.setText("1234");
		f.maxEngineSizeTextField.setText("6");
		f.switchTimeTextField.setText("3");
		f.travelTimeTextField.setText("4");
		f.ownerTextField.setText("Bob J");
		
		getHelper().enterClickAndLeave( new MouseEventData( this, f.buildNormal ) );
		getHelper().enterClickAndLeave( new MouseEventData( this, f.scaleHO ) );
		getHelper().enterClickAndLeave( new MouseEventData( this, f.typeDesc ) );
		
		f.panelTextField.setText("Test Panel Name");
		
		f.eastComboBox.setSelectedItem(LocoIcon.RED);
		f.westComboBox.setSelectedItem(LocoIcon.BLUE);
		f.northComboBox.setSelectedItem(LocoIcon.WHITE);
		f.southComboBox.setSelectedItem(LocoIcon.GREEN);
		f.terminateComboBox.setSelectedItem(LocoIcon.GRAY);
		f.localComboBox.setSelectedItem(LocoIcon.YELLOW);

		getHelper().enterClickAndLeave( new MouseEventData( this, f.saveButton ) );
		//done
		f.dispose();
	}
	
	public void testSetupFrameRead(){
		OperationsSetupFrame f = new OperationsSetupFrame();
		f.initComponents();
		
		Assert.assertEquals("railroad name", "Test Railroad Name", f.railroadNameTextField.getText());
		Assert.assertEquals("max length", "1234", f.maxLengthTextField.getText());
		Assert.assertEquals("max engines", "6", f.maxEngineSizeTextField.getText());
		Assert.assertEquals("switch time", "3", f.switchTimeTextField.getText());
		Assert.assertEquals("travel time", "4", f.travelTimeTextField.getText());
		Assert.assertEquals("owner", "Bob J", f.ownerTextField.getText());
		
		Assert.assertTrue("build normal",f.buildNormal.isSelected());
		Assert.assertFalse("build aggressive",f.buildAggressive.isSelected());
		
		Assert.assertTrue("HO scale", f.scaleHO.isSelected());
		Assert.assertFalse("N scale", f.scaleN.isSelected());
		Assert.assertFalse("Z scale", f.scaleZ.isSelected());
		Assert.assertFalse("TT scale", f.scaleTT.isSelected());
		Assert.assertFalse("HOn3 scale", f.scaleHOn3.isSelected());
		Assert.assertFalse("OO scale", f.scaleOO.isSelected());
		Assert.assertFalse("Sn3 scale", f.scaleSn3.isSelected());
		Assert.assertFalse("S scale", f.scaleS.isSelected());
		Assert.assertFalse("On3 scale", f.scaleOn3.isSelected());
		Assert.assertFalse("O scale", f.scaleO.isSelected());
		Assert.assertFalse("G scale", f.scaleG.isSelected());
		
		Assert.assertTrue("descriptive", f.typeDesc.isSelected());
		Assert.assertFalse("AAR", f.typeAAR.isSelected());
		
		Assert.assertEquals("panel name", "Test Panel Name", f.panelTextField.getText());
		
		Assert.assertEquals("east color", LocoIcon.RED, f.eastComboBox.getSelectedItem());
		Assert.assertEquals("west color", LocoIcon.BLUE, f.westComboBox.getSelectedItem());
		Assert.assertEquals("north color", LocoIcon.WHITE, f.northComboBox.getSelectedItem());
		Assert.assertEquals("south color", LocoIcon.GREEN, f.southComboBox.getSelectedItem());
		Assert.assertEquals("terminate color", LocoIcon.GRAY, f.terminateComboBox.getSelectedItem());
		Assert.assertEquals("local color", LocoIcon.YELLOW, f.localComboBox.getSelectedItem());
		//done
		f.dispose();
	}



	
	// Ensure minimal setup for log4J
	@Override
    protected void setUp() throws Exception { 
        super.setUp();
		apps.tests.Log4JFixture.setUp();
		
		// Repoint OperationsXml to JUnitTest subdirectory
		OperationsXml.setOperationsDirectoryName("operations"+File.separator+"JUnitTest");
		// Change file names to ...Test.xml
		OperationsXml.setOperationsFileName("OperationsJUnitTest.xml"); 
		RouteManagerXml.setOperationsFileName("OperationsJUnitTestRouteRoster.xml");
		EngineManagerXml.setOperationsFileName("OperationsJUnitTestEngineRoster.xml");
		CarManagerXml.setOperationsFileName("OperationsJUnitTestCarRoster.xml");
		LocationManagerXml.setOperationsFileName("OperationsJUnitTestLocationRoster.xml");
		TrainManagerXml.setOperationsFileName("OperationsJUnitTestTrainRoster.xml");

	}

	public OperationsSetupGuiTest(String s) {
		super(s);
	}

	// Main entry point
	static public void main(String[] args) {
		String[] testCaseName = {"-noloading", OperationsSetupGuiTest.class.getName()};
		junit.swingui.TestRunner.main(testCaseName);
	}

	// test suite from all defined tests
	public static Test suite() {
		TestSuite suite = new TestSuite(OperationsSetupGuiTest.class);
		return suite;
	}

	// The minimal setup for log4J
	@Override
    protected void tearDown() throws Exception { 
        apps.tests.Log4JFixture.tearDown();
        super.tearDown();
    }
}
