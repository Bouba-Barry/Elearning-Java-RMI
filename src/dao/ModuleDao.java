package dao;

import connectionDB.ConnectDB;
import metier.Classe;
import metier.Module;
import metier.Ressource;
import metier.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModuleDao {
    private static Connection connection = ConnectDB.getConnection();

    public static void addModule(Module module) {
        String sql = "INSERT INTO Module (libelle, teacher_id, classe_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,module.getLibelle());
            statement.setInt(2, module.getTeacher_id());
            statement.setInt(3, module.getClasse_id());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            ConnectDB.closeConnection();
        }
    }

    public static int readModuleIdByTeacher(int teacher_id){
        String sql = "SELECT * FROM Module WHERE teacher_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,teacher_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){

            }
        }catch (SQLException e){e.printStackTrace();}
        return 0;
    }

    public static List<Module> getAllModulesByClasse(int classe_id){
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT * FROM Module Where classe_id = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,classe_id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                int idModule = resultSet.getInt("id");
                int teacher_id = resultSet.getInt("teacher_id");
                String libelle = resultSet.getString("libelle");
                modules.add(new Module(idModule,libelle,teacher_id));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return modules;
    }

    public static Module getModuleByName(String nameModule){
        String sql = "SELECT * FROM Module Where libelle = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,nameModule);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                int idModule = resultSet.getInt("id");
                int teacher_id = resultSet.getInt("teacher_id");
                String libelle = resultSet.getString("libelle");
                return new Module(idModule,libelle,teacher_id);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Module> getAllModulesByTeacher(int teacher){
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT * FROM Module Where teacher_id = ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,teacher);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                int idModule = resultSet.getInt("id");
                int teacher_id = resultSet.getInt("teacher_id");
                String libelle = resultSet.getString("libelle");
                modules.add(new Module(idModule,libelle,teacher_id));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return modules;
    }

    public static List<String> getAllStudentByTeacher(int teacher){
        List<String> classes = new ArrayList<>();
        String sql = "SELECT distinct(c.id) FROM Module m , Classe c Where teacher_id = ? and m.classe_id = c.id ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,teacher);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                classes.add(new String(String.valueOf(id)));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

}
