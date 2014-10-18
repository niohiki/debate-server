package org.niohiki.debateserver.servlets;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.niohiki.debateserver.html.Form;
import org.niohiki.debateserver.html.HTML;
import org.niohiki.debateserver.html.Head;
import org.niohiki.debateserver.html.Input;

public class ChronoServlet extends HttpServlet {

    private final HashMap<String, Chronometer> chronometers;
    private final Configuration configuration;
    private final Locale locale;
    private final String md5password;

    public ChronoServlet(Configuration conf, Locale loc, String md5pass) {
        configuration = conf;
        locale = loc;
        md5password = md5pass;
        chronometers = new HashMap<>();
        Chronometer chronometer = new Chronometer(conf);
        chronometer.mainReset();
        chronometer.mainRun();
        chronometers.put("ajaj", chronometer);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("1".equals(request.getParameter("login"))) {
            String password = request.getParameter("password");
            try {
                String md5try = new String(MessageDigest.getInstance("MD5").
                        digest(password.getBytes()));
                if (md5try.equals(md5password)) {
                    request.getSession().setAttribute("chrono_authenticated", "1");
                    response.sendRedirect((String) request.getSession().
                            getAttribute("login_redirection"));
                }
            } catch (NoSuchAlgorithmException ex) {
            }
        }
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
            if ("1".equals(httpSession.getAttribute("chrono_authenticated"))) {

            } else {
                request.getSession().setAttribute("login_redirection",
                        request.getRequestURI() + "?" + request.getQueryString());
                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(makePassword());
            }
        }
    }

    private String makeChronoList() {
        Div[] items = new Div[chronometers.size() + 1];
        items[0] = new Div("select_item", "", "",
                new ClickableDiv("select_text select_button", "location.href='chrono?new=1'",
                        "", locale.chrono.newChrono)
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

    private String makePassword() {
        return new HTML(
                new Head(
                        new CSSLink("/static/chrono.css")
                ),
                new Body(
                        new Form("password_form", "post", "chrono",
                                new Input("password_text", "password", "", "password"),
                                new Input("", "login", "1", "hidden")
                        )
                )
        ).toHTML();
    }
}
