package org.niohiki.debateserver;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.niohiki.debateserver.handlers.StaticHandler;
import org.niohiki.debateserver.servlets.ChronoServlet;
import org.xml.sax.SAXException;

public class MainFrame extends javax.swing.JFrame {

    private final Locale locale;
    private final Configuration configuration;
    private final Session session;

    private Server server;
    private HashSessionIdManager idManager;
    private StaticHandler staticHandler;
    private ServletContextHandler servletHandler;

    // <editor-fold defaultstate="collapsed" desc="constructor">
    public MainFrame() throws Exception {
        locale = loadLocale();
        configuration = loadConfiguration();
        session = loadSession();
        initComponents();
        setupIPArea();
        setupServer();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setup tabs">
    private void setupIPArea() throws SocketException {
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        StringBuilder result = new StringBuilder("");
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            Enumeration<InetAddress> as = ni.getInetAddresses();
            StringBuilder ips = new StringBuilder("");
            int ip_count = 0;
            while (as.hasMoreElements()) {
                String address = as.nextElement().getHostAddress();
                if (address.split("\\.").length == 4) {
                    ips.append("\n\t").append(address);
                    ip_count++;
                }
            }
            if (ip_count > 0) {
                result.append(ni.getDisplayName()).append(ips).append("\n");
            }
        }
        result.append("\n").append(locale.ipInfo.firewallInfo);
        ipTextArea.setText(result.toString());
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="load xml methods">
    private Locale loadLocale() throws
            ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return new Locale(dBuilder.parse(new File(Names.localeFile)));
    }

    private Configuration loadConfiguration() throws
            ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return new Configuration(dBuilder.parse(new File(Names.configurationFile)));
    }

    private Session loadSession() throws
            ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return new Session(dBuilder.parse(new File(Names.sessionFile)));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="server setup">
    private void setupServer() throws Exception {
        staticHandler = new StaticHandler();

        servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletHandler.setContextPath("/");
        servletHandler.addServlet(new ServletHolder(
                new ChronoServlet(configuration, locale)),
                Names.chronoContext);

        HandlerList handlers = new HandlerList();
        handlers.addHandler(staticHandler);
        handlers.addHandler(servletHandler);

        server = new Server(80);
        server.setSessionIdManager(idManager);
        server.setHandler(handlers);
        server.start();
    }

    // </editor-fold>
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        ipInfoPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ipTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(locale.app.title);

        tabbedPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText(locale.ipInfo.ipLabel);

        ipTextArea.setEditable(false);
        ipTextArea.setBackground(new java.awt.Color(240, 240, 240));
        ipTextArea.setColumns(20);
        ipTextArea.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ipTextArea.setRows(5);
        jScrollPane1.setViewportView(ipTextArea);

        javax.swing.GroupLayout ipInfoPanelLayout = new javax.swing.GroupLayout(ipInfoPanel);
        ipInfoPanel.setLayout(ipInfoPanelLayout);
        ipInfoPanelLayout.setHorizontalGroup(
            ipInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ipInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ipInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                    .addGroup(ipInfoPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ipInfoPanelLayout.setVerticalGroup(
            ipInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ipInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(locale.ipInfo.tabTitle, ipInfoPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ipInfoPanel;
    private javax.swing.JTextArea ipTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
