package io.github.melangad.spring.config.client;

import java.util.Date;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;

public class ConfigManager {

	private RestClientPropertySource propertySource;

	public ConfigManager(Environment environment) {
		AbstractEnvironment env = (AbstractEnvironment) environment;
		if (env.getPropertySources().contains(Constants.RESTTEMPLATE_PROPERTY_SOURCE)) {
			this.propertySource = (RestClientPropertySource) env.getPropertySources()
					.get(Constants.RESTTEMPLATE_PROPERTY_SOURCE);
		}
	}

	public int getCurrentConfigVersion() {
		if (null != this.propertySource) {
			return this.propertySource.getCurrentConfigVersion();
		} else {
			return 0;
		}
	}

	public Date getLastConfigUpdateTime() {
		if (null != this.propertySource) {
			return this.propertySource.getLastConfigUpdateTime();
		} else {
			return null;
		}
	}

}
