package jmri.jmrix.zimo.swing;

import jmri.jmrix.zimo.Mx1SystemConnectionMemo;

/**
 * JPanel extension to handle automatic creation of window title and help
 * reference for Mrc panels
 * <p>
 * For use with JmriAbstractAction, etc
 *
 * @author Bob Jacobsen Copyright 2010 Copied from nce.swing
 * @author Ken Cameron 2014
 * @author Kevin Dickerson 2014
 * @version $Revision: 22942 $
 */
abstract public class Mx1Panel extends jmri.util.swing.JmriPanel implements Mx1PanelInterface {

    /**
     *
     */
    private static final long serialVersionUID = -8704964386237089071L;

    /**
     * make "memo" object available as convenience
     */
    protected Mx1SystemConnectionMemo memo;

    public void initComponents(Mx1SystemConnectionMemo memo) throws Exception {
        this.memo = memo;
    }

    public void initContext(Object context) throws Exception {
        if (context instanceof Mx1SystemConnectionMemo) {
            try {
                initComponents((Mx1SystemConnectionMemo) context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
