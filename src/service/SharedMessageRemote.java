package service;

import metier.Messagerie;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SharedMessageRemote extends Remote {
    public void setSender(int id) throws RemoteException;
    public int getSender() throws RemoteException;
    public void setReceiver(int id) throws RemoteException;
    public int getReceiver() throws RemoteException;
    public Messagerie getContent() throws RemoteException;
    public void setContent(Messagerie messagerie) throws RemoteException;
}
