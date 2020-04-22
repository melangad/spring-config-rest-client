package io.github.melangad.spring.config.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

public class RestConfigEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

	public static final int DEFAULT_ORDER = ConfigFileApplicationListener.DEFAULT_ORDER + 1;
	private int order = DEFAULT_ORDER;

	private RestTemplate restTemplate = new RestTemplate();

	public int getOrder() {
		return order;
	}

	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

		if (!this.isConfigServerEnables(environment)) {
			return;
		}

		final String configUrl = environment.getProperty(Constants.CONFIG_SERVER_URL_PROPERTY);
		final String label = environment.getProperty(Constants.CONFIG_LABEL);
		final String feebackURI = environment.getProperty(Constants.CONFIG_SERVER_FEEDBACK_URL_PROPERTY);

		Assert.notNull(configUrl, "Invalid Config Host URI. Please set " + Constants.CONFIG_SERVER_URL_PROPERTY);
		Assert.notNull(label, "Invalid Label. Please set " + Constants.CONFIG_LABEL);

		final RestConfigProvider configProvider = new RestConfigProvider(restTemplate, configUrl, feebackURI, label);

		try {
			final MutablePropertySources sources = environment.getPropertySources();

			if (sources.contains(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)) {
				sources.addAfter(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
						new RestClientPropertySource(configProvider));
			} else {
				sources.addFirst(new RestClientPropertySource(configProvider));
			}

		} catch (final Exception ex) {
			throw new IllegalStateException("Failed to configure Rest Client property source", ex);
		}

	}

	private boolean isConfigServerEnables(ConfigurableEnvironment environment) {
		return new Boolean(environment.getProperty(Constants.IS_CONFIG_SERVER_ENABLED_PROPERTY)).booleanValue();
	}
}
