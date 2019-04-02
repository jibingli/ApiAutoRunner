package com.testing.apirunner.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;


public class DataBaseFactory {

    private static Logger logger = Logger.getLogger(DataBaseFactory.class);

    /**
     * 根据db config xml 创建sql session factory
     *
     * @param resource db config xml文件
     * @return sqlSessionFactory
     */
    public static SqlSessionFactory createSessionFactory(String resource) {
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            logger.error(e.getMessage() + "db配置文件不存在： " + resource);
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }
}
