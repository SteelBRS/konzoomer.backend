package com.konzoomer;

import com.konzoomer.domain.Brand;
import com.konzoomer.domain.Offer;
import com.konzoomer.domain.Store;
import com.konzoomer.domain.Units;
import com.konzoomer.repo.PMF;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 22-11-2010
 * Time: 13:03:33
 */
public class Utils {

    private static final Logger log = Logger.getLogger(Utils.class.getName());

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");
    public static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");
    public static final TimeZone TIME_ZONE_GMT_PLUS_ONE = TimeZone.getTimeZone("GMT+1");
    public static final int MINUTE_IN_MILLISECONDS = 60 * 1000;
    public static final int HOUR_IN_MILLISECONDS = 60 * MINUTE_IN_MILLISECONDS;
    public static final double EARTHS_MEAN_RADIUS_METERS = 6371009.0;

    static {
        DATE_FORMAT.setTimeZone(TIME_ZONE_GMT_PLUS_ONE);
    }

    public static Date parseEnd(String source) throws ParseException {
        Date date = DATE_FORMAT.parse(source);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        calendar.add(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Integer parseTime(String time) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(HOUR_FORMAT.parse(time));
            return calendar.get(Calendar.HOUR_OF_DAY) * Utils.HOUR_IN_MILLISECONDS + calendar.get(Calendar.MINUTE) * Utils.MINUTE_IN_MILLISECONDS;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getTime(Integer time) {
        if (time == 0)      // jsp get converts null to 0
            return "";
        else {
            Calendar open = getResetCalendar();
            open.add(Calendar.MILLISECOND, time);
            return HOUR_FORMAT.format(open.getTime());
        }
    }

    private static Calendar getResetCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar;
    }

    public static boolean contains(Collection<?> coll, Object o) {
        return o != null && coll.contains(o);
    }

    public static String getDateTime(long dateTime) {
        return DATE_FORMAT.format(new Date(dateTime));
    }

    public static Integer getStoreCountInChain(Long chainID) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Store.class, "chainID==:chainID");
        List<Store> storesInChain = (List<Store>) query.execute(chainID);
        return storesInChain.size();
    }

    public static short calculateOfferRating(Offer offer) {

        // Calculate average unit price

        // Get comparable units
        List<Byte> units = new ArrayList<Byte>();
        switch (offer.getTotalQuantityUnit()) {
            case Units.LITER:
            case Units.CENTILITER:
            case Units.MILLILITER:
                units = Arrays.asList(Units.LITER, Units.CENTILITER, Units.MILLILITER);
                break;
            case Units.GRAM:
                units = Arrays.asList(Units.GRAM);
                break;
            case Units.PIECE:
                units = Arrays.asList(Units.PIECE);
                break;
        }

        // Get offer average unit price
        double offerAverageUnitPrice;
        double unitModifier = 1.;
        if (offer.getTotalQuantityUnit() == Units.CENTILITER)
            unitModifier = 100.;
        else if (offer.getTotalQuantityUnit() == Units.MILLILITER)
            unitModifier = 1000.;
        if (offer.getTotalQuantityTo() == 0)
            offerAverageUnitPrice = (offer.getPriceE2() / 100.) / (offer.getTotalQuantity() / unitModifier);
        else
            offerAverageUnitPrice = (offer.getPriceE2() / 100.) / (((offer.getTotalQuantity() + offer.getTotalQuantityTo()) / 2) / unitModifier);

        // Get comparable offers
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Offer.class, "category==" + offer.getCategory() + " && :units.contains(totalQuantityUnit)");
        List<Offer> comparableOffers = (List<Offer>) query.execute(units);

        double categoryAverageUnitPrice = 0.;
        for (Offer comparableOffer : comparableOffers) {
            double averageUnitPrice;
            unitModifier = 1.;
            if (comparableOffer.getTotalQuantityUnit() == Units.CENTILITER)
                unitModifier = 100.;
            else if (comparableOffer.getTotalQuantityUnit() == Units.MILLILITER)
                unitModifier = 1000.;

            if (comparableOffer.getTotalQuantityTo() == 0)
                averageUnitPrice = (comparableOffer.getPriceE2() / 100.) / (comparableOffer.getTotalQuantity() / unitModifier);
            else
                averageUnitPrice = (comparableOffer.getPriceE2() / 100.) / (((comparableOffer.getTotalQuantity() + comparableOffer.getTotalQuantityTo()) / 2) / unitModifier);

            categoryAverageUnitPrice += averageUnitPrice;
            log.warning("Comparable offer found: " + comparableOffer.getName() + ", averageUnitPrice=" + averageUnitPrice);
        }

        categoryAverageUnitPrice /= comparableOffers.size();

        log.warning("Offer average unit price: " + offerAverageUnitPrice);
        log.warning("Category average unit price: " + categoryAverageUnitPrice);

        // TODO use Brand(s)
        // IF unit price is in pr./stk. don't weigh unit price as much as pr./kg. or pr./l
        // Also category weights
        long offerRating = Math.round(Brand.RATING_NONAME * (categoryAverageUnitPrice / offerAverageUnitPrice));
        log.warning("long offerRating: " + offerRating);
        if (offerRating > Short.MAX_VALUE)
            return Short.MAX_VALUE;
        else
            return (short) offerRating;
    }

    /**
     * Calculate distance between 2 points
     *
     * @param latitudeE12    client latitude multiplied by 1E12
     * @param longitudeE12   client longitude multiplied by 1E12
     * @param storeLatitude  store latitude
     * @param storeLongitude store longitude
     * @return distance between points in meters, or Double.MAX_VALUE if either Double is null
     */
    public static double calculateDistance(long latitudeE12, long longitudeE12, Double storeLatitude, Double storeLongitude) {
        if (storeLatitude != null && storeLongitude != null) {
            double lat1 = latitudeE12 / 1E12;
            double lon1 = longitudeE12 / 1E12;
            return calculateDistance(lat1, lon1, storeLatitude, storeLongitude);
        } else
            return Double.MAX_VALUE;
    }

    /**
     * Calculate distance between 2 points
     *
     * @param lat1 point1 latitude
     * @param lon1 point1 longitude
     * @param lat2 point2 latitude
     * @param lon2 point2 longitude
     * @return distance between points in meters
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTHS_MEAN_RADIUS_METERS * c;
    }

    /**
     * Calculates the end-point from a given source at a given range (meters) and bearing (degrees).
     * This methods uses simple geometry equations to calculate the end-point.
     *
     * @param source Point of origin
     * @param range Range in meters
     * @param bearing Bearing in degrees
     * @return End-point from the source given the desired range and bearing.
     */
    public static LatLon calculateDerivedPosition(LatLon source, double range, double bearing) {
        double latA = Math.toRadians(source.latitude);
        double lonA = Math.toRadians(source.longitude);
        double angularDistance = range / EARTHS_MEAN_RADIUS_METERS;
        double trueCourse = Math.toRadians(bearing);
        double lat = Math.asin(Math.sin(latA) * Math.cos(angularDistance) + Math.cos(latA) * Math.sin(angularDistance) * Math.cos(trueCourse));
        double dLon = Math.atan2(Math.sin(trueCourse) * Math.sin(angularDistance) * Math.cos(latA), Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat));
        double lon = ((lonA + dLon + Math.PI) % (2*Math.PI)) - Math.PI;
        return new LatLon(Math.toDegrees(lat), Math.toDegrees(lon));
    } 

    public static class LatLon {
        public final double latitude;
        public final double longitude;

        public LatLon(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
