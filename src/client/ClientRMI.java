package client;

import dao.UserDao;
import metier.Messagerie;
import metier.User;
import service.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientRMI {
    private Scanner input = new Scanner(System.in);
    private String role;
    protected UserRMI userRMI;
    private String username;
    private String password;
    private String email;
    private UserRemote userSession;
    public ClientRMI(){
        username = "";
        password = "";
        role = "";
        //Authentification();
    }
    public boolean Authentification(){
        boolean ret = false;
        try{
            AuthentificationRemote auth = (AuthentificationRemote) Naming.lookup("rmi://localhost/AuthentificationService");
            System.out.println("Entrez vos informations pour le login");
            System.out.print("email: ");
            email = input.nextLine();
            System.out.print("password: ");
            password = input.nextLine();
            boolean isAuthenticated = false;
           if(auth.loginUser(email, password) != null){
               userRMI = auth.loginUser(email, password);
               isAuthenticated = true;
               System.out.println("liste des user connecté");

           }else{
               isAuthenticated = false;
               System.out.println("echeck d'authentification et user null");
           }

            if(isAuthenticated){
                System.out.println("création de votre od sur le server");
                userRMI.test();
                ret = true;
                //userRMI.sendMessage("hello you all", userRMI);
            }
            else{
                ret = false;
                System.out.println("error login incorrect ");
            }
        }catch (Exception e){e.printStackTrace();}

        return ret;
    }
    public boolean login(String email, String password){
        boolean ret = false;
        try{
            AuthentificationRemote auth = (AuthentificationRemote) Naming.lookup("rmi://localhost:1099/AuthentificationService");
            boolean isAuthenticated = false;
            if(auth.loginUser(email, password) != null){
                UserRMI userLogged = auth.loginUser(email, password);
                System.out.println("fisst_role_test => " + userLogged.getRole());
                switch (userLogged.getRole()){
                    case "student":
                       UserRMI od_user = (UserRMI)  Naming.lookup("rmi://localhost:1099/StudentService");
                        System.out.println("role user_actuel = > "+od_user.getRole());
                        setUserRMI(od_user);
                        break;
                    case "teacher":
                       UserRMI od_user_teacher = (UserRMI)  Naming.lookup("rmi://localhost:1099/TeacherService");
                        System.out.println("role user_actuel = > "+od_user_teacher.getRole());
                        setUserRMI(od_user_teacher);
                        break;
                    case "admin":
                        UserRMI od_user_admin = (UserRMI)  Naming.lookup("rmi://localhost:1099/AdminService");
                        System.out.println("role user_actuel = > "+od_user_admin.getRole());
                        setUserRMI(od_user_admin);
                        break;
                }
                isAuthenticated = true;
            }
            else{
                isAuthenticated = false;
                System.out.println("echeck d'authentification et user reçu = null");
            }

            if(isAuthenticated){
                System.out.println("création de votre od sur le server");
                userSession = (UserRemote) Naming.lookup("rmi://localhost:1099/UserSession");
                User user = UserDao.getUserByEmail(email);
                userSession.addUserOnline(user);
                userRMI.test();
                System.out.println("user connecté : ");
                for(User u : userSession.getAllLoggedUser()){
                    System.out.println(u.getNom());
                }
                ret = true;
            }
            else{
                ret = false;
                System.out.println("error login incorrect ");
            }
        }catch (Exception e){e.printStackTrace();}
        return ret;
    }
    public void menuAppAdmin(){
        System.out.println("Faite votre choix ! ");
        System.out.println("1: Consulter classes créer");
        System.out.println("2: créer une classe");
        System.out.println("3: ajouter etudiant dans classe");
        System.out.println("4: ajouter prof dans classe");
    }
    public void menuAppTeacher(){
        System.out.println("Faite votre choix ! ");
        System.out.println("1: consulter classe");
        System.out.println("2: ajouter cours");
        System.out.println("3: ecrire dans tableau blanc");
        System.out.println("4: deconnexion");
    }
    public void menuAppStudent(){
        System.out.println("Faite votre choix ! ");
        System.out.println("1: consulter classes");
        System.out.println("2: deconnexion ");
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRMI getUserRMI() {
        return userRMI;
    }

    public void setUserRMI(UserRMI userRMI) {
        this.userRMI = userRMI;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRemote getUserSession() {
        return userSession;
    }

    public void setUserSession(UserRemote userSession) {
        this.userSession = userSession;
    }

    public void messagerie() throws MalformedURLException, NotBoundException, RemoteException {
        ChatRemote chatRemote = (ChatRemote) Naming.lookup("rmi://localhost:1099/ChatService");
        //chatRemote.sendMessage("hello",userRMI,userRMI);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                   for(Messagerie m : chatRemote.getMessageries()){
                       System.out.println("sender => "+m.getContent());
                   }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        ClientRMI clientRMI = new ClientRMI();
       if(clientRMI.Authentification()){
           ChatClient chatClient = new ChatClient(clientRMI.userRMI);
       }

       else
           System.out.println("error d'authentification  !!!! ");
    }
}
