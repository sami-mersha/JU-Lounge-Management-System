package ju.loungemanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SignUp extends javax.swing.JFrame {

    //DataFields
    boolean notInformed = true;
    int day, month, year;
    public SignUp() {
        initComponents();
    }

    public void setDateNow(){
        String date = java.time.LocalDate.now().toString();
        String[] dateParts = date.split("-");
        day = Integer.parseInt(dateParts[2]);
        month = Integer.parseInt(dateParts[1]);
        year = Integer.parseInt(dateParts[0]);
    }
    
    public void addToHistory(String fullName, String userName){
        String event = "New account has been created for " + fullName + " with <" + userName + "> username.";
        
        try {
            Connection con = getConnection();
            Statement st = con.createStatement();
            setDateNow();
            String cmd = "INSERT INTO JULOUNGE.HISTORY VALUES("+ day +", "+ month +", "+ year +", '" + event +"')";
            st.executeUpdate(cmd);
        } catch (Exception e) {
            System.out.println(e);
        }
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

    public boolean checkPassword(String firstPwd, String secondPwd) {
        JFrame jf = new JFrame();
        boolean valid = true;

        if (firstPwd == null) {
            JOptionPane.showMessageDialog(jf, "Please enter your password!");
            signupPwdField1.setText("Your password here...");
            valid = false;
            notInformed = false;
        } else if (secondPwd == null) {
            JOptionPane.showMessageDialog(jf, "Please confirm your password!");
            signupPwdField2.setText("Confirm your password here...");
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

    public boolean checkUsername(String username) {
        JFrame jf = new JFrame();
        boolean valid = true;

        if (username == null || username.equals("Your username Here...")) {
            JOptionPane.showMessageDialog(jf, "Please enter your username correctly!");
            usernametf.setText("Your username Here...");
            valid = false;
            notInformed = false;
        } else if (username.length() > 10) {
            JOptionPane.showMessageDialog(jf, "Username's size must be less than 10 digit. \nPlease re enter another username!");
            valid = false;
            notInformed = false;
        }

        try {
            Connection con = getConnection();
            Statement st = con.createStatement();

            String cmd = "SELECT * FROM JULOUNGE.DATA";
            ResultSet set = st.executeQuery(cmd);

            while (set.next()) {
                if (set.getString("USERNAME").equals(username)) {
                    JOptionPane.showMessageDialog(jf, "The username already exists.\nPlease Use another username!");
                    valid = false;
                    notInformed = false;
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(jf, e);
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

    public int registerData(String fullName, String username, String pwd, int age, String gender, int phoneNo) throws Exception {
        int checker = -1;
        try {
            Connection cn = getConnection();
            Statement st = cn.createStatement();
            String cmd = "INSERT INTO JULOUNGE.DATA VALUES('" + fullName + "', '" + username + "', '" + pwd + "', " + age + ", '" + gender + "', " + phoneNo + ")";

            checker = st.executeUpdate(cmd);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return checker;
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

        jComboBox1 = new javax.swing.JComboBox();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fullNametf = new javax.swing.JTextField();
        usernametf = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        agetf = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        maleRb = new javax.swing.JRadioButton();
        femaleRb = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        signupPwdField1 = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        signupPwdField2 = new javax.swing.JPasswordField();
        signUpButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        phoneNotf = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        exitBtn = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        adminLabel = new javax.swing.JLabel();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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

        usernametf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        usernametf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernametfActionPerformed(evt);
            }
        });
        jPanel2.add(usernametf, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 300, -1));

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

        buttonGroup1.add(maleRb);
        maleRb.setText(" Male");
        maleRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maleRbActionPerformed(evt);
            }
        });
        jPanel2.add(maleRb, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, -1, -1));

        buttonGroup1.add(femaleRb);
        femaleRb.setText(" Female");
        jPanel2.add(femaleRb, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Gender: ");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, -1, 20));

        signupPwdField1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        signupPwdField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupPwdField1ActionPerformed(evt);
            }
        });
        jPanel2.add(signupPwdField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 300, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Confirm Password:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 120, 30));

        signupPwdField2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        signupPwdField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupPwdField2ActionPerformed(evt);
            }
        });
        jPanel2.add(signupPwdField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 300, -1));

        signUpButton.setBackground(new java.awt.Color(255, 255, 255));
        signUpButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        signUpButton.setText("Sign Up");
        signUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signUpButtonActionPerformed(evt);
            }
        });
        jPanel2.add(signUpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 290, 200, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setText("Phone Number: ");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, 30));

        phoneNotf.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel2.add(phoneNotf, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 300, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 560, 350));

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        titleLabel.setFont(new java.awt.Font("Malgun Gothic", 1, 36)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setText("Lounge Management System");
        jPanel3.add(titleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(197, 5, -1, -1));

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

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 70));

        adminLabel.setBackground(new java.awt.Color(204, 204, 204));
        adminLabel.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        adminLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/user_30px.png"))); // NOI18N
        adminLabel.setText("Sign Up Form");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(adminLabel)
                .addGap(18, 18, 18))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(adminLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, 200, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void usernametfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernametfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernametfActionPerformed

    private void signupPwdField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signupPwdField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_signupPwdField1ActionPerformed

    private void signupPwdField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signupPwdField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_signupPwdField2ActionPerformed

    private void signUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signUpButtonActionPerformed
        String fullName, username, gender, firstPassword, secondPassword;
        int age, phoneNo;
        JFrame jf = new JFrame();
        int registered = -1;
        notInformed = true;

        try {
            fullName = fullNametf.getText();
            username = usernametf.getText();
            age = Integer.parseInt(agetf.getText());

            if (maleRb.isSelected()) {
                gender = "Male";
            } else if (femaleRb.isSelected()) {
                gender = "Female";
            } else {
                gender = null;
            }

            firstPassword = signupPwdField1.getText();
            secondPassword = signupPwdField2.getText();
            phoneNo = Integer.parseInt(phoneNotf.getText());

            if (checkFullName(fullName) && checkUsername(username) && checkPassword(firstPassword, secondPassword) && checkAge(agetf.getText()) && checkGender(gender) && checkPhoneNo(phoneNotf.getText())) {
                registered = registerData(fullName, username, secondPassword, age, gender, phoneNo);
            }

            if (notInformed) {
                if (registered > 0) {
                    JOptionPane.showMessageDialog(jf, "Your account is Successfully created!");
                    addToHistory(fullName, username);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(jf, "Your account is not created please try again!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(jf, "Invalid Input!\nPlease fill the required information carefully!");
        }
    }//GEN-LAST:event_signUpButtonActionPerformed

    private void maleRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maleRbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maleRbActionPerformed

    private void exitBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitBtnMouseClicked
        dispose();
    }//GEN-LAST:event_exitBtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignUp().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adminLabel;
    private javax.swing.JTextField agetf;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel exitBtn;
    private javax.swing.JRadioButton femaleRb;
    private javax.swing.JTextField fullNametf;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton maleRb;
    private javax.swing.JTextField phoneNotf;
    private javax.swing.JButton signUpButton;
    private javax.swing.JPasswordField signupPwdField1;
    private javax.swing.JPasswordField signupPwdField2;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField usernametf;
    // End of variables declaration//GEN-END:variables
}
