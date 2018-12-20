package com.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import com.admin.exception.ThrowErrorHandler;

@MapperScan("com.otcbi.admin.dao")
@ServletComponentScan
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class TRCApplication extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer{

	public static void main(String[] args) {
		SpringApplication.run(TRCApplication.class, args);
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
		container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
	}

	@Bean
	public RestTemplate restTemplate(){
		RestTemplate restTemplate=new RestTemplate();
		//Response status code 4XX or 5XX to the client.

		System.out.println("!!!");
		restTemplate.setErrorHandler(new ThrowErrorHandler());
		return restTemplate;
	}
	
}
