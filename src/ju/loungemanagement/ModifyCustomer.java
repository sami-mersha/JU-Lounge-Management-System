package ju.loungemanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ModifyCustomer extends javax.swing.JFrame {

    int day, month, year;
    boolean notInformed;
    String idNumber;

    public ModifyCustomer() {
        initComponents();
    }

    public void setDateNow(){
        String date = java.time.LocalDate.now().toString();
        String[] dateParts = date.split("-");
        day = Integer.parseInt(dateParts[2]);
        month = Integer.parseInt(dateParts[1]);
        year = Integer.parseInt(dateParts[0]);
    }
    
    public void addToHistory(String fullName, String idNo){
        String event = "Customer account has been modified for " + fullName + " with <" + idNo + "> id.";
        
        try {
            Connection con = getConnection();
            Statement st = con.createStatement();
            setDateNow();
            String cmd = "INSERT INTO JULOUNGE.HISTORY VALUES("+ day +","+ month +", "+ year +", '" + event +"')";
            st.executeUpdate(cmd);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void fillCustomersData(String idn) {
        idNumber = idn;
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();
            String command = "SELECT * FROM JULOUNGE.CUSTOMER";

            ResultSet set = st.executeQuery(command);

            String fullName, gender, campus;
            int age, phoneNo, zone, block, room;
            while (set.next()) {
                if (idn.equals(set.getString("ID"))) {
                    fullName = set.getString("FULLNAME");
                    fullNametf.setText(fullName);
                    idNoLabel.setText(idn);
                    age = set.getInt("AGE");
                    agetf.setText("" + age);
                    gender = set.getString("SEX");

                    if (gender.equals("Male")) {
                        malerb.setSelected(true);
                    } else {
                        femalerb.setSelected(true);
                    }

                    phoneNo = set.getInt("PHONENO");
                    phoneNotf.setText("0" + phoneNo);
                    campus = set.getString("CAMPUS");

                    if (campus.equals("Jimma University - Main")) {
                        mainrb.setSelected(true);
                    } else if (campus.equals("Jimma Institute of Technology")) {
                        techrb.setSelected(true);
                    } else {
                        agrirb.setSelected(true);
                    }

                    zone = set.getInt("ZONE");
                    zonetf.setText("" + zone);
                    block = set.getInt("BLOCK");
                    blocktf.setText("" + block);
                    room = set.getInt("ROOM");
                    roomtf.setText("" + room);

                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean checkPhoneNo(String phoneNo) {
        JFrame jf = new JFrame();
        boolean valid = true;

        if (phoneNo == null) {
            JOptionPane.showMessageDialog(jf, "Please enter your phone number!");
            phoneNotf.setText("Phone number here...");
            notInformed = false;
            valid = false;
        } else if (phoneNo.length() != 10) {
            JOptionPane.showMessageDialog(jf, "Phone numbers must have 10 digit!");
            valid = false;
            notInformed = false;
        }

        return valid;
    }

    public boolean checkAge(String age) {
        JFrame jf = new JFrame();
        boolean valid = true;

        if (age == null) {
            JOptionPane.showMessageDialog(jf, "Please enter your age!");
            agetf.setText("Your age here...");
            valid = false;
            notInformed = false;
        } else if (Integer.parseInt(age) < 0) {
            JOptionPane.showMessageDialog(jf, "Invalid Age!");
            valid = false;
            notInformed = false;
        }

        return valid;
    }

    public boolean checkFullName(String fullName) {
        JFrame jf = new JFrame();
        boolean valid = true;

        if (fullName == null || fullName.equals("Full name Here...")) {
            JOptionPane.showMessageDialog(jf, "Please enter full name!");
            fullNametf.setText("Full name Here...");
            valid = false;
            notInformed = false;
        } else if (fullName.length() > 25) {
            JOptionPane.showMessageDialog(jf, "Names must be less than 25 character!");
            valid = false;
            notInformed = false;
        }

        return valid;
    }

    public boolean updateData(String fullName, String id, int age, String gender, int phoneNo, String campus, int zone, int block, int room) {
        int checker = -1;
        boolean registered = false;

        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();
            String cmd = "UPDATE JULOUNGE.CUSTOMER SET FULLNAME = '" + fullName + "', AGE = " + age + ", SEX = '" + gender + "', PHONENO = " + phoneNo + ", CAMPUS = '" + campus + "', ZONE = " + zone + ", BLOCK = " + block + ", ROOM = " + room + " WHERE ID = '" + id + "'";

            checker = st.executeUpdate(cmd);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }

        if (checker > 0) {
            registered = true;
        }
        return registered;
    }

    public Connection getConnection() throws Exception {
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        exitPanel = new javax.swing.JPanel();
        exitLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        phoneNotf = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        agetf = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fullNametf = new javax.swing.JTextField();
        malerb = new javax.swing.JRadioButton();
        femalerb = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        roomtf = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        blocktf = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        zonetf = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        agrirb = new javax.swing.JRadioButton();
        mainrb = new javax.swing.JRadioButton();
        techrb = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        submitButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        idNoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Gill Sans MT", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Modify Customer");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, -1, -1));

        exitLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete_32px.png"))); // NOI18N
        exitLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                exitLabelMousePressed(evt);
            }
        });

        javax.swing.GroupLayout exitPanelLayout = new javax.swing.GroupLayout(exitPanel);
        exitPanel.setLayout(exitPanelLayout);
        exitPanelLayout.setHorizontalGroup(
            exitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exitPanelLayout.createSequentialGroup()
                .addComponent(exitLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        exitPanelLayout.setVerticalGroup(
            exitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, exitPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(exitLabel))
        );

        jPanel1.add(exitPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 10, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 70));

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Arial", 2, 14)); // NOI18N
        jLabel7.setText("Personal Informations");

        phoneNotf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Phone Number");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Gender");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Age");

        agetf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Id No.");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Full Name");

        fullNametf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        buttonGroup1.add(malerb);
        malerb.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        malerb.setText("Male");

        buttonGroup1.add(femalerb);
        femalerb.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        femalerb.setText("Female");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel3))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel4)
                        .addGap(15, 15, 15)
                        .addComponent(agetf, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(6, 6, 6)
                        .addComponent(phoneNotf, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(malerb)
                                .addGap(18, 18, 18)
                                .addComponent(femalerb))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(7, 7, 7)
                                .addComponent(fullNametf, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2))
                    .addComponent(fullNametf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addComponent(jLabel3)
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel4))
                    .addComponent(agetf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(malerb)
                    .addComponent(femalerb))
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel6))
                    .addComponent(phoneNotf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setText("Room");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, -1, -1));

        roomtf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel4.add(roomtf, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, 50, -1));

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setText("Block");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, -1, -1));

        blocktf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel4.add(blocktf, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 50, -1));

        jLabel11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel11.setText("Zone");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        zonetf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel4.add(zonetf, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, 40, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel12.setText("Campus");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, -1, -1));

        buttonGroup2.add(agrirb);
        agrirb.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        agrirb.setText("Jimma University - Agri Campus");
        jPanel4.add(agrirb, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, -1, -1));

        buttonGroup2.add(mainrb);
        mainrb.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mainrb.setText("Jimma University - Main Campus");
        jPanel4.add(mainrb, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, -1, -1));

        buttonGroup2.add(techrb);
        techrb.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        techrb.setText("Jimma Institute of Technology");
        jPanel4.add(techrb, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, -1, -1));
        jPanel4.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 70, -1));
        jPanel4.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 300, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 2, 14)); // NOI18N
        jLabel13.setText("Location");

        submitButton.setBackground(new java.awt.Color(204, 255, 204));
        submitButton.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        submitButton.setForeground(new java.awt.Color(0, 102, 102));
        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        idNoLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(idNoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(idNoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(452, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(110, 110, 110)
                            .addComponent(jLabel7)
                            .addGap(262, 262, 262)
                            .addComponent(jLabel13))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(240, 240, 240)
                            .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(246, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7)
                        .addComponent(jLabel13))
                    .addGap(3, 3, 3)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(20, 20, 20)
                    .addComponent(submitButton)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 800, 380));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitLabelMousePressed
        dispose();
    }//GEN-LAST:event_exitLabelMousePressed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        notInformed = true;
        try {
            String fullName = fullNametf.getText();
            String id = idNumber;
            int age = Integer.parseInt(agetf.getText());

            String gender = null;
            if (malerb.isSelected()) {
                gender = "Male";
            } else if (femalerb.isSelected()) {
                gender = "Female";
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Please select customers gender!");
                notInformed = false;
            }

            int phoneNo = Integer.parseInt(phoneNotf.getText());

            String campus = null;
            if (mainrb.isSelected()) {
                campus = "Jimma University - Main";
            } else if (techrb.isSelected()) {
                campus = "Jimma Institute of Technology";
            } else if (agrirb.isSelected()) {
                campus = "Jimma University - Agri";
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Please select customers campus!");
                notInformed = false;
            }

            int zone = Integer.parseInt(zonetf.getText());
            int block = Integer.parseInt(blocktf.getText());
            int room = Integer.parseInt(roomtf.getText());

            boolean isModified = false;

            if (checkFullName(fullName) && checkPhoneNo(phoneNotf.getText()) && gender != null & campus != null) {
                isModified = updateData(fullName, id, age, gender, phoneNo, campus, zone, block, room);
            }

            if (isModified) {
                addToHistory(fullName, id);
                JOptionPane.showMessageDialog(new JFrame(), "Successfully Modified!");
                dispose();
            } else if (notInformed) {
                JOptionPane.showMessageDialog(new JFrame(), "Customers data is not added.\nPlease try again!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "Please fill the required information correctly!");
        }

    }//GEN-LAST:event_submitButtonActionPerformed

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
            java.util.logging.Logger.getLogger(ModifyCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModifyCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModifyCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModifyCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ModifyCustomer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField agetf;
    private javax.swing.JRadioButton agrirb;
    private javax.swing.JTextField blocktf;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel exitLabel;
    private javax.swing.JPanel exitPanel;
    private javax.swing.JRadioButton femalerb;
    private javax.swing.JTextField fullNametf;
    private javax.swing.JLabel idNoLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JRadioButton mainrb;
    private javax.swing.JRadioButton malerb;
    private javax.swing.JTextField phoneNotf;
    private javax.swing.JTextField roomtf;
    private javax.swing.JButton submitButton;
    private javax.swing.JRadioButton techrb;
    private javax.swing.JTextField zonetf;
    // End of variables declaration//GEN-END:variables
}
