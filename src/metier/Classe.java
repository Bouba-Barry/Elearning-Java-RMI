package metier;

import java.io.Serializable;
import java.util.Date;

public class Classe implements Serializable {
    private int id;
    private String subject;
    private String date_creation;
    private String desc;
    public Classe() {
    }
    public Classe(int id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public Classe(int id, String subject,String desc, String date) {
        this.id = id;
        this.subject = subject;
        this.date_creation = date;
        this.desc = desc;
    }

    public Classe(String subject) {
        this.subject = subject;
    }

    public Classe(String classeName, String description) {
        this.subject = classeName;
        this.desc = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
