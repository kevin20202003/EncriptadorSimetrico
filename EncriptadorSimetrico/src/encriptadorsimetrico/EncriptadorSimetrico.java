package encriptadorsimetrico;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

public class EncriptadorSimetrico {
    
    // La llave para encriptar/desencriptar será introducida por el usuario
    String LLAVE;

    public static void main(String[] args) {
        String encriptada = "";
        String aEnccriptar = "";
        
        // Crear una instancia de la clase EncriptadorSimetrico
        EncriptadorSimetrico main = new EncriptadorSimetrico();
        
        // Solicitar al usuario la cadena a encriptar
        aEnccriptar = JOptionPane.showInputDialog("Ingresa la cadena a encriptar: ");
        
        // Solicitar al usuario la llave de encriptación
        main.LLAVE = JOptionPane.showInputDialog("Ingresa la llave de encriptación: ");
        
        // Encriptar la cadena ingresada
        encriptada = main.Encriptar(aEnccriptar);
        
        // Mostrar la cadena encriptada
        JOptionPane.showMessageDialog(null, encriptada);
        
        // Desencriptar la cadena encriptada y mostrarla
        JOptionPane.showMessageDialog(null, main.Desencriptar(encriptada));
    }

    // Método para crear la clave de encriptación/desencriptación a partir de la llave ingresada
    public SecretKeySpec CrearCalve(String llave) {
        try {
            // Convertir la llave a un arreglo de bytes en formato UTF-8
            byte[] cadena = llave.getBytes("UTF-8");
            
            // Crear un MessageDigest para SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            
            // Obtener el hash de la llave
            cadena = md.digest(cadena);
            
            // Tomar los primeros 16 bytes del hash para crear la clave AES
            cadena = Arrays.copyOf(cadena, 16);
            
            // Crear y retornar el SecretKeySpec para AES
            SecretKeySpec secretKeySpec = new SecretKeySpec(cadena, "AES");
            return secretKeySpec;
        } catch (Exception e) {
            return null;
        }
    }

    // Método para encriptar una cadena
    public String Encriptar(String encriptar) {
        try {
            // Crear la clave de encriptación
            SecretKeySpec secretKeySpec = CrearCalve(LLAVE);
            
            // Obtener una instancia de Cipher para AES
            Cipher cipher = Cipher.getInstance("AES");
            
            // Inicializar el cipher en modo de encriptación con la clave
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            
            // Convertir la cadena a bytes en formato UTF-8
            byte[] cadena = encriptar.getBytes("UTF-8");
            
            // Encriptar los bytes de la cadena
            byte[] encriptada = cipher.doFinal(cadena);
            
            // Codificar los bytes encriptados en Base64 y retornar como cadena
            String cadena_encriptada = Base64.getEncoder().encodeToString(encriptada);
            return cadena_encriptada;
        } catch (Exception e) {
            return "";
        }
    }

    // Método para desencriptar una cadena
    public String Desencriptar(String desencriptar) {
        try {
            // Crear la clave de desencriptación
            SecretKeySpec secretKeySpec = CrearCalve(LLAVE);
            
            // Obtener una instancia de Cipher para AES
            Cipher cipher = Cipher.getInstance("AES");
            
            // Inicializar el cipher en modo de desencriptación con la clave
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            
            // Decodificar la cadena en Base64 a bytes
            byte[] cadena = Base64.getDecoder().decode(desencriptar);
            
            // Desencriptar los bytes
            byte[] desencriptacion = cipher.doFinal(cadena);
            
            // Convertir los bytes desencriptados a cadena y retornar
            String cadena_desencriptada = new String(desencriptacion);
            return cadena_desencriptada;
        } catch (Exception e) {
            return "";
        }
    }
}
