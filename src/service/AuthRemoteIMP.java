package service;

import dao.UserDao;
import metier.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AuthRemoteIMP extends UnicastRemoteObject implements AuthentificationRemote {

protected UserRMI user;
protected static CopyOnWriteArrayList<UserRMI> allLoggedUsers = new CopyOnWriteArrayList<>();
    public AuthRemoteIMP() throws RemoteException {
    }

    @Override
    public UserRMI loginUser(String username, String password) throws RemoteException {
        List<User> users = UserDao.getAllUsers();
        boolean tmp = false;
        for(User u : users){
            if(u.getEmail().equalsIgnoreCase(username) && u.getPassword().equals(password)){
                user =  UserFactory.createUser(u.getNom(),u.getEmail(),u.getPassword(),u.getType());
                    allLoggedUsers.add(user);
                return user;
            }
        }
        return null;
    }

    @Override
    public UserRMI getLoggedUser() throws RemoteException {
        return user;
    }

    @Override
    public List<UserRMI> AllLoggedUser() throws RemoteException {
        //allLoggedUsers.add(getLoggedUser());
        return allLoggedUsers;
    }

    @Override
    public boolean registerUser(String username, String mail, String password, String role) throws RemoteException {
        List<User> users = UserDao.getAllUsers();

        User userLog = new User();
        boolean tmp = false;
        for(User u : users){
            if(! u.getEmail().equalsIgnoreCase(mail)){
                userLog = u;
                // creation d'un nouveau user en base de donnée
                UserDao.addUser(new User(username, mail,password, role));
                tmp = true;
            }
        }


        return true;
    }
}
