package service;

import java.rmi.RemoteException;

public class UserFactory {
    public static UserRMI createUser(String username, String email, String password,String role) throws RemoteException {
        if (role.equals("admin")) {
            return new AdminRemoteImpl(username,email, password);
        } else if (role.equals("teacher")) {
            return new TeacherRemoteImpl(username, email,password);
        } else if (role.equals("student")) {
            return new StudentRemoteImpl(username,email, password);
        } else {
            return null;
        }
    }
}
