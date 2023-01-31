package service;

import dao.MessagerieDao;
import dao.ModuleDao;
import metier.Messagerie;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatRemoteImp extends UnicastRemoteObject implements ChatRemote {
List<Messagerie> messages  = new ArrayList<>();
protected UserRMI userRMI;
    public ChatRemoteImp() throws RemoteException {}
    public ChatRemoteImp(UserRMI userRMI) throws RemoteException{
        this.userRMI = userRMI;
    }
    @Override
    public boolean sendMessage(String message, int sender_id, int receiver_id) throws RemoteException {
        messages.add(new Messagerie(sender_id, receiver_id, message,"envoyé"));
        //userRMI.setNewMessage(new Messagerie(sender_id, receiver_id, message,"envoyé"));
        return MessagerieDao.addMessagerie(new Messagerie(sender_id, receiver_id, message,"envoyé"));
    }

    @Override
    public  void broadcastMessage(String message, List<UserRMI> list) throws RemoteException {
        for(UserRMI u: list){
            u.notifyMessage(message, u);
        }
    }

    @Override
    public List<Messagerie> getMessages(int user_id, int sender_id) throws RemoteException {
        return MessagerieDao.getMessagesByUserId(user_id,sender_id);
    }

    @Override
    public List<Messagerie> getMessageries() throws RemoteException {
        return messages;
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    public void setUserRMI(UserRMI userRMI) {
        this.userRMI = userRMI;
    }

    public UserRMI getUserRMI() {
        return userRMI;
    }
}
