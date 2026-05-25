package com.esg.compliance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Properties;


//APLICAÇÃO ESTAVA DANDO MUITO ERRO, ENTÃO PEDI AJUDA AO GEMINI PARA SOLUÇÃO DE PROBLEMA, ME PROPONDO, ADICIONAR ESSAS props. PARA FACILITAR LEITURA DE CÓDIGO E ACESSO AO DATASOURCE
@SpringBootApplication
public class EsgComplianceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EsgComplianceApplication.class);

        Properties props = new Properties();
        props.put("spring.datasource.url", "jdbc:oracle:thin:@oracle.fiap.com.br:1521/ORCL");
        props.put("spring.datasource.username", "rm566105");
        props.put("spring.datasource.password", "fiap25");
        props.put("spring.datasource.driver-class-name", "oracle.jdbc.OracleDriver");
        props.put("spring.jpa.database-platform", "org.hibernate.dialect.OracleDialect");
        props.put("spring.jpa.hibernate.ddl-auto", "none");
        props.put("spring.jpa.show-sql", "true");
        props.put("spring.flyway.enabled", "true");
        props.put("spring.flyway.locations", "classpath:db/migration");
        props.put("spring.flyway.baseline-on-migrate", "true");
        props.put("spring.datasource.hikari.maximum-pool-size", "2");
        props.put("spring.datasource.hikari.minimum-idle", "1");
        props.put("spring.flyway.enabled", "false");
        props.put("spring.jpa.hibernate.ddl-auto", "create");

        app.setDefaultProperties(props);
        app.run(args);
    }
}