import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel(" client area ");
    private JTextArea msgArea = new JTextArea();
    private JTextField msgInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {

        try {
            createGUI();
            handleEvents();
            System.out.println("client is connecting");

            socket=new Socket("127.0.0.1", 7778);
            System.out.println("connection established");
            // creating buffer for reading
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // java streams are unidirectional
            //for writing
            out=new PrintWriter(socket.getOutputStream());

            startReading();
            // startWriting();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void handleEvents() {
        msgInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub


            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // key code for enter is 10
                if( e.getKeyCode()==10)
                {
                    String msgToSend=msgInput.getText();
                    msgArea.append("Me :"+msgToSend+"\n");
                    out.println(msgToSend);
                    out.flush();
                    msgInput.setText("");
                    msgInput.requestFocus();
                }

            }

        } );
    }

    private void createGUI()
    {
        this.setTitle("Client Messenger");
        this.setSize(500,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        // setting each componnet
        heading.setFont(font);
        msgArea.setFont(font);
        msgInput.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        //frame layout
        this.setLayout(new BorderLayout());
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20)); 
        msgArea.setEditable(false);
       
        msgInput.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane jScrollPane=new JScrollPane(msgArea);
        // adding commponent to frames 
        this.add(heading,BorderLayout.NORTH);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(msgInput,BorderLayout.SOUTH);

    }
    public void startReading()
    {
        Runnable r1=()->{
            System.out.println("reader started......");
            try {
                while(true)
                {

                        String msg=br.readLine();
                        System.out.println("Server :"+msg);
                        msgArea.append("Server :"+msg+"\n");
                        msgArea.setCaretPosition(msgArea.getDocument().getLength());
                    if(msg.equals("bye")){
                        System.out.println("client done with chat");
                        JOptionPane.showMessageDialog(this , "client done with chat");
                        msgInput.setEnabled(false);
                        socket.close();
                        break;
                    } 
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
                        if(msg.equals("bye")){
                            System.out.println("client done with chat");
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

        System.out.println("this is client");
        new Client();
    }
}
