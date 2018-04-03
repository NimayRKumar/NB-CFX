package assignment5;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;


public class Main extends Application {


    private double width = 650;
    private double height = 650;

    private Button make = new Button("Make");
    private Button stats = new Button("Stats");
    private Button step = new Button("Step");
    private Button seed = new Button("Seed");
    private Button quit = new Button("Quit");
    private Button play = new Button("Play");
    private Button pause = new Button("Pause");

    private double tile_width = width / Params.world_width;
    private double tile_height = height / Params.world_height;

    private AtomicBoolean playing = new AtomicBoolean(true);

    private static GridPane grid = new GridPane();
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



        drop_down.getItems().addAll("Algae", "Craig", "Critter1", "Critter2", "Critter3", "Critter4");
        input.setMaxWidth(74);

        GridPane.setConstraints(make, 1, 81);
        GridPane.setConstraints(step, 1, 83);
        GridPane.setConstraints(input, 0, 83);
        GridPane.setConstraints(drop_down, 0, 81);
        GridPane.setConstraints(quit,2, 83);
        GridPane.setConstraints(seed, 2, 81);
        GridPane.setConstraints(play,3, 81 );
        GridPane.setConstraints(pause, 3, 83);

	    make.setOnAction(e -> makeCritter(drop_down, input));
	    step.setOnAction(e -> worldStep(input));
	    seed.setOnAction(e -> setSeed(input));
	    quit.setOnAction(e -> System.exit(0));
        play.setOnAction(e -> playWorld());


        grid.getChildren().addAll(input, drop_down, make, step, quit, seed, pause, play);

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
	    int num;

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

        Critter.displayWorld();
    }

    private void worldStep(TextField input){

	    int num;

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




    private void playWorld(){

        AnimationTimer frame = new AnimationTimer() {
            @Override
            public void handle(long now) {

                Critter.worldTimeStep();
                Critter.displayWorld();
            }
        };

        step.setDisable(true);
        make.setDisable(true);
        seed.setDisable(true);
        frame.start();

        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playing.set(false);

                step.setDisable(false);
                make.setDisable(false);
                seed.setDisable(false);

                frame.stop();
            }
        });

            //Animation FrameRateMeter    animationtimer
        
            //pause.setOnAction(e-> update());


//            while(playing.get()) {
//
//                try {
//                    Thread.sleep(1000);
//                }
//                catch(InterruptedException e) {
//                     break;
//
//                }
//
//                Critter.worldTimeStep();
//                Critter.displayWorld();
//                System.out.println("working");
//            }

    }

    private void update(){
	    System.out.println("Pause");
	    playing.set(false);

        step.setDisable(false);
        make.setDisable(false);
        seed.setDisable(false);
    }

}
