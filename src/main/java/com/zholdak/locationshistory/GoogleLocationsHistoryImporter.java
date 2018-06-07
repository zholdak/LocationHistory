package com.zholdak.locationshistory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zholdak.locationshistory.model.LocationDto;
import com.zholdak.locationshistory.model.LocationsDto;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * @author Aleksey Zholdak (aleksey@zholdak.com) 2018-06-05 13:52
 */
@Component
public class GoogleLocationsHistoryImporter {

	private Logger logger = LoggerFactory.getLogger(LocationsHistoryApplication.class);

	private ObjectMapper mapper = new ObjectMapper();

	/** Minimum distance between points on tracks in meters */
	private static final int MIN_TRACK_DISTANCE = 100;

	/** Minimum distance between points on area in meters */
	private static final int MIN_AREA_DISTANCE = 100;

	public void run(List<String> inputLocationsHistory ) throws IOException {

		requireNonNull(inputLocationsHistory, "inputLocationsHistory must be not null");

		List<LocationDto> allRawLocations = new ArrayList<>();

		logger.info("Start importing processing locations history ...");
		for(String input: inputLocationsHistory) {
			File inputFile = new File(input);
			if (!inputFile.exists() || !inputFile.isFile() || !inputFile.canRead()) {
				throw new IOException(
						format("Input file '%s' can't be processed: not exists / not a file / not readable", input));
			}
			logger.info("Processing input file '{}' ...", inputFile.getName());

			LocationsDto locs = mapper.readValue(inputFile, LocationsDto.class);

			logger.info("  {} location(s) parsed", locs.getLocations().size());

			allRawLocations.addAll(locs.getLocations());
		}

		logger.info("{} total location(s) parsed", allRawLocations.size());

		if (allRawLocations.size() == 0) {
			throw new IllegalArgumentException("Empty locations, nothing to process.");
		}

		logger.info("Sorting locations by timestamp ...");
		allRawLocations.sort(new LocationDto.ComparatorByTimestamp());

		logger.info("Filtering locations and building waypoints list ...");
		List<WayPoint> allWayPoints = new ArrayList<>();
		Iterator<LocationDto> locationsIterator = allRawLocations.iterator();
		WayPoint prevWP = toWayPoint(locationsIterator.next());
		allWayPoints.add(prevWP);
		while (locationsIterator.hasNext()) {
			WayPoint curWP = toWayPoint(locationsIterator.next());
			if (Geoid.WGS84.distance(prevWP, curWP).intValue() >= MIN_TRACK_DISTANCE) {
				allWayPoints.add(curWP);
				prevWP = curWP;
			}
		}
		logger.info("{} total waypoint(s) collected by filtering locations with min distance of {}m", allWayPoints.size(),
				MIN_TRACK_DISTANCE);
		allRawLocations = null;

		if (allWayPoints.size() == 0) {
			throw new IllegalStateException("No waypoints: nothing to process.");
		}

		logger.info("Filtering for radius ...");
		WayPoint kiev = WayPoint.of(50.4501, 30.5234);
		List<WayPoint> kievWayPoints = allWayPoints.stream().filter(wp -> wp.distance(kiev).intValue() <= 30000).collect(Collectors.toList());
		logger.info("{} total waypoint(s) collected by filtering locations in Kiev", kievWayPoints.size());

		logger.info("Filtering for area ...");
		List<WayPoint> areaWayPoints = new ArrayList<>();
		Iterator<WayPoint> allWayPointsIterator = kievWayPoints.iterator();
		WayPoint curWP = allWayPointsIterator.next();
		areaWayPoints.add(curWP);
		int allWayPointsProcessed = 1;
		nextNewWayPoint: while (allWayPointsIterator.hasNext()) {
			WayPoint newWP = allWayPointsIterator.next();
			allWayPointsProcessed ++;
			for (WayPoint wp : areaWayPoints) {
				if (newWP.distance(wp).intValue() <= MIN_AREA_DISTANCE) {
					continue nextNewWayPoint;
				}
			}
			areaWayPoints.add(newWP);
			logger.info("areaWayPoints={} {}%", areaWayPoints.size(), (int)(((float)allWayPointsProcessed/(float)allWayPoints.size())*100));
		}
		logger.info("{} total waypoint(s) collected by area filtering with min diatance of {}m", areaWayPoints.size(),
				MIN_AREA_DISTANCE);

		GPX.Builder gpxBuilder = GPX.builder();

		logger.info("Building gpx ...");
		areaWayPoints.forEach(gpxBuilder::addWayPoint);

//		TrackSegment.Builder segmentBuilder = TrackSegment.builder();
//		wayPoints.forEach(segmentBuilder::addPoint);
//
//		Track.Builder trackBuilder = Track.builder();
//		trackBuilder.addSegment(segmentBuilder.build());
//
//		gpxBuilder.addTrack(trackBuilder.build());

		logger.info("Writing gpx ...");
		GPX.write(gpxBuilder.version(GPX.Version.V11).build(), new File("/home/lion/test11.gpx").toPath());

	}

	private WayPoint toWayPoint(LocationDto loc) {
		WayPoint.Builder wpb = WayPoint.builder()
				.lat(loc.getLatitude().doubleValue())
				.lon(loc.getLongitude().doubleValue())
				.time(loc.getTimestamp());
		if (loc.getAltitude() != null) {
			wpb.ele(loc.getAltitude());
		}
		if (loc.getHeading() != null) {
			wpb.course(loc.getHeading());
		}
		if (loc.getVelocity() != null) {
			wpb.speed(loc.getVelocity());
		}
		return wpb.build();
	}

}
