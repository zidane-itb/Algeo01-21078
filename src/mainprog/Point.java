import lombok.*;

// import java.util.Scanner;

public class Point {
    @Setter(AccessLevel.PRIVATE)
    private double x, y;

    public Point(Double x, Double y){
        this.x = x;
        this.y = y;
    }

    public void printPoint(){
        System.out.printf("(%f, %f)\n", this.x, this.y);
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }
}
