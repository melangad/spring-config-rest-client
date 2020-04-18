package io.github.melangad.spring.config.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestConfigProvider {

	private RestTemplate restTemplate;
	private Map<String, Object> configMap = new HashMap<String, Object>();
	private String configURI;

	@Getter
	private Date lastUpdateTime = null;

	@Getter
	private int currentVersion = 0;

	public RestConfigProvider(RestTemplate restTemplate, String configURI) {
		this.restTemplate = restTemplate;
		this.configURI = configURI;
		this.loadConfigCache();
	}

	public String getValue(final String propertyKey) {
		return (String) this.configMap.get(propertyKey);
	}

	public List<String> getPropertyNames() {
		if (this.configMap.isEmpty()) {
			this.loadConfigCache();
		}

		return new ArrayList<String>(this.configMap.keySet());
	}

	private void loadConfigCache() {
		log.debug("Loading Config From Remote Server");
		try {
			ConfigDetailDAO response = this.restTemplate.getForObject(new URI(this.configURI), ConfigDetailDAO.class);
			if (null != response && null != response.getConfigData()) {
				this.currentVersion = response.getVersion();

				Map<String, ConfigMetaDAO> configMap = response.getConfigData();
				configMap.forEach((k, v) -> {
					this.configMap.put(k, v.getValue());
				});

				this.lastUpdateTime = new Date();
			}
		} catch (RestClientException e) {
			log.error(e.getMessage(), e);
		} catch (URISyntaxException e) {
			log.error("Invalid Config Host URI", e);
		}
	}

}
