package com.konzoomer.admin;

import com.konzoomer.Utils;
import com.konzoomer.domain.Store;
import com.konzoomer.repo.PMF;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 06-11-2010
 * Time: 15:05:51
 */
public class SaveStoreServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(SaveStoreServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.warning("parameterMap: " + request.getParameterMap());

        PersistenceManager pm = PMF.get().getPersistenceManager();

        Store store;
        Long id;

        try {
            id = Long.valueOf(request.getParameter("id"));
            store = pm.getObjectById(Store.class, id);
        } catch (NumberFormatException e) {
            store = new Store();
        } catch (JDOObjectNotFoundException e) {
            response.sendRedirect("/admin/editOffer");
            return;
        }

        store.setLastTouched(System.currentTimeMillis());
        store.setChainID(Long.valueOf(request.getParameter("chainID")));
        store.setName(request.getParameter("name"));
        store.setStreetName(request.getParameter("streetName"));
        store.setStreetBuildingIdentifier(request.getParameter("streetBuildingIdentifier"));
        store.setPostCodeIdentifier(request.getParameter("postCodeIdentifier"));
        store.setDistrictName(request.getParameter("districtName"));
        store.setDistrictSubDivisionIdentifier(request.getParameter("districtSubDivisionIdentifier"));
        store.setContactPerson(request.getParameter("contactPerson"));
        store.setTelephone(request.getParameter("telephone"));
        store.setTelefax(request.getParameter("telefax"));
        store.setEmail(request.getParameter("email"));
        store.setWebsite(request.getParameter("website"));
        try {
            store.setLatitude(Double.valueOf(request.getParameter("latitude")));
            store.setLongitude(Double.valueOf(request.getParameter("longitude")));
        } catch (NumberFormatException e) {
            // ignore
        }
        store.setMondayOpen(Utils.parseTime(request.getParameter("mondayOpen")));
        store.setMondayClose(Utils.parseTime(request.getParameter("mondayClose")));
        store.setTuesdayOpen(Utils.parseTime(request.getParameter("tuesdayOpen")));
        store.setTuesdayClose(Utils.parseTime(request.getParameter("tuesdayClose")));
        store.setWednesdayOpen(Utils.parseTime(request.getParameter("wednesdayOpen")));
        store.setWednesdayClose(Utils.parseTime(request.getParameter("wednesdayClose")));
        store.setThursdayOpen(Utils.parseTime(request.getParameter("thursdayOpen")));
        store.setThursdayClose(Utils.parseTime(request.getParameter("thursdayClose")));
        store.setFridayOpen(Utils.parseTime(request.getParameter("fridayOpen")));
        store.setFridayClose(Utils.parseTime(request.getParameter("fridayClose")));
        store.setSaturdayOpen(Utils.parseTime(request.getParameter("saturdayOpen")));
        store.setSaturdayClose(Utils.parseTime(request.getParameter("saturdayClose")));
        store.setSundayOpen(Utils.parseTime(request.getParameter("sundayOpen")));
        store.setSundayClose(Utils.parseTime(request.getParameter("sundayClose")));
        store.setFirstAndLastSundayInMonthOpen(Utils.parseTime(request.getParameter("firstAndLastSundayInMonthOpen")));
        store.setFirstAndLastSundayInMonthClose(Utils.parseTime(request.getParameter("firstAndLastSundayInMonthClose")));

        // persist store
        pm.makePersistent(store);
        pm.close();

        response.sendRedirect("/admin/editStore?savedWithID=" + store.getId());
    }
}
