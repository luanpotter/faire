package xyz.luan.faire.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class GsonUtils {

	public final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'.000Z'").withZone(ZoneOffset.UTC);
	public final JsonDeserializer<Instant> ADAPTER = (json, type, jsonDeserializationContext) -> parseTimestamp(json.getAsJsonPrimitive().getAsString());

	public Instant parseTimestamp(String str) {
		return FORMATTER.parse(str, Instant::from);
	}

	public Gson build() {
		return new GsonBuilder().registerTypeAdapter(Instant.class, ADAPTER).create();

	}
}
