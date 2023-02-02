package metier;

public class Forum {
    private int id;
    private int classe_id;
    private String message;
    private String date;
    private int sender_id;
    private String sender_name;
    private String sender_email;

    public Forum() {
    }

    public Forum(int id, int classe_id, String message, int sender_id,String date) {
        this.id = id;
        this.classe_id = classe_id;
        this.message = message;
        this.date = date;
        this.sender_id = sender_id;
    }

    public Forum(int classe_id, String message) {
        this.classe_id = classe_id;
        this.message = message;
    }

    public Forum(int classeId, int senderId, String message) {
        this.classe_id = classeId;
        this.sender_id =senderId;
        this.message = message;
    }

    public Forum(String messageToSend, String dateToString, String senderName, String email) {
        this.message= messageToSend;
        this.date = dateToString;
        this.sender_name = senderName;
        this.sender_email = email;
    }

    public String getSender_email() {
        return sender_email;
    }

    public void setSender_email(String sender_email) {
        this.sender_email = sender_email;
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

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public int getClasse_id() {
        return classe_id;
    }

    public void setClasse_id(int classe_id) {
        this.classe_id = classe_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
