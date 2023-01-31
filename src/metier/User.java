package metier;

import dao.UserDao;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String nom;
    private String email;
    private String password;
    private String type;
    private int classe_id;
    private String newMessage;
    private boolean notifyUser = false;

    public User(){}
    public User(String nom, String email, String password, String type) {
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.type = type;
    }
    public User(int id, String nom, String email, String password, String type, int classe_id) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.type = type;
        this.classe_id = classe_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getClasse_id() {
        return classe_id;
    }

    public void setClasse_id(int classe_id) {
        this.classe_id = classe_id;
    }
    public String getFiliere(){
        if(! getEmail().isEmpty()){
            String filiere =  UserDao.getClassUser(email).getSubject();
            return filiere;
        }
        return null;
    }

    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    public boolean isNotifyUser() {
        return notifyUser;
    }

    public void setNotifyUser(boolean notifyUser) {
        this.notifyUser = notifyUser;
    }
}
