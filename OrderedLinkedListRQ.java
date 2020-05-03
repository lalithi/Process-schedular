import java.io.PrintWriter;
import java.lang.String;

public class OrderedLinkedListRQ implements Runqueue {

    protected Node head;
    protected Node tail;
    protected int length;

    /**
     * Constructs empty linked list
     */
    public OrderedLinkedListRQ() {

       head = null;
       tail = null;
       length = 0;

    }  // end of OrderedLinkedList()


    @Override
    public void enqueue(String procLabel, int vt) {
        Process proc = new Process(procLabel, vt);

        Node newNode = new Node(proc);

        if (head == null) { // If list is empty
            head = newNode;
            tail = newNode;
            length += 1;
        }
        else if(findProcess(procLabel) != true){
            
            if (head.getProcess().getvruntime() > vt){
                newNode.setNext(head);
                head.setPrev(newNode);
                head = newNode;
            }
            else if (tail.getProcess().getvruntime() <= vt){
                tail.setNext(newNode);
                newNode.setPrev(tail);
                tail = newNode;
            }
            else{
                Node currNode = head;
                int index=0;
    
                for (int i = 0; i < length; i++){
                    if (currNode.getProcess().getvruntime() > vt) {
                        index = i;
                        break;
                    }
                    currNode = currNode.getNext();
                }
                insert(index, newNode);
            }
            length += 1;
        }
        
        
    } // end of enqueue()

    public void insert(int index, Node newNode){

        // depending on where index is, we either go forward or backward in list
        if (index < Math.ceil(length / 2)) {
            // go forward from head
            Node currNode = head;
            for (int i = 0; i < index-1; ++i) {
                    currNode = currNode.getNext();
            }

            // nextNode is the one being shift up
            Node nextNode = currNode.getNext();
            nextNode.setPrev(newNode);
            newNode.setNext(nextNode);
            newNode.setPrev(currNode);
            currNode.setNext(newNode);
            
        }
        else {
            // go backward from tail
            Node currNode = tail;
            for (int i = length-1; i > index; --i) {
                currNode = currNode.getPrev();
            }

            Node prevNode = currNode.getPrev();
            prevNode.setNext(newNode);
            newNode.setPrev(prevNode);
            newNode.setNext(currNode);
            currNode.setPrev(newNode);
        }
    } // end of insert()


    @Override
    public String dequeue() {
        if(head == null)
            return "";
        else {
            String p = head.getProcess().getProcLabel();
            head = head.getNext();
            length--;
            return p;
        }
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel) {
        Node currNode = head;
        Node prev = currNode;
        boolean processFound = false;

        while (currNode != null && !processFound){
            if(currNode.getProcess().getProcLabel().equals(procLabel)){
                processFound = true;
                break;
            }
            else {
                currNode = currNode.getNext();
            }
        }
        return processFound; 
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        if(findProcess(procLabel)){
            Node currNode = head;
            // check if value is head node
            if (currNode.getProcess().getProcLabel().equals(procLabel)) {
                if (length == 1) {
                    head = tail= null;
                }
                else {
                    head = currNode.getNext();
                    head.setPrev(null);
                    currNode = null;
                }
            }
            // search for value in rest of list
            else {
                currNode = currNode.getNext();
    
                while (currNode != null) {
                    if (currNode.getProcess().getProcLabel().equals(procLabel)) {
                        Node prevNode = currNode.getPrev();
                        prevNode.setNext(currNode.getNext());
                        // check if tail
                        if (currNode.getNext() == null) {
                            tail = prevNode;
                        }
                        else {
                            currNode.getNext().setPrev(prevNode); 
                        }
                        currNode = null;
                        break;
                    }
                    currNode = currNode.getNext();
                }
            }
            length--;
            return true;
        }
        else
            return false;    
    } // End of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
        if(!findProcess(procLabel)){
            return -1;
        }
        else if(head.getProcess().getProcLabel().equals(procLabel)){
            return 0;
        }
        else{
            Node currNode = head;
            int pt = 0;

            while (currNode != null){
                if(currNode.getProcess().getProcLabel().equals(procLabel)){
                    break;
                }
                else {
                pt += currNode.getProcess().getvruntime();
                currNode = currNode.getNext();
                }
            }
            return pt;
        } 
    } // end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        if(!findProcess(procLabel)){
            return -1;
        }
        else if(tail.getProcess().getProcLabel().equals(procLabel)){
            return 0;
        }
        else{
            Node currNode = tail;
            int st = 0;

            while (currNode != null){
                if(currNode.getProcess().getProcLabel().equals(procLabel)){
                    break;
                }
                else {
                    st += currNode.getProcess().getvruntime();
                    currNode = currNode.getPrev();
                }
            }
            return st;
        }
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) { 
        if (head == null) {
            os.println("");
        }
        else{
            Node currNode = head;
            while (currNode != null) { 
                // Print the procLabel at current node 
                os.printf(currNode.getProcess().getProcLabel() + " ");
                currNode = currNode.getNext(); 
            }
            os.printf("\n");
        }
    } // end of printAllProcess()

    //////////////////////////////////////

    private class Node
    {
        /** Stored value of node. */
        protected Process proc;
        /** Reference to next node. */
        protected Node next;
        protected Node prev;

        public Node(Process p) {
            this.proc = p;
            next = null;
            prev = null;
        }

        public Process getProcess() {
            return proc;
        }

        public Node getNext() {
            return next;
        }

        public Boolean hasNext() {
            if (next == null)
                return false;
            else
                return true;
        }

        public Node getPrev() {
            return prev;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    } // end of inner class Node

} // end of class OrderedLinkedListRQ
