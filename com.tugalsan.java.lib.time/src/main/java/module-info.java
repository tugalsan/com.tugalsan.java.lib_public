module com.tugalsan.java.lib.time {
    requires com.tugalsan.java.core.time;
    requires com.tugalsan.java.core.url;
    requires com.tugalsan.java.core.string;
    requires com.tugalsan.java.core.cast;
    requires com.tugalsan.java.core.log;
    requires com.tugalsan.java.core.union;
    exports com.tugalsan.java.lib.time.client;
    exports com.tugalsan.java.lib.time.server;
}
