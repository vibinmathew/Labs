package com.telstra.datacat.adapters.databases.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class FakeDatasourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource fakeDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public NamedParameterJdbcTemplate fakeJdbcTemplate(@Qualifier("fakeDatasource") DataSource fakeDatasource) {
        return new NamedParameterJdbcTemplate( fakeDatasource);
    }
}
