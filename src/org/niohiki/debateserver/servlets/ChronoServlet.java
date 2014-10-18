package org.niohiki.debateserver.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.niohiki.debateserver.Configuration;
import org.niohiki.debateserver.Locale;
import org.niohiki.debateserver.chronometer.Chronometer;
import org.niohiki.debateserver.html.Body;
import org.niohiki.debateserver.html.CSSLink;
import org.niohiki.debateserver.html.ClickableDiv;
import org.niohiki.debateserver.html.Div;
import org.niohiki.debateserver.html.HTML;
import org.niohiki.debateserver.html.Head;

public class ChronoServlet extends HttpServlet {

    private final HashMap<String, Chronometer> chronometers;
    private final Configuration configuration;
    private final Locale locale;

    public ChronoServlet(Configuration conf, Locale loc) {
        configuration = conf;
        locale = loc;
        chronometers = new HashMap<>();
        Chronometer chronometer = new Chronometer(conf);
        chronometer.mainReset();
        chronometer.mainRun();
        chronometers.put("ajaj", chronometer);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameterMap().isEmpty()) {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(makeChronoList());
        } else if ("1".equals(request.getParameter("get"))) {

        } else if ("1".equals(request.getParameter("see"))) {
            Chronometer chrono = chronometers.get(request.getParameter("id"));

            response.setContentType("text");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(chrono.mainTimeNanos() / (1000 * 1000 * 1000));
        } else {
            HttpSession httpSession = request.getSession();
            if ("1".equals(httpSession.getAttribute("authenticated"))) {

            } else {

            }
        }
    }

    private String makeChronoList() {
        Div[] items = new Div[chronometers.size() + 1];
        items[0] = new ClickableDiv("select_item", "location.href='chrono?new=1'", "", "",
                new Div("select_text select_button", "", locale.chrono.newChrono)
        );
        Iterator<String> keys = chronometers.keySet().iterator();
        for (int i = 1; i < items.length; i++) {
            String key = keys.next();
            Chronometer chrono = chronometers.get(key);
            items[i] = new Div("select_item", "", "",
                    new Div("select_text", "", key),
                    new Div("select_text select_button", "", locale.chrono.see),
                    new Div("select_text select_button", "", locale.chrono.control)
            );
        }
        return new HTML(
                new Head(
                        new CSSLink("/static/chrono.css")
                ),
                new Body(
                        new Div("select_list", "", "",
                                items
                        )
                )
        ).toHTML();
    }
}
