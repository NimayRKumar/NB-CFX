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

//Virus critter
public class Critter1 extends Critter {

    private int dir;
    private int type;

    /**
     * Creates new Critter1 object
     * assigns direction and type randomly
     */
    public Critter1() {

        dir = getRandomInt(8);
        type = getRandomInt(2);
    }

    @Override
    public CritterShape viewShape() {

        return CritterShape.TRIANGLE;
    }
    public javafx.scene.paint.Color viewColor() {

        return Color.LIGHTSTEELBLUE;
    }

    /**
     * Does time step for Critter1
     * Walks in direction of dir, and sets new distinct direction
     * Reproduces at 80 energy: two offspring if type B, one if type A
     */
    public void doTimeStep() {

        int new_dir = getRandomInt(8);

        walk(dir);

        if(getEnergy() >= 80){

            createOffspring();
            if(type == 1){
                createOffspring();
            }
        }

        while (new_dir != dir) {
            new_dir = getRandomInt(8);
        }

        dir = new_dir;
    }

    /**
     * Critter1 always fights
     *
     * @param irrelevant is irrelevant
     * @return true every time
     */
    public boolean fight(String irrelevant) {

        return true;
    }

    /**
     * Getter function for type of Virus
     * @return type of virus
     */

    public int getType(){
        return type;
    }

    /**
     * creates offspring, and calls reproduce
     */
    private void createOffspring() {

        Critter1 child = new Critter1();
        reproduce(child, getRandomInt(8));
    }

    /**
     * toString for Critter1
     * @return V for Virus
     */
    public String toString() {

        return "V";
    }

    /**
     * Prints out total number of Critter1 on map, as well as number of A and B type
     * @param viruses list of all living Critter1
     */
    public static String runStats(java.util.List<Critter> viruses){

        int numA = 0;
        int numB = 0;

        for(Object o : viruses){

            Critter1 c = (Critter1) o;
            if(c.type == 0){
                ++numA;
            }
            else{
                ++numB;
            }
        }

        return viruses.size() + " total Viruses\n" + numA + " type A viruses\n" + numB + " type B viruses\n";
    }
}



