package com.konzoomer;

import javax.servlet.*;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 22-11-2010
 * Time: 20:26:29
 */
public class CharsetFilter implements Filter {

    private static final Logger log = Logger.getLogger(CharsetFilter.class.getName());

    private String encoding;

    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("requestEncoding");
        if (encoding == null)
            encoding = "UTF-8";
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("Before CharsetFilter");
        log.info("request.characterEncoding=" + request.getCharacterEncoding());
        log.info("response.contentType=" + response.getContentType());
        log.info("response.characterEncoding=" + response.getCharacterEncoding());

        // Respect the client-specified character encoding
        // (see HTTP specification section 3.4.1)
        if (request.getCharacterEncoding() == null)
            request.setCharacterEncoding(encoding);

        // Set the default response content type and encoding
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
