package metier;

import service.WhiteBoardImp;

import java.io.Serializable;

public class WhiteBoard implements Serializable {
    private int id;
    private int module_id;
    private byte[] content;
    private String chemin;

    public WhiteBoard(){}

    public WhiteBoard(int module_id, byte[] content, String chemin) {
        this.module_id = module_id;
        this.content = content;
        this.chemin = chemin;
    }

    public WhiteBoard(int id, int module_id, byte[] content, String chemin) {
        this.id = id;
        this.module_id = module_id;
        this.content = content;
        this.chemin = chemin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }
}
