package org.niohiki.debateserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
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
import org.niohiki.debateserver.chronometer.Chronometer;
import org.niohiki.debateserver.handlers.StaticHandler;
import org.niohiki.debateserver.servlets.ChronoServlet;
import org.niohiki.debateserver.servlets.MainServlet;
import org.niohiki.debateserver.swing.ChronoTableModel;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Santiago Codesido Sanchez
 *
 */
public class MainFrame extends javax.swing.JFrame {

    public final Locale locale;
    public final Options options;
    public final DebateSession session;
    public final Configuration configuration;

    public final String passwordScoreMD5;
    public final String passwordChronoMD5;

    private Server server;
    private HashSessionIdManager idManager;
    private StaticHandler staticHandler;
    private ServletContextHandler servletHandler;

    private final HashMap<String, Chronometer> chronometers;
    private final ChronoTableModel chronoTableModel;

    // <editor-fold defaultstate="collapsed" desc="constructor">
    public MainFrame() throws Exception {

        configuration = new Configuration(new File(Utils.configurationDir + Utils.configurationFile));
        locale = loadLocale();
        options = loadOptions();
        session = loadSession();

        chronometers = new HashMap<>();
        chronoTableModel = new ChronoTableModel(locale, chronometers);

        PasswordDialog pd = new PasswordDialog(this);
        pd.setVisible(true);
        passwordScoreMD5 = pd.getPasswordScoreMD5();
        passwordChronoMD5 = pd.getPasswordChronoMD5();

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
        return new Locale(dBuilder.parse(new FileInputStream(new File(configuration.localeFile))));
    }

    private Options loadOptions() throws
            ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return new Options(dBuilder.parse(new FileInputStream(new File(configuration.optionsFile))));
    }

    private DebateSession loadSession() throws
            ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return new DebateSession(dBuilder.parse(new FileInputStream(new File(configuration.sessionFile))));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="server setup">
    private void setupServer() throws Exception {
        staticHandler = new StaticHandler();

        servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletHandler.setContextPath("/");
        servletHandler.addServlet(new ServletHolder(
                new ChronoServlet(options, session, locale,
                        chronometers, passwordChronoMD5)),
                Utils.chronoContext);
        servletHandler.addServlet(new ServletHolder(
                new MainServlet(locale)), "/");

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
        chronoPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        chronoTable = new javax.swing.JTable();
        toggleMain = new javax.swing.JButton();
        toggleSecondary = new javax.swing.JButton();
        deleteChrono = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(locale.app.title);

        tabbedPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabChanged(evt);
            }
        });

        jLabel1.setText(locale.ipInfo.ipLabel);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(locale.ipInfo.tabTitle, ipInfoPanel);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        chronoTable.setModel(chronoTableModel);
        chronoTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                chronoTableMouseMoved(evt);
            }
        });
        jScrollPane2.setViewportView(chronoTable);

        toggleMain.setText(locale.chronoInfo.toggleMain);
        toggleMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleMainActionPerformed(evt);
            }
        });

        toggleSecondary.setText(locale.chronoInfo.toggleSecondary);
        toggleSecondary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSecondaryActionPerformed(evt);
            }
        });

        deleteChrono.setText(locale.chronoInfo.delete);
        deleteChrono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteChronoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout chronoPanelLayout = new javax.swing.GroupLayout(chronoPanel);
        chronoPanel.setLayout(chronoPanelLayout);
        chronoPanelLayout.setHorizontalGroup(
            chronoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chronoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chronoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                    .addGroup(chronoPanelLayout.createSequentialGroup()
                        .addComponent(toggleMain)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toggleSecondary)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteChrono)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        chronoPanelLayout.setVerticalGroup(
            chronoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chronoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(chronoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toggleMain)
                    .addComponent(toggleSecondary)
                    .addComponent(deleteChrono))
                .addContainerGap())
        );

        tabbedPane.addTab(locale.chronoInfo.tabTitle, chronoPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabChanged
        chronoTableModel.fireTableDataChanged();
    }//GEN-LAST:event_tabChanged

    private void toggleMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleMainActionPerformed
        try {
            int selected = chronoTable.getSelectedRow();
            String key = (String) chronoTableModel.getValueAt(selected, 1);
            Chronometer chrono = chronometers.get(key);
            if (chrono.isMainRunning()) {
                chrono.mainReset();
            } else {
                chrono.mainRun();
            }
            chronoTableModel.fireTableDataChanged();
            chronoTable.setRowSelectionInterval(selected, selected);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_toggleMainActionPerformed

    private void toggleSecondaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleSecondaryActionPerformed
        try {
            int selected = chronoTable.getSelectedRow();
            String key = (String) chronoTableModel.getValueAt(selected, 1);
            Chronometer chrono = chronometers.get(key);
            if (chrono.isSecondaryRunning()) {
                chrono.secondaryStop();
            } else {
                chrono.secondaryStart();
            }
            chronoTableModel.fireTableDataChanged();
            chronoTable.setRowSelectionInterval(selected, selected);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_toggleSecondaryActionPerformed

    private void chronoTableMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chronoTableMouseMoved

    }//GEN-LAST:event_chronoTableMouseMoved

    private void deleteChronoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteChronoActionPerformed
        try {
            String key = (String) chronoTableModel.getValueAt(chronoTable.getSelectedRow(), 1);
            Chronometer chrono = chronometers.get(key);
            chrono.kill();
            chronometers.remove(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        chronoTableModel.fireTableDataChanged();
    }//GEN-LAST:event_deleteChronoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chronoPanel;
    private javax.swing.JTable chronoTable;
    private javax.swing.JButton deleteChrono;
    private javax.swing.JPanel ipInfoPanel;
    private javax.swing.JTextArea ipTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JButton toggleMain;
    private javax.swing.JButton toggleSecondary;
    // End of variables declaration//GEN-END:variables
}
