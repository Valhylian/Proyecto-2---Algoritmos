package laberintoproyecto;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

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
        if (seleccionados.size() > limite){
            return seleccionados;
        }
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
            
            Individuo nuevo1 = new Individuo(primero.x, segundo.y, "Cruce de seleccionados Generacion: "+generation,1);
            nuevo1.asignarPuntosColor(ProcesamientoImagenes.getPixel(primero.x,segundo.y,"Laberinto"));
            nuevo1.calcularPuntosUbicacion ();
            nuevo1.actSumaPuntos(); //actualiza la suma total
            poblacion.add(nuevo1);  //se agrega a la poblacion
            
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
            
            Individuo old2 = new Individuo(segundo.x, segundo.y, "Seleccionado generacion: "+generation,1);
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
    
    public static boolean buscarIndividuo (int x, int y, ArrayList <Individuo> generation){
        for (int i=0; i<generation.size(); i++){
            Individuo indv = generation.get(i);
            if (indv.x == x & indv.y == y){
                return true;
            }
        }
        return false;
    } 
    //GENERAR ARCHIVO GENERACION X
    public static String generarArchivo (int x){
        String info = " ";
        ArrayList <Individuo> generation = generaciones.get(x);
        ArrayList <Individuo> sinRepetidos = new ArrayList <Individuo> ();
        for (int i=0; i<generation.size(); i++){
            Individuo indv = generation.get(i);
            if (!buscarIndividuo (indv.x, indv.y, sinRepetidos)){
                //si no se ha seleccionado antes se agrega
                sinRepetidos.add(indv);
            }
        }
        for (int j=0; j<sinRepetidos.size(); j++){
                info += "------------------------\n";
                info += "Individuo "+j+"\n";
                Individuo indv = sinRepetidos.get(j);
                info += indv.imprimirIndividuo();
            }
        crearArchivo(info, "Generacion"+x);
        return info;
    }
    //RETORNA LA RUTA ABSOLUTA DEL PROYECTO---------------------------------------
    public static String getRuta () {
	Path path = Paths.get("");
	String directoryName = path.toAbsolutePath().toString();
	System.out.println("El directorio de este proyecto: " +directoryName);
        return directoryName;
    }
    public static void crearArchivo(String info, String nombre){
        try {
            Path root = Paths.get(".").normalize().toAbsolutePath();
            String ruta = root.toString(); 
            ruta = ruta + "\\src\\imagenes\\"+nombre+".txt";
            
            String contenido = info;
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenido);
            bw.close();
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //MUTACION
    //indice de mutacion = 0.0 = 2%
    public static void mutacion (ArrayList <Individuo> arreglo){
        //seleccionar x ind aleatorios
        
        //cambiar gen
        
        //regresar a la generacion
        
        //actualizarImagen
        
    }
    
    public static void iniciar (String nombreImagen) throws IOException{
        Path root = Paths.get(".").normalize().toAbsolutePath(); 
        String ruta = root.toString()+"\\src\\imagenes\\"+nombreImagen+".png";
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
        generarArchivo (0);
        
        //CICLO GENERACIONES 
        for (int i=1; i<5; i++){
            String name = "Generacion"+i;
            //SELECCION
            selected.add(seleccion (i,200));
            System.out.println("cantSelect " + selected.get(i).size());
            generarImagenSeleccionados(i, name);
            //CRUCE
            generaciones.add(Cruce (i));
            System.out.println("cantCruce " + generaciones.get(i+1).size()); //aqui se genera la imagen
            //1.3 puntos x indivuos cercanos
            Individuo.actPuntosCercanosGeneracion(i+1);
            //1.4 actualizar fitness
            Individuo.actualizarFitness(i+1);
            generarArchivo (i);
        }
        String st = "LISTOO";
        JOptionPane.showMessageDialog(null, st);
    }
    
    /*
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
        for (int i=1; i<5; i++){
            String name = "Generacion"+i;
            //SELECCION
            selected.add(seleccion (i,200));
            System.out.println("cantSelect " + selected.get(i).size());
            generarImagenSeleccionados(i, name);
            //CRUCE
            generaciones.add(Cruce (i));
            System.out.println("cantCruce " + generaciones.get(i+1).size()); //aqui se genera la imagen
            //1.3 puntos x indivuos cercanos
            Individuo.actPuntosCercanosGeneracion(i+1);
            //1.4 actualizar fitness
            Individuo.actualizarFitness(i+1);
        }
        generarArchivo (0);
        generarArchivo (1);

    }*/
    
    

    
}
