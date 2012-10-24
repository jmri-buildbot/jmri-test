package jmri.util.swing;

import javax.swing.JLabel;
import java.awt.Dimension;
/**
 * Status Bar
 *
 * A little status bar widget that can be put at the bottom of
 * a panel.
 */

public class StatusBar extends JLabel {

    /** Creates a new instance of StatusBar */
    public StatusBar() {
        super();
        super.setPreferredSize(new Dimension(100, 16));
        setMessage("Ready");
    }

    /** Update the status bar message */
    public void setMessage(String message) {
        setText(" "+message);        
    }        
}
