package server;

import dao.MessagerieDao;
import dao.UserDao;
import metier.Messagerie;
import metier.User;
import service.UserRMI;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
private List<UserRMI> loggedUser ;
        private DatagramSocket socket;
        private Thread run, receive;
        private boolean running = false;

        public ChatServer(List<UserRMI> lists) {
            try {
                socket = new DatagramSocket(3002);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            loggedUser = lists;
            run = new Thread(() -> {
                running = true;
                try {
                    receive();
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
            }, "Receive");
            run.start();
        }

        private void receive() throws UnknownHostException {
            while (running) {
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String message = new String(packet.getData());
                if(message.startsWith("UNIQUE:")){
                   String [] parts =  message.split(":");
                   String sender = parts[1];
                   String contents = parts[2];
                   String receiver = parts[3];
                   int id_receiver = Integer.parseInt(receiver.trim());
                   int id_sender = Integer.parseInt(sender.trim());
                    Messagerie messagerie = new Messagerie(id_sender, id_receiver,contents,"envoyé");
                    MessagerieDao.addMessagerie(messagerie);
                    User user = UserDao.getUserById(id_receiver);
                    send(contents.getBytes(),user.getNom(),packet.getPort());
                }
                else if(message.startsWith("GROUP:")){
                    String [] parts =  message.split(":");
                    String sender = parts[1];
                    String contents = parts[2];
                    String receiver_group = parts[3];
                   // MessagerieDao.addMessagerie();

                }
                System.out.println(message);
            }
        }

        public void send(byte[] data, String receiver, int port) {
          Thread  send = new Thread("Send") {
                public void run() {
                    DatagramPacket packet = null;
                    try {
                        packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), port);
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        for(UserRMI u: loggedUser){
                            //if(u.getUserName().equals(receiver))
                                socket.send(packet);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };send.start();
        }

}
