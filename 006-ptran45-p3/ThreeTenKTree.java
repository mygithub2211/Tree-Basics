/**
 * K Tree array class.
 * @param <E> takes any data types
 * @author Phat Tran
 */
public class ThreeTenKTree<E> {
	//K-ary tree with an array as internal storage.
	//All nodes are stored in the array following level-order top-down 
	//and left-to-right within one level.
	//Root at index 0.

	//underlying array for k-ary tree storage 
	// -- you MUST use this for credit! Do NOT change the name or type
	/**
	 * storage array will be used to store node of K Tree array.
	 */
	private E[] storage;
	
	//hash table to help remember the index of each stored value
	/**
	 * hash Map.
	 */
	private ThreeTenHashTable<E, Integer> indexMap;

	//branching factor
	/**
	 * number of K (children that 1 node can have the most).
	 */
	private int branchK; 

	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!
	/**
	 * size, numbers of node in the tree.
	 */
	private int size;

	/**
	 * height of the tree.
	 */
	private int treeHeight;

	/**
	 * Constructor.
	 * initialize tree storage as an array of given length and branching factor as k.
	 * For example, if k=2, length will be of 1, 3, 7, 15, 29, etc. 
	 * May assume the given length ensures the storage for a perfect tree.
	 * If k=3, length will be of length 1, 4, 13, etc.
	 * May also assume k>=2.
	 * Also initialize the hash table with ThreeTenHashTable.defaultTableLength.
	 * @param length length of the storage array
	 * @param k numbers of children a node can have the most
	 */
	@SuppressWarnings("unchecked")
	public ThreeTenKTree(int length, int k) {
		storage = (E[]) new Object[length];
		indexMap = new ThreeTenHashTable<E, Integer>();
		branchK = k;
		size = 0;
		treeHeight = 0;
	}
	
	/**
	 * getter for branchK, which is number of children a node can have the most.
	 * O(1).
	 * @return branchK
	 */
	public int getBranch(){
		return branchK; 
	}
	
	/**
	 * report number of non-null nodes in tree.
	 * O(1).
	 * @return size - number of non-null nodes in tree
	 */
	public int size() {
		return size; 
	}
	
	/**
	 * report the length of storage.
	 * O(1).
	 * @return numbers of nodes of a perfect tree of the current height.
	 */
	public int capacity(){
		return storage.length; 
	}
	
	/**
	 * report the tree height.
	 * O(1).
	 * @return the treeHeight
	 */
	public int height() {
		return treeHeight;
	}

	/**
	 * set value at the specified index.
	 * either add or remove. Remove when value is null.
	 * remove a leaf node only.
	 * @param index where we want to store the node in storage array
	 * @param value value of the node
	 * @return true if successfully set, otherwise false
	 */
	@SuppressWarnings("unchecked")
	public boolean set(int index, E value) {
		// Set value at index in tree storage.
		// If value is null, this method attempts to remove a (leaf) node.
		if(value == null){
			//If index is not valid or the given index does not have a node, 
			//no change to tree and return false;
			if((index < 0) || (index >= capacity()) || (storage[index] == null)){
				return false;
			}
			//If node at given index has any child, do not remove but return false;
			//Check if the passed in node is a leaf
			for(int i = 1; i <= branchK; i++){
				int child = (branchK * index) + i;
				if(child >= capacity()){//means no child
					break;
				}
				if(storage[child] != null){// find one child, return false
					return false;
				}
			}//loop exits mean no child --> leaf node

			//Before remove the node, we need to check if the node at the index 
			//has siblings, so we can adjust the tree height
			int sibling = 0;
			int root = 0; //root index
			int leftMost = 0;
			int leftMost2 = 0;
			boolean run = true;
			
			while(run){//this loop find leftMost and leftMost2
				leftMost = (branchK * root) + 1;//to get leftmost node index
				leftMost2 = (branchK * leftMost) + 1;
				//must in the range
				if((index >= leftMost) && (index < leftMost2)){
					break;
				}
				root = leftMost;
			}
			/*now checking sibling nodes*/
			if(index == leftMost){
				for(int i = index; i < leftMost2; i++){
					if(storage[i] != null){
						sibling = 1;
						break;
					}
				}
			}
			else if((index > leftMost) && (index < (leftMost2-1))){
				for(int i = index+1; i < leftMost2; i++){
					if(storage[i] != null){
						sibling = 1;
						break;
					}
				}
				if(sibling == 0){
					for(int i = index-1; i >= leftMost; i--){
						if(storage[i] != null){
							sibling = 1;
							break;
						}
					}
				}
			}
			else if(index == (leftMost2 - 1)){
				for(int i = index-1; i >= leftMost; i--){
					if(storage[i] != null){
						sibling = 1;
						break;
					}
				}
			}
			if(sibling == 0){
				treeHeight--;
			}
			//remove the node and return true.
			indexMap.remove(storage[index]);//update hash table
			storage[index] = value;	
			size--;
			return true;	
		}

		// If value is not null, this method attempts to add/replace a node.
		// - If value is already in tree (any index), no change and return false;
		// - If value is new and index denotes a valid node of current tree, set value
		//   at this node and return true;
		//   - You may need to grow the tree storage (i.e. add a level) for this node
		// - If adding this node would make the tree invalid, no change and return false.
		// See examples in main() below for different cases.
		// Remember to update the hash table if tree is updated.
	
		if(value != null){
			int sibling = 0;
			int root = 0; //root index
			int leftMost = 0;
			int leftMost2 = 0;
			boolean run = true;
			if(index < 0){
				return false;
			}
			/*to check if the value already exists in the tree*/
			for(int i = 0; i < capacity(); i++){	
				if(storage[i] == null){
					continue;
				}
				if(storage[i].equals(value)){
					return false;
				}
			}
			
			if(index < capacity()){
				if(storage[index] != null){//replace
					indexMap.remove(storage[index]);
					storage[index] = value;
					indexMap.put(value, index);
					
				}
				else{				
					int parent = (index - 1)/branchK;
					if(index == 0){
						storage[index] = value;
						indexMap.put(value, index);
						size++;
					}
					else if((storage[parent] == null) && (index != 0)){/*no parent so cannot add child*/
						return false;							  
					}
					else{
						storage[index] = value;
						indexMap.put(value, index);//update hashMap
						size++;//keep track # of nodes
					}
					// we need to check if the node at the index 
					//has siblings, so we can adjust the tree height
	
					if(index == 0){
						treeHeight = 0;
						return true;
					}
					while(run){//this loop find leftMost and leftMost2
						leftMost = (branchK * root) + 1;//to get leftmost node index
						leftMost2 = (branchK * leftMost) + 1;
						//must in the range
						if((index >= leftMost) && (index < leftMost2)){
							break;
						}
						root = leftMost;
					}
					/*now checking sibling nodes*/
					if(index == leftMost){//index is the leftmost node in the level
						for(int i = index+1; i < leftMost2; i++){
							if(storage[i] != null){
								sibling = 1;
								break;
							}
						}
					}
					else if((index > leftMost) && (index < (leftMost2-1))){/*somewhere in the middle of the level*/
						for(int i = index+1; i < leftMost2; i++){
							if(storage[i] != null){
								sibling = 1;
								break;
							}
						}
						if(sibling == 0){
							for(int i = index-1; i >= leftMost; i--){
								if(storage[i] != null){
									sibling = 1;
									break;
								}
							}
						}
					}
					else if(index == (leftMost2 - 1)){//index is the rightmost node in the level
						for(int i = index-1; i >= leftMost; i--){
							if(storage[i] != null){
								sibling = 1;
								break;
							}
						}
					}
					if(sibling == 0){
						treeHeight++;
					}
				}
			}
			else if(index >= capacity()){
				int countNode = 0;
				leftMost = 0;//to get leftmost node index
				leftMost2 = (branchK * leftMost) + 1;
				while(run){//this loop find leftMost and leftMost2
					//must in the range
					if((index >= leftMost) && (index < leftMost2)){
						break;
					}
					leftMost = (branchK * root) + 1;//to get leftmost node index
					leftMost2 = (branchK * leftMost) + 1;
					root = leftMost;
				}
				
				/*adding new level for the tree*/
				for(int i = leftMost; i < leftMost2; i++){		
					countNode++;/*# of nodes in last level*/
				}
				/*to get # of nodes with new last level*/
				int newSize = capacity() + countNode;
				E[] newStorage = (E[]) new Object[newSize];/*new array with 1 more level added*/
				for(int i = 0; i < capacity(); i++){/*transferring elements to new array*/
					newStorage[i] = storage[i];
				}
				storage = newStorage;	
				/*if node at the index is a child of other node*/
				/*so we can add the child node into the tree*/
				int parent = (index-1)/branchK;
				if(storage[parent] == null){/*no parent node, so we cannot add child node*/
					return false;
				}
				else{
					storage[index] = value;
					indexMap.put(value, index);//update hashmap
					size++;
				}
				treeHeight++;		
			}
		}
		return true;
	}

	/**
	 * get the node at specified index of the tree(storage array).
	 * O(1).
	 * @param index where we want to remove the node.
	 * @return the node that we removed. Otherwise, return null for invalid index or index with no node 
	 */
	public E get(int index) {
		if((index < 0) || (index >= capacity())){
			return null;
		}
		if(storage[index] == null){
			return null;
		}
		return storage[index]; 
	}

	/**
	 * Return a string of all nodes (excluding null nodes) in tree storage.
	 * include all levels from top down.
	 * all nodes are printed with a single space separated.
	 * return an empty string if tree is null.
	 * Note: use StringBuilder instead of String concatenation.
	 * @return a string contains node in level order
	 */
	public String toStringLevelOrder() {
		// Example:
		//  binary tree:      A
		//                  /   \
		//                 B     C
		//                /     /
		//               D     E
		//
		// toStringLevelOrder() should return "A B C D E"
		StringBuilder string = new StringBuilder();
		boolean treeNull = false;
		for(int i = 0; i < capacity(); i++){
			if(storage[i] == null){
				continue;
			}
			treeNull = true;
			string.append(storage[i]);
			string.append(" ");
		}
		if(treeNull == true){
			return string.toString().trim();
		}
		return ""; 
	}
	

	/**
	 * Return a string of all nodes (including null nodes) in tree storage.
	 * include all levels from top down.
	 * each level is printed on its own line.
	 * each node in the level is printed with a single space separated.
	 * null nodes included in string.
	 * return an empty string if tree is null.
	 * Note: use StringBuilder instead of String concatenation.
	 * @return string contains all nodes, including null, of the tree
	 */
	@Override
	public String toString() {
		// Example:
		//  binary tree:      A
		//                  /   \
		//                 B     C
		//                /     /
		//               D     E
		//
		// toString() should return "A\nB C\nD null E null"
		StringBuilder string = new StringBuilder();
		string.append(storage[0]);
		string.append("\n");

		int root = 0; //root index
		int leftMost = 0;
		int leftMost2 = 0;
		boolean run = true;
		if(storage[0] == null){
			return "";
		}
		while(run){//this loop find leftMost and leftMost2
			leftMost = (branchK * root) + 1;//to get leftmost node index
			leftMost2 = (branchK * leftMost) + 1;

			if((leftMost-1) == (capacity()-1)){//means that we have reached a level not in the array
				break;
			}
			if(leftMost < capacity()){
				for(int i = leftMost; i < leftMost2; i++){//loop the same level
					string.append(storage[i]);
					string.append(" ");
				}
				/*to trim the space at the end to make sure there is no space at the end of the string*/
				if((string.length() > 0)){
					if(string.charAt(string.length() - 1) == ' ') {
						string.setLength(string.length() - 1);
					}
				}
				string.append("\n");
			}
			root = leftMost;
		}
		return string.toString().trim();//trim() to truncate newline at the end of the string
	}

	/**
	 * Find the node of the given value and return the ancestors of the node in a string.
	 * if value not present, return null.
	 * return string should include all ancestors including the node itself.
	 * ancestors should start from root and separated by "-->".
	 * O(height) assuming hash table search is O(1).
	 * @param value the node will be used to check its ancestors
	 * @return a string including all the ancestors and the node itself. Otherwise, return null
	 */
	public String getAncestors(E value){
		int index = 0;
		int parent = 0;
		int present = 0;
		String string = null;
		StringBuilder tempString = new StringBuilder();	
		if(value == null){
			return null;
		}
		/*find the node of the given value*/
		for(int i = 0; i < capacity(); i++){
			if(storage[i] == null){
				continue;
			}
			if(storage[i].equals(value)){
				present = 1;
				index = i;
				break;
			}
		}
		if(present == 0){
			return null;//value is not present
		}
		if(index == 0){//root node, so no ancestors
			return (tempString.append(storage[index])).toString();
		}
		tempString.append(storage[index]);
		tempString.append(">--");
		for(int i = 0; i < treeHeight; i++){
			parent = (index - 1)/branchK;
			if(parent < 0){
				break;
			}
			else if(parent == 0){//root node
				tempString.append(storage[parent]);
				break;
			}
			else if(indexMap.get(storage[parent]) == parent){//search hash table
				tempString.append(storage[parent]);
				tempString.append(">--");
			}
			index = parent;
		}
		string = (tempString.reverse()).toString();
		return string; 	
	}


	/**
	 * Find the node of the given value and return the children of the node in a string.
	 * if value not present, return null.
	 * if the node is a leaf, return an empty string.
	 * O(K) where K is the branch factor assuming hash table search is O(1).
	 * @param value the node is used to check its children
	 * @return string should include all children, from left to right, and separated by a single space
	 */
	public String getChildren(E value){
		int index = 0;
		int present = 0;
		StringBuilder string = new StringBuilder();
		if(value == null){
			return null;
		}
		for(int i = 0; i < capacity(); i++){//find the node of given value
			if(storage[i] == null){
				continue;
			}
			if(storage[i].equals(value)){
				present = 1;
				index = i;
				break;
			}
		}
		if(present == 0){//value not present
			return null;
		}
		if(isLeaf(value)){
			return "";
		}
		for(int i = 1; i <= branchK; i++){//putting children into string
			int child = (branchK * index) + i;
			if(child >= capacity()){
				break;
			}
			if(indexMap.get(storage[child]) == null){
				continue;
			}
			else if(indexMap.get(storage[child]) == child){//search hash Map
				string.append(storage[child]);
				string.append(" ");
			}
		}
		/*to trim the space at the end to make sure there is no space at the end of the string*/
		if((string.length() > 0)){
			if(string.charAt(string.length() - 1) == ' ') {
				string.setLength(string.length() - 1);
			}
		}
		return string.toString();
	}
	
	/**
	 * Determine if value is in tree or not.
	 * null is not a valid value in tree.
	 * O(1) assuming hash table search is O(1).
	 * @param value node is used to check if there is this node in the tree
	 * @return true if a tree node has value; false otherwise.
	 */
	public boolean has(E value){
		if(value == null){
			return false;
		}
		if(indexMap.get(value) == null){//value(key)
			return false;
		}
		return true; 
	}
	
	/**
	 * check if a node is a leaf node.
	 * If node at given index has any child, do not remove and return false.
	 * O(K) where K is the branching factor assuming hash table search is O(1).
	 * @param value will be used to check if this node is a leaf
	 * @return true if a leaf node has value; false otherwise
	 */
	public boolean isLeaf(E value){
		int index = 0;

		if(value == null){
			return false;
		}
		if(!has(value)){
			return false;
		}	
		
		index = indexMap.get(value);
		for(int i = 1; i <= branchK; i++){//checking children for the node at the index
			int child = (branchK * index) + i;
			if((child >= capacity())){
				return true;
			}
			if((storage[child] != null) ){
				return false;//not a leaf node, it has children
			}
		}
		return true;
	}
	
	/**
	 * Remove value from tree if value is in a leaf node.
	 * if value not present, return false.
	 * if value present but not in a leaf node, do not remove and return false.
	 * if value is in a leaf node, remove node from tree and return true.
	 * @param value will be removed
	 * @return true if value if a leaf. Otherwise, false
	 */
	public boolean remove(E value){ 
		int index = 0;
		if(value == null){
			return false;
		}
		if(isLeaf(value)){
			index = indexMap.get(value);
		}
		else{
			return false;
		}
		if(set(index, null)){//remove the value of leaf node
			return true;
		}
		return false; 
	} 


	/**
	 * this method will create the FCNS tree basedo on the K tree.
	 * @param root the root
	 * @return the FCNS tree to the method createFcnsTree()
	 */
	private FcnsTreeNode<E> helpCreate(FcnsTreeNode<E> root){
		if(root == null){
			return null;
		}

		int parentIndex = indexMap.get(root.getValue());
		int childIndex = (branchK * parentIndex) + 1;

		FcnsTreeNode<E> child = null;
		if(childIndex < capacity()){
			if(storage[childIndex] != null){
				child = new FcnsTreeNode<E>(storage[childIndex]);
				root.setChild(helpCreate(child));
			}
		}
		

		/*then get nextSibling*/
		FcnsTreeNode<E> currentNode = root.getChild();
		for(int i = 2; i <= branchK; i++){
			child = null;
			childIndex = (branchK * parentIndex)+ i;

			if(childIndex < capacity()){
				if(storage[childIndex] != null){
					child = new FcnsTreeNode<E>(storage[childIndex]);
				}
				if(currentNode == null){
					root.setChild(helpCreate(child));
					currentNode = root.getChild();
				}
				else{
					currentNode.setSibling(helpCreate(child));
					if(currentNode.getSibling() != null){
						currentNode = currentNode.getSibling();
					}
				}

			}
			
		}
		return root;
	}

	/**
	 * this method calls the methods above.
	 * @param <E> generic type
	 * @param ktree this is the KTree
	 * @return the FCNS tree
	 */
	public static <E> FcnsTreeNode<E> createFcnsTree(ThreeTenKTree<E> ktree){
		//construct the corresponding first-child-next-sibling tree 
		//for this k-ary tree and return the root node of the FCNS tree.
		//Consider helper methods; consider a recursive approach.
		//O(N) where N is the size of the current K-ary tree.
		FcnsTreeNode<E> root = new FcnsTreeNode<E>(ktree.storage[0]);
		return ktree.helpCreate(root);
	}
	//-------------------------------------------------------------
	// TESTING CODE   
	//-------------------------------------------------------------
	

	/**
	 * for testing purposes.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		ThreeTenKTree<Integer> t;
		t = new ThreeTenKTree<>(7, 2);
		
		//      0
		//       \
		//        2
		//       / \
		//      5   6

		//set up tree, basics
		if (t.set(0,0) && t.set(2,2) && t.set(6,6) && t.set(5,5) && t.height()==2
			&& t.size() == 4 && t.getBranch() == 2 && t.capacity() == 7){
			System.out.println("Yay1");
		}

		//get, toString, toStringLevelOrder		
		if (t.get(0) == 0 && t.get(3) == null 
			&& t.toString().equals("0\nnull 2\nnull null 5 6") 
			&& t.toStringLevelOrder().equals("0 2 5 6")){
			System.out.println("Yay2");		
		}
			
		//System.out.println("|"+t.toStringLevelOrder()+"|");

		// uncomment the following block when you are ready to test FcnsTreeNode class
		 
				
		//        0
		//       /
		//      2
		//     /
		//    5
		//     \
		//      6

		//construct an FCNS tree for comparison
		FcnsTreeNode<Integer> node1 = new FcnsTreeNode<>(0);
		FcnsTreeNode<Integer> node2 = new FcnsTreeNode<>(2);
		FcnsTreeNode<Integer> node3 = new FcnsTreeNode<>(5);
		FcnsTreeNode<Integer> node4 = new FcnsTreeNode<>(6);
		
		node1.setChild(node2);
		node2.setChild(node3);
		node3.setSibling(node4);


		FcnsTreeNode<Integer> myNode = ThreeTenKTree.createFcnsTree(t);
		
		//FCNS tree
		if (myNode.toStringLevelOrder().equals("0 2 5 6")
			&& myNode.toStringPostOrder().equals("6 5 2 0")
			&& myNode.equals(node1)){
			System.out.println("Yay3");				
		
		}
		//System.out.println(myNode.toStringPreOrder());
		//System.out.println(myNode.toStringPostOrder());
		
		//simulating K-ary tree traversals
		if (myNode.toStringKTreePreOrder().equals("0 2 5 6") 
			&& myNode.toStringKTreePostOrder().equals("5 6 2 0")
			&& myNode.toStringKTreeLevelOrder().equals("0 2 5 6")){
			System.out.println("Yay4");				
		
		}
		//System.out.println(myNode.toStringKTreePreOrder());
		//System.out.println(myNode.toStringKTreePostOrder());
		//System.out.println(myNode.toStringKTreeLevelOrder());
	
		
		// end of tests for FcnsTreeNode
		
		//check / inspect one value in tree
		if (t.has(6) && !t.has(10) && t.isLeaf(5) && !t.isLeaf(0) && !t.isLeaf(9) &&
			t.getAncestors(6).equals("0-->2-->6") && t.getChildren(2).equals("5 6")){
			System.out.println("Yay5");						
		}

		//set more scenarios		
		if (t.set(1,1) && t.set(11,11) //expansion of storage
			&& !t.set(7,7) //7 has no parent in tree
			&& !t.set(1,11) // value 11 already in tree
			&& t.height() == 3 && t.capacity() == 15 && 
			t.toString().equals("0\n1 2\nnull null 5 6\nnull null null null 11 null null null")){			
			System.out.println("Yay6");						
			
		}

		//set to remove
		if (!t.set(20,null) //index out of range		
			&& !t.set(5,null) //cannot remove a non-leaf
			&& t.set(6,null) && !t.has(6) //successful removal
			&& t.toString().equals("0\n1 2\nnull null 5 null\nnull null null null 11 null null null")){			
			System.out.println("Yay7");						
			
		}
		
		//remove by value
		
		if (!t.remove(12) && !t.remove(0) && t.remove(1) && !t.has(1)){
			System.out.println("Yay8");						
		
		}
	}
}



