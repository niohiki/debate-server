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
import org.niohiki.debateserver.DebateSession;
import org.niohiki.debateserver.DebateSession.Teams.Team;
import org.niohiki.debateserver.Locale;
import org.niohiki.debateserver.chronometer.Chronometer;
import org.niohiki.debateserver.html.Body;
import org.niohiki.debateserver.html.CSSLink;
import org.niohiki.debateserver.html.Div;
import org.niohiki.debateserver.html.Form;
import org.niohiki.debateserver.html.HTML;
import org.niohiki.debateserver.html.Head;
import org.niohiki.debateserver.html.Input;
import org.niohiki.debateserver.html.Option;
import org.niohiki.debateserver.html.Script;
import org.niohiki.debateserver.html.Select;
import org.niohiki.debateserver.html.Tag;

public class ChronoServlet extends HttpServlet {

    private final HashMap<String, Chronometer> chronometers;
    private final Configuration configuration;
    private final DebateSession debateSession;
    private final Locale locale;
    private final String md5password;
    private final SecureRandom random;

    public ChronoServlet(Configuration conf, DebateSession session, Locale loc,
            HashMap<String, Chronometer> chronos, String md5pass) {
        configuration = conf;
        debateSession = session;
        locale = loc;
        md5password = md5pass;
        random = new SecureRandom();
        chronometers = chronos;
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
                    Team teamA = debateSession.teams.get(
                            Integer.parseInt(request.getParameter("teama")));
                    Team teamB = debateSession.teams.get(
                            Integer.parseInt(request.getParameter("teamb")));
                    Chronometer newChronometer = new Chronometer(configuration,
                            teamA, teamB);
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
            Chronometer chrono = chronometers.get(request.getParameter("id"));
            StringBuilder JSON = new StringBuilder("");
            JSON.append("{\n").
                    append("\t\"stance\" : \"").append(chrono.stance()).append("\",\n").
                    append("\t\"mainTime\" : \"").append(chrono.mainTimeNanos()).append("\",\n").
                    append("\t\"secondaryTime\" : \"").append(chrono.secondaryTimeNanos()).append("\",\n").
                    append("}");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(JSON.toString());
        } else if ("1".equals(request.getParameter("watch"))) {
            Chronometer chrono = chronometers.get(request.getParameter("id"));
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(makeWatch(chrono));
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
                    new Div("select_text").content(chrono.name()),
                    new Div("select_text select_button").content(locale.chrono.watch).
                    attribute("onClick", "location.href='chrono?watch=1&id=" + key + "'"),
                    new Div("select_text select_button").content(locale.chrono.control)
            );
        }
        return new HTML().child(
                new Head().child(
                        new CSSLink("/static/chrono.css")
                ),
                new Body().child(
                        new Div("select_list").child(items)
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
        Tag[] teamOptions = new Tag[debateSession.teams.number];
        for (int i = 0; i < teamOptions.length; i++) {
            teamOptions[i] = new Option(Integer.toString(i), debateSession.teams.get(i).name);
        }
        return new HTML().child(
                new Head().child(new CSSLink("/static/chrono.css")),
                new Body().child(
                        new Form("post", "chrono").attribute("class", "new_form").child(
                                new Select("teama").attribute("class", "new_select").child(
                                        teamOptions
                                ),
                                new Select("teamb").attribute("class", "new_select").child(
                                        teamOptions
                                ),
                                new Input().attribute("type", "submit").
                                attribute("value", locale.chrono.newSubmit).
                                attribute("class", "new_button"),
                                new Input().attribute("name", "create").
                                attribute("type", "hidden").
                                attribute("value", "1")
                        )
                )
        ).toHTML();
    }

    private String makeWatch(Chronometer chrono) {
        return new HTML().child(
                new Head().child(
                        new CSSLink("/static/chrono.css"),
                        new Script().attribute("src", "/static/chrono.js")
                ),
                new Body().child(
                        new Div("watch_chrono").attribute("id", "chrono").child(
                                new Div("").child(
                                        new Div("watch_text").content(chrono.name())
                                ),
                                new Div("").child(
                                        new Div("watch_display").attribute("id", "display").content("3:00")
                                ),
                                new Div("").child(
                                        new Div("watch_text").content("LAlaLaalalAlala")
                                )
                        ),
                        new Script().content("readapt(); setInterval(readapt, 200);")
                )
        ).toHTML();
    }
}
