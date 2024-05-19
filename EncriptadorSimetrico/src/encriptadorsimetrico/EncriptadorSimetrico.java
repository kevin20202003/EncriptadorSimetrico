package encriptadorsimetrico;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EncriptadorSimetrico extends JFrame {

    // Componentes de la interfaz gráfica
    private JTextField textFieldCadena;     // Campo de texto para la cadena a encriptar
    private JTextField textFieldLlave;      // Campo de texto para la llave de encriptación
    private JTextArea textAreaResultado;    // Área de texto para mostrar el resultado
    private JButton btnEncriptar;           // Botón para encriptar
    private JButton btnDesencriptar;        // Botón para desencriptar

    public EncriptadorSimetrico() {
        // Configuración básica de la ventana
        setTitle("Encriptador Simétrico");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());     // Usar GridBagLayout para un diseño flexible
        
        // Inicializar componentes
        textFieldCadena = new JTextField(20);
        textFieldLlave = new JTextField(20);
        textAreaResultado = new JTextArea(5, 20);
        textAreaResultado.setEditable(false);
        btnEncriptar = new JButton("Encriptar");
        btnDesencriptar = new JButton("Desencriptar");

        // Configuración de GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Añadir y organizar componentes en la ventana
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Cadena a encriptar:"), gbc);
        gbc.gridx = 1;
        add(textFieldCadena, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Llave de encriptación:"), gbc);
        gbc.gridx = 1;
        add(textFieldLlave, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(btnEncriptar, gbc);
        gbc.gridx = 1;
        add(btnDesencriptar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;                  // Abarcar dos columnas
        gbc.fill = GridBagConstraints.BOTH; // Llenar todo el espacio disponible
        add(new JScrollPane(textAreaResultado), gbc);

        // Listener para el botón Encriptar
        btnEncriptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cadena = textFieldCadena.getText();
                String llave = textFieldLlave.getText();
                String encriptada = Encriptar(cadena, llave);
                textAreaResultado.setText("Encriptada: " + encriptada);
            }
        });

        // Listener para el botón Desencriptar
        btnDesencriptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cadenaEncriptada = textFieldCadena.getText();
                String llave = textFieldLlave.getText();
                String desencriptada = Desencriptar(cadenaEncriptada, llave);
                textAreaResultado.setText("Desencriptada: " + desencriptada);
            }
        });
    }

    // Método para crear la clave de encriptación/desencriptación a partir de la llave ingresada
    public SecretKeySpec CrearCalve(String llave) {
        try {
            byte[] cadena = llave.getBytes("UTF-8");    // Convertir la llave a bytes en formato UTF-8
            MessageDigest md = MessageDigest.getInstance("SHA-1"); // Crear un MessageDigest para SHA-1
            cadena = md.digest(cadena);                 // Obtener el hash de la llave
            cadena = Arrays.copyOf(cadena, 16);         // Tomar los primeros 16 bytes del hash para la clave AES
            SecretKeySpec secretKeySpec = new SecretKeySpec(cadena, "AES"); // Crear la clave AES
            return secretKeySpec;
        } catch (Exception e) {
            return null;
        }
    }

    // Método para encriptar una cadena
    public String Encriptar(String encriptar, String llave) {
        try {
            SecretKeySpec secretKeySpec = CrearCalve(llave); // Crear la clave de encriptación
            Cipher cipher = Cipher.getInstance("AES");       // Obtener una instancia de Cipher para AES
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec); // Inicializar el cipher en modo encriptación
            byte[] cadena = encriptar.getBytes("UTF-8");     // Convertir la cadena a bytes en formato UTF-8
            byte[] encriptada = cipher.doFinal(cadena);      // Encriptar los bytes de la cadena
            String cadena_encriptada = Base64.getEncoder().encodeToString(encriptada); // Codificar los bytes encriptados en Base64
            return cadena_encriptada;
        } catch (Exception e) {
            return "";
        }
    }

    // Método para desencriptar una cadena
    public String Desencriptar(String desencriptar, String llave) {
        try {
            SecretKeySpec secretKeySpec = CrearCalve(llave); // Crear la clave de desencriptación
            Cipher cipher = Cipher.getInstance("AES");       // Obtener una instancia de Cipher para AES
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec); // Inicializar el cipher en modo desencriptación
            byte[] cadena = Base64.getDecoder().decode(desencriptar); // Decodificar la cadena en Base64 a bytes
            byte[] desencriptacion = cipher.doFinal(cadena); // Desencriptar los bytes
            String cadena_desencriptada = new String(desencriptacion); // Convertir los bytes desencriptados a cadena
            return cadena_desencriptada;
        } catch (Exception e) {
            return "";
        }
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EncriptadorSimetrico().setVisible(true); // Crear y mostrar la ventana principal
            }
        });
    }
}
