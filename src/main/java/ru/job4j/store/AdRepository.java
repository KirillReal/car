package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.Ads;
import ru.job4j.model.Car;
import ru.job4j.model.CarBodyType;
import ru.job4j.model.CarModel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Function;

public class AdRepository implements AutoCloseable {
    public static final AdRepository INST = new AdRepository();
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();


    private AdRepository() { }

    public static AdRepository instOf() {
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
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Ads> getLastDayAds() {
       return tx(session -> {
            Date date = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            date = cal.getTime();

           return session.createQuery("from Ads ad "
                    + "where ad.created > :date", Ads.class)
                    .setParameter("date", date).list();
        });
    }

    public List<Ads> getAdsWithPhoto() {
        return this.tx(session -> session.createQuery(
                "select distinct ad from Ads ad "
                        + "where ad.car.carPhotos.size > 0", Ads.class)
                .list()
        );
    }

    public List<Ads> getAdsByModel(int carModelId) {
        return tx(session -> session.createQuery("select distinct ads "
            + "from Ads ads " + "where ads.car.carModel.id = :modelId", Ads.class)
        .setParameter("modelId", carModelId).list());
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
