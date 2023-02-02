package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import metier.Forum;
import metier.User;
import service.ChatRemote;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.Naming;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class ForumUI {

    public BorderPane createConversationBorderPane(User etudiantInfo, int classeId, String groupName,BorderPane main,ScrollPane pred){
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        Label groupNameLabel = new Label(groupName);
        groupNameLabel.setFont(Font.font("bold",30));
        Image image;
        try {
            image = new Image(new FileInputStream("ressource/image/Group.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        groupNameLabel.setGraphic(imageView);

        hBox.getChildren().addAll(groupNameLabel);
        hBox.setSpacing(30);
        hBox.setPadding(new Insets(10,0,0,40));
        hBox.setStyle("-fx-background-color: white");

        borderPane.setTop(hBox);

        //Creating method that generate conversation scrolling
        VBox conversationBox = new VBox();
        List<Forum> groupMessageList = new ArrayList<>();
        try {
            ChatRemote messageRemote = (ChatRemote) Naming.lookup("rmi://localhost:1099/ChatService");
            groupMessageList = messageRemote.getGroupMessages(classeId);
        }catch (Exception e){
            System.out.println("Failed to get group messages !!!");
        }
        System.out.println(groupMessageList);

        //VBox conversationBox = new VBox();
        for (int i = 0; i < groupMessageList.size(); i++) {
            Forum groupMessage = groupMessageList.get(i);
            VBox oneGroupMessageVBox = new VBox();

            HBox senderInfoDisplayBox = new HBox();
            Label senderNameLabel = new Label(groupMessage.getSender_name());
            senderNameLabel.setFont(Font.font("bold",15));
            Label senderEmailLabel = new Label("sender@test.com");
            senderEmailLabel.setFont(Font.font("italic",10));
            senderInfoDisplayBox.getChildren().addAll(senderNameLabel,senderEmailLabel);
            senderInfoDisplayBox.setSpacing(40);
            senderInfoDisplayBox.setMinWidth(600);

            HBox groupMessageHBox = new HBox();
            Label groupMessageLabel = new Label(groupMessage.getMessage());
            groupMessageLabel.setFont(Font.font("bold",15));
            groupMessageHBox.getChildren().add(groupMessageLabel);
            groupMessageHBox.setMinWidth(600);
            groupMessageHBox.setPadding(new Insets(10,0,0,0));

            HBox groupMessageDateHBox = new HBox();
            Label groupMessageDateLabel = new Label(groupMessage.getDate());
            groupMessageDateLabel.setFont(Font.font("italic",10));
            groupMessageDateHBox.getChildren().add(groupMessageDateLabel);
            groupMessageDateHBox.setMinWidth(600);
            groupMessageDateHBox.setPadding(new Insets(10,0,0,150));

            oneGroupMessageVBox.getChildren().add(senderInfoDisplayBox);
            oneGroupMessageVBox.getChildren().add(groupMessageHBox);
            oneGroupMessageVBox.getChildren().add(groupMessageDateHBox);
            if (i%2 ==0){
                oneGroupMessageVBox.setStyle("-fx-background-color: #00FF00");
            }
            else {
                oneGroupMessageVBox.setStyle("-fx-background-color: #C0C0C0");
            }
            conversationBox.getChildren().add(oneGroupMessageVBox);
        }

        conversationBox.setPrefWidth(800);
        conversationBox.setSpacing(20);
        conversationBox.setPadding(new Insets(0,100,0,100));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(conversationBox);
        scrollPane.setVvalue(300);

        borderPane.setCenter(scrollPane);

        //RefreshGroupMessageThread refreshGroupMessageThread= new RefreshGroupMessageThread(classeId,borderPane);

        ///Footer form for sending messages
        HBox writingHbox = new HBox();
        TextArea messageField = new TextArea();
        messageField.setPrefWidth(600);
        messageField.setPrefHeight(30);
        messageField.setBorder(Border.EMPTY);

        Button sendButton = new Button("send");
        sendButton.setPrefSize(80,40);
        sendButton.setFont(Font.font("bold",20));
        sendButton.setTextFill(Color.WHITE);
        sendButton.setStyle("-fx-background-color: #00FF00");

        int groupMessageNumber = groupMessageList.size();
        List<Forum> finalGroupMessageList = groupMessageList;
        sendButton.setOnAction(event -> {
            String messageToSend = messageField.getText();
            if (!messageToSend.equals("")){
                try {
                    ChatRemote messageRemote = (ChatRemote) Naming.lookup("rmi://localhost:1099/ChatService");
                    if (messageRemote.addMessageToGroup(classeId,etudiantInfo.getId(),messageToSend)){
                        DateFormat dateFormat = new SimpleDateFormat();
                        String dateToString = dateFormat.format(new Date());
                        String senderName = etudiantInfo.getNom();
                        Forum groupMessage = new Forum(messageToSend,dateToString,senderName,etudiantInfo.getEmail());
                        finalGroupMessageList.add(groupMessage);

                        addMessageInConversationBox(messageToSend,etudiantInfo,conversationBox,groupMessageNumber+1);

                        messageField.setText("");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Failed to send new message to a group !!!");
                }


            }

        });

        writingHbox.getChildren().addAll(messageField,sendButton);
        writingHbox.setPadding(new Insets(10));
        writingHbox.setStyle("-fx-background-color: #00c4ff");
        borderPane.setBottom(writingHbox);

        return borderPane;
    }

    public void addMessageInConversationBox(String message,User etudiantInfo,VBox conversationBox,int groupMessageNumber)
    {
        DateFormat dateFormat = new SimpleDateFormat();
        String dateToString = dateFormat.format(new Date());

        VBox oneGroupMessageVBox = new VBox();
        String senderName = etudiantInfo.getNom();
        Forum groupMessage = new Forum(message,dateToString,senderName,etudiantInfo.getEmail());

        HBox senderInfoDisplayBox = new HBox();
        Label senderNameLabel = new Label(groupMessage.getSender_name());
        senderNameLabel.setFont(Font.font("bold",15));
        Label senderEmailLabel = new Label(groupMessage.getSender_email());
        senderEmailLabel.setFont(Font.font("italic",10));
        senderInfoDisplayBox.getChildren().addAll(senderNameLabel,senderEmailLabel);
        senderInfoDisplayBox.setSpacing(40);

        HBox groupMessageHBox = new HBox();
        Label groupMessageLabel = new Label(groupMessage.getMessage());
        groupMessageLabel.setFont(Font.font("bold",15));
        groupMessageHBox.getChildren().add(groupMessageLabel);

        HBox groupMessageDateHBox = new HBox();
        Label groupMessageDateLabel = new Label(groupMessage.getDate());
        groupMessageDateLabel.setFont(Font.font("italic",10));
        groupMessageDateHBox.getChildren().add(groupMessageDateLabel);

        oneGroupMessageVBox.getChildren().addAll(senderInfoDisplayBox,groupMessageHBox,groupMessageDateHBox);
        if (groupMessageNumber%2 ==0){
            oneGroupMessageVBox.setStyle("-fx-background-color: #00FF00");
        }
        else {
            oneGroupMessageVBox.setStyle("-fx-background-color: #C0C0C0");
        }
        conversationBox.getChildren().add(oneGroupMessageVBox);
    }

}