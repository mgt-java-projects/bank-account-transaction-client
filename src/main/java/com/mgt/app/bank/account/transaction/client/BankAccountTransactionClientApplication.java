package com.mgt.app.bank.account.transaction.client;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.mgt.app.bank.account.transaction.event.persist.config.AccountTransactionEventPersistConfig;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Boot strap bank account transaction client application.
 * Configuration for swagger interface for dev and qa testing.
 * @author stami
 *
 */
@SpringBootApplication
@EnableSwagger2
@ComponentScan("com.mgt.app.bank.account.transaction.client")
@Import(AccountTransactionEventPersistConfig.class)
public class BankAccountTransactionClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountTransactionClientApplication.class, args);
	}

	/**
	 * Swagger config.
	 * @return
	 */
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.mgt.app.bank.account.transaction.client")).build()
				.genericModelSubstitutes(Optional.class);
	}


}
