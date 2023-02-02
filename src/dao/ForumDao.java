package dao;

import connectionDB.ConnectDB;
import metier.Forum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ForumDao {
    private static Connection connection = ConnectDB.getConnection();

    public static boolean addMessageToGroup(Forum forum){
        boolean state = false;

            String query="INSERT INTO Forum(classe_id,sender_id,message) VALUES(?,?,?)";
            try {
                PreparedStatement pStmt = connection.prepareStatement(query);
                pStmt.setInt(1,forum.getClasse_id());
                pStmt.setInt(2,forum.getSender_id());
                pStmt.setString(3, forum.getMessage());
                pStmt.execute();
                connection.close();
                state=true;
            }catch (SQLException e){
                System.out.println("Failed to add new message to a group !!!");
            }
        return state;
    }

    public static List<Forum> getGroupMessages(int classeId){
        List<Forum> groupMessageList = new ArrayList<>();
            String sql = "SELECT * From Forum WHERE classe_id = ? ";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1,classeId);
                ResultSet res = statement.executeQuery();
                while (res.next()){
                    int id = res.getInt("id");
                    String content = res.getString("message");
                    int sender_id = res.getInt("sender_id");
                    String date = res.getTimestamp("date").toString();
                    groupMessageList.add(new Forum(id,classeId,content,sender_id,date));
                }
            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("Failed to select group messages !!!");
            }
        return groupMessageList;
    }

}
