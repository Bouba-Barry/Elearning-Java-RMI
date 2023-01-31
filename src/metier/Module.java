package metier;

import java.io.Serializable;

public class Module implements Serializable {
    private int id;
    private String libelle;
    private int teacher_id;
    private int classe_id;

    public Module(){}
    public Module(String libelle,int teacher_id, int classe_id){
        this.libelle = libelle;
        this.teacher_id = teacher_id;
        this.classe_id = classe_id;
    }

    public Module(int id, String libelle, int teacher_id) {
        this.id = id;
        this.libelle = libelle;
        this.teacher_id = teacher_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getClasse_id() {
        return classe_id;
    }

    public void setClasse_id(int classe_id) {
        this.classe_id = classe_id;
    }
}
