import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;

public class Cliente {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 12345); // Endereço e porta do servidor

        // Receba a chave pública do servidor
        ObjectInputStream keyIn = new ObjectInputStream(socket.getInputStream());
        SecretKey secretKey = (SecretKey) keyIn.readObject();

        // Leia a mensagem do teclado
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Digite a mensagem a ser enviada: ");
        String message = reader.readLine();

        // Criptografe a mensagem
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());

        // Envie a mensagem criptografada ao servidor
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(encryptedMessage);

        // Exiba a mensagem original e a mensagem criptografada
        System.out.println("Mensagem original: " + message);
        System.out.println("Mensagem criptografada enviada ao servidor: " + Base64.getEncoder().encodeToString(encryptedMessage));

        socket.close();
    }
}
