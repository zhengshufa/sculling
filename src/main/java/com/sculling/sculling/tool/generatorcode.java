package com.sculling.sculling.tool;


import java.io.File;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * @author hpdata
 * @DATE 2024/3/614:17
 */
public class generatorcode {
    private static final String whsleSaasMasterUrl = "jdbc:mysql://192.168.11.246:3306/whsle_saas_master?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";

    private static final String whsleSaasMasterName = "root";

    private static final String whsleSaasMasterPassword = "123456";

    private static final String merchantUrl = "jdbc:mysql://192.168.11.248:3306/hopdata_merchants?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";

    private static final String marketUrl = "jdbc:mysql://192.168.11.248:3306/hopdata_market?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";

    private static final String merchant206Url = "jdbc:mysql://192.168.31.206:3306/hopdata_merchants?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";

    private static final String coreUrl = "jdbc:mysql://192.168.11.248:3306/whsle_saas_master?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";

    private static final String couponUrl = "jdbc:mysql://192.168.11.248:3306/hopdata_coupon?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";

    private static final String entryUrl = "jdbc:mysql://192.168.11.248:3306/hopdata_entry?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";

    private static final String reportUrl = "jdbc:mysql://192.168.11.248:3306/hopdata_report?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";

    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder(reportUrl,
            whsleSaasMasterName, whsleSaasMasterPassword);


    public static void main(String[] args) {

        File dir = new File("");

        FastAutoGenerator.create(DATA_SOURCE_CONFIG)

                // 全局配置
                .globalConfig(builder -> {
                    builder.author("baomidou") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(dir.getAbsolutePath()); // 指定输出目录
                })

                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.hopdata.entry") // 设置父包名
                            .controller("controller")
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .xml("mapper"); // 设置mapperXml生成路径
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude("hp_market_capital_month_report").build();// 设置需要生成的表名,生成所有表注释此行

                    // controller配置
                    builder.controllerBuilder()
                            .enableHyphenStyle()
                            .enableRestStyle();

                    // entity配置
                    builder.entityBuilder()
                            .idType(IdType.AUTO)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .enableLombok()
                            .logicDeleteColumnName("is_deleted")
//                            .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                            .build();

                    // service配置
                    builder.serviceBuilder()
                            .superServiceClass(IService.class)
                            .superServiceImplClass(ServiceImpl.class)
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImp")
                            .build();

                    // mapper配置
                    builder.mapperBuilder()
                            .superClass(BaseMapper.class)
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper")
                            .build();

                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
