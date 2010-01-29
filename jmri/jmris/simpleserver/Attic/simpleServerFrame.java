// simpleServerFrame.java

package jmri.jmris.simpleserver;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Frame displaying start/stop buttons for the JMRI server.
 *
 * @author			Paul Bender  Copyright (C) 2009
 * @version			$Revision: 1.1 $
 */
public class simpleServerFrame extends jmri.util.JmriJFrame {

    public simpleServerFrame() {
        this("Jmri Simple Server Starter");
    }

    public simpleServerFrame(String FrameName) {
        super(FrameName);
        getContentPane().setLayout(new BoxLayout(getContentPane(), 
				   BoxLayout.Y_AXIS));

        JPanel pane0 = new JPanel();
        pane0.add(startButton);
        pane0.add(stopButton);
        pane0.add(closeButton);
        getContentPane().add(pane0);

        // and prep for display
        pack();

        // install start button handler
        startButton.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent a) {
			startSimpleServer();
                }
            }
        );

        // install stop button handler
        stopButton.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent a) {
			stopSimpleServer();
                }
            }
        );

        // install close button handler
        closeButton.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent a) {
                	setVisible(false);
        		dispose();
                }
            }
        );

    }

    JToggleButton startButton = new JToggleButton("Start Simple Server");
    JToggleButton stopButton = new JToggleButton("Stop Simple Server");
    JToggleButton closeButton = new JToggleButton("Close Simple Server");

    // Close the window when the close box is clicked
    void thisWindowClosing(java.awt.event.WindowEvent e) {
        setVisible(false);
        dispose();
    }

    public void dispose() {
        // take apart the JFrame
        super.dispose();
    }

    public void startSimpleServer() {
	simpleServer.instance().start();
    }

    public void stopSimpleServer() {
	simpleServer.instance().stop();
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(simpleServerFrame.class.getName());

}
