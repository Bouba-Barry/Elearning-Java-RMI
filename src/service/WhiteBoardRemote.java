package service;

import metier.WhiteBoard;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WhiteBoardRemote extends Remote {
    public boolean updateWhiteBoard(int module_id, byte[] content) throws RemoteException;
    public WhiteBoard getWhiteBoardUpdated(int module_id)throws RemoteException;
}
