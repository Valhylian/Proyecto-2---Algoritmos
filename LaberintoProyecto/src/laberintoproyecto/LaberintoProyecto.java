package laberintoproyecto;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class LaberintoProyecto {
    //arreglo que almacena las generaciones
    public static ArrayList <ArrayList <Individuo> > generaciones= new ArrayList <ArrayList <Individuo> > ();
   
   //imagen inicial 
    public static BufferedImage imgenReferencia = null;
    public static int width   = 0;
    public static int height  = 0;
    
    //SELECCION--------------------------------------------------------------------
    public static ArrayList <Individuo> seleccion(int generacion){
        //generacion de individuos
        ArrayList <Individuo> datos = generaciones.get(generacion);
        //probabilidades de cada indivuo (fitness)
        ArrayList <Double> probabilidad = new ArrayList <Double> ();
        for (int i=0; i<datos.size(); i++){
            Individuo ind = datos.get(i);
            probabilidad.add(ind.puntosFitness);
        }
        //probabilidades acumuladas
        ArrayList <Double> acumuladas = new ArrayList <Double> ();
        for (int j=1; j<probabilidad.size(); j++){
            if (j==1){
                double acum = probabilidad.get(j)+probabilidad.get(j-1);
                acumuladas.add(acum);
            }
            else{
                double acum = probabilidad.get(j)+acumuladas.get(j-2);
                acumuladas.add(acum);
            }
        }
        acumuladas.set(acumuladas.size()-1, 1.0);
        //Retorna arreglo de los individuos seleccionados
        return seleccionador(acumuladas,datos);
    }
    
    //SELECCIONADOR DE INDIVIDUOS-----------------------------------------------
    public static ArrayList <Individuo> seleccionador (ArrayList <Double> probabilidades, ArrayList <Individuo> individuos){
        int cantGenerar = individuos.size();
        ArrayList <Individuo> seleccionados = new ArrayList <Individuo> ();
        
        while (cantGenerar > 0){
            Double number = Math.random();//GENERA RANDOM ENTRE 0-1
            //Se selecciona el individuo 
            Individuo seleccionado = individuos.get(retornaIndividuoPosicion (number, probabilidades));
            if (!seleccionados.contains(seleccionado)){
                //si no se ha seleccionado antes se agrega
                seleccionados.add(seleccionado);
            }
            cantGenerar --;
        }
        //retorna array de los individuos seleccionados
        return seleccionados;
    }
    
    //RETORNA POSICION DE INDIVIDUO---------------------------------------------------
    //recibe la probabilidad aleatoria y recorre el arreglo hasta encontrar el individuo
    public static int retornaIndividuoPosicion (Double proba, ArrayList <Double> probabilidades){
        int posicion = 0;
        for (int i=0; i<probabilidades.size();i++){
            if (probabilidades.get(i) > proba){
                posicion = i;
                return posicion;
            }
        }
        return posicion;
    }
    
    //IMPRIME TODAS LAS POBLACIONES----------------------------------------------
    public static String imprimirPoblaciones(){
        String info = "";
        for (int i=0; i<generaciones.size(); i++){
            info += "Generacion: "+i+"\n";
            for (int j=0; j<generaciones.get(i).size(); j++){
                info += "------------------------\n";
                info += "Individuo "+j+"\n";
                Individuo indv = generaciones.get(i).get(j);
                info += indv.imprimirIndividuo();
            }
        }
        return info;
    }
    
    //RETORNA CANT DE PUNTOS DE X GENERACION-------------------------------------
    public static int sumaPuntosGeneracion(int generacion){
        int puntos = 0;
        ArrayList <Individuo>  gen = generaciones.get(generacion);
        for (int i=0; i<gen.size(); i++){
            Individuo ind = gen.get(i);
            puntos += ind.sumaPuntos;
        }
        return puntos;
    }
    

    public static void main(String args[])throws IOException
    {
        Path root = Paths.get(".").normalize().toAbsolutePath(); 
        String ruta = root.toString()+"\\src\\imagenes\\laberinto.png";
        File archivoImagen = new File(ruta);
        imgenReferencia = ImageIO.read(archivoImagen); //cargo imagen de laberinto inicial
        width   = imgenReferencia.getWidth();
        height  = imgenReferencia.getHeight();
       
        generaciones.add(Individuo.generarPrimeraPoblacion(20, 100, 100));
        Individuo.actPuntosCercanosGeneracion(0);
        Individuo.actualizarFitness(0);
        System.out.println(imprimirPoblaciones());
        System.out.println(sumaPuntosGeneracion(0));
        
        seleccion(0);
        ArrayList <Individuo> selected = seleccion (0);
        System.out.println("cantSelect " + selected.size());
        
    }

    
}
