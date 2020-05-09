package io.github.melangad.spring.config.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public abstract class AbstractPushNotificatioReciever {

	private WebClient webClient;
	private Environment environment;

	public AbstractPushNotificatioReciever(final Environment environment) {
		this.environment = environment;
		this.webClient = WebClient.create();
		this.consumeServerSentEvent();
	}

	private void consumeServerSentEvent() {

		String label = this.environment.getProperty(Constants.CONFIG_LABEL);
		String notificationurl = this.environment.getProperty(Constants.CONFIG_SERVER_NOTIFICATION_URL_PROPERTY);

		ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<ServerSentEvent<String>>() {
		};

		Flux<ServerSentEvent<String>> eventStream = webClient.get().uri(notificationurl + "/" + label).retrieve()
				.bodyToFlux(type);

		eventStream.subscribe(event -> this.processPushEvent(event), error -> log.error(error.getMessage()),

				() -> log.info("Client Connection Completed"));
	}

	public abstract void processPushEvent(ServerSentEvent<String> event);

}
