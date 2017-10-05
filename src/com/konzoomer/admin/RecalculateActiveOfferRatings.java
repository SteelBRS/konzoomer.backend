package com.konzoomer.admin;

import com.konzoomer.Utils;
import com.konzoomer.domain.Offer;
import com.konzoomer.repo.PMF;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 05-01-2011
 * Time: 12:40:59
 */
public class RecalculateActiveOfferRatings extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        byte category = 0;
        try {
            category = Byte.valueOf(request.getParameter("category"));
        } catch (NumberFormatException e) {
            // No category indicated
        }

        response.setContentType("text/plain");
        PrintWriter responseWriter = response.getWriter();

        int updatedOfferRatings = 0;

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Date now = new Date();
        List<Offer> offers;
        if (category == 0)
            offers = (List<Offer>) pm.newQuery(Offer.class, "end>:now").execute(now);
        else
            offers = (List<Offer>) pm.newQuery(Offer.class, "end>:now && category==:category").execute(now, category);

        for (Offer offer : offers) {
            short oldRating = offer.getRating();
            short newRating = Utils.calculateOfferRating(offer);

            if (oldRating != newRating) {
                responseWriter.println("Offer: " + offer.getId() + ", oldRating=" + oldRating + ", newRating=" + newRating);

                offer.setRating(newRating);
                // persist offer
                pm.makePersistent(offer);
                updatedOfferRatings++;
            }
        }

        responseWriter.println();
        responseWriter.println("Updated offer ratings: " + updatedOfferRatings);

        pm.close();
    }
}
