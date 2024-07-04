/**
 * FcnsTreeNode class represent a node in First Child Next Sibling tree.
 * @param <E> type of the value stored in a node
 * @author Phat Tran
 */

public class FcnsTreeNode<E> {

	//-------------------------------------------------------------
	// DO NOT EDIT ANYTHING FOR THIS SECTION EXCEPT TO ADD JAVADOCS
	//-------------------------------------------------------------
	/**
	 * value of the node.
	 */
	private E value;

	/**
	 * pointer points to the nodes's first child.
	 */
	private FcnsTreeNode<E> firstChild;

	/**
	 * pointer points to the node's siblings.
	 */
	private FcnsTreeNode<E> nextSibling;

	/**
	 * Constructor.
	 * which initializes value, firstChild, nextSibling.
	 * @param value takes the passed in value 
	 */
	public FcnsTreeNode(E value){
		this.value = value;
		firstChild = null;
		nextSibling = null;
	}
	
	/**
	 * getter for value.
	 * @return value of the node
	 */
	public E getValue(){
		return this.value;
	}
	
	/**
	 * Set the firstChild to the passed in child (node).
	 * @param node takes the child 
	 */
	public void setChild(FcnsTreeNode<E> node){
		this.firstChild = node;
	}
	
	/**
	 * Set the nextSibling to the passed in sibling (node).
	 * @param node takes the sibling
	 */
	public void setSibling(FcnsTreeNode<E> node){
		this.nextSibling = node;
	}
	
	/**
	 * getter for firstChild.
	 * @return first child
	 */
	public FcnsTreeNode<E> getChild(){
		return this.firstChild;
	}

	/**
	 * getter for nextSibling.
	 * @return next sibling
	 */
	public FcnsTreeNode<E> getSibling(){
		return this.nextSibling;
	}


	/**
	 * Overriding toString().
	 * @return the value as a String
	 */
	@Override
	public String toString(){
		return value.toString();
	}
	        
	/**
	 * compare two nodes (for testing purpose).
	 * return true if: 1) they have the same element; and.
	 * 2) their have matching firstChild (subtree) and nextSibling (subtree).
	 * @param another takes the specific node
	 * @return true or false
	 */
	public boolean equals(FcnsTreeNode<E> another){
   		if (another==null)
   			return false;
   			
   		if (!this.value.equals(another.getValue()))
   			return false;
   		
  		if (this.firstChild==null){
   			if (another.firstChild!=null)
   				return false;
   		}
   		else if (!this.firstChild.equals(another.getChild()))
   			return false;
   			
   		if (this.nextSibling==null){
   			if (another.nextSibling!=null)
   				return false;
   		}
   		else if (!this.nextSibling.equals(another.getSibling()))
   			return false;
   			
   		return true;
   	
   	}
	
	//-------------------------------------------------------------
	// END OF PROVIDED "DO NOT EDIT" SECTION 
	//-------------------------------------------------------------

	//-------------------------------------------------------------
	// NOTE: You are NOT allowed to add any additional 
	// instance/class variables for this class. 
	//-------------------------------------------------------------

	// Hint: many of the methods should be straightforward if you code recursively.
	// Feel free to define private helper method(s).

	/**
	 * calculate the number of nodes in the tree rooted at a specific node.
	 * helper method for size().
	 * @param root takes the root(specific node)
	 * @return size to the size() below
	 */
	private int helpSize(FcnsTreeNode<E> root){
		int size = 1;
		if(root == null){
			return 0;
		}
		size += helpSize(root.firstChild);//recursive
		size += helpSize(root.nextSibling);//recursive
		return size;
	}
	
	/**
	 * call the function above.
	 * report the number of nodes in the tree rooted at this node.
	 * O(n) where n is the size of the tree.
	 * @return size to wherever calls this
	 */
	public int size(){
		return helpSize(this); 
	}
	

	/**
	 * minK().
	 * helper method to find minK().
	 * @param root takes root
	 * @return the answer to minK()
	 */
	private int helpMinK(FcnsTreeNode<E> root){
		if(root == null){
			return 0;
		}
		int max = 0;
		Queue<FcnsTreeNode<E>> queue = new Queue<FcnsTreeNode<E>>();
		/*get the first child*/
		FcnsTreeNode<E> child = root.firstChild;
		/*now getting its sibling*/
		if(child != null){
			queue.addLast(child);
			max++;
			while(!queue.isEmpty()){				
				child = queue.removeFirst();
				if(child != null){
					child = child.nextSibling;
					if(child != null){
						queue.addLast(child);
					}
				}
				if(child != null){
					max++;
				}
			}
		}
		/*Now make the first child as the root, recall the function to find its first child and the siblings of its first child */
		int mx = helpMinK(root.firstChild);
		if(mx > max){
			return mx;
		}
		else{
			return max;
		}
	}
	/**
	 * check degenerate Tree.
	 * @param root takes the passed in root
	 * @return 1 if it is not degenerated tree, otherwise 0
	 */
	public int checkDegTree(FcnsTreeNode<E> root){
		//1st Base Case
		if((root.getChild() == null) && (root.getSibling() == null)){
			return 0;
		}
		if((root.getChild() != null) && (root.getSibling() == null)){
			return checkDegTree(root.getChild());
		}
		if((root.getChild() == null) && (root.getSibling() != null)){
			return checkDegTree(root.getSibling());
		}
		if((root.getChild() != null) && (root.getSibling() != null)){
			return 1;//not degenerated tree
		}
		return 0;
	}
	/**
	 * Given the FCNS tree rooted at this node corresponds to a k-ray tree.
	 * Report the minimum k value.
	 * O(n) where n is the size of the tree.
	 * @return 0 if the tree has only one node, 1 if the corresponding k-ary tree is a degenerated tree.
	*/
	public int minK(){
		if((this.getChild() == null) && (this.getSibling() == null)){
			return 0;
		}
		if(checkDegTree(this) == 1){//not a degenerated tree
			return helpMinK(this); 
		}
		return 1;
	}


	/**
	 * LEVEL ORDER.
	 * @param root takes root
	 * @return String to toStringLevelOrder()
	 */
	private String levelOrder(FcnsTreeNode<E> root){
		StringBuilder string = new StringBuilder();
		Queue<FcnsTreeNode<E>> queue = new Queue<FcnsTreeNode<E>>();
		queue.addLast(root);
		if(root == null){
			return "";
		}
		while(!queue.isEmpty()){		
			FcnsTreeNode<E> node = queue.removeFirst();
			string.append(node.getValue());
			string.append(" ");

			if(node.getChild() != null){
				queue.addLast(node.getChild());
			}
			if(node.getSibling() != null){
				queue.addLast(node.getSibling());
			}
		}
		return string.toString();
	}
	/**
	 * Return a string of all nodes in the tree rooted at this node.
	 * - include all nodes in level order (top-down, left-to-right).
	 * - a single space is padded between two nodes, no space at the end.
	 * - O(n) where n is the size of the tree.
	 * - return an empty string for null tree.
	 * @return string 
	 */
	public String toStringLevelOrder(){
		return levelOrder(this).trim(); 
	}


	/**
	 * POST ORDER.
	 * @param root takes root
	 * @return String to toStringPostOrder()
	 */
	private String helpStringPostOrder(FcnsTreeNode<E> root){
		if(root == null){
			return "";
		}
		StringBuilder string = new StringBuilder();
		String left = helpStringPostOrder(root.firstChild);
		String right = helpStringPostOrder(root.nextSibling);
		if((left != null) || (right != null)){
			string.append(left);
			string.append(right);
			string.append(root.value);
			string.append(" ");
		}
		return string.toString();
	}

	/**
	 * Return a string of all nodes in the tree rooted at this node.
	 *  - include all nodes in post-oder.
	 * - a single space is padded between two nodes, no space at the end.
	 * - return an empty string for null tree.
	 * O(n) where n is the size of the tree.
	 * @return String contains all the nodes following post-order traversal
	*/
	public String toStringPostOrder(){
		return helpStringPostOrder(this).trim();
	}

	/**
	 * PRE ORDER for K Tree.
	 * @param root takes root
	 * @return String to toStringKTreePreOrder()
	*/
	private String helpStringKTreePreOrder(FcnsTreeNode<E> root){
		//pre-order
		if(root == null){
			return "";
		}
		StringBuilder string = new StringBuilder();
		string.append(root.value);
		string.append(" ");
		String left = helpStringKTreePreOrder(root.firstChild);
		String right = helpStringKTreePreOrder(root.nextSibling);
		if((left != null) || (right != null)){
			string.append(left);
			string.append(right);
		}
		return string.toString();
	}
	/**
	 * For the FCNS tree rooted at this node, simulate tree traversal of the.
	 * original k-ary tree and include all (non-null) nodes in pre-order.
	 *  - a single space is padded between two nodes, no space at the end.
	 *  - return an empty string for null tree.
	 * O(n) where n is the size of the tree.
	 * @return String contains all the node following the pre-order traversal
	 */
	public String toStringKTreePreOrder(){
		return helpStringKTreePreOrder(this).trim();
	}

	/**
	 * POST ORDER for KTREE.
	 * @param root takes root
	 * @return String to toStringKTreePostOrder();
	 */
	private String helpStringKTreePostOrder(FcnsTreeNode<E> root){
		//same as in-order for FCNS Tree
		if(root == null){
			return "";
		}
		StringBuilder string = new StringBuilder();
		if(root.firstChild != null){
			string.append(helpStringKTreePostOrder(root.firstChild));
		}
		string.append(root.value);
		string.append(" ");
		if(root.nextSibling != null){
			string.append(helpStringKTreePostOrder(root.nextSibling));
		}
		return string.toString();
	}
	/**
	 * For the Fcns tree rooted at this node, simulate tree traversal of the .
	 * original k-ary tree and include all (non-null) nodes in post-order.
	 *  - a single space is padded between two nodes, no space at the end.
	 * 	- return an empty string for null tree.
	 * O(n) where n is the size of the tree.
	 * @return String contains all the node following the post-order of K tree
	 */
	public String toStringKTreePostOrder(){
		return helpStringKTreePostOrder(this).trim();
	}

	/**
	 * LEVEL ORDER KTree.
	 * @param root takes root
	 * @return String to toStringKTreeLevelOrder()
	*/
	private String helpStringKTreeLevelOrder(FcnsTreeNode<E> root){
		StringBuilder string = new StringBuilder();
		Queue<FcnsTreeNode<E>> queue = new Queue<FcnsTreeNode<E>>();
		queue.addLast(root);
		FcnsTreeNode<E> node = null;
		FcnsTreeNode<E> tempNode = null;

		while(!queue.isEmpty()){	
			node = queue.removeFirst();

			string.append(node.getValue());
			string.append(" ");

			while((node.getSibling() != null) || (node.getChild() != null)){
				if(node.getSibling() != null){
					tempNode = node.getSibling();
					string.append(tempNode.getValue());
					string.append(" ");
					
				}
				/*get child of node*/
				if(node.getChild() != null){
					queue.addLast(node.getChild());	
					node = node.getSibling();/*move node to its sibling*/
					if(node == null){
						break;//break inner loop
					}
				}	
				else{/*move node to its siblings */
					node = node.getSibling();
				}
		
			}
		}
		return string.toString();
	}
	/**
	 * For the Fcns tree rooted at this node, simulate tree traversal of the.
	 * original k-ary tree and include all (non-null) nodes in level-order.
	 * - a single space is padded between two nodes, no space at the end.
	 * - return an empty string for null tree.
	 * O(n) where n is the size of the tree.
	 * @return String contains all nodes following the level order of K tree
	 */
	public String toStringKTreeLevelOrder(){
		return helpStringKTreeLevelOrder(this).trim(); 
	}

	//-------------------------------------------------------------
	// You can add private helper methods or edit the main() below.
	//-------------------------------------------------------------
	/**
	 * main method for testing purposes.
	 * @param args command line argument
	 */
	public static void main(String[] args) {
		//Sample testing code
		// Fcns tree:
		//                  A
		//                 /
		//                B
		//               / \
		//              F   C
		//               \   \
		//                G   D

		// corresponding K-ary tree:
		//                   A
		//                 / | \
		//                B  C  D
		//               / \
		//              F   G

		//creating a tree
		FcnsTreeNode<String> node1 = new FcnsTreeNode<>("A");
		FcnsTreeNode<String> node2 = new FcnsTreeNode<>("B");
		FcnsTreeNode<String> node3 = new FcnsTreeNode<>("C");
		FcnsTreeNode<String> node4 = new FcnsTreeNode<>("D");
		FcnsTreeNode<String> node5 = new FcnsTreeNode<>("F");
		FcnsTreeNode<String> node6 = new FcnsTreeNode<>("G");
		
		node1.setChild(node2);
		node2.setChild(node5);
		node2.setSibling(node3);
		node5.setSibling(node6);
		node3.setSibling(node4);
		
		//size/minK
		if (node1.size() == 6 && node1.minK()==3){
			System.out.println("Yay1");				
		}

		//Fcns tree traversals
		if (node1.toStringLevelOrder().equals("A B F C G D") &&
			node1.toStringPostOrder().equals("G F D C B A")){
			System.out.println("Yay2");				
		}
		
		//K-ary tree traversals
		if (node1.toStringKTreeLevelOrder().equals("A B C D F G") &&
			node1.toStringKTreePreOrder().equals("A B F G C D") &&
			node1.toStringKTreePostOrder().equals("F G B C D A")){
			System.out.println("Yay3");				
		}
		
	
	}
	
	
	//If you need a queue, implement a private queue class using the template below.
	//Hint: 
	// - It should be easy to implement a queue using code from ThreeTenDLList of P2.
	// - You likely only need a subset of the code from ThreeTenDLList class.
	
	// Feel free to change the class definition but it has to be a private inner class.

	// Feel free to remove the class if you do not need to use it.
	/**
	 * Queue class.
	 * @param <T> takes any data types
	 */
	private class Queue<T> {

		/**
		 * instance variables: front.
		 */
		private Node<T> front;
		/**
		 * instance variables: back.
		 */
		private Node<T> back;

		/**
		 * node inner class.
		 * @param <T> takes any data types
		 */
		private class Node<T> {
			/**
			 * value .
			 */
			private T value;
			/**
			 * next, go to next node.
			 */
			private Node<T> next;
		
			/**
			 * Constructor.
			 * @param x is the value 
			 */
			public Node(T x){
				value = x;
				next = null;
			}
		}

		/**
		 * Queue Constructor.
		 */
		public Queue() {
			front = null; 
			back = null;
		}

		/**
		 * size, keep tracking number of nodes.
		 */
		private int size = 0;
		
		/**
		 * add to tail.
		 * @param value takes the value
		 */
		private void addLast(T value) {
			Node<T> newNode = new Node<T>(value);	
			if(back == null){
				front = newNode;
				back = newNode;
				size ++;
				return;
			}
			back.next = newNode;
			back = newNode;
			size++;
		}
		/**
		 * remove head.
		 * @return T value of the first node
		 */
		private T removeFirst(){
			T returnedValue;
			if(front == null){
				return null;
			}
			if(size == 1){
				returnedValue = front.value;
				front = front.next; /*let head point to the next node */
				back = back.next;
			}
			else{
				returnedValue = front.value;
				front = front.next;
			}
			size--;
			return returnedValue;	
		}

		/**
		 * check if queue is empty.
		 * @return return true if queue is empty, false otherwise
		 */
		private boolean isEmpty(){
			if(front == null){
				return true;
			}
			return false;
		}
	}
}
