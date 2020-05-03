import java.io.PrintWriter;
import java.lang.String;


public class OrderedArrayRQ implements Runqueue {

    public Process rq[];

    public OrderedArrayRQ() {
        rq = new Process[0];
    }  


    @Override
    public void enqueue(String procLabel, int vt) {
        Process p = new Process(procLabel, vt);
        Process[] rq_new = new Process[rq.length + 1];

        if (rq.length == 0){
            rq_new[0] = p;
            rq = rq_new;
        }
        else if(findProcess(procLabel) != true){
            int index = rq.length;

            for (int i=0; i < rq.length; i++){
                if(rq[i].getvruntime() > vt){
                    index = i;
                    break;
                }
            }

            for (int i=0; i < rq_new.length; i++){

                if (i < index)
                    rq_new[i]= rq[i];
            
                else if (i == index)
                    rq_new[i] = p;
            
                else 
                    rq_new[i] = rq[i-1];
            }
            rq = rq_new;
        }
        
        

    } // end of enqueue()


    @Override
    public String dequeue() {
        if (rq.length == 0){
            return "";
        }
        else{
            String process = rq[0].getProcLabel();
            Process[] rq_new = new Process[rq.length - 1];

            for (int i=0; i < rq_new.length; i++){
                rq_new[i] = rq[i+1];
            }
            rq = rq_new;
            return process;
        }

    } // end of dequeue()




    @Override
    public boolean findProcess(String procLabel){
        boolean processFound = false;

            for (int i=0; i < rq.length; i++){
                if(rq[i].getProcLabel().equals(procLabel)){
                    processFound = true;
                    break;
                }
            }
        return processFound;
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {

        if(findProcess(procLabel)){
            Process[] rq_new = new Process[rq.length - 1]; // New array to sort remaining processes
            int index = 0;

            for (int i=0; i < rq.length; i++){ // Finding the index of process to be removed
                if(rq[i].getProcLabel().equals(procLabel))
                    index = i;
            }
                    
            for(int i = 0; i<index ; i++){
                rq_new[i] = rq[i];
            }
            for(int i = index; i < rq_new.length; i++){
                rq_new[i] = rq[i+1];
            }
            rq = rq_new;
            return true;
        }
        else
            return false;
    } // end of removeProcess()




    @Override
    public int precedingProcessTime(String procLabel) {
        
        if(findProcess(procLabel)){ 
            int pt = 0;
            int index = 0;
            for (int i=0; i < rq.length; i++){
                if(rq[i].getProcLabel().equals(procLabel)){
                    index = i;
                    break;
                }
            }
            for(int i = 0; i<index ; i++){
                pt += rq[i].getvruntime();
            }
            return pt;   
        }
        else
            return -1;
    }// end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        if(findProcess(procLabel)){ 
            int st = 0;
            int index = 0;
            for (int i=0; i < rq.length; i++){
                if(rq[i].getProcLabel().equals(procLabel)){
                    index = i;
                    break;
                }
            }
            for(int i = rq.length-1; i>index ; i--){
                st += rq[i].getvruntime();
            }
            return st;   
        }
        else
            return -1;
    } // end of precedingProcessTime()



    @Override
    public void printAllProcesses(PrintWriter os) {
        if (rq.length == 0){
            os.println("");
            //System.out.print("");
        }
        else{
            for (int i=0; i < rq.length; i++) {
                os.printf(rq[i].getProcLabel() + " ");
            }
            os.printf("\n");
        }
        
    } 

} // end of class OrderedArrayRQ
