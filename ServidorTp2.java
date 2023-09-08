import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;

public class Servidor {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12345); // Porta do servidor

        while (true) {
            System.out.println("Aguardando conexão do cliente...");
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            // Receba a mensagem cifrada do cliente
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            byte[] encryptedMessage = (byte[]) in.readObject();
            byte[] keyBytes = (byte[]) in.readObject();

            // Recupere a chave secreta do cliente
            SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

            // Crie um objeto de cifra AES e inicialize-o no modo de descriptografia
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decifre a mensagem
            byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

            // Exiba a mensagem criptografada e a mensagem decifrada
            String encryptedMessageString = Base64.getEncoder().encodeToString(encryptedMessage);
            String originalMessage = new String(decryptedMessage);
            System.out.println("Mensagem criptografada recebida: " + encryptedMessageString);
            System.out.println("Mensagem decifrada: " + originalMessage);

            // Gravar a mensagem criptografada e a mensagem decifrada em um arquivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("mensagem.txt"))) {
                writer.write("Mensagem criptografada: " + encryptedMessageString + "\n");
                writer.write("Mensagem decifrada: " + originalMessage);
            }

            socket.close();
        }
    }
}
