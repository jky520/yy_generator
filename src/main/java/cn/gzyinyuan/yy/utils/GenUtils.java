package cn.gzyinyuan.yy.utils;

import cn.gzyinyuan.yy.entity.ColumnEntity;
import cn.gzyinyuan.yy.entity.TableEntity;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器工具类
 * Created by DT人 on 2017/9/8 11:06.
 */
public class GenUtils {

    public static List<String> getTemplates(){
        List<String> templates = new ArrayList<String>();
        templates.add("template/Entity.java.vm");
        templates.add("template/Dao.java.vm");
        templates.add("template/Dao.xml.vm");
        templates.add("template/Service.java.vm");
        templates.add("template/ServiceImpl.java.vm");
        templates.add("template/Controller.java.vm");
        templates.add("template/list.html.vm");
        templates.add("template/list.js.vm");
        templates.add("template/menu.sql.vm");
        return templates;
    }

    /**
     * 生成压缩包的代码生成方法
     */
    /*public static void generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns, ZipOutputStream zip){*/
    /**
     * 生成代码
     */
    public static void generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns, String m){
        //配置信息
        Configuration config = getConfig();

        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = table2Java(tableEntity.getTableName(), config.getString("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for(Map<String, String> column : columns){
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));

            //列名转换成Java属性名
            String attrName = column2Java(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);

            //是否主键
            if("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null){
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if(tableEntity.getPk() == null){
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("package", config.getString("package"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("date", DateUtils.format(new Date(), DateUtils.DATE_PATTERN));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));

        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for(String template : templates){
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);


            String baseDir = config.getString("baseDirect");
            File f = new File(baseDir);
            if(!f.exists()) {
                m = "文件夹不存在：" + baseDir;
                throw new RuntimeException("文件夹不存在：" + baseDir);
            }

            // 生成文件的全路径
            String direct = baseDir + getFileName(template, tableEntity.getClassName(), config.getString("package"));;
            try {
                // 将字符串写入指定文件并生成
                FileUtils.writeStringToFile(new File(direct), sw.toString(),"utf-8");
                m = "生成代码成功！";
            } catch (IOException e) {
                m =  "渲染模板失败，表名：" + tableEntity.getTableName()+e;
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
            // 下面是生成压缩包的实现方式
            /*try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getClassName(), config.getString("package"))));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }*/
        }
    }


    /**
     * 列名转换成Java属性名
     */
    public static String column2Java(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String table2Java(String tableName, String tablePrefix) {
        if(StringUtils.isNotBlank(tablePrefix)){
            int index = tableName.indexOf("_");
            if(index != -1) {
                String tp = tableName.substring(0,index+1);
                tableName = tablePrefix.equals(tp) ? tableName.replace(tablePrefix, "") : tableName.replace(tp, "");
            }
        }
        return column2Java(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig(){
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName){
        String packagePath = "main" + File.separator + "java" + File.separator;
        if(StringUtils.isNotBlank(packageName)){
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if(template.contains("Entity.java.vm")){
            return packagePath + "entity" + File.separator + className + "Entity.java";
        }

        if(template.contains("Dao.java.vm")){
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

        if(template.contains("Service.java.vm")){
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if(template.contains("ServiceImpl.java.vm")){
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if(template.contains("Controller.java.vm")){
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if(template.contains("Dao.xml.vm")){
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + "generator" + File.separator + className + "Dao.xml";
        }

        if(template.contains("list.html.vm")){
            return "main" + File.separator + "resources" + File.separator + "views" + File.separator
                    + "modules" + File.separator + "generator" + File.separator + className.toLowerCase() + ".html";
        }

        if(template.contains("list.js.vm")){
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js" + File.separator
                    + "modules" + File.separator + "generator" + File.separator + className.toLowerCase() + ".js";
        }

        if(template.contains("menu.sql.vm")){
            return "sql" + File.separator + className.toLowerCase() + "_menu.sql";
        }

        return null;
    }
}
