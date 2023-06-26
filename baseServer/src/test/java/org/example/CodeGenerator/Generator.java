package org.example.CodeGenerator;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.example.POJO.domainmodel.BaseEntity;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author edenia
 * @version 1.0
 * @date 2023/6/5 15:56
 */
public class Generator {
    @Test
    public void testGenerator() {
        //获取当前系统的目录
        String ObjectPath = System.getProperty("user.dir");

        //1.全局策略配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true)//是否支持AR模式
                .setAuthor("edenia")//作者
                .setOutputDir(ObjectPath+"/src/main/java")//生成路径
                .setFileOverride(true)//文件覆盖
                .setIdType(IdType.ASSIGN_ID)//主键策略
                .setServiceName("%sService")//设置生成service接口名字的首字母是否为I（默认会生成I开头的IStudentService）
                .setBaseResultMap(true)//自动SQL映射文件，生成基本的ResultMap
                .setBaseColumnList(true)//生成基本的SQL片段
                .setSwagger2(true); //实体属性 Swagger2 注解

        //2.数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)//设置数据库类型
                .setDriverName("com.mysql.jdbc.Driver")//数据库驱动名
                .setUrl("jdbc:mysql://localhost:3306/testblog?autoReconnect=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8")//数据库地址
                .setUsername("root")//数据库名字
                .setPassword("root");//数据库密码


        //自动填充配置
        TableFill gmtCreate=new TableFill("gmt_create", FieldFill.INSERT);
        TableFill gemModified=new TableFill("gmt_modified",FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills=new ArrayList<>();
        //3.策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(true)//全局大写命名
                .setNaming(NamingStrategy.underline_to_camel)//数据库表映射到实体的命名策略
                .setColumnNaming(NamingStrategy.underline_to_camel)//列的命名也支持驼峰命名规则
                //.setTablePrefix("tbl_")//数据库表的前缀
                .setInclude("user")//设置要映射的表名，这里可以写多个
                .setEntityLombokModel(true)  //使用Lombok开启注解
                .setLogicDeleteFieldName("deleted")//设置逻辑删除字段
                .setTableFillList(tableFills)//设置自动填充配置
                .setVersionFieldName("version")//乐观锁配置
                .setSuperEntityClass(BaseEntity.class)
                .setSuperEntityColumns("id", "created_by", "created_time", "updated_by", "updated_time")
                .setRestControllerStyle(true)//开启驼峰命名格式
                .setControllerMappingHyphenStyle(true);//controller层，开启下划线url : //localhost:8080/hello_id_2

        //4.包名策略
        PackageConfig packageConfig = new PackageConfig();
        packageConfig
                .setModuleName("example")//设置模块名
                .setParent("org")//所放置的包(父包)
                .setMapper("mapper")//Mapper包
                .setService("Service")//服务层包
                .setController("Controller")//控制层
                .setEntity("POJO.domainmodel")//实体类
                .setXml("mapper");//映射文件
        //5.整合配置
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategy)
                .setPackageInfo(packageConfig);
        //6.执行
        autoGenerator.execute();
    }

}
