package assignment5;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ControllerUI {
    public Button makeBtn;
    public Button stepBtn;
    public Button seedBtn;
    public Button statsBtn;
    public Button quitBtn;
    public Button playBtn;
    public Button pauseBtn;
    public ChoiceBox<String> critterBox;
    public Slider sliderBox;
    public Label statsBox;
    public TextField numBox;
    public CheckBox runstatBox;
    public Button nukeBtn;
    public static AnimationTimer frame;
    private ArrayList<String> files;
    private static String myPackage;

    @FXML
    protected void initialize(){
        frame = new AnimationTimer() {
            private long prevStep = 0;

            @Override
            public void handle(long now) {
                if(now - prevStep >= (long)((100_000_000))){
                    prevStep = now;
                    Critter.worldTimeStep();
                    Critter.displayWorld();

                    if(runstatBox.isSelected()){
                        updateStats();
                    }
                }
            }
        };
        files = new ArrayList<String>();
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
        getFiles();
        initDropDown();
    }

    private void getFiles(){

        File cur = new File("./src/assignment5");
        File[] list = cur.listFiles();

        for(File f : list){

            if(f.isFile()){

                String name = f.toString().replace(".java", "");
                name = name.replace(cur.toString(), "");
                name = name.substring(1);
                files.add(name);
            }
        }
    }

    private void initDropDown(){

        String myPackage = Critter.class.getPackage().toString().split(" ")[1];

        for(String f : files){

            if(f.equals("Critter")){
                continue;
            }

            try{
                Class o = Critter.class;
                Class c = Class.forName(myPackage + "." + f).asSubclass(o);

                critterBox.getItems().add(f);
            }
            catch(ClassNotFoundException | ClassCastException e){
                continue;
            }
        }
    }

    public void makeCritter(){

        String type = critterBox.getValue();
        String input = numBox.getText();

        int num = 0;

        if(!input.equals("")){
            try{
                num = Integer.parseInt(input);
            }
            catch(NumberFormatException e){
                System.out.println("error processing");
            }
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
        if(runstatBox.isSelected()){
            updateStats();
        }
    }

    public void worldStep(){

        int num;
        String input = numBox.getText();
        if(!input.equals("")){
            num = getInput(numBox);
        }
        else{
            num = 1;
        }

        while(num > 0){

            Critter.worldTimeStep();
            --num;
        }

        Critter.displayWorld();
        if(runstatBox.isSelected()){
            updateStats();
        }
    }

    public void setSeed(){

        String input = numBox.getText();
        int num = 0;
        try{
            num = Integer.parseInt(input);
            Critter.setSeed(num);
        }
        catch(NumberFormatException e){
            System.out.println("error processing");
        }
    }

    public void playWorld(){

        buttonEnable(true);
        pauseBtn.setDisable(false);
        frame.start();

        pauseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                buttonEnable(false);
                pauseBtn.setDisable(true);
                System.out.println("working");
                frame.stop();
            }
        });
    }

    public void buttonEnable(boolean able) {

        stepBtn.setDisable(able);
        makeBtn.setDisable(able);
        seedBtn.setDisable(able);
        playBtn.setDisable(able);
    }

    public void updateStats(){
        String critterName = critterBox.getValue();
        try{
            List<Critter> list = Critter.getInstances(critterName);
            Class critterClass = Class.forName(myPackage + "." + critterName); //Reflection
            Method methodCall = critterClass.getMethod("runStats", List.class);
            String s = (String) methodCall.invoke(null, list);
            statsBox.setText(s);
        }
        catch(InvalidCritterException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoClassDefFoundError e){
            System.out.println("error processing");
        }
    }

    public void nuke(){
        Critter.clearWorld();
        Critter.displayWorld();
        if(runstatBox.isSelected()){
            updateStats();
        }

    }

    public void quit(){
        System.exit(0);
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
