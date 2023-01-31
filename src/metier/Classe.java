package metier;

import java.io.Serializable;
import java.util.Date;

public class Classe implements Serializable {
    private int id;
    private String subject;
    private Date date_creation;
    public Classe() {
    }
    public Classe(int id, String subject) {
        this.id = id;
        this.subject = subject;
        this.date_creation = new Date();
    }

    public Classe(String subject) {
        this.subject = subject;
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

    public Date getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Date date_creation) {
        this.date_creation = date_creation;
    }
}
