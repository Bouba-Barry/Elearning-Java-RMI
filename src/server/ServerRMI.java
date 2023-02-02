package server;

import metier.User;
import service.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class ServerRMI {
    protected static List<UserRMI> loggedUser;
    public static void main(String[] args) {

    try {

        //le remote pour la méthode d'authentification du user
        AuthentificationRemote auth_od = new AuthRemoteIMP();
        LocateRegistry.createRegistry(1099);
        Naming.rebind("AuthentificationService", auth_od);

        //le remote pour la méthode de création d'une session du user
        UserRemote userRemote = new UserRemoteImp();
        //LocateRegistry.createRegistry(1098);
        Naming.rebind("UserSession", userRemote);

        UserRMI student = new StudentRemoteImpl();
       // LocateRegistry.createRegistry(1097);
        Naming.rebind("StudentService", student);

        UserRMI teacher = new TeacherRemoteImpl();
        //LocateRegistry.createRegistry(1096);
        Naming.rebind("TeacherService", teacher);

        UserRMI admin = new AdminRemoteImpl();
        //LocateRegistry.createRegistry(1095);
        Naming.rebind("AdminService", admin);



        System.out.println("affichage des users connecté !!!! ");
        for (User u : userRemote.getAllLoggedUser()) {
            System.out.println("n: " + u.getNom());
            //  System.out.println("c: "+u.getFiliere());
        }

        ChatRemote chatRemote = new ChatRemoteImp();
        //LocateRegistry.createRegistry(1094);
        Naming.rebind("ChatService", chatRemote);

        SharedMessageRemote serverChat = new ImpMessage();
        //LocateRegistry.createRegistry(1094);
        Naming.rebind("SharedService", serverChat);

        System.out.println("server can receive and send message to other");
        System.out.println("Server is ready.");

        FileRemote fileRemote = new FileRemoteImpl();
        Naming.rebind("FileService", fileRemote);

        WhiteBoardRemote whiteBoardRemote = new WhiteBoardImp();
        Naming.rebind("WhiteBoardService", whiteBoardRemote);

    } catch (Exception e) {
        e.printStackTrace();
    }

        Thread thread = new Thread(() -> {
           ChatServer chatServer = new ChatServer(loggedUser);
        });

    }

}
