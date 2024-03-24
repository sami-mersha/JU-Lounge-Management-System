package ju.loungemanagement;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Home extends javax.swing.JFrame {

    int day, month, year;
    double todaysPayment;

    public Home() {
        initComponents();
        setColor(homeSubPanel);
    }

    public void setDateNow() {
        String date = java.time.LocalDate.now().toString();
        String[] dateParts = date.split("-");
        day = Integer.parseInt(dateParts[2]);
        month = Integer.parseInt(dateParts[1]);
        year = Integer.parseInt(dateParts[0]);

    }

    public void calculateTotalDailyPayed() {
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();

            String command = "SELECT * FROM JULOUNGE.HISTORY";

            ResultSet rset = st.executeQuery(command);

            double totalDailyPayed = 0;
            setDateNow();
            while (rset.next()) {
                if (rset.getInt("DATE") == day && rset.getString("EVENT").contains("[Payed_Credit]")) {
                    String event = rset.getString("EVENT");
                    int indexOfBirr = event.indexOf("birr");
                    int indexOfAdded = event.indexOf("payed");

                    totalDailyPayed += Double.parseDouble(event.substring(indexOfAdded + 6, indexOfBirr - 1));
                }
            }

            todaysPayment = totalDailyPayed;
            totalDailyPayedLabel.setText(totalDailyPayed + "");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e);
        }
    }

    public void calculateTotalDailyCredit() {
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();

            String command = "SELECT * FROM JULOUNGE.HISTORY";

            ResultSet rset = st.executeQuery(command);

            double totalDailyCredit = 0;
            setDateNow();
            while (rset.next()) {
                if (rset.getInt("DATE") == day && rset.getString("EVENT").contains("[Added_Credit]")) {
                    String event = rset.getString("EVENT");
                    int indexOfBirr = event.indexOf("birr");
                    int indexOfAdded = event.indexOf("added");

                    totalDailyCredit += Double.parseDouble(event.substring(indexOfAdded + 6, indexOfBirr - 1));

                }

            }
            setDateNow();
            totalDailyCredit -= todaysPayment;
            totalDailyCredit = Math.round(totalDailyCredit * 100) / 100.0;
            totalDailyCreditLabel.setText(totalDailyCredit + "");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e);
        }
    }

    public void calculateTotalCredit() {
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();

            String command = "SELECT * FROM JULOUNGE.CUSTOMER";

            ResultSet rset = st.executeQuery(command);

            double totalCredit = 0;
            while (rset.next()) {
                totalCredit += Double.parseDouble(rset.getString("CREDIT"));
            }

            totalCredit = Math.round(totalCredit * 100) / 100.0;
            totalCreditLabel.setText(totalCredit + "");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e);
        }
    }

    public void addToRemovedHistory(String fullName, String id) {
        String event = "Customers account with name <" + fullName + "> and id:<" + id + "> has been removed from the list!";

        try {
            Connection con = getConnection();
            Statement st = con.createStatement();
            setDateNow();
            String cmd = "INSERT INTO JULOUNGE.HISTORY VALUES(" + day + "," + month + ", " + year + ", '" + event + "')";
            st.executeUpdate(cmd);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addToPaymentHistory(String fullName, String payment, String existingCredit, String totalCredit) {
        String event = "[Payed_Credit] " + fullName + " payed " + payment + " birr credit.";
        PrintInfo pf = new PrintInfo();
        pf.fillandPrintPayment(fullName, payment, existingCredit, totalCredit);

        try {
            Connection con = getConnection();
            Statement st = con.createStatement();
            setDateNow();
            String cmd = "INSERT INTO JULOUNGE.HISTORY VALUES(" + day + "," + month + ", " + year + ", '" + event + "')";
            st.executeUpdate(cmd);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addToCreditHistory(String fullName, String credit, String totalCredit) {
        String event = "[Added_Credit] " + fullName + " added " + credit + " birr credit.";
        PrintInfo pf = new PrintInfo();
        pf.fillandPrintCredit(fullName, credit, totalCredit);
        try {
            Connection con = getConnection();
            Statement st = con.createStatement();
            setDateNow();
            String cmd = "INSERT INTO JULOUNGE.HISTORY VALUES(" + day + "," + month + ", " + year + ", '" + event + "')";
            st.executeUpdate(cmd);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void setColor(JPanel jp) {
        jp.setBackground(Color.cyan);
    }

    private void resetColor(JPanel jp) {
        jp.setBackground(Color.white);
    }

    public void payCredit(String id) {
        boolean notInformed = true;
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();

            //finding the amount of credit that already exists
            String cmd = "SELECT * FROM JULOUNGE.CUSTOMER";
            ResultSet rset = st.executeQuery(cmd);

            String existingCredit = null, fullName = null;
            while (rset.next()) {
                if (id.equals(rset.getString("ID"))) {
                    existingCredit = rset.getString("CREDIT");
                    fullName = rset.getString("FULLNAME");
                }
            }

            //Substracting from existing credit
            String payment = JOptionPane.showInputDialog(new JFrame(), "Enter the amount in birr");
            double totalCredit = Double.parseDouble(existingCredit) - Double.parseDouble(payment);
            totalCredit = Math.round(totalCredit * 100) / 100.00;

            int decision = JOptionPane.showConfirmDialog(new JFrame(), "Existing Credit: " + existingCredit + "\nDo you want to make " + payment + " birr payment to " + fullName + "'s Account?", "Confirm to pay credit", JOptionPane.OK_CANCEL_OPTION);
            //decision = 0 means ok

            int a = -1;
            if (decision == 0) {
                cmd = "UPDATE JULOUNGE.CUSTOMER SET CREDIT = " + totalCredit + " WHERE ID = '" + id + "'";
                a = st.executeUpdate(cmd);
            } else {
                notInformed = false;
            }

            if (a > 0) {
                String message = "Old Credit: " + existingCredit + "\nPayment: " + payment + "\nTotal Credit left: " + totalCredit;
                addToPaymentHistory(fullName, payment, existingCredit, totalCredit + "");
                JOptionPane.showMessageDialog(new JFrame(), "Successfully Payed!");
                JOptionPane.showMessageDialog(new JFrame(), message);
            } else if (notInformed) {
                JOptionPane.showMessageDialog(new JFrame(), "failed to update the Credit!\nPlease try again!");
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void addCredit(String id) {
        boolean notInformed = true;
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();

            //finding the amount of credit that already exists
            String cmd = "SELECT * FROM JULOUNGE.CUSTOMER";
            ResultSet rset = st.executeQuery(cmd);

            String existingCredit = null, fullName = null;
            while (rset.next()) {
                if (id.equals(rset.getString("ID"))) {
                    existingCredit = rset.getString("CREDIT");
                    fullName = rset.getString("FULLNAME");
                }
            }

            //Adding to credit
            String newCredit = JOptionPane.showInputDialog(new JFrame(), "Enter the amount in birr");
            double totalCredit = Double.parseDouble(newCredit) + Double.parseDouble(existingCredit);

            int decision = JOptionPane.showConfirmDialog(new JFrame(), "Existing Credit: " + existingCredit + "\nDo you want to add " + newCredit + " birr to " + fullName + "'s Account?", "Confirm to add credit", JOptionPane.OK_CANCEL_OPTION);
            //decision = 0 means ok

            int a = -1;
            if (decision == 0) {
                cmd = "UPDATE JULOUNGE.CUSTOMER SET CREDIT = " + totalCredit + " WHERE ID = '" + id + "'";
                a = st.executeUpdate(cmd);
            } else {
                notInformed = false;
            }

            if (a > 0) {
                addToCreditHistory(fullName, newCredit, totalCredit + "");
                JOptionPane.showMessageDialog(new JFrame(), "Successfully Added to Credit!");
            } else if (notInformed) {
                JOptionPane.showMessageDialog(new JFrame(), "failed to add the Credit!\nPlease try again!");
            }

        } catch (Exception e) {
            if (notInformed) {
                JOptionPane.showMessageDialog(new JFrame(), e);
            }
        }

    }

    public void removeCustomer(String id) {

        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();
            String command = "SELECT * FROM JULOUNGE.CUSTOMER";

            ResultSet set = st.executeQuery(command);

            String fullName = null, gender = null;
            double credit = 0.0;
            while (set.next()) {
                if (id.equals(set.getString("ID"))) {
                    fullName = set.getString("FULLNAME");
                    credit = set.getDouble("CREDIT");
                    gender = set.getString("SEX");
                    break;
                }
            }

            if (credit != 0.00) {
                if (gender.equals("Male")) {
                    JOptionPane.showMessageDialog(new JFrame(), "The customer should have to cover his " + credit + " birr credit before deleting his account!");
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "The customer should have to cover her " + credit + " birr credit before deleting her account!");
                }
            } else {
                int response = JOptionPane.showConfirmDialog(new JFrame(), "Do you want to delete " + fullName + "'s Data?", "Confirm to Delete", JOptionPane.YES_NO_CANCEL_OPTION);
                //0 means yes 1 means no 
                if (response == 0) {
                    command = "DELETE FROM JULOUNGE.CUSTOMER WHERE ID = '" + id + "'";
                    st.executeUpdate(command);
                    addToRemovedHistory(fullName, id);
                    JOptionPane.showMessageDialog(new JFrame(), "Successfully deleted!");
                } else if (response == 1) {
                    JOptionPane.showMessageDialog(new JFrame(), "The data is not deleted!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public boolean checkValidity(String usrnm, String pwd) {
        boolean valid = false, userNotFound = true;
        JFrame jf = new JFrame();

        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();

            String cmd = "SELECT * FROM JULOUNGE.DATA";
            ResultSet set = st.executeQuery(cmd);

            while (set.next()) {
                if (usrnm.equals(set.getString("USERNAME")) && pwd.equals(set.getString("PASSWORD"))) {
                    valid = true;
                    userNotFound = false;
                    break;
                } else if (usrnm.equals(set.getString("USERNAME"))) {
                    JOptionPane.showMessageDialog(jf, "Incorrect Password!\nPlease try again!", "Login Error", JOptionPane.ERROR_MESSAGE);
                    userNotFound = false;
                }
            }

            if (userNotFound) {
                JOptionPane.showMessageDialog(jf, "Username not found!\nPlease try again!", "Login Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(jf, e);
        }

        return valid;
    }

    public boolean findUsername(String username) {
        boolean found = false;
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();
            String cmd = "SELECT * FROM JULOUNGE.DATA";
            ResultSet set = st.executeQuery(cmd);

            while (set.next()) {
                if (set.getString("USERNAME").equals(username)) {
                    found = true;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return found;
    }

    public boolean findId(String id) {
        boolean found = false;
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();
            String cmd = "SELECT * FROM JULOUNGE.CUSTOMER";
            ResultSet set = st.executeQuery(cmd);

            while (set.next()) {
                if (set.getString("ID").equals(id)) {
                    found = true;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return found;
    }

    public static Connection getConnection() throws Exception {
        Connection con = null;

        try {
            String driver = "org.apache.derby.jdbc.ClientDriver";
            String url = "jdbc:derby://localhost:1527/Lounge";
            String username = "julounge";
            String password = "lounge@ju";

            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }

        return con;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        leftSidePanel = new javax.swing.JPanel();
        topLeftPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        homeSubPanel = new javax.swing.JPanel();
        homeLabel = new javax.swing.JLabel();
        creditsSubPanel = new javax.swing.JPanel();
        creditsLabel = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        customersSubPanel = new javax.swing.JPanel();
        customersLabel = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        accountsSubPanel = new javax.swing.JPanel();
        accountsLabel = new javax.swing.JLabel();
        jSeparator15 = new javax.swing.JSeparator();
        aboutSubPanel = new javax.swing.JPanel();
        aboutLabel = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        rightSidePanel = new javax.swing.JPanel();
        homePanel = new javax.swing.JPanel();
        homeTitleLabel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        addCreditPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        addNewCustomerPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        payCreditPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        activityHistoryPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        customersPanel = new javax.swing.JPanel();
        customersTitleLabel1 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        displayCustomerPanel = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        searchCustomerPanel = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        modifyCustomersPanel = new javax.swing.JPanel();
        modifyCustomerPanel = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        removeCustomerPanel = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        creditsPanel = new javax.swing.JPanel();
        creditsTitleLabel = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        totalCreditLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        totalDailyCreditLabel = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        totalDailyPayedLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        accountsPanel = new javax.swing.JPanel();
        accountsTitleLabel = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        displayAccountsPanel = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        searchAccountsPanel = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        modifyAccountsPanel = new javax.swing.JPanel();
        modifyCustomerPanel1 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        removeAccountsPanel = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        aboutPanel = new javax.swing.JPanel();
        creditsTitleLabel1 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel48 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        leftSidePanel.setBackground(new java.awt.Color(255, 255, 255));
        leftSidePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        topLeftPanel.setBackground(new java.awt.Color(102, 153, 255));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("LOUNGE MANAGEMENT SYSTEM");

        javax.swing.GroupLayout topLeftPanelLayout = new javax.swing.GroupLayout(topLeftPanel);
        topLeftPanel.setLayout(topLeftPanelLayout);
        topLeftPanelLayout.setHorizontalGroup(
            topLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topLeftPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        topLeftPanelLayout.setVerticalGroup(
            topLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topLeftPanelLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jLabel1)
                .addContainerGap(69, Short.MAX_VALUE))
        );

        leftSidePanel.add(topLeftPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 160));
        leftSidePanel.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 510, 320, 10));
        leftSidePanel.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 320, 10));

        homeSubPanel.setBackground(new java.awt.Color(255, 255, 255));
        homeSubPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                homeSubPanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                homeSubPanelMousePressed(evt);
            }
        });

        homeLabel.setBackground(new java.awt.Color(255, 255, 255));
        homeLabel.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        homeLabel.setForeground(new java.awt.Color(0, 51, 102));
        homeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/home_40px.png"))); // NOI18N
        homeLabel.setText("Home");
        homeLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout homeSubPanelLayout = new javax.swing.GroupLayout(homeSubPanel);
        homeSubPanel.setLayout(homeSubPanelLayout);
        homeSubPanelLayout.setHorizontalGroup(
            homeSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeSubPanelLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(homeLabel)
                .addContainerGap(115, Short.MAX_VALUE))
        );
        homeSubPanelLayout.setVerticalGroup(
            homeSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeSubPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(homeLabel)
                .addContainerGap())
        );

        leftSidePanel.add(homeSubPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 320, 70));

        creditsSubPanel.setBackground(new java.awt.Color(255, 255, 255));
        creditsSubPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                creditsSubPanelMousePressed(evt);
            }
        });

        creditsLabel.setBackground(new java.awt.Color(255, 255, 255));
        creditsLabel.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        creditsLabel.setForeground(new java.awt.Color(0, 51, 102));
        creditsLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/money_32px.png"))); // NOI18N
        creditsLabel.setText("Credits");
        creditsLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        creditsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                creditsLabelMousePressed(evt);
            }
        });

        javax.swing.GroupLayout creditsSubPanelLayout = new javax.swing.GroupLayout(creditsSubPanel);
        creditsSubPanel.setLayout(creditsSubPanelLayout);
        creditsSubPanelLayout.setHorizontalGroup(
            creditsSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(creditsSubPanelLayout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(creditsLabel)
                .addContainerGap(113, Short.MAX_VALUE))
            .addComponent(jSeparator14)
        );
        creditsSubPanelLayout.setVerticalGroup(
            creditsSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(creditsSubPanelLayout.createSequentialGroup()
                .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(creditsLabel)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        leftSidePanel.add(creditsSubPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 300, 320, 70));

        customersSubPanel.setBackground(new java.awt.Color(255, 255, 255));
        customersSubPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                customersSubPanelMousePressed(evt);
            }
        });

        customersLabel.setBackground(new java.awt.Color(255, 255, 255));
        customersLabel.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        customersLabel.setForeground(new java.awt.Color(0, 51, 102));
        customersLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user_40px.png"))); // NOI18N
        customersLabel.setText("Customers");
        customersLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        customersLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customersLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout customersSubPanelLayout = new javax.swing.GroupLayout(customersSubPanel);
        customersSubPanel.setLayout(customersSubPanelLayout);
        customersSubPanelLayout.setHorizontalGroup(
            customersSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customersSubPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(customersLabel)
                .addContainerGap(98, Short.MAX_VALUE))
            .addComponent(jSeparator13)
        );
        customersSubPanelLayout.setVerticalGroup(
            customersSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customersSubPanelLayout.createSequentialGroup()
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(customersLabel)
                .addContainerGap())
        );

        leftSidePanel.add(customersSubPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 320, 70));

        accountsSubPanel.setBackground(new java.awt.Color(255, 255, 255));
        accountsSubPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                accountsSubPanelMousePressed(evt);
            }
        });

        accountsLabel.setBackground(new java.awt.Color(255, 255, 255));
        accountsLabel.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        accountsLabel.setForeground(new java.awt.Color(0, 51, 102));
        accountsLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user_shield_40px.png"))); // NOI18N
        accountsLabel.setText("Accounts");
        accountsLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        accountsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                accountsLabelMousePressed(evt);
            }
        });

        javax.swing.GroupLayout accountsSubPanelLayout = new javax.swing.GroupLayout(accountsSubPanel);
        accountsSubPanel.setLayout(accountsSubPanelLayout);
        accountsSubPanelLayout.setHorizontalGroup(
            accountsSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountsSubPanelLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(accountsLabel)
                .addContainerGap(100, Short.MAX_VALUE))
            .addComponent(jSeparator15)
        );
        accountsSubPanelLayout.setVerticalGroup(
            accountsSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accountsSubPanelLayout.createSequentialGroup()
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(accountsLabel)
                .addContainerGap())
        );

        leftSidePanel.add(accountsSubPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 320, 70));

        aboutSubPanel.setBackground(new java.awt.Color(255, 255, 255));
        aboutSubPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                aboutSubPanelMousePressed(evt);
            }
        });

        aboutLabel.setBackground(new java.awt.Color(255, 255, 255));
        aboutLabel.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        aboutLabel.setForeground(new java.awt.Color(0, 51, 102));
        aboutLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/info_40px.png"))); // NOI18N
        aboutLabel.setText("About");
        aboutLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        aboutLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                aboutLabelMousePressed(evt);
            }
        });

        javax.swing.GroupLayout aboutSubPanelLayout = new javax.swing.GroupLayout(aboutSubPanel);
        aboutSubPanel.setLayout(aboutSubPanelLayout);
        aboutSubPanelLayout.setHorizontalGroup(
            aboutSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aboutSubPanelLayout.createSequentialGroup()
                .addContainerGap(112, Short.MAX_VALUE)
                .addComponent(aboutLabel)
                .addGap(105, 105, 105))
            .addComponent(jSeparator16)
        );
        aboutSubPanelLayout.setVerticalGroup(
            aboutSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aboutSubPanelLayout.createSequentialGroup()
                .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(aboutLabel)
                .addContainerGap())
        );

        leftSidePanel.add(aboutSubPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 320, 70));

        getContentPane().add(leftSidePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 600));

        homePanel.setBackground(new java.awt.Color(255, 204, 153));
        homePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        homeTitleLabel.setBackground(new java.awt.Color(255, 102, 0));
        homeTitleLabel.setForeground(new java.awt.Color(255, 102, 0));

        jLabel10.setFont(new java.awt.Font("Forte", 0, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Home");

        javax.swing.GroupLayout homeTitleLabelLayout = new javax.swing.GroupLayout(homeTitleLabel);
        homeTitleLabel.setLayout(homeTitleLabelLayout);
        homeTitleLabelLayout.setHorizontalGroup(
            homeTitleLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeTitleLabelLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel10)
                .addContainerGap(88, Short.MAX_VALUE))
        );
        homeTitleLabelLayout.setVerticalGroup(
            homeTitleLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeTitleLabelLayout.createSequentialGroup()
                .addComponent(jLabel10)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        homePanel.add(homeTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 220, 50));

        addCreditPanel.setBackground(new java.awt.Color(255, 255, 255));
        addCreditPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addCreditPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addCreditPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                addCreditPanelMousePressed(evt);
            }
        });
        addCreditPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 0, 51));
        jLabel11.setText("Add Credit");
        addCreditPanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, -1, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/expensive_70px.png"))); // NOI18N
        addCreditPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        homePanel.add(addCreditPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 260, 180));

        addNewCustomerPanel.setBackground(new java.awt.Color(255, 255, 255));
        addNewCustomerPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addNewCustomerPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addNewCustomerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                addNewCustomerPanelMousePressed(evt);
            }
        });
        addNewCustomerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 0, 51));
        jLabel13.setText("Add New Customer");
        addNewCustomerPanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/plus_70px.png"))); // NOI18N
        addNewCustomerPanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        homePanel.add(addNewCustomerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 350, 260, 180));

        payCreditPanel.setBackground(new java.awt.Color(255, 255, 255));
        payCreditPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        payCreditPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        payCreditPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                payCreditPanelMousePressed(evt);
            }
        });
        payCreditPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 0, 51));
        jLabel15.setText("Pay Credit");
        payCreditPanel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, -1, -1));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/payment_history_70px.png"))); // NOI18N
        payCreditPanel.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        homePanel.add(payCreditPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 130, 260, 180));

        activityHistoryPanel.setBackground(new java.awt.Color(255, 255, 255));
        activityHistoryPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        activityHistoryPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        activityHistoryPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                activityHistoryPanelMousePressed(evt);
            }
        });
        activityHistoryPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 0, 51));
        jLabel17.setText("Activity History");
        activityHistoryPanel.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, -1, -1));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/activity_history_70px.png"))); // NOI18N
        activityHistoryPanel.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        homePanel.add(activityHistoryPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 350, 260, 180));

        customersPanel.setBackground(new java.awt.Color(153, 255, 204));
        customersPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        customersTitleLabel1.setBackground(new java.awt.Color(255, 102, 0));
        customersTitleLabel1.setForeground(new java.awt.Color(255, 102, 0));

        jLabel20.setFont(new java.awt.Font("Forte", 0, 36)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Customers");

        javax.swing.GroupLayout customersTitleLabel1Layout = new javax.swing.GroupLayout(customersTitleLabel1);
        customersTitleLabel1.setLayout(customersTitleLabel1Layout);
        customersTitleLabel1Layout.setHorizontalGroup(
            customersTitleLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customersTitleLabel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel20)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        customersTitleLabel1Layout.setVerticalGroup(
            customersTitleLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customersTitleLabel1Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        customersPanel.add(customersTitleLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 220, 50));

        displayCustomerPanel.setBackground(new java.awt.Color(255, 255, 255));
        displayCustomerPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        displayCustomerPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        displayCustomerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                displayCustomerPanelMousePressed(evt);
            }
        });
        displayCustomerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 0, 51));
        jLabel21.setText("Display Customers");
        displayCustomerPanel.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/customer_70px.png"))); // NOI18N
        displayCustomerPanel.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        customersPanel.add(displayCustomerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 260, 180));

        searchCustomerPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchCustomerPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        searchCustomerPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        searchCustomerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                searchCustomerPanelMousePressed(evt);
            }
        });
        searchCustomerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 0, 51));
        jLabel23.setText("Search Customer");
        searchCustomerPanel.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, -1, -1));

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search_70px.png"))); // NOI18N
        searchCustomerPanel.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        customersPanel.add(searchCustomerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 350, 260, 180));

        modifyCustomersPanel.setBackground(new java.awt.Color(255, 255, 255));
        modifyCustomersPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        modifyCustomersPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        modifyCustomersPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                modifyCustomersPanelMousePressed(evt);
            }
        });
        modifyCustomersPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        modifyCustomerPanel.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        modifyCustomerPanel.setForeground(new java.awt.Color(51, 0, 51));
        modifyCustomerPanel.setText("Modify Customer");
        modifyCustomersPanel.add(modifyCustomerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pencil_drawing_70px.png"))); // NOI18N
        modifyCustomersPanel.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        customersPanel.add(modifyCustomersPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 130, 260, 180));

        removeCustomerPanel.setBackground(new java.awt.Color(255, 255, 255));
        removeCustomerPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        removeCustomerPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        removeCustomerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                removeCustomerPanelMousePressed(evt);
            }
        });
        removeCustomerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 0, 51));
        jLabel27.setText("Remove Customer");
        removeCustomerPanel.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/remove_70px.png"))); // NOI18N
        removeCustomerPanel.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        customersPanel.add(removeCustomerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 350, 260, 180));

        creditsPanel.setBackground(new java.awt.Color(204, 204, 255));
        creditsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        creditsTitleLabel.setBackground(new java.awt.Color(255, 102, 0));
        creditsTitleLabel.setForeground(new java.awt.Color(255, 102, 0));

        jLabel25.setFont(new java.awt.Font("Forte", 0, 36)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Credits");

        javax.swing.GroupLayout creditsTitleLabelLayout = new javax.swing.GroupLayout(creditsTitleLabel);
        creditsTitleLabel.setLayout(creditsTitleLabelLayout);
        creditsTitleLabelLayout.setHorizontalGroup(
            creditsTitleLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(creditsTitleLabelLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel25)
                .addContainerGap(76, Short.MAX_VALUE))
        );
        creditsTitleLabelLayout.setVerticalGroup(
            creditsTitleLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, creditsTitleLabelLayout.createSequentialGroup()
                .addComponent(jLabel25)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        creditsPanel.add(creditsTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 220, 50));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Total Given Credit");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 19, 670, -1));
        jPanel1.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 215, 13));
        jPanel1.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, 215, 13));

        jPanel4.setBackground(new java.awt.Color(255, 204, 204));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalCreditLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalCreditLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalCreditLabel.setText("0.0");
        jPanel4.add(totalCreditLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 10, 290, -1));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 310, 40));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Birr");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 90, -1, -1));

        creditsPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 690, 140));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setText("Total Given Credit");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel29.setText("Total Payed Credit");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 283, Short.MAX_VALUE)
                .addComponent(jLabel29)
                .addGap(104, 104, 104))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 15, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel29)))
        );

        jPanel8.setBackground(new java.awt.Color(255, 204, 204));

        totalDailyCreditLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalDailyCreditLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalDailyCreditLabel.setText("0.0");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalDailyCreditLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totalDailyCreditLabel)
                .addContainerGap())
        );

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel31.setText("Birr");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel32.setText("Birr");

        jPanel9.setBackground(new java.awt.Color(204, 255, 204));

        totalDailyPayedLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalDailyPayedLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalDailyPayedLabel.setText("0.0");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalDailyPayedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totalDailyPayedLabel)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31))
                    .addComponent(jSeparator9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32))
                    .addComponent(jSeparator10))
                .addGap(28, 28, 28))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(jLabel31)
                        .addGap(33, 33, 33))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        creditsPanel.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 690, 140));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Book", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 153));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/today_50px.png"))); // NOI18N
        jLabel3.setText("Daily");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3))
        );

        creditsPanel.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 200, 50));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 153));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sigma_50px.png"))); // NOI18N
        jLabel2.setText("Generally");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(41, 41, 41))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2))
        );

        creditsPanel.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 290, 300, -1));

        accountsPanel.setBackground(new java.awt.Color(204, 204, 255));
        accountsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        accountsTitleLabel.setBackground(new java.awt.Color(255, 102, 0));
        accountsTitleLabel.setForeground(new java.awt.Color(255, 102, 0));

        jLabel34.setFont(new java.awt.Font("Forte", 0, 36)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Accounts");

        javax.swing.GroupLayout accountsTitleLabelLayout = new javax.swing.GroupLayout(accountsTitleLabel);
        accountsTitleLabel.setLayout(accountsTitleLabelLayout);
        accountsTitleLabelLayout.setHorizontalGroup(
            accountsTitleLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountsTitleLabelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel34)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        accountsTitleLabelLayout.setVerticalGroup(
            accountsTitleLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accountsTitleLabelLayout.createSequentialGroup()
                .addComponent(jLabel34)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        accountsPanel.add(accountsTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 220, 50));

        displayAccountsPanel.setBackground(new java.awt.Color(255, 255, 255));
        displayAccountsPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        displayAccountsPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        displayAccountsPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                displayAccountsPanelMousePressed(evt);
            }
        });
        displayAccountsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(51, 0, 51));
        jLabel35.setText("Display Admin Accounts");
        displayAccountsPanel.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user_shield_70px.png"))); // NOI18N
        displayAccountsPanel.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        accountsPanel.add(displayAccountsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 260, 180));

        searchAccountsPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchAccountsPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        searchAccountsPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        searchAccountsPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                searchAccountsPanelMousePressed(evt);
            }
        });
        searchAccountsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(51, 0, 51));
        jLabel37.setText("Search Account");
        searchAccountsPanel.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, -1, -1));

        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search_70px.png"))); // NOI18N
        searchAccountsPanel.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        accountsPanel.add(searchAccountsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 350, 260, 180));

        modifyAccountsPanel.setBackground(new java.awt.Color(255, 255, 255));
        modifyAccountsPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        modifyAccountsPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        modifyAccountsPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                modifyAccountsPanelMousePressed(evt);
            }
        });
        modifyAccountsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        modifyCustomerPanel1.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        modifyCustomerPanel1.setForeground(new java.awt.Color(51, 0, 51));
        modifyCustomerPanel1.setText("Modify Account");
        modifyAccountsPanel.add(modifyCustomerPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, -1, -1));

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pencil_drawing_70px.png"))); // NOI18N
        modifyAccountsPanel.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        accountsPanel.add(modifyAccountsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 130, 260, 180));

        removeAccountsPanel.setBackground(new java.awt.Color(255, 255, 255));
        removeAccountsPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        removeAccountsPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        removeAccountsPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                removeAccountsPanelMousePressed(evt);
            }
        });
        removeAccountsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel40.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(51, 0, 51));
        jLabel40.setText("Remove Account");
        removeAccountsPanel.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/remove_70px.png"))); // NOI18N
        removeAccountsPanel.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        accountsPanel.add(removeAccountsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 350, 260, 180));

        aboutPanel.setBackground(new java.awt.Color(102, 102, 102));
        aboutPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        creditsTitleLabel1.setBackground(new java.awt.Color(255, 102, 0));
        creditsTitleLabel1.setForeground(new java.awt.Color(255, 102, 0));

        jLabel42.setFont(new java.awt.Font("Forte", 0, 36)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("About");

        javax.swing.GroupLayout creditsTitleLabel1Layout = new javax.swing.GroupLayout(creditsTitleLabel1);
        creditsTitleLabel1.setLayout(creditsTitleLabel1Layout);
        creditsTitleLabel1Layout.setHorizontalGroup(
            creditsTitleLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(creditsTitleLabel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel42)
                .addContainerGap(71, Short.MAX_VALUE))
        );
        creditsTitleLabel1Layout.setVerticalGroup(
            creditsTitleLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, creditsTitleLabel1Layout.createSequentialGroup()
                .addComponent(jLabel42)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        aboutPanel.add(creditsTitleLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 220, 50));

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel45.setText("This application is designed and developed by Students of Electrical and Computer");

        jLabel46.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel46.setText("Engineering at Jimma Institute of Technology. Thank you for using our application!");

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel43.setText("Group Members");
        jPanel10.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        jLabel44.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel44.setText("Id Number");
        jPanel10.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, -1, -1));
        jPanel10.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 23, 155, -1));
        jPanel10.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 23, 145, -1));

        jLabel48.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel48.setText("Etsegenet Melkamu");
        jPanel10.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        jLabel47.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel47.setText("RU3192/11");
        jPanel10.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, -1, -1));

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel50.setText("RU3423/11");
        jPanel10.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, -1, -1));

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel51.setText("Biniam Alemayehu");
        jPanel10.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jLabel54.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel54.setText("Samuel Mersha");
        jPanel10.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel55.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel55.setText("RU3822/11");
        jPanel10.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, -1, -1));

        jLabel56.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel56.setText("Saladin Yusuf");
        jPanel10.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel57.setText("RU3698/11");
        jPanel10.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, -1, -1));

        jLabel58.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel58.setText("RU4139/11");
        jPanel10.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 110, -1, -1));

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel59.setText("Abdulwahab Mohammed");
        jPanel10.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel60.setText("Safahudin Gudeta");
        jPanel10.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        jLabel61.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel61.setText("RT0096/12");
        jPanel10.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, -1, -1));

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel62.setText("Kapital Tasisa");
        jPanel10.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel63.setText("RU3386/11");
        jPanel10.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, -1, -1));

        jLabel64.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel64.setText("RU3160/11");
        jPanel10.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 170, -1, -1));

        jLabel65.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel65.setText("Azmeraw Dawit");
        jPanel10.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        jLabel66.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel66.setText("Bethelihem Abebe");
        jPanel10.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jLabel67.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel67.setText("RU3998/11");
        jPanel10.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 190, -1, -1));

        jLabel68.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel68.setText("Behigu Meseret");
        jPanel10.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        jLabel69.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel69.setText("RU3158/11");
        jPanel10.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 210, -1, -1));

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel49.setText("Group: 4");

        jLabel52.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel52.setText("Sub Group: 10");

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel53.setText("Academic Year: 3rd");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel46)
                            .addComponent(jLabel45)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel49)
                            .addGap(45, 45, 45)
                            .addComponent(jLabel52)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel53))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGap(112, 112, 112)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel46)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(jLabel52)
                    .addComponent(jLabel53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        aboutPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 600, 400));

        javax.swing.GroupLayout rightSidePanelLayout = new javax.swing.GroupLayout(rightSidePanel);
        rightSidePanel.setLayout(rightSidePanelLayout);
        rightSidePanelLayout.setHorizontalGroup(
            rightSidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightSidePanelLayout.createSequentialGroup()
                .addGroup(rightSidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(homePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(creditsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accountsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(aboutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        rightSidePanelLayout.setVerticalGroup(
            rightSidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightSidePanelLayout.createSequentialGroup()
                .addComponent(homePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(creditsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aboutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(rightSidePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 0, 780, 600));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void customersLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customersLabelMouseClicked
        customersPanel.setVisible(true);
        homePanel.setVisible(false);
        creditsPanel.setVisible(false);
        accountsPanel.setVisible(false);
        aboutPanel.setVisible(false);
        resetColor(homeSubPanel);
        setColor(customersSubPanel);//blue
        resetColor(creditsSubPanel);
        resetColor(accountsSubPanel);
        resetColor(aboutSubPanel);
    }//GEN-LAST:event_customersLabelMouseClicked

    private void homeSubPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeSubPanelMousePressed
        homePanel.setVisible(true);
        customersPanel.setVisible(false);
        creditsPanel.setVisible(false);
        accountsPanel.setVisible(false);
        aboutPanel.setVisible(false);
        setColor(homeSubPanel);
        resetColor(customersSubPanel);
        resetColor(creditsSubPanel);
        resetColor(accountsSubPanel);
        resetColor(aboutSubPanel);
    }//GEN-LAST:event_homeSubPanelMousePressed

    private void customersSubPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customersSubPanelMousePressed
        customersPanel.setVisible(true);
        homePanel.setVisible(false);
        creditsPanel.setVisible(false);
        accountsPanel.setVisible(false);
        aboutPanel.setVisible(false);
        resetColor(homeSubPanel);
        setColor(customersSubPanel);
        resetColor(creditsSubPanel);
        resetColor(accountsSubPanel);
        resetColor(aboutSubPanel);
    }//GEN-LAST:event_customersSubPanelMousePressed

    private void homeSubPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeSubPanelMouseExited
        setColor(homeSubPanel);
        resetColor(customersSubPanel);
        resetColor(creditsSubPanel);
        resetColor(accountsSubPanel);
        resetColor(aboutSubPanel);
    }//GEN-LAST:event_homeSubPanelMouseExited

    private void creditsSubPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_creditsSubPanelMousePressed
        calculateTotalCredit();
        calculateTotalDailyPayed();
        calculateTotalDailyCredit();
        creditsPanel.setVisible(true);
        customersPanel.setVisible(false);
        homePanel.setVisible(false);
        accountsPanel.setVisible(false);
        aboutPanel.setVisible(false);
        resetColor(homeSubPanel);
        resetColor(customersSubPanel);
        setColor(creditsSubPanel);
        resetColor(accountsSubPanel);
        resetColor(aboutSubPanel);
    }//GEN-LAST:event_creditsSubPanelMousePressed

    private void aboutSubPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutSubPanelMousePressed
        aboutPanel.setVisible(true);
        accountsPanel.setVisible(false);
        creditsPanel.setVisible(false);
        customersPanel.setVisible(false);
        homePanel.setVisible(false);
        resetColor(homeSubPanel);
        resetColor(customersSubPanel);
        resetColor(creditsSubPanel);
        resetColor(accountsSubPanel);
        setColor(aboutSubPanel);
    }//GEN-LAST:event_aboutSubPanelMousePressed

    private void creditsLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_creditsLabelMousePressed
        calculateTotalCredit();
        calculateTotalDailyPayed();
        calculateTotalDailyCredit();
        creditsPanel.setVisible(true);
        customersPanel.setVisible(false);
        homePanel.setVisible(false);
        accountsPanel.setVisible(false);
        aboutPanel.setVisible(false);
        resetColor(homeSubPanel);
        resetColor(customersSubPanel);
        setColor(creditsSubPanel);
        resetColor(accountsSubPanel);
        resetColor(aboutSubPanel);
    }//GEN-LAST:event_creditsLabelMousePressed

    private void aboutLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutLabelMousePressed
        aboutPanel.setVisible(true);
        accountsPanel.setVisible(false);
        creditsPanel.setVisible(false);
        customersPanel.setVisible(false);
        homePanel.setVisible(false);
        resetColor(homeSubPanel);
        resetColor(customersSubPanel);
        resetColor(creditsSubPanel);
        resetColor(accountsSubPanel);
        setColor(aboutSubPanel);
    }//GEN-LAST:event_aboutLabelMousePressed

    private void addNewCustomerPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addNewCustomerPanelMousePressed
        RegisterCustomer rg = new RegisterCustomer();
        rg.setVisible(true);
    }//GEN-LAST:event_addNewCustomerPanelMousePressed

    private void displayCustomerPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_displayCustomerPanelMousePressed
        DisplayCustomers dc = new DisplayCustomers();
        dc.fillCustomerTable();
        dc.setVisible(true);
    }//GEN-LAST:event_displayCustomerPanelMousePressed

    private void modifyCustomersPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modifyCustomersPanelMousePressed
        String id = JOptionPane.showInputDialog(new JFrame(), "Enter Id ");

        boolean isFound = findId(id);

        if (id.equals("")) {
            JOptionPane.showMessageDialog(new JFrame(), "Id field is empty!\nPlease Try again! ", "LoginError", JOptionPane.ERROR_MESSAGE);
        } else if (isFound) {
            ModifyCustomer mc = new ModifyCustomer();
            mc.fillCustomersData(id);
            mc.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "This id is not registered!\nPlease try again!", "LoginError", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_modifyCustomersPanelMousePressed

    private void searchCustomerPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchCustomerPanelMousePressed
        String id = JOptionPane.showInputDialog(new JFrame(), "Enter Id ");

        boolean isFound = findId(id);

        if (id.equals("")) {
            JOptionPane.showMessageDialog(new JFrame(), "Id field is empty!\nPlease Try again! ", "LoginError", JOptionPane.ERROR_MESSAGE);
        } else if (isFound) {
            SearchResult sr = new SearchResult();
            sr.fillCustomersData(id);
            sr.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "This id is not registered!\nPlease try again!", "LoginError", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_searchCustomerPanelMousePressed

    private void removeCustomerPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeCustomerPanelMousePressed
        String id = JOptionPane.showInputDialog(new JFrame(), "Enter Id ");

        boolean isFound = findId(id);

        if (id.equals("")) {
            JOptionPane.showMessageDialog(new JFrame(), "Id field is empty!\nPlease Try again! ", "LoginError", JOptionPane.ERROR_MESSAGE);
        } else if (isFound) {
            removeCustomer(id);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "This id is not registered!\nPlease try again!", "LoginError", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_removeCustomerPanelMousePressed

    private void addCreditPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addCreditPanelMousePressed
        String id = JOptionPane.showInputDialog(new JFrame(), "Enter Id ");

        boolean isFound = findId(id);

        if (id.equals("")) {
            JOptionPane.showMessageDialog(new JFrame(), "Id field is empty!\nPlease Try again! ", "LoginError", JOptionPane.ERROR_MESSAGE);
        } else if (isFound) {
            addCredit(id);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "This id is not registered!\nPlease try again!", "LoginError", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addCreditPanelMousePressed

    private void payCreditPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payCreditPanelMousePressed
        String id = JOptionPane.showInputDialog(new JFrame(), "Enter Id ");

        boolean isFound = findId(id);

        if (id.equals("")) {
            JOptionPane.showMessageDialog(new JFrame(), "Id field is empty!\nPlease Try again! ", "LoginError", JOptionPane.ERROR_MESSAGE);
        } else if (isFound) {
            payCredit(id);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "This id is not registered!\nPlease try again!", "LoginError", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_payCreditPanelMousePressed

    private void activityHistoryPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_activityHistoryPanelMousePressed
        DisplayActivities da = new DisplayActivities();
        da.fillActivityTable();
        da.setVisible(true);
    }//GEN-LAST:event_activityHistoryPanelMousePressed

    private void displayAccountsPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_displayAccountsPanelMousePressed
        DisplayAdmins da = new DisplayAdmins();
        da.fillAdminsTable();
        da.setVisible(true);
    }//GEN-LAST:event_displayAccountsPanelMousePressed

    private void modifyAccountsPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modifyAccountsPanelMousePressed
        JOptionPane.showMessageDialog(new JFrame(), "Admin Verification is needed to perform this action!");
        Verify vr = new Verify();
        vr.setToDo("ModifyAdmin");
        vr.setVisible(true);
    }//GEN-LAST:event_modifyAccountsPanelMousePressed

    private void searchAccountsPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchAccountsPanelMousePressed
        String usrnm = JOptionPane.showInputDialog(new JFrame(), "Enter Username", "Search Account", JOptionPane.DEFAULT_OPTION);
        boolean isValid = false;
        isValid = findUsername(usrnm);

        if (isValid) {
            AccountSearchResult asr = new AccountSearchResult();
            asr.fillAdminsData(usrnm);
            asr.setVisible(true);
        } else if (usrnm.equals("")) {
            JOptionPane.showMessageDialog(new JFrame(), "Invalid Input!!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Sorry this username is not registered!");
        }
    }//GEN-LAST:event_searchAccountsPanelMousePressed

    private void removeAccountsPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeAccountsPanelMousePressed
        JOptionPane.showMessageDialog(new JFrame(), "Admin Verification is needed to perform this action!");
        Verify vr = new Verify();
        vr.setToDo("RemoveAccount");
        vr.setVisible(true);
    }//GEN-LAST:event_removeAccountsPanelMousePressed

    private void accountsSubPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsSubPanelMousePressed
        accountsPanel.setVisible(true);
        creditsPanel.setVisible(false);
        customersPanel.setVisible(false);
        homePanel.setVisible(false);
        aboutPanel.setVisible(false);
        resetColor(homeSubPanel);
        resetColor(customersSubPanel);
        resetColor(creditsSubPanel);
        setColor(accountsSubPanel);
        resetColor(aboutSubPanel);
    }//GEN-LAST:event_accountsSubPanelMousePressed

    private void accountsLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsLabelMousePressed
        accountsPanel.setVisible(true);
        creditsPanel.setVisible(false);
        customersPanel.setVisible(false);
        homePanel.setVisible(false);
        aboutPanel.setVisible(false);
        resetColor(homeSubPanel);
        resetColor(customersSubPanel);
        resetColor(creditsSubPanel);
        setColor(accountsSubPanel);
        resetColor(aboutSubPanel);
    }//GEN-LAST:event_accountsLabelMousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aboutLabel;
    private javax.swing.JPanel aboutPanel;
    private javax.swing.JPanel aboutSubPanel;
    private javax.swing.JLabel accountsLabel;
    private javax.swing.JPanel accountsPanel;
    private javax.swing.JPanel accountsSubPanel;
    private javax.swing.JPanel accountsTitleLabel;
    private javax.swing.JPanel activityHistoryPanel;
    private javax.swing.JPanel addCreditPanel;
    private javax.swing.JPanel addNewCustomerPanel;
    private javax.swing.JLabel creditsLabel;
    private javax.swing.JPanel creditsPanel;
    private javax.swing.JPanel creditsSubPanel;
    private javax.swing.JPanel creditsTitleLabel;
    private javax.swing.JPanel creditsTitleLabel1;
    private javax.swing.JLabel customersLabel;
    private javax.swing.JPanel customersPanel;
    private javax.swing.JPanel customersSubPanel;
    private javax.swing.JPanel customersTitleLabel1;
    private javax.swing.JPanel displayAccountsPanel;
    private javax.swing.JPanel displayCustomerPanel;
    private javax.swing.JLabel homeLabel;
    private javax.swing.JPanel homePanel;
    private javax.swing.JPanel homeSubPanel;
    private javax.swing.JPanel homeTitleLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel leftSidePanel;
    private javax.swing.JPanel modifyAccountsPanel;
    private javax.swing.JLabel modifyCustomerPanel;
    private javax.swing.JLabel modifyCustomerPanel1;
    private javax.swing.JPanel modifyCustomersPanel;
    private javax.swing.JPanel payCreditPanel;
    private javax.swing.JPanel removeAccountsPanel;
    private javax.swing.JPanel removeCustomerPanel;
    private javax.swing.JPanel rightSidePanel;
    private javax.swing.JPanel searchAccountsPanel;
    private javax.swing.JPanel searchCustomerPanel;
    private javax.swing.JPanel topLeftPanel;
    private javax.swing.JLabel totalCreditLabel;
    private javax.swing.JLabel totalDailyCreditLabel;
    private javax.swing.JLabel totalDailyPayedLabel;
    // End of variables declaration//GEN-END:variables
}
