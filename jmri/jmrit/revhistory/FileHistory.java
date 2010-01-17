package jmri.jmrit.revhistory;

import java.util.ArrayList;

/**
 * Memo class to remember a file revision history.
 * <p>
 * These can be nested:  A revision can come with a history.
 *
 * @author Bob Jacobsen  Copyright (c) 2010
 * @version $Revision: 1.1 $
 */
    
public class FileHistory {
    
    ArrayList<OperationMemo> list = new ArrayList<OperationMemo>();

    /**
     * Used to add a revision form
     * complete information created elsewhere
     */
    public void addOperation(
        String type,
        String date,
        String filename,
        FileHistory history
        )
    {
        OperationMemo r = new OperationMemo();
        r.type = type;
        r.date = date;
        r.filename = filename;
        r.history = history;
        
        list.add(r);
    }

    public void addOperation(OperationMemo r) {
        list.add(r);
    }
    
    public void addOperation(
        String type,
        String filename,
        FileHistory history
        )
    {
        OperationMemo r = new OperationMemo();
        r.type = type;
        r.date = (new java.util.Date()).toString();
        r.filename = filename;
        r.history = history;
        
        list.add(r);
    }

    
    public String toString(String prefix) {
        String retval = "";
        for (int i = 0; i < list.size(); i++) {
            OperationMemo r = list.get(i);
            retval += prefix+r.date+": "+r.type+" "+r.filename+"\n";
            if (r.history != null) {
                retval += r.history.toString(prefix+"    ");
            }
        }
        return retval;
    }
    
    public String toString() {
        return toString("");
    }
    
    public ArrayList<OperationMemo> getList() { return list; }
    
    /**
     * Memo class for each revision itself
     */
    public class OperationMemo {
        public String type;  // load, store
        public String date;
        public String filename;
        public FileHistory history;  // only with load
    }

}
    
