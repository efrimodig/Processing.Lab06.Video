
import gab.opencv.*;
import processing.core.*;
import processing.video.*;
import java.awt.*;
import java.util.*;

/**
 * Video is a subclass of Processing.
 *     -Shapes and text are drawn on the screen.
 *     -Mouse and keyboard input are read in.
 */
public class Video extends PApplet
{
    public OpenCV opencv;
    public Capture video;
    private final String DESIRED_CAMERA = "640x480";

    /**
     * Launch the Processing Application, Calls settings() once, then setup()
     * once, then draw() 30 times per second.
     */
    public static void main(String args[]) {
        String packageFilename = "Video";
        PApplet.main(new String[] { packageFilename });
    }

    /**
     * Sets the Application Properties.
     */
    public void settings() {
        size(640, 480);
    }

    /**
     * Sets the Application Properties. Loads the first camera in the available list.
     */
    public void setup() {
        opencv = new OpenCV(this, 640, 480);
        // TODO 3: Change to CASCADE_EYE
        opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);

        String cam = "";
        String[] cams = Capture.list();
        if (cams == null || cams.length == 0) {
            System.out.println("There are no cameras available for capture.");
            exit();
        } else {
            System.out.println("Available cameras for capture:");
            cam = cams[0];
            for (String camName : cams) {
                System.out.println("    " + camName);
                if(camName.contains(DESIRED_CAMERA))
                {
                    cam = camName;
                }
            }
        }
        try {
            System.out.println("Starting camera: " + cam);
            video = new Capture(this, cam);
            video.start();
        } catch (Exception e) {
            System.out.println("Exception: " + e + " starting camera.");
            e.printStackTrace();
            exit();
        }
    }

    /**
     * Called repeatedly (once per frame)
     */
    public void draw() {
        if (video.available()) {
            // If so, read the image from the camera.
            video.read();
        }
        scale(1.0f);
        image(video, 0, 0);

        // Sometimes the camera takes a few moments to startup, during which time the video is 0.
        if (video.height > 0 && video.width > 0) {
            opencv.loadImage(video);
            
            // TODO 4: Uncomment the block to see other video manipulations
            /*
            // Draw a 1/2 size video of the Canny Edges
            scale(0.5f);
            opencv.loadImage(video);
            opencv.findCannyEdges(20, 75);
            image(opencv.getSnapshot(), 0, 0);
            // Draw a 1/2 size video of the Scharr Edges
            opencv.loadImage(video);
            opencv.findScharrEdges(OpenCV.HORIZONTAL);
            image(opencv.getSnapshot(), width, 0);
            */
        }

        noFill();
        stroke(0, 255, 0);
        strokeWeight(3);

        Rectangle[] faces = opencv.detect();
        for (int i = 0; i < faces.length; i++) { 
            // TODO 1: Instead of drawing a rectangle, load a PImage of antlers.png position it over the face.
            rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height); 
        }

        // TODO 2: Comment out the face rectangle, and uncomment the contours below.
        // Instead of drawing a stroke around the contours, fill each shape with a different color.
        // Try random colors as well as changing the shade (i.e. add 10 to the blue value each iteration) for each contour.
        /*
        opencv.threshold(100); 
        ArrayList<Contour> contours = opencv.findContours();
        for (Contour contour : contours) { 
            stroke(0, 255, 0);
            contour.draw();
            
            stroke(255, 0, 0);
            beginShape(); 
            for (PVector point : contour.getPolygonApproximation().getPoints()) { 
                vertex(point.x,point.y); 
            } 
            endShape(); 
        }
        */
    }

    /**
     * Called once at the end. Closes the camera.
     */
    public void stop() {
        video.stop();
        super.stop();
    }
}