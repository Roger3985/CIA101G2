package com.Cia101G2;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CustomTimestampDeserializer extends JsonDeserializer<Timestamp> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Timestamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        String rentalDate = p.getText().trim();

        try {
            rentalDate = rentalDate.replace("T", " ") + ":00";
            return new Timestamp(dateFormat.parse(rentalDate).getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date: " + rentalDate, e);
        }

    }

}
