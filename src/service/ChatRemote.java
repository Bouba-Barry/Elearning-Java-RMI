package service;

import metier.Messagerie;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatRemote extends Remote {
    public boolean sendMessage(String message, int sender_id, int receiver_id) throws RemoteException;
    public void broadcastMessage(String message,List<UserRMI> list) throws RemoteException;
    public List<Messagerie> getMessages(int user_id, int sender_id) throws RemoteException;
    public List<Messagerie> getMessageries() throws RemoteException;
    public void receiveMessage(String message) throws RemoteException;
}
