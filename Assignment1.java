//ICS4U1 Assignment 1: REVIEW Part 3
//Name: Veronica Smith
//Mr. Radulovic
//ICS4U1
//September 30, 2019

/*About the Assignment:
 * This program approximates a hardcoded function or a drawing
 * The coefficients, starting and end point of each spinning line is found through equations 
 * At the end of the last spinning line, the approximated function will trace
 * The approximated function is compared to the desired function (shown on screen)
 * The function is callled right after public void start
 * To call the hardcoded function, leave the argument blank
 * To call a function from a text file, write the file as a String in the argument
 */

//importing 
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Assignment1 extends Application {
  
  int WIDTH = 600; //window width (horizontal length)
  int HEIGHT = 600; //window height (vertical length)
  double time = 0; //setting time for animation timer
  
  ArrayList<Double[]> cf = new ArrayList<Double[]>(); // x and y starting end points (coefficients: (cfx, cfy))
  
  ArrayList<Double[]> function = new ArrayList<Double[]>(); //(x,y) coordinates of the desired values from function
  ArrayList<Double[]> approxCor = new ArrayList<Double[]>(); //(x,y) coordinates of the approximated values from function
  
  Group group = new Group(); //new group 
  
  /*HARDCODED FUNCTION
   * this method returns a hardcoded function with (x, y) coordinates
   * it goes through a for loop from the lowest to highest x value
   * y is a function of x 
   * x and y are added to an Array, which is then added to the ArrayList
   */
  public ArrayList<Double[]> loadFunction() 
  {
    ArrayList<Double[]> graph = new ArrayList<>(); //hardcoded (x,y) coordinates
    for(Double x=-150.0; x <= 150.0; x++) //lowest to highest x v alue
    {
      Double y = 0.01*x*x; 
      //y = f(x) = 0.01x^2 
      //vertically compressed so its points on the window could be looked at closer 
      Double [] xy = {x, y}; //Array of (x, y) coordinate
      graph.add(xy); //added to the ArrayList
    }      
    return graph;
  }
  
  /* READING DATA COORDINATES FROM A TEXT FILE
   * text file name is passed through as an argument
   * It scans through the file, and separates the x and y coordinates
   * x and y are added to an Array, which is then added to the ArrayList
   */
  public ArrayList<Double[]> loadFunction(String file) throws FileNotFoundException
  { 
    File coordinates = new File(file); //text file
    ArrayList<Double[]> graph = new ArrayList<Double[]>(); // (x, y) coordinates from text file
    
    Scanner scanner = new Scanner(coordinates); //opening Scanner
    
    while (scanner.hasNextLine()) //while there is a next line
    {
      String[] str = new String[2]; //two points
      str = scanner.nextLine().split(","); //separated by comma
      
      //converting String value to Double value
      Double x = Double.parseDouble(str[0]); //first is the x coordinate
      Double y = Double.parseDouble(str[1]); //second is the y coordinate
      Double [] xy = {x, y}; //Array of (x, y) coordinate
      graph.add(xy); //added to the ArrayList
    }  
    scanner.close();  //closing Scanner
    return graph;
  }
  
  /* DESIRED FUNCTION
   * function is drawn from already given coordinates 
   * from ArrayList<Double[]> function
   * Lines are connected from point to point
   */
  public void drawFunction()
  {
    for (int i = 0; i < function.size(); i++) //for all points in function
    {
      Line desiredF = new Line(); //new line that connects from desired point to point
      
      /*
       * WIDTH/2 is added to x because the x coordinate of the center of the window is WIDTH/2, not 0
       * y is subtracted from HEIGHT/2 because the y coordinate of the center of the window is HEIGHT/2, not 0
       * and as the y points increase, it goes down on the window, not up
       */
      
      //start point of line  
      desiredF.setStartX(WIDTH/2 + function.get(i)[0]); 
      desiredF.setStartY(HEIGHT/2 - function.get(i)[1]);
      
      //if it's the first line, the start point and the end point are equal
      if (i==0) 
      {
        desiredF.setEndX(WIDTH/2 + function.get(i)[0]);
        desiredF.setEndY(HEIGHT/2 - function.get(i)[1]);
      }
      
      //the end point is the point from the previous line
      else
      {
        desiredF.setEndX(WIDTH/2 + function.get(i-1)[0]);
        desiredF.setEndY(HEIGHT/2 - function.get(i-1)[1]);
      }
      group.getChildren().add(desiredF); //line is added 
    }
  }
  
  public static void main(String[] args) //main method
  {
    launch(args);
  }
  /*
   * Making window (stage)
   * coefficients (cfx and cfy) of the lines are calculated
   * The animation timer will start
   * drawFunction() is called - the desired function is drawn on the window
   * the x and y points of the approximated function is calculated
   * the ending point of the last line will trace the function
   */
  public void start(Stage primaryStage) throws FileNotFoundException 
  {
    //adding the desired coordinates to function made from loadFunction()
    //put in String file in argument to call for coordinates from text file
    //leave the argument blank to call for coordinates from the hard coded function
    function = loadFunction("function.txt"); 
    
    double fSize = function.size(); //number of coordinates in function 
    double deltaT = 1.0/(fSize-1); //deltaT: time increment that will take t from 0 to 1 in fSize steps
    
    //the number of frequencies are equal to the number of coordinates to maximize the approximation
    int initialF = -function.size()/2; //frequency in initial line
    int finalF = function.size()/2; //frequency in final line 
    
    for (int f = initialF; f <= finalF; f++) //for all lines with frequencies
    {
      int c = 0; // to get item on function array 
      double cfx = 0;  //coefficient of x 
      double cfy = 0; //coefficient of y
      
      for (double t = 0; t <= 1; t+= deltaT) //implementing for every value in function
      {        
        //getting (x,y) values from function  
        double x = function.get(c)[0]; 
        double y = function.get(c)[1];
        
        //getting the coefficients for x and y from this equation
        cfx += (x*Math.cos(2*Math.PI*f*t) + y*Math.sin(2*Math.PI*f*t))*deltaT;
        cfy -= (x*Math.sin(2*Math.PI*f*t) - y*Math.cos(2*Math.PI*f*t))*deltaT;
        c++;  //updating c              
      }
      //adding the coefficients to array
      Double [] cfxy = {cfx, cfy};
      cf.add(cfxy);
    }
    
    //setting the scene of the window
    Scene scene = new Scene(group, WIDTH, HEIGHT);
    scene.setFill(Color.WHITE);
    primaryStage.setScene(scene);      
    
    //animation timer starts here
    AnimationTimer timer = new AnimationTimer()
    {
      @Override
      public void handle(long now)
      {
        //clearing the window so the old spinning lines clear and the new spinning lines appear
        group.getChildren().clear();
        drawFunction(); //calling drawFunction() - drawing the desired function to compare to the approximated function
        
        //setting the lines to start spinning in the origin (in the middle of the window)
        double x = WIDTH/2;
        double y = HEIGHT/2;
        double previousX = WIDTH/2;
        double previousY = HEIGHT/2;
        
        for (int i = 0; i < cf.size(); i++) //for all values in the array of coefficients
        {        
          int f = i + initialF; //frequency 
          
          //getting the coefficient values from the array
          double cfx = cf.get(i)[0];
          double cfy = cf.get(i)[1];
          
          //getting the (x,y) coordinates of the approximated function with this equation
          x += cfx * Math.cos(2*Math.PI*f*time) - cfy * Math.sin(2*Math.PI*f*time);
          y += cfx * Math.sin(2*Math.PI*f*time) + cfy * Math.cos(2*Math.PI*f*time);
          
          if (f == finalF) //if it's the last line
          { 
            //the coordinates of the ending point of the last line will trace the function
            Double [] xy = {x, y}; 
            approxCor.add(xy); //adding the (x,y) coordinates to the array of approximated function
          }
          
          /* making spinning line
           * setting the starting x and y coordinates with values of the previous line's ending point
           * if it's the first line, the starting coordinates will be the center of the window
           * setting the ending coordinates as (x,y)
           * the y value is subtracted from the height to reflect the function
           * since the y on the window increases as it goes down
           */
          Line spinningLine = new Line(previousX, HEIGHT-previousY, x, HEIGHT-y); 
          
          //after making the line, the previous x and y values become the x and y values
          previousX = x; //set after line is drawn so the first time the line is drawn, it starts at (0,0)
          previousY = y;
          
          group.getChildren().add(spinningLine); //adding the line 
        }
        
        //drawing the approximated function
        for (int i = 0; i < approxCor.size(); i++) //for all the coordinates of the approximated function
        {
          Line approx = new Line(); //this line will connect the approximated coordinates
          
          //the y value is subtracted from the height to reflect the function
          
          //start point of line is index i from the the approximated coordinates array
          approx.setStartX(approxCor.get(i)[0]);
          approx.setStartY(HEIGHT-approxCor.get(i)[1]);
          
          //setting the end point equal to the start point if it is the first line, since there is no point before
          if (i==0)
          {
            approx.setEndX(approxCor.get(i)[0]);
            approx.setEndY(HEIGHT-approxCor.get(i)[1]);
          }
          else //setting the end point equal to the point before 
          {
            approx.setEndX(approxCor.get(i-1)[0]);
            approx.setEndY(HEIGHT-approxCor.get(i-1)[1]);
          }
          //setting the approximated function blue to easily compare it to the desired function
          approx.setStroke(Color.BLUE); 
          group.getChildren().add(approx); //adding line
        }
        
        time += 1.0/WIDTH; //increasing time 
      }
    };
    timer.start(); //timer starts
    primaryStage.show();    //show primary stage 
  }
}