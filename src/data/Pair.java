package data;

public class Pair {

    private int val, sSeq, rSeq, numReaders;

    public Pair(int val, int sSeq, int rSeq, int numReaders){
        this.val = val;
        this.sSeq = sSeq;
        this.rSeq = rSeq;
        this.numReaders = numReaders;
    }

    public int getVal() {
        return val;
    }

    public int getsSeq() {
        return sSeq;
    }

    public int getrSeq() {
        return rSeq;
    }

    public int getNumReaders() {
        return numReaders;
    }


}
