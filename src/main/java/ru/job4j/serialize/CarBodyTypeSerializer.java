package ru.job4j.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.job4j.model.CarBodyType;

import java.lang.reflect.Type;

public class CarBodyTypeSerializer implements JsonSerializer<CarBodyType> {

    @Override
    public JsonElement serialize(CarBodyType carBodyType, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", carBodyType.getId());
        result.addProperty("name", carBodyType.getName());
        return result;
    }
}
