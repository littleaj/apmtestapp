package org.littleaj.apmtestapp.servlet31.misc;

import org.littleaj.apmtestapp.model.TestDomain;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextInit implements ServletContextListener {

    private final TestDomain domain;

    public ContextInit() {
        domain = new TestDomain();
        domain.setName("Servlet 3.1");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Setting ServletContext attributes...");
        sce.getServletContext().setAttribute("domain", domain);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Removing ServletContext attributes...");
        sce.getServletContext().removeAttribute("domain");
    }
}
