/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package laberintoproyecto;
import java.io.File;        //Para leer y escribir un archivo de imagen
import java.io.IOException; //Para manejar errores
import java.awt.image.BufferedImage;  //objeto BufferedImage para almacenar una imagen en RAM.  
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;   //tiene métodos estáticos para leer y escribir una imagen.

/**
 *
 * @author valeria
 */
public class ProcesamientoImagenes {
    public static void getPixel (int x, int y){
        BufferedImage img = null;
        File f = null;
        
        //read image
        try
        {
            Path root = Paths.get(".").normalize().toAbsolutePath();
            String ruta = root.toString(); 
            ruta = ruta + "\\src\\imagenes\\laberinto.jpg";
           
            f = new File(ruta);
            img = ImageIO.read(f);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        /* get pixel value (the arguments in the getRGB method
         denotes the  coordinates of the image from which the
         pixel values need to be extracted) */
        int p = img.getRGB(x,y);
        
        // get alpha
        int a = (p>>24) & 0xff;
 
        // get red
        int r = (p>>16) & 0xff;
 
        // get green
        int g = (p>>8) & 0xff;
 
        // get blue
        int b = p & 0xff;
        
        System.out.println("alpha "+a);
        System.out.println("red "+r);
        System.out.println("green "+g);
        System.out.println("blue "+b);
    }
    
    public static void setPixel(){
        int a = 255;
        int r = 100;
        int g = 150;
        int b = 200;
        //read image
        BufferedImage img = null;
        File f = null;
        try
        {
            Path root = Paths.get(".").normalize().toAbsolutePath();
            String ruta = root.toString(); 
            ruta = ruta + "\\src\\imagenes\\laberinto.jpg";
           
            f = new File(ruta);
            img = ImageIO.read(f);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        //set the pixel value
        int p = img.getRGB(0,0);
        p = (a<<24) | (r<<16) | (g<<8) | b;
        img.setRGB(0, 0, p);
 
        //write image
        try
        {
            Path root = Paths.get(".").normalize().toAbsolutePath();
            String ruta = root.toString(); 
            ruta = ruta + "\\src\\imagenes\\laberinto.jpg";
            f = new File(ruta);
            ImageIO.write(img, "jpg", f);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
    public static void readImage(String name){
        // READ IMAGE--------------------------------------------------
        int width = 50;    //width of the image
        int height = 50;   //height of the image
  
        // For storing image in RAM
        BufferedImage image = null;
        
        try
        {   
            Path root = Paths.get(".").normalize().toAbsolutePath();
            String ruta = root.toString(); 
            ruta = ruta + "\\src\\imagenes";
            File input_file = new File(ruta+"\\"+name); //image file path
  
            /* create an object of BufferedImage type and pass
               as parameter the width,  height and image int
               type.TYPE_INT_ARGB means that we are representing
               the Alpha, Red, Green and Blue component of the
               image pixel using 8 bit integer value. */
            image = new BufferedImage(width, height,
                                    BufferedImage.TYPE_INT_ARGB);
  
             // Reading input file
            image = ImageIO.read(input_file);
  
            System.out.println("Reading complete.");
        }
        catch(IOException e)
        {
            System.out.println("Error: "+e);
        }
    }
    
    public static void writeImage(){
        /*
        try
        {
            // Output file path
            File output_file = new File("G:\\Out.jpg");
  
            // Writing to file taking type and path as
            ImageIO.write(image, "jpg", output_file);
  
            System.out.println("Writing complete.");
        }
        catch(IOException e)
        {
            System.out.println("Error: "+e);
        }*/
    }
    
    
}
