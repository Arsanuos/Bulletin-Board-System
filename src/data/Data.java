package data;

public class Data {

    private int val, sSeq;
    private static Data data = new Data();


    private Data(){
        this.val = -1;
        this.sSeq = 0;
    }

    public synchronized Pair getVal(){
        this.sSeq++;
        return new Pair(this.val, this.sSeq);
    }

    public synchronized int setVal(int val){
        this.sSeq++;
        this.val = val;
        return this.sSeq;
    }

    public static Data getInstance(){
        return data;
    }

}
