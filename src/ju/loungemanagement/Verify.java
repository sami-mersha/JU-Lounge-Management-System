package ju.loungemanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Verify extends javax.swing.JFrame {

    int day, month, year;
    String toDo = null;
    boolean notInformed;

    public Verify() {
        initComponents();
    }

    public void setToDo(String toDo) {
        this.toDo = toDo;
    }

    public void setDateNow() {
        String date = java.time.LocalDate.now().toString();
        String[] dateParts = date.split("-");
        day = Integer.parseInt(dateParts[2]);
        month = Integer.parseInt(dateParts[1]);
        year = Integer.parseInt(dateParts[0]);
    }

    
    public void addToHistory(String fullName, String username) {
        String event = "Activity table data has been cleared by <" + fullName + "> with <" + username + "> username.";

        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();
            setDateNow();
            String cmd = "INSERT INTO JULOUNGE.HISTORY VALUES(" + day + ", " + month + ", " + year + ", '" + event + "')";
            st.executeUpdate(cmd);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e);
        }
    }
    
    
    public void clearActivities(String username) {

        String fullName = getFullName(username);
        
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();

            int response = JOptionPane.showConfirmDialog(new JFrame(), "Do you really want to delete all datas from activity history until now?", "Confirm to Delete", JOptionPane.YES_NO_CANCEL_OPTION);

            //0 means yes 1 means no 
            if (response == 0) {
                String cmd = "DELETE FROM JULOUNGE.HISTORY";
                int check = st.executeUpdate(cmd);
                if (check > 0) {
                    JOptionPane.showMessageDialog(new JFrame(), "Successfully Cleared!");
                    
                    addToHistory(fullName, username);
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Not Successful! Please Try Again");
                }
            } else if (response == 1) {
                JOptionPane.showMessageDialog(new JFrame(), "The data is not deleted!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e);
        }
    }

    public String getFullName(String username) {
        String fullName = "";
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();
            String cmd = "SELECT * FROM JULOUNGE.DATA";
            ResultSet set = st.executeQuery(cmd);

            while (set.next()) {
                if (set.getString("USERNAME").equals(username)) {
                    fullName = set.getString("FULLNAME");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return fullName;
    }

    public void addToRemovedAdminHistory(String fullName, String username) {
        String event = "Admins account with name <" + fullName + "> and username:<" + username + "> has been removed from the list!";

        try {
            Connection con = getConnection();
            Statement st = con.createStatement();
            Home hm = new Home();
            hm.setDateNow();
            String cmd = "INSERT INTO JULOUNGE.HISTORY VALUES(" + hm.day + "," + hm.month + ", " + hm.year + ", '" + event + "')";
            st.executeUpdate(cmd);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void removeAdmin(String username) {

        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();
            String command = "SELECT * FROM JULOUNGE.DATA";

            ResultSet set = st.executeQuery(command);

            String fullName = null;
            while (set.next()) {
                if (username.equals(set.getString("USERNAME"))) {
                    fullName = set.getString("FULLNAME");
                    break;
                }
            }

            int response = JOptionPane.showConfirmDialog(new JFrame(), "Do you want to delete " + fullName + "'s Account?", "Confirm to Delete", JOptionPane.YES_NO_CANCEL_OPTION);

            //0 means yes 1 means no 
            if (response == 0) {
                command = "DELETE FROM JULOUNGE.DATA WHERE USERNAME = '" + username + "'";
                st.executeUpdate(command);
                addToRemovedAdminHistory(fullName, username);
                JOptionPane.showMessageDialog(new JFrame(), "Successfully deleted!");
            } else if (response == 1) {
                JOptionPane.showMessageDialog(new JFrame(), "The data is not deleted!");
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
                    JOptionPane.showMessageDialog(jf, "Incorrect Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                    userNotFound = false;
                    notInformed = false;
                }
            }

            if (userNotFound) {
                JOptionPane.showMessageDialog(jf, "Username not found!", "Login Error", JOptionPane.ERROR_MESSAGE);
                notInformed = false;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(jf, e);
        }

        return valid;
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

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        verificationPanel = new javax.swing.JPanel();
        usernameVerificationtf = new javax.swing.JTextField();
        passwordVerificationpf = new javax.swing.JPasswordField();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel6.setFont(new java.awt.Font("Arial", 2, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Verification Page");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete_32px.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 30));

        verificationPanel.setBackground(new java.awt.Color(102, 102, 102));
        verificationPanel.setPreferredSize(new java.awt.Dimension(330, 145));
        verificationPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        usernameVerificationtf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        verificationPanel.add(usernameVerificationtf, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 263, -1));

        passwordVerificationpf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        verificationPanel.add(passwordVerificationpf, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 263, -1));

        cancelButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cancelButtonMousePressed(evt);
            }
        });
        verificationPanel.add(cancelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, -1, -1));

        okButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        okButton.setText("Ok");
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                okButtonMousePressed(evt);
            }
        });
        verificationPanel.add(okButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 80, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Password");
        verificationPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Username");
        verificationPanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, -1, -1));

        getContentPane().add(verificationPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 400, 170));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelButtonMousePressed
        dispose();
    }//GEN-LAST:event_cancelButtonMousePressed

    private void okButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMousePressed
        String usrnm = usernameVerificationtf.getText();
        String pwd = passwordVerificationpf.getText();
        boolean isValid = false;
        notInformed = true;
        isValid = checkValidity(usrnm, pwd);

        if (isValid) {
            if (toDo.equals("ModifyAdmin")) {
                ModifyAdmin ma = new ModifyAdmin();
                ma.fillAdminData(usrnm);
                ma.setVisible(true);
                dispose();
            } else if (toDo.equals("RemoveAccount")) {
                dispose();
                removeAdmin(usrnm);
            } else if (toDo.equals("SignUp")) {
                dispose();
                new SignUp().setVisible(true);
            } else if (toDo.equals("DeleteActivities")) {
                dispose();
                clearActivities(usrnm);
            }
        } else if (notInformed) {
            dispose();
            JOptionPane.showMessageDialog(new JFrame(), "Sorry! \nPermission denied!");
        }
    }//GEN-LAST:event_okButtonMousePressed

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        dispose();
    }//GEN-LAST:event_jLabel1MousePressed

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
            java.util.logging.Logger.getLogger(Verify.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Verify.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Verify.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Verify.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Verify().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton okButton;
    private javax.swing.JPasswordField passwordVerificationpf;
    private javax.swing.JTextField usernameVerificationtf;
    private javax.swing.JPanel verificationPanel;
    // End of variables declaration//GEN-END:variables
}
