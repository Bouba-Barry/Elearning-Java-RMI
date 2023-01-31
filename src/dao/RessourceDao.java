package dao;

import connectionDB.ConnectDB;
import metier.Ressource;

import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RessourceDao {
    private static Connection connection = ConnectDB.getConnection();

    public static boolean addBinary(Ressource ressource){
        boolean ret = false;
        String sql = "INSERT INTO Ressource(type_ressource, ajouter_par, module_id, ressource_name, data) VALUES(?,?,?,?,?)";
        try  {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, ressource.getType_ressource());
            statement.setString(2, ressource.getAjouter_par());
            statement.setInt(3, ressource.getModule_id());
            statement.setString(4, ressource.getRessource_name());
            statement.setBinaryStream(5, new ByteArrayInputStream(ressource.getData()));
            statement.executeUpdate();
            ret = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    public static boolean addText(Ressource ressource){
      boolean ret = false;
        String sql = "INSERT INTO Ressource(type_ressource, ajouter_par, module_id, ressource_name, text_data) VALUES(?,?,?,?,?)";
        try  {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, ressource.getType_ressource());
            statement.setString(2, ressource.getAjouter_par());
            statement.setInt(3, ressource.getModule_id());
            statement.setString(4, ressource.getRessource_name());
            statement.setString(5, ressource.getText_data());
            statement.executeUpdate();
            ret = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static List<Ressource> getRessourcesByModuleId(int module_id) {
        String sql = "SELECT * FROM Ressource WHERE module_id = ?";
        List<Ressource> ressources = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, module_id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if(result.getString("text_data") == null){
                    int id = result.getInt("id");
                    String type_ressource = result.getString("type_ressource");
                    String ajouter_par = result.getString("ajouter_par");
                    String content = result.getString("ressource_name");
                    InputStream input = result.getBinaryStream("data");
                    byte[] data = new byte[input.available()];
                    input.read(data);
                    Ressource ressource = new Ressource(id, type_ressource, ajouter_par, module_id, content, data);
                    ressources.add(ressource);
                }
                else{
                    int id = result.getInt("id");
                    String type_ressource = result.getString("type_ressource");
                    String ajouter_par = result.getString("ajouter_par");
                    String content = result.getString("ressource_name");
                    String text_data = result.getString("text_data");
                    Ressource ressource = new Ressource(id, type_ressource, ajouter_par, module_id, content, text_data);
                    ressources.add(ressource);
                }
            }
            return ressources;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Ressource getRessourceByName(String fileName) {
        String sql = "SELECT * FROM Ressource WHERE ressource_name = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, fileName);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int id = result.getInt("id");
                String type_ressource = result.getString("type_ressource");
                String ajouter_par = result.getString("ajouter_par");
                String content = result.getString("ressource_name");
                InputStream input = result.getBinaryStream("data");
                byte[] data = new byte[input.available()];
                input.read(data);
                return new Ressource(id,type_ressource,ajouter_par,content,data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}