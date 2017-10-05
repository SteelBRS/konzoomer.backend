package com.konzoomer;

import javax.servlet.*;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 19-11-2010
 * Time: 15:09:40
 */
public class ResourceFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            ServletContext context = filterConfig.getServletContext();
            Properties categories_da = new Properties();
            categories_da.loadFromXML(context.getResourceAsStream("/WEB-INF/res/categories_da.xml"));
            context.setAttribute("categories_da", categories_da);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(request, response);
    }

    public void destroy() {
    }
}
