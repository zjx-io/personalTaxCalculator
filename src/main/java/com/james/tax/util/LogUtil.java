package com.james.tax.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LogUtil {
    static String logPath = System.getProperty("user.dir") + File.separator + "logs" + File.separator + "info.log";


    public static String getLog() {
        return FileUtil.readUtf8String(logPath);
    }

    public static void removeLog() {
        FileUtil.writeString("",logPath, StandardCharsets.UTF_8);
    }
}
