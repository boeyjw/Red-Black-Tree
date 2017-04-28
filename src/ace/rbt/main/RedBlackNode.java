package ace.rbt.main;

import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Class Node */
class RedBlackNode {    
	RedBlackNode left, right;
	int element;
	int color;
	int pos;

	/* Constructor */
	public RedBlackNode(int theElement) {
		this( theElement, null, null );
	} 

	/* Constructor */
	public RedBlackNode(int theElement, RedBlackNode lt, RedBlackNode rt) {
		left = lt;
		right = rt;
		element = theElement;
		color = 1;
		pos = 1;
	}    

}

/**
 * Sorts input tree in ascending position order.
 * This sorting is required as insertion order matters.
 * Worse case of O(n log n). Java built-in mergesort is used.
 */
class TreeElementStringSorter implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {
		//Filter for position and convert to primitive int for safe comparison.
		//If compare in String, the order returned may not be correct.
		return Integer.parseInt(o1.replaceFirst("\\d+[R|B]", "")) > Integer.parseInt(o2.replaceFirst("\\d+[R|B]", "")) ? 1 : -1;
	}
}

/* Class RBTree */
class RBTree {
	private RedBlackNode current;
	private RedBlackNode parent;
	private RedBlackNode grand;
	private RedBlackNode great;
	private RedBlackNode header;    
	private RedBlackNode saveTree; //Saves a tree structure
	private static RedBlackNode nullNode;

	/* static initializer for nullNode */
	static {
		nullNode = new RedBlackNode(0);
		nullNode.left = nullNode;
		nullNode.right = nullNode;
	}

	/* Black - 1  RED - 0 */
	static final int BLACK = 1;
	static final int RED   = 0;

	/* Constructor */
	public RBTree(int negInf) {
		header = new RedBlackNode(negInf);
		header.left = nullNode;
		header.right = nullNode;
		
		saveTree = nullNode;
	}

	/* Function to check if tree is empty */
	public boolean isEmpty() {
		return header.right == nullNode;
	}

	/* Make the tree logically empty */
	public void makeEmpty() {
		header.right = nullNode;
	}

	//=================================ADDED FUNCTIONALITY STARTS HERE================================	
	/**
	 * Inserts the user input tree into this tree.
	 * @param input An array of strings which represent each node's elementCOLOURposition.
	 */
	public boolean validateUserTree(String[] input) {
		if(input.length <= 1) {
			if(input.length == 0 || input[0].isEmpty() || input[0].equals("\"\"")) {
				System.out.println("Empty tree");
				return true;
			}
			else if(input[0].matches("\\d+B1")) {
				System.out.println("Root node\nBlack height: " + getBlackHeight(header.right));
				return true;
			}
			else {
				if(input[0].matches("\\d+B[^1]"))
					System.out.println("Node not root");
				else if(input[0].matches("\\d+R1"))
					System.out.println("Root not black");
				return false;
			}
		}
		
		//Sorts the array in ascending position order for easy insertion and validation
		Arrays.sort(input, new TreeElementStringSorter());
		
		if(input[0].matches("\\d+B[^1]")) {
			System.out.println("No root node");
			return false;
		}
		else if(input[0].matches("\\d+R\\d+")) {
			System.out.println("Root not black");
			return false;
		}
		
		//Saves any tree before this
		this.saveTree = isEmpty() ? nullNode : header.right;
		makeEmpty();
		
		//Initially, user input is assumed to be true
		boolean insertInputAsIs = true;
		//Precompile regex pattern
		Pattern strpat = Pattern.compile("\\d+[R]\\d+");
		Matcher matpat = strpat.matcher("");
		Pattern splitpat = Pattern.compile("(\\d+)[R|B](\\d+)");
		Matcher matsplit = splitpat.matcher("");
		
		//Insert user input into tree accordingly
		for(int i = 0; i < input.length; i++) {
			matsplit = matsplit.reset(input[i]);
			if(matsplit.find()) {
				matpat = matpat.reset(input[i]);
				//Insert into tree accordingly while validating each node as it inserts
				if(!insert(Integer.parseInt(matsplit.group(1)), matpat.matches() ? RED : BLACK, Integer.parseInt(matsplit.group(2)))) {
					//If validation on insert fails, mark user input as false
					insertInputAsIs = false;
					System.out.println("[VALIDATION ERROR] Constraint failed for node: " + input[i]);
					//Stop inserting incorrect nodes
					break;
				}
			}
			else {
				System.out.println("Incorrect input format. Format: elementCOLOURposition *space*");
				return false;
			}
		}
		
		//Get the black height of the user tree.
		int blackHeight = getBlackHeight(header.right);
		//Assign validity of user input tree
		boolean isUserTreeValid = insertInputAsIs && blackHeight > 0; 
		
		//Enters if insert validation fails or black height are dissimilar
		if(!insertInputAsIs || blackHeight == 0) {
			System.out.println("[VALIDATION ERROR] Black height of tree are dissimilar.");
			//Empty the user tree to begin inserting using program default insertion
			makeEmpty();
			for(int i = 0; i < input.length; i++) {
				matsplit = matsplit.reset(input[i]);
				if(matsplit.find())
					insert(Integer.parseInt(matsplit.group(1)));
				//Placed for fail safe
				else {
					System.out.println("Incorrect input format. Format: elementCOLOURposition *space*");
					return false;
				}
			}
		}
		System.out.println("Black Height: ".concat(blackHeight > 0 ? String.valueOf(blackHeight) : String.valueOf(getBlackHeight(header.right))) + 
				" (".concat(insertInputAsIs && blackHeight > 0 ? "INPUT" : "CORRECTED") + ")");
		
		//Save validated tree for future
		swapSave();
		
		return isUserTreeValid;
	}
	
	/**
	 * Logically swaps from current tree to previous tree.
	 */
	public void swapTree() {
		if(this.saveTree == nullNode && isEmpty()) {
			System.out.println("No tree found. Only validating tree (case 6) will trigger the save and swap.");
			return;
		}
		else if(this.saveTree == nullNode) {
			System.out.println("No other tree found.");
			return;
		}
		
		swapSave();
	}
	
	/**
	 * Swap from current tree to previously saved tree.
	 */
	private void swapSave() {
		RedBlackNode prevTree = isEmpty() ? nullNode : header.right;
		header.right = this.saveTree;
		this.saveTree = prevTree;
	}

	/**
	 * Compare the black height of left and right subtree.
	 * @param node Root node
	 * @return Number of black nodes in any path or 0 if invalid red black tree
	 */
	private int getBlackHeight(RedBlackNode node) {
		if(node == nullNode)
			return 1;
		
		int lbh = getBlackHeight(node.left);
		if(lbh == 0)
			return lbh;
		int rbh = getBlackHeight(node.right);
		if(rbh == 0)
			return rbh;
		
		return lbh != rbh ? 0 : lbh + node.color;
	}
	
	/**
	 * Online validation of user tree as each node is inserted.
	 * @param item Node element.
	 * @param colour Node color.
	 * @param pos Node position.
	 * @return True if parent of node is not red or position is placed correctly. False otherwise.
	 */
	private boolean insert(int item, int colour, int pos) {
		current = parent = grand = header;
		nullNode.element = item;
		
		while (current.element != item) {
			great = grand;
			grand = parent;
			parent = current;
			current = item < current.element ? current.left : current.right;
		}
		
		//Ignore node if node has same element as parent
		if (current != nullNode){
			System.out.println("Ignoring insertion for node: " + String.valueOf(item).concat(colour == RED ? "R" : "B") + pos);
			return true;
		}

		current = new RedBlackNode(item, nullNode, nullNode);
		
		//If node has parent, check its positioning
		if(parent.element != Integer.MIN_VALUE) {
			if(pos == parent.pos * 2 && item < parent.element) {
				parent.left = current;
			}
			else if(pos == parent.pos * 2 + 1 && item > parent.element) {
				parent.right = current;
			}
			//Violation of node positioning
			else {
				return false;
			}
		}
		//The node is a root
		else {
			parent.right = current;
		}
		
		//Rejects tree if node parent is red
		if(colour == RED && parent.color == RED && header.right != nullNode) {
			return false;
		}
		
		//Set properties
		current.color = colour;
		current.pos = pos;
		
		return true;
	}
	//=================================ADDED FUNCTIONALITY ENDS HERE================================
	
	//=================================MODIFIED SLIGHTLY STARTS HERE================================
	/* Function to insert item */
	public void insert(int item) {
		current = parent = grand = header;
		nullNode.element = item;

		while (current.element != item) {
			great = grand;
			grand = parent;
			parent = current;
			current = item < current.element ? current.left : current.right;

			// Check if two red children and fix if so
			if (current.left.color == RED && current.right.color == RED)
				handleReorient(item);
		}

		// Insertion fails if already present
		if (current != nullNode)
			return;

		current = new RedBlackNode(item, nullNode, nullNode);
		
		// Attach to parent
		if (item < parent.element)
			parent.left = current;
		else
			parent.right = current;
		
		//Assign each node with a position.
		//Root is always 1 whereas other nodes are positioned from left to right, breadth first search structure.
		current.pos = current == header.right ? 1 : current == parent.left ? parent.pos * 2 : parent.pos * 2 + 1;
		
		handleReorient(item);
	}

	private void handleReorient(int item) {
		// Do the color flip
		current.color = RED;
		current.left.color = BLACK;
		current.right.color = BLACK;
		
		if (parent.color == RED) {
			// Have to rotate
			grand.color = RED;
			
			if (item < grand.element != item < parent.element) {
				parent = rotate( item, grand );  // Start dbl rotate
			}

			current = rotate(item, great );
			current.color = BLACK;
			//Preorder repositioning with subtree if root not involved in reorientation.
			//Worse case O(n) if root is reoriented.
			reposition(great, great == header.right ? 1 : great.pos);
		}
		// Make root black
		header.right.color = BLACK;
	}
	
	private void reposition(RedBlackNode subtree, int pos) {
		if(subtree != nullNode) {
			subtree.pos = pos;
			reposition(subtree.left, pos * 2);
			reposition(subtree.right, pos * 2 + 1);
		}
	}
	//=================================MODIFIED SLIGHTLY ENDS HERE=================================
	
	private RedBlackNode rotate(int item, RedBlackNode parent) {
		if(item < parent.element)
			return parent.left = item < parent.left.element ? rotateWithLeftChild(parent.left) : rotateWithRightChild(parent.left) ;  
		else
			return parent.right = item < parent.right.element ? rotateWithLeftChild(parent.right) : rotateWithRightChild(parent.right);  

	}

	/* Rotate binary tree node with left child */
	private RedBlackNode rotateWithLeftChild(RedBlackNode k2) {
		RedBlackNode k1 = k2.left;

		k2.left = k1.right;
		k1.right = k2;

		return k1;
	}

	/* Rotate binary tree node with right child */
	private RedBlackNode rotateWithRightChild(RedBlackNode k1) {
		RedBlackNode k2 = k1.right;

		k1.right = k2.left;
		k2.left = k1;

		return k2;
	}

	/* Functions to count number of nodes */

	public int countNodes() {
		return countNodes(header.right);
	}

	private int countNodes(RedBlackNode r) {
		if (r == nullNode)
			return 0;
		else {
			int l = 1;
			l += countNodes(r.left);
			l += countNodes(r.right);
			return l;
		}
	}

	/* Functions to search for an element */

	public boolean search(int val) {
		return search(header.right, val);
	}

	private boolean search(RedBlackNode r, int val) {
		boolean found = false;

		while ((r != nullNode) && !found) {
			int rval = r.element;
			if (val < rval)
				r = r.left;
			else if (val > rval)
				r = r.right;
			else {
				found = true;
				break;
			}
			found = search(r, val);
		}
		return found;
	}

	/* Function for inorder traversal */ 
	public void inorder() {
		inorder(header.right);
	}

	private void inorder(RedBlackNode r) {
		if (r != nullNode) {
			inorder(r.left);
			char c = 'B';
			if (r.color == 0)
				c = 'R';
			System.out.print(r.element +""+c+r.pos+" ");
			inorder(r.right);
		}
	}

	/* Function for preorder traversal */
	public void preorder() {
		//Root node position is 1. Imitating Heap configuration.
		preorder(header.right);
	}

	private void preorder(RedBlackNode r) {
		if (r != nullNode) {
			char c = 'B';
			if (r.color == 0)
				c = 'R';
			System.out.print(r.element +""+c+r.pos+" ");
			preorder(r.left);
			preorder(r.right);
		}
	}

	/* Function for postorder traversal */
	public void postorder() {
		postorder(header.right);
	}

	private void postorder(RedBlackNode r) {
		if (r != nullNode) {
			postorder(r.left);             
			postorder(r.right);
			char c = 'B';
			if (r.color == 0)
				c = 'R';
			System.out.print(r.element +""+c+r.pos+" ");
		}
	}
}
