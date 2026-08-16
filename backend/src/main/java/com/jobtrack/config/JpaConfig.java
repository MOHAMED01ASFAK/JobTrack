package com.jobtrack.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA Auditing for automatic createdAt and updatedAt timestamp population.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
