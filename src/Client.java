/**
 * Created by collin on 3/14/2017.
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Client {

    private static Socket sockID;
    private static boolean stayConnected;


    public static void main(String[] args) throws IOException{
        sockID = new Socket("localhost", 1254);
        stayConnected = true;

        Thread listen_from_server = new Thread(new listen_to_server());
        Thread listen_from_user = new Thread(new listen_to_user());

    }

    private static class listen_to_server implements Runnable{
        @Override
        public void run(){
            InputStream s1in;

            while(stayConnected){
                try{
                    s1in = sockID.getInputStream();
                    DataInputStream dos = new DataInputStream (s1in);

                    if(dos != null){
                        parseMessage(dos);
                    }
                }
                catch(IOException e){

                }
            }
        }

        private void parseMessage(DataInputStream dos) throws IOException{
            String line = dos.readUTF();

            if(line.length()<1)
                return;

            char cmd = line.charAt(0);

            switch(cmd){
                case 'a' :
                    break;
                case 'b' :
                    break;
                case 'c' :
                    break;
                case 'd' :
                    break;
                case 'e' :
                    break;
                case 'f' :
                    break;
                case 'g' :
                    break;
                case 'h' :
                    break;
                case 'l' :
                    break;
                case 'n' :
                    break;
                case 'r' :
                    break;
                case 's' :
                    break;
                case 'w' :
                    break;
                default  :
                    break;
            }
        }
    }


    private static class listen_to_user implements Runnable{
        @Override
        public void run(){
            Scanner in = new Scanner(System.in);
            String line_in;

            while(stayConnected){
                line_in = in.nextLine();

                parseMessageFromUser(line_in);
            }
        }

        public void parseMessageFromUser(String line){
            if(line.length()<1)
                return;

            char cmd = line.charAt(0);

            switch(cmd){
                case 'a' :
                            break;
                case 'b' :
                            break;
                case 'c' :
                            break;
                case 'd' :
                            break;
                case 'e' :
                            break;
                case 'f' :
                            break;
                case 'g' :
                            break;
                case 'h' :
                            break;
                case 'l' :
                            break;
                case 'n' :
                            break;
                case 'r' :
                            break;
                case 's' :
                            break;
                case 'w' :
                            break;
                default  :
                            break;
            }
        }
    }

}
