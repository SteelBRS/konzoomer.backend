package com.konzoomer;

import com.konzoomer.domain.Offer;
import com.konzoomer.repo.PMF;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 22-11-2010
 * Time: 12:01:58
 */
public class GetOfferImageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.valueOf(request.getParameter("id"));

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Offer offer = pm.getObjectById(Offer.class, id);

        byte[] image = offer.getImage().getBytes();

        response.setContentType("image/jpeg");
        response.setContentLength(image.length);
        response.getOutputStream().write(image);
    }
}
