package service;

import metier.Ressource;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FileRemote extends Remote {

    public void sendFile(String fileName) throws RemoteException;
    public boolean uploadTxtFile(String fileName, String desc,int module_id ,String addBy,File file) throws RemoteException;
    public boolean uploadBinaryFile(String fileName, String desc,int module_id ,String addBy,File file) throws RemoteException;
    void receiveTxtFile(String fileName, File dest) throws RemoteException;
    public void receiveFileBinary(String fileName, File dest) throws RemoteException;
    public List<Ressource> getAllFileFromModule(int module_id) throws RemoteException;
}
