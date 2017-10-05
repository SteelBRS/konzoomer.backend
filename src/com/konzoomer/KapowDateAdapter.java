package com.konzoomer;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 11-12-2010
 * Time: 13:34:13
 */
public class KapowDateAdapter extends XmlAdapter<String, XMLGregorianCalendar>{

    private static final SimpleDateFormat KAPOW_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    private static final GregorianCalendar CALENDAR = new GregorianCalendar(Utils.TIME_ZONE_GMT);
    private static final DatatypeFactory DATATYPE_FACTORY;

    static {
        try {
            DATATYPE_FACTORY = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public XMLGregorianCalendar unmarshal(String input) {
        Date date;
        try {
            date = KAPOW_DATE_FORMAT.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        CALENDAR.setTime(date);
        return DATATYPE_FACTORY.newXMLGregorianCalendar(CALENDAR);
    }

    public String marshal(XMLGregorianCalendar input) {
        String output = null;
        if (input != null)
            output = KAPOW_DATE_FORMAT.format(input.toGregorianCalendar().getTime());
        return output;
    }
}
