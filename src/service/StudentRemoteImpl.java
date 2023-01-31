package service;

import dao.ModuleDao;
import dao.UserDao;
import metier.Messagerie;
import metier.User;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StudentRemoteImpl extends UnicastRemoteObject implements UserRMI{
private String username;
private String email;
private String password;
private Messagerie message;
    public StudentRemoteImpl() throws RemoteException {}

    public StudentRemoteImpl(String username, String email ,String password) throws RemoteException {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public void test() throws RemoteException {
        System.out.println("tu es student");
    }

    @Override
    public String getRole() throws RemoteException {
        return "student";
    }


    @Override
    public String getEmail() throws RemoteException {
        return this.email;
    }

    @Override
    public void notifyMessage(String message, UserRMI user) throws RemoteException {
       // user.setNewMessage(message);
    }

    @Override
    public Messagerie getNewMessage() throws RemoteException {
        return message;
    }

    @Override
    public void setNewMessage(Messagerie message) throws RemoteException {
        this.message = message;
    }

    @Override
    public User getUser(String email) throws RemoteException {
        return UserDao.getUserByEmail(email);
    }
}

