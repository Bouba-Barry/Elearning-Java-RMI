package gui;

import client.ClientRMI;
import dao.ClasseDao;
import dao.ModuleDao;
import dao.RessourceDao;
import dao.UserDao;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
        Button logoutButton = new Button("logout");
        logoutHbox.getChildren().add(logoutButton);
        logoutButton.setPrefSize(180,10);

        leftPanel.getChildren().addAll(conversationHbox,logoutHbox);
        leftPanel.setSpacing(20);
        leftPanel.setPadding(new Insets(20,5,0,0));
        leftPanel.setStyle("-fx-background-color: lightgray");

        /*************************  right Panel ************************************  ***/
        for(Module m: modules){
            VBox module1Vbox = new VBox();
            Label module1Label = new Label(m.getLibelle());
            module1Label.setPadding(new Insets(3,150,0,0));
            Button module1Button = new Button("voir plus");
            module1Vbox.getChildren().addAll(module1Label,module1Button);
            module1Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");


            module1Vbox.setSpacing(10);
            module1Vbox.setAlignment(Pos.BASELINE_CENTER);
            module1Vbox.setPrefSize(200,80);
            rightPanel.getChildren().addAll(module1Vbox);

            module1Button.setOnAction(event -> {
                BorderPane pane = moduleComponent(root,m.getLibelle(),m.getId() ,scrollPaneRightPanel,primaryStage);
                root.setCenter(pane);
            });
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
        // label du prof
        HBox AccueilProf = new HBox();
        Label profName = new Label(""+currentUser.getNom());
        profName.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        AccueilProf.getChildren().addAll(profName);
        AccueilProf.setPadding(new Insets(20,0,20,40));

        HBox CoursModuleHbox = new HBox();
        Label modLabel = new Label("Vos Modules");
        modLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
        CoursModuleHbox.getChildren().add(modLabel);
        CoursModuleHbox.setAlignment(Pos.BASELINE_CENTER);
        CoursModuleHbox.setPadding(new Insets(20,100,20,300));

        HBox topHbox = new HBox();
        topHbox.setSpacing(20);
        topHbox.setPadding(new Insets(5,50,30,0));
        topHbox.getChildren().addAll(AccueilProf,CoursModuleHbox);
        topHbox.setStyle("-fx-background-color: rgb(234, 202, 83, 1)");

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
        moduleNameLabel.setAlignment(Pos.BASELINE_CENTER);
        moduleNameLabel.setFont(new Font(20));

        Button backButton = new Button("retour Menu");
        backButton.setAlignment(Pos.TOP_LEFT);

        Button whiteBoardButton = new Button("tableau blanc");
        whiteBoardButton.setAlignment(Pos.TOP_RIGHT);


        topLabHbox.getChildren().addAll(backButton,moduleNameLabel,whiteBoardButton );
        topLabHbox.setSpacing(125);
        topLabHbox.setPadding(new Insets(5,5,10,5));

        HBox topOpeHbox = new HBox();
        TextField descriptionTextFied = new TextField();
        descriptionTextFied.setPromptText("describe file");
        Button uploadButton = new Button("upload");
        //uploadButton.setAlignment(Pos.TOP_RIGHT);
        uploadButton.setOnAction(event -> {
            String desc = descriptionTextFied.getText();
            uploadFile(desc,module_id, currentUser.getNom(),stage);
        });

        topOpeHbox.getChildren().addAll(descriptionTextFied,uploadButton);
        topOpeHbox.setSpacing(5);
        topOpeHbox.setAlignment(Pos.BASELINE_CENTER);

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
        centerVbox.getChildren().add(ressourceTitle);

        for(Ressource r : ressources){
            VBox module1Vbox = new VBox();
            Label module1Label = new Label(r.getRessource_name());
            module1Label.setPadding(new Insets(3,150,0,0));
            Button module1Button = new Button("remove");
            module1Vbox.getChildren().addAll(module1Label,module1Button);
            module1Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");
            module1Vbox.setSpacing(10);
            module1Vbox.setAlignment(Pos.BASELINE_CENTER);
            module1Vbox.setPrefSize(200,80);
            centerVbox.getChildren().add(module1Vbox);

        }

        centerVbox.setSpacing(30);
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

        Button backButton = new Button("retour Menu");
        backButton.setAlignment(Pos.TOP_LEFT);
        backButton.setOnAction(event -> {
            main.setCenter(pred);
        });


        hBox.getChildren().addAll(backButton,receiverNameLabel);
        hBox.setSpacing(80);
        // ce qui est ajouté de suite ....
        hBox.setPadding(new Insets(10,0,0,150));
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
            Label leftLabel = new Label(message.getContent());
            leftLabel.setFont(Font.font("bold",15));
            Label rightLabel = new Label(message.getContent());
            rightLabel.setFont(Font.font("bold",15));
            if (message.getSender_id()==receiverInfo.getId()){
                //Message have to be displayed at left
                leftHBox.getChildren().add(leftLabel);
                leftHBox.setStyle("-fx-background-color: lightgray");
                leftMessageDisplay.getChildren().add(leftHBox);
                rightLabel.setVisible(false);
                rightHBox.getChildren().add(rightLabel);
                rightMessageDisplay.getChildren().add(rightHBox);
            }else {
                //Message have to be displayed at right
                rightHBox.getChildren().add(rightLabel);
                rightLabel.setStyle("-fx-background-color: #C0C0C0");
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
        messageField.setPrefWidth(700);
        messageField.setPrefHeight(30);
        messageField.setBorder(Border.EMPTY);

        Button sendButton = new Button("send");
        sendButton.setPrefSize(100,40);
        sendButton.setFont(Font.font("bold",20));
        //sendButton.setTextFill(Color.WHITE);
        sendButton.setStyle("-fx-background-color: green");
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
        leftLabel.setFont(Font.font("bold",15));
        Label rightLabel = new Label(message);
        rightLabel.setFont(Font.font("bold",15));
        HBox leftHBox = new HBox();
        HBox rightHBox = new HBox();
        rightHBox.getChildren().add(rightLabel);
        rightLabel.setStyle("-fx-background-color: #C0C0C0");
        rightMessageDisplay.getChildren().add(rightHBox);
        leftLabel.setVisible(false);
        leftHBox.getChildren().add(leftLabel);
        leftMessageDisplay.getChildren().add(leftHBox);
    }

}
