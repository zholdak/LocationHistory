package com.zholdak.locationshistory;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SpringBootApplication
public class LocationsHistoryApplication implements ApplicationRunner, ApplicationContextAware {

	private static final String INPUT_LOCATIONS_HISTORY = "input-locations-history";

	private ApplicationContext applicationContext;

	private List<String> inputLocationsHistory = null;

	public static void main(String[] args) {
		SpringApplication.run(LocationsHistoryApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		if (args.containsOption(INPUT_LOCATIONS_HISTORY)) {
			inputLocationsHistory = args.getOptionValues(INPUT_LOCATIONS_HISTORY);
		}

		applicationContext.getBean(GoogleLocationsHistoryImporter.class).run(inputLocationsHistory);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
