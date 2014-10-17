package org.niohiki.debateserver.servlets;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.niohiki.debateserver.chronometer.Chronometer;

public class ChronoServlet extends HttpServlet {

    private final Chronometer chronometer;

    public ChronoServlet() {
        chronometer = new Chronometer();
        chronometer.mainReset(30);
        chronometer.mainRun();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(chronometer.mainTimeNanos() / (1000 * 1000 * 1000));
    }
}
