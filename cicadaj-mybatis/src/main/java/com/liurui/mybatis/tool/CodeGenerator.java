package com.liurui.mybatis.tool;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.google.common.base.Strings;
import com.liurui.mybatis.SuperEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;

/**
 * @author liu-rui
 * @date 18:11
 * @description mybatis代码生成
 * @since 0.1.0
 */
@Log4j2
public final class CodeGenerator {
    /**
     * 执行代码生成
     */
    public static void exec(String packageName, DataSourceConfig dataSourceConfig) {
        Assert.notNull(dataSourceConfig, "dataSourceConfig不能为null");

        AutoGenerator autoGenerator = new AutoGenerator();
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/target/mybatis-code-generator/");
        gc.setAuthor("robot");
        gc.setOpen(true);
        autoGenerator.setGlobalConfig(gc);

        autoGenerator.setDataSource(dataSourceConfig);

        PackageConfig pc = new PackageConfig();
        pc.setParent(Strings.nullToEmpty(packageName));
        autoGenerator.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        autoGenerator.setCfg(cfg);


        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass(SuperEntity.class.getName());
        strategy.setEntityLombokModel(true);
        strategy.setLogicDeleteFieldName("deleted");
        strategy.setRestControllerStyle(true);
        strategy.setInclude("\\w+");
        strategy.setSuperEntityColumns("create_time", "update_time");
        autoGenerator.setStrategy(strategy);
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.execute();
    }
}
