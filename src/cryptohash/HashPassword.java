/* Daniel Eggers 
 * University of Missouri-St. Louis
 * CS3780 Software Security
 * Professor Mark Hauschild
 * Project 2 - Password and Authentication
 * December 12, 2018
 * */
package cryptohash;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
/* supports MD5, SHA-1, and SHA-256
 * https://docs.oracle.com/javase/7/docs/api/java/security/MessageDigest.html */

public class HashPassword extends SSCrypto {
	
	public static String hashPassword(String password, String algorithm) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.reset(); //
		md.update(password.getBytes());  //digest password output in bytes format
		byte[] hash = md.digest();  //complete the digest
	
		StringBuffer sb = new StringBuffer(); //prepare to make hex string format	
		for(byte b1 : hash) {  //for-each element in array
			sb.append(Integer.toHexString(b1 & 0xff).toString()); //bit-wise operation turning int to hex string
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
	/* This method used only for testing.  Real main() is in SSCrypto class */
		String password = "default";
		String algorithm = "MD5";
		System.out.println(password);
		try {
			System.out.println(hashPassword(password, algorithm));
		}
		catch(NoSuchAlgorithmException e) {
			System.out.println(e);
		}
	}
}
