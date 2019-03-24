package data;


import configuration.Configuration;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Data {

    private int val, sSeq, rSeq, numReaders, numberOfRequests;
    private static Data data = new Data();
    private ReadWriteLock lock;
    private Lock writeLock;
    private Lock readLock;

    private Data(){
        this.val = -1;
        this.sSeq = 0;
        this.numReaders = 0;
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        this.numberOfRequests = this.getNumberOfReq();
    }

    private int getNumberOfReq(){
        return Integer.valueOf(Configuration.getInstance().getProp("RW.numberOfAccesses")) *
                (Integer.valueOf(Configuration.getInstance().getProp("RW.numberOfReaders")) +
                Integer.valueOf(Configuration.getInstance().getProp("RW.numberOfWriters")));
    }

    public Pair getVal(){
        //Write lock on sSeq, rSeq and val
        writeLock.lock();
        this.sSeq++;
        this.rSeq++;
        this.numReaders++;
        this.numberOfRequests--;
        writeLock.unlock();

        // the time at which a thread can acquire readlock is either when
        // no one write to the local instances.
        readLock.lock();
        Pair tmp = new Pair(this.val, this.sSeq, this.rSeq, this.numReaders);
        readLock.unlock();

        writeLock.lock();
        this.numReaders--;
        writeLock.unlock();

        return tmp;
    }

    public Pair setVal(int val){

        writeLock.lock();
        this.sSeq++;
        this.val = val;
        this.numberOfRequests--;
        writeLock.unlock();

        readLock.lock();
        Pair tmp = new Pair(this.val, this.sSeq, this.rSeq, this.numReaders);
        readLock.unlock();

        return tmp;
    }

    public static Data getInstance(){
        return data;
    }

    public int getNumberOfRequests(){
        readLock.lock();
        int tmp = this.numberOfRequests;
        readLock.unlock();
        return tmp;
    }

}
