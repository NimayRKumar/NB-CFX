package assignment5;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends GridPane {

    private double width;
    private double height;
    private double x;
    private double y;
    private Rectangle rect;

    public Tile(double x, double y, double width, double height){

        rect = new Rectangle(width, height);
        rect.setStroke(Color.BLACK);
        rect.setFill(Color.LIGHTGREY);

        setTranslateX(x);
        setTranslateY(y);

        getChildren().add(rect);

        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void setTile(Color fill){

        Rectangle new_rect = new Rectangle(width, height);

        new_rect.setFill(fill);
        new_rect.setStroke(Color.BLACK);


        getChildren().remove(rect);
        getChildren().add(new_rect);

        rect = new_rect;
    }
}
