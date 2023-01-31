package service;

import metier.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserRemoteImp extends UnicastRemoteObject implements UserRemote {
private CopyOnWriteArrayList<User> userOnlines = new CopyOnWriteArrayList<>();
    public UserRemoteImp() throws RemoteException {}

    @Override
    public List<User> getAllLoggedUser() throws RemoteException {
        return userOnlines;
    }

    @Override
    public void addUserOnline(User user) throws RemoteException {
        if(! userOnlines.contains(user)){
            userOnlines.add(user);
        }
    }

    @Override
    public boolean logout(User user) throws RemoteException {
        boolean tmp = false;
       if(userOnlines.contains(user)){
           tmp = userOnlines.remove(user);
       }
        return tmp;
    }

    @Override
    public boolean isUserOnline(User user) throws RemoteException {
        return userOnlines.contains(user);
    }
}
