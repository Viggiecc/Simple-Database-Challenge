package database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import database.transaction.UnitTransaction;
import database.transaction.TransactionBlocks;

public class Database {
	private static UnitTransaction unitTransaction;
	private static TransactionBlocks transactionBlocks;

	private final static String BEGIN = "BEGIN";
	private final static String SET = "SET";
	private final static String GET = "GET";
	private final static String UNSET = "UNSET";
	private final static String NUMEQUALTO = "NUMEQUALTO";
	private final static String ROLLBACK = "ROLLBACK";
	private final static String COMMIT = "COMMIT";
	private final static String END = "END";


	/*
     * Split input line to get the Data Commands/Transaction Commands
	 *
	 * Data Commands:
	 *
	 * SET <name> <value> - Set the variable <name> to the value <value>. Neither variable names nor values will contain spaces. 
	 * GET <name> - Print out the value of the variable <name>, or NULL if that variable is not set.
	 * UNSET <name> - Unset the variable <name>, making it just like that variable was never set.
     * NUMEQUALTO <value> - Print out the number of variables that are currently set to <value>. If no variables equal that <value>, print 0.
	 * END - Exit the program. Your program will always receive this as its last command.
     *
     * Transaction Commands:
     *
     * BEGIN – Open a new transaction block. Transaction blocks can be nested; a BEGIN can be issued inside of an existing block.
     * ROLLBACK - Undo all of the commands issued in the most recent transaction block, and close the block. Print nothing if successful, or print NO TRANSACTION if no transaction is in progress.
     * COMMIT – Close all open transaction blocks, permanently applying the changes made in them. Print nothing if successful, or print NO TRANSACTION if no transaction is in progress.
     * 
	*/

	 public static void handleCommands (String inputline) {
	 	String[] commands = inputline.split("\\s"); //split string inuputline into an array of substrings, the separator is "spacing"
	 	String command = commands[0];
	 	String name;
	 	int value;
	 	boolean isInvalid = false;

	 	try {
	 		switch (command.toUpperCase()) {
		 		case BEGIN: 
		 			if (commands.length == 1) {
		 				unitTransaction = transactionBlocks.begin(unitTransaction);
		 			} else {
		 				isInvalid = true;
		 			}
		 			break;
		 		case SET:
		 			if (commands.length == 3) {
		 				name = commands[1];
		 				value = Integer.parseInt(commands[2]);
		 				unitTransaction.set(name, value);
		 			} else {
		 				isInvalid = true;
		 			}
		 			break;
		 		case GET:
		 			if (commands.length == 2) {
		 				name = commands[1];
		 				unitTransaction.get(name);
					} else {
						isInvalid = true;
					}
					break;
		 		case UNSET:
		 			if (commands.length == 2) {
		 				name = commands[1];
		 				unitTransaction.unset(name);
		 			} else {
		 				isInvalid = true;
		 			}
		 			break;
		 		case NUMEQUALTO:
		 			if (commands.length == 2) {
		 				value = Integer.parseInt(commands[1]);
		 				unitTransaction.numEqualto(value);
		 			} else {
		 				isInvalid = true;
		 			}
		 			break;
		 		case ROLLBACK:
		 			if (commands.length == 1) {
		 				unitTransaction = transactionBlocks.rollback(unitTransaction);
		 			} else {
		 				isInvalid = true;
		 			}
		 			break;
		 		case COMMIT:
		 			if (commands.length == 1) {
		 				unitTransaction = transactionBlocks.commit(unitTransaction);
		 			} else {
		 				isInvalid = true;
		 			}
		 			break;
		 		default:
		 			isInvalid = true;
		 			break;
	 		}
	 	
	 	} catch (NumberFormatException e) {
	 		isInvalid = true;
	 		e.printStackTrace();
	 	} catch (NullPointerException e) {
	 		isInvalid = true;
	 		e.printStackTrace();
	 	}
	 	if (isInvalid) {
		 	System.err.println("Invalid commands for " + command + " command." + '\n' + "Please check your input");
		 }

	 	

	 }

	/*
	 * Read the given input file and call handleInputLine for each line in the given file
	 *
	 * @param inputFileLocation
	 *
	*/

	public static void fileInput (String inputFileLocation) {
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(inputFileLocation);
			reader = new BufferedReader (new InputStreamReader(fis));

			String inputline;

			while ((inputline = reader.readLine())!=null) {
				if (inputline.equals(END)) break;
				handleCommands(inputline);
				// System.out.println(inputline);
			}

		} catch (FileNotFoundException ex) {
			System.err.println ("Could not find the file");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.err.println ("Issue with file input");
			ex.printStackTrace();
		} finally {
			try {
				reader.close();
				fis.close();
			} catch (IOException ex) {
				System.err.println ("Issue with close file input");
			}
		}
	}

	/*
	 * Read the command line input and call the handleInputLine for each line input
	*/

	public static void commandLineInput () {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader (new InputStreamReader(System.in));

			String inputline = reader.readLine();

			while (!inputline.equals(END)) {
				// System.out.println(line);
				handleCommands(inputline);
				inputline = reader.readLine();
			}

		} catch (IOException ex) {
			System.err.println("Issue with command line input");
			ex.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException ex) {
				System.err.println ("Issue with close file input");
				ex.printStackTrace();
			}
		}
	}
	public static void main (String[] args) {
		unitTransaction = new UnitTransaction();
		transactionBlocks = new TransactionBlocks();

		if (args.length == 0) {
			commandLineInput();
		} else if (args.length == 1) {
			fileInput(args[0]);
		} else {
			System.out.println("Invalid numbers of arguments");
		}

	}
}
