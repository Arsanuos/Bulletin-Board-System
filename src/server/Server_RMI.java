package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server_RMI extends Remote {

    String apply(boolean type, int id) throws RemoteException;

}
