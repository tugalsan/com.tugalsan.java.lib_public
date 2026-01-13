module com.tugalsan.java.core.servlet.http {
    requires jdk.httpserver;
//    requires com.tugalsan.java.core.crypto;
    requires com.tugalsan.java.core.function;
    requires com.tugalsan.java.core.tuple;
    requires com.tugalsan.java.core.union;
    requires com.tugalsan.java.core.charset;
    requires com.tugalsan.java.core.url;
    requires com.tugalsan.java.core.network;
    requires com.tugalsan.java.core.log;
    requires com.tugalsan.java.core.file;    
    requires com.tugalsan.java.core.stream;
    requires com.tugalsan.java.core.string;
    exports com.tugalsan.java.core.servlet.http.sun.server;
}
