package service;

import metier.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AuthentificationRemote extends Remote {
    public UserRMI loginUser(String username, String password) throws RemoteException;
    public UserRMI getLoggedUser() throws RemoteException;
    public List<UserRMI> AllLoggedUser() throws RemoteException;
    public boolean registerUser(String username, String email, String password, String role) throws RemoteException;
}
