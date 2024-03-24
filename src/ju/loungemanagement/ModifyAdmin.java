
package ju.loungemanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class ModifyAdmin extends javax.swing.JFrame {

    int day, month, year;
    boolean notInformed;
    String usernm;
    public ModifyAdmin() {
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
        String event = "Admin account has been modified for " + fullName + " with <" + idNo + "> id.";
        
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
    
    public boolean checkPhoneNo(String phoneNo) {
        JFrame jf = new JFrame();
        boolean valid = true;

        if (phoneNo == null) {
            JOptionPane.showMessageDialog(jf, "Please enter your phone number!");
            phoneNotf.setText("Your phone number here...");
            valid = false;
            notInformed = false;
        } else if (phoneNo.length() != 10) {
            JOptionPane.showMessageDialog(jf, "Phone numbers must have 10 digit!");
            valid = false;
            notInformed = false;
        }

        return valid;
    }
    
    public boolean checkGender(String gender) {
        JFrame jf = new JFrame();
        boolean valid = true;

        if (gender == null) {
            JOptionPane.showMessageDialog(jf, "Please select gender!");
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
    
    public boolean checkPassword(String firstPwd, String secondPwd) {
        JFrame jf = new JFrame();
        boolean valid = true;

        if (firstPwd == null) {
            JOptionPane.showMessageDialog(jf, "Please enter your password!");
            passwordField1.setText("Your password here...");
            valid = false;
            notInformed = false;
        } else if (secondPwd == null) {
            JOptionPane.showMessageDialog(jf, "Please confirm your password!");
            passwordField2.setText("Confirm your password here...");
            valid = false;
            notInformed = false;
        } else if (firstPwd.equals(secondPwd) == false) {
            JOptionPane.showMessageDialog(jf, "Passwords do not match!\nPlease try again!");
            valid = false;
            notInformed = false;
        } else if (secondPwd.length() > 16) {
            JOptionPane.showMessageDialog(jf, "Your password must be less than 16 character!");
            valid = false;
            notInformed = false;
        } else if (secondPwd.length() < 8) {
            JOptionPane.showMessageDialog(jf, "Your password must be greater or equal to 8 character.\nPlease enter another password!");
            valid = false;
            notInformed = false;
        } else if (secondPwd.equals("12345678")) {
            JOptionPane.showMessageDialog(jf, "The password is too easy to predict.\nPlease enter another password!");
            valid = false;
            notInformed = false;
        }

        return valid;
    }
    
    public boolean checkFullName(String fullName) {
        JFrame jf = new JFrame();
        boolean valid = true;

        if (fullName == null || fullName.equals("Your full name Here...")) {
            JOptionPane.showMessageDialog(jf, "Please enter your full name correctly!");
            fullNametf.setText("Your full name Here...");
            valid = false;
            notInformed = false;
        } else if (fullName.length() > 25) {
            JOptionPane.showMessageDialog(jf, "Your name must be less than 25 character!");
            valid = false;
            notInformed = false;
        }

        return valid;
    }
    
    public boolean updateData(String fullName, String username, String password, int age, String gender, int phoneNo) {
        int checker = -1;
        boolean updated = false;

        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();
            String cmd = "UPDATE JULOUNGE.DATA SET FULLNAME = '" + fullName + "', PASSWORD = '" + password + "', AGE = " + age + ", GENDER = '" + gender + "', PHONENUMBER = " + phoneNo + " WHERE USERNAME = '" + username + "'";

            checker = st.executeUpdate(cmd);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(), e);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e);
        }

        if (checker > 0) {
            updated = true;
        }
        return updated;
    }
    
    public void fillAdminData(String username){
        usernm = username;
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();
            String command = "SELECT * FROM JULOUNGE.DATA";

            ResultSet set = st.executeQuery(command);

            String fullName, gender, password;
            int age, phoneNo;
            while (set.next()) {
                if (username.equals(set.getString("USERNAME"))) {
                    fullName = set.getString("FULLNAME");
                    fullNametf.setText(fullName);
                    usernameLabel.setText(username);
                    age = set.getInt("AGE");
                    agetf.setText("" + age);
                    gender = set.getString("GENDER");

                    if (gender.equals("Male")) {
                        maleRb.setSelected(true);
                    } else {
                        femaleRb.setSelected(true);
                    }

                    phoneNo = set.getInt("PHONENUMBER");
                    phoneNotf.setText("0" + phoneNo);
                    
                    password = set.getString("PASSWORD");
                    passwordField1.setText(password);
                    passwordField2.setText(password);
                    
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fullNametf = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        agetf = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        maleRb = new javax.swing.JRadioButton();
        femaleRb = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        passwordField1 = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        passwordField2 = new javax.swing.JPasswordField();
        submitButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        phoneNotf = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        exitBtn = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Full Name: ");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, -1, 30));

        fullNametf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel2.add(fullNametf, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 300, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Username:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, -1, 30));

        agetf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel2.add(agetf, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 300, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("New Password:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, -1, 30));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Age: ");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, -1, 30));

        maleRb.setText(" Male");
        maleRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maleRbActionPerformed(evt);
            }
        });
        jPanel2.add(maleRb, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, -1, -1));

        femaleRb.setText(" Female");
        jPanel2.add(femaleRb, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Gender: ");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, -1, 20));

        passwordField1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        passwordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordField1ActionPerformed(evt);
            }
        });
        jPanel2.add(passwordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 300, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Confirm Password:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 120, 30));

        passwordField2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        passwordField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordField2ActionPerformed(evt);
            }
        });
        jPanel2.add(passwordField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 300, -1));

        submitButton.setBackground(new java.awt.Color(255, 255, 255));
        submitButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });
        jPanel2.add(submitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 290, 200, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setText("Phone Number: ");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, 30));

        phoneNotf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel2.add(phoneNotf, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 300, -1));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        usernameLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usernameLabel)
                .addContainerGap(290, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 300, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 560, 350));

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete_32px.png"))); // NOI18N
        exitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(exitBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(exitBtn))
        );

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 10, 30, -1));

        jLabel8.setFont(new java.awt.Font("Gill Sans MT", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Modify Admins Information");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, -1, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 70));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void maleRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maleRbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maleRbActionPerformed

    private void passwordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordField1ActionPerformed

    private void passwordField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordField2ActionPerformed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        String fullName, username, gender, firstPassword, secondPassword;
        username = usernm;
        int age, phoneNo;
        JFrame jf = new JFrame();
        boolean isUpdated = false;
        notInformed = true;

        try {
            fullName = fullNametf.getText();
            age = Integer.parseInt(agetf.getText());

            if (maleRb.isSelected()) {
                gender = "Male";
            } else if (femaleRb.isSelected()) {
                gender = "Female";
            } else {
                gender = null;
            }

            firstPassword = passwordField1.getText();
            secondPassword = passwordField2.getText();
            phoneNo = Integer.parseInt(phoneNotf.getText());

            if (checkFullName(fullName) && checkPassword(firstPassword, secondPassword) && checkAge(agetf.getText()) && checkGender(gender) && checkPhoneNo(phoneNotf.getText())) {
                isUpdated = updateData(fullName, username, secondPassword, age, gender, phoneNo);
            }

            if (notInformed) {
                if (isUpdated) {
                    JOptionPane.showMessageDialog(jf, "Your account is Successfully modified!");
                    addToHistory(fullName, username);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(jf, "Your account is not modified\n please try again!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(jf, "Invalid Input!\nPlease fill the required information carefully!");
        }
    }//GEN-LAST:event_submitButtonActionPerformed

    private void exitBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitBtnMouseClicked
        dispose();
    }//GEN-LAST:event_exitBtnMouseClicked

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
            java.util.logging.Logger.getLogger(ModifyAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModifyAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModifyAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModifyAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ModifyAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField agetf;
    private javax.swing.JLabel exitBtn;
    private javax.swing.JRadioButton femaleRb;
    private javax.swing.JTextField fullNametf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton maleRb;
    private javax.swing.JPasswordField passwordField1;
    private javax.swing.JPasswordField passwordField2;
    private javax.swing.JTextField phoneNotf;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
