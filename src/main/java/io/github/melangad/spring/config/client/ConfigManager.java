package io.github.melangad.spring.config.client;

import java.util.Date;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;

public class ConfigManager {

	private RestClientPropertySource propertySource;

	/**
	 * Default constructor
	 * 
	 * @param environment Spring Environment
	 */
	public ConfigManager(Environment environment) {
		AbstractEnvironment env = (AbstractEnvironment) environment;
		if (env.getPropertySources().contains(Constants.RESTTEMPLATE_PROPERTY_SOURCE)) {
			this.propertySource = (RestClientPropertySource) env.getPropertySources()
					.get(Constants.RESTTEMPLATE_PROPERTY_SOURCE);
		}
	}

	/**
	 * Get current configuration version on the client side
	 * 
	 * @return Current configuration version
	 */
	public int getCurrentConfigVersion() {
		if (null != this.propertySource) {
			return this.propertySource.getCurrentConfigVersion();
		} else {
			return 0;
		}
	}

	/**
	 * Get last updated time of the configuration on client side
	 * 
	 * @return Last update time of the configuration
	 */
	public Date getLastConfigUpdateTime() {
		if (null != this.propertySource) {
			return this.propertySource.getLastConfigUpdateTime();
		} else {
			return null;
		}
	}

	/**
	 * Provide current client configuration state to the server
	 * 
	 * @param clientId Unique client ID
	 */
	public void provideFeedback(final String clientId) {
		if (null != this.propertySource) {
			this.propertySource.provideClientFeedback(clientId);
		}
	}
	

}
