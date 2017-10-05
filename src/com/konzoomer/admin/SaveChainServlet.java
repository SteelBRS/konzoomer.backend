package com.konzoomer.admin;

import com.konzoomer.domain.Chain;
import com.konzoomer.repo.PMF;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 09-11-2010
 * Time: 22:33:48
 */
public class SaveChainServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Chain chain;
        Long id = 0L;
        try {
            id = Long.parseLong(request.getParameter("id"));
            chain = pm.getObjectById(Chain.class, id);
        } catch (NumberFormatException e) {
            response.sendRedirect("/admin/editChain");
            return;
        } catch (JDOObjectNotFoundException e) {
            chain = new Chain();
        }

        Long motherChainID;
        try {
            motherChainID = Long.parseLong(request.getParameter("motherChainID"));
        } catch (NumberFormatException e) {
            motherChainID = null;
        }

        String name = request.getParameter("name");
        String hasStores = request.getParameter("hasStores");
        String country = request.getParameter("country");
        String currency = request.getParameter("currency");

        chain.setId(id);
        chain.setMotherChainID(motherChainID);
        chain.setName(name);
        chain.setHasStores(hasStores != null);
        chain.setCountry(country);
        chain.setCurrency(currency);

        try {
            pm.makePersistent(chain);
        } finally {
            pm.close();
        }

        response.sendRedirect("/admin/editChain?id=" + chain.getId());
    }
}
