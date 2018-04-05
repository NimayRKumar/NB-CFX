package assignment5;
/* CRITTERS Main.java
 * EE422C Project 5 submission by
 * Nimay Kumar
 * nrk472
 * 15470
 * Benson Huang
 * bkh642
 * 15470
 * Slip days used: <0>
 * Spring 2018
 */

import javafx.scene.paint.Color;

//Herbivore critter
public class Critter2 extends Critter{

    private int dir;


    /**
     * Returns new Critter2
     * sets direction randomly
     */
    public Critter2(){

        dir = getRandomInt(8);
    }

    @Override
    public CritterShape viewShape() {

        return CritterShape.DIAMOND;
    }
    public javafx.scene.paint.Color viewColor() {

        return Color.OLDLACE;
    }
        /**
         * Does 1 time step of action for Critter2 objects
         * 1/2 chance to walk. 1/2 chance to rest
         * Reproduces at 120 energy
         * Sets new direction that is different from current direction
         */
    public void doTimeStep(){

        int new_dir = getRandomInt(8);

        if(getEnergy() > 120){
            Critter2 child = new Critter2();
            reproduce(child, getRandomInt(8));
        }

        if("@".equals(this.look(dir, false))){
            walk(dir);
        }
        else if("@".equals(this.look(dir, true))){
            run(dir);
        }
        else{
            walk(new_dir);
        }
        dir = new_dir;
    }

    /**
     * Only fights against algae
     * @param opponent String of opponent
     * @return true if opponent is algae, false else
     */

    public boolean fight(String opponent){

        if(opponent.equals("@")){
            return true;
        }

        return false;
    }

    /**
     * Returns M to represent Herbivore
     */
    public String toString(){

        return "M";
    }

    /**
     * Prints out number of Critter2 on map
     * @param herbivores list of all Critter2 alive
     */
    public static void runStats(java.util.List<Critter> herbivores ){

        System.out.println(herbivores.size() + " total Herbivores");
    }
}
