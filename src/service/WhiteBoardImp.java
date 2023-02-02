package service;

import dao.WhiteBoardDao;
import metier.WhiteBoard;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteBoardImp extends UnicastRemoteObject implements WhiteBoardRemote {
    public WhiteBoardImp() throws RemoteException {}

    @Override
    public boolean updateWhiteBoard(int module_id,byte [] content) throws RemoteException {
         return WhiteBoardDao.Update(module_id,content);
    }

    @Override
    public WhiteBoard getWhiteBoardUpdated(int module_id) throws RemoteException {
        return WhiteBoardDao.getWhiteBoard(module_id);
    }
}
