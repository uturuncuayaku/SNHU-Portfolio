package com.snhu.hash;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public final class HashController {
	
	@GetMapping("/hash")
	public String hashPage() throws NoSuchAlgorithmException {
		
		Hash myHash;
		String name = "Andres Trujillo";
		String cipher = "SHA3-256";
		
	    LocalDateTime myDateObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
	    String formattedDate = myDateObj.format(myFormatObj);
	    
	    myHash = new Hash(name, cipher);
	    if(myHash.getHash() == null) {
	    	System.err.println("Error!");
	    }
		
        return "<img src=\"https://upload.wikimedia.org/wikipedia/en/a/ac/Southern_New_Hampshire_University_seal.svg\">"+
		"<p>data:" + name + "</p>"+
		"<p>Cipher Algorithm: " + cipher + "</p>" + 
		"<p>Checksum Value: " + myHash.getHash() + "</p>" +
		"<p>Date: " + formattedDate + "</p>";
	}

	
	protected static class Hash {
		
		private final String hash;
		
	    public Hash(String data, String cipher) {
	        this.hash = generateHash(data, cipher);
	    }
		
	    private String generateHash(String data, String cipher) {
	    	MessageDigest myDigest;
	        try {
	            myDigest = MessageDigest.getInstance(cipher);

	        } catch (NoSuchAlgorithmException nsae) {
	            System.err.printf("No such algorithm error: %s%n", nsae.getMessage());
	            return null;
	        }
	        
	        byte[] hashBytes = myDigest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
	    }		
		
		public final String getHash() { return hash; }
		
	}

}
