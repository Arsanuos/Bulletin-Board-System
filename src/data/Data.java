package data;

public class Data {

    private int val;
    private static Data data = new Data();

    private Data(){
        this.val = -1;
    }

    public int getVal(){
        return this.val;
    }

    public synchronized void setVal(int val){
        this.val = val;
    }

    public static Data getInstance(){
        return data;
    }

}
