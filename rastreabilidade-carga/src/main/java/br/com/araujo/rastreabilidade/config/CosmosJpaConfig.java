package br.com.araujo.rastreabilidade.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
		basePackages = "br.com.araujo.rastreabilidade.repository.cosmos",
		entityManagerFactoryRef = "cosmosEntityManager", 
		transactionManagerRef = "cosmosTransactionManager"
)
public class CosmosJpaConfig {

	@Bean
	public LocalContainerEntityManagerFactoryBean cosmosEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(cosmosDataSource());
		em.setPackagesToScan(new String[] { "br.com.araujo.rastreabilidade.model.cosmos" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());

		return em;
	}

	@Bean
	public DataSource cosmosDataSource() {
		JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
		DataSource dataSource = dataSourceLookup.getDataSource("java:/comp/env/jdbc/cosmos");
		return dataSource;

	}		


	@Bean	
	public PlatformTransactionManager cosmosTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(cosmosEntityManager().getObject());
		return transactionManager;
	}

	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "validate");
		properties.setProperty("hibernate.show_sql", "false");
		properties.setProperty("hibernate.format_sql", "true");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");

		return properties;
	}
}
