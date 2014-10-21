package org.niohiki.debateserver;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Locale {

    public final IpInfo ipInfo;
    public final ChronoInfo chronoInfo;
    public final App app;
    public final Chrono chrono;

    public Locale(Document doc) {
        Element root = (Element) doc.getElementsByTagName("locale").item(0);
        ipInfo = new IpInfo((Element) root.getElementsByTagName("ipinfo").item(0));
        chronoInfo = new ChronoInfo((Element) root.getElementsByTagName("chronoinfo").item(0));
        app = new App((Element) root.getElementsByTagName("app").item(0));
        chrono = new Chrono((Element) root.getElementsByTagName("chrono").item(0));
    }

    public class Chrono {

        public final String newChrono;
        public final String newFormTitle;
        public final String watch;
        public final String control;
        public final String newSubmit;
        
        public final String controlMain;
        public final String controlSecondary;
        public final String controlRun;
        public final String controlPause;
        public final String controlStart;
        public final String controlStop;
        public final String controlReset;
        public final String controlSwap;
        public final String controlNext;
        

        public Chrono(Element e) {
            newChrono = e.getElementsByTagName("newchrono").item(0).getTextContent();
            newSubmit = e.getElementsByTagName("newsubmit").item(0).getTextContent();
            newFormTitle = e.getElementsByTagName("newformtitle").item(0).getTextContent();
            watch = e.getElementsByTagName("watch").item(0).getTextContent();
            control = e.getElementsByTagName("control").item(0).getTextContent();
            controlMain = e.getElementsByTagName("controlmain").item(0).getTextContent();
            controlSecondary = e.getElementsByTagName("controlsecondary").item(0).getTextContent();
            controlRun = e.getElementsByTagName("controlrun").item(0).getTextContent();
            controlPause = e.getElementsByTagName("controlpause").item(0).getTextContent();
            controlStart = e.getElementsByTagName("controlstart").item(0).getTextContent();
            controlStop = e.getElementsByTagName("controlstop").item(0).getTextContent();
            controlReset = e.getElementsByTagName("controlreset").item(0).getTextContent();
            controlSwap = e.getElementsByTagName("controlswap").item(0).getTextContent();
            controlNext = e.getElementsByTagName("controlnext").item(0).getTextContent();
        }
    }

    public class App {

        public final String title;
        public final String passwordInputScore;
        public final String passwordInputChrono;
        public final String okButton;

        public App(Element e) {
            title = e.getElementsByTagName("title").item(0).getTextContent();
            passwordInputScore = e.getElementsByTagName("passwordinputscore").item(0).getTextContent();
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

    public class ChronoInfo {

        public final String tabTitle;
        public final String chronoName;
        public final String chronoID;
        public final String mainRunning;
        public final String secondaryRunning;
        public final String stance;
        public final String toggleMain;
        public final String toggleSecondary;
        public final String delete;

        public ChronoInfo(Element e) {
            tabTitle = e.getElementsByTagName("tabtitle").item(0).getTextContent();
            chronoName = e.getElementsByTagName("chrononame").item(0).getTextContent();
            chronoID = e.getElementsByTagName("chronoid").item(0).getTextContent();
            mainRunning = e.getElementsByTagName("mainrunning").item(0).getTextContent();
            secondaryRunning = e.getElementsByTagName("secondaryrunning").item(0).getTextContent();
            stance = e.getElementsByTagName("stance").item(0).getTextContent();
            toggleMain = e.getElementsByTagName("togglemain").item(0).getTextContent();
            toggleSecondary = e.getElementsByTagName("togglesecondary").item(0).getTextContent();
            delete = e.getElementsByTagName("delete").item(0).getTextContent();
        }
    }
}
