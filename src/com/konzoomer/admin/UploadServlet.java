package com.konzoomer.admin;

import com.konzoomer.Utils;
import com.konzoomer.domain.Store;
import com.konzoomer.jaxb.LocationType;
import com.konzoomer.jaxb.Objects;
import com.konzoomer.jaxb.StoreType;
import com.konzoomer.repo.PMF;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 10-12-2010
 * Time: 19:06:44
 */
public class UploadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload();

        HashMap<String, String> requestParameters = new HashMap<String, String>();

        // Parse the request
        try {
            FileItemIterator iterator = upload.getItemIterator(request);
            while (iterator.hasNext()) {
                FileItemStream item = iterator.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    String value = Streams.asString(stream, request.getCharacterEncoding());
                    requestParameters.put(name, value);
                } else {
                    JAXBContext jc = JAXBContext.newInstance("com.konzoomer.jaxb");
                    Unmarshaller u = jc.createUnmarshaller();

                    response.setContentType("text/plain");
                    PrintWriter responseWriter = response.getWriter();
                    responseWriter.println("Processing: " + item.getName());

                    ZipInputStream zis = new ZipInputStream(stream);
                    ZipEntry entry;

                    // Read each entry from the ZipInputStream until no more entry found 
                    // indicated by a null return value of the getNextEntry() method.
                    while ((entry = zis.getNextEntry()) != null) {
                        responseWriter.println("Unzipping: " + entry.getName());

                        ByteArrayInputStream bais = readZipEntry(zis);
                        Objects objects = (Objects) u.unmarshal(bais);
                        if ("store".equals(requestParameters.get("type")))
                            processStores(objects.getStore(), responseWriter);
                        else if ("location".equals(requestParameters.get("type")))
                            processLocations(objects.getLocation(), responseWriter);
                        else
                            responseWriter.println("Can't process type: " + requestParameters.get("type"));
                    }
                }
            }
        } catch (JAXBException e) {
            throw new ServletException(e);
        } catch (FileUploadException e) {
            throw new ServletException(e);
        }
    }

    private void processLocations(List<LocationType> locations, PrintWriter responseWriter) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(
                Store.class, "streetName==:streetName && " +
                        "streetBuildingIdentifier==:streetBuildingIdentifier && " +
                        "postCodeIdentifier==:postCodeIdentifier && districtName==:districtName && " +
                        "districtSubDivisionIdentifier==:districtSubDivisionIdentifier");

        int updatedStores = 0;

        for (LocationType location : locations) {
            String streetName = location.getStreetName();
            String streetBuildingIdentifier = location.getStreetBuildingIdentifier();
            String postCodeIdentifier = location.getPostCodeIdentifier();
            String districtName = location.getDistrictName();
            String districtSubDivisionIdentifier = location.getDistrictSubDivisionIdentifier();

            // Check if store exists
            List<Store> matchedStores = (List<Store>) query.executeWithArray(streetName, streetBuildingIdentifier, postCodeIdentifier, districtName, districtSubDivisionIdentifier);
            for (Store storeDB : matchedStores) {

                if (matchedStores.size() > 1)
                    responseWriter.println("-!- multiple stores at same address. Store.id=" + storeDB.getId() + ", Store.chainID=" + storeDB.getChainID());

                // TODO if storeDB has latitude/longitude - log both coordinates & address for manual verification
                storeDB.setLastTouched(System.currentTimeMillis());
                storeDB.setLatitude(location.getLatitude());
                storeDB.setLongitude(location.getLongitude());

                pm.makePersistent(storeDB);
                updatedStores++;
            }

            if (matchedStores.size() > 1)
                responseWriter.println();
        }

        pm.close();

        responseWriter.println("Locations: " + locations.size());
        responseWriter.println("Updated Stores: " + updatedStores);
    }

    private void processStores(List<StoreType> stores, PrintWriter responseWriter) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(
                Store.class, "chainID==:chainID && streetName==:streetName && " +
                        "streetBuildingIdentifier==:streetBuildingIdentifier && " +
                        "postCodeIdentifier==:postCodeIdentifier && districtName==:districtName && " +
                        "districtSubDivisionIdentifier==:districtSubDivisionIdentifier");

        int updatedStores = 0;
        int insertedStores = 0;

        for (StoreType store : stores) {
            Long chainID = (long) store.getChainID();
            String streetName = store.getStreetName();
            String streetBuildingIdentifier = store.getStreetBuildingIdentifier();
            String postCodeIdentifier = store.getPostCodeIdentifier();
            String districtName = store.getDistrictName();
            String districtSubDivisionIdentifier = store.getDistrictSubDivisionIdentifier();

            // Check if store exists
            List<Store> matchedStores = (List<Store>) query.executeWithArray(chainID, streetName, streetBuildingIdentifier, postCodeIdentifier, districtName, districtSubDivisionIdentifier);
            if (matchedStores.size() > 0) {
                if (matchedStores.size() > 1)
                    responseWriter.println("-!- Multiple matches for chain: " + chainID + " and address: " +
                            streetName + " " + streetBuildingIdentifier + ", " +
                            postCodeIdentifier + " " + districtName + ", " + districtSubDivisionIdentifier);

                Store storeDB = matchedStores.get(0);
                setStoreUnmatchables(store, storeDB);

                pm.makePersistent(storeDB);
                updatedStores++;
            } else {
                Store storeDB = new Store();
                storeDB.setChainID(chainID);
                storeDB.setStreetName(streetName);
                storeDB.setStreetBuildingIdentifier(streetBuildingIdentifier);
                storeDB.setPostCodeIdentifier(postCodeIdentifier);
                storeDB.setDistrictName(districtName);
                storeDB.setDistrictSubDivisionIdentifier(districtSubDivisionIdentifier);
                setStoreUnmatchables(store, storeDB);

                pm.makePersistent(storeDB);
                insertedStores++;
            }
        }

        pm.close();

        responseWriter.println("Stores: " + stores.size());
        responseWriter.println("Updated Stores: " + updatedStores);
        responseWriter.println("Inserted Stores: " + insertedStores);
    }

    private ByteArrayInputStream readZipEntry(ZipInputStream zis) throws IOException {
        int size;
        byte[] buffer = new byte[2048];

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos, buffer.length);

        while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
            bos.write(buffer, 0, size);
        }
        bos.flush();
        bos.close();

        return new ByteArrayInputStream(baos.toByteArray());
    }

    private static void setStoreUnmatchables(StoreType store, Store storeDB) {
        storeDB.setLastTouched(System.currentTimeMillis());
        storeDB.setName(store.getName());
        storeDB.setContactPerson(store.getContactPerson());
        storeDB.setTelephone(store.getTelephone());
        storeDB.setTelefax(store.getTelefax());
        storeDB.setEmail(store.getEmail());
        storeDB.setWebsite(store.getWebsite());
        if (store.getLatitude() != 0)
            storeDB.setLatitude(store.getLatitude());
        if (store.getLongitude() != 0)
            storeDB.setLongitude(store.getLongitude());
        XMLGregorianCalendar calendar = store.getMondayOpen();
        if (calendar != null)
            storeDB.setMondayOpen(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getMondayClose();
        if (calendar != null)
            storeDB.setMondayClose(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getTuesdayOpen();
        if (calendar != null)
            storeDB.setTuesdayOpen(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getTuesdayClose();
        if (calendar != null)
            storeDB.setTuesdayClose(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getWednesdayOpen();
        if (calendar != null)
            storeDB.setWednesdayOpen(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getWednesdayClose();
        if (calendar != null)
            storeDB.setWednesdayClose(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getThursdayOpen();
        if (calendar != null)
            storeDB.setThursdayOpen(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getThursdayClose();
        if (calendar != null)
            storeDB.setThursdayClose(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getFridayOpen();
        if (calendar != null)
            storeDB.setFridayOpen(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getFridayClose();
        if (calendar != null)
            storeDB.setFridayClose(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getSaturdayOpen();
        if (calendar != null)
            storeDB.setSaturdayOpen(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getSaturdayClose();
        if (calendar != null)
            storeDB.setSaturdayClose(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getSundayOpen();
        if (calendar != null)
            storeDB.setSundayOpen(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getSundayClose();
        if (calendar != null)
            storeDB.setSundayClose(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getFirstAndLastSundayInMonthOpen();
        if (calendar != null)
            storeDB.setFirstAndLastSundayInMonthOpen(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
        calendar = store.getFirstAndLastSundayInMonthClose();
        if (calendar != null)
            storeDB.setFirstAndLastSundayInMonthClose(calendar.getHour() * Utils.HOUR_IN_MILLISECONDS + calendar.getMinute() * Utils.MINUTE_IN_MILLISECONDS);
    }
}
