package service;

import dao.ModuleDao;
import dao.UserDao;
import metier.Messagerie;
import metier.Module;
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


    @Override
    public User getUser(String email) throws RemoteException {
        return UserDao.getUserByEmail(email);
    }

    @Override
    public boolean addUserToClasse(int user_id, int classe_id) throws RemoteException {
        return UserDao.updateUserClasse(user_id,classe_id);
    }

    @Override
    public boolean addModuleToClasse(int module_id, int classe_id) throws RemoteException {
        return ModuleDao.updateModuleClasse(module_id,classe_id);
    }

    @Override
    public boolean addNewUser(User user) throws RemoteException {
        return UserDao.addUserWithClasse(user);
    }

    @Override
    public boolean addNewModule(Module module) throws RemoteException {
        return ModuleDao.addModule(module);
    }

    @Override
    public boolean addSimpleUser(User user) throws RemoteException {
        return UserDao.addUser(user);
    }
}
