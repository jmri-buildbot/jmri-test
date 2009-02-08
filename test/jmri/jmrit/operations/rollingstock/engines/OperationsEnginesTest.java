// OperationsEnginesTest.java

package jmri.jmrit.operations.rollingstock.engines;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import jmri.jmrit.XmlFile;
import jmri.jmrit.operations.locations.LocationManagerXml;
import jmri.jmrit.operations.rollingstock.cars.CarManagerXml;
import jmri.jmrit.operations.routes.RouteManagerXml;
import jmri.jmrit.operations.setup.OperationsXml;
import jmri.jmrit.operations.trains.TrainManagerXml;

/**
 * Tests for the Operations RollingStock Engine class
 * Last manually cross-checked on 20090131
 * 
 * Still to do:
 *   Engine: Type, HP, Destination
 *   Engine: Verify everything else 
 *   EngineTypes: get/set Names lists 
 *   EngineModels: get/set Names lists
 *   EngineLengths: Everything
 *   Consist: Everything
 *   Import: Everything  
 *   EngineManager: Engine register/deregister
 *   EngineManager: Consists
 * 
 * @author	Bob Coleman Copyright (C) 2008, 2009
 * @version $Revision: 1.8 $
 */
public class OperationsEnginesTest extends TestCase {

	// test Engine Class
        // test Engine creation
	public void testCreate() {
		Engine e1 = new Engine("TESTROAD", "TESTNUMBER1");
		e1.setModel("TESTMODEL");
		e1.setLength("TESTLENGTH");

		Assert.assertEquals("Engine Road", "TESTROAD", e1.getRoad());
		Assert.assertEquals("Engine Number", "TESTNUMBER1", e1.getNumber());
		Assert.assertEquals("Engine Model", "TESTMODEL", e1.getModel());
		Assert.assertEquals("Engine Length", "TESTLENGTH", e1.getLength());
	}


	// test EngineLengths Class
        // test EngineLengths creation
	public void testEngineLengthsCreate() {
		EngineLengths el1 = new EngineLengths();
	}

        // test EngineLengths public constants
	public void testEngineLengthsConstants() {
		EngineLengths el1 = new EngineLengths();

		Assert.assertEquals("EngineTypes ENGINELENGTHS_CHANGED_PROPERTY", "EngineLengths", EngineLengths.ENGINELENGTHS_CHANGED_PROPERTY);
	}

        // test EngineLengths Names
	public void testEngineLengthsNames() {
		EngineLengths el1 = new EngineLengths();

		Assert.assertEquals("EngineLengths Null Names", false, el1.containsName("TESTENGINELENGTHNAME1"));

                el1.addName("TESTENGINELENGTHNAME1");
                Assert.assertEquals("EngineLengths add Name1", true, el1.containsName("TESTENGINELENGTHNAME1"));

                el1.addName("TESTENGINELENGTHNAME2");
                Assert.assertEquals("EngineLengths add Name2", true, el1.containsName("TESTENGINELENGTHNAME2"));

                el1.deleteName("TESTENGINELENGTHNAME2");
                Assert.assertEquals("EngineLengths delete Name2", false, el1.containsName("TESTENGINELENGTHNAME2"));

                el1.deleteName("TESTENGINELENGTHNAME1");
                Assert.assertEquals("EngineLengths delete Name1", false, el1.containsName("TESTENGINELENGTHNAME1"));
	}

        
	// test EngineTypes Class
        // test EngineTypes creation
	public void testEngineTypesCreate() {
		EngineTypes et1 = new EngineTypes();
	}

        // test EngineTypes public constants
	public void testEngineTypesConstants() {
		EngineTypes et1 = new EngineTypes();

		Assert.assertEquals("EngineTypes ENGINETYPES_LENGTH_CHANGED_PROPERTY", "EngineTypes Length", EngineTypes.ENGINETYPES_LENGTH_CHANGED_PROPERTY);
		Assert.assertEquals("EngineTypes ENGINETYPES_NAME_CHANGED_PROPERTY", "EngineTypes Name", EngineTypes.ENGINETYPES_NAME_CHANGED_PROPERTY);
	}

        // test EngineTypes Names
	public void testEngineTypesNames() {
		EngineTypes et1 = new EngineTypes();

		Assert.assertEquals("EngineTypes Null Names", false, et1.containsName("TESTENGINETYPENAME1"));

                et1.addName("TESTENGINETYPENAME1");
                Assert.assertEquals("EngineTypes add Name1", true, et1.containsName("TESTENGINETYPENAME1"));

                et1.addName("TESTENGINETYPENAME2");
                Assert.assertEquals("EngineTypes add Name2", true, et1.containsName("TESTENGINETYPENAME2"));

                et1.replaceName("TESTENGINETYPENAME1", "TESTENGINETYPENAME3");
                Assert.assertEquals("EngineTypes replace Name1", false, et1.containsName("TESTENGINETYPENAME1"));
                Assert.assertEquals("EngineTypes replace Name3", true, et1.containsName("TESTENGINETYPENAME3"));

                et1.deleteName("TESTENGINETYPENAME2");
                Assert.assertEquals("EngineTypes delete Name2", false, et1.containsName("TESTENGINETYPENAME2"));

                et1.deleteName("TESTENGINETYPENAME3");
                Assert.assertEquals("EngineTypes delete Name3", false, et1.containsName("TESTENGINETYPENAME3"));
	}

        
	// test EngineModels Class
        // test EngineModels creation
	public void testEngineModelsCreate() {
		EngineModels em1 = new EngineModels();
	}

        // test EngineModels public constants
	public void testEngineModelsConstants() {
		EngineModels em1 = new EngineModels();

		Assert.assertEquals("EngineModels ENGINEMODELS_CHANGED_PROPERTY", "EngineModels", EngineModels.ENGINEMODELS_CHANGED_PROPERTY);
	}

        // test EngineModels Names
	public void testEngineModelsNames() {
		EngineModels em1 = new EngineModels();

		Assert.assertEquals("EngineModels Null Names", false, em1.containsName("TESTENGINEMODELNAME1"));

                em1.addName("TESTENGINEMODELNAME1");
                Assert.assertEquals("EngineModels add Name1", true, em1.containsName("TESTENGINEMODELNAME1"));

                em1.addName("TESTENGINEMODELNAME2");
                Assert.assertEquals("EngineModels add Name2", true, em1.containsName("TESTENGINEMODELNAME2"));

                em1.deleteName("TESTENGINEMODELNAME2");
                Assert.assertEquals("EngineModels delete Name2", false, em1.containsName("TESTENGINEMODELNAME2"));

                em1.deleteName("TESTENGINEMODELNAME1");
                Assert.assertEquals("EngineModels delete Name1", false, em1.containsName("TESTENGINEMODELNAME1"));
	}

        // test EngineModels Attributes
	public void testEngineModelsAttributes() {
		EngineModels em1 = new EngineModels();

                em1.setModelHorsepower("TESTENGINEMODELNAME1", "3800");
                Assert.assertEquals("EngineModels HorsePower1", "3800", em1.getModelHorsepower("TESTENGINEMODELNAME1"));

                em1.setModelHorsepower("TESTENGINEMODELNAME2", "2400");
                Assert.assertEquals("EngineModels HorsePower2+1", "3800", em1.getModelHorsepower("TESTENGINEMODELNAME1"));
                Assert.assertEquals("EngineModels HorsePower2", "2400", em1.getModelHorsepower("TESTENGINEMODELNAME2"));

                em1.setModelLength("TESTENGINEMODELNAME1", "60");
                Assert.assertEquals("EngineModels Length1", "60", em1.getModelLength("TESTENGINEMODELNAME1"));

                em1.setModelLength("TESTENGINEMODELNAME2", "50");
                Assert.assertEquals("EngineModels Length2+1", "60", em1.getModelLength("TESTENGINEMODELNAME1"));
                Assert.assertEquals("EngineModels Length2", "50", em1.getModelLength("TESTENGINEMODELNAME2"));

                em1.setModelType("TESTENGINEMODELNAME1", "TESTDiesel");
                Assert.assertEquals("EngineModels Type1", "TESTDiesel", em1.getModelType("TESTENGINEMODELNAME1"));

                em1.setModelType("TESTENGINEMODELNAME2", "TESTSteam");
                Assert.assertEquals("EngineModels Type2+1", "TESTDiesel", em1.getModelType("TESTENGINEMODELNAME1"));
                Assert.assertEquals("EngineModels Type2", "TESTSteam", em1.getModelType("TESTENGINEMODELNAME2"));
                Assert.assertEquals("EngineModels Type+Length2+1", "60", em1.getModelLength("TESTENGINEMODELNAME1"));
                Assert.assertEquals("EngineModels Type+Length2", "50", em1.getModelLength("TESTENGINEMODELNAME2"));
                Assert.assertEquals("EngineModels Type+HorsePower2+1", "3800", em1.getModelHorsepower("TESTENGINEMODELNAME1"));
                Assert.assertEquals("EngineModels Type+HorsePower2", "2400", em1.getModelHorsepower("TESTENGINEMODELNAME2"));
	}

        // test EngineModels Defaults
	public void testEngineModelsDefaults() throws Exception {

                EngineModels.instance().addName("E8");
                Assert.assertEquals("EngineModels Default Model E8", true, EngineModels.instance().containsName("E8"));
                Assert.assertEquals("EngineModels Default Horse E8", "2250", EngineModels.instance().getModelHorsepower("E8"));
                Assert.assertEquals("EngineModels Default Length E8", "70", EngineModels.instance().getModelLength("E8"));
                Assert.assertEquals("EngineModels Default Type E8", "Diesel", EngineModels.instance().getModelType("E8"));

                EngineModels.instance().addName("FT");
                Assert.assertEquals("EngineModels Default Model FT", true, EngineModels.instance().containsName("FT"));
                Assert.assertEquals("EngineModels Default Horse FT", "1350", EngineModels.instance().getModelHorsepower("FT"));
                Assert.assertEquals("EngineModels Default Length FT", "50", EngineModels.instance().getModelLength("FT"));
                Assert.assertEquals("EngineModels Default Type FT", "Diesel", EngineModels.instance().getModelType("FT"));

                EngineModels.instance().addName("F3");
                Assert.assertEquals("EngineModels Default Model F3", true, EngineModels.instance().containsName("F3"));
                Assert.assertEquals("EngineModels Default Horse F3", "1500", EngineModels.instance().getModelHorsepower("F3"));
                Assert.assertEquals("EngineModels Default Length F3", "50", EngineModels.instance().getModelLength("F3"));
                Assert.assertEquals("EngineModels Default Type F3", "Diesel", EngineModels.instance().getModelType("F3"));

                EngineModels.instance().addName("F7");
                Assert.assertEquals("EngineModels Default Model F7", true, EngineModels.instance().containsName("F7"));
                Assert.assertEquals("EngineModels Default Horse F7", "1500", EngineModels.instance().getModelHorsepower("F7"));
                Assert.assertEquals("EngineModels Default Length F7", "50", EngineModels.instance().getModelLength("F7"));
                Assert.assertEquals("EngineModels Default Type F7", "Diesel", EngineModels.instance().getModelType("F7"));

                EngineModels.instance().addName("F9");
                Assert.assertEquals("EngineModels Default Model F9", true, EngineModels.instance().containsName("F9"));
                Assert.assertEquals("EngineModels Default Horse F9", "1750", EngineModels.instance().getModelHorsepower("F9"));
                Assert.assertEquals("EngineModels Default Length F9", "50", EngineModels.instance().getModelLength("F9"));
                Assert.assertEquals("EngineModels Default Type F9", "Diesel", EngineModels.instance().getModelType("F9"));

                EngineModels.instance().addName("GG1");
                Assert.assertEquals("EngineModels Default Model GG1", true, EngineModels.instance().containsName("GG1"));
                Assert.assertEquals("EngineModels Default Horse GG1", "4620", EngineModels.instance().getModelHorsepower("GG1"));
                Assert.assertEquals("EngineModels Default Length GG1", "80", EngineModels.instance().getModelLength("GG1"));
                Assert.assertEquals("EngineModels Default Type GG1", "Electric", EngineModels.instance().getModelType("GG1"));

                EngineModels.instance().addName("GP20");
                Assert.assertEquals("EngineModels Default Model GP20", true, EngineModels.instance().containsName("GP20"));
                Assert.assertEquals("EngineModels Default Horse GP20", "2000", EngineModels.instance().getModelHorsepower("GP20"));
                Assert.assertEquals("EngineModels Default Length GP20", "56", EngineModels.instance().getModelLength("GP20"));
                Assert.assertEquals("EngineModels Default Type GP20", "Diesel", EngineModels.instance().getModelType("GP20"));

                EngineModels.instance().addName("GP30");
                Assert.assertEquals("EngineModels Default Model GP30", true, EngineModels.instance().containsName("GP30"));
                Assert.assertEquals("EngineModels Default Horse GP30", "2250", EngineModels.instance().getModelHorsepower("GP30"));
                Assert.assertEquals("EngineModels Default Length GP30", "56", EngineModels.instance().getModelLength("GP30"));
                Assert.assertEquals("EngineModels Default Type GP30", "Diesel", EngineModels.instance().getModelType("GP30"));

                EngineModels.instance().addName("GP35");
                Assert.assertEquals("EngineModels Default Model GP35", true, EngineModels.instance().containsName("GP35"));
                Assert.assertEquals("EngineModels Default Horse GP35", "2500", EngineModels.instance().getModelHorsepower("GP35"));
                Assert.assertEquals("EngineModels Default Length GP35", "56", EngineModels.instance().getModelLength("GP35"));
                Assert.assertEquals("EngineModels Default Type GP35", "Diesel", EngineModels.instance().getModelType("GP35"));

                EngineModels.instance().addName("GP38");
                Assert.assertEquals("EngineModels Default Model GP38", true, EngineModels.instance().containsName("GP38"));
                Assert.assertEquals("EngineModels Default Horse GP38", "2000", EngineModels.instance().getModelHorsepower("GP38"));
                Assert.assertEquals("EngineModels Default Length GP38", "59", EngineModels.instance().getModelLength("GP38"));
                Assert.assertEquals("EngineModels Default Type GP38", "Diesel", EngineModels.instance().getModelType("GP38"));

                EngineModels.instance().addName("GP40");
                Assert.assertEquals("EngineModels Default Model GP40", true, EngineModels.instance().containsName("GP40"));
                Assert.assertEquals("EngineModels Default Horse GP40", "3000", EngineModels.instance().getModelHorsepower("GP40"));
                Assert.assertEquals("EngineModels Default Length GP40", "59", EngineModels.instance().getModelLength("GP40"));
                Assert.assertEquals("EngineModels Default Type GP40", "Diesel", EngineModels.instance().getModelType("GP40"));

                EngineModels.instance().addName("GTEL");
                Assert.assertEquals("EngineModels Default Model GTEL", true, EngineModels.instance().containsName("GTEL"));
                Assert.assertEquals("EngineModels Default Horse GTEL", "4500", EngineModels.instance().getModelHorsepower("GTEL"));
                Assert.assertEquals("EngineModels Default Length GTEL", "80", EngineModels.instance().getModelLength("GTEL"));
                Assert.assertEquals("EngineModels Default Type GTEL", "Gas Turbine", EngineModels.instance().getModelType("GTEL"));

                EngineModels.instance().addName("RS1");
                Assert.assertEquals("EngineModels Default Model RS1", true, EngineModels.instance().containsName("RS1"));
                Assert.assertEquals("EngineModels Default Horse RS1", "1000", EngineModels.instance().getModelHorsepower("RS1"));
                Assert.assertEquals("EngineModels Default Length RS1", "51", EngineModels.instance().getModelLength("RS1"));
                Assert.assertEquals("EngineModels Default Type RS1", "Diesel", EngineModels.instance().getModelType("RS1"));

                EngineModels.instance().addName("RS2");
                Assert.assertEquals("EngineModels Default Model RS2", true, EngineModels.instance().containsName("RS2"));
                Assert.assertEquals("EngineModels Default Horse RS2", "1500", EngineModels.instance().getModelHorsepower("RS2"));
                Assert.assertEquals("EngineModels Default Length RS2", "52", EngineModels.instance().getModelLength("RS2"));
                Assert.assertEquals("EngineModels Default Type RS2", "Diesel", EngineModels.instance().getModelType("RS2"));

                EngineModels.instance().addName("RS3");
                Assert.assertEquals("EngineModels Default Model RS3", true, EngineModels.instance().containsName("RS3"));
                Assert.assertEquals("EngineModels Default Horse RS3", "1600", EngineModels.instance().getModelHorsepower("RS3"));
                Assert.assertEquals("EngineModels Default Length RS3", "51", EngineModels.instance().getModelLength("RS3"));
                Assert.assertEquals("EngineModels Default Type RS3", "Diesel", EngineModels.instance().getModelType("RS3"));

                EngineModels.instance().addName("RS11");
                Assert.assertEquals("EngineModels Default Model RS11", true, EngineModels.instance().containsName("RS11"));
                Assert.assertEquals("EngineModels Default Horse RS11", "1800", EngineModels.instance().getModelHorsepower("RS11"));
                Assert.assertEquals("EngineModels Default Length RS11", "53", EngineModels.instance().getModelLength("RS11"));
                Assert.assertEquals("EngineModels Default Type RS11", "Diesel", EngineModels.instance().getModelType("RS11"));

                EngineModels.instance().addName("RS18");
                Assert.assertEquals("EngineModels Default Model RS18", true, EngineModels.instance().containsName("RS18"));
                Assert.assertEquals("EngineModels Default Horse RS18", "1800", EngineModels.instance().getModelHorsepower("RS18"));
                Assert.assertEquals("EngineModels Default Length RS18", "52", EngineModels.instance().getModelLength("RS18"));
                Assert.assertEquals("EngineModels Default Type RS18", "Diesel", EngineModels.instance().getModelType("RS18"));

                EngineModels.instance().addName("RS27");
                Assert.assertEquals("EngineModels Default Model RS27", true, EngineModels.instance().containsName("RS27"));
                Assert.assertEquals("EngineModels Default Horse RS27", "2400", EngineModels.instance().getModelHorsepower("RS27"));
                Assert.assertEquals("EngineModels Default Length RS27", "57", EngineModels.instance().getModelLength("RS27"));
                Assert.assertEquals("EngineModels Default Type RS27", "Diesel", EngineModels.instance().getModelType("RS27"));

                EngineModels.instance().addName("RSD4");
                Assert.assertEquals("EngineModels Default Model RSD4", true, EngineModels.instance().containsName("RSD4"));
                Assert.assertEquals("EngineModels Default Horse RSD4", "1600", EngineModels.instance().getModelHorsepower("RSD4"));
                Assert.assertEquals("EngineModels Default Length RSD4", "52", EngineModels.instance().getModelLength("RSD4"));
                Assert.assertEquals("EngineModels Default Type RSD4", "Diesel", EngineModels.instance().getModelType("RSD4"));

                EngineModels.instance().addName("Shay");
                Assert.assertEquals("EngineModels Default Model Shay", true, EngineModels.instance().containsName("Shay"));
                Assert.assertEquals("EngineModels Default Horse Shay", "70", EngineModels.instance().getModelHorsepower("Shay"));
                Assert.assertEquals("EngineModels Default Length Shay", "50", EngineModels.instance().getModelLength("Shay"));
                Assert.assertEquals("EngineModels Default Type Shay", "Steam", EngineModels.instance().getModelType("Shay"));

                EngineModels.instance().addName("SD26");
                Assert.assertEquals("EngineModels Default Model SD26", true, EngineModels.instance().containsName("SD26"));
                Assert.assertEquals("EngineModels Default Horse SD26", "2650", EngineModels.instance().getModelHorsepower("SD26"));
                Assert.assertEquals("EngineModels Default Length SD26", "61", EngineModels.instance().getModelLength("SD26"));
                Assert.assertEquals("EngineModels Default Type SD26", "Diesel", EngineModels.instance().getModelType("SD26"));

                EngineModels.instance().addName("SD45");
                Assert.assertEquals("EngineModels Default Model SD45", true, EngineModels.instance().containsName("SD45"));
                Assert.assertEquals("EngineModels Default Horse SD45", "3600", EngineModels.instance().getModelHorsepower("SD45"));
                Assert.assertEquals("EngineModels Default Length SD45", "66", EngineModels.instance().getModelLength("SD45"));
                Assert.assertEquals("EngineModels Default Type SD45", "Diesel", EngineModels.instance().getModelType("SD45"));

                EngineModels.instance().addName("SW1200");
                Assert.assertEquals("EngineModels Default Model SW1200", true, EngineModels.instance().containsName("SW1200"));
                Assert.assertEquals("EngineModels Default Horse SW1200", "1200", EngineModels.instance().getModelHorsepower("SW1200"));
                Assert.assertEquals("EngineModels Default Length SW1200", "45", EngineModels.instance().getModelLength("SW1200"));
                Assert.assertEquals("EngineModels Default Type SW1200", "Diesel", EngineModels.instance().getModelType("SW1200"));

                EngineModels.instance().addName("SW1500");
                Assert.assertEquals("EngineModels Default Model SW1500", true, EngineModels.instance().containsName("SW1500"));
                Assert.assertEquals("EngineModels Default Horse SW1500", "1500", EngineModels.instance().getModelHorsepower("SW1500"));
                Assert.assertEquals("EngineModels Default Length SW1500", "45", EngineModels.instance().getModelLength("SW1500"));
                Assert.assertEquals("EngineModels Default Type SW1500", "Diesel", EngineModels.instance().getModelType("SW1500"));

                EngineModels.instance().addName("SW8");
                Assert.assertEquals("EngineModels Default Model SW8", true, EngineModels.instance().containsName("SW8"));
                Assert.assertEquals("EngineModels Default Horse SW8", "800", EngineModels.instance().getModelHorsepower("SW8"));
                Assert.assertEquals("EngineModels Default Length SW8", "44", EngineModels.instance().getModelLength("SW8"));
                Assert.assertEquals("EngineModels Default Type SW8", "Diesel", EngineModels.instance().getModelType("SW8"));

                EngineModels.instance().addName("TRAINMASTER");
                Assert.assertEquals("EngineModels Default Model TRAINMASTER", true, EngineModels.instance().containsName("TRAINMASTER"));
                Assert.assertEquals("EngineModels Default Horse TRAINMASTER", "2400", EngineModels.instance().getModelHorsepower("TRAINMASTER"));
                Assert.assertEquals("EngineModels Default Length TRAINMASTER", "66", EngineModels.instance().getModelLength("TRAINMASTER"));
                Assert.assertEquals("EngineModels Default Type TRAINMASTER", "Diesel", EngineModels.instance().getModelType("TRAINMASTER"));

                EngineModels.instance().addName("U28B");
                Assert.assertEquals("EngineModels Default Model U28B", true, EngineModels.instance().containsName("U28B"));
                Assert.assertEquals("EngineModels Default Horse U28B", "2800", EngineModels.instance().getModelHorsepower("U28B"));
                Assert.assertEquals("EngineModels Default Length U28B", "60", EngineModels.instance().getModelLength("U28B"));
                Assert.assertEquals("EngineModels Default Type U28B", "Diesel", EngineModels.instance().getModelType("U28B"));
	}

	public void testConsist() {
		Consist c1 = new Consist("TESTCONSIST");
		Assert.assertEquals("Consist Name", "TESTCONSIST", c1.getName());

		Engine e1 = new Engine("TESTROAD", "TESTNUMBER1");
		e1.setModel("GP35");  //  e1.setLength("56");
		Engine e2 = new Engine("TESTROAD", "TESTNUMBER2");
		e2.setModel("GP40");  //  e2.setLength("59");
		Engine e3 = new Engine("TESTROAD", "TESTNUMBER3");
		e3.setModel("SW1500");  //  e3.setLength("45");
		Engine e4 = new Engine("TESTROAD", "TESTNUMBER4");
		e4.setModel("SW1500");  //  e3.setLength("45");

		Assert.assertEquals("Consist Initial Length", 0, c1.getLength());
		Assert.assertFalse("Consist Lead Engine 0", c1.isLeadEngine(e1));

		c1.addEngine(e1);
		Assert.assertEquals("Consist Engine 1 Length", 56+4, c1.getLength());
		Assert.assertTrue("Consist Lead Engine 1", c1.isLeadEngine(e1));

		c1.addEngine(e2);
		Assert.assertEquals("Consist Engine 2 Length", 56+4+59+4, c1.getLength());
		Assert.assertTrue("Consist Lead Engine 1 after 2", c1.isLeadEngine(e1));

                c1.setLeadEngine(e2);
		Assert.assertFalse("Consist Lead Engine 1 after 2c", c1.isLeadEngine(e1));
		Assert.assertTrue("Consist Lead Engine 2 after 2c", c1.isLeadEngine(e2));
                
		c1.addEngine(e3);
		Assert.assertEquals("Consist Engine 3 Length", 56+4+59+4+45+4, c1.getLength());
		Assert.assertTrue("Consist Lead Engine 2 after 3", c1.isLeadEngine(e2));
		Assert.assertFalse("Consist Lead Engine 1 after 3", c1.isLeadEngine(e1));
		Assert.assertFalse("Consist Lead Engine 3 after 3", c1.isLeadEngine(e3));

                // Can't set lead engine if not part of consist
                c1.setLeadEngine(e4);
		Assert.assertTrue("Consist Lead Engine 2 after 4c", c1.isLeadEngine(e2));
		Assert.assertFalse("Consist Lead Engine 4 after 4c", c1.isLeadEngine(e4));
              	List<Engine> tempengines = new ArrayList<Engine>();
                tempengines = c1.getEngines();
		Assert.assertTrue("Consist Engine 2 after 4c", tempengines.contains(e2));
		Assert.assertFalse("Consist Engine 4 after 4c", tempengines.contains(e4));
                


		c1.deleteEngine(e2);
		Assert.assertEquals("Consist Engine Delete 2 Length", 56+4+45+4, c1.getLength());

		c1.deleteEngine(e1);
		Assert.assertEquals("Consist Engine Delete 1 Length", 45+4, c1.getLength());

		c1.deleteEngine(e3);
		Assert.assertEquals("Consist Engine Delete 3 Length", 0, c1.getLength());

	}

	public void testEngineConsist() {
		Consist cold = new Consist("TESTCONSISTOLD");
		Assert.assertEquals("Consist Name old", "TESTCONSISTOLD", cold.getName());

		Consist cnew = new Consist("TESTCONSISTNEW");
		Assert.assertEquals("Consist Name new", "TESTCONSISTNEW", cnew.getName());

		Engine e1 = new Engine("TESTROAD", "TESTNUMBER1");
		e1.setModel("GP35");  //  e1.setLength("56");
		Engine e2 = new Engine("TESTROAD", "TESTNUMBER2");
		e2.setModel("GP40");  //  e2.setLength("59");
		Engine e3 = new Engine("TESTROAD", "TESTNUMBER3");
		e3.setModel("SW1500");  //  e3.setLength("45");
		Engine e4 = new Engine("TESTROAD", "TESTNUMBER4");
		e4.setModel("SW1500");  //  e3.setLength("45");

		//  All three engines start out in the old consist with engine 1 as the lead engine.
		e1.setConsist(cold);
		e2.setConsist(cold);
		e3.setConsist(cold);
		Assert.assertEquals("Consist Name for engine 1 before", "TESTCONSISTOLD", e1.getConsistName());
		Assert.assertEquals("Consist Name for engine 2 before", "TESTCONSISTOLD", e2.getConsistName());
		Assert.assertEquals("Consist Name for engine 3 before", "TESTCONSISTOLD", e3.getConsistName());
		Assert.assertEquals("Consist old length before", 56+4+59+4+45+4, cold.getLength());
		Assert.assertEquals("Consist new length before", 0, cnew.getLength());
		Assert.assertTrue("Consist old Lead is Engine 1 before", cold.isLeadEngine(e1));
		Assert.assertFalse("Consist old Lead is not Engine 2 before", cold.isLeadEngine(e2));
		Assert.assertFalse("Consist old Lead is not Engine 3 before", cold.isLeadEngine(e3));
		Assert.assertFalse("Consist new Lead is not Engine 1 before", cnew.isLeadEngine(e1));
		Assert.assertFalse("Consist new Lead is not Engine 2 before", cnew.isLeadEngine(e2));
		Assert.assertFalse("Consist new Lead is not Engine 3 before", cnew.isLeadEngine(e3));

		//  Move engine 1 to the new consist where it will be the lead engine.
		//  Engine 2 should now be the lead engine of the old consist.
		e1.setConsist(cnew);
		Assert.assertEquals("Consist Name for engine 1 after", "TESTCONSISTNEW", e1.getConsistName());
		Assert.assertEquals("Consist Name for engine 2 after", "TESTCONSISTOLD", e2.getConsistName());
		Assert.assertEquals("Consist Name for engine 3 after", "TESTCONSISTOLD", e3.getConsistName());
		Assert.assertEquals("Consist old length after", 59+4+45+4, cold.getLength());
		Assert.assertEquals("Consist new length after", 56+4, cnew.getLength());
		Assert.assertFalse("Consist old Lead is not Engine 1 after", cold.isLeadEngine(e1));
		Assert.assertTrue("Consist old Lead is Engine 2 after", cold.isLeadEngine(e2));
		Assert.assertFalse("Consist old Lead is not Engine 3 after", cold.isLeadEngine(e3));
		Assert.assertTrue("Consist new Lead is Engine 1 after", cnew.isLeadEngine(e1));
		Assert.assertFalse("Consist new Lead is not Engine 2 after", cnew.isLeadEngine(e2));
		Assert.assertFalse("Consist new Lead is not Engine 3 after", cnew.isLeadEngine(e3));

		//  Move engine 3 to the new consist.
		e3.setConsist(cnew);
		Assert.assertEquals("Consist Name for engine 1 after3", "TESTCONSISTNEW", e1.getConsistName());
		Assert.assertEquals("Consist Name for engine 2 after3", "TESTCONSISTOLD", e2.getConsistName());
		Assert.assertEquals("Consist Name for engine 3 after3", "TESTCONSISTNEW", e3.getConsistName());
		Assert.assertEquals("Consist old length after3", 59+4, cold.getLength());
		Assert.assertEquals("Consist new length after3", 56+4+45+4, cnew.getLength());
		Assert.assertFalse("Consist old Lead is not Engine 1 after3", cold.isLeadEngine(e1));
		Assert.assertTrue("Consist old Lead is Engine 2 after3", cold.isLeadEngine(e2));
		Assert.assertFalse("Consist old Lead is not Engine 3 after3", cold.isLeadEngine(e3));
		Assert.assertTrue("Consist new Lead is Engine 1 after3", cnew.isLeadEngine(e1));
		Assert.assertFalse("Consist new Lead is not Engine 2 after3", cnew.isLeadEngine(e2));
		Assert.assertFalse("Consist new Lead is not Engine 3 after3", cnew.isLeadEngine(e3));
	}


	// test location Xml create support
	public void testXMLCreate() throws Exception {

                EngineManager manager = EngineManager.instance();
                List tempengineList = manager.getEnginesByIdList();

                Assert.assertEquals("Starting Number of Engines", 0, tempengineList.size());
                manager.newEngine("CP", "Test Number 1");
                manager.newEngine("ACL", "Test Number 2");
                manager.newEngine("CP", "Test Number 3");

                tempengineList = manager.getEnginesByIdList();

                Assert.assertEquals("New Number of Engines", 3, tempengineList.size());
                Assert.assertEquals("New Engine by Id 1", "Test Number 1", manager.getEngineById("CPTest Number 1").getNumber());
                Assert.assertEquals("New Engine by Id 2", "Test Number 2", manager.getEngineById("ACLTest Number 2").getNumber());
                Assert.assertEquals("New Engine by Id 3", "Test Number 3", manager.getEngineById("CPTest Number 3").getNumber());

                Assert.assertEquals("New Location by Road+Name 1", "Test Number 1", manager.getEngineByRoadAndNumber("CP", "Test Number 1").getNumber());
                Assert.assertEquals("New Location by Road+Name 2", "Test Number 2", manager.getEngineByRoadAndNumber("ACL", "Test Number 2").getNumber());
                Assert.assertEquals("New Location by Road+Name 3", "Test Number 3", manager.getEngineByRoadAndNumber("CP", "Test Number 3").getNumber());

                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setBuilt("1923");
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setColor("Black");
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setComment("Nice runner");
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setConsist(consist);
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setDestination(destination, track);
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setHp("23");
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setLength("50");
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setLocation(location, track);
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setModel("E8");
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setMoves(5);
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setOwner("TestOwner");
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setRouteDestination(routeDestination);
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setRouteLocation(routeLocation);
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setSavedRouteId(id);
//                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setTrain(train);
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setWeight("87");
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setWeightTons("97");
                
                
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setType("Gas Turbine");
                
                manager.getEngineByRoadAndNumber("CP", "Test Number 1").setModel("E8");
                
/*
		manager.getLocationByName("Test Location 1").setLocationOps(Location.NORMAL);
		manager.getLocationByName("Test Location 1").setSwitchList(true);
		manager.getLocationByName("Test Location 1").setTrainDirections(Location.EAST+Location.WEST);
		manager.getLocationByName("Test Location 1").addTypeName("Baggage");
		manager.getLocationByName("Test Location 1").addTypeName("BoxCar");
		manager.getLocationByName("Test Location 1").addTypeName("Caboose");
		manager.getLocationByName("Test Location 1").addTypeName("Coal");
		manager.getLocationByName("Test Location 1").addTypeName("Engine");
		manager.getLocationByName("Test Location 1").addTypeName("Hopper");
                manager.getLocationByName("Test Location 2").setComment("Test Location 2 Comment");
		manager.getLocationByName("Test Location 2").setLocationOps(Location.NORMAL);
		manager.getLocationByName("Test Location 2").setSwitchList(true);
		manager.getLocationByName("Test Location 2").setTrainDirections(Location.EAST+Location.WEST);
		manager.getLocationByName("Test Location 2").addTypeName("Baggage");
		manager.getLocationByName("Test Location 2").addTypeName("BoxCar");
		manager.getLocationByName("Test Location 2").addTypeName("Caboose");
		manager.getLocationByName("Test Location 2").addTypeName("Coal");
		manager.getLocationByName("Test Location 2").addTypeName("Engine");
		manager.getLocationByName("Test Location 2").addTypeName("Hopper");
                manager.getLocationByName("Test Location 3").setComment("Test Location 3 Comment");
		manager.getLocationByName("Test Location 3").setLocationOps(Location.NORMAL);
		manager.getLocationByName("Test Location 3").setSwitchList(true);
		manager.getLocationByName("Test Location 3").setTrainDirections(Location.EAST+Location.WEST);
		manager.getLocationByName("Test Location 3").addTypeName("Baggage");
		manager.getLocationByName("Test Location 3").addTypeName("BoxCar");
		manager.getLocationByName("Test Location 3").addTypeName("Caboose");
		manager.getLocationByName("Test Location 3").addTypeName("Coal");
		manager.getLocationByName("Test Location 3").addTypeName("Engine");
		manager.getLocationByName("Test Location 3").addTypeName("Hopper");
*/
/*                
                locationList = manager.getLocationsByIdList();
                Assert.assertEquals("New Number of Locations", 3, locationList.size());

                for (int i = 0; i < locationList.size(); i++) {
                    String locationId = (String)locationList.get(i);
                    Location loc = manager.getLocationById(locationId);
                    String locname = loc.getName();
                    if (i == 0) {
                        Assert.assertEquals("New Location by Id List 1", "Test Location 2", locname);
                    }
                    if (i == 1) {
                        Assert.assertEquals("New Location by Id List 2", "Test Location 1", locname);
                    }
                    if (i == 2) {
                        Assert.assertEquals("New Location by Id List 3", "Test Location 3", locname);
                    }
                }

*/
/*                
                locationList = manager.getLocationsByNameList();
                Assert.assertEquals("New Number of Locations", 3, locationList.size());

                for (int i = 0; i < locationList.size(); i++) {
                    String locationId = (String)locationList.get(i);
                    Location loc = manager.getLocationById(locationId);
                    String locname = loc.getName();
                    if (i == 0) {
                        Assert.assertEquals("New Location by Name List 1", "Test Location 1", locname);
                    }
                    if (i == 1) {
                        Assert.assertEquals("New Location by Name List 2", "Test Location 2", locname);
                    }
                    if (i == 2) {
                        Assert.assertEquals("New Location by Name List 3", "Test Location 3", locname);
                    }
                }
*/
                

                EngineManagerXml.instance().writeOperationsEngineFile();

                // Add some more engines and write file again
                // so we can test the backup facility
                manager.newEngine("CP", "Test Number 4");
                manager.newEngine("CP", "Test Number 5");
                manager.newEngine("CP", "Test Number 6");
                manager.getEngineByRoadAndNumber("ACL", "Test Number 2").setComment("Test Engine 2 Changed Comment");
                
                EngineManagerXml.instance().writeOperationsEngineFile();
        }

	// TODO: Add test for import

	// TODO: Add test to create xml file

	// TODO: Add test to read xml file

    // from here down is testing infrastructure

    // Ensure minimal setup for log4J
    @Override
    protected void setUp() {
        apps.tests.Log4JFixture.setUp();
        
        // This test doesn't touch setup but we'll protect
        // Repoint OperationsXml to JUnitTest subdirectory
        String tempstring = OperationsXml.getOperationsDirectoryName();
        if (!tempstring.contains(File.separator+"JUnitTest")){
        	OperationsXml.setOperationsDirectoryName(OperationsXml.getOperationsDirectoryName()+File.separator+"JUnitTest");
        	OperationsXml.setOperationsFileName("OperationsJUnitTest.xml"); 
        }
        
        // This test doesn't touch routes but we'll protect
        // Repoint RouteManagerXml to JUnitTest subdirectory
        tempstring = RouteManagerXml.getOperationsDirectoryName();
        if (!tempstring.contains(File.separator+"JUnitTest")){
        	RouteManagerXml.setOperationsDirectoryName(RouteManagerXml.getOperationsDirectoryName()+File.separator+"JUnitTest");
        	RouteManagerXml.setOperationsFileName("OperationsJUnitTestRouteRoster.xml");
        }
        
        // Repoint EngineManagerXml to JUnitTest subdirectory
        tempstring = EngineManagerXml.getOperationsDirectoryName();
        if (!tempstring.contains(File.separator+"JUnitTest")){
        	EngineManagerXml.setOperationsDirectoryName(EngineManagerXml.getOperationsDirectoryName()+File.separator+"JUnitTest");
        	EngineManagerXml.setOperationsFileName("OperationsJUnitTestEngineRoster.xml");
        }
        
        // This test doesn't touch cars but we'll protect
        // Repoint CarManagerXml to JUnitTest subdirectory
        tempstring = CarManagerXml.getOperationsDirectoryName();
        if (!tempstring.contains(File.separator+"JUnitTest")){
        	CarManagerXml.setOperationsDirectoryName(CarManagerXml.getOperationsDirectoryName()+File.separator+"JUnitTest");
        	CarManagerXml.setOperationsFileName("OperationsJUnitTestCarRoster.xml");
        }
        
        // Repoint LocationManagerXml to JUnitTest subdirectory
        tempstring = LocationManagerXml.getOperationsDirectoryName();
        if (!tempstring.contains(File.separator+"JUnitTest")){
        	LocationManagerXml.setOperationsDirectoryName(LocationManagerXml.getOperationsDirectoryName()+File.separator+"JUnitTest");
        	LocationManagerXml.setOperationsFileName("OperationsJUnitTestLocationRoster.xml");
        }
        
        // Repoint TrainManagerXml to JUnitTest subdirectory
        tempstring = TrainManagerXml.getOperationsDirectoryName();
        if (!tempstring.contains(File.separator+"JUnitTest")){
        	TrainManagerXml.setOperationsDirectoryName(TrainManagerXml.getOperationsDirectoryName()+File.separator+"JUnitTest");
        	TrainManagerXml.setOperationsFileName("OperationsJUnitTestTrainRoster.xml");
        }
    	
        XmlFile.ensurePrefsPresent(XmlFile.prefsDir()+File.separator+LocationManagerXml.getOperationsDirectoryName());

        // Need to clear out EngineManager global variables
        EngineManager manager = EngineManager.instance();
        List tempconsistList = manager.getConsistNameList();
        for (int i = 0; i < tempconsistList.size(); i++) {
            String consistId = (String)tempconsistList.get(i);
            manager.deleteConsist(consistId);
        }
        EngineModels.instance().dispose();
        EngineLengths.instance().dispose();
        manager.dispose();
    }

	public OperationsEnginesTest(String s) {
		super(s);
	}

	// Main entry point
	static public void main(String[] args) {
		String[] testCaseName = {"-noloading", OperationsEnginesTest.class.getName()};
		junit.swingui.TestRunner.main(testCaseName);
	}

	// test suite from all defined tests
	public static Test suite() {
		TestSuite suite = new TestSuite(OperationsEnginesTest.class);
		return suite;
	}

    // The minimal setup for log4J
    @Override
    protected void tearDown() { apps.tests.Log4JFixture.tearDown(); }
}
