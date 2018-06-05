package com.zholdak.locationshistory.converter;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Aleksey Zholdak (aleksey@zholdak.com) 2017-09-05 12:27
 * @version 1.0.1
 */
public class InstantConverter {

  public static class ToMillisJsonSerializer extends JsonSerializer<Instant> {

    @Override
    public Class<Instant> handledType() {
      return Instant.class;
    }

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      gen.writeNumber(value.toEpochMilli());
    }
  }

  public static class FromMillisJsonDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Class<?> handledType() {
      return Instant.class;
    }

    @Override
    public Instant deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      try {
        long time = Long.parseLong(jp.getText());
        return Instant.ofEpochMilli(time);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Not a time in milliseconds");
      }
    }
  }
}
