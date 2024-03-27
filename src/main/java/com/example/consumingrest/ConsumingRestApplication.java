package com.example.consumingrest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@SpringBootApplication
public class ConsumingRestApplication {
	private static final Logger log = LoggerFactory.getLogger(ConsumingRestApplication.class);

	@JsonIgnoreProperties(ignoreUnknown = true)
	record FakeDate(Long userId, Long id, String title, boolean completed){}


	public static void main(String[] args) {
		SpringApplication.run(ConsumingRestApplication.class, args);
	}


	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		var num =  new Random().nextInt(200) - 1;
		var url = "https://jsonplaceholder.typicode.com/todos/" + num;
		return args -> {
			FakeDate fakeData = restTemplate.getForObject(url, FakeDate.class);
			if(fakeData == null) return;
			log.info(fakeData.toString());
		};
	}
}
