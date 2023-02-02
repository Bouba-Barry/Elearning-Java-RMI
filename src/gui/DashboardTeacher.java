package gui;

import client.ClientRMI;
import dao.ClasseDao;
import dao.ModuleDao;
import dao.RessourceDao;
import dao.UserDao;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import metier.*;
import metier.Module;
import service.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class DashboardTeacher {
    protected UserRMI userRMI;
    private List<Module> modules;
    private Classe classe_user;
    private Module currentModule = null;
    private User profModule = null;
    protected User currentUser;
    protected UserRemote sessionUser;
    public Scene DashboardTeacher(Stage primaryStage, String email, ClientRMI clientRMI){
        /** ---------Récupération des Informations du User -------------------- **/
        this.userRMI = clientRMI.getUserRMI();
        sessionUser = clientRMI.getUserSession();
        try{
            currentUser = userRMI.getUser(email);
        }catch (Exception e) {e.printStackTrace();}

        // recuperation des informations du users .....
        //classe_user = UserDao.getClassUser(email);
        modules = ModuleDao.getAllModulesByTeacher(currentUser.getId());

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,800,600);

        VBox leftPanel = new VBox();
        VBox rightPanel = new VBox();

        /* * ********** traitement du leftPanel ***********************/
        HBox conversationHbox = new HBox();
        HBox logoutHbox = new HBox();
        // le content de mon center dans le scrollPane
        ScrollPane scrollPaneRightPanel = new ScrollPane();

        // recuperez la list des etudiants du prof ...

        // part conversation
        TitledPane titledPaneConv = new TitledPane();
        titledPaneConv.setPrefSize(180,50);
        titledPaneConv.setText("Conversation");
        titledPaneConv.setExpanded(false);

        // recuperez la listes des classes de ce prof ...
        List<Classe> classeStudent = new ArrayList<>();
        for(String s : ModuleDao.getAllStudentByTeacher(currentUser.getId())){
              int idClasse =  Integer.parseInt(s);
           classeStudent.add(ClasseDao.getClassById(Integer.parseInt(s)));
        }
        VBox tmpVbox = new VBox();
        for(Classe c : classeStudent){
            TitledPane titledPaneClasse = new TitledPane();
            titledPaneClasse.setText(c.getSubject());
            titledPaneClasse.setExpanded(false);
            VBox boxUser = new VBox();
            boxUser.setSpacing(5);
            List<User> UserByClasse = UserDao.getAllUserByClasse(c.getId());
            for(User u : UserByClasse){
                Button userName = new Button(u.getNom());
                userName.setPrefSize(80,12);
                userName.setOnAction(event -> {
                    User userReceiver = UserDao.getUserByName(u.getNom());
                    BorderPane pane =  createConversationBorderPane(userRMI, userReceiver,scrollPaneRightPanel ,root);
                    pane.setPadding(new Insets(0,15,0,0));
                    //borderPane.setCenter(pane);
                    root.setCenter(pane);
                });
                boxUser.getChildren().add(userName);
            }
            titledPaneClasse.setContent(boxUser);
            tmpVbox.getChildren().add(titledPaneClasse);
        }
        //vBoxTmp.getChildren().addAll(new Label("classe1"),new Label("classe2"));
        tmpVbox.setSpacing(5);
        titledPaneConv.setContent(tmpVbox);
        conversationHbox.getChildren().addAll(titledPaneConv);

        //part of logout
        //Button logoutButton = new Button("logout");
        //logoutHbox.getChildren().add(logoutButton);
        //logoutButton.setPrefSize(180,10);

        leftPanel.getChildren().addAll(conversationHbox);
        leftPanel.setSpacing(20);
        leftPanel.setPadding(new Insets(20,5,0,0));
        leftPanel.setStyle("-fx-background-color: lightgray");

        /*************************  right Panel ************************************  ***/
        for(Module m: modules){
            VBox module2Vbox = new VBox();
            HBox module2Hbox = new HBox();
            // Button classeButton2 = new Button();
            Image Imgclasse2 = null;
            try {
                Imgclasse2 = new Image(new FileInputStream("ressource/image/doc.png"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            ImageView classeImgView2 = new ImageView(Imgclasse2);
            classeImgView2.setFitHeight(60);
            classeImgView2.setFitWidth(70);
            //classeButton2.setGraphic(classeImgView2);
            //classeButton2.setPadding(new Insets(20,5,0,5));

            Label module2Label = new Label(m.getLibelle()+"\n"+"Depuis le 2022/12/01/ 1h:34:00");
            //Label create_atLabel = new Label("2022/12/01/ 1h:34:00 ");
            module2Label.setPadding(new Insets(5,10,20,5));
            module2Hbox.getChildren().addAll(classeImgView2,module2Label);
            module2Hbox.setAlignment(Pos.TOP_LEFT);

            HBox gerezHbox2 = new HBox();
            Button module2Button = new Button("Voir Plus");
            gerezHbox2.getChildren().add(module2Button);
            gerezHbox2.setAlignment(Pos.BOTTOM_RIGHT);
            gerezHbox2.setPadding(new Insets(5,20,5,100));

            module2Button.setOnAction(event -> {
                BorderPane pane = moduleComponent(root,m.getLibelle(),m.getId() ,scrollPaneRightPanel,primaryStage);
                root.setCenter(pane);
            });
            module2Vbox.getChildren().addAll(module2Hbox,gerezHbox2);
            module2Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");
            module2Vbox.setSpacing(10);
            module2Vbox.setAlignment(Pos.BASELINE_CENTER);
            module2Vbox.setPrefSize(200,80);
            rightPanel.getChildren().addAll(module2Vbox);
        }
       // rightPanel.getChildren().addAll(module1Vbox,module2Vbox,module3Vbox);
        rightPanel.setSpacing(30);
        rightPanel.setPadding(new Insets(15,20,0,20));
        rightPanel.setPrefWidth(600);
        //ScrollPane scrollPaneRightPanel = new ScrollPane(rightPanel);
        scrollPaneRightPanel.setContent(rightPanel);
        scrollPaneRightPanel.setBorder(Border.EMPTY);
        //scrollPaneRightPanel.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);



        /** *******traitement du top *********************************/
        HBox accueilAdmin = new HBox();
        Label welcomeLabel = new Label("Dashboard teacher");
        welcomeLabel.setFont(Font.font("bold",20));
        welcomeLabel.setTextFill(Color.WHITE);
        //welcomeLabel.setPadding(new Insets(10));
        accueilAdmin.getChildren().addAll(welcomeLabel);
        accueilAdmin.setPadding(new Insets(20,100,20,10));
        accueilAdmin.setAlignment(Pos.TOP_LEFT);

        HBox profNameHbox = new HBox();
        Label label = new Label("Mr "+currentUser.getNom());
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("bold",20));
        Image image = null;
        try {
            image = new Image(new FileInputStream("ressource/image/profile.jpg"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        profNameHbox.getChildren().addAll(imageView,label);
        profNameHbox.setPadding(new Insets(20,100,20,5));
        profNameHbox.setAlignment(Pos.TOP_CENTER);

        HBox leftTopHbox = new HBox();
        Button logoutButton = new Button();
        Image image2 = null;
        try {
            image2 = new Image(new FileInputStream("ressource/image/logout.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView logoutImg = new ImageView(image2);
        logoutImg.setFitHeight(20);
        logoutImg.setFitWidth(20);
        logoutButton.setGraphic(logoutImg);
        //logoutButton.setTextFill(Color.WHITE);
        logoutButton.setOnAction(event -> {
            logoutMethod(userRMI,event);
        });
        logoutButton.setStyle("-fx-background-color: #FF0F0F");
        leftTopHbox.getChildren().add(logoutButton);
        leftTopHbox.setAlignment(Pos.TOP_RIGHT);
        leftTopHbox.setPadding(new Insets(20,0,20,100));

        HBox topHbox = new HBox();
        topHbox.setSpacing(20);
        topHbox.setPadding(new Insets(5,50,30,0));
        topHbox.getChildren().addAll(accueilAdmin,profNameHbox,leftTopHbox);
        topHbox.setStyle("-fx-background-color: rgb(23, 50, 113, 1)");
        // label du prof


        root.setTop(topHbox);
        root.setLeft(leftPanel);
        root.setCenter(scrollPaneRightPanel);

        /**  ---------------traitement des actions des buttons ---------------------***/


        primaryStage.setTitle("Menu Teacher");
        /* primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show(); */
        return scene;
    }

    public BorderPane moduleComponent(BorderPane main, String moduleName, int module_id,ScrollPane pred,Stage stage){

        List<Ressource> ressources = RessourceDao.getRessourcesByModuleId(module_id);
        BorderPane borderPane = new BorderPane();
        /** ------------la partie top du border ----------------------------- **/
        VBox topVbox = new VBox();
        HBox topLabHbox = new HBox();

        Label moduleNameLabel = new Label(moduleName);
        moduleNameLabel.setAlignment(Pos.TOP_CENTER);
        moduleNameLabel.setFont(new Font("Bold",20));

        Button backButton = new Button();
        backButton.setAlignment(Pos.TOP_LEFT);
        Image backImg = null;
        try {
            backImg = new Image(new FileInputStream("ressource/image/iconRetour.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView backImgView = new ImageView(backImg);
        backImgView.setFitHeight(30);
        backImgView.setFitWidth(30);
        backButton.setGraphic(backImgView);

        Button whiteBoardButton = new Button();
        Image whiteImg = null;
        try {
            whiteImg = new Image(new FileInputStream("ressource/image/whiteBoard1.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView whiteImgView = new ImageView(whiteImg);
        whiteImgView.setFitHeight(30);
        whiteImgView.setFitWidth(30);
        whiteBoardButton.setGraphic(whiteImgView);
        whiteBoardButton.setAlignment(Pos.TOP_RIGHT);
        whiteBoardButton.setOnAction(event -> {
            try {
                main.setCenter(new WhiteBoard(module_id).start(stage,main,pred));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        //** boutton d'affichage de upload file
        Button uploButton = new Button();
        //uploButton.setPadding(new Insets(10,5,10,100));
        Image uploImg = null;
        try {
            uploImg = new Image(new FileInputStream("ressource/image/upload.jpeg"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView uploImgView = new ImageView(uploImg);
        uploImgView.setFitHeight(30);
        uploImgView.setFitWidth(30);
        uploButton.setGraphic(uploImgView);
        //uploButton.setAlignment(Pos.TOP_);
        HBox buttonEnd = new HBox();
        buttonEnd.getChildren().addAll(uploButton,whiteBoardButton);
        buttonEnd.setSpacing(10);
        buttonEnd.setAlignment(Pos.TOP_RIGHT);
        buttonEnd.setPadding(new Insets(0,5,0,120));



        topLabHbox.getChildren().addAll(backButton,moduleNameLabel,buttonEnd );
        topLabHbox.setSpacing(50);
        topLabHbox.setPadding(new Insets(5,5,10,5));

        HBox topOpeHbox = new HBox();
        TextField descriptionTextFied = new TextField();
        descriptionTextFied.setPromptText("describe file");
        descriptionTextFied.setPrefSize(200,20);
        Button uploadButton = new Button();
        Image uploadBtnImg = null;
        try {
            uploadBtnImg = new Image(new FileInputStream("ressource/image/uploadButton.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView uploadBtnImgView = new ImageView(uploadBtnImg);
        uploadBtnImgView.setFitHeight(30);
        uploadBtnImgView.setFitWidth(30);
        //uploadButton.setAlignment(Pos.TOP_RIGHT);
        uploadButton.setGraphic(uploadBtnImgView);
        uploadButton.setOnAction(event -> {
            String desc = descriptionTextFied.getText();
            uploadFile(desc,module_id, currentUser.getNom(),stage);
        });

        topOpeHbox.getChildren().addAll(descriptionTextFied,uploadButton);
        topOpeHbox.setSpacing(2);
        topOpeHbox.setAlignment(Pos.BASELINE_CENTER);
        topOpeHbox.setVisible(false);
       // boolean tmp = false;
        uploButton.setOnAction(event -> {
         topOpeHbox.setVisible(true);
        });

        topVbox.setSpacing(10);
        topVbox.getChildren().addAll(topLabHbox,topOpeHbox);
        topVbox.setPadding(new Insets(10,10,8,10));
        Border border = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        topVbox.setBorder(border);
        topVbox.setStyle("-fx-background-color: lightgray ");
        //Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        //topVbox.setBorder(border);

        /** -------------la partie center du borderPane --------------------- **/
        VBox centerVbox = new VBox();
        Label ressourceTitle = new Label("Ressource Uploader");
        ressourceTitle.setAlignment(Pos.BASELINE_CENTER);
        ressourceTitle.setPadding(new Insets(5,100,5,100));
        centerVbox.getChildren().add(ressourceTitle);

        for(Ressource r : ressources){
            ///********
            VBox module2Vbox = new VBox();
            HBox module2Hbox = new HBox();
            // Button classeButton2 = new Button();
            Image Imgclasse2 = null;
            try {
                Imgclasse2 = new Image(new FileInputStream("ressource/image/ressource.png"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            ImageView classeImgView2 = new ImageView(Imgclasse2);
            classeImgView2.setFitHeight(60);
            classeImgView2.setFitWidth(70);
            //classeButton2.setGraphic(classeImgView2);
            //classeButton2.setPadding(new Insets(20,5,0,5));

            Label module2Label = new Label(r.getRessource_name()+"\n"+r.getType_ressource()+"\n Depuis le");
            Label desc = new Label(r.getType_ressource());

            //Label create_atLabel = new Label("2022/12/01/ 1h:34:00 ");
            module2Label.setPadding(new Insets(5,10,20,5));
            module2Hbox.getChildren().addAll(classeImgView2,module2Label);
            module2Hbox.setAlignment(Pos.TOP_LEFT);

            HBox gerezHbox2 = new HBox();
            Button module2Button = new Button("remove");
            gerezHbox2.getChildren().add(module2Button);
            gerezHbox2.setAlignment(Pos.BOTTOM_RIGHT);
            gerezHbox2.setPadding(new Insets(5,20,5,100));

            module2Button.setOnAction(event -> {

            });
            module2Vbox.getChildren().addAll(module2Hbox,gerezHbox2);
            module2Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");
            module2Vbox.setSpacing(10);
            module2Vbox.setAlignment(Pos.BASELINE_CENTER);
            module2Vbox.setPrefSize(200,80);
            centerVbox.getChildren().add(module2Vbox);
        }

        centerVbox.setSpacing(20);
        centerVbox.setPadding(new Insets(15,20,0,20));
        centerVbox.setPrefWidth(600);
        ScrollPane scrollPaneRightPanel = new ScrollPane(centerVbox);
        scrollPaneRightPanel.setBorder(Border.EMPTY);

        backButton.setOnAction(event -> {
            //stagePrincipale.setScene(scenePred);
            main.setCenter(pred);
        });

        borderPane.setTop(topVbox);
        borderPane.setCenter(scrollPaneRightPanel);
        return borderPane;
    }

    public void uploadFile(String desc,int module_id,String addBy ,Stage stage){
        try {
            FileRemote fileRemote = (FileRemote)  Naming.lookup("rmi://localhost:1099/FileService");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload File");
            //fileChooser.setInitialFileName();
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                String extension = getExtension(file.getName());
                if(extension.equals("txt")){
                   if(fileRemote.uploadTxtFile(file.getName(),desc,module_id,addBy,file)){
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Retour de L'upload ");
                       alert.setHeaderText(null);
                       alert.setContentText("Votre Fichier est Uploader Avec Success");
                       alert.showAndWait();
                   }
                   else{
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Retour de L'upload ");
                       alert.setHeaderText(null);
                       alert.setContentText("Echec D'upload Du Fichier ! Réessayez SVP");
                       alert.showAndWait();
                   }
                }else{
                    if(fileRemote.uploadBinaryFile(file.getName(),desc,module_id,addBy,file)){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Retour de L'upload ");
                        alert.setHeaderText(null);
                        alert.setContentText("Votre Fichier est Uploader Avec Success");
                        alert.showAndWait();
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Retour de L'upload ");
                        alert.setHeaderText(null);
                        alert.setContentText("Echec D'upload Du Fichier ! Réessayez SVP");
                        alert.showAndWait();
                    }
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public String getExtension(String fileName){
        String extension = "";
        int index = fileName.lastIndexOf(".");
        if(index > 0){
            extension = fileName.substring(index+1);
        }
        return extension;
    }

    public BorderPane createConversationBorderPane(UserRMI user, User receiverInfo,ScrollPane pred,BorderPane main){
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        String receiverName = receiverInfo.getNom();
        Label receiverNameLabel = new Label(receiverName);
        receiverNameLabel.setFont(Font.font("bold",30));
        receiverNameLabel.setAlignment(Pos.TOP_CENTER);


        Button backButton = new Button();
        backButton.setAlignment(Pos.TOP_LEFT);
        Image backImg = null;
        try {
            backImg = new Image(new FileInputStream("ressource/image/iconRetour.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView backImgView = new ImageView(backImg);
        backImgView.setFitHeight(30);
        backImgView.setFitWidth(30);
        backButton.setGraphic(backImgView);
        //backButton.setPadding(new Insets(10,150,10,5));
        backButton.setAlignment(Pos.TOP_LEFT);
        backButton.setOnAction(event -> {
            main.setCenter(pred);
        });
        HBox buttonBack = new HBox();
        buttonBack.getChildren().add(backButton);
        buttonBack.setAlignment(Pos.TOP_LEFT);
        buttonBack.setPadding(new Insets(0,80,0,5));


        hBox.getChildren().addAll(backButton,receiverNameLabel);
        hBox.setSpacing(80);
        // ce qui est ajouté de suite ....
        hBox.setPadding(new Insets(10,0,0,10));
        hBox.setStyle("-fx-background-color: white");

        borderPane.setTop(hBox);
        //Creating method that generate conversation scrolling
        List<Messagerie> messageList = new ArrayList<>();
        try{
            ChatRemote chatRemote = (ChatRemote) Naming.lookup("rmi://localhost:1099/ChatService");
            messageList = chatRemote.getMessages(currentUser.getId(),receiverInfo.getId());
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erreur de recupération de conversation");
        }
        System.out.println(messageList);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(500);
        scrollPane.setVvalue(300);
        VBox leftMessageDisplay = new VBox();
        VBox rightMessageDisplay = new VBox();
        int i=0;
        for (Messagerie message:messageList) {
            HBox leftHBox = new HBox();
            HBox rightHBox = new HBox();
           // leftHBox.setPadding(new Insets());
            Label leftLabel = new Label(message.getContent() + "\n" + message.getDate());
            leftLabel.setFont(Font.font("bold",15));
            Label rightLabel = new Label(message.getContent() + "\n" + message.getDate());
            rightLabel.setFont(Font.font("bold",15));
            if (message.getSender_id()==receiverInfo.getId()){
                //Message have to be displayed at left
                leftHBox.getChildren().add(leftLabel);
                leftHBox.setStyle("-fx-background-color: #89849C");
                leftMessageDisplay.getChildren().add(leftHBox);
                rightLabel.setVisible(false);
                rightHBox.getChildren().add(rightLabel);
                rightMessageDisplay.getChildren().add(rightHBox);
            }else {
                //Message have to be displayed at right
                rightHBox.getChildren().add(rightLabel);
                rightLabel.setStyle("-fx-background-color: #2caf19");
                rightMessageDisplay.getChildren().add(rightHBox);
                leftLabel.setVisible(false);
                leftHBox.getChildren().add(leftLabel);
                leftMessageDisplay.getChildren().add(leftHBox);
            }

        }
        BorderPane conversationBox = new BorderPane();
        leftMessageDisplay.setSpacing(20);
        rightMessageDisplay.setSpacing(20);
        conversationBox.setLeft(leftMessageDisplay);
        conversationBox.setRight(rightMessageDisplay);
        conversationBox.setPrefWidth(450);
        scrollPane.setContent(conversationBox);

        borderPane.setCenter(scrollPane);

        HBox writingHbox = new HBox();
        TextArea messageField = new TextArea();
        messageField.setPrefWidth(550);
        messageField.setPrefHeight(30);
        messageField.setBorder(Border.EMPTY);

        Button sendButton = new Button();
        Image sendImg = null;
        try {
            sendImg = new Image(new FileInputStream("ressource/image/send.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView sendImgView = new ImageView(sendImg);
        sendImgView.setFitHeight(30);
        sendImgView.setFitWidth(30);
        sendButton.setGraphic(sendImgView);
        sendButton.setPrefSize(30,30);
        //sendButton.setTextFill(Color.WHITE);
        //sendButton.setStyle("-fx-background-color: green");
        sendButton.setOnAction(event -> {
            String messageToSend = messageField.getText();
            if (!messageToSend.equals("")){
                /*DateFormat dateFormat = new SimpleDateFormat();
                String dateToString = dateFormat.format(new Date());*/

                try {
                    ChatRemote messageRemote = (ChatRemote) Naming.lookup("rmi://localhost:1099/ChatService");
                    if (messageRemote.sendMessage(messageToSend,currentUser.getId(), receiverInfo.getId())){
                        addMessageInConversationBox(messageToSend,leftMessageDisplay,rightMessageDisplay,conversationBox);
                        for(User u : sessionUser.getAllLoggedUser()){
                            if(u.getId() == receiverInfo.getId()){
                                u.setNotifyUser(false);
                                SharedMessageRemote sharedMes = (SharedMessageRemote) Naming.lookup("rmi://localhost:1099/SharedService");
                                sharedMes.setContent(new Messagerie(currentUser.getId(),receiverInfo.getId(),messageToSend,"envoyé"));
                                sharedMes.setReceiver(receiverInfo.getId());
                                sharedMes.setSender(currentUser.getId());
                                if(sharedMes.getContent() != null && sharedMes.getReceiver() == currentUser.getId()){
                                    addMessageInConversationBox(sharedMes.getContent().getContent(),rightMessageDisplay,leftMessageDisplay,conversationBox);
                                }
                                u.setNewMessage(messageToSend);
                            }
                        }
                        messageField.setText("");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Failed to send new message !!!");
                }
            }

        });
        writingHbox.getChildren().addAll(messageField,sendButton);
        writingHbox.setPadding(new Insets(10));
        writingHbox.setStyle("-fx-background-color: white");
        borderPane.setBottom(writingHbox);

        return borderPane;
    }

    public void addMessageInConversationBox(String message,VBox leftMessageDisplay,VBox rightMessageDisplay,BorderPane conversationBox)
    {
        Label leftLabel = new Label(message);
        leftLabel.setFont(Font.font("bold",17));
        Label rightLabel = new Label(message);
        rightLabel.setTextFill(Color.WHITE);
        rightLabel.setFont(Font.font("bold",15));
        HBox leftHBox = new HBox();
        HBox rightHBox = new HBox();
        rightHBox.getChildren().add(rightLabel);
        rightLabel.setStyle("-fx-background-color: #2caf19");
        rightMessageDisplay.getChildren().add(rightHBox);
        leftLabel.setVisible(false);
        leftHBox.getChildren().add(leftLabel);
        leftMessageDisplay.getChildren().add(leftHBox);
    }
public void logoutMethod(UserRMI u, Event event){
    try{
        if (sessionUser.logout(u.getUser(u.getEmail()))){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }catch (Exception e){
        System.out.println("Failed to delete user session !!!");
    }
}
}
