package sshHandler;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.Properties;



public class sshHandler {

    private JSch jSch;
    private Session session;
    private Channel channel;
    private OutputStream outputStream;
    private InputStream inputStream;
    private My_logger logger;
    private static final int time_out = 10000;

    public sshHandler() throws FileNotFoundException {
        logger = My_logger.getInstance();
    }

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
            session.connect(time_out);

            // open channel to talk to other
            channel = session.openChannel("shell");
            channel.connect(time_out);

            // will receive commands through terminal
            inputStream = channel.getInputStream();

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    byte[] msg = new byte[512];
                    while(true){
                        while(inputStream.available() > 0){
                            int read = inputStream.read(msg, 0, msg.length);
                            String s = new String(msg, 0, read);
                            logger.log_msg(s);
                            System.out.println(s);
                        }
                        if(channel.isClosed()){
                            if(inputStream.available() > 0){
                                continue;
                            }
                            break;
                        }
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    channel.disconnect();
                    session.disconnect();
                }
            }
        }).start();
    }

}


class My_logger{

    private PrintWriter pw;
    private static final String start_log = "TCP_IP_log.txt";
    private static My_logger logger;

    private My_logger() throws FileNotFoundException {
        pw = new PrintWriter(start_log);
    }

    public synchronized void log_msg(String msg){
        pw.println(msg);
        pw.flush();
    }

    public static My_logger getInstance() throws FileNotFoundException {
        if(logger == null){
            logger = new My_logger();
        }
        return logger;
    }
}