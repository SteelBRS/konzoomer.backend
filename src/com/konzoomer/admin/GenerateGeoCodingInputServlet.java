package com.konzoomer.admin;

import com.konzoomer.domain.Store;
import com.konzoomer.repo.PMF;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 12-12-2010
 * Time: 14:25:04
 */
public class GenerateGeoCodingInputServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/plain");
        PrintWriter responseWriter = response.getWriter();

        responseWriter.println("CREATE  TABLE test.Location (");
        responseWriter.println("  streetName VARCHAR(255) NOT NULL ,");
        responseWriter.println("  streetBuildingIdentifier VARCHAR(255) NOT NULL ,");
        responseWriter.println("  postCodeIdentifier VARCHAR(255) NOT NULL ,");
        responseWriter.println("  districtName VARCHAR(255) NOT NULL ,");
        responseWriter.println("  districtSubDivisionIdentifier VARCHAR(255) NOT NULL);");
        responseWriter.println();

        PersistenceManager pm = PMF.get().getPersistenceManager();
        List<Store> stores = (List<Store>) pm.newQuery(Store.class).execute();
        for (Store store : stores)
            if ((store.getLatitude() == null || store.getLatitude() == 0) ||
                    (store.getLongitude() == null || store.getLongitude() == 0))
                responseWriter.println("INSERT INTO test.Location VALUES (\"" + store.getStreetName() + "\",\"" +
                        store.getStreetBuildingIdentifier() + "\",\"" + store.getPostCodeIdentifier() + "\",\"" +
                        store.getDistrictName() + "\",\"" + store.getDistrictSubDivisionIdentifier() + "\");");
    }
}
