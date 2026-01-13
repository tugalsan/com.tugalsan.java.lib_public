package com.tugalsan.java.core.servlet.http.sun.server;

import module com.tugalsan.java.core.function;
import module com.tugalsan.java.core.file;
import module com.tugalsan.java.core.log;
import module com.tugalsan.java.core.string;
import module com.tugalsan.java.core.tuple;
import module jdk.httpserver;

public abstract class TS_SHttpHandlerAbstract<T> implements HttpHandler {

    final private static TS_Log d = TS_Log.of(false, TS_SHttpHandlerAbstract.class);

    final public String slash_path;
    final protected TGS_FuncMTU_OutTyped_In1<TGS_Tuple2<TGS_FileTypes, T>, TS_SHttpHandlerRequest> request;
    final protected TGS_FuncMTU_OutBool_In1<TS_SHttpHandlerRequest> allow;

    protected TS_SHttpHandlerAbstract(String slash_path, TGS_FuncMTU_OutBool_In1<TS_SHttpHandlerRequest> allow, TGS_FuncMTU_OutTyped_In1<TGS_Tuple2<TGS_FileTypes, T>, TS_SHttpHandlerRequest> request) {
        this.slash_path = TGS_FuncMTUEffectivelyFinal.ofStr()
                .anoint(val -> slash_path)
                .anointIf(TGS_StringUtils.cmn()::isNullOrEmpty, val -> {
                    d.ci("constructor", "TGS_StringUtils::isNullOrEmpty", "set as '/'");
                    return "/";
                })
                .anointIf(val -> val.charAt(0) != '/', val -> {
                    d.ci("constructor", "val.charAt(0) != '/'", "add '/' to the left");
                    return "/" + val;
                })
                .coronate();
        d.ci("constructor", "slash_path", slash_path);
        this.allow = allow;
        this.request = request;
    }
}
