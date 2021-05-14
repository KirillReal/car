package ru.job4j.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.*;
import ru.job4j.serialize.*;
import ru.job4j.store.Store;
import ru.job4j.store.UserStore;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Function;

public class AdsService {
    private static final Logger LOG = LoggerFactory.getLogger(AdsService.class.getName());
    private final Store store = UserStore.instOf();
    private final Map<String, Function<HttpServletRequest, Optional<String>>> dispatch =
            new HashMap<>();
    private final Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy HH:mm:ss")
            .registerTypeAdapter(City.class, new CitySerializer())
            .registerTypeAdapter(CarModel.class, new CarModelSerializer())
            .registerTypeAdapter(CarBodyType.class, new CarBodyTypeSerializer())
            .registerTypeAdapter(CarEngineType.class, new CarEngineTypeSerializer())
            .registerTypeAdapter(CarPhoto.class, new CarPhotoSerializer())
            .registerTypeAdapter(
                    CarTransmissionBoxType.class, new CarTransmissionBoxTypeSerializer()
            )
            .registerTypeAdapter(Car.class, new CarSerializer())
            .registerTypeAdapter(User.class, new UserSerializer())
            .registerTypeAdapter(AdsType.class, new AdsTypeSerializer())
            .create();

    private AdsService() {
        this.load("save", save());
        this.load("update", update());
        this.load("get-form-fields", getFormFields());
        this.load("get-user-ads", getUserAds());
        this.load("get-all-ads", getAllAds());
    }

    public static AdsService getInstance() {
        return AdsService.Holder.INSTANCE;
    }

    private static final class Holder {
        private static final AdsService INSTANCE = new AdsService();
    }

    public void load(String action, Function<HttpServletRequest, Optional<String>> handle) {
        this.dispatch.put(action, handle);
    }

    private Function<HttpServletRequest, Optional<String>> save() {
        return request -> {
            Optional<String> ads = Optional.empty();
            try {
                Ads adsFromForm = gson.fromJson(
                        request.getReader(), Ads.class
                );
                HttpSession session = request.getSession();
                User currentUser = (User) session.getAttribute("user");
                if (adsFromForm.getUser().getId() != currentUser.getId()) {
                    throw new IllegalStateException("Ошибка в переданных данных, пользователь");
                }
                store.save(adsFromForm);
                ads = Optional.of(gson.toJson(adsFromForm));
            } catch (Exception e) {
                LOG.error("Ошибка сохранения объявления", e);
            }
            return ads;
        };
    }

    private Function<HttpServletRequest, Optional<String>> update() {
        return request -> {
            Optional<String> ads = Optional.empty();
            try {
                JsonObject jsonFromForm = gson.fromJson(request.getReader(), JsonObject.class);
                Ads adsFromDb = store.findAdsById(
                        jsonFromForm.get("adsId").getAsInt()
                );
                HttpSession session = request.getSession();
                User currentUser = (User) session.getAttribute("user");
                if (adsFromDb.getUser().getId() != currentUser.getId()) {
                    throw new IllegalStateException("Попытка изменения чужого объявления");
                }
                if (jsonFromForm.has("isSold")) {
                    adsFromDb.setSold(jsonFromForm.get("isSold").getAsBoolean());
                }
                store.update(adsFromDb);
                ads = Optional.of(gson.toJson(adsFromDb));
            } catch (Exception e) {
                LOG.error("Ошибка обновления объявления", e);
            }
            return ads;
        };
    }

    private Function<HttpServletRequest, Optional<String>> getFormFields() {
        return request -> {
            Optional<String> rsl = Optional.empty();
            try {
                Map<String, Object> response = new LinkedHashMap<>();
                Map<String, Object> fieldsData = new LinkedHashMap<>();

                List<City> cities = store.findAllCites();
                fieldsData.put("cities", cities);

                List<CarModel> carModels = store.findAllCarModel();
                fieldsData.put("carModels", carModels);

                List<CarBodyType> carBodyTypes = store.findAllCarBodyType();
                fieldsData.put("carBodyTypes", carBodyTypes);

                List<CarEngineType> carEngineTypes = store.findAllCarEngineType();
                fieldsData.put("carEngineTypes", carEngineTypes);

                List<CarTransmissionBoxType> carTransmissionBoxTypes =
                        store.findAllCarTransmissionBoxType();
                fieldsData.put("carTransmissionBoxTypes", carTransmissionBoxTypes);

                List<AdsType> adsTypes = store.findAllAdsType();
                fieldsData.put("adsType", adsTypes.get(0));

                response.put("fields", fieldsData);

                HttpSession session = request.getSession();
                User currentUser = (User) session.getAttribute("user");
                response.put("user", currentUser);

                rsl = Optional.of(gson.toJson(response));
            } catch (Exception e) {
                LOG.error("Ошибка получения данных объявления", e);
            }
            return rsl;
        };
    }

    private Function<HttpServletRequest, Optional<String>> getUserAds() {
        return request -> {
            Optional<String> rsl = Optional.empty();
            try {
                String userId = request.getParameter("id");
                List<Ads> ads =
                        store.findAdsByUserId(Integer.parseInt(userId));
                rsl = Optional.of(gson.toJson(ads));
            } catch (Exception e) {
                LOG.error("Ошибка получения данных объявления", e);
            }
            return rsl;
        };
    }

    private Function<HttpServletRequest, Optional<String>> getAllAds() {
        return request -> {
            Optional<String> rsl = Optional.empty();
            try {
                List<Ads> ads = store.findAllAds();
                rsl = Optional.of(gson.toJson(ads));
            } catch (Exception e) {
                LOG.error("Ошибка получения данных объявления", e);
            }
            return rsl;
        };
    }

    public Optional<String> execute(HttpServletRequest request) {
        String action = request.getParameter("action");
        if (!dispatch.containsKey(action)) {
            throw new IllegalArgumentException("Действие не найдено");
        }
        return dispatch.get(action).apply(request);
    }
}
