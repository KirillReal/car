package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.*;

import java.util.List;
import java.util.function.Function;

public class UserStore implements Store, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(UserStore.class.getName());
    private static final Store INST = new UserStore();
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private UserStore() { }

    public static Store instOf() {
        return INST;
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            LOG.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }
    }


    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }


    @Override
    public User findUserByLogin(String login) {
        return tx(session -> {
            String sql = "FROM User WHERE login=:login";
            final Query<User> query = session.createQuery(sql);
            query.setParameter("login", login);
            return query.uniqueResult();
        });
    }

    @Override
    public void save(User user) {
        try {
            tx(session -> {
                session.save(user);
                return user;
            });
        } catch (Exception e) {
            LOG.error("Ошибка сохранения нового пользователя");
        }
    }

    @Override
    public void delete(User user) {
        tx(session -> {
            session.delete(user);
            return user;
        });
    }

    @Override
    public List<City> findAllCites() {
        return tx(session -> {
            String sql = "FROM City city ORDER BY city.name ";
            final Query query = session.createQuery(sql);
            return query.list();
        });
    }

    @Override
    public List<CarModel> findAllCarModel() {
        return tx(session -> {
            String sql = "FROM CarModel carModel ORDER BY carModel.name ";
            final Query query = session.createQuery(sql);
            return query.list();
        });
    }

    @Override
    public List<CarBodyType> findAllCarBodyType() {
        return tx(session -> {
            String sql = "FROM CarBodyType body ORDER BY body.name ";
            final Query query = session.createQuery(sql);
            return query.list();
        });
    }

    @Override
    public List<CarEngineType> findAllCarEngineType() {
        return tx(session -> {
            String sql = "FROM CarEngineType engine ORDER BY engine.name ";
            final Query query = session.createQuery(sql);
            return query.list();
        });
    }

    @Override
    public List<CarTransmissionBoxType> findAllCarTransmissionBoxType() {
        return tx(session -> {
            String sql = "FROM CarTransmissionBoxType transmission "
                    + "ORDER BY transmission.name";
            final Query query = session.createQuery(sql);
            return query.list();
        });
    }

    @Override
    public List<AdsType> findAllAdsType() {
        return tx(session -> {
            String sql = "FROM AdsType";
            final Query query = session.createQuery(sql);
            return query.list();
        });
    }

    @Override
    public Ads findAdsById(int id) {
        return tx(session -> {
            String sql = "SELECT ads "
                    + "FROM Ads ads "
                    + "LEFT JOIN FETCH ads.user "
                    + "LEFT JOIN FETCH ads.city "
                    + "LEFT JOIN FETCH ads.adsType "
                    + "LEFT JOIN FETCH ads.car car "
                    + "LEFT JOIN FETCH car.carModel "
                    + "LEFT JOIN FETCH car.carPhotos "
                    + "LEFT JOIN FETCH car.carBodyType "
                    + "LEFT JOIN FETCH car.carEngineType "
                    + "LEFT JOIN FETCH car.carTransmissionBoxType "
                    + "WHERE ads.id = :aid";
            final Query query = session.createQuery(sql);
            query.setParameter("aid", id);
            Ads ads = (Ads) query.uniqueResult();
            return ads;
        });
    }

    @Override
    public List<Ads> findAdsByUserId(int userId) {
        return tx(session -> {
            String sql = "SELECT DISTINCT ads "
                    + "FROM Ads ads "
                    + "LEFT JOIN FETCH ads.user "
                    + "LEFT JOIN FETCH ads.city "
                    + "LEFT JOIN FETCH ads.adsType "
                    + "LEFT JOIN FETCH ads.car car "
                    + "LEFT JOIN FETCH car.carModel "
                    + "LEFT JOIN FETCH car.carPhotos "
                    + "LEFT JOIN FETCH car.carBodyType "
                    + "LEFT JOIN FETCH car.carEngineType "
                    + "LEFT JOIN FETCH car.carTransmissionBoxType "
                    + "WHERE ads.user.id = :uId";
            final Query query = session.createQuery(sql);
            query.setParameter("uId", userId);
            return query.list();
        });
    }

    @Override
    public List<Ads> findAllAds() {
        return tx(session -> {
            String sql = "SELECT DISTINCT ads "
                    + "FROM Ads ads "
                    + "LEFT JOIN FETCH ads.user "
                    + "LEFT JOIN FETCH ads.city "
                    + "LEFT JOIN FETCH ads.adsType "
                    + "LEFT JOIN FETCH ads.car car "
                    + "LEFT JOIN FETCH car.carModel "
                    + "LEFT JOIN FETCH car.carPhotos "
                    + "LEFT JOIN FETCH car.carBodyType "
                    + "LEFT JOIN FETCH car.carEngineType "
                    + "LEFT JOIN FETCH car.carTransmissionBoxType "
                    + "WHERE ads.isSold = false";
            final Query query = session.createQuery(sql);
            return query.list();
        });
    }

    @Override
    public void save(Ads ads) {
        try {
            tx(session -> {
                User user = session.get(User.class, ads.getUser().getId());
                AdsType adsType = session.get(
                        AdsType.class, ads.getAdsType().getId()
                );
                City city = session.get(City.class, ads.getCity().getId());
                user.addAds(ads);
                adsType.addAds(ads);
                city.addAds(ads);
                ads.getCar().setAds(ads);
                return true;
            });
        } catch (Exception e) {
            LOG.error("Ошибка сохранения нового объявления");
        }
    }

    @Override
    public void update(Ads ads) {
        tx(session -> {
            session.update(ads);
            return true;
        });
    }

    @Override
    public void saveCarPhoto(CarPhoto carPhoto, int adsId) {
        final Ads ads = findAdsById(adsId);
        tx(session -> {
            ads.getCar().addCarPhoto(carPhoto);
            session.update(ads);
            return true;
        });
    }
}
