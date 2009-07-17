package jmri.configurexml;

import jmri.InstanceManager;
import jmri.jmrit.XmlFile;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

//import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;

/**
 * Provides the mechanisms for storing an entire layout configuration
 * to XML.  "Layout" refers to the hardware:  Specific communcation
 * systems, etc.
 * @see <A HREF="package-summary.html">Package summary for details of the overall structure</A>
 * @author Bob Jacobsen  Copyright (c) 2002, 2008
 * @version $Revision: 1.58 $
 */
public class ConfigXmlManager extends jmri.jmrit.XmlFile
    implements jmri.ConfigureManager {

    /**
     * Define the current DTD version string for the layout-config DTD.
     * See the <A HREF="package-summary.html#DTD">DTD versioning discussion</a>
     */
    static final public String dtdVersion = "2-7-2";
    
    public ConfigXmlManager() {
    }

    public void registerPref(Object o) {
        // skip if already present, leaving in original order
        if (plist.contains(o)) return;
        // find the class name of the adapter
        String adapter = adapterName(o);
        if (log.isDebugEnabled()) log.debug("register "+o+" adapter "+adapter);
        if (adapter!=null)
            try {
                Class.forName(adapter);
            } catch (java.lang.ClassNotFoundException ex) {
                locateClassFailed(ex, adapter, o);
            }
        // and add to list
        plist.add(o);
    }

    /**
     * Remove the registered preference items.  This is used
     * e.g. when a GUI wants to replace the preferences with new
     * values.
     */
    public void removePrefItems(){
        if (log.isDebugEnabled()) log.debug("removePrefItems dropped "+plist.size());
        plist.clear();
    }

    public Object findInstance(Class<?> c, int index) {
        ArrayList<Object> temp = new ArrayList<Object>(plist);
        temp.addAll(clist);
        temp.addAll(tlist);
        temp.addAll(ulist);
        for (int i=0; i<temp.size(); i++) {
            if (c.isInstance(temp.get(i))) { 
                index--;
                if (index==0) return temp.get(i);
            }
        }
        return null;
    }

    public void registerConfig(Object o) {
        // skip if already present, leaving in original order
        if (clist.contains(o)) return;
        // find the class name of the adapter
        String adapter = adapterName(o);
        if (adapter!=null)
            try {
                Class.forName(adapter);
            } catch (java.lang.ClassNotFoundException ex) {
                locateClassFailed(ex, adapter, o);
            } catch (java.lang.NoClassDefFoundError ex) {
                locateClassFailed(ex, adapter, o);
            }
        // and add to list
        clist.add(o);
    }
    public void registerTool(Object o) {
        // skip if already present, leaving in original order
        if (tlist.contains(o)) return;
        // find the class name of the adapter
        String adapter = adapterName(o);
        if (adapter!=null)
            try {
                Class.forName(adapter);
            } catch (java.lang.ClassNotFoundException ex) {
                locateClassFailed(ex, adapter, o);
            }
        // and add to list
        tlist.add(o);
    }
    /**
     * Register an object whose state is to be tracked.
     * It is not an error if the original object was already registered.
     * @param o The object, which must have an
     *              associated adapter class.
     */
    public void registerUser(Object o) {
        // skip if already present, leaving in original order
        if (ulist.contains(o)) return;
        // find the class name of the adapter
        String adapter = adapterName(o);
        if (adapter!=null)
            try {
                Class.forName(adapter);
            } catch (java.lang.ClassNotFoundException ex) {
                locateClassFailed(ex, adapter, o);
            }
        // and add to list
        ulist.add(o);
    }

    public void deregister(Object o) {
        plist.remove(o);
        clist.remove(o);
        tlist.remove(o);
        ulist.remove(o);
    }

    ArrayList<Object> plist = new ArrayList<Object>();
    ArrayList<Object> clist = new ArrayList<Object>();
    ArrayList<Object> tlist = new ArrayList<Object>();
    ArrayList<Object> ulist = new ArrayList<Object>();

    /**
     * Find the name of the adapter class for an object.
     * @param o object of a configurable type
     * @return class name of adapter
     */
    public static String adapterName(Object o) {
        String className = o.getClass().getName();
        if (log.isDebugEnabled()) log.debug("handle object of class "+className);
        int lastDot = className.lastIndexOf(".");
        String result = null;

        if (lastDot>0) {
            // found package-class boundary OK
            result = className.substring(0,lastDot)
                +".configurexml."
                +className.substring(lastDot+1,className.length())
                +"Xml";
            if (log.isDebugEnabled()) log.debug("adapter class name is "+result);
            return result;
        } else {
            // no last dot found!
            log.error("No package name found, which is not yet handled!");
            return null;
        }
    }

    /**
     * Handle failure to load adapter class. Although only a
     * one-liner in this class, it is a separate member to facilitate testing.
     */
    void locateClassFailed(Throwable ex, String adapterName, Object o) {
        log.error(ex.getMessage()+" could not load adapter class "+adapterName);
        if (log.isDebugEnabled()) ex.printStackTrace();
    }

    protected Element initStore() {
        Element root = new Element("layout-config");
        return root;
    }
    protected void addPrefsStore(Element root) {
        for (int i=0; i<plist.size(); i++) {
            Object o = plist.get(i);
            Element e = elementFromObject(o);
            if (e!=null) root.addContent(e);
        }
    }
    protected void addConfigStore(Element root) {
        for (int i=0; i<clist.size(); i++) {
            Object o = clist.get(i);
            Element e = elementFromObject(o);
            if (e!=null) root.addContent(e);
        }
    }
    protected void addToolsStore(Element root) {
        for (int i=0; i<tlist.size(); i++) {
            Object o = tlist.get(i);
            Element e = elementFromObject(o);
            if (e!=null) root.addContent(e);
        }
    }
    protected void addUserStore(Element root) {
        for (int i=0; i<ulist.size(); i++) {
            Object o = ulist.get(i);
            Element e = elementFromObject(o);
            if (e!=null) root.addContent(e);
        }
    }
    protected void finalStore(Element root, File file) {
        try {
            Document doc = newDocument(root, dtdLocation+"layout-config-"+dtdVersion+".dtd");

            // add XSLT processing instruction
            // <?xml-stylesheet type="text/xsl" href="XSLT/DecoderID.xsl"?>
            java.util.Map<String,String> m = new java.util.HashMap<String,String>();
            m.put("type", "text/xsl");
            m.put("href", xsltLocation+"panelfile.xsl");
            ProcessingInstruction p = new ProcessingInstruction("xml-stylesheet", m);
            doc.addContent(0,p);

            writeXML(file, doc);
        } catch (java.io.FileNotFoundException ex3) {
            log.error("FileNotFound error writing file: "+ex3.getLocalizedMessage());
        } catch (java.io.IOException ex2) {
            log.error("IO error writing file: "+ex2.getLocalizedMessage());
        } 
    }

    /**
     * Writes config, tools and user to a file.
     * @param file
     */
    public void storeAll(File file) {
        Element root = initStore();
        addConfigStore(root);
        addToolsStore(root);
        addUserStore(root);
        finalStore(root, file);
    }
    /**
     * Writes prefs to a file.
     * @param file
     */
    public void storePrefs(File file) {
        Element root = initStore();
        addPrefsStore(root);
        finalStore(root, file);
    }

    /**
     * Writes prefs to a file.
     * @param file
     */
    public void storeConfig(File file) {
        Element root = initStore();
        addConfigStore(root);
        finalStore(root, file);
    }

     /**
     * Writes user and config info to a file.
     * <P>
     * Config is included here because it doesnt hurt to
     * read it again, and the user data (typically a panel)
     * requires it to be present first.
     * @param file
     */
    public void storeUser(File file) {
        Element root = initStore();
        addConfigStore(root);
        addUserStore(root);
        finalStore(root, file);
    }
    
    public boolean makeBackup(File file){
    	return makeBackupFile(defaultBackupDirectory, file);
    }
    
    String defaultBackupDirectory = XmlFile.prefsDir()+"backupPanels";

    static public Element elementFromObject(Object o) {
        String aName = adapterName(o);
        log.debug("store using "+aName);
        XmlAdapter adapter = null;
        try {
            adapter = (XmlAdapter)Class.forName(adapterName(o)).newInstance();
        } catch (java.lang.ClassNotFoundException ex1) {
            log.fatal("Cannot load configuration adapter for "+o.getClass().getName()+" due to "+ex1);
        } catch (java.lang.IllegalAccessException ex2) {
            log.fatal("Cannot load configuration adapter for "+o.getClass().getName()+" due to "+ex2);
        } catch (java.lang.InstantiationException ex3) {
            log.fatal("Cannot load configuration adapter for "+o.getClass().getName()+" due to "+ex3);
        }
        if (adapter!=null){
            return adapter.store(o);
        } else {
            log.fatal("Cannot store configuration for "+o.getClass().getName());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
	public boolean load(File fi) {
        boolean result = true;
        try {
            Element root = super.rootFromFile(fi);
            // get the objects to load
            List<Element> items = root.getChildren();
            for (int i = 0; i<items.size(); i++) {
                // get the class, hence the adapter object to do loading
                Element item = items.get(i);
                String adapterName = item.getAttribute("class").getValue();
                log.debug("load via "+adapterName);
                try {
                    XmlAdapter adapter = (XmlAdapter)Class.forName(adapterName).newInstance();
                    // and do it
                    boolean loadStatus = adapter.load(item);
                    log.debug("load status for "+adapterName+" is "+loadStatus);
                    // if any adaptor load fails, then the entire load has failed
                    if (!loadStatus)
                    	result = false;
                } catch (Exception e) {
                    log.error("Exception while loading "+item.getName()+":"+e);
                    e.printStackTrace();
                    result = false;  // keep going, but return false to signal problem
                } catch (Throwable et) {
                    log.error("Thrown while loading "+item.getName()+":"+et);
                    et.printStackTrace();
                    result = false;  // keep going, but return false to signal problem
                }
            }

        } catch (java.io.FileNotFoundException e1) {
            // this returns false to indicate un-success, but not enough
            // of an error to require a message
            log.warn("File not found: "+fi.getAbsolutePath());
            return false;
        } catch (org.jdom.JDOMException e) {
            log.error("Exception reading: "+e);
            e.printStackTrace();
            return false;
        } catch (java.lang.Exception e) {
            log.error("Exception reading: "+e);
            e.printStackTrace();
            return false;
        }
		// all loaded, initialize objects as necessary
		InstanceManager.logixManagerInstance().activateAllLogixs();
		InstanceManager.layoutBlockManagerInstance().initializeLayoutBlockPaths();
        new jmri.jmrit.catalog.configurexml.DefaultCatalogTreeManagerXml().readCatalogTrees();
        return result;
    }

    static public String fileLocation = "layout"+File.separator;

    /**
     * Find a file by looking
     * <UL>
     * <LI> in xml/layout/ in the preferences directory, if that exists
     * <LI> in xml/layout/ in the application directory, if that exists
     * <LI> in xml/ in the preferences directory, if that exists
     * <LI> in xml/ in the application directory, if that exists
     * <LI> at top level in the application directory
     * <LI>
     * </ul>
     * @param f Local filename, perhaps without path information
     * @return Corresponding File object
     */
    public File find(String f) {
        File result;
        result = findFile(f);
        if (result != null) {
            log.debug("found at "+result.getAbsolutePath());
            return result;
        } else if ( null != (result = findFile(fileLocation+f) )) {
            log.debug("found at "+result.getAbsolutePath());
            return result;
        } else if ((result = new File(f)).exists() ) {
            log.debug("found at "+result.getAbsolutePath());
            return result;
        } else {
            locateFileFailed(f);
            return null;
        }
    }

    /**
     * Report a failure to find a file.  This is a separate member
     * to ease testing.
     * @param f Name of file not located.
     */
    void locateFileFailed(String f) {
        log.warn("Could not locate file "+f);
    }

    // initialize logging
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ConfigXmlManager.class.getName());
}

