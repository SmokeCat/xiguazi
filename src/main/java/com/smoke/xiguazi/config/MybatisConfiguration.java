package com.smoke.xiguazi.config;

import com.smoke.xiguazi.mapper.MapperMarker;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackageClasses = {MapperMarker.class})
public class MybatisConfiguration {

    @Bean
    @Autowired
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource){
        TransactionFactory transactionFactory = new JdbcTransactionFactory();

        Environment environment = new Environment("development", transactionFactory, dataSource);

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setEnvironment(environment);
        configuration.addMappers("com.smoke.xiguazi.mapper");

        return new SqlSessionFactoryBuilder().build(configuration);
    }
}
