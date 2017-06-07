package com.telstra.datacat.adapters.databases.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class RtiDatasourceConfiguration {

    @Primary
    @Bean
    @ConfigurationProperties("spring.rtidatasource")
    public DataSource rtiDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public NamedParameterJdbcTemplate rtiJdbcTemplate(@Qualifier("rtiDatasource") DataSource rtiDatasource) {
        return new NamedParameterJdbcTemplate(rtiDatasource);
    }
}
