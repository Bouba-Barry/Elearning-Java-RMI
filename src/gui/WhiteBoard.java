package gui;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.WhiteBoardRemote;

import javax.imageio.ImageIO;
import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;


public class WhiteBoard {
    private WhiteBoardRemote whiteBoardRemote;
    private int module_id;
    public WhiteBoard(int module_id){
        this.module_id = module_id;
        try{
            whiteBoardRemote = (WhiteBoardRemote) Naming.lookup("rmi://localhost:1099/WhiteBoardService");
        }catch (Exception e){e.printStackTrace();}
     }
    public BorderPane start(Stage primaryStage,BorderPane main,ScrollPane pred) throws Exception {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas();
        canvas.setHeight(400);
        canvas.setWidth(600);

        metier.WhiteBoard whiteBoard = whiteBoardRemote.getWhiteBoardUpdated(module_id);
        byte[] imageData = whiteBoard.getContent();

        if(imageData != null){
            try {
                Image image = new Image(new ByteArrayInputStream(imageData));
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.drawImage(image, 0, 0);
            } catch (Exception e) {
                System.out.println("Failed to load image !!!");
            }
        }

        root.setCenter(canvas);
        root.setTop(getHeaderElement(canvas,primaryStage,main,pred));

        //Using Timer and TimerTask
      /*  Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        };
        timer.scheduleAtFixedRate(task, 0, 5000); */
        return root;

    }

    public VBox getHeaderElement(Canvas canvas,Stage stage, BorderPane main,ScrollPane pred){
        VBox vBox = new VBox();
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Files");
        MenuItem menuItem1 = new MenuItem("save");
        menuItem1.setOnAction(event -> {
            try {
                whiteBoardRemote.updateWhiteBoard(module_id,onSave(canvas));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        MenuItem menuItem2 = new MenuItem("exit");
        menuItem2.setOnAction(event -> {
            //onExit();
            // enregistrement du whiteBoard en cours
            try {
                whiteBoardRemote.updateWhiteBoard(module_id,onSave(canvas));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            main.setCenter(pred);

        });
        menu.getItems().addAll(menuItem1,menuItem2);
        menuBar.getMenus().add(menu);


        ToolBar toolBar = new ToolBar();
        HBox hBox = new HBox();
        TextField brushSize = new TextField("20");
        ColorPicker colorPicker = new ColorPicker();
        CheckBox eraser = new CheckBox("eraser");
        CheckBox shareWhiteboard = new CheckBox("share whiteboard");
        shareWhiteboard.setAlignment(Pos.TOP_RIGHT);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);
        hBox.getChildren().addAll(brushSize,colorPicker,eraser);
        toolBar.getItems().addAll(hBox,shareWhiteboard);

        vBox.getChildren().addAll(menuBar,toolBar);





        //Canvas events
        GraphicsContext g = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(event -> {
            double size = Double.parseDouble(brushSize.getText());
            double x = event.getX() -size/2;
            double y = event.getY() -size/2;
            if (eraser.isSelected()){
                g.clearRect(x,y,size,size);
            }else {
                g.setFill(colorPicker.getValue());
                g.fillRect(x,y,size,size);
            }
        });
        canvas.setOnMouseDragReleased(event -> {

            //onSave(canvas);
        });


        return vBox;
    }

    public byte[] onSave(Canvas canvas){
        try{
            Image snapshot = canvas.snapshot(null,null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null),"png", baos);
            return baos.toByteArray();
        } catch (Exception e){
            System.out.println("Failed to save !!!");
            return null;
        }
    }

    public void onExit(){
        Platform.exit();
    }
}
