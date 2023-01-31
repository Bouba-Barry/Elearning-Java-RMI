package service;

import metier.Messagerie;
import metier.User;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface UserRMI extends Remote {

    public void test() throws RemoteException;
    public String getRole() throws RemoteException;
    public User getUser(String email) throws RemoteException;
    public String getEmail() throws RemoteException;
    public void notifyMessage(String message,UserRMI user) throws RemoteException;
    public Messagerie getNewMessage() throws RemoteException;
    public void setNewMessage(Messagerie message) throws RemoteException;
}
