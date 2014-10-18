package org.niohiki.debateserver;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Locale {

    public final IpInfo ipInfo;
    public final App app;
    public final Chrono chrono;

    public Locale(Document doc) {
        Element root = (Element) doc.getElementsByTagName("locale").item(0);
        ipInfo = new IpInfo((Element) root.getElementsByTagName("ipinfo").item(0));
        app = new App((Element) root.getElementsByTagName("app").item(0));
        chrono = new Chrono((Element) root.getElementsByTagName("chrono").item(0));
    }

    public class Chrono {

        public final String newChrono;
        public final String see;
        public final String control;

        public Chrono(Element e) {
            newChrono = e.getElementsByTagName("newchrono").item(0).getTextContent();
            see = e.getElementsByTagName("see").item(0).getTextContent();
            control = e.getElementsByTagName("control").item(0).getTextContent();
        }
    }

    public class App {

        public final String title;
        public final String passwordInput;
        public final String passwordInputChrono;
        public final String okButton;

        public App(Element e) {
            title = e.getElementsByTagName("title").item(0).getTextContent();
            passwordInput = e.getElementsByTagName("passwordinput").item(0).getTextContent();
            passwordInputChrono = e.getElementsByTagName("passwordinputchrono").item(0).getTextContent();
            okButton = e.getElementsByTagName("okbutton").item(0).getTextContent();
        }
    }

    public class IpInfo {

        public final String tabTitle;
        public final String firewallInfo;
        public final String ipLabel;

        public IpInfo(Element e) {
            tabTitle = e.getElementsByTagName("tabtitle").item(0).getTextContent();
            firewallInfo = e.getElementsByTagName("firewallinfo").item(0).getTextContent();
            ipLabel = e.getElementsByTagName("iplabel").item(0).getTextContent();
        }
    }
}
