package com.konzoomer.admin;

import com.google.appengine.api.datastore.Blob;
import com.konzoomer.Utils;
import com.konzoomer.domain.Offer;
import com.konzoomer.repo.PMF;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 06-11-2010
 * Time: 15:05:51
 */
public class SaveOfferServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(SaveOfferServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload();

        HashMap<String, String> requestParameters = new HashMap<String, String>();
        HashSet<Long> brands = new HashSet<Long>();
        Blob uploadedImageBlob = null;

        // Parse the request
        try {
            FileItemIterator iterator = upload.getItemIterator(request);
            while (iterator.hasNext()) {
                FileItemStream item = iterator.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    String value = Streams.asString(stream, request.getCharacterEncoding());
                    if (name.equals("brands"))
                        brands.add(Long.valueOf(value));
                    else
                        requestParameters.put(name, value);
                } else
                    uploadedImageBlob = new Blob(IOUtils.toByteArray(stream));
            }
        } catch (FileUploadException e) {
            throw new ServletException(e);
        }

        PersistenceManager pm = PMF.get().getPersistenceManager();

        Offer offer;
        Long id;

        try {
            id = Long.valueOf(requestParameters.get("id"));
            offer = pm.getObjectById(Offer.class, id);
        } catch (NumberFormatException e) {
            offer = new Offer();
        } catch (JDOObjectNotFoundException e) {
            response.sendRedirect("/admin/editOffer");
            return;
        }

        offer.setChainID(Long.valueOf(requestParameters.get("chainID")));
        offer.setName(requestParameters.get("name"));
        offer.setDescription(requestParameters.get("description"));

        if (uploadedImageBlob.getBytes().length != 0)
            offer.setImage(uploadedImageBlob);

        try {
            offer.setStart(Utils.DATE_FORMAT.parse(requestParameters.get("start")));
            offer.setEnd(Utils.parseEnd(requestParameters.get("end")));
        } catch (ParseException e) {
            response.sendRedirect("/admin/editOffer?dateUnparsable");
            return;
        }

        offer.setBrands(brands);
        offer.setCategory(Byte.valueOf(requestParameters.get("category")));

        offer.setTotalQuantity(Short.valueOf(requestParameters.get("totalQuantity")));
        offer.setTotalQuantityTo(Short.valueOf(requestParameters.get("totalQuantityTo")));
        offer.setTotalQuantityUnit(Byte.valueOf(requestParameters.get("totalQuantityUnit")));

        offer.setUnitQuantity(Short.valueOf(requestParameters.get("unitQuantity")));
        offer.setUnitQuantityTo(Short.valueOf(requestParameters.get("unitQuantityTo")));
        offer.setUnitQuantityUnit(Byte.valueOf(requestParameters.get("unitQuantityUnit")));

        offer.setNumber(Byte.valueOf(requestParameters.get("number")));
        offer.setType(Byte.valueOf(requestParameters.get("type")));
        offer.setPriceE2(Integer.valueOf(requestParameters.get("priceE2")));

        offer.setRating(Utils.calculateOfferRating(offer));

        // persist offer
        pm.makePersistent(offer);
        pm.close();

        response.sendRedirect("/admin/editOffer?savedWithID=" + offer.getId());
    }
}