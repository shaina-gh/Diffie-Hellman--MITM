import java.io.*;
import java.net.*;

public class ClientAlice {
    // Public parameters for Diffie-Hellman
    private static final int PRIME = 23;  // Prime modulus p
    private static final int BASE = 5;    // Generator g

    public static void main(String[] args) {
        try {
            // Connect to Eve (who Alice thinks is directly connected to Bob)
            Socket socket = new Socket("localhost", 5555);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            System.out.println("=== Alice ===");
            System.out.println("Parameters: Prime = " + PRIME + ", Base = " + BASE);
            
            // Step 1: Generate Alice's private key (a)
            int privateKey = 6; // For simplicity and memorization, using a fixed key
            System.out.println("Alice's private key: " + privateKey);
            
            // Step 2: Calculate Alice's public key (A = g^a mod p)
            int publicKey = modPow(BASE, privateKey, PRIME);
            System.out.println("Alice's public key: " + publicKey);
            
            // Step 3: Send Alice's public key to "Bob" (actually Eve)
            out.println(publicKey);
            System.out.println("Sent public key to Bob");
            
            // Step 4: Receive "Bob's" public key (actually Eve's)
            int receivedKey = Integer.parseInt(in.readLine());
            System.out.println("Received public key from Bob: " + receivedKey);
            
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

/*
 * javac ClientAlice.java ClientBob.java MitmEve.java
 * java MitmEve # Terminal 1
 * java ClientAlice # Terminal 2
 * java ClientBob # Terminal 3
 */