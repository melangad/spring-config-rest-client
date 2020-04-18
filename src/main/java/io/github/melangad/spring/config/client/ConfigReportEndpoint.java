package io.github.melangad.spring.config.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "remote-config")
public class ConfigReportEndpoint {

	@Autowired
	private ConfigManager configManager;

	@ReadOperation
	public Map<String, Object> getconfigReport() {
		Map<String, Object> report = new HashMap<String, Object>();

		report.put("currentConfigVersion", this.configManager.getCurrentConfigVersion());

		report.put("lastUpdateTime", this.configManager.getLastConfigUpdateTime());

		return report;
	}

}
