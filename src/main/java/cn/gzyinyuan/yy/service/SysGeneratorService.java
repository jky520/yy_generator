package cn.gzyinyuan.yy.service;

import cn.gzyinyuan.yy.dao.SysGeneratorDao;
import cn.gzyinyuan.yy.utils.GenUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 * Created by DT人 on 2017/9/8 9:46.
 */
@Service
public class SysGeneratorService {

    @Autowired
    private SysGeneratorDao sysGeneratorDao;

    public List<Map<String, Object>> queryList(Map<String, Object> map) {
        return sysGeneratorDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return sysGeneratorDao.queryTotal(map);
    }

    public Map<String, String> queryTable(String tableName) {
        return sysGeneratorDao.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return sysGeneratorDao.queryColumns(tableName);
    }

    /**
     * 生成压缩包的代码生成方法
     * @param tableNames
     * @return
     */
    public byte[] generatorCode1(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, String> table = queryTable(tableName);
            // 查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            // 生成代码
            //GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 生代码生成方法
     * @param tableNames
     * @return
     */
    public String generatorCode(String[] tableNames) {
        String str = null;

        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, String> table = queryTable(tableName);
            // 查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            // 生成代码
            GenUtils.generatorCode(table, columns, str);
        }
        return str;
    }
}
