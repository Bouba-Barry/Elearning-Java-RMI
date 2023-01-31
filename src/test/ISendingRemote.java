package test;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISendingRemote extends Remote {
    public String getMessage(String message) throws RemoteException;
}
