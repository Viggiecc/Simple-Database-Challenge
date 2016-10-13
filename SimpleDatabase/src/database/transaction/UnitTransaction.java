package database.transaction;

import java.util.HashMap;

/*
 * Handle data commands: SET GET UNSET NUMEQUALTO
*/

public class UnitTransaction {

	public HashMap<String, Integer> name_value; 
	public HashMap<Integer, Integer> value_count;

	public UnitTransaction() {
		name_value = new HashMap<String, Integer>();
		value_count = new HashMap<Integer, Integer>();
	}

	public UnitTransaction(UnitTransaction unitTransaction) {
		this.name_value = new HashMap<String, Integer>(unitTransaction.getNameValue());
		this.value_count = new HashMap<Integer, Integer>(unitTransaction.getValueCount());
	}	
	/**
	 * SET:  Update name_value, set variable name to value in name_value and update value_count accourdingly
	 *
	 * @param name
	 * @param value
	 *
	*/
	public void set (String name, int value) {
		if (name_value != null && value_count != null) {
			int curValue;
			int curCount;
			if (name_value.containsKey(name)) {
				curValue = name_value.get(name);
				curCount = value_count.get(curValue);
				name_value.put(name, value);
				value_count.put(curValue, curCount - 1);
			} else {
				name_value.put(name, value);
			}

			if (value_count.containsKey(value)) {
				curCount = value_count.get(value);
				value_count.put(value, curCount + 1);
			} else {
				value_count.put(value, 1);
			}
		}
	}

	/**
	 * GET:  Print out the value of name in name_value, or NULL if this variable name is not set
	 *
	 * @param name
	 *
	*/

	public void get (String name) {
		if (name_value != null) {
			if (name_value.containsKey(name)) {
				System.out.println(name_value.get(name));
			} else {
				System.out.println("NULL");
			}
		}
	}

	/**
	 * UNSET:  Remove the value of name in name_value, update value_count accordingly
	 *
	 * @param name
	 *
	*/

	public void unset (String name) {
		if(name_value != null && value_count != null) {
			int curValue;
			int curCount;
			if(name_value.containsKey(name)) {
				curValue = name_value.get(name);
				curCount = value_count.get(curValue);
				value_count.put(curValue, curCount - 1);
				name_value.remove(name);
			} else  {
				System.err.println("Variable " + name + " does exit in memory!");
			}
		}
	}

	/**
	 * NUMEQUALTO: Print out the count of variables that are currently set to  value. If no variables equal value, print 0
	 *
	 * @param value
	 *
	*/

	public void numEqualto (int value) {
		if(value_count != null) {
			if(value_count.containsKey(value)) {
				System.out.println(value_count.get(value));
			} else {
				System.out.println(0);
			}
		}
	}

	public HashMap<String, Integer> getNameValue() {
		return name_value;
	}

	public HashMap<Integer, Integer> getValueCount() {
		return value_count;
	}

	// public boolean equals(UnitTransaction unitTransaction) {
	// 	if (this.name_value.keySet().size() == unitTransaction.getNameValue().keySet().size()) {
	// 		for (String name : this.name_value.keySet()) {
	// 			if (unitTransaction.getNameValue().containsKey(name)
	// 					&& unitTransaction.getNameValue().get(name) == this.name_value.get(name))
	// 				continue;
	// 			else
	// 				return false;
	// 		}
	// 	} else
	// 		return false;
	// 	return true;
	// }


}