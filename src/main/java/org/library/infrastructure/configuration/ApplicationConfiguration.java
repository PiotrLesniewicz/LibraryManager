package org.library.infrastructure.configuration;

import org.library._ApplicationMarker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = _ApplicationMarker.class)
@Import({PersistenceJpaRepositories.class})
public class ApplicationConfiguration {
}
