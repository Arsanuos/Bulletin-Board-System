package thread;

import data.Data;

public class Reader implements Runnable {

    
    @Override
    public void run() {
        int data = Data.getInstance().getVal();
        //TODO:: print here.

    }
}
