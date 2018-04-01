package assignment5;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


//import java.awt.*;


public class Main extends Application {

	public static void main(String[] args) {

	    Critter.clearWorld();
	    launch(args);


	}

    @Override
    public void start(Stage primaryStage) throws Exception {

	    primaryStage.setTitle("Critters");

	    Pane layout = new Pane();
        Scene scene = new Scene(layout, 500, 500);

        ChoiceBox<String> drop_down = new ChoiceBox<>();
        TextField input = new TextField();

        Button make = new Button("Make");
        Button stats = new Button("Run Stats");
        Button step = new Button("Step");



        drop_down.getItems().addAll("Algae", "Craig", "Critter1", "Critter2", "Critter3", "Critter4");

	    make.setLayoutX(5);
	    make.setLayoutY(475);

	    stats.setLayoutX(100);
        stats.setLayoutY(475);

        step.setLayoutX(55);
        step.setLayoutY(475);

        input.setMaxWidth(50);
        input.setLayoutX(80);


	    make.setOnAction(e -> makeCritter(drop_down, input));
	    step.setOnAction(e -> worldStep(input));

        layout.getChildren().addAll(input, make, stats, step, drop_down);

	    primaryStage.setScene(scene);
	    primaryStage.show();
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
