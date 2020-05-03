import java.io.PrintWriter;
import java.lang.String;

//import javax.xml.crypto.URIReference;

public class BinarySearchTreeRQ implements Runqueue {
    protected Node root;
    int pt = 0; // Preceding process time
    int st = 0; // Succeeding process time
    boolean correctNode =false;

    public BinarySearchTreeRQ() {
        this.root = null;

    }  // end of BinarySearchTreeRQ()


    @Override
    public void enqueue(String procLabel, int vt) {
        if (findProcess(procLabel) != true){
            Process p = new Process(procLabel, vt);
            root = insert(root, p);
        }
    } // end of enqueue()

    //A recursive function to insert a new key in BST 
    private Node insert(Node n, Process p) { 
        /* If the tree is empty, return a new node */
        if (n == null) { 
            n = new Node(p); 
            return n; 
        } 
        /* Otherwise, recur down the tree */
        int vt = p.getvruntime();
        if (vt < n.getProcess().getvruntime()) 
            n.leftChild = insert(n.leftChild, p); 
        else 
            n.rightChild = insert(n.rightChild, p); 
  
        /* return the (unchanged) node pointer */
        return n; 
    } 


    @Override
    public String dequeue() {
        String label= "";
        Node current = root;
        Node parent = root;
        if (root == null)
            label = "";
        
        else {
            // traverse to the left most node
            while(current.leftChild != null){
                parent = current;
                current = current.leftChild;
            }
            label = current.getProcess().getProcLabel();
            if(current.rightChild == null){ // If the dequeueing node is a leaf
                if(current==root)
                    root = null;
                else{
                    parent.leftChild = null;
                    current = null;
                }
            }
            else{           // If the dequeueing node has a subtree(right)
                if(current==root)
                    root = current.rightChild;
                else{
                    parent.leftChild = current.rightChild;
                    current = null;
                }
            }
        }
        return label;
    } // end of dequeue()


    // A recursive method to search the corresponding node of a given procLabel
    public Node searchLabel(Node n, String procLabel) { 
        if (n != null){
            if(n.getProcess().getProcLabel().equals(procLabel)){
                //System.out.println(n.getProcess().getProcLabel());
                return n;
            }
            else{
                if(searchLabel(n.leftChild, procLabel) != null)
                return searchLabel(n.leftChild, procLabel);
            else if (searchLabel(n.rightChild, procLabel) != null)
                return searchLabel(n.rightChild, procLabel);
            else
                return null;
            }
        }
        else
            return null;
    } 

    @Override
    public boolean findProcess(String procLabel) {
        if (root == null) {
            return false;
        }
        else if (searchLabel(root, procLabel) != null)
            return true;
        else
            return false;
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        Node p = searchLabel(root, procLabel);
        if(p != null){
            int vt = p.getProcess().getvruntime();
            Node parent = root;
		    Node current = root;
            boolean isLeftChild = false;
            // boolean isLeftNull = false;
            // boolean isRightNull = false;
            // Finding the node position
            while(!current.getProcess().getProcLabel().equals(procLabel)){
			    parent = current;
			    if(current.getProcess().getvruntime()>vt){
				    isLeftChild = true;
				    current = current.leftChild;
			    }else{
				    isLeftChild = false;
				    current = current.rightChild;
			    }
		    }
		    //Case 1: if node to be deleted has no children
		    if(current.leftChild==null && current.rightChild==null){
			    if(current==root)
				    root = null;
			    if(isLeftChild ==true)
				    parent.leftChild = null;
			    else
				    parent.rightChild = null;
		    }
            //Case 2 : if node to be deleted has only one child
		    else if(current.rightChild==null){
			    if(current==root)
				    root = current.leftChild;
			    else if(isLeftChild)
				    parent.leftChild = current.leftChild;
			    else
				    parent.rightChild = current.leftChild;
		    }
		    else if(current.leftChild==null){
			    if(current==root)
				    root = current.rightChild;
			    else if(isLeftChild)
				    parent.leftChild = current.rightChild;
			    else
                    parent.rightChild = current.rightChild;
                
            }
            //Case 3 : if node to be deleted has more than one child
            else if(current.leftChild!=null && current.rightChild!=null){
			//now we have found the minimum element in the right tree
			    Node successor	 = getSuccessor(current);
			    if(current==root)
				    root = successor;
			    else if(isLeftChild)
				    parent.leftChild = successor;
			    else
				parent.rightChild= successor;		
			successor.leftChild = current.leftChild;
		    }		
		    return true;
        }

        else
            return false; 
    } // end of removeProcess()

    // A method to find the successor of the node to be deleted
    public Node getSuccessor(Node deleleNode){
		Node successsor =null;
		Node successsorParent =null;
		Node current = deleleNode.rightChild;
		while(current!=null){
			successsorParent = successsor;
			successsor = current;
			current = current.leftChild;
		}
		//check if successor has the rightChildchild, it cannot have left child for sure
		// if it does have the rightChilchild, add it to the left of successorParent.
		if(successsor!=deleleNode.rightChild){
			successsorParent.leftChild = successsor.rightChild;
			successsor.rightChild = deleleNode.rightChild;
		}
		return successsor;
	}

    // A method to calculate preceding process time for a given root
    public void PtCal(Node n, int vt, String procLabel){
        if(n != null){
            PtCal(n.leftChild, vt, procLabel); 

            if(n.getProcess().getProcLabel().equals(procLabel)) 
            	correctNode = true;
            if(n.getProcess().getvruntime() == vt && !correctNode) 
                pt = pt + n.getProcess().getvruntime();
            if(n.getProcess().getvruntime() < vt ) 
                pt = pt + n.getProcess().getvruntime();
            
            PtCal(n.rightChild, vt, procLabel);
        } 
    }

    @Override
    public int precedingProcessTime(String procLabel) {
        Node p = searchLabel(root, procLabel);
        if (p != null){ 
            pt = 0;
            int vt = p.getProcess().getvruntime();
            correctNode =false;
            PtCal(root, vt, procLabel);
            return pt;
        }
       return -1;
    } // end of precedingProcessTime()

    // A method to calculate succeding process time for a given root (inverse of in order)
    public void StCal(Node n, int vt, String procLabel){
        if(n != null){
            StCal(n.rightChild, vt, procLabel); 
            
            if(n.getProcess().getProcLabel().equals(procLabel)) 
            	correctNode = true;
            if(n.getProcess().getvruntime() == vt && !correctNode) 
                st = st + n.getProcess().getvruntime();
            if(n.getProcess().getvruntime() > vt ) 
                st = st + n.getProcess().getvruntime();

            StCal(n.leftChild, vt, procLabel);
        } 
    }
   
    @Override
    public int succeedingProcessTime(String procLabel) {
        Node p = searchLabel(root, procLabel);
        if (p != null){ 
            st = 0;
            int vt = p.getProcess().getvruntime();
            correctNode = false;
            StCal(root, vt, procLabel);
            return st;
        }
       return -1;
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        traverseInOrder(root);
        os.println();
    }// end of printAllProcess()

    public void traverseInOrder(Node n){
		if(n != null){
			traverseInOrder(n.leftChild);
			System.out.print(n.getProcess().getProcLabel() + " ");
			traverseInOrder(n.rightChild);
        }
	}


    // Node class
    class Node{
        Process proc;
        Node leftChild;
        Node rightChild;
        //int size;	// number of nodes in subtree
        public Node(Process p){
            this.proc = p;
            leftChild = null;
            rightChild = null;
        }

        public Process getProcess() {
            return proc;
        }
    }

} // end of class BinarySearchTreeRQ
