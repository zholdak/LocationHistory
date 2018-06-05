package com.zholdak.locationshistory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zholdak.locationshistory.model.Locations;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * @author Aleksey Zholdak (aleksey@zholdak.com) 2018-06-05 13:52
 */
@Component
public class GoogleLocationsHistoryImporter {

	private Logger logger = LoggerFactory.getLogger(LocationsHistoryApplication.class);

	private ObjectMapper mapper = new ObjectMapper();

	public void run(List<String> inputLocationsHistory ) throws IOException {

		requireNonNull(inputLocationsHistory, "inputLocationsHistory must be not null");

		logger.info("Start importing processing locations history ...");
		for(String input: inputLocationsHistory) {
			File inputFile = new File(input);
			if (!inputFile.exists() || !inputFile.isFile() || !inputFile.canRead()) {
				throw new IOException(
						format("Input file '%s' can't be processed: not exists / not a file / not readable", input));
			}
			logger.info("Processing input file '{}' ...", inputFile.getName());

			Locations locations = mapper.readValue(inputFile, Locations.class);

			logger.info("  {} locations parsed", locations.getLocations().size());
			logger.info("Example location: {}", locations.getLocations().get(14));
		}

	}
}
