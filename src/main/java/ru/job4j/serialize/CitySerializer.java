package ru.job4j.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.job4j.model.City;

import java.lang.reflect.Type;

public class CitySerializer implements JsonSerializer<City> {

    @Override
    public JsonElement serialize(City city, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", city.getId());
        result.addProperty("name", city.getName());
        return result;
    }
}
