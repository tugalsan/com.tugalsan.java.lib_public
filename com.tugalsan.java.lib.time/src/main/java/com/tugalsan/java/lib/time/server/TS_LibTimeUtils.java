package com.tugalsan.java.lib.time.server;

import module com.tugalsan.java.core.cast;
import module com.tugalsan.java.core.log;
import module com.tugalsan.java.core.string;
import module com.tugalsan.java.core.union;
import module com.tugalsan.java.core.url;
import module com.tugalsan.java.lib.time;
import module com.tugalsan.java.core.time;
import java.time.*;
import java.util.*;

public class TS_LibTimeUtils {

    final private static TS_Log d = TS_Log.of(false, TS_LibTimeUtils.class);

    public static TGS_UnionExcuse<TGS_Time> fetch(CharSequence ip, int sslPort) {
        var url = TGS_LibTime.url(ip, sslPort);
        var urlData = TS_UrlDownloadUtils.toText(url, Duration.ofSeconds(10));
        if (TGS_StringUtils.cmn().isNullOrEmpty(urlData)) {
            return TGS_UnionExcuse.ofExcuse(d.className(), "fetch",
                    "REASON: TGS_StringUtils.cmn().isNullOrEmpty(urlData)"
            );
        }
        var pattern = TGS_LibTime.PATTERN();
        if (pattern.length() != urlData.length()) {
            return TGS_UnionExcuse.ofExcuse(d.className(), "fetch", TGS_StringUtils.cmn().concat(
                    "REASON: pattern.length() != urlData.length() where",
                    ", pattern: ", pattern,
                    ", urlData: ", urlData,
                    ", url:", url.toString()
            ));
        }
        var st = new StringTokenizer(urlData, " ");
        var tokenCount = st.countTokens();
        if (tokenCount != 2) {
            return TGS_UnionExcuse.ofExcuse(d.className(), "fetch", TGS_StringUtils.cmn().concat(
                    "REASON: tokenCount != 2 where",
                    ", tokenCount: ", String.valueOf(tokenCount),
                    ", pattern: ", pattern,
                    ", urlData: ", urlData,
                    ", url:", url.toString()
            ));
        }
        var dateStr = st.nextToken();
        var dateLng = TGS_CastUtils.toLong(dateStr).orElse(null);
        if (dateLng == null) {
            return TGS_UnionExcuse.ofExcuse(d.className(), "fetch", TGS_StringUtils.cmn().concat(
                    "REASON: dateLng == null where",
                    ", pattern: ", pattern,
                    ", urlData: ", urlData,
                    ", dateStr: ", dateStr,
                    ", url:", url.toString()
            ));
        }
        var timeStr = st.nextToken();
        var timeLng = TGS_CastUtils.toLong(timeStr).orElse(null);
        if (timeLng == null) {
            return TGS_UnionExcuse.ofExcuse(d.className(), "fetch", TGS_StringUtils.cmn().concat(
                    "REASON: dateLng == null where",
                    ", pattern: ", pattern,
                    ", urlData: ", urlData,
                    ", dateStr: ", dateStr,
                    ", timeStr: ", timeStr,
                    ", url:", url.toString()
            ));
        }
        return TGS_UnionExcuse.of(TGS_Time.ofDateAndTime(dateLng, timeLng));
    }
}
