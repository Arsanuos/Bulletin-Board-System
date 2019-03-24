package thread;

import data.Data;

public class Reader implements Runnable {

    @Override
    public void run() {
        Data.getInstance().getVal();
    }
}
