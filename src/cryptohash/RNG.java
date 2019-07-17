/* Daniel Eggers 
 * University of Missouri-St. Louis
 * CS3780 Software Security
 * Professor Mark Hauschild
 * Project 2 - Password and Authentication
 * December 12, 2018
 * */

package cryptohash;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class RNG extends SSCrypto {
	
	public static int getRandomNumberInRange(int min, int max) {
		// TODO Auto-generated method stub
		if (min > max) {
			throw new IllegalArgumentException("Upper bound less than lower bound.");
		}

		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	
	public static String getRandomString(int lengthOfNewString) {
		//generate a single random string of specified length; [a..z][0..9]	
		
		char alphanumeric[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
							   'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
							   '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		
		String rstring = "";
		for (int i=0; i<lengthOfNewString; i++) {		
			rstring = ( rstring + alphanumeric[new Random().nextInt(alphanumeric.length)] );
			//concatenate characters randomly chosen from alpha array to build password
		}
		
		return rstring;
	}
	
	
	public static byte getRandomSalt() {
		//generate a single random byte
		byte[] b = new byte[1];
		new Random().nextBytes(b);
		return b[0];
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/* This method used only for testing.  Real main() is in SSCrypto class */
		
		String str = "abc";
		str += getRandomSalt();
		System.out.println(str);
		
		try {
			System.out.println( HashPassword.hashPassword(str, "MD5") );
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println();
		//prints 10 random passwords
		for (int i = 0; i<10; i++) {
			System.out.println(getRandomString( getRandomNumberInRange(3,8) )); 
		}
	}
}
