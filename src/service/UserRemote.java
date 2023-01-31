package service;

import metier.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface UserRemote extends Remote {

    public List<User> getAllLoggedUser() throws RemoteException;
    public void addUserOnline(User user) throws RemoteException;
    public boolean logout(User user) throws RemoteException;
    public boolean isUserOnline(User user) throws RemoteException;
}
