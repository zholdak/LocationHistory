package com.zholdak.locationshistory;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.zholdak.locationshistory.location.Distance;

import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;

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

//		System.out.println("####d " + Distance.distance(50.432565, 30.460247, 50.432359, 30.511133));
//		System.out.println("####w " + Geoid.WGS84.distance(WayPoint.of(50.432565, 30.460247), WayPoint.of(50.432359, 30.511133)));
//		System.out.println("####d " + Distance.distance(50.468514, 27.206538, 50.272173, 30.017172));
//		System.out.println("####w " + Geoid.WGS84.distance(WayPoint.of(50.468514, 27.206538), WayPoint.of(50.272173, 30.017172)));

		applicationContext.getBean(GoogleLocationsHistoryImporter.class).run(inputLocationsHistory);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
