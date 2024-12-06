package by.zemich.userms.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
@AllArgsConstructor
public class DatabaseConnectionProdConfig {

    private final Environment env;

    @Bean
    DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/users-db");
        config.setUsername(env.getProperty("DB_USERNAME"));
        config.setPassword(env.getProperty("DB_PASSWORD"));
        config.setSchema("app");
        return new HikariDataSource(config);
    }

}
