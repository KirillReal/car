package ru.job4j.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.job4j.model.Car;

import java.lang.reflect.Type;

public class CarSerializer implements JsonSerializer<Car> {
    @Override
    public JsonElement serialize(Car car, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", car.getId());
        result.addProperty("isNew", car.isNew());
        result.addProperty("mileage", car.getMileage());
        result.addProperty("isBroken", car.isBroken());
        result.addProperty("description", car.getDescription());
        result.add("carModel", jsonSerializationContext.serialize(car.getCarModel()));
        result.add("carBodyType", jsonSerializationContext.serialize(car.getCarBodyType()));
        result.add("carEngineType", jsonSerializationContext.serialize(car.getCarEngineType()));
        result.add("carTransmissionBoxType", jsonSerializationContext.serialize(car.getCarTransmissionBoxType()));
        result.add("carPhotos", jsonSerializationContext.serialize(car.getCarPhotos()));
        return result;
    }
}
