package test;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Locale;

public class SendingRemote extends UnicastRemoteObject implements ISendingRemote {
    protected SendingRemote() throws RemoteException {
    }

    @Override
    public String getMessage(String message) throws RemoteException {
        return "Server " + message;
    }

    public static void main(String[] args) {
        try{
            ISendingRemote sendingRemote = new SendingRemote();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/test",sendingRemote);
            System.out.println("server running...............");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
