package ju.loungemanagement;

public class Splash extends javax.swing.JFrame {

    int day, month, year;
    public Splash() {
        initComponents();
    }

    public void setDateNow() {
        String date = java.time.LocalDate.now().toString();
        String[] dateParts = date.split("-");
        day = Integer.parseInt(dateParts[2]);
        month = Integer.parseInt(dateParts[1]);
        year = Integer.parseInt(dateParts[0]);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BackgroundPanel = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        loadingLabel = new javax.swing.JLabel();
        loadingValueLabel = new javax.swing.JLabel();
        ProgramTitle = new javax.swing.JLabel();
        StaffPicture = new javax.swing.JLabel();
        bgimagePanel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BackgroundPanel.setBackground(new java.awt.Color(153, 153, 255));
        BackgroundPanel.setPreferredSize(new java.awt.Dimension(900, 500));
        BackgroundPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        BackgroundPanel.add(progressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 510, 930, 10));

        loadingLabel.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        loadingLabel.setForeground(new java.awt.Color(255, 255, 255));
        loadingLabel.setText("Loading...");
        BackgroundPanel.add(loadingLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, -1, -1));

        loadingValueLabel.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        loadingValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        loadingValueLabel.setText("0 %");
        BackgroundPanel.add(loadingValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 490, -1, -1));

        ProgramTitle.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        ProgramTitle.setForeground(new java.awt.Color(255, 255, 255));
        ProgramTitle.setText("LOUNGE MANAGEMENT SYSTEM");
        BackgroundPanel.add(ProgramTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 410, -1, -1));

        StaffPicture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/cc3.jpg"))); // NOI18N
        StaffPicture.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BackgroundPanel.add(StaffPicture, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 620, 360));
        BackgroundPanel.add(bgimagePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 520));

        getContentPane().add(BackgroundPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 520));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        Splash s = new Splash();
        s.setVisible(true);
        
        try {
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(60);
                s.loadingValueLabel.setText(i + "%");

                if (i == 10) {
                    s.setDateNow();
                    s.loadingLabel.setText("Initialising the program...");
                }
                if (i == 50) {
                    s.loadingLabel.setText("Loading the modules...");
                }
                if (i == 70) {
                    s.loadingLabel.setText("Launching the application...");
                }
                s.progressBar.setValue(i);
            }

            s.dispose();
            new Login().setVisible(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BackgroundPanel;
    private javax.swing.JLabel ProgramTitle;
    private javax.swing.JLabel StaffPicture;
    private javax.swing.JLabel bgimagePanel;
    private javax.swing.JLabel loadingLabel;
    private javax.swing.JLabel loadingValueLabel;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
