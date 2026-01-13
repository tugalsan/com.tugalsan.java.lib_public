package com.tugalsan.java.lib.file.tmcr.server.code.parser;

import module com.tugalsan.java.core.file.common;

public class TS_LibFileTmcrParser_WhiteSpace {

    private TS_LibFileTmcrParser_WhiteSpace() {
    }

    public static boolean is_WHITE_SPACE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLine.startsWith("//") || fileCommonConfig.macroLine.isEmpty();
    }
}
