import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;

public class Servidor{
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12345); // Porta do servidor

        while (true) {
            System.out.println("Aguardando conexão do cliente...");
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            // Gere uma chave de criptografia simétrica (exemplo com AES)
            SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

            // Envie a chave pública ao cliente
            ObjectOutputStream keyOut = new ObjectOutputStream(socket.getOutputStream());
            keyOut.writeObject(secretKey);

            // Receba a mensagem criptografada do cliente
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            byte[] encryptedMessage = (byte[]) in.readObject();

            // Decifre a mensagem
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

            // Exiba a mensagem criptografada e a mensagem decifrada
            System.out.println("Mensagem criptografada recebida do cliente: " + Base64.getEncoder().encodeToString(encryptedMessage));
            System.out.println("Mensagem decifrada: " + new String(decryptedMessage));

            socket.close();
        }
    }
}
