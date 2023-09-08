import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;

public class Cliente {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 12345); // Endereço e porta do servidor

        // Leia a mensagem do teclado
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Digite a mensagem a ser enviada: ");
        String message = reader.readLine();

        // Gere uma chave de criptografia simétrica (exemplo com AES)
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

        // Crie um objeto de cifra AES e inicialize-o no modo de criptografia
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Criptografe a mensagem
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());

        // Exiba a mensagem original e a mensagem criptografada
        System.out.println("Mensagem original: " + message);
        System.out.println("Mensagem criptografada: " + Base64.getEncoder().encodeToString(encryptedMessage));

        // Envie a mensagem cifrada ao servidor e a chave cifrada
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(encryptedMessage);
        out.writeObject(secretKey.getEncoded());

        socket.close();
    }
}
