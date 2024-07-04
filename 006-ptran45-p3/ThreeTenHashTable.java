/**
 * creating a Hash Map.
 * @param <K> takes any types
 * @param <V> takes any types
 * @author Phat Tran
 */

public class ThreeTenHashTable<K, V> {

	//-------------------------------------------------------------
	// DO NOT EDIT ANYTHING FOR THIS SECTION EXCEPT TO ADD JAVADOCS
	//-------------------------------------------------------------

	/**
	 * this class is a pair we will put in hash table.
	 * @param <K> takes any types
	 * @param <V> takes any types
	 */
	private class TableEntry<K,V> {

		/**
		 * key.
		 */
		private K key;

		/**
		 * value.
		 */
		private V value;

		/**
		 * Constructors.
		 * @param key takes key
		 * @param value takes value
		 */
		public TableEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * getter for key.
		 * @return K key
		 */
		public K getKey() {
			return key;
		}

		/**
		 * getter for value.
		 * @return  V value
		 */
		public V getValue() {
			return value;
		}
		
		/**
		 * toString().
		 * @return a string format of key:value
		 */
		public String toString() {
			return key.toString()+":"+value.toString();
		}
	}

	//underlying array for hash table storage 
	// -- you MUST use this for credit! Do NOT change the name or type
	/**
	 * hash table named storage.
	 */
	private TableEntry<K,V>[] storage;

	/**
	 * default length for the hash table.
	 */
	public static int defaultTableLength = 10;

	/**
	 * toString().
	 * for testing purposes.
	 * @return String contains elements in storage array
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < storage.length; i++) {
			if(storage[i] != null && !isTombstone(i)) {
				s.append(storage[i] + "\n");
			}
		}
		return s.toString().trim();
	}
	
	/**
	 * toStringDebug().
	 * for testing purposes.
	 * @return String for debugging
	 */
	public String toStringDebug() {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < storage.length; i++) {
			if(!isTombstone(i)) {
				s.append("[" + i + "]: " + storage[i] + "\n");
			}
			else {
				s.append("[" + i + "]: tombstone\n");
			}
			
		}
		return s.toString().trim();
	}
	


	//-------------------------------------------------------------
	// END OF PROVIDED "DO NOT EDIT" SECTION 
	//-------------------------------------------------------------

	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!

	/**
	 * keep track of size (number of elements in the array).
	 */
	private int size; 

	/**
	 * tombStone for removed value.
	 */
	private TableEntry<K,V> tombStone; 
	

	/**
	 * Constructor.
	 * Create a hash table where the initial storage.
	 * has a capacity of initCapacity.
	 * You may assume initCapacity is >= 2.
	 * @param initCapacity user's capacity
	 */
	@SuppressWarnings("unchecked")
	public ThreeTenHashTable(int initCapacity) {
		storage = (TableEntry<K,V>[]) new TableEntry[initCapacity];
		size = 0;
		tombStone = new TableEntry<K,V>(null, null);
	}

	/**
	 * Constructor.
	 * Create a hash table where the initial storage.
	 * has a capacity of defaultTableLength.
	 * You may assume initCapacity is >= 2.
	 */
	@SuppressWarnings("unchecked")
	public ThreeTenHashTable() {
		storage = (TableEntry<K,V>[]) new TableEntry[defaultTableLength];
		size = 0;
		tombStone = new TableEntry<K,V>(null, null);
	}

	/**
	 * O(1).
	 * keep tracks the capacity of array.
	 * @return the how big the storage is 
	 */
	public int capacity() {
		return storage.length;
	}

	/**
	 * O(1).
	 * keeps track numbers of elements in the table.
	 * @return the number of elements in the table
	 */
	
	public int size() {
		return size;
	}
		
	/**
	 * Place value val at the location determined by key.
	 * Use linear probing for collisions.
	 * Hint: Make a TableEntry to store the mapping of key:val.
	 * and determine the probe start based on: - the absolute value of k.hashCode() and.
	 * 										   - the table length.
	 * If the key already exists in the table, replace the mapping to be val (i.e. no duplicate keys in hash table).
	 * If the key isn't in the table, add key:val in table.
	 * If after the addition, load of table >= 0.8, rehash to ensure the table is expanded to twice the current capacity.
	 * Worst case: O(n) where n is the number of items in table, Average case: O(1).
	 * @param key takes key
	 * @param val takes value
	 * @return false w/o updating the table if either key or val is null; otherwise return true
	 */
	public boolean put(K key, V val) {
		if(key == null || val == null){
			return false;
		}
		TableEntry<K,V> pair = new TableEntry<K,V>(key, val);
		int index = Math.abs(key.hashCode()) % capacity();
		double load = 0.0;
		int newCap = capacity() * 2;

		//use for linear probing
		for(int i = index; i < capacity(); i++){
			if((storage[i] == null) || (isTombstone(i))){//put pair into array
				storage[i] = pair;
				size++;
				load = (double)size / capacity();
				if(load >= 0.8){//rehash the table
					rehash(newCap);
				}
				break;
			}
			else{//means something is already at this index
				if((storage[i].key).equals(key)){
					storage[i].value = val;
					break;
				}
			}
		}
		return true; 
	}

	/**
	 * Given a key, return the value it maps to from the table.
	 * If key is not in the table, return null.
	 * Worst case: O(n) where n is the number of items in table, Average case: O(1).
	 * @param key takes key
	 * @return	value that maps the key, if key does not exist, return null
	 */
	public V get(K key) {
		for(int i = 0; i < capacity(); i++){
			if((storage[i] == null) || (isTombstone(i))){
				continue;
			}
			else if((storage[i].key).equals(key)){
				return storage[i].value;
			}
		}
		return null;
	}
	
	/**
	 * this is a helper method needed for printing: hence you.
	 * can assume loc is always valid.
	 * O(1).
	 * @param loc index
	 * @return whether or not there is a tombstone at the given index
	 */
	public boolean isTombstone(int loc) {
		if(storage[loc] == null){
			return false;
		}
		return storage[loc].equals(tombStone);
	}

	/**
	 * Increase or decrease the capacity of the storage to be newCap.
	 * Remember to rehash all exiting key:value pairs. 
	 * - Traverse the old table in ascending order of indexes in rehashing/copying.		
	 * If the new capacity will make the load to be at or above 0.8, do not rehash and return false.
	 * Otherwise, rehash and return true.
	 * @param newCap new capacity of the storage array
	 * @return true if rehash successfully, otherwise, false
	 */
	@SuppressWarnings("unchecked")
	public boolean rehash(int newCap) {
		double load = (double)size / newCap;
		TableEntry<K,V>[] newStorage = (TableEntry<K,V>[]) new TableEntry[newCap];
		int index = 0;

		if(load >= 0.8){
			return false;
		}
		
		//move pairs from storage to newStorage
		for(int i = 0; i < capacity(); i++){
			if(isTombstone(i) || (storage[i] == null)){//there is Tombstone or null slot
				continue;
			}
			else{//no Tombstone
				index = Math.abs((storage[i].key).hashCode()) % newCap;

				if(newStorage[index] == null){//empty slot in newStorage
					newStorage[index] = storage[i];
				}
				else{//means something is already here
					index++;//move to next index
					while(index < newCap){
						if(newStorage[index] == null){//empty slot in newStorage
							newStorage[index] = storage[i];
							break;
						}
						else{
							index++;
						}
					}
					
				}
			}
		}
		storage = newStorage;
		return true;
	}
	
	/**
	 * Remove the given key (and associated value) from the table. Return the value removed.	
	 * If the key is not in the table, return null.
	 * Hint: Remember to leave a tombstone!
	 * Worst case: O(n) where n is the number of items in table, Average case: O(1).
	 * @param key takes key
	 * @return value removed
	 */
	public V remove(K key) {
		V removal = null;
		for(int i = 0; i < capacity(); i++){
			if((storage[i] == null) || (isTombstone(i))){
				continue;
			}
			else if((storage[i].key).equals(key)){
				removal = storage[i].value;
				storage[i] = tombStone;//set tombStone
				size--;
				return removal;
			}
		}
		return null;
	}


	//-------------------------------------------------------------
	// TESTING CODE   
	//-------------------------------------------------------------
	
	/**
	 * for testing purposes.
	 * @param args command line argument
	 */
	public static void main(String[] args) {
		//main method for testing, edit as much as you want
		ThreeTenHashTable<Integer,Character> ht1 = new ThreeTenHashTable<>(5);
		
		//init, put, get
		if(ht1.capacity() == 5 && ht1.size() == 0 && ht1.put(1, 'A')
			&& ht1.put(2, 'B') && ht1.size() == 2 && ht1.get(1).equals('A') 
			&& ht1.get(2).equals('B')) {
			System.out.println("Yay 1");
		}
				
		//put to change mapping, toString
		if( ht1.put(1, 'Z') && ht1.size()==2 && ht1.get(1).equals('Z') 
			&& ht1.toString().equals("1:Z\n2:B") 
			&& ht1.toStringDebug().equals("[0]: null\n[1]: 1:Z\n[2]: 2:B\n[3]: null\n[4]: null")) {
			System.out.println("Yay 2");
		}
		
		//System.out.println(ht1);
		//System.out.println(ht1.toStringDebug());

		
		//put with collision
		if (ht1.put(7,'S')  && ht1.get(7).equals('S')
			&& ht1.toStringDebug().equals("[0]: null\n[1]: 1:Z\n[2]: 2:B\n[3]: 7:S\n[4]: null")){
			System.out.println("Yay 3");		
		}
		
		//put that will trigger rehash
		if (ht1.put(5,'F') && ht1.capacity()==10 
			&& ht1.toString().equals("1:Z\n2:B\n5:F\n7:S")
			&& ht1.toStringDebug().equals("[0]: null\n[1]: 1:Z\n[2]: 2:B\n[3]: null\n[4]: null\n[5]: 5:F\n[6]: null\n[7]: 7:S\n[8]: null\n[9]: null")){
			System.out.println("Yay 4");					
		}
		
		//remove
		if (ht1.remove(2).equals('B') && ht1.remove(5).equals('F') && ht1.put(11, 'E') && 
			ht1.toStringDebug().equals("[0]: null\n[1]: 1:Z\n[2]: 11:E\n[3]: null\n[4]: null\n[5]: tombstone\n[6]: null\n[7]: 7:S\n[8]: null\n[9]: null")){
			System.out.println("Yay 5");								
		}
		//System.out.println(ht1.toStringDebug());

		//rehash
		if (!ht1.rehash(2) && !ht1.rehash(3) && ht1.rehash(6) && ht1.capacity() == 6 &&
			ht1.toString().equals("1:Z\n7:S\n11:E")&&
			ht1.toStringDebug().equals("[0]: null\n[1]: 1:Z\n[2]: 7:S\n[3]: null\n[4]: null\n[5]: 11:E")){
			System.out.println("Yay 6");										
		}

	}
}