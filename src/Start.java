import configuration.Configuration;

public class Start {

    public static void main(String[] args) {
        // load configuration
        Configuration configuration = Configuration.getInstance();

        // start server (new thread) when accepts new req will pass it to new req.

        // start clients using ssh each client is either reader or writer .
    }
}
