import java.net.*;

//import jdk.internal.org.jline.utils.InputStreamReader;

import java.io.*;
public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Server()
    {
        
        try {
            server=new ServerSocket(7778);
            System.out.println("Server is ready to connect");
            System.out.println("waiting......");

            socket=server.accept();
            // creating buffer for reading 
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // java streams are unidirectional
            //for writing 
            out=new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        
         
    }
    public void startReading()
    {
        Runnable r1=()->{
            System.out.println("reader started......");
            
           try {
                while(true)
                {
                    String msg=br.readLine();
                    if(msg.equals("bye")){
                        System.out.println("Server done with chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client :"+msg);
                }
           } catch (Exception e) {
               //TODO: handle exception
               System.out.println("connection closed");
           }

        };
        new Thread(r1).start();
    }
    public void startWriting()
    {
        Runnable r2=()->{
            System.out.println("writer started.......");
            try {
                while(true && socket.isClosed()==false)
                {                                     
                    //reading data from buffer which reads from consle(user)
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String msg=br1.readLine(); 
                    out.println(msg);
                    out.flush();
                    if(msg.equals("bye"))
                    {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                //TODO: handle exception
                System.out.println("connection closed");
            }
        };
        new Thread(r2).start();
    }
    public static void main(String []args){
        new Server();
    }
}
