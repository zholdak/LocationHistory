package com.zholdak.locationshistory.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zholdak.locationshistory.converter.GeocodeE7Converter;
import com.zholdak.locationshistory.converter.InstantConverter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Aleksey Zholdak (aleksey@zholdak.com) 2018-06-05 16:03
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = NON_NULL)
@JsonAutoDetect(fieldVisibility = NONE, getterVisibility = NONE, setterVisibility = NONE, isGetterVisibility = NONE)
public class LocationDto {

	@JsonProperty("timestampMs")
	@JsonSerialize(using = InstantConverter.ToMillisJsonSerializer.class)
	@JsonDeserialize(using = InstantConverter.FromMillisJsonDeserializer.class)
	private Instant timestamp;

	@JsonProperty("latitudeE7")
	@JsonSerialize(using = GeocodeE7Converter.ToE7JsonSerializer.class)
	@JsonDeserialize(using = GeocodeE7Converter.FromE7JsonDeserializer.class)
	private BigDecimal latitude;

	@JsonProperty("longitudeE7")
	@JsonSerialize(using = GeocodeE7Converter.ToE7JsonSerializer.class)
	@JsonDeserialize(using = GeocodeE7Converter.FromE7JsonDeserializer.class)
	private BigDecimal longitude;

	@JsonProperty("accuracy")
	private Integer accuracy;

	@JsonProperty("velocity")
	private Integer velocity;

	@JsonProperty("heading")
	private Integer heading;

	@JsonProperty("altitude")
	private Integer altitude;

	@JsonProperty("verticalAccuracy")
	private Integer verticalAccuracy;

	public static class ComparatorByTimestamp implements Comparator<LocationDto> {
		@Override
		public int compare(LocationDto location1, LocationDto location2) {
			return location1.getTimestamp().compareTo(location2.getTimestamp());
		}
	}
}
