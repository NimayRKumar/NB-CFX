package assignment5;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


//import java.awt.*;


public class Main extends Application {


    private double width = 500;
    private double height = 500;

    private double tile_width = width / Params.world_width;
    private double tile_height = height / Params.world_height;




    private Pane layout = new Pane();
    private GridPane grid = new GridPane();
    protected static Tile[][] world = new Tile[Params.world_width][Params.world_height];
    private Scene scene = new Scene(grid, width,  height);

	public static void main(String[] args) {

	    Critter.clearWorld();
	    launch(args);
	}

    @Override
    public void start(Stage primaryStage) throws Exception {

	    primaryStage.setTitle("Critters GUI");

	    grid.setPadding(new Insets(10, 10, 10, 10));
	    grid.setVgap(8);
	    grid.setHgap(10);


	    initWorld();


        ChoiceBox<String> drop_down = new ChoiceBox<>();
        TextField input = new TextField();

        Button make = new Button("Make");
        Button stats = new Button("Stats");
        Button step = new Button("Step");
        Button seed = new Button("Seed");
        Button quit = new Button("Quit");

        drop_down.getItems().addAll("Algae", "Craig", "Critter1", "Critter2", "Critter3", "Critter4");
        input.setMaxWidth(74);

        GridPane.setConstraints(make, 35, 0);
        GridPane.setConstraints(step, 35, 1);
        GridPane.setConstraints(input, 35, 2);

	    make.setOnAction(e -> makeCritter(drop_down, input));
	    step.setOnAction(e -> worldStep(input));
	    seed.setOnAction(e -> setSeed(input));
	    quit.setOnAction(e -> System.exit(0));

        grid.getChildren().addAll(input, drop_down, make, step);

	    primaryStage.setScene(scene);
	    primaryStage.show();
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
    private void makeCritter(ChoiceBox<String> drop_down, TextField input){

	    String type = drop_down.getValue();
	    int num = 0;

        if(!input.getText().equals("")){

            num = getInput(input);
        }
        else{
            num = 1;
        }

        while(num > 0) {

            try {
                Critter.makeCritter(type);
            }
            catch (NumberFormatException | InvalidCritterException e) {
                System.out.println("error processing");
            }

            --num;
        }
    }

    private void worldStep(TextField input){

	    int num = 0;

	    if(!input.getText().equals("")){

	        num = getInput(input);
        }
        else{
	        num = 1;
        }

        while(num > 0){

	        Critter.worldTimeStep();
	        --num;
        }

        Critter.displayWorld();
    }

    private void setSeed(TextField input){

	    long num = getInput(input);
	    Critter.setSeed(num);
    }

    private int getInput(TextField input){

	    int num = 0;

	    try{
	        num = Integer.parseInt(input.getText());
        }
        catch(NumberFormatException e){
	        System.out.println("error processing");
        }

        return num;
    }


}
