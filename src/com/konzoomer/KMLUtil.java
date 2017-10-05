package com.konzoomer;

import com.konzoomer.domain.Store;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 15-12-2010
 * Time: 19:52:17
 */
public class KMLUtil {

    private static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");
    
    private final Store store;

    /**
     * Default constructor for useBean tag
     */
    public KMLUtil() {
        store = null;
    }

    public KMLUtil(Store store) {
        this.store = store;
    }

    public String getName() {
        return store.getName();
    }

    public String getOpeningHoursToday() {
        // TODO: Use client time instead of hard coding to Danish time
        int dayOfWeek = Calendar.getInstance(Utils.TIME_ZONE_GMT_PLUS_ONE).get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return getOpeningHours(store.getMondayOpen(), store.getMondayClose());
            case Calendar.TUESDAY:
                return getOpeningHours(store.getTuesdayOpen(), store.getTuesdayClose());
            case Calendar.WEDNESDAY:
                return getOpeningHours(store.getWednesdayOpen(), store.getWednesdayClose());
            case Calendar.THURSDAY:
                return getOpeningHours(store.getThursdayOpen(), store.getThursdayClose());
            case Calendar.FRIDAY:
                return getOpeningHours(store.getFridayOpen(), store.getFridayClose());
            case Calendar.SATURDAY:
                return getOpeningHours(store.getSaturdayOpen(), store.getSaturdayClose());
            case Calendar.SUNDAY:
                return getOpeningHours(store.getSundayOpen(), store.getSundayClose());
            default:
                throw new RuntimeException("Strange DAY_OF_WEEK: " + dayOfWeek);
        }
    }

    private String getOpeningHours(Integer mondayOpen, Integer mondayClose) {
        if (mondayOpen != null && mondayClose != null) {
            Calendar open = getResetCalendar();
            Calendar close = getResetCalendar();
            open.add(Calendar.MILLISECOND, mondayOpen);
            close.add(Calendar.MILLISECOND, mondayClose);
            return HOUR_FORMAT.format(open.getTime()) + " - " + HOUR_FORMAT.format(close.getTime());
        } else
            return "lukket";
    }

    private Calendar getResetCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar;
    }

    public String getTelephone() {
        return store.getTelephone();
    }

    public String getStreetAndNumber() {
        return store.getStreetName() + " " + store.getStreetBuildingIdentifier();
    }

    public String getDistrictSubDivision() {
        return store.getDistrictSubDivisionIdentifier();
    }

    public String getPostCodeAndDistrict() {
        return store.getPostCodeIdentifier() + " " + store.getDistrictName();
    }

    public Double getLatitude() {
        return store.getLatitude();
    }

    public Double getLongitude() {
        return store.getLongitude();
    }

    public byte getChainID() {
        return (byte) store.getChainID();
    }

    public String getIconURL() {
        String iconURL = "http://m.konzoomer.com/img/";
        switch((byte) store.getChainID()) {
            case Chains.ID_SUPERBRUGSEN:
                iconURL += "ic_chain_superbrugsen.png";
                break;
            case Chains.ID_DAGLI_BRUGSEN:
                iconURL += "ic_chain_dagli_brugsen.png";
                break;
            case Chains.ID_KVICKLY:
                iconURL += "ic_chain_kvickly.png";
                break;
            case Chains.ID_FAKTA:
                iconURL += "ic_chain_fakta.png";
                break;
            case Chains.ID_IRMA:
                iconURL += "ic_chain_irma.png";
                break;
            case Chains.ID_BILKA:
                iconURL += "ic_chain_bilka.png";
                break;
            case Chains.ID_FOTEX:
                iconURL += "ic_chain_fotex.png";
                break;
            case Chains.ID_NETTO:
                iconURL += "ic_chain_netto.png";
                break;
            case Chains.ID_SPAR:
                iconURL += "ic_chain_spar.png";
                break;
            case Chains.ID_KWIK_SPAR:
                iconURL += "ic_chain_kwik_spar.png";
                break;
            case Chains.ID_SUPER_SPAR:
                iconURL += "ic_chain_super_spar.png";
                break;
            case Chains.ID_EUROSPAR:
                iconURL += "ic_chain_eurospar.png";
                break;
            case Chains.ID_ABC_LAVPRIS:
                iconURL += "ic_chain_abc_lavpris.png";
                break;
            case Chains.ID_ALDI:
                iconURL += "ic_chain_aldi.png";
                break;
            case Chains.ID_KIWI_MINIPRIS:
                iconURL += "ic_chain_kiwi_minipris.png";
                break;
            case Chains.ID_LIDL:
                iconURL += "ic_chain_lidl.png";
                break;
            case Chains.ID_LOVBJERG:
                iconURL += "ic_chain_lovbjerg.png";
                break;
            case Chains.ID_REMA_1000:
                iconURL += "ic_chain_rema_1000.png";
                break;
            case Chains.ID_SUPERBEST:
                iconURL += "ic_chain_superbest.png";
                break;
        }
        return iconURL;
    }

    interface Chains {

        public static final byte ID_SUPERBRUGSEN = 2;
        public static final byte ID_DAGLI_BRUGSEN = 3;
        public static final byte ID_KVICKLY = 4;
        public static final byte ID_FAKTA = 5;
        public static final byte ID_IRMA = 6;
        public static final byte ID_BILKA = 8;
        public static final byte ID_FOTEX = 9;
        public static final byte ID_NETTO = 10;
        public static final byte ID_SPAR = 12;
        public static final byte ID_KWIK_SPAR = 13;
        public static final byte ID_SUPER_SPAR = 14;
        public static final byte ID_EUROSPAR = 15;
        public static final byte ID_ABC_LAVPRIS = 16;
        public static final byte ID_ALDI = 17;
        public static final byte ID_KIWI_MINIPRIS = 18;
        public static final byte ID_LIDL = 19;
        public static final byte ID_LOVBJERG = 20;
        public static final byte ID_REMA_1000 = 21;
        public static final byte ID_SUPERBEST = 22;
    }
}
