package metier;

import java.io.Serializable;

public class Ressource implements Serializable {
    private int id;
    private String type_ressource;
    private String ajouter_par;
    private int module_id;
    private String ressource_name;
    private byte[] data;
    private String text_data;

    public Ressource(){}

    public Ressource(String type_ressource, String ajouter_par, int module_id, String content, byte[] data) {
        this.type_ressource = type_ressource;
        this.ajouter_par = ajouter_par;
        this.module_id = module_id;
        this.ressource_name = content;
        this.data = data;
    }

    public Ressource(int id, String type_ressource, String ajouter_par, int module_id, String ressource_name, String text_data) {
        this.id = id;
        this.type_ressource = type_ressource;
        this.ajouter_par = ajouter_par;
        this.module_id = module_id;
        this.ressource_name = ressource_name;
        this.text_data = text_data;
    }
    public Ressource(String type_ressource, String ajouter_par, int module_id, String ressource_name, String text_data) {
        this.type_ressource = type_ressource;
        this.ajouter_par = ajouter_par;
        this.module_id = module_id;
        this.ressource_name = ressource_name;
        this.text_data = text_data;
    }


    public Ressource(int id, String type_ressource, String ajouter_par, int module_id, String ressource_name, byte[] data) {
        this.id = id;
        this.type_ressource = type_ressource;
        this.ajouter_par = ajouter_par;
        this.module_id = module_id;
        this.ressource_name = ressource_name;
        this.data = data;
    }

    public Ressource(int id, String typeRessource, String ajouterPar, String content, byte[] data) {
        this.id = id;
        this.type_ressource = typeRessource;
        this.ajouter_par = ajouterPar;
        this.ressource_name = content;
        this.data = data;
    }
    public Ressource(String nom,String typeRessource, String ajouterPar,int module_id, byte[] data) {
        this.ressource_name = nom;
        this.type_ressource = typeRessource;
        this.ajouter_par = ajouterPar;
        this.module_id = module_id;
        this.data = data;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_ressource() {
        return type_ressource;
    }

    public void setType_ressource(String type_ressource) {
        this.type_ressource = type_ressource;
    }

    public String getAjouter_par() {
        return ajouter_par;
    }

    public void setAjouter_par(String ajouter_par) {
        this.ajouter_par = ajouter_par;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public String getRessource_name() {
        return ressource_name;
    }

    public void setRessource_name(String ressource_name) {
        this.ressource_name = ressource_name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getText_data() {
        return text_data;
    }

    public void setText_data(String text_data) {
        this.text_data = text_data;
    }
}
