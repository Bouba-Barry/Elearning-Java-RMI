package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import metier.User;

import java.util.ArrayList;
import java.util.List;

public class MenuTeacher extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,800,600);

        VBox leftPanel = new VBox();
        VBox rightPanel = new VBox();

        /* * ********** traitement du leftPanel ***********************/
        HBox conversationHbox = new HBox();
        HBox logoutHbox = new HBox();

        // part conversation
        TitledPane titledPaneConv = new TitledPane();
        titledPaneConv.setPrefSize(200,50);
        titledPaneConv.setText("Conversation");
        titledPaneConv.setExpanded(false);
        VBox vBoxTmp = new VBox();
        vBoxTmp.getChildren().addAll(new Label("classe1"),new Label("classe2"));
        titledPaneConv.setContent(vBoxTmp);
        conversationHbox.getChildren().addAll(titledPaneConv);

        //part of logout
        Button logoutButton = new Button("logout");
        logoutHbox.getChildren().add(logoutButton);
        logoutButton.setPrefSize(200,10);

        leftPanel.getChildren().addAll(conversationHbox,logoutHbox);
        leftPanel.setSpacing(20);
        leftPanel.setPadding(new Insets(20,5,0,0));
        leftPanel.setStyle("-fx-background-color: lightgray");

        /*************************  right Panel ************************************  ***/

        VBox module1Vbox = new VBox();
        Label module1Label = new Label("Module 1");
        module1Label.setPadding(new Insets(3,150,0,0));
        Button module1Button = new Button("voir plus");
        module1Vbox.getChildren().addAll(module1Label,module1Button);
        module1Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");

        VBox module2Vbox = new VBox();
        Label module2Label = new Label("Module 2");
        module2Label.setPadding(new Insets(3,150,0,0));
        Button module2Button = new Button("voir plus");
        module2Vbox.getChildren().addAll(module2Label,module2Button);
        module2Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");

        VBox module3Vbox = new VBox();
        Label module3Label = new Label("Module 3");
        module3Label.setPadding(new Insets(3,150,0,0));
        Button module3Button = new Button("voir plus");
        module3Vbox.getChildren().addAll(module3Label,module3Button);
        module3Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");

        module1Vbox.setSpacing(10);
        module1Vbox.setAlignment(Pos.BASELINE_CENTER);
        module1Vbox.setPrefSize(200,80);

        module2Vbox.setSpacing(10);
        module2Vbox.setAlignment(Pos.BASELINE_CENTER);
        module2Vbox.setPrefSize(200,80);


        module3Vbox.setSpacing(10);
        module3Vbox.setAlignment(Pos.BASELINE_CENTER);
        module3Vbox.setPrefSize(200,80);

        rightPanel.getChildren().addAll(module1Vbox,module2Vbox,module3Vbox);
        rightPanel.setSpacing(30);
        rightPanel.setPadding(new Insets(15,20,0,20));
        rightPanel.setPrefWidth(600);
        ScrollPane scrollPaneRightPanel = new ScrollPane(rightPanel);
        scrollPaneRightPanel.setBorder(Border.EMPTY);
        scrollPaneRightPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        /** *******traitement du top *********************************/
        // label du prof
        HBox AccueilProf = new HBox();
        Label profName = new Label("Mr Inconnu");
        AccueilProf.getChildren().addAll(profName);
        AccueilProf.setPadding(new Insets(20,0,20,40));

        HBox CoursModuleHbox = new HBox();
        CoursModuleHbox.getChildren().add(new Label("Cours Module"));
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
        module1Button.setOnAction(event -> {
            BorderPane pane = moduleComponent(root,"Module1", scrollPaneRightPanel);
            root.setCenter(pane);
        });

        primaryStage.setTitle("Menu Teacher");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public BorderPane moduleComponent(BorderPane main,String moduleName, ScrollPane pred){
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
        VBox module1Vbox = new VBox();
        Label module1Label = new Label("CoursUml1.pdf ");
        module1Label.setPadding(new Insets(3,150,0,0));
        Button module1Button = new Button("remove");
        module1Vbox.getChildren().addAll(module1Label,module1Button);
        module1Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");

        VBox module2Vbox = new VBox();
        Label module2Label = new Label("Tp1.pdf");
        module2Label.setPadding(new Insets(3,150,0,0));
        Button module2Button = new Button("remove");
        module2Vbox.getChildren().addAll(module2Label,module2Button);
        module2Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");

        VBox module3Vbox = new VBox();
        Label module3Label = new Label("EXAMEN.pdf");
        module3Label.setPadding(new Insets(3,10,0,0));
        Label description = new Label("Ce fichier contient lllllqmflqf qsdmldfmfqdm");
        Label date_ajout = new Label("2022/24/12");
        Button module3Button = new Button("remove");
        module3Vbox.getChildren().addAll(module3Label,description,date_ajout,module3Button);
        module3Vbox.setStyle("-fx-background-color: rgba(255,255,255,0.87)");

        module1Vbox.setSpacing(10);
        module1Vbox.setAlignment(Pos.BASELINE_CENTER);
        module1Vbox.setPrefSize(200,80);

        module2Vbox.setSpacing(10);
        module2Vbox.setAlignment(Pos.BASELINE_CENTER);
        module2Vbox.setPrefSize(200,80);


        module3Vbox.setSpacing(10);
        module3Vbox.setAlignment(Pos.BASELINE_CENTER);
        module3Vbox.setPrefSize(200,80);

        Label ressourceTitle = new Label("Ressource Uploader");
        ressourceTitle.setAlignment(Pos.BASELINE_CENTER);

        centerVbox.getChildren().addAll(ressourceTitle,module1Vbox,module2Vbox,module3Vbox);
        centerVbox.setSpacing(30);
        centerVbox.setPadding(new Insets(15,20,0,20));
        centerVbox.setPrefWidth(600);
        ScrollPane scrollPaneRightPanel = new ScrollPane(centerVbox);
        scrollPaneRightPanel.setBorder(Border.EMPTY);
        scrollPaneRightPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        backButton.setOnAction(event -> {
            //stagePrincipale.setScene(scenePred);
            main.setCenter(pred);
        });

        borderPane.setTop(topVbox);
        borderPane.setCenter(scrollPaneRightPanel);
        return borderPane;
    }
}
