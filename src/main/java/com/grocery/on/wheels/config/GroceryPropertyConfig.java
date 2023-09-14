package com.grocery.on.wheels.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConfigurationProperties(prefix="*")
public class GroceryPropertyConfig implements EnvironmentAware {
	private Environment environment;
	/*
	 * @Autowired private Environment env;
	 * 
	 * public String getProperty(String propKey) { return env.getProperty(propKey);
	 * }
	 */
	

	@Value("${s3.domain}")
	public String s3Domain;

	public String getS3Domain() {
		return s3Domain;
	}

	
	public void setS3Domain(String s3Domain) {
		this.s3Domain = s3Domain;
	}


	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	

}
