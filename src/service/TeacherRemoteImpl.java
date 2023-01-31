package service;

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

public class TeacherRemoteImpl extends UnicastRemoteObject implements UserRMI {
private String username;
private String password;
private String email;
private FileRemote fileRemote;
protected AuthRemoteIMP authRemoteIMP;
private Messagerie message;

    public TeacherRemoteImpl() throws RemoteException {}

    public TeacherRemoteImpl(String username, String email,String password) throws RemoteException{
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public void test() throws RemoteException {
        System.out.println("tu es un teacher");
    }

    @Override
    public String getRole() throws RemoteException {
        return "teacher";
    }
    public void uploadFile(String fileName) throws RemoteException {
        fileRemote = new FileRemoteImpl();
        fileRemote.sendFile(fileName);
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
