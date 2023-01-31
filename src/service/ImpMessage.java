package service;

import metier.Messagerie;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ImpMessage extends UnicastRemoteObject implements SharedMessageRemote {
private Messagerie content;
private int sender_id;
private int receiver_id;
    public ImpMessage() throws RemoteException {
    }

    @Override
    public void setSender(int id) throws RemoteException {
        this.sender_id = id;
    }

    @Override
    public int getSender() throws RemoteException {
        return sender_id;
    }

    @Override
    public void setReceiver(int id) throws RemoteException {
        this.receiver_id = id;
    }

    @Override
    public int getReceiver() throws RemoteException {
        return receiver_id;
    }

    @Override
    public Messagerie getContent() throws RemoteException {
        return content;
    }

    @Override
    public void setContent(Messagerie messagerie) throws RemoteException {
        this.content = messagerie;
    }
}
