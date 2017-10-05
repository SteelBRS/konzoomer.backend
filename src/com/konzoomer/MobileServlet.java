package com.konzoomer;

import com.konzoomer.domain.Brand;
import com.konzoomer.domain.Offer;
import com.konzoomer.domain.Store;
import com.konzoomer.repo.PMF;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 05-11-2010
 * Time: 12:48:50
 */
public class MobileServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(MobileServlet.class.getName());

    private static final byte COMMUNICATION_PROTOCOL_VERSION = 1;

    private static final byte REQUEST_GET_OFFERS_WITHIN_DISTANCE = 1;
    private static final byte REQUEST_GET_BRANDS = 2;
    private static final byte REQUEST_GET_STORES_WITHIN_DISTANCE = 3;
    private static final byte REQUEST_GET_STORES_IN_VIEW = 4;
    private static final byte REQUEST_GET_STORE_DETAIL = 5;
    private static final byte REQUEST_GET_CLOSEST_STORE_IN_CHAIN = 6;

    private static final byte RESPONSE_OK = 1;
    private static final byte RESPONSE_BAD_VERSION = 2;

    @Override
    public void init() throws ServletException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        InputStream is = request.getInputStream();
        int versionByte = is.read();
        int requestByte = is.read();

        DataOutputStream dos = new DataOutputStream(response.getOutputStream());

        if (versionByte == COMMUNICATION_PROTOCOL_VERSION) {
            DataInputStream dis = new DataInputStream(is);
            switch (requestByte) {
                case REQUEST_GET_OFFERS_WITHIN_DISTANCE:
                    handleGetOffersWithinDistance(dis, dos);
                    break;
                case REQUEST_GET_BRANDS:
                    handleGetBrands(dis, dos);
                    break;
                case REQUEST_GET_STORES_WITHIN_DISTANCE:
                    handleGetStoresWithinDistance(dis, dos);
                    break;
                case REQUEST_GET_STORES_IN_VIEW:
                    handleGetStoresInView(dis, dos);
                    break;
                case REQUEST_GET_STORE_DETAIL:
                    handleGetStoreDetail(dis, dos);
                    break;
                case REQUEST_GET_CLOSEST_STORE_IN_CHAIN:
                    handleGetClosestStoreInChain(dis, dos);
                    break;
            }
        } else {
            log.info("RESPONSE_BAD_VERSION");
            dos.writeByte(RESPONSE_BAD_VERSION);
        }
    }

    private void handleGetOffersWithinDistance(DataInputStream dis, DataOutputStream dos) throws IOException {
        long latitudeE12 = dis.readLong();
        long longitudeE12 = dis.readLong();
        int radiusMeters = dis.readInt();

        // TODO get chainIDs within distance - maybe not - client brand search should return all offers, not just those in range
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Offer.class, "end>=:timeNow");

        // TODO: Use client time instead of hard coding to Danish time
        Date timeNow = new Date();
        List<Offer> offers = (List<Offer>) query.execute(timeNow);

        dos.writeByte(RESPONSE_OK);
        dos.writeShort(offers.size());
        for (Offer offer : offers) {
            dos.writeLong(offer.getId());
            dos.writeByte((int) offer.getChainID());
            dos.writeUTF(offer.getName());
            dos.writeUTF(offer.getDescription());
            dos.writeLong(offer.getStart().getTime());
            dos.writeLong(offer.getEnd().getTime());
            log.warning("timeNow=" + timeNow);
            log.warning("offer.start=" + offer.getStart());
            log.warning("offer.end=" + offer.getEnd());
            dos.writeByte(offer.getBrands().size());
            for (long brandID : offer.getBrands())
                dos.writeLong(brandID);
            dos.writeByte(offer.getCategory());
            dos.writeShort(offer.getTotalQuantity());
            dos.writeShort(offer.getTotalQuantityTo());
            dos.writeByte(offer.getTotalQuantityUnit());
            dos.writeShort(offer.getUnitQuantity());
            dos.writeShort(offer.getUnitQuantityTo());
            dos.writeByte(offer.getUnitQuantityUnit());
            dos.writeByte(offer.getNumber());
            dos.writeByte(offer.getType());
            dos.writeInt(offer.getPriceE2());
            dos.writeShort(offer.getRating());
        }
    }

    private void handleGetBrands(DataInputStream dis, DataOutputStream dos) throws IOException {
        int noOfRequestedBrands = dis.readInt();
        Set<Long> brandIDs = new TreeSet<Long>();
        for (int i = 0; i < noOfRequestedBrands; i++)
            brandIDs.add(dis.readLong());

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Brand.class, ":brandIDs.contains(id)");
        List<Brand> foundBrands = (List<Brand>) query.execute(brandIDs);

        if (brandIDs.size() != foundBrands.size()) {
            log.severe("Requested brands=" + brandIDs.size() + ", found brands in DB=" + foundBrands.size());
            log.severe("Requested brands: " + brandIDs);
        }

        dos.writeByte(RESPONSE_OK);
        dos.writeInt(foundBrands.size());
        for (Brand brand : foundBrands) {
            dos.writeLong(brand.getId());
            dos.writeUTF(brand.getName());
        }
    }

    private void handleGetStoresWithinDistance(DataInputStream dis, DataOutputStream dos) throws IOException {

        long latitudeE12 = dis.readLong();
        long longitudeE12 = dis.readLong();
        int radiusMeters = dis.readInt();

        Utils.LatLon clientLocation = new Utils.LatLon(latitudeE12 / 1E12, longitudeE12 / 1E12);
        double fromLatitude = Utils.calculateDerivedPosition(clientLocation, radiusMeters, 180.0).latitude;
        double toLatitude = Utils.calculateDerivedPosition(clientLocation, radiusMeters, 0.0).latitude;

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Store.class, "latitude>=:fromLatitude && latitude<=:toLatitude");
        List<Store> storesInLatitude = (List<Store>) query.execute(fromLatitude, toLatitude);
        List<Store> storesWithinDistance = new ArrayList<Store>();
        for (Store store : storesInLatitude)
            if (Utils.calculateDistance(latitudeE12, longitudeE12, store.getLatitude(), store.getLongitude()) <= radiusMeters)
                storesWithinDistance.add(store);

        dos.writeByte(RESPONSE_OK);
        dos.writeShort(storesWithinDistance.size());
        for (Store store : storesWithinDistance) {
            dos.writeLong(store.getId());
            dos.writeByte((int) store.getChainID());
            dos.writeInt((int) Math.round(store.getLatitude() * 1E6));
            dos.writeInt((int) Math.round(store.getLongitude() * 1E6));
        }
    }

    private void handleGetStoresInView(DataInputStream dis, DataOutputStream dos) throws IOException {

        int fromLatitudeE6 = dis.readInt();
        int toLatitudeE6 = dis.readInt();
        int fromLongitudeE6 = dis.readInt();
        int toLongitudeE6 = dis.readInt();

        double fromLatitude = ((double) fromLatitudeE6) / 1E6;
        double toLatitude = ((double) toLatitudeE6) / 1E6;
        double fromLongitude = ((double) fromLongitudeE6) / 1E6;
        double toLongitude = ((double) toLongitudeE6) / 1E6;

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Store.class, "latitude>=:fromLatitude && latitude<=:toLatitude");
        List<Store> storesInLatitude = (List<Store>) query.execute(fromLatitude, toLatitude);
        List<Store> storesInView = new ArrayList<Store>();
        for (Store store : storesInLatitude)
            if (store.getLongitude() >= fromLongitude && store.getLongitude() <= toLongitude)
                storesInView.add(store);

        dos.writeByte(RESPONSE_OK);
        dos.writeShort(storesInView.size());
        for (Store store : storesInView) {
            dos.writeLong(store.getId());
            dos.writeByte((int) store.getChainID());
            dos.writeInt((int) Math.round(store.getLatitude() * 1E6));
            dos.writeInt((int) Math.round(store.getLongitude() * 1E6));
        }
    }

    private void handleGetStoreDetail(DataInputStream dis, DataOutputStream dos) throws IOException {
        long id = dis.readLong();

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Store.class, "id==:id");
        List<Store> stores = (List<Store>) query.execute(id);
        Store store = stores.get(0);

        dos.writeByte(RESPONSE_OK);
        writeStore(dos, store);
    }

    private void handleGetClosestStoreInChain(DataInputStream dis, DataOutputStream dos) throws IOException {
        byte chainID = dis.readByte();
        long latitudeE12 = dis.readLong();
        long longitudeE12 = dis.readLong();

        Store closestStore = null;
        double distanceToStore = Double.MAX_VALUE;

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Store.class, "chainID==:chainID");
        List<Store> stores = (List<Store>) query.execute(chainID);
        for (Store store : stores) {
            Double storeLatitude = store.getLatitude();
            Double storeLongitude = store.getLongitude();
            if (Utils.calculateDistance(latitudeE12, longitudeE12, storeLatitude, storeLongitude) < distanceToStore) {
                closestStore = store;
                distanceToStore = Utils.calculateDistance(latitudeE12, longitudeE12, storeLatitude, storeLongitude);
            }
        }

        if (closestStore != null) {
            dos.writeByte(RESPONSE_OK);
            dos.writeLong(closestStore.getId());
            dos.writeInt((int) Math.round(closestStore.getLatitude() * 1E6));
            dos.writeInt((int) Math.round(closestStore.getLongitude() * 1E6));
            writeStore(dos, closestStore);

        } else
            log.severe("No geocoded stores in chainID=" + chainID);
    }

    private void writeStore(DataOutputStream dos, Store store) throws IOException {
        dos.writeUTF(store.getName());
        dos.writeUTF(store.getStreetName());
        dos.writeUTF(store.getStreetBuildingIdentifier());
        dos.writeUTF(store.getPostCodeIdentifier());
        dos.writeUTF(store.getDistrictName());
        dos.writeUTF(store.getDistrictSubDivisionIdentifier());
        dos.writeUTF(store.getContactPerson());
        dos.writeUTF(store.getTelephone());
        dos.writeUTF(store.getTelefax());
        dos.writeUTF(store.getEmail());
        dos.writeUTF(store.getWebsite());

        dos.writeInt(store.getMondayOpen() == null ? -1 : store.getMondayOpen());
        dos.writeInt(store.getMondayClose() == null ? -1 : store.getMondayClose());
        dos.writeInt(store.getTuesdayOpen() == null ? -1 : store.getTuesdayOpen());
        dos.writeInt(store.getTuesdayClose() == null ? -1 : store.getTuesdayClose());
        dos.writeInt(store.getWednesdayOpen() == null ? -1 : store.getWednesdayOpen());
        dos.writeInt(store.getWednesdayClose() == null ? -1 : store.getWednesdayClose());
        dos.writeInt(store.getThursdayOpen() == null ? -1 : store.getThursdayOpen());
        dos.writeInt(store.getThursdayClose() == null ? -1 : store.getThursdayClose());
        dos.writeInt(store.getFridayOpen() == null ? -1 : store.getFridayOpen());
        dos.writeInt(store.getFridayClose() == null ? -1 : store.getFridayClose());
        dos.writeInt(store.getSaturdayOpen() == null ? -1 : store.getSaturdayOpen());
        dos.writeInt(store.getSaturdayClose() == null ? -1 : store.getSaturdayClose());

        // TODO - if first or last sunday in month - return those opening hours
        dos.writeInt(store.getSundayOpen() == null ? -1 : store.getSundayOpen());
        dos.writeInt(store.getSundayClose() == null ? -1 : store.getSundayClose());
    }
}
