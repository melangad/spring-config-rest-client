package io.github.melangad.spring.config.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestConfigProvider {

	private RestTemplate restTemplate;
	private Map<String, Object> configMap = new HashMap<String, Object>();
	private String configURI;
	private String feebackURI;
	private String label;

	@Getter
	private Date lastUpdateTime = null;

	@Getter
	private int currentVersion = 0;

	public RestConfigProvider(RestTemplate restTemplate, final String configURI, final String feebackURI,
			final String label) {
		this.restTemplate = restTemplate;
		this.configURI = configURI;
		this.feebackURI = feebackURI;
		this.label = label;
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
			final URI uri = new URI(this.configURI + "/" + label);
			ConfigDetailDAO response = this.restTemplate.getForObject(uri, ConfigDetailDAO.class);
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

	public void provideClientfeedback(final String clientId) {

		Assert.notNull(this.feebackURI,
				"Invalid Feedback URI. Please set " + Constants.CONFIG_SERVER_FEEDBACK_URL_PROPERTY);
		try {
			final URI uri = new URI(this.feebackURI);
			ClientFeedback feedback = new ClientFeedback();
			feedback.setClientId(clientId);
			feedback.setClientVersion(this.currentVersion);
			feedback.setLabel(this.label);
			feedback.setLastUpdateTime(this.lastUpdateTime);

			ResponseEntity<String> response = this.restTemplate.postForEntity(uri, feedback, String.class);

			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				log.warn("Invalid response status for client feedback with %", response.getStatusCodeValue());
			}
		} catch (RestClientException e) {
			log.error(e.getMessage(), e);
		} catch (URISyntaxException e) {
			log.error("Invalid Config Feedback URI", e);
		}
	}

}
