/**
 * Created by collin on 3/14/2017.
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Client {

    protected static Socket sockID;
    protected static boolean stayConnected;


    public static void main(String[] args) throws IOException{
        sockID = new Socket("localhost", 1254);
        stayConnected = true;

        Thread listen_from_server = new Thread(new listen_to_server());
        listen_from_server.start();
        Thread listen_from_user = new Thread(new listen_to_user());
        listen_from_user.start();

    }

    /**
     * This Runnable object is used in a thread that is constantly listening to the server for protocol packets.
     * Once a packet is received, its sent to the parseMessageFromServer() where a switch is used delegate what to do next based on information from the packet.
     */
    private static class listen_to_server implements Runnable{
        @Override
        public void run(){
            InputStream s1in;

            while(stayConnected){
                try{
                    s1in = sockID.getInputStream();
                    DataInputStream dis = new DataInputStream (s1in);

                    if(dis.available() > 0){
                        parseMessageFromServer(dis);
                    }
                }
                catch(IOException e){

                }
            }
        }

        private void parseMessageFromServer(DataInputStream in) throws IOException{
            char[] protocol_formed_charArray = DataInput_to_charArray(in);

            char cmd = protocol_formed_charArray[0];

            //NOTE SOME OF THESE COMMANDS ARE NOT IMPLEMENTED PLEASE REFER TO THE PROTOCOL DOCUMENT.
            switch(cmd){
                case 'a' : //TODO
                    break;
                case 'b' : //TODO
                    break;
                case 'c' : //TODO
                    break;
                case 'd' : //TODO
                    break;
                case 'e' : //TODO
                    break;
                case 'f' : //TODO
                    break;
                case 'g' : //TODO
                    break;
                case 'h' : //TODO
                    break;
                case 'l' : //TODO
                    break;
                case 'n' : //TODO
                    break;
                case 'r' : //Doesn't exist because this protocol is questionable
                    break;
                case 's' : //TODO
                    break;
                case 'w' : //TODO
                    break;
                default  : receivedTransmission(protocol_formed_charArray);
                    break;
            }
        }

        /**
         * Receives a packet from server with '\0' as the cmd
         * Parses the packet and forms a message that is printed to the clients screen in which shows the username of the person who sent the packet followed by their message.
         * @param protocolPacketReceived
         */
        public void receivedTransmission(char[] protocolPacketReceived){

            String sender="";
            String temp="";
            String message="";
            int length_of_message=0;

            //Retrieves the user's name
            for(int i=1; i<20; i++){
                if(protocolPacketReceived[i]=='\0')
                    break;

                sender+= protocolPacketReceived[i];
            }

            //Retrieves the length of the message as a string
            for(int i = 21; i<28; i++){
                if(protocolPacketReceived[i]=='\0')
                    break;

                temp += protocolPacketReceived[i];
            }

            //Parses the aboves string into an int
            try{
                length_of_message = Integer.parseInt(temp);
            }
            catch(NumberFormatException e){
                e.printStackTrace();
            }

            //Retrieves the message
            for(int i = 29; i < length_of_message+29; i++){
                if(protocolPacketReceived[i]=='\0')
                    break;
                message += protocolPacketReceived[i];
            }

            String themessage = sender + ": " + message;

            System.out.println(themessage);
        }

        /**
         *   Creates a char[] from the DataInputStream
         *   returns a char[]
         */
        public char[] DataInput_to_charArray(DataInputStream in){
            char[] buff = new char[262173];
            int counter =0;

            try {
                for(int i=0; i< 262173; i++) {
                    buff[counter] = in.readChar();
                    counter ++;
                }
            }
            catch(IOException e){
                return null;
            }

            return buff;

        }
    }


    /**
     * This runnable object is used to listen to the User for keyboard inputs.
     * Once a line is read, it is sent to the parseMessageFromUser() methods where it will parse based off the first letter of the line.
     * Once parsed, it will send a protocol formatted packet to the server.
     */
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
                case 'a' : //TODO
                            break;
                case 'b' : //TODO
                            break;
                case 'c' : //TODO
                            break;
                case 'd' : //TODO
                            break;
                case 'e' : //TODO
                            break;
                case 'f' : //TODO
                            break;
                case 'g' : //TODO
                            break;
                case 'h' : //TODO
                            break;
                case 'l' : //TODO
                            break;
                case 'n' : //TODO
                            break;
                case 'r' :  sendRoomMessage(line);
                            break;
                case 's' : //TODO
                            break;
                case 'w' :  sendPrivateMessage(line);
                            break;
                default  : //TODO
                            break;
            }
        }



        /**
         * Creates and sends protocol char[] indicating a message to the chat room the user is currently in.
         *
         */
        public void sendRoomMessage(String line){

            if(line.length() < 3)
                return;

            String message = null;

            // removes the r in front of the message and the following space.
            if(line.contains(" ")){
                message= line.substring(line.indexOf(" ")+1, line.length());
                char[] toServer = protocolFormer('r',null, message);
                sendToServer(toServer);
            }
        }
        
         /**
         * Creates and sends protocol char[] indicating a message to the specified user.
         *
         */
        public void sendPrivateMessage(String line){

            if(line.length() < 3)
                return;

            String message = null;
            String option  = null;

            // removes the w in front of the message and the following space.
            if(line.contains(" ")){
                message = line.substring(line.indexOf(" ")+1, line.length());
                
                // removes the w in front of the message and the following space.
                if(message.contains(" ")){
                	option = message.substring(0, message.indexOf(" "));
                	message= message.substring(message.indexOf(" ")+1, message.length());
                	
                    char[] toServer = protocolFormer('w',option, message);
                    sendToServer(toServer);
                }
            }
        }


        /**
        *   Forms the protocol structure by sending in the cmd, option, and message
         *   returns a char[] to be sent to the server.
         **/
        public char[] protocolFormer(char cmd, String options, String message){
            char [] protocol = new char[262173];

            protocol[0]=cmd;
            for(int i=1; i<21; i++){
                if(options!= null && i < options.length()){
                    protocol[i]=options.charAt(i-1);
                }
                else{
                    //nulls out the remaining bits
                    protocol[i] = '\0';
                }
            }

            String message_size = Integer.toString(message.length());

            for(int i = 21; i<29; i++){
                if(i-21 < message_size.length()){
                    protocol[i]= message_size.charAt(i-21);
                }
                else{
                    //nulls out the remaining bits
                    protocol[i] = '\0';
                }
            }

            for(int i = 29; i < 262173; i++){
                if(i-29 < message.length())
                    protocol[i]=message.charAt(i-29);
                else
                    protocol[i]='\0';
            }

            // Use this for testing if the protocol is outputting the correct format.
            //for(char c : protocol){
                //if(c != '\0'){
                //    System.out.print(c);
                //}
                //else
                //    System.out.print('~');
             //}

            return protocol;
        }


        /**
         * Sends the char[] to the server through a DataOutPutStream
         *
         */
        public void sendToServer(char[] protocol_to_server){
            try {

                OutputStream o_out = sockID.getOutputStream();
                DataOutputStream d_out = new DataOutputStream(o_out);

                for (int i = 0; i < protocol_to_server.length; i++) {
                    d_out.writeChar(protocol_to_server[i]);
                }



            }
            catch(IOException e){

            }
        }
    }

}
