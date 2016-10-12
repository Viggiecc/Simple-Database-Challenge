package database.transaction;

import java.util.Stack;

/*
 * Handle transaction commands: BEGIN ROLLBACK COMMIT 
*/

public class TransactionBlocks {
	/**
	 * Push for each begin, pop for each rollback and clear all but top elements for commit.
	 */

	Stack<UnitTransaction> transactionBlocks = new Stack<UnitTransaction>();
	private boolean hasBegan = false;

	/**
	 * Begin - Open a new transaction block. Transaction blocks can be nested; a BEGIN can be issued inside of an existing block.
	 * Push current UnitTransaction to top of stack and return copy of the UnitTransaction 
	 */

	public UnitTransaction begin (UnitTransaction unitTransaction) {
		transactionBlocks.push(unitTransaction);
		hasBegan = true;
		return unitTransaction;
	}

	/**
	 * Rollback - Undo all of the commands issued in the most recent transaction block, and close the block. Print nothing if successful, or print NO TRANSACTION if no transaction is in progress.
	 * Pop current UnitTransaction from top of stack and if transaction block is empty, print NO TRANSACTION   
	 */

	public UnitTransaction rollback (UnitTransaction unitTransaction) {
		if (!hasBegan) {
			System.err.println("Could not rollback with out begin, no transaction exists");
			return unitTransaction;
		}

		if (!transactionBlocks.empty()) {
			transactionBlocks.pop();
			UnitTransaction curTransaction = transactionBlocks.peek();
			if (transactionBlocks.empty()) hasBegan = false;
			return curTransaction;
		} else {
			System.out.println("NO TRANSACTION");
			return unitTransaction;
		}
	}

	/**
	 * Commit - Close all open transaction blocks, permanently applying the changes made in them. Print nothing if successful, or print NO TRANSACTION if no transaction is in progress.
	 * Clear the stack and push just the newest unitTransaction object into it.  
	 */

	public UnitTransaction commit (UnitTransaction unitTransaction) {
		if (!hasBegan) {
			System.err.println("Could not commit with out begin, no transaction exists");
			return unitTransaction;
		}
		if (!transactionBlocks.empty()) {
			UnitTransaction newestTransaction = transactionBlocks.peek();
			transactionBlocks.clear();
			return newestTransaction;
		} else {
			System.out.println("NO TRANSACTION");
			return unitTransaction;
		}
	}



}