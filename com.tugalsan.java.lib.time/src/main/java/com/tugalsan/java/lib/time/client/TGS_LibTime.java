package com.tugalsan.java.lib.time.client;

import com.tugalsan.java.core.url.client.TGS_Url;

public class TGS_LibTime {

    public static String PATTERN() {
        return "YYYYAAGG HHMMSS";
    }

    public static String CMD_GET() {
        return "get";
    }

    public static TGS_Url url(CharSequence ip, int sslPort) {
        return TGS_Url.of("https://" + ip + ":" + sslPort + "/com.tugalsan.gvm.time?cmd=get");
    }
}
