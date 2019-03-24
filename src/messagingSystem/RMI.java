package messagingSystem;

import configuration.Configuration;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI {
    private static String ip = Configuration.getInstance().getProp("RW.server");
    private static int registryPort = Integer.valueOf(Configuration.getInstance().getProp("RW.rmiregistry.port"));
    private static Registry registry;


    private RMI() {
    }

    public static Registry getInstance(){
        //open socket with server.
        try {
             registry = LocateRegistry.getRegistry(ip, registryPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return registry;
    }
}
