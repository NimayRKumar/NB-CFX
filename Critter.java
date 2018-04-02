package assignment5;

import javafx.scene.paint.Color;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR,
        HOURGLASS
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

    private static List<List<List<Critter>>> map = new ArrayList<List<List<Critter>>>();
    private static List<Critter3> reincarnation = new ArrayList<Critter3>();


	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	protected final String look(int direction, boolean steps) {

	    energy -= Params.look_energy_cost;
	    int dist = 1;

	    if(steps){
	        ++dist;
        }

	    if(!isEmpty(direction, dist)){
	        ArrayList<Integer> coord = getNewCoord(direction, dist);
	        for(Critter c : population){
	            if(c.old_x_coord == coord.get(0) && c.old_y_coord == coord.get(1)){
	                return c.toString();
                }
            }
        }
        return "";
	}
	
	/* rest is unchanged from Project 4 */
	
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	private int old_x_coord;
	private int old_y_coord;

    private boolean alive;
    private boolean moved;

    private boolean isAlive(){
        return alive;
    }
    private boolean hasMoved(){
        return moved;
    }

    private static void initializeMap(){
        map.clear();
        for (int row = 0; row < Params.world_height; ++row) {
            map.add(row, new ArrayList<>());
            for (int col = 0; col < Params.world_width; ++col) {
                map.get(row).add(col, new ArrayList<Critter>()); //sets every cell to empty
            }
        }
    }

    protected final void walk(int direction) {

        if(!moved){
            map.get(y_coord).get(x_coord).remove(this);
            move(direction, 1);
            map.get(y_coord).get(x_coord).add(this);
            moved = true;
        }
        energy -= Params.walk_energy_cost;
    }

    protected final void run(int direction) {

        if(!moved){
            map.get(y_coord).get(x_coord).remove(this);
            move(direction, 2);
            map.get(y_coord).get(x_coord).add(this);
            moved = true;
        }
        energy -= Params.run_energy_cost;
    }

    protected final void reproduce(Critter offspring, int direction) {

        if(this.getEnergy() < Params.min_reproduce_energy){
            return;
        }

        offspring.alive = true;
        offspring.moved = false;
        offspring.energy = (this.energy / 2);
        this.energy = (this.energy + 1) / 2; //pseudo CEIL formula
        offspring.x_coord = this.x_coord;
        offspring.y_coord = this.y_coord;
        offspring.move(direction, 1);
        babies.add(offspring);
    }

    private void move(int direction, int dist){

        if(direction == 0 || direction == 1 || direction == 7){
            x_coord += dist;
        }
        else if(direction == 3 || direction ==4 || direction == 5){
            x_coord -= dist;
        }

        if(direction == 1 || direction == 2 || direction == 3){
            y_coord -= dist;
        }
        else if(direction == 5 || direction == 6 || direction == 7) {
            y_coord += dist;
        }
        fixCoord();
    }

    private void fixCoord(){

        if(x_coord >= Params.world_width){
            x_coord %= Params.world_width;
        }
        else if(x_coord < 0){
            x_coord += Params.world_width ;
        }

        if(y_coord >= Params.world_height){
            y_coord %= Params.world_height;
        }
        else if(y_coord < 0){
            y_coord += Params.world_height;
        }
    }

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);


    public static void worldTimeStep() {

        individualTimeStep();
        removeDeadCritters();
        resolveEncounters();
        postEncounterTimeStep();
        removeDeadCritters();
        respawnAlgae();
        babyPopulate();
    }

    private static void saveOldCoord(){

        for(Critter c : population){
            c.old_x_coord = c.x_coord;
            c.old_y_coord = c.y_coord;
        }
    }

    private static void individualTimeStep(){
        saveOldCoord();
        for(Critter c : population){
            c.doTimeStep();
        }
        List<Critter3> reincarnated = new ArrayList<>();
        for(Critter3 hindu : reincarnation){
            hindu.doTimeStep();
            if(!hindu.getDead()){
                reincarnated.add(hindu);
            }
        }
        reincarnation.removeAll(reincarnated);
        reincarnated.clear();
        saveOldCoord();
    }

    private static void postEncounterTimeStep(){
        for(Critter c: population){
            c.energy -= Params.rest_energy_cost;
            if(c.getEnergy() <= 0){
                c.alive = false;
            }
            else{
                c.moved = false;
            }
        }
    }

    private static void resolveEncounters() {
        int luckA, luckB;
        Critter critterA, critterB, cr;
        boolean fightA, fightB;


        for (int row = 0; row < Params.world_height; ++row) {
            for (int col = 0; col < Params.world_width; ++col) {

                //Critter dies naturally
                if(map.get(row).get(col).size() == 1){

                    cr = (Critter) map.get(row).get(col).get(0);

                    if(cr.getEnergy() <= 0){
                        map.get(row).get(col).remove(cr);
                    }
                }

                //encounters to be resolved
                while(map.get(row).get(col).size() > 1){
                    critterA = (Critter) map.get(row).get(col).get(0);
                    critterB = (Critter) map.get(row).get(col).get(1);

                    if(critterA.getEnergy() <= 0 ){
                        map.get(row).get(col).remove(critterA);
                    }

                    if(critterB.getEnergy() <= 0 ){
                        map.get(row).get(col).remove(critterA);
                    }

                    if(critterA.getEnergy() <= 0 || critterB.getEnergy() <= 0){
                        continue;
                    }


                    fightA = critterA.fight(critterB.toString());
                    if(!fightA && !(critterA instanceof TestCritter)){
                        critterA.flee();
                    }
                    fightB = critterB.fight(critterA.toString());
                    if(!fightB && !(critterB instanceof TestCritter)){
                        critterA.flee();
                    }

                    if(sameLocation(critterA, critterB) && critterA.getEnergy() > 0 && critterB.getEnergy() > 0){

                        if(fightA){
                            luckA = getRandomInt(critterA.getEnergy());
                        }
                        else{
                            luckA = 0;
                        }

                        if(fightB){
                            luckB = getRandomInt(critterB.getEnergy());
                        }
                        else{
                            luckB = 0;
                        }

                        if(luckA >= luckB){
                            critterA.energy += (critterB.energy/2);
                            critterB.alive = false;
                            map.get(row).get(col).remove(critterB);
                        }
                        else{
                            critterB.energy += (critterA.energy/2);
                            critterA.alive = false;
                            map.get(row).get(col).remove(critterA);
                        }

                    }
                }
            }
        }
    }

    private static boolean sameLocation(Critter a, Critter b){
        return (a.x_coord == b.x_coord && a.y_coord == b.y_coord);
    }

    private static void removeDeadCritters(){
        List<Critter> deadCritters = new java.util.ArrayList<>();
        for(Critter c : population){
            if(!c.alive || c.getEnergy() <= 0){
                map.get(c.y_coord).get(c.x_coord).remove(c);
                deadCritters.add(c);
                if(c instanceof Critter3){
                    ((Critter3) c).setDead();
                    reincarnation.add((Critter3) c);
                }
            }
        }
        population.removeAll(deadCritters);
    }

    private static void respawnAlgae(){
        for(int i = 0; i < Params.refresh_algae_count; ++i){
            try{
                Critter.makeCritter("Algae");
            }
            catch(InvalidCritterException e){
                e.printStackTrace(); //do something with exception
            }
        }
    }

    private static void babyPopulate(){

        for(Critter c : babies){

            map.get(c.y_coord).get(c.x_coord).add(c);
        }

        population.addAll(babies);
        babies.clear();
    }


	public static void displayWorld() {

        String cr;
        Tile tile;

        for(int i = 0; i < Params.world_width; ++i){

            for(int j = 0; j < Params.world_height; ++j){

                tile = Main.world[i][j];

                if(map.get(j).get(i).size() > 0) {
                    tile.setTile(map.get(j).get(i).get(0));

                }
                else{
                    tile.setTile(null);
                }

                }
            }
        }

    public static void makeCritter(String critter_class_name) throws InvalidCritterException {

        try {
            Class c = Class.forName(myPackage + "." + critter_class_name);
            Critter cr = (Critter) c.getDeclaredConstructor().newInstance();
            cr.energy = Params.start_energy;
            cr.x_coord = getRandomInt(Params.world_width);
            cr.y_coord = getRandomInt(Params.world_height);
            cr.alive = true;
            cr.moved = false;
            population.add(cr);
            map.get(cr.y_coord).get(cr.x_coord).add(cr);

        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException | NoClassDefFoundError e) {
            throw new InvalidCritterException(critter_class_name);
        }

    }

    private void flee(){

        if(moved){
            energy -= Params.walk_energy_cost;
            return;
        }

        int new_dir = getRandomInt(8);
        if(isEmpty(new_dir, 1)){
            walk(new_dir);
        }
    }

    private boolean isEmpty(int dir, int steps){

        ArrayList<Integer> coord = getNewCoord(dir, steps);

        for(Critter c : population){
            if(c.old_x_coord == coord.get(0) && c.old_y_coord == coord.get(1)){
                return false;
            }
        }

        return true;
    }

    private ArrayList<Integer> getNewCoord(int dir, int steps){

        int new_x = x_coord;
        int new_y = y_coord;
        ArrayList<Integer> coord = new ArrayList<>();

        if(dir == 0 || dir == 1 || dir == 7){
            new_x += steps;
        }
        else if(dir == 3 || dir == 4 || dir ==5){
            new_x -= steps;
        }

        if(dir == 1 || dir == 2 || dir ==3){
            new_y -= steps;
        }
        else if(dir == 5 || dir == 6 || dir == 7){
            new_y += steps;
        }

        if(new_x >= Params.world_width){
            new_x %= Params.world_width;
        }
        else if(new_x < 0){
            new_x += Params.world_width ;
        }

        if(new_y >= Params.world_height){
            new_y %= Params.world_height;
        }
        else if(new_y < 0){
            new_y += Params.world_height;
        }

        coord.add(new_x);
        coord.add(new_y);

        return coord;
    }

        public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		return null;
	}



	public static void runStats(List<Critter> critters) {}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure thath the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctup update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}
	
	/**
	 * Clear the world of all critters, dead and alive
	 */
        public static void clearWorld() {

            population.clear();
            babies.clear();
            initializeMap();
        }

}
