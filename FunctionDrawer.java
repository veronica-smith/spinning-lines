import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FunctionDrawer extends Application {
 ArrayList<Point2D> points;
 Point2D centre;
 public static final int WIDTH = 600;
 public static final int HEIGHT = 600;
 public static void main(String[] args) {
  launch(args);

 }

 @Override
 public void start(Stage primaryStage) throws Exception {
  primaryStage.setOnHiding(event -> {savePointsToFile("function.txt");});
  points = new ArrayList<Point2D>();
  centre = new Point2D(WIDTH/2, HEIGHT/2);
  Group root = new Group();
  Canvas canvas = new Canvas(WIDTH,HEIGHT);
  GraphicsContext gc = canvas.getGraphicsContext2D();
  root.getChildren().add(canvas);
  primaryStage.setScene(new Scene(root));
  primaryStage.show();

  // Clear the canvas when the user double-clicks
  canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, 
    new EventHandler<MouseEvent>() {
   @Override
   public void handle(MouseEvent t) {            
    if (t.getClickCount() >1) {
     points.clear();
     System.out.println("points cleared");
     gc.clearRect(0, 0, WIDTH, HEIGHT);
     clear(canvas, Color.WHITE);
    }  
   }
  });

  // Clear away portions as the user drags the mouse
  canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
    new EventHandler<MouseEvent>() {
   @Override
   public void handle(MouseEvent e) {
    points.add(new Point2D(e.getX()-centre.getX(), e.getY()));
    gc.beginPath();
    gc.lineTo(e.getX(), e.getY());
    gc.stroke();
    gc.closePath();
   }
  });

  canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
    new EventHandler<MouseEvent>() {
   @Override
   public void handle(MouseEvent e) {  
    gc.moveTo(e.getX(), e.getY());
    points.add(new Point2D(e.getX()-centre.getX(), e.getY()));
    gc.getPixelWriter().setColor((int)e.getX(), (int)e.getY(), Color.BLUE);
   }
  });
 }
 
 private void savePointsToFile(String filename)
 {
  String s;
  try {
   points = removeDuplicates();
   PrintWriter file = new PrintWriter(filename);
   for(int i=0; i<points.size(); i++) {
    Point2D p = points.get(i);
    p = new Point2D(p.getX(), centre.getY()-p.getY());
    s = Double.toString(p.getX()) + "," + Double.toString(p.getY())+"\n";
    file.write(s);
   }
   file.close();
    
  } catch (FileNotFoundException e) {
   e.printStackTrace();
  }
 }
 
 public ArrayList<Point2D> removeDuplicates()
 {
  ArrayList<Point2D> p = new ArrayList<Point2D>();
  p.add(points.get(0));
  for(int i=1; i<points.size()-1; i++)
  {
   if(points.get(i).distance(points.get(i-1)) != 0)
    p.add(points.get(i));
  }
  return p;
 }

 private void clear(Canvas canvas, Color color) {
  GraphicsContext gc = canvas.getGraphicsContext2D();
  gc.setFill(color);
  gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
 }
}