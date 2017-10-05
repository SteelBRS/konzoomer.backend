package com.konzoomer.admin;

import com.konzoomer.domain.Brand;
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
 * Date: 18-11-2010
 * Time: 11:44:09
 */
public class SaveBrandServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Brand brand;
        Long id;
        try {
            id = Long.parseLong(request.getParameter("id"));
            brand = pm.getObjectById(Brand.class, id);
        } catch (NumberFormatException e) {
            brand = new Brand();
        } catch (JDOObjectNotFoundException e) {
            response.sendRedirect("/admin/editBrand");
            return;
        }

        String name = request.getParameter("name");

        // Validate brand name doesn't already exist
        brand.setName(name);
        brand.setRating(Short.parseShort(request.getParameter("rating")));
        try {
            pm.makePersistent(brand);
        } finally {
            pm.close();
        }

        response.sendRedirect("/admin/editBrand");
    }
}
