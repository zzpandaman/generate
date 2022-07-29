package com.baomidou.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.skkj.common.core.web.controller.BaseController;
import com.skkj.common.core.web.domain.BaseEntity;

import java.util.Collections;

public class MyGenerator {
    private static final String url = "jdbc:mysql://192.168.1.109:3306/sclead_tpm" +
        "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull" +
        "&useSSL=false&serverTimezone=GMT%2B8&allowMultiQueries=true";;
    private static final String username = "root";
    private static final String password = "123456";
    private static String outputDir = "D:\\idea_workplace\\Itip-api-new\\itip-modules\\itip-tpm\\tpm-base\\src\\main\\java";
    private static String parent = "com.skkj.tpm.base";
    private static final String tableName = "tb_equipment_check_group";

    public static void main(String[] args) {
        generator();
    }

    private static void generator() {
        /* 数据源配置 */
        DataSourceConfig.Builder dataSourceConfig = new DataSourceConfig.Builder(url, username, password)
//            .dbQuery(new MySqlQuery())  //是否使用sql查询表、字段信息
        ;

        FastAutoGenerator.create(dataSourceConfig)
            /* 全局配置 */
            .globalConfig(builder -> builder
                .disableOpenDir()   //禁止打开输出目录
                .outputDir(outputDir)   //指定输出目录
                .author("xjx")  //作者
                .enableSwagger()
                .dateType(DateType.TIME_PACK)
                .commentDate("yyyy-MM-dd")
            )
            /* 包配置 */
            .packageConfig(builder -> builder
                .parent(parent) //父包名
//                .moduleName(parent) //父包模块名
                .entity("domain")   //entity 包名
                .pathInfo(Collections.singletonMap(OutputFile.xml
                    ,outputDir.replace("java","resources\\mapper")))   //替换默认 xml 路径
            )
            /* 模板配置 */
            .templateConfig(builder -> builder
                .controller("/templates/sclead/itip-api/controller.java")
                .service("/templates/sclead/itip-api/service.java")
                .serviceImpl("/templates/sclead/itip-api/serviceImpl.java")
                .entity("/templates/sclead/itip-api/entity.java")
                .mapper("/templates/sclead/itip-api/mapper.java")
                .xml("/templates/sclead/itip-api/mapper.xml")
            )
            /* 注入配置 */
            .injectionConfig(builder -> builder
                .beforeOutputFile((tableInfo, stringObjectMap) ->{
                    stringObjectMap.put("businessName",tableInfo.getName().substring(tableInfo.getName().lastIndexOf("_")+1));  //业务名
                    stringObjectMap.put("permissionPrefix" , stringObjectMap.get("businessName"));  //接口权限前缀
                    stringObjectMap.put("EntityName",tableInfo.getEntityName().charAt(0)+tableInfo.getEntityName().substring(1));   //实例变量名
                    String commonFieldNames = tableInfo.getCommonFields().stream()
                        .reduce("" , (res, e) -> res + ", " + e.getColumnName(), String::concat);
                    stringObjectMap.put("commonFieldNames",commonFieldNames.trim());    //拼接公共字段名

                    tableInfo.getFields().stream().filter(TableField::isKeyFlag).limit(1).forEach(e->{
                        stringObjectMap.put("pkName",e.getPropertyName());  //主键名
                        stringObjectMap.put("pkColumn",e.getName());  //主键数据库列名
                        stringObjectMap.put("pkType",e.getPropertyType());  //主键类型
                        stringObjectMap.put("isKeyIdentityFlag" , e.isKeyIdentityFlag()); //主键是否自增
                    });

                }) //输出前操作，这里添加了模板上下文信息
//                .customFile(myBuilder -> {
//                    if (!StringUtils.endsWith(outputDir, File.separator)) {
//                        outputDir += File.separator;
//                    }
//                    parent = parent.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
//                    myBuilder
//                        .templatePath("/templates/sclead/itip-api/vo.java.vm" ) //模板路径
//                        .filePath(outputDir + parent +  File.separator + "vo" ) //文件路径
//                        .fileName("Vo" + StringPool.DOT_JAVA)  //源码以 entityName 开头，需和模板类名保持一致
//                        .enableFileOverride();
//                }) //自定义 Dto Vo
            )
            /* 策略配置 */
            .strategyConfig(builder -> builder
                .addInclude(tableName)  //表匹配
                .addTablePrefix("tb_")  //过滤表前缀
                /* 实体策略配置 */
                .entityBuilder()
                .superClass(BaseEntity.class)   //设置父类
                .enableFileOverride() //允许覆盖
                .enableLombok() //开启 lombok
                .enableTableFieldAnnotation()   //开启字段注释
                .naming(NamingStrategy.underline_to_camel)  //数据库表映射到实体的命名规则,字段映射规则未配置也用这个
                .addSuperEntityColumns("searchValue", "create_by", "create_time",
                    "update_by", "update_time","remark")  //父类公共字段
                /* controller 策略配置 */
                .controllerBuilder()
                .enableFileOverride()
                .superClass(BaseController.class)   //设置父类
                .enableRestStyle()  //开启 @RestController
                /* service 策略配置 */
                .serviceBuilder()
                .enableFileOverride()
                /* mapper 策略配置 */
                .mapperBuilder()
                .enableBaseResultMap()
                .enableMapperAnnotation()
                .enableFileOverride()
            )
            /* 模板引擎 */
            .templateEngine(new VelocityTemplateEngine())
            .execute();
    }
}
