package test;

import java.rmi.Naming;

public class ClientSendingRemote {
    public static void main(String[] args) {
        try {
            ISendingRemote sendingRemote = (ISendingRemote) Naming.lookup("rmi://localhost:1099/test");
            System.out.println(sendingRemote.getMessage("salut"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
