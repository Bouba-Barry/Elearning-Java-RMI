package client;

import service.UserRMI;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ChatClient {
    private DatagramSocket socket;
    private InetAddress address;
    private Thread send;
    private int port;
    private UserRMI userRMI;

    public ChatClient(UserRMI userRMI) {
        this.userRMI = userRMI;
        try {
            this.address = InetAddress.getLocalHost();
            this.port = 3002;
            socket = new DatagramSocket();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        send = new Thread("Send") {
            public void run() {
                Scanner scan = new Scanner(System.in);
                while (true) {
                    System.out.print("message: ");
                    String message = scan.nextLine();
                    try {

                        String content = "UNIQUE:3:"+message+":4";
                        byte[] data = content.getBytes();
                        DatagramPacket packet = null;
                        packet = new DatagramPacket(data, data.length,address, port);
                        socket.send(packet);
                    }
                     catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        send.start();

        Thread thread = new Thread(() -> {
            while (true) {
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String message = new String(packet.getData());
                System.out.println(message);
            }
        });thread.start();
    }

    public void receive() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message = new String(packet.getData());
        System.out.println(message);
    }
}
