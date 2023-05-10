package com.james.tax;

import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.dialect.Props;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.math.BigDecimal;

/**
 * 个税参数工具类
 *
 * @author jianxuezhang
 * @date 2022/10/11
 */
@Slf4j
public class TaxPropsProvider {
    static String propertiesPath = System.getProperty("user.dir") + File.separator + "data" + File.separator + "tax.properties";
    static Props taxProps = Props.getProp(propertiesPath);


    public static void reload() {
        taxProps.load();
     log.info("重新加载配置成功!");
    }

    public static String showData() {
        return FileUtil.readUtf8String(propertiesPath);
    }

    public static int getInt(String key) {
        return taxProps.getInt(key);
    }

    public static String getString(String key) {
        return taxProps.getStr(key);
    }

    public static BigDecimal getBigDecimal(String key) {
        return taxProps.getBigDecimal(key);
    }

    public static boolean getBoolean(String key) {
        return taxProps.getBool(key);
    }


    public static void storeData(String data) {

        FileUtil.writeUtf8String(data, propertiesPath);
        log.info("保存配置成功!");
    }


}
