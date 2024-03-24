package ju.loungemanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {

    int day, month, year;

    public Login() {
        initComponents();
    }

    public void setDateNow() {
        String date = java.time.LocalDate.now().toString();
        String[] dateParts = date.split("-");
        day = Integer.parseInt(dateParts[2]);
        month = Integer.parseInt(dateParts[1]);
        year = Integer.parseInt(dateParts[0]);
    }

    public void addToHistory(String userName) {
        String event = "New login with <" + userName + "> username.";

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

    public static Connection getConnection() {
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

        bgPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        usernametf = new javax.swing.JTextField();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        cnaButton = new javax.swing.JButton();
        passwordtf = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        adminLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bgPanel.setBackground(new java.awt.Color(51, 51, 51));
        bgPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        usernametf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel2.add(usernametf, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 300, -1));

        usernameLabel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        usernameLabel.setText("Username");
        jPanel2.add(usernameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, -1, -1));

        passwordLabel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        passwordLabel.setText("Password");
        jPanel2.add(passwordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, -1, -1));

        loginButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        loginButton.setText("Log In");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        jPanel2.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, -1, -1));

        cnaButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cnaButton.setText("Create New Account");
        cnaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cnaButtonActionPerformed(evt);
            }
        });
        jPanel2.add(cnaButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 310, -1, -1));

        passwordtf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        passwordtf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordtfActionPerformed(evt);
            }
        });
        jPanel2.add(passwordtf, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, 300, -1));

        adminLabel.setBackground(new java.awt.Color(204, 204, 204));
        adminLabel.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        adminLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user_30px.png"))); // NOI18N
        adminLabel.setText("Admin Login");
        jPanel1.add(adminLabel);

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 190, 50));

        bgPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 390, 370));

        titleLabel.setFont(new java.awt.Font("Malgun Gothic", 1, 36)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setText("Lounge Management System");
        bgPanel.add(titleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, -1, 50));

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        bgPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 70));

        getContentPane().add(bgPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void passwordtfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordtfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordtfActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        String username, password;
        username = usernametf.getText();
        password = passwordtf.getText();
        boolean isvalid = false;
        JFrame jf = new JFrame();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(jf, "Username field is empty!", "Login Error", JOptionPane.ERROR_MESSAGE);
            usernametf.setText("Your username here...");
        } else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(jf, "Password field is empty!", "Login Error", JOptionPane.ERROR_MESSAGE);
        } else {
            isvalid = checkValidity(username, password);
        }

        if (isvalid) {
            password = "";
            dispose();
            addToHistory(username);
            new Home().setVisible(true);
        }


    }//GEN-LAST:event_loginButtonActionPerformed

    private void cnaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cnaButtonActionPerformed
        JOptionPane.showMessageDialog(new JFrame(), "please ask any admin to verify this sign up process!");
        Verify vrf = new Verify();
        vrf.setToDo("SignUp");
        vrf.setVisible(true);
    }//GEN-LAST:event_cnaButtonActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adminLabel;
    private javax.swing.JPanel bgPanel;
    private javax.swing.JButton cnaButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordtf;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernametf;
    // End of variables declaration//GEN-END:variables
}
