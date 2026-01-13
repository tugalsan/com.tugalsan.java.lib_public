package com.tugalsan.java.lib.file.pdf.sign.server;

import module com.tugalsan.java.core.file.properties;
import module com.tugalsan.java.core.file;
import module com.tugalsan.java.core.function;
import module com.tugalsan.java.core.log;
import module com.tugalsan.java.core.os;
import module com.tugalsan.java.core.union;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

@Deprecated //JUST USE com.tugalsan.java.core.file.pdf.pdfbox3.server.TS_FilePdfBox3UtilsSign.sign(TS_FilePdfBox3UtilsSign.SignConfig signConfig, Path pathPdfInput, Path pathPdfOutput);
public class TS_LibFilePdfSignUtils {

    final private static TS_Log d = TS_Log.of(true, TS_LibFilePdfSignUtils.class);

    public static Optional<Path> pathDriver() {
        //var driverPackageName = TS_LibFilePdfSignUtils.class.getPackageName().replace(".lib.", ".dsk.").replace(".server", "");
        var driverPackageName = "com.tugalsan.dsk.pdf.sign";
        d.ci("pathDriver", "driverPackageName", driverPackageName);
        for (var root : File.listRoots()) {
            var pathDriver = root.toPath().resolve("bin").resolve(driverPackageName)
                    .resolve("home").resolve("target")
                    .resolve(driverPackageName + "-1.0-SNAPSHOT-jar-with-dependencies.jar");
            if (TS_FileUtils.isExistFile(pathDriver)) {
                d.cr("pathDriver", pathDriver.toAbsolutePath().toString(), "found");
                return Optional.of(pathDriver);
            } else {
                d.ci("pathDriver", pathDriver.toAbsolutePath().toString(), "not-found");
            }
        }
        return Optional.empty();
    }

    public static Path pathOutput(Path rawPdf) {
        var label = TS_FileUtils.getNameLabel(rawPdf);
        return rawPdf.resolveSibling(label + "_executeed.pdf");
    }

    public static Path pathConfig(Path rawPdf) {
        return rawPdf.resolveSibling("config.properties");
    }

    public static Properties makeConfig(TS_LibFilePdfSignCfgSsl cfgSssl, TS_LibFilePdfSignCfgDesc cfgDesc, Path pathInput) {
        var props = new Properties();
        props.setProperty("certification.level", "NOT_CERTIFIED");
        props.setProperty("crl.enabled", "false");
        props.setProperty("enc.home", System.getProperty("user.home"));
        props.setProperty("enc.keyPwd", cfgSssl.keyStorePass());
        props.setProperty("enc.keystorePwd", cfgSssl.keyStorePass());
        props.setProperty("hash.algorithm", "SHA512");
        props.setProperty("inpdf.file", pathInput.toAbsolutePath().toString());
        props.setProperty("keystore.alias", "myallias");
        props.setProperty("keystore.file", cfgSssl.keyStorePath().toAbsolutePath().toString());
        props.setProperty("keystore.keyIndex", "0");
        props.setProperty("keystore.type", cfgSssl.keyType());
        props.setProperty("ocsp.enabled", "false");
        props.setProperty("outpdf.file", pathOutput(pathInput).toAbsolutePath().toString());
        props.setProperty("pdf.encryption", "NONE");
        props.setProperty("signature.append", "false");
        props.setProperty("signature.contact", cfgDesc.contact());
        props.setProperty("signature.location", cfgDesc.place());
        props.setProperty("signature.reason", cfgDesc.reason());
        props.setProperty("store.passwords", "true");
        props.setProperty("tsa.enabled", "true");
        props.setProperty("tsa.serverAuthn", "NONE");
        props.setProperty("tsa.url", cfgSssl.tsa().toString());
        return props;
    }

    public static TGS_UnionExcuse<Path> execute(Path driver, TS_LibFilePdfSignCfgSsl cfgSssl, TS_LibFilePdfSignCfgDesc cfgDesc, Path pathInput) {
        return TGS_FuncMTCUtils.call(() -> {
            d.ci("execute", "pathInput", pathInput);
            //CREATE TMP-INPUT BY MAIN-INPUT
            var tmp = Files.createTempDirectory("tmp").toAbsolutePath();
            var _pathInput = tmp.resolve("_pathInput.pdf");
            TS_FileUtils.copyAs(pathInput, _pathInput, true);

            //IF SINGED, COPY TMP-OUTPUT TO MAIN-OUTPUT
            var u = _execute(driver, cfgSssl, cfgDesc, _pathInput);
            if (u.isExcuse()) {
                return u.toExcuse();
            }
            var pdfOutput = pathOutput(pathInput);
            TS_FileUtils.copyAs(u.value(), pdfOutput, true);

            return TGS_UnionExcuse.of(pdfOutput);
        }, e -> TGS_UnionExcuse.ofExcuse(e));
    }

    private static TGS_UnionExcuse<Path> _execute(Path driver, TS_LibFilePdfSignCfgSsl cfgSssl, TS_LibFilePdfSignCfgDesc cfgDesc, Path pathInput) {
        var pathOutput = pathOutput(pathInput);
        d.ci("_execute", "pathOutput", pathOutput);
        var pathConfig = pathConfig(pathInput);
        d.ci("_execute", "pathConfig", pathConfig);
        TS_FilePropertiesUtils.write(makeConfig(cfgSssl, cfgDesc, pathInput), pathConfig);
        return TGS_FuncMTCUtils.call(() -> {
            d.ci("_execute", "cfgSssl", cfgSssl);
            d.ci("_execute", "cfgDesc", cfgDesc);
            d.ci("_execute", "rawPdf", pathInput);
            //CHECK IN-FILE
            if (pathInput == null || !TS_FileUtils.isExistFile(pathInput)) {
                return TGS_UnionExcuse.ofExcuse(d.className(), "_execute", "pathInput not exists-" + pathInput);
            }
            if (TS_FileUtils.isEmptyFile(pathInput)) {
                return TGS_UnionExcuse.ofExcuse(d.className(), "_execute", "pathInput is empty-" + pathInput);
            }
            //CHECK OUT-FILE
            TS_FileUtils.deleteFileIfExists(pathOutput);
            if (TS_FileUtils.isExistFile(pathOutput)) {
                return TGS_UnionExcuse.ofExcuse(d.className(), "_execute", "pathOutput cleanup error-" + pathOutput);
            }
            //EXECUTE
            List<String> args = new ArrayList();
            args.add("\"" + TS_OsJavaUtils.getPathJava().resolveSibling("java.exe") + "\"");
            args.add("-jar");
            args.add("\"" + driver.toAbsolutePath().toString() + "\"");
            args.add("--load-properties-file");
            args.add("\"" + pathConfig.toAbsolutePath().toString() + "\"");
            d.cr("_execute", "args", args);
            var cmd = args.stream().collect(Collectors.joining(" "));
            d.cr("_execute", "cmd", cmd);
            var p = TS_OsProcess.of(args);
            //CHECK OUT-FILE
            if (!TS_FileUtils.isExistFile(pathOutput)) {
                d.ce("_execute", "cmd", p.toString());
                return TGS_UnionExcuse.ofExcuse(d.className(), "_execute", "pathOutput not created-" + pathOutput);
            }
            if (TS_FileUtils.isEmptyFile(pathOutput)) {
                d.ce("_execute", "cmd", p.toString());
                TS_FileUtils.deleteFileIfExists(pathOutput);
                return TGS_UnionExcuse.ofExcuse(d.className(), "_execute", "pathOutput is empty-" + pathOutput);
            }
            //RETURN
            d.cr("_execute", "returning pathOutput", pathOutput);
            return TGS_UnionExcuse.of(pathOutput);
        }, e -> {
            //HANDLE EXCEPTION
            d.ce("_execute", "HANDLE EXCEPTION...");
            TS_FileUtils.deleteFileIfExists(pathOutput);
            return TGS_UnionExcuse.ofExcuse(e);
        }, () -> TS_FileUtils.deleteFileIfExists(pathConfig));
    }
}
