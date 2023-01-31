package service;

import dao.UserDao;
import metier.Messagerie;
import metier.User;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AdminRemoteImpl extends UnicastRemoteObject implements UserRMI {

private String username;
private String email;
private String password;
private Messagerie message;
    public AdminRemoteImpl() throws RemoteException {
    }
    public AdminRemoteImpl(String username,String email ,String password) throws RemoteException{
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public void test() throws RemoteException {
        System.out.println("tu es un admin");
    }

    @Override
    public String getRole() throws RemoteException {
        return "admin";
    }

    @Override
    public String getEmail() throws RemoteException {
        return this.email;
    }

    @Override
    public void notifyMessage(String message, UserRMI user) throws RemoteException {
        //user.setNewMessage(new Messagerie());
    }

    @Override
    public Messagerie getNewMessage() throws RemoteException {
        return message;
    }

    @Override
    public void setNewMessage(Messagerie message) throws RemoteException {
        this.message = message;
    }

    public InetAddress getAdresseHOST() throws RemoteException {
        try{
            InetAddress adresse = InetAddress.getLocalHost();
            return adresse;
        }catch (UnknownHostException e){e.printStackTrace();}
        return null;
    }

    @Override
    public User getUser(String email) throws RemoteException {
        return UserDao.getUserByEmail(email);
    }
}
