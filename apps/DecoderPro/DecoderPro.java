// DecoderPro.java

package jmri.apps;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import BasicWindowMonitor;

/** 
 * DecoderPro application. 
 *
 * @author			Bob Jacobsen
 * @version			$Id: DecoderPro.java,v 1.7 2002-02-02 07:06:59 jacobsen Exp $
 */
public class DecoderPro extends JPanel {
	public DecoderPro() {

        super(true);
	
	// create basic GUI
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Create a menu bar and give it a bevel border
        menuBar = new JMenuBar();
        menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));
    
    // load preferences
    	jmri.apps.DecoderProConfigAction prefs 
    				= new jmri.apps.DecoderProConfigAction("Preferences...");
        
	// populate GUI
			
		// create actions with side-effects if you need to reference them more than once
		Action paneprog = new jmri.jmrit.symbolicprog.tabbedframe.PaneProgAction("New Programmer...");
		Action quit = new AbstractAction("Quit"){
    				public void actionPerformed(ActionEvent e) {
    					System.exit(0);
    				}
        		};
        		
        // Create menu categories and add to the menu bar, add actions to menus
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
	  		fileMenu.add(paneprog);
        	fileMenu.add(new JSeparator());        	
        	fileMenu.add(quit);

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
	        editMenu.add(prefs);

        JMenu toolMenu = new JMenu("Tools");
        menuBar.add(toolMenu);
	        toolMenu.add(new jmri.jmrit.XmlFileCheckAction("Check XML File", this));
	        toolMenu.add(new jmri.jmrit.NameCheckAction("Check decoder names", this));
	        toolMenu.add(new jmri.jmrit.tabbedframe.ProgCheckAction("Check programmer names", this));
	        toolMenu.add(new jmri.jmrit.decoderdefn.DecoderIndexCreateAction("Create decoder index"));

        JMenu debugMenu = new JMenu("Debug");
        menuBar.add(debugMenu);
	        debugMenu.add(new jmri.jmrix.loconet.locomon.LocoMonAction("LocoNet Monitor"));
	        debugMenu.add(new jmri.jmrix.nce.ncemon.NceMonAction("Nce Command Monitor"));
	        debugMenu.add(new jmri.jmrit.MemoryFrameAction("Memory usage monitor"));

		// Label & text
		JPanel pane1 = new JPanel();
			pane1.setLayout(new FlowLayout());
			pane1.add(new JLabel(new ImageIcon(ClassLoader.getSystemResource("decoderpro.gif"),"Decoder Pro label"), JLabel.LEFT));
			JPanel pane2 = new JPanel();
				pane2.setLayout(new BoxLayout(pane2, BoxLayout.Y_AXIS));
				pane2.add(new JLabel(" Decoder Pro 0.9b7 by Bob Jacobsen "));
				pane2.add(new JLabel("   http://jmri.sf.net/DecoderPro "));
				pane2.add(new JLabel(" "));
				pane2.add(new JLabel(" Connected via "+prefs.getCurrentProtocolName()));
				pane2.add(new JLabel(" on port "+prefs.getCurrentPortName()));
			pane1.add(pane2);
		add(pane1);
		
		// Buttons
		JButton b1 = new JButton("Program locomotive ...");
		b1.addActionListener(paneprog);
		b1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		add(b1);
		
		JButton q1 = new JButton("Quit");
		q1.addActionListener(quit);
		q1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		add(q1);
	}

	// Main entry point
    public static void main(String s[]) {
    
    	// initialize log4j - from logging control file (lcf) only 
    	// if can find it!
    	String logFile = "default.lcf";
    	try {
	    	if (new java.io.File(logFile).canRead()) {
	   	 		org.apache.log4j.PropertyConfigurator.configure("default.lcf");
	    	} else {
		    	org.apache.log4j.BasicConfigurator.configure();
	    	}
	    }
		catch (java.lang.NoSuchMethodError e) { System.out.println("Exception starting logging: "+e); }

		log.info("DecoderPro starts");
		    		
    	// create the demo frame and menus
        DecoderPro containedPane = new DecoderPro();
        JFrame frame = new JFrame("DecoderPro");
        frame.addWindowListener(new BasicWindowMonitor());
        frame.setJMenuBar(containedPane.menuBar);
        frame.getContentPane().add(containedPane);
        frame.pack();
        frame.setVisible(true);
    }
	
	// GUI members
    private JMenuBar menuBar;	
	
   static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(DecoderPro.class.getName());
}

