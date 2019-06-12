package com.tyg.util;

import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;


import org.mybatis.generator.api.MyBatisGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MybatisGeneratorimpl {

    public void generator() throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = false;
        // 指定配置文件
        String path = this.getClass().getClassLoader().getResource("mybatis/generatorConfig.xml").getPath();
        File configFile = new File(path);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }

    // 执行main方法以生成代码
    public static void main(String[] args) {
        try {
            MybatisGeneratorimpl generatorSqlmap = new MybatisGeneratorimpl();
            generatorSqlmap.generator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
