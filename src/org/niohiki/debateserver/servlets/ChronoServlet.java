package org.niohiki.debateserver.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
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
import org.niohiki.debateserver.html.Div;
import org.niohiki.debateserver.html.Form;
import org.niohiki.debateserver.html.HTML;
import org.niohiki.debateserver.html.Head;
import org.niohiki.debateserver.html.Input;
import org.niohiki.debateserver.html.Tag;

public class ChronoServlet extends HttpServlet {

    private final HashMap<String, Chronometer> chronometers;
    private final Configuration configuration;
    private final Locale locale;
    private final String md5password;
    private final SecureRandom random;

    public ChronoServlet(Configuration conf, Locale loc, String md5pass) {
        configuration = conf;
        locale = loc;
        md5password = md5pass;
        random = new SecureRandom();
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
                } else {
                    response.sendRedirect("/chrono");
                }
            } catch (NoSuchAlgorithmException ex) {
            }
        } else {
            HttpSession httpSession = request.getSession();
            if ("1".equals(httpSession.getAttribute("chrono_authenticated"))) {
                if ("1".equals(request.getParameter("create"))) {
                    Chronometer newChronometer = new Chronometer(configuration);
                    String id;
                    do {
                        id = new BigInteger(130, random).toString(32);
                    } while (chronometers.keySet().contains(id));
                    chronometers.put(id, newChronometer);
                    response.sendRedirect("/chrono");
                }
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
                if ("1".equals(request.getParameter("new"))) {
                    response.setContentType("text/html");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println(makeNew());
                }
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
        Tag[] items = new Div[chronometers.size() + 1];
        items[0] = new Div("select_item").child(
                new Div("select_text select_button").
                content(locale.chrono.newChrono).
                attribute("onClick", "location.href='chrono?new=1'")
        );
        Iterator<String> keys = chronometers.keySet().iterator();
        for (int i = 1; i < items.length; i++) {
            String key = keys.next();
            Chronometer chrono = chronometers.get(key);
            items[i] = new Div("select_item").child(
                    new Div("select_text").content(key),
                    new Div("select_text select_button").content(locale.chrono.see),
                    new Div("select_text select_button").content(locale.chrono.control)
            );
        }
        return new HTML().child(
                new Head().child(
                        new CSSLink("/static/chrono.css")
                ),
                new Body().child(
                        new Div("main").child(items)
                )
        ).toHTML();
    }

    private String makePassword() {
        return new HTML().child(
                new Head().child(new CSSLink("/static/chrono.css")),
                new Body().child(
                        new Form("post", "chrono").attribute("class", "password_form").child(
                                new Input().
                                attribute("name", "password").attribute("type", "password").
                                attribute("value", "").attribute("class", "password_text"),
                                new Input().
                                attribute("name", "login").attribute("type", "hidden").
                                attribute("value", "1")
                        )
                )
        ).toHTML();
    }

    private String makeNew() {
        return new HTML().child(
                new Head().child(new CSSLink("/static/chrono.css")),
                new Body().child(
                        new Form("post", "chrono").attribute("class", "password_form").child(
                                new Input().
                                attribute("name", "name").attribute("type", "text").
                                attribute("value", "").attribute("class", "password_text"),
                                new Input().
                                attribute("name", "create").attribute("type", "hidden").
                                attribute("value", "1")
                        )
                )
        ).toHTML();
    }
}
