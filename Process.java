
public class Process {

    private int vruntime;
    private String procLabel;

    public Process (String procLabel, int vruntime){
        this.procLabel = procLabel;
        this.vruntime = vruntime;
    }

    /**
     * @return the procLabel
     */
    public String getProcLabel() {
        return procLabel;
    }

    public int getvruntime(){
        return vruntime;
    }

    // public void setvruntime(int vt){
    //     this.vt = vt;
    // }
}