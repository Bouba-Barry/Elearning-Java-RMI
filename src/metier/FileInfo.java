package metier;

public class FileInfo {
    private String nom;
    private String url;
    private long taille;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUrl() {
        return url;
    }

    public long getTaille() {
        return taille;
    }

    public void setTaille(long taille) {
        this.taille = taille;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
