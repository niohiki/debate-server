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
import org.niohiki.debateserver.Configuration.Stances.Stance;
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
                    newChronometer.mainReset();
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
            String key = request.getParameter("id");
            Chronometer chrono = chronometers.get(key);
            if (chrono != null) {
                String mainTag = chrono.isMainRunning()
                        ? locale.chrono.controlPause : locale.chrono.controlRun;
                String secondaryTag = chrono.isSecondaryRunning()
                        ? locale.chrono.controlStop : locale.chrono.controlStart;
                String resetTime = Integer.toString(configuration.stances.get(chrono.getStanceIndex()).time);
                StringBuilder JSON = new StringBuilder("");
                JSON.append("{\n").
                        append("\t\"stance\" : \"").append(chrono.stance()).append("\",\n").
                        append("\t\"name\" : \"").append(chrono.fullName()).append("\",\n").
                        append("\t\"mainTag\" : \"").append(mainTag).append("\",\n").
                        append("\t\"secondaryTag\" : \"").append(secondaryTag).append("\",\n").
                        append("\t\"resetTime\" : ").append(resetTime).append(",\n").
                        append("\t\"mainTime\" : ").append(chrono.mainTimeNanos()).append(",\n").
                        append("\t\"secondaryTime\" : ").append(chrono.secondaryTimeNanos()).append("\n").
                        append("}");
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(JSON.toString());
            }
        } else if ("1".equals(request.getParameter("watch"))) {
            String key = request.getParameter("id");
            Chronometer chrono = chronometers.get(key);
            if (chrono != null) {
                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(makeWatch(key, chrono));
            } else {
                response.sendRedirect("/chrono");
            }
        } else {
            HttpSession httpSession = request.getSession();
            if ("1".equals(httpSession.getAttribute("chrono_authenticated"))) {
                if ("1".equals(request.getParameter("new"))) {
                    response.setContentType("text/html");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println(makeNew());
                } else if ("1".equals(request.getParameter("control"))) {
                    String key = request.getParameter("id");
                    Chronometer chrono = chronometers.get(key);
                    if (chrono != null) {
                        if ("1".equals(request.getParameter("manage"))) {
                            if ("main".equals(request.getParameter("type"))) {
                                if ("toggle".equals(request.getParameter("action"))) {
                                    chrono.mainToggle();
                                } else if ("reset".equals(request.getParameter("action"))) {
                                    String time = request.getParameter("time");
                                    if (time != null) {
                                        chrono.mainReset(Long.parseLong(time) * 1000 * 1000 * 1000);
                                    }
                                }
                            } else if ("secondary".equals(request.getParameter("type"))) {
                                if ("toggle".equals(request.getParameter("action"))) {
                                    chrono.secondaryToggle();
                                }
                            } else if ("stance".equals(request.getParameter("type"))) {
                                if ("next".equals(request.getParameter("action"))) {
                                    chrono.nextStance();
                                } else if ("set".equals(request.getParameter("action"))) {
                                    String i = request.getParameter("i");
                                    if (i != null) {
                                        chrono.setStance(Integer.parseInt(i));
                                    }
                                }
                            }
                            if ("sides".equals(request.getParameter("type"))) {
                                if ("swap".equals(request.getParameter("action"))) {
                                    chrono.swapTeamSides();
                                }
                            }
                        } else {
                            response.setContentType("text/html");
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().println(makeControl(key, chrono));
                        }
                    } else {
                        response.sendRedirect("/chrono");
                    }
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
                    new Div("select_text").content(chrono.shortName()),
                    new Div("select_text select_button").content(locale.chrono.watch).
                    attribute("onClick", "location.href='chrono?watch=1&id=" + key + "'"),
                    new Div("select_text select_button").content(locale.chrono.control).
                    attribute("onClick", "location.href='chrono?control=1&id=" + key + "'")
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

    private String makeWatch(String id, Chronometer chrono) {
        return new HTML().child(
                new Head().child(
                        new CSSLink("/static/chrono.css"),
                        new Script().attribute("src", "/static/chrono.js")
                ),
                new Body().child(
                        new Div("watch_chrono").attribute("id", "chrono").child(
                                new Div("").child(
                                        new Div("watch_text").attribute("id", "name").content(chrono.fullName())
                                ),
                                new Div("").child(
                                        new Div("watch_display").attribute("id", "display").content("---")
                                ),
                                new Div("").child(
                                        new Div("watch_text").attribute("id", "stance").content("---")
                                )
                        ),
                        new Script().content(
                                "readapt(\"" + id + "\"); "
                                + "setInterval(function(){readapt(\"" + id + "\");}, 200);")
                )
        ).toHTML();
    }

    private String makeControl(String id, Chronometer chrono) {
        Tag[] stances = new Tag[configuration.stances.number];
        for (int i = 0; i < stances.length; i++) {
            Stance stance = configuration.stances.get(i);
            stances[i] = new Div("control_button").content(stance.fullName).
                    attribute("onClick", "control('" + id + "','type=stance&action=set&i=" + i + "')");
        }
        return new HTML().child(
                new Head().child(
                        new CSSLink("/static/chrono.css"),
                        new Script().attribute("src", "/static/chrono.js")
                ),
                new Body().child(
                        new Div("control_block").child(
                                new Div("control_button control_button_big").child(
                                        new Div("").attribute("id", "main").content(locale.chrono.controlRun),
                                        new Div("").content(locale.chrono.controlMain)
                                ).attribute("onClick", "control('" + id + "','type=main&action=toggle')"),
                                new Div("control_button control_button_big").child(
                                        new Div("").attribute("id", "secondary").content(locale.chrono.controlStart),
                                        new Div("").content(locale.chrono.controlSecondary)
                                ).attribute("onClick", "control('" + id + "','type=secondary&action=toggle')")
                        ),
                        new Div("control_block").child(
                                new Div("control_button").content(locale.chrono.controlReset).
                                attribute("onClick", "controlReset('" + id + "')"),
                                new Input().attribute("id", "reset").attribute("class", "control_button control_text")
                        ),
                        new Div("control_block").child(
                                new Div("control_button").content(locale.chrono.controlNext).
                                attribute("onClick", "control('" + id + "','type=stance&action=next')"),
                                new Div("control_button").content(locale.chrono.controlSwap).
                                attribute("onClick", "control('" + id + "','type=sides&action=swap')")
                        ),
                        new Div("control_block").child(
                                stances
                        ),
                        new Script().content("controlTags(\"" + id + "\"); ")
                )
        ).toHTML();
    }
}
