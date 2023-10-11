/**
 * @author paulamh
 */
package bsc;

import java.awt.Image;
import java.awt.Toolkit;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class Login extends javax.swing.JFrame {
    private Icon iconoError = new ImageIcon(getClass().getResource("/images/error_bsc.png"));
    private Icon iconoWarning = new ImageIcon(getClass().getResource("/images/warning_bsc.png"));
    private static Conexion conexionBD = Conexion.getInstancia();
    private static Connection conexion = conexionBD.getConexion();
    
    public Login() {
        initComponents();
        setTitle("Login Brawl Stars Championship");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/images/bsc_100x100.png")).getImage());
    }

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/bsc_100x100.png"));
        return retValue;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagen = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnEnviar = new javax.swing.JButton();
        txtPass = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        imagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bsc_100x100.png"))); // NOI18N
        getContentPane().add(imagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, -1, -1));
        getContentPane().add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 190, -1));

        jLabel2.setText("Usuario:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 320, -1, -1));

        jLabel3.setText("Contraseña:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 380, -1, -1));

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel4.setText("¡Bienvenido/a ");
        jLabel4.setToolTipText("");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, 180, 40));

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel5.setText("Brawl Stars Championship 2023!");
        jLabel5.setToolTipText("");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 470, 40));

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEnviar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 460, -1, -1));
        getContentPane().add(txtPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 400, 190, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        validarForm();
    }//GEN-LAST:event_btnEnviarActionPerformed

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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
  
    public void validarForm(){
        
            boolean a = false;
            String usuario = txtUsuario.getText();
            String password = txtPass.getText();

            String passEncriptada = encriptaPass(password);

            if (compruebaUsuarioPass(usuario, passEncriptada)){
                BSC bsc = new BSC();
                bsc.setVisible(true);
                dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Login incorrecto. Vuelve a intentarlo" , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
            }
        
    }
    
    public static String encriptaPass(String contraseña) {
        StringBuilder sb = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(contraseña.getBytes());

            sb = new StringBuilder();
            for (byte b: hash) {
                sb.append(String.format("%02x", b));
            }
        }catch(NoSuchAlgorithmException nsae){}
        return sb.toString();
    }
    
    
    public static boolean compruebaUsuarioPass(String usuario, String pass){
        String contra = "";
        boolean valido = false;
        String instruccionSQL = "SELECT password FROM usuarios WHERE usuario = ?"; //sentencia SQL
        try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)){
            pstmt.setString(1, usuario);
            ResultSet ejecucion = pstmt.executeQuery();
            while (ejecucion.next()){
                contra = ejecucion.getString(1);
            }  
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al comprobar la contraseña: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        if (contra.equals(pass)){
            valido = true;
        }
        return valido;
    }
    
    
    // Obtener la contraseña almacenada en la base de datos para el usuarioIngresado
    // Comparar contraseñaEncriptadaIngresada con la contraseña almacenada

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviar;
    private javax.swing.JLabel imagen;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField txtPass;
    private static javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
