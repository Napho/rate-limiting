package com.irembo.interview.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.irembo.*")
@EntityScan(basePackages = "com.irembo.*")
public class DatabaseConfiguration {

}
