package ru.job4j.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.job4j.model.CarPhoto;

import java.lang.reflect.Type;

public class CarPhotoSerializer implements JsonSerializer<CarPhoto> {
    @Override
    public JsonElement serialize(CarPhoto carPhoto, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", carPhoto.getId());
        return result;
    }
}
