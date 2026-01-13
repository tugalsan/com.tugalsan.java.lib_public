module com.tugalsan.java.lib.vnc.desktop {
    requires java.logging;
    requires java.datatransfer;
    requires java.desktop;
    requires java.prefs;
    requires com.sun.jna.platform;
    requires com.tugalsan.java.core.desktop;
    requires com.tugalsan.java.core.thread;    
    requires com.tugalsan.java.core.charset;
    requires com.tugalsan.java.core.log;   
    requires com.tugalsan.java.core.function;
    exports com.tugalsan.java.lib.vnc.desktop.server;
}
