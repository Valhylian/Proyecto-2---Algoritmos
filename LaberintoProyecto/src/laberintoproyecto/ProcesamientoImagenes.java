package laberintoproyecto;
import java.io.File;        //Para leer y escribir un archivo de imagen
import java.io.IOException; //Para manejar errores
import java.awt.image.BufferedImage;  //objeto BufferedImage para almacenar una imagen en RAM.  
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;   //tiene métodos estáticos para leer y escribir una imagen.


public class ProcesamientoImagenes {
    
    public static int getPixel (int x, int y, String imageName){
        int color = 0;
        BufferedImage img = null;
        File f = null;
        
        //read image
        try
        {
            Path root = Paths.get(".").normalize().toAbsolutePath();
            String ruta = root.toString(); 
            ruta = ruta + "\\src\\imagenes\\"+imageName+".png";
           
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
        if (r==0 && g==0 && b==0){
            color = 1;
        }
       
        else if (r==255 && g==255 && b==255){
            color = 0;
        }
        
        else{
            color = 2;
        }
        
        return color;
    }
    
    public static void setPixel(int x, int y, int color, String imageName, String generacion){
        int a = 255;
        int r = 255;
        int g = 255;
        int b = 0;

        //read image
        BufferedImage img = null;
        File f = null;
        try
        {
            Path root = Paths.get(".").normalize().toAbsolutePath();
            String ruta = root.toString(); 
            ruta = ruta + "\\src\\imagenes\\"+imageName+".png";
           
            f = new File(ruta);
            img = ImageIO.read(f);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        //set the pixel value
        int p = img.getRGB(x,y);
        p = (a<<24) | (r<<16) | (g<<8) | b;
    
        img.setRGB(x, y, p);
        System.out.println("entra "+x+" "+y);
        //write image
        try
        {
            Path root = Paths.get(".").normalize().toAbsolutePath();
            String ruta = root.toString(); 
            ruta = ruta + "\\src\\imagenes\\"+generacion+".png";
          
            f = new File(ruta);
            ImageIO.write(img, "png", f);
            
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
