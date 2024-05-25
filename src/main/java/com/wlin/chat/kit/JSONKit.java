package com.wlin.chat.kit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public abstract class JSONKit {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJsonString(Object object){
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("convert object to json string error", ex);
        }
    }


    public static <T>T jsonToBean(String text,Class<T> classType){
        try {
            return OBJECT_MAPPER.readValue(text, classType);
        }catch (IOException ex) {
            throw new RuntimeException("convert json string to object error", ex);
        }
    }
}
