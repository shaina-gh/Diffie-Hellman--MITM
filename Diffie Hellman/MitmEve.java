import java.io.*;
import java.net.*;

public class MitmEve {
    // Public parameters for Diffie-Hellman
    private static final int PRIME = 23;  // Prime modulus p
    private static final int BASE = 5;    // Generator g

    public static void main(String[] args) {
        try {
            // Create server socket
            ServerSocket serverSocket = new ServerSocket(5555);
            System.out.println("=== Eve (MITM) ===");
            System.out.println("Parameters: Prime = " + PRIME + ", Base = " + BASE);
            System.out.println("Waiting for connections...");
            
            // Step 1: Generate Eve's private key (e)
            int privateKey = 12; // For simplicity and memorization, using a fixed key
            System.out.println("Eve's private key: " + privateKey);
            
            // Step 2: Calculate Eve's public key (E = g^e mod p)
            int publicKey = modPow(BASE, privateKey, PRIME);
            System.out.println("Eve's public key: " + publicKey);
            
            // Step 3: Accept connection from Alice
            Socket aliceSocket = serverSocket.accept();
            System.out.println("Alice connected");
            
            // Step 4: Accept connection from Bob
            Socket bobSocket = serverSocket.accept();
            System.out.println("Bob connected");
            
            // Setup I/O streams for both connections
            BufferedReader aliceIn = new BufferedReader(new InputStreamReader(aliceSocket.getInputStream()));
            PrintWriter aliceOut = new PrintWriter(aliceSocket.getOutputStream(), true);
            
            BufferedReader bobIn = new BufferedReader(new InputStreamReader(bobSocket.getInputStream()));
            PrintWriter bobOut = new PrintWriter(bobSocket.getOutputStream(), true);
            
            // Step 5: Intercept Alice's public key
            int alicePublicKey = Integer.parseInt(aliceIn.readLine());
            System.out.println("Intercepted Alice's public key: " + alicePublicKey);
            
            // Step 6: Intercept Bob's public key
            int bobPublicKey = Integer.parseInt(bobIn.readLine());
            System.out.println("Intercepted Bob's public key: " + bobPublicKey);
            
            // Step 7: Send Eve's public key to both Alice and Bob
            aliceOut.println(publicKey);
            bobOut.println(publicKey);
            System.out.println("Sent Eve's public key to both Alice and Bob");
            
            // Step 8: Calculate shared secret with Alice (alicePublicKey^privateKey mod p)
            int aliceShared = modPow(alicePublicKey, privateKey, PRIME);
            System.out.println("Shared secret with Alice: " + aliceShared);
            
            // Step 9: Calculate shared secret with Bob (bobPublicKey^privateKey mod p)
            int bobShared = modPow(bobPublicKey, privateKey, PRIME);
            System.out.println("Shared secret with Bob: " + bobShared);
            
            // Close all connections
            aliceSocket.close();
            bobSocket.close();
            serverSocket.close();
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
    

