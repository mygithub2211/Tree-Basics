import java.util.Scanner;
import java.io.File;
import java.io.IOException;

/**
 *  A textual UI to help you to interact with ThreeTenKTree.
 *  Use with the command:
 * 		java KTreeUI Input_File_Name
 *  
 *  @author Y. Zhong
 */	
public class KTreeUI {

	/**
	 * String for better formatting.
	 */
	private static String divider = "----------------------------------------\n";
	
	/**
	 * kTree to interact with.
	 */
	private static ThreeTenKTree<String> kTree = null;

	/**
	 * Scanner to get input from keyboard.
	 */
	private static Scanner stdIn = null;

	/**
	 *  The main method that presents the UI.
	 *  
	 *  @param args command line args: first arg specifies an input file 
	 */
	public static void main(String[] args) {
		//open and read from input file to initialize K-tree
		if(args.length != 1){
			System.out.println("Usage: java KTreeUI Input_File_Name");
			return;
		}
		else {
			kTree = fileToTree(args[0]);
			if (kTree==null){
				System.out.println("File " + args[0] + " initialization error.");
				return;
			}
		}
				
		int option;
		FcnsTreeNode<String> fcnsRoot;
		stdIn = new Scanner(System.in);
		while(true){
			displayMenu();
			
			option = stdIn.nextInt(); //get the next menu choice
			stdIn.nextLine();
			switch(option){
				case 1: //display k-ary tree with null nodes
					System.out.print(divider);
					System.out.println(kTree.toString());
					break;
				case 2: //k-ary tree level order traversal (w/o null nodes)
					System.out.print(divider);
					System.out.println(kTree.toStringLevelOrder());
					break;
				case 3: //display first-child-next-sibling tree
					System.out.print(divider);
					fcnsRoot = ThreeTenKTree.createFcnsTree(kTree);
					System.out.println("Mink: "+fcnsRoot.minK());
					System.out.print(divider);
					System.out.print("FCNS Tree Level-Order:");					
					System.out.println(fcnsRoot.toStringLevelOrder());
					System.out.print(divider);
					System.out.print("FCNS Tree Post-Order:");					
					System.out.println(fcnsRoot.toStringPostOrder());
					break;
				case 4: //use a first-child-next-sibling tree to simulate traversals
					fcnsRoot = ThreeTenKTree.createFcnsTree(kTree);
					System.out.print(divider);
					System.out.print("FCNS Tree Simulating K-ary Tree Level-Order:");					
					System.out.println(fcnsRoot.toStringKTreeLevelOrder());
					System.out.print(divider);
					System.out.print("FCNS Tree Simulating K-ary Tree Pre-Order:");					
					System.out.println(fcnsRoot.toStringKTreePreOrder());
					System.out.print(divider);
					System.out.print("FCNS Tree Simulating K-ary Tree Post-Order:");					
					System.out.println(fcnsRoot.toStringKTreePostOrder());
					break;
				case 5: //ask for a value, report details of the node with this value
					displayNode();
					break;
				case 6: //remove a leaf node by value
					removeNode();
					break;
				case 7: //exit
					System.out.println("Good-bye!");
					return;
				default:
					System.out.println("Option not supported!");
			
			}			
		}

	
	}
	
	/**
	 *  The method that initialize a K-ary tree from file.
	 *  
	 *  @param fileName name of the input file 
	 *  @return the created k-ary tree
	 */
	public static ThreeTenKTree<String> fileToTree(String fileName){
		Scanner s = null;
		try{
			// open file for input
			s = new Scanner(new File(fileName));				
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		
		int k = Integer.parseInt(s.next());
		ThreeTenKTree<String> tree = new ThreeTenKTree<>(1, k); //start with one item
		
		for(int i = 0; s.hasNext(); i++) {
			String val = s.next();
			if (val.equals("_")) //null nodes should be default array item
				continue;
			tree.set(i, val); //expand storage as needed
		}
		
		return tree;
	}

	/**
	 *  The method that displays the menu.
	 *  
	 */
	private static void displayMenu(){
		System.out.println("\nPlease select from the following options:");
		System.out.println("1 - Display whole K-ary tree (w/ null nodes)"); 
		System.out.println("2 - Display K-ary tree in LEVEL-ORDER (w/o null nodes)");
		System.out.println("3 - Display its FCNS tree");
		System.out.println("4 - Use FCNS tree to simulate K-ary tree traversals");
		System.out.println("5 - Show details of one node in tree");
		System.out.println("6 - Remove a node");
		System.out.println("7 - Exit");
		System.out.print(divider);
		System.out.print("Your choice (1-7): ");	
	}
	
	/**
	 *  The method that displays the details of one node.
	 *  
	 */
	private static void displayNode(){
		//asking for a value
		System.out.print("Please enter the name of a value/node: ");
		String name = stdIn.nextLine();
		
		//verify value in tree
		if (!kTree.has(name)){
			System.out.println("Value " + name + " not present.");
			return;
		}
				
		//display details 
		System.out.println("Path from root to this node:");
		System.out.println(kTree.getAncestors(name));
		
		System.out.println("Children of this node:");
		System.out.println(kTree.getChildren(name));
		
	
	}

	/**
	 *  The method that removes one leaf node.
	 *  
	 */
	private static void removeNode(){
		//asking for a value
		System.out.print("Please enter the node/value to remove: ");
		String name = stdIn.nextLine();
		
		//verify value in tree
		if (!kTree.has(name)){
			System.out.println("Value " + name + " not present.");
			return;
		}
			
		//is leaf node?
		if (kTree.isLeaf(name)){
			if (kTree.remove(name)){
				System.out.println("Value " + name + " removed.");
			}
			else
				System.out.println("Value " + name + " cannot be removed.");		
		}
		else{
			System.out.println("Cannot remove a value in a non-leaf node.");
		}	
	
	}



}