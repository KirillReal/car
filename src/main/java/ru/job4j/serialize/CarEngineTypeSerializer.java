package ru.job4j.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.job4j.model.CarEngineType;

import java.lang.reflect.Type;

public class CarEngineTypeSerializer implements JsonSerializer<CarEngineType> {
    @Override
    public JsonElement serialize(CarEngineType carEngineType, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", carEngineType.getId());
        result.addProperty("name", carEngineType.getName());
        return result;
    }
}
