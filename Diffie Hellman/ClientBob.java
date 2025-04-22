import java.io.*;
import java.net.*;

public class ClientBob {
    // Public parameters for Diffie-Hellman
    private static final int PRIME = 23;  // Prime modulus p
    private static final int BASE = 5;    // Generator g

    public static void main(String[] args) {
        try {
            // Connect to Eve (who Bob thinks is directly connected to Alice)
            Socket socket = new Socket("localhost", 5555);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            System.out.println("=== Bob ===");
            System.out.println("Parameters: Prime = " + PRIME + ", Base = " + BASE);
            
            // Step 1: Generate Bob's private key (b)
            int privateKey = 15; // For simplicity and memorization, using a fixed key
            System.out.println("Bob's private key: " + privateKey);
            
            // Step 2: Calculate Bob's public key (B = g^b mod p)
            int publicKey = modPow(BASE, privateKey, PRIME);
            System.out.println("Bob's public key: " + publicKey);
            
            // Step 3: Send Bob's public key to "Alice" (actually Eve)
            out.println(publicKey);
            System.out.println("Sent public key to Alice");
            
            // Step 4: Receive "Alice's" public key (actually Eve's)
            int receivedKey = Integer.parseInt(in.readLine());
            System.out.println("Received public key from Alice: " + receivedKey);
            
            // Step 5: Calculate shared secret (receivedKey^privateKey mod p)
            int sharedSecret = modPow(receivedKey, privateKey, PRIME);
            System.out.println("Calculated shared secret: " + sharedSecret);
            
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Helper method for modular exponentiation (b^e mod m)
    private static int modPow(int base, int exponent, int modulus) {
        int result = 1;
        base = base % modulus;
        
        while (exponent > 0) {
            // If exponent is odd, multiply result with base
            if (exponent % 2 == 1) {
                result = (result * base) % modulus;
            }
            // Exponent is halved
            exponent = exponent >> 1;
            // Base is squared
            base = (base * base) % modulus;
        }
        
        return result;
    }
}