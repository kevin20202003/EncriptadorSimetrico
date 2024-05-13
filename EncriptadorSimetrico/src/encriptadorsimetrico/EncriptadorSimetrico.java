/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encriptadorsimetrico;

/*
 * Este es un programa de ejemplo que implementa un encriptador y desencriptador simétrico utilizando el algoritmo AES.
 * Proporciona una interfaz gráfica de usuario para ingresar texto, una clave y realizar operaciones de encriptación y desencriptación.
 */

// Importaciones de clases necesarias
import javax.swing.*;
import java.awt.event.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// Definición de la clase principal que extiende JFrame (ventana)
public class EncriptadorSimetrico extends JFrame {
    // Declaración de componentes de la interfaz de usuario
    private JTextField claveTextField;
    private JTextArea textoEntradaTextArea;
    private JTextArea textoSalidaTextArea;
    private JButton encriptarButton;
    private JButton desencriptarButton;

    // Constantes para el algoritmo de encriptación
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;

    // Constructor de la clase
    public EncriptadorSimetrico() {
        initComponents(); // Método para inicializar los componentes de la interfaz
    }

    // Método para inicializar los componentes de la interfaz de usuario
    private void initComponents() {
        // Creación de los componentes
        claveTextField = new JTextField();
        textoEntradaTextArea = new JTextArea();
        textoSalidaTextArea = new JTextArea();
        encriptarButton = new JButton("Encriptar");
        desencriptarButton = new JButton("Desencriptar");

        // Configuración de la ventana principal
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Encriptador y Desencriptador");

        // Creación de paneles con barras de desplazamiento para las áreas de texto
        JScrollPane scrollPaneEntrada = new JScrollPane();
        JScrollPane scrollPaneSalida = new JScrollPane();

        // Configuración de los paneles con las áreas de texto
        scrollPaneEntrada.setViewportView(textoEntradaTextArea);
        scrollPaneSalida.setViewportView(textoSalidaTextArea);

        // Adición de acciones a los botones de encriptar y desencriptar
        encriptarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                encriptarButtonActionPerformed(evt);
            }
        });

        desencriptarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                desencriptarButtonActionPerformed(evt);
            }
        });

        // Configuración del diseño de la ventana utilizando GroupLayout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneEntrada)
                    .addComponent(scrollPaneSalida)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(claveTextField, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(encriptarButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(desencriptarButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(claveTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(encriptarButton)
                    .addComponent(desencriptarButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneEntrada, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneSalida, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        // Ajuste automático del tamaño de la ventana
        pack();
    }

    // Método para la acción del botón de encriptar
    private void encriptarButtonActionPerformed(ActionEvent evt) {
        String textoEntrada = textoEntradaTextArea.getText();
        String clave = claveTextField.getText();
        try {
            String textoEncriptado = encrypt(textoEntrada, clave);
            textoSalidaTextArea.setText(textoEncriptado);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al encriptar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para la acción del botón de desencriptar
    private void desencriptarButtonActionPerformed(ActionEvent evt) {
        String textoEncriptado = textoEntradaTextArea.getText();
        String clave = claveTextField.getText();
        try {
            String textoDesencriptado = decrypt(textoEncriptado, clave);
            textoSalidaTextArea.setText(textoDesencriptado);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al desencriptar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para encriptar el texto utilizando AES
    private String encrypt(String plainText, String key) throws Exception {
        Key secretKey = generateKey(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Método para desencriptar el texto utilizando AES
    private String decrypt(String encryptedText, String key) throws Exception {
        Key secretKey = generateKey(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    // Método para generar una clave utilizando la clave proporcionada por el usuario
    private Key generateKey(String key) throws Exception {
        byte[] keyValue = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyValue, ALGORITHM);
        return secretKeySpec;
    }

    // Método principal para iniciar la aplicación
    public static void main(String args[]) {
        // Se ejecuta la aplicación en el hilo de eventos de la interfaz de usuario
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EncriptadorSimetrico().setVisible(true);
            }
        });
    }
}

