package ru.job4j.store;

import ru.job4j.model.*;

import java.util.List;

public interface Store {
    User findUserByLogin(String login);

    void save(User user);

    void delete(User user);

    List<City> findAllCites();

    List<CarModel> findAllCarModel();

    List<CarBodyType> findAllCarBodyType();

    List<CarEngineType> findAllCarEngineType();

    List<CarTransmissionBoxType> findAllCarTransmissionBoxType();

    List<AdsType> findAllAdsType();

    Ads findAdsById(int id);

    List<Ads> findAdsByUserId(int userId);

    List<Ads> findAllAds();

    void save(Ads ads);

    void update(Ads ads);

    void saveCarPhoto(CarPhoto carPhoto, int adsId);
}
