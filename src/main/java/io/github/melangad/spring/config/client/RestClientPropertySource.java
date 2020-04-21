package io.github.melangad.spring.config.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.core.env.EnumerablePropertySource;

public class RestClientPropertySource extends EnumerablePropertySource<RestConfigProvider> {

	private RestConfigProvider restConfigProvider;

	protected RestClientPropertySource(RestConfigProvider restConfigProvider) {
		super(Constants.RESTTEMPLATE_PROPERTY_SOURCE, restConfigProvider);
		this.restConfigProvider = restConfigProvider;
	}

	@Override
	public String[] getPropertyNames() {
		List<String> list = new ArrayList<String>();
		list.addAll(this.restConfigProvider.getPropertyNames());
	
		return list.stream().toArray(String[]::new);
	}

	@Override
	public Object getProperty(String name) {

		return this.restConfigProvider.getValue(name);
	}

	public int getCurrentConfigVersion() {
		return this.restConfigProvider.getCurrentVersion();
	}

	public Date getLastConfigUpdateTime() {
		return this.restConfigProvider.getLastUpdateTime();
	}
	
	public void provideClientFeedback(final String clientId) {
		this.restConfigProvider.provideClientfeedback(clientId);
	}

}
