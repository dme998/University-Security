/* Daniel Eggers 
 * University of Missouri-St. Louis
 * CS3780 Software Security
 * Professor Mark Hauschild
 * Project 2 - Password and Authentication
 * December 12, 2018
 * */

package cryptohash;

import java.util.Scanner;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter; /* http://www.tutorialspoint.com/java/java_filewriter_class.htm */
import java.io.IOException;

public class SSCrypto {

	public static final Scanner input = new Scanner(System.in);
	private static Scanner f;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		checkFiles();
		menu();
		exitProgram();
	}
	
	
	public static boolean authenticate(String username, String password, String filepath, int loginMethod) {
		/* Method returns TRUE if username & password combo is authenticated */
		println("    You entered username: " + username);
		println("    You entered password: " + password);
		boolean match = false; //true if username-password match was found
		String tempUsername = "";
		String tempPassword = "";
		//String hashPassword = "";
		
		if (loginMethod == 2) { //hash the password if using Type2 login
			try {
				password = HashPassword.hashPassword(password, "MD5");
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (loginMethod == 3) {
			/* get salt */
			String tempSalt;
			String SaltHashPassword = "";
			try {
				f = new Scanner(new File(filepath));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			f.useDelimiter("[,\n]");
			
			/* Read file sequentially and compare read-in username & password with what the user entered */
			while(f.hasNext() && (match == false)) {
				tempUsername = f.next();
				tempSalt = f.next();
				tempPassword = f.next();
				
				if ( tempUsername.trim().equals(username.trim()) ) {					
					try {
						SaltHashPassword = HashPassword.hashPassword(password + tempSalt, "MD5");
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if ( SaltHashPassword.trim().equals(tempPassword.trim()) ) {
						match = true;
						//println("MATCH SET TRUE");
						//System.out.println(SaltHashPassword);
						//System.out.println(tempPassword);
						//System.out.println(tempSalt);
						//println("-endofmatch-");
					}
					else {
						match = false;
						//println("MATCH SET FALSE");
						//println("Salt value:       " + tempSalt);
						//println("Correct Password: " + tempPassword.trim());
						//println("You entered:      " + SaltHashPassword.trim());
					}
					//System.out.println("Returning " + match);
					//System.out.println(SaltHashPassword);
					//System.out.println(tempPassword);
					//System.out.println(tempSalt);
					return match;
				}
			}
		}
		
		/* compare matches if using loginMethod 1 or 2 */
		if (loginMethod == 1 || loginMethod == 2) {
			try {
				f = new Scanner(new File(filepath)); //read from password file
				f.useDelimiter("[,\n]");
				
				/* Read file sequentially and compare read-in username & password with what the user entered */
				while(f.hasNext() && (match == false)) {
					tempUsername = f.next();
					tempPassword = f.next();
					
					/* compare user-entered data with temporary data read from file */
					if ( tempUsername.trim().equals(username.trim()) && tempPassword.trim().equals(password.trim()) ) {
						match = true;
						//println("    Authenticated.  Press ENTER to continue.");
						//pressToContinue();
					}
				}
				f.close();
				/*if (match == false) {
					println("    Failed to authenticate.  Press ENTER to continue.");
					pressToContinue();
				}*/
			} catch(Exception e) {
				println("Error with authentication method.");
				exitProgram();
			}
		}
		
		return match;
	}

	
	private static void createAccount(String usernameMake, String passwordMake) {
		// Create a single account in all 3 password files, appending them
		String passwordPlain = passwordMake;
		String passwordHash = passwordMake;
		String passwordSaltHash = passwordMake;
				
		/* CREATE ACCOUNT IN TYPE1 */
		//print("Creating account in PlainText file: ");
		try {
			File file1 = new File("type1.txt");
			if (file1.exists()) {
				BufferedWriter fw1 = new BufferedWriter(new FileWriter(file1, true));
				print("Writing to " + file1 + "... ");
				fw1.write(usernameMake + "," + passwordPlain);
				fw1.newLine();
				println(" Done.");
				fw1.close();
			}
			else {
				println("File not found.");
				print("Creating file " + file1 + "... ");
				file1.createNewFile();
				BufferedWriter fw1 = new BufferedWriter(new FileWriter(file1, false));
				println("Initialized");
				print("Writing to " + file1 + "... ");
				fw1.write(usernameMake + "," + passwordPlain);
				println(" Done.");
				fw1.close();
			}
		}catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* CREATE ACCOUNT IN TYPE2 */
		//print("Creating account in Hash file: ");
		try {
			File file2 = new File("type2.txt");
			if (file2.exists()) {
				BufferedWriter fw2 = new BufferedWriter(new FileWriter(file2, true));
				print("Writing to " + file2 + "... ");
				
				try {
					passwordHash = HashPassword.hashPassword(passwordMake, "MD5");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					System.out.println(e);
					e.printStackTrace();
				}
				
				fw2.write(usernameMake + "," + passwordHash);
				fw2.newLine();
				println(" Done.");
				fw2.close();
			}
			else {
				println("File not found.");
				print("Creating file " + file2 + "... ");
				file2.createNewFile();
				BufferedWriter fw2 = new BufferedWriter(new FileWriter(file2, false));
				println("Initialized");
				print("Writing to " + file2 + "... ");
				
				try {
					passwordHash = HashPassword.hashPassword(passwordMake, "MD5");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					System.out.println(e);
					e.printStackTrace();
				}
				fw2.write(usernameMake + "," + passwordHash);
				fw2.newLine();
				println(" Done.");
				fw2.close();
			}
		}catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/* CREATE ACCOUNT IN TYPE3 */
		//print("Creating account in Salt Hash file: ");
		try {
			byte salt = RNG.getRandomSalt();
			File file3 = new File("type3.txt");
			if (file3.exists()) {
				BufferedWriter fw3 = new BufferedWriter(new FileWriter(file3, true));
				print("Writing to " + file3 + "... ");
				
				try {
					passwordSaltHash = HashPassword.hashPassword(passwordMake + salt, "MD5");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					System.out.println(e);
					e.printStackTrace();
				}
				
				fw3.write(usernameMake + "," + salt + "," + passwordSaltHash);
				fw3.newLine();
				println(" Done.");
				fw3.close();
			}
			else {
				println("File not found.");
				print("Creating file " + file3 + "... ");
				file3.createNewFile();
				BufferedWriter fw3 = new BufferedWriter(new FileWriter(file3, false));
				println("Initialized");
				print("Writing to " + file3 + "... ");
				
				try {
					passwordSaltHash = HashPassword.hashPassword(passwordMake + salt, "MD5");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					System.out.println(e);
					e.printStackTrace();
				}
				
				fw3.write(usernameMake + "," + passwordSaltHash + "," + salt);
				println(" Done.");
				fw3.close();
			}
		}catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}


	public static void displayMenu(int arg0) {
		if (arg0 == 1) {
			println("MAIN MENU:"
					+ "\n[0] Exit"
					+ "\n[1] Login"
					+ "\n[2] Create Account"
					+ "\n[3] Generate Test Accounts"
					+ "\n[4] Password File Cracking Tool"
					+ "\n[5] Flush Password Files"
			);
		}
		else if (arg0 == 2) {
			println("    PASSWORD FILE CRACKING TOOL (SUBMENU):"
					+ "\n    [0] .."
					+ "\n    [1] Crack Type1: PlainText File"
					+ "\n    [2] Crack Type2: Hashes File"
					+ "\n    [3] Crack Type3: Salted Hashes File"
			);
		}
		else if (arg0 == 3) {
			println("    LOGIN (SUBMENU):"
					+ "\n    [0] .."
					+ "\n    [1] Login using PlainText method"
					+ "\n    [2] Login using Hash method"
					+ "\n    [3] Login using Salted Hash method"
					);
		}
	}
	
	
	public static void generateAccounts(int min, int max, int numberOfAccounts) {
		//generate n number of accounts: min 1, max 100
		
		if (numberOfAccounts > 100) {
			numberOfAccounts = 100;
		}
		else if (numberOfAccounts < 1) {
			numberOfAccounts = 1;
		}
		
		String usernameMake, passwordMake;
		int lengthOfPassword;
		int lengthOfUsername = 8; //usernames restricted to 8 characters
		
		for (int i=0; i<numberOfAccounts; i++) {
			usernameMake = RNG.getRandomString(lengthOfUsername);
			lengthOfPassword = RNG.getRandomNumberInRange(min, max);
			passwordMake = RNG.getRandomString(lengthOfPassword);
			println( "Making account (" + (i+1) + "/" + numberOfAccounts + "): ");
			createAccount(usernameMake, passwordMake);
		}
	}


	private static void checkFiles() {
		/* Check if password files exist, create them it not */
		println("Checking for password files...");
		try {	
			/* File 1 */
			print("Checking for file type1... ");
			File file1 = new File("type1.txt");
			if (file1.exists()) {
				println("Found.");
				BufferedWriter pw1 = new BufferedWriter(new FileWriter(file1, true));
				//write using .write()
				pw1.close();
			}
			else {
				println("File not found.");
				print("Creating file type1... ");
				file1.createNewFile();
				BufferedWriter pw1 = new BufferedWriter(new FileWriter(file1, false));
				//pw1.write("<USERNAME>" + "," + "<PASSWORD>");
				//pw1.newLine();
				println("Initialized.");
				pw1.close();
			}
			
			/* File 2 */
			print("Checking for file type2... ");
			File file2 = new File("type2.txt");
			if (file2.exists()) {
				println("Found.");
				BufferedWriter pw2 = new BufferedWriter(new FileWriter(file2, true));
				//write using .write()
				pw2.close();
			}
			else {
				println("File not found.");
				print("Creating file type2... ");
				file2.createNewFile();
				BufferedWriter pw2 = new BufferedWriter(new FileWriter(file2, false));
				//pw2.write("<USERNAME>" + "," + "<PASSWORD>");
				//pw2.newLine();
				println("Initialized.");
				pw2.close();
			}
			
			/* File 3 */
			print("Checking for file type3... ");
			File file3 = new File("type3.txt");
			if (file3.exists()) {
				println("Found.");
				BufferedWriter pw3 = new BufferedWriter(new FileWriter(file3, true));
				//write using .write()
				pw3.close();
			} 
			else {
				println("File not found.");
				print("Creating file type2... ");
				file3.createNewFile();
				BufferedWriter pw3 = new BufferedWriter(new FileWriter(file3, false));
				//pw3.write("<USERNAME>" + "," + "<PASSWORD>");
				//pw3.newLine();
				println("Initialized.");
				pw3.close();
			}
		}catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}


	private static void flushFiles() {
		/* reinitialize all password files */
		println("Checking for password files...");
		try {	
			/* File 1 */
			print("Checking for file type1... ");
			File file1 = new File("type1.txt");
			if (file1.exists()) {
				print("Initializing... ");
				BufferedWriter pw1 = new BufferedWriter(new FileWriter(file1, false));
				//write using .write()
				println("Done.");
				pw1.close();
			}
			else {
				println("File not found.");
				print("Creating file type1... ");
				file1.createNewFile();
				BufferedWriter pw1 = new BufferedWriter(new FileWriter(file1, false));
				//pw1.write("<USERNAME>" + "," + "<PASSWORD>");
				//pw1.newLine();
				println("Initialized.");
				pw1.close();
			}
			
			/* File 2 */
			print("Checking for file type2... ");
			File file2 = new File("type2.txt");
			if (file2.exists()) {
				print("Initializing... ");
				BufferedWriter pw2 = new BufferedWriter(new FileWriter(file2, false));
				//write using .write()
				println("Done.");
				pw2.close();
			}
			else {
				println("File not found.");
				print("Creating file type2... ");
				file2.createNewFile();
				BufferedWriter pw2 = new BufferedWriter(new FileWriter(file2, false));
				//pw2.write("<USERNAME>" + "," + "<PASSWORD>");
				//pw2.newLine();
				println("Initialized.");
				pw2.close();
			}
			
			/* File 3 */
			print("Checking for file type3... ");
			File file3 = new File("type3.txt");
			if (file3.exists()) {
				print("Initializing... ");
				BufferedWriter pw3 = new BufferedWriter(new FileWriter(file3, true));
				//write using .write()
				println("Done.");
				pw3.close();
			} 
			else {
				println("File not found.");
				print("Creating file type2... ");
				file3.createNewFile();
				BufferedWriter pw3 = new BufferedWriter(new FileWriter(file3, false));
				//pw3.write("<USERNAME>" + "," + "<PASSWORD>");
				//pw3.newLine();
				println("Initialized.");
				pw3.close();
			}
		}catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public static void login(int loginMethod) {
		//Scanner input = new Scanner(System.in); //Commented out because Scanner has now been raised in scope
		String filename = "";
		switch(loginMethod) {
			case 1: filename = "type1.txt"; break;
			case 2: filename = "type2.txt"; break;
			case 3: filename = "type3.txt"; break;
			default: println("Invalid login method"); break;
		}
		
		print("    Enter username $>");
		String username = input.next();
		print("    Enter password $>");
		String password = input.next();
		boolean match = false;
		match = authenticate(username, password, filename, loginMethod);
		
		if (match == true) {
		println("    Authenticated.  Press ENTER to continue.");
		pressToContinue();
		} 
		else {
			println("    Failed to authenticate.  Press ENTER to continue.");
			pressToContinue();
		}
		
		//input.close();
	}
	
	
	public static void menu() {
		displayMenu(1);		
		//Scanner input = new Scanner(System.in);  //commented out because Scanner has been raised in scope
		int menuChoice;
		boolean bigloop; //do loop again if true
		do {
			bigloop = false;
			print("Select Option $>");
			menuChoice = input.nextInt();
			switch(menuChoice) {
				case 0: //Exit 
					exitProgram();
				case 1: //Login
					displayMenu(3);
					boolean subloop;
					do {
						subloop = false;
						print("    Select Option $>");
						menuChoice = input.nextInt();
						switch(menuChoice) {
							case 0:
								break;
							case 1: //Login Type1 PlainText
								login(1);								
								break;
							case 2: //Login Type2 Hash
								println("    Using Hash Method.");
								login(2);
								break;
							case 3: //Login Type3 Salt & Hash
								println("    Using Salted Hash Method.");
								login(3);
								break;
							default:
								println("    Invalid option.");
								subloop = true;
								break;
						}
					}while(subloop);
					bigloop = true;
					displayMenu(1);
					break;
				case 2: //Create Account
					print("Enter username $>");
					String usernameMake = input.next();
					print("Enter password $>");
					String passwordMake = input.next();
					createAccount(usernameMake, passwordMake);
					bigloop = true;
					break;
				case 3: //Generate Test Accounts
					print("Give lower bounds: $>"); 
					int lower = input.nextInt();
					print("Give upper bounds: $>");
					int upper = input.nextInt();
					print("Number of accounts to create: $>");
					int number = input.nextInt();
					generateAccounts(lower, upper, number);
					println("Press ENTER to continue.");
					pressToContinue();
					bigloop = true;
					displayMenu(1);
					break;
				case 4: //Password File Cracking Tool
					displayMenu(2);
					boolean subloop2;
					do {
						subloop2 = false;
						print("    Select Option $>");
						menuChoice = input.nextInt();
						switch(menuChoice) {
							case 0:
								break;
							case 1: //Crack Type1 PlainText
								println("This is trivial.");
								/* PRINT FILE */
								break;
							case 2: //Crack Type2 Hash
								break;
							case 3: //Crack Type3 Salt & Hash
								break;
							default:
								println("    Invalid option.");
								subloop2 = true;
								break;
						}
					}while(subloop2);
					bigloop = true;
					displayMenu(1);
					break;
				case 5:
					flushFiles();
					bigloop = true;
					break;
				default:
					println("Invalid option.");
					bigloop = true;
					break;
			}
		}while(bigloop);
	}
	
	
	public static void exitProgram() {
		input.close();
		println("\nProgram terminated normally.");
		System.exit(0); 
	}
	
	
	private static void pressToContinue() { 
		//System.out.println("Press Enter key to continue...");
	    try {
	    	System.in.read();
	    }catch(Exception e) {}  
	}
	
	
	/* lazy print functions */
	public static void println(String arg0) {System.out.println(arg0);}
	public static void print(String arg0) {System.out.print(arg0);}
	public static void printf(String arg0) {System.out.printf(arg0);}

}
