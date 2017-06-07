package com.telstra.datacat.adapters.databases.repositories;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.jdbc.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.*;

import javax.sql.*;

@Configuration
public class OndbDatasourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.ondbdatasource")
    public DataSource ondbDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public NamedParameterJdbcTemplate ondbJdbcTemplate(@Qualifier("ondbDatasource") DataSource ondbDatasource) {
        return new NamedParameterJdbcTemplate(ondbDatasource);
    }
}
