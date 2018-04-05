package assignment5;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Tile extends GridPane {

    private double width;
    private double height;
    private double x_coord;
    private double y_coord;
    private double radius;
    private Rectangle cell;
    private Shape icon;

    public Tile(double x, double y, double width, double height){

        cell = new Rectangle(width, height);
        cell.setFill(Color.LIGHTGREY);
        cell.setStroke(Color.BLACK);

        cell.setX(x);
        cell.setY(y);
        setTranslateX(x);
        setTranslateY(y);
        getChildren().add(cell);

        this.width = width;
        this.height = height;
        this.x_coord = x;
        this.y_coord = y;
        this.radius = (width > height)? (height/2): (width/2); //takes smaller value and divides by two
        icon = null;

    }

    public void setTile(Critter cr){


        Shape new_icon;
        new_icon = new Rectangle(width, height);
        Color color = Color.LIGHTGREY;

        if(cr != null){
            Critter.CritterShape shape = cr.viewShape();
            color = cr.viewColor();

            switch(shape){
                case CIRCLE:
                    Circle new_circle = new Circle(x_coord, y_coord, radius);
                    new_icon = new_circle;
                    break;

                case SQUARE:
                    new_icon = new Rectangle(width, height);
                    break;

                case TRIANGLE:
                    Polygon new_tri = new Polygon();
                    new_tri.getPoints().addAll(x_coord + width/2, y_coord + 1 , x_coord + 1, y_coord + height - 1, x_coord + width - 1, y_coord + height - 1);
                    new_icon = new_tri;
                    break;

                case DIAMOND:
                    Polygon new_dia = new Polygon();
                    new_dia.getPoints().addAll(x_coord + width/2, y_coord + 1, x_coord + 1, y_coord + height/2, x_coord + width/2, y_coord + height - 1, x_coord + width - 1, y_coord + height/2);
                    new_icon = new_dia;
                    break;
                case STAR:
                    new_icon = new Circle(x_coord, y_coord, radius);
                break;
                case HOURGLASS:
                    Polygon new_hg = new Polygon();
                    new_hg.getPoints().addAll(x_coord, y_coord, x_coord + width - 1, y_coord, x_coord, y_coord + height, x_coord + width - 1, y_coord + height);
                    new_icon = new_hg;
                    break;
                default:
                    break;
            }
        }
        new_icon.setFill(color);
        new_icon.setStroke(Color.BLACK);
        if(icon != null){
            getChildren().remove(icon);
        }
        getChildren().add(new_icon);
        icon = new_icon;
    }
}
