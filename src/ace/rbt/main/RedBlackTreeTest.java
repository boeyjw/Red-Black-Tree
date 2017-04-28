package ace.rbt.main;

import java.util.InputMismatchException;
import java.util.Scanner; 
/* Class RedBlackTreeTest */

public class RedBlackTreeTest {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        /* Creating object of RedBlack Tree */
        RBTree rbt = new RBTree(Integer.MIN_VALUE); 
        System.out.println("Red Black Tree Test\n");          
        char ch;

        /*  Perform tree operations  */
        do {
            System.out.println("\nRed Black Tree Operations\n");
            System.out.println("1. insert ");
            System.out.println("2. search");
            System.out.println("3. count nodes");
            System.out.println("4. check empty");
            System.out.println("5. clear tree");
            System.out.println("6. Check whether valid RBT");
            System.out.println("7. Swap between previous tree and current tree if exists. Stores up to 2 trees.");

            int choice = scan.nextInt();
            switch (choice) {
                case 1 : 
                    System.out.println("Enter integer element to insert");
                    rbt.insert(scan.nextInt());                     
                    break;                          
                case 2 : 
                    System.out.println("Enter integer element to search");
                    System.out.println("Search result : "+ rbt.search(scan.nextInt()));
                    break;                                          
                case 3 : 
                    System.out.println("Nodes = "+ rbt.countNodes());
                    break;     
                case 4 : 
                    System.out.println("Empty status = "+ rbt.isEmpty());
                    break;     
                case 5 : 
                    System.out.println("\nTree Cleared");
                    rbt.makeEmpty();
                    break;
                case 6 :
                	scan.nextLine();
                	System.out.print("Enter elements (format: elementCOLOURposition *space*) (root node index: 1, left to right increment): ");
                	try {
                		boolean isValid = rbt.validateUserTree(scan.nextLine().toUpperCase().trim().split("\\s+|,\\s+|,"));
                		System.out.println("RBT validity: " + isValid);
                		System.out.println("Use case 7 to display ".concat(isValid ? "validated" : "corrected") + " input tree");
                	} catch(InputMismatchException ime) {
                		System.err.println("Invalid format (InputMismatchException): " + ime.getMessage());
                	} catch(NumberFormatException nfe) {
                		System.err.println("Invalid format (NumberFormatException): " + nfe.getMessage());
                	}
                	break;
                case 7:
                	rbt.swapTree();
                	break;
                default :
                    System.out.println("Wrong Entry \n ");
                    break;
            }
            
            /*  Display tree  */
            System.out.print("\nPost order : ");
            rbt.postorder();
            System.out.print("\nPre order : ");
            rbt.preorder();
            System.out.print("\nIn order : ");
            rbt.inorder();

            System.out.println("\nDo you want to continue (Type y or n) \n");
            ch = scan.next().charAt(0);
        } while (ch == 'Y'|| ch == 'y');
        
        scan.close();
    }
}