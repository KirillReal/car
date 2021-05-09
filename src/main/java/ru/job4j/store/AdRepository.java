package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.Car;

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

    public List<Car> getLastDayAds() {
       return tx(session -> {
            Date date = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            date = cal.getTime();

           return session.createQuery("from Car car "
                    + "where car.created > :date", Car.class)
                    .setParameter("date", date).list();
        });
    }

    public List<Car> getAdsWithPhoto() {
        return tx(session -> session.createQuery("select distinct car "
        + "from Car car " + "where car.carPhotos.size > 0 ", Car.class)
                .list());
    }

    public List<Car> getAdsByModel(int carModelId) {
        return tx(session -> session.createQuery("select distinct car "
            + "from Car car " + "where car.carModel.id = :modelId", Car.class)
        .setParameter("modelId", carModelId).list());
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
