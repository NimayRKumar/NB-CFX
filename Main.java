package assignment5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Main extends Application {
    private double width = 600;
    private double height = 600;
    private double controlWidth = 327;
    private double controlHeight = 400;
    private double tile_width = width / Params.world_width;
    private double tile_height = height / Params.world_height;
    private Scene scene = new Scene(grid, width + 25,  height + 25);
    private static GridPane grid = new GridPane();
    private static GridPane tool = new GridPane();
    protected static Tile[][] world = new Tile[Params.world_width][Params.world_height];


    @Override
    public void start(Stage primaryStage) throws Exception { ;
        Parent root = FXMLLoader.load(getClass().getResource("ControllerUI.fxml"));
        Stage viewStage = new Stage();
        initWorld();
        viewStage.setScene(scene);
        viewStage.setTitle("Critters GUI");
        viewStage.show();
        primaryStage.setScene(new Scene(root, controlWidth, controlHeight));
        primaryStage.setTitle("ToolBox");
        primaryStage.show();
    }

    public static void main(String[] args) {
        Critter.clearWorld();
        launch(args);
    }

    private void initWorld(){

        for(int i = 0; i < Params.world_width; ++i){

            for(int j = 0; j < Params.world_height; ++j){

                Tile tile = new Tile(i * tile_width, j * tile_height, tile_width, tile_height);
                grid.getChildren().add(tile);

                world[i][j] = tile;
            }
        }
    }


}