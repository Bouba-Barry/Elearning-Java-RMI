package metier;

import java.io.Serializable;
import java.util.Date;

public class Messagerie implements Serializable {
private int id;
private int sender_id;
private int receiver_id;
private String content;
private String etat;
private String date;
    public Messagerie() {}

    public Messagerie(int sender_id, int receiver_id, String content, String etat) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.content = content;
        this.etat = etat;
    }
    public Messagerie(int id,int sender_id, int receiver_id, String content, String etat) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.content = content;
        this.etat = etat;
    }
    public Messagerie(int id,int sender_id, int receiver_id, String content, String etat,String date) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.content = content;
        this.etat = etat;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
