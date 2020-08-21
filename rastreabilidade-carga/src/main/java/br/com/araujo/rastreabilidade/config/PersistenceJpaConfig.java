package br.com.araujo.rastreabilidade.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
		basePackages = "br.com.araujo.rastreabilidade.repository.rcarga", 
		entityManagerFactoryRef = "rcargaEntityManager", 
		transactionManagerRef = "rcargaTransactionManager"
)
public class PersistenceJpaConfig {

	@Bean
	@Primary
	@Qualifier("rcargaEntityManager")
	public LocalContainerEntityManagerFactoryBean rcargaEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] { "br.com.araujo.rastreabilidade.model.rcarga" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());

		return em;
	}

	@Bean
	@Primary
	public DataSource dataSource() {
		JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
		DataSource dataSource = dataSourceLookup.getDataSource("java:/comp/env/jdbc/rastreabilidade");
		return dataSource;
	}

	@Bean
	@Primary
	public PlatformTransactionManager rcargaTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(rcargaEntityManager().getObject());
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