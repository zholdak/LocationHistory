package com.zholdak.locationshistory.converter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Aleksey Zholdak (aleksey@zholdak.com) 2018-06-05 16:11
 */
public class GeocodeE7Converter {

	public static class ToE7JsonSerializer extends JsonSerializer<BigDecimal> {

		@Override
		public Class<BigDecimal> handledType() {
			return BigDecimal.class;
		}

		@Override
		public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
			gen.writeNumber(value.movePointRight(7).toString());
		}
	}

	public static class FromE7JsonDeserializer extends JsonDeserializer<BigDecimal> {

		@Override
		public Class<BigDecimal> handledType() {
			return BigDecimal.class;
		}

		@Override
		public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			try {
				return new BigDecimal(Long.parseLong(jp.getText())).movePointLeft(7);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Not a E7 geocode");
			}
		}
	}
}
