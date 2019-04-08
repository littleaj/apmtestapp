package org.littleaj.apmtestapp.servlet31;

import org.littleaj.apmtestapp.model.TestDomain;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

@WebServlet(urlPatterns = "/configure")
public class ConfigureServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/configure.jsp").forward(req, resp);
    }
}
