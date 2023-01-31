package dao;

import connectionDB.ConnectDB;
import metier.Messagerie;
import metier.Module;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessagerieDao {
    private static Connection connection = ConnectDB.getConnection();
    public static boolean addMessagerie(Messagerie messagerie) {
        boolean tmp = false;
        String sql = "INSERT INTO Messagerie (sender_id, receiver_id,content,etat) VALUES (?, ?, ?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
          statement.setInt(1,messagerie.getSender_id());
          statement.setInt(2, messagerie.getReceiver_id());
          statement.setString(3,messagerie.getContent());
          statement.setString(4, messagerie.getEtat());
          statement.executeUpdate();
          tmp = true;
           // ConnectDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public static List<Messagerie> getMessagesByUserId(int currentUserId, int receiverId){
        List<Messagerie> messageList = new ArrayList<>();
            String sql ="SELECT * FROM Messagerie WHERE (sender_id = ? and receiver_id = ? ) or (sender_id = ? and receiver_id = ? )";
            try{
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1,currentUserId);
                statement.setInt(2,receiverId);
                statement.setInt(3,receiverId);
                statement.setInt(4,currentUserId);
                ResultSet rs = statement.executeQuery();
                while (rs.next()){
                    int id = rs.getInt("id");
                    int id_sender = rs.getInt("sender_id");
                    int id_receiver = rs.getInt("receiver_id");
                    String content = rs.getString("content");
                    String etat = rs.getString("etat");
                    Timestamp dateTime = rs.getTimestamp("date_envoi");
                    messageList.add(new Messagerie(id,id_sender,id_receiver,content,etat,dateTime.toString()));
                }
            }catch (SQLException e){
                System.out.println("Failed to select messages !!!");
            }

        return messageList;
    }
}
