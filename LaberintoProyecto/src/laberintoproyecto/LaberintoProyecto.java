package laberintoproyecto;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;

public class LaberintoProyecto {
    //arreglo que almacena las generaciones
    public static ArrayList <ArrayList <Individuo> > generaciones= new ArrayList <ArrayList <Individuo> > ();
    //almacena los seleccionadosxGeneracion
   public static ArrayList <ArrayList <Individuo> > selected= new ArrayList <ArrayList <Individuo> > ();
   //imagen inicial 
    public static BufferedImage imgenReferencia = null;
    public static int width   = 0;
    public static int height  = 0;
    
    //SELECCION--------------------------------------------------------------------
    
    public static ArrayList <Individuo>  seleccionarMejores(ArrayList <Individuo> individuos){
        ArrayList <Individuo> seleccionados = new ArrayList <Individuo> ();
        Collections.sort(individuos);

        int total = individuos.size() / 2;
        for (int i=0; i<total; i++){
            seleccionados.add(individuos.get(i));
        }
        return seleccionados;
    }
    public static ArrayList <Individuo> seleccion(int generacion, int limite){
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
        return seleccionador(acumuladas,datos,limite);
    }
    
    //SELECCIONADOR DE INDIVIDUOS-----------------------------------------------
    public static ArrayList <Individuo> seleccionador (ArrayList <Double> probabilidades, ArrayList <Individuo> individuos, int limite){
        int cantGenerar = individuos.size();
        if (cantGenerar > limite){
            cantGenerar = limite;
        }
        ArrayList <Individuo> seleccionados =seleccionarMejores(individuos);
        
        while (cantGenerar > 0 ){
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
    
    //CRUCE--------------------------------------------------------------------------
    public static ArrayList <Individuo>  Cruce (int generation){
        ArrayList <Individuo> seleccionados = selected.get(generation);
        ArrayList<Individuo> poblacion = new ArrayList<Individuo>();
        
        for (int i=0; i<seleccionados.size();i++){
            int n1 = (int)(Math.random()*seleccionados.size());
            int n2 = (int)(Math.random()*seleccionados.size());
            while (n1 == n2){
                n2 = (int)(Math.random()*seleccionados.size());
            }
            Individuo primero = seleccionados.get(n1);
            Individuo segundo = seleccionados.get(n2);
            
            
            //nuevo1
            Individuo nuevo1 = new Individuo(primero.x, segundo.y, "Cruce de seleccionados Generacion: "+generation,1);
            if (!poblacion.contains(nuevo1)){
                //si no se ha seleccionado antes se agrega
                nuevo1.asignarPuntosColor(ProcesamientoImagenes.getPixel(primero.x,segundo.y,"Laberinto"));
                nuevo1.calcularPuntosUbicacion ();
                nuevo1.actSumaPuntos(); //actualiza la suma total
                poblacion.add(nuevo1);  //se agrega a la poblacion
            }
            
            
            if(i == 0){
                ProcesamientoImagenes.setPixel(primero.x,segundo.y,2,"laberinto","Cruce generacion "+generation);
            }
            else{
                ProcesamientoImagenes.setPixel(primero.x,segundo.y,2,"Cruce generacion "+generation,"Cruce generacion "+generation);
            }
            
            Individuo nuevo2 = new Individuo(segundo.x, primero.y, "Cruce de seleccionados Generacion: "+generation,1);
            nuevo2.asignarPuntosColor(ProcesamientoImagenes.getPixel(segundo.x,primero.y,"Laberinto"));
            nuevo2.calcularPuntosUbicacion ();
            nuevo2.actSumaPuntos(); //actualiza la suma total
            poblacion.add(nuevo2);  //se agrega a la poblacion
            ProcesamientoImagenes.setPixel(segundo.x,primero.y,2,"Cruce generacion "+generation,"Cruce generacion "+generation);
            
            Individuo old1 = new Individuo(primero.x, primero.y, "Seleccionado generacion: "+generation,1);
            old1.asignarPuntosColor(ProcesamientoImagenes.getPixel(primero.x,primero.y,"Laberinto"));
            old1.calcularPuntosUbicacion ();
            old1.actSumaPuntos(); //actualiza la suma total
            poblacion.add(old1);  //se agrega a la poblacion
            ProcesamientoImagenes.setPixel(primero.x,primero.y,1,"Cruce generacion "+generation,"Cruce generacion "+generation);
            
            Individuo old2 = new Individuo(segundo.x, segundo.y, "Seleccionado primera generacion: "+generation,1);
            old2.asignarPuntosColor(ProcesamientoImagenes.getPixel(segundo.x,segundo.y,"Laberinto"));
            old2.calcularPuntosUbicacion ();
            old2.actSumaPuntos(); //actualiza la suma total
            poblacion.add(old2);  //se agrega a la poblacion
            ProcesamientoImagenes.setPixel(segundo.x,segundo.y,1,"Cruce generacion "+generation,"Cruce generacion "+generation);
        }
        return poblacion;
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
            info += "------------------------\n";
            info += "------------------------\n";
            info += "Generacion: "+i+"\n";
            info += "------------------------\n";
            info += "------------------------\n";
            for (int j=0; j<generaciones.get(i).size(); j++){
                info += "------------------------\n";
                info += "Individuo "+j+"\n";
                Individuo indv = generaciones.get(i).get(j);
                info += indv.imprimirIndividuo();
            }
        }
        return info;
    }
    
    
    
   //IMPRIME SELECCIONADOS----------------------------------------------
    public static String imprimirSelecionados(){
        String info = "";
        for (int i=0; i<selected.size(); i++){
            info += "------------------------\n";
            info += "------------------------\n";
            info += "selected: "+i+"\n";
            info += "------------------------\n";
            info += "------------------------\n";
            for (int j=0; j<selected.get(i).size(); j++){
                info += "------------------------\n";
                info += "Individuo "+j+"\n";
                Individuo indv = selected.get(i).get(j);
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
    
    public static void generarImagenSeleccionados(int generation, String name){
        ArrayList <Individuo> seleccionados = selected.get(generation);
        for (int i=0; i<seleccionados.size(); i++){
            Individuo indv = seleccionados.get(i);
            //agrega pixel a la imagen
            if (i==0){
                ProcesamientoImagenes.setPixel(indv.x,indv.y,1,"laberinto",name);
            }
            else{
                ProcesamientoImagenes.setPixel(indv.x,indv.y,1,name,name);
            }
        }
    }
    
    //ORDEN DE LOS MEJORORES X GENERACION
    
   
    
    public static void generarImagenGeneracion(int generation, String name){
        ArrayList <Individuo> generacion = generaciones.get(generation);
        System.out.println("Sizeeeee: "+ generacion.size());
        for (int i=0; i<generacion.size(); i++){
            Individuo indv = generacion.get(i);
            //agrega pixel a la imagen
            if (i==0){
                ProcesamientoImagenes.setPixel(indv.x,indv.y,1,"laberinto",name);
            }
            else{
                ProcesamientoImagenes.setPixel(indv.x,indv.y,1,name,name);
            }
        }
    }
    //MUTACION
    //indice de mutacion = 0.05 = 5%
    public static void mutacion (ArrayList <Individuo> arreglo){
        
        
    }
    

    public static void main(String args[])throws IOException
    {
        Path root = Paths.get(".").normalize().toAbsolutePath(); 
        String ruta = root.toString()+"\\src\\imagenes\\laberinto.png";
        File archivoImagen = new File(ruta);
        imgenReferencia = ImageIO.read(archivoImagen); //cargo imagen de laberinto inicial
        width   = imgenReferencia.getWidth();
        height  = imgenReferencia.getHeight();
       
        //1-GENERA LA PRIMERA GENERACION--------------------------------------------
        //1.1 genera individuos aleatorios y asigna puntosxUbicacion
        generaciones.add(Individuo.generarPrimeraPoblacion(100, 100, 100));
        //1.3 puntos x indivuos cercanos
        Individuo.actPuntosCercanosGeneracion(0);
        //1.4 actualizar fitness
        Individuo.actualizarFitness(0);
        //SELECCION
        selected.add(seleccion (0,1000));
        generarImagenSeleccionados(0, "SeleccionadosPrimeraGeneracion");
        System.out.println("cantSelect " + selected.get(0).size());
        //CRUCE
        generaciones.add(Cruce (0));
        System.out.println("cantCruce " + generaciones.get(1).size()); //aqui se genera la imagen
        //1.3 puntos x indivuos cercanos
        Individuo.actPuntosCercanosGeneracion(1);
        //1.4 actualizar fitness
        Individuo.actualizarFitness(1);
        
        //CICLO GENERACIONES 
        for (int i=1; i<6; i++){
            String name = "Generacion"+i;
            //SELECCION
            selected.add(seleccion (i,200));
            generarImagenSeleccionados(i, name);
            //CRUCE
            generaciones.add(Cruce (i));
            System.out.println("cantCruce " + generaciones.get(i+1).size()); //aqui se genera la imagen
            //1.3 puntos x indivuos cercanos
            Individuo.actPuntosCercanosGeneracion(i+1);
            //1.4 actualizar fitness
            Individuo.actualizarFitness(i+1);
        }
        /*
        //SEGUNDA GENERACION----------------------------------------------------
        //SELECCION
        selected.add(seleccion (1,200));
        generarImagenSeleccionados(1, "SegundaGeneracion");
        //CRUCE
        generaciones.add(Cruce (1));
        System.out.println("cantCruce " + generaciones.get(2).size()); //aqui se genera la imagen
        //1.3 puntos x indivuos cercanos
        Individuo.actPuntosCercanosGeneracion(2);
        //1.4 actualizar fitness
        Individuo.actualizarFitness(2);
        
        //TERCERA GENERACION----------------------------------------------------
        //SELECCION
        selected.add(seleccion (2,200));
        generarImagenSeleccionados(2, "TerceraGeneracion");
        //CRUCE
        generaciones.add(Cruce (2));
        System.out.println("cantCruce " + generaciones.get(3).size()); //aqui se genera la imagen
        //1.3 puntos x indivuos cercanos
        Individuo.actPuntosCercanosGeneracion(3);
        //1.4 actualizar fitness
        Individuo.actualizarFitness(3);
        
        //CUARTA GENERACION----------------------------------------------------
        //SELECCION
        selected.add(seleccion (3,200));
        generarImagenSeleccionados(3, "CuartaGeneracion");
        //CRUCE
        generaciones.add(Cruce (3));
        System.out.println("cantCruce " + generaciones.get(4).size()); //aqui se genera la imagen
        //1.3 puntos x indivuos cercanos
        Individuo.actPuntosCercanosGeneracion(4);
        //1.4 actualizar fitness
        Individuo.actualizarFitness(4);
        
        //QUINTA GENERACION----------------------------------------------------
        //SELECCION
        selected.add(seleccion (4,200));
        generarImagenSeleccionados(4, "QuintaGeneracion");
        //CRUCE
        generaciones.add(Cruce (4));
        System.out.println("cantCruce " + generaciones.get(4).size()); //aqui se genera la imagen
        //1.3 puntos x indivuos cercanos
        Individuo.actPuntosCercanosGeneracion(5);
        //1.4 actualizar fitness
        Individuo.actualizarFitness(5);
        
        //SEXTA GENERACION----------------------------------------------------
        //SELECCION
        selected.add(seleccion (5,200));
        generarImagenSeleccionados(5, "SextaGeneracion");*/
    }
    
    

    
}
