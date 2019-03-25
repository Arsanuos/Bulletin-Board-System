package sshHandler;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.OutputStream;
import java.util.Properties;

public class sshHandler {

    private JSch jSch;
    private Session session;
    private Channel channel;
    private OutputStream outputStream;

    public boolean canConnect(String username, String password, String ip, int port){

        boolean status = false;
        jSch = new JSch();

        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");

        try {

            // make session and open connection
            session = jSch.getSession(username, ip, port);
            session.setConfig(properties);
            session.setPassword(password);
            session.connect();

            // open channel to talk to other
            channel = session.openChannel("shell");
            channel.connect();

            // will receive commands through terminal
            channel.setInputStream(System.in);

            outputStream = channel.getOutputStream();

            status = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public boolean applyCommand(String command){
        boolean status = false;
        try {

            this.outputStream.write(command.getBytes());

            this.outputStream.flush();

            Thread.sleep(800);

            status = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public void close(){
        channel.disconnect();
        session.disconnect();
    }


}
