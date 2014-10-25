package org.niohiki.debateserver.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.niohiki.debateserver.html.Body;
import org.niohiki.debateserver.html.CSSLink;
import org.niohiki.debateserver.html.Div;
import org.niohiki.debateserver.html.Form;
import org.niohiki.debateserver.html.HTML;
import org.niohiki.debateserver.html.Head;
import org.niohiki.debateserver.html.Input;

public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(makeHTML());
    }

    private String makeHTML() {
        return new HTML().child(
                new Head().child(new CSSLink("/static/main.css")),
                new Body().child(
                        new Div("main_item").child(
                                new Div("main_text").content("Ccc").attribute("onClick", "location.href='/chrono'")
                        )
                )
        ).toHTML();
    }

}
