package laberintoproyecto;
import java.io.IOException;
import java.util.ArrayList;
import laberintoproyecto.Individuo;
import static laberintoproyecto.Individuo.actualizarFitness;
import laberintoproyecto.ProcesamientoImagenes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author valeria
 */
public class LaberintoProyecto {

    /**
     * 
     */
    //arreglo que almacena las generaciones
    public static ArrayList <ArrayList <Individuo> > generaciones= new ArrayList <ArrayList <Individuo> > ();
    
    //SELECCION--------------------------------------------------------------------
    public static ArrayList <Individuo> seleccion(int generacion){
        ArrayList <Individuo> datos = generaciones.get(generacion);
        ArrayList <Double> probabilidad = new ArrayList <Double> ();
        for (int i=0; i<datos.size(); i++){
            Individuo ind = datos.get(i);
            probabilidad.add(ind.puntosFitness);
        }
        
        ArrayList <Double> acumuladas = new ArrayList <Double> ();
        //generar probabilidades acumuladas
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
        return seleccionador(acumuladas,datos);
        
        
    }
    
    //seleccionar x cantidad de ind 
    public static ArrayList <Individuo> seleccionador (ArrayList <Double> probabilidades, ArrayList <Individuo> individuos){
        int cantGenerar = individuos.size();
        ArrayList <Individuo> seleccionados = new ArrayList <Individuo> ();
        
        while (cantGenerar > 0){
            Double number = Math.random();
            Individuo seleccionado = individuos.get(retornaIndividuoPosicion (number, probabilidades));
            if (!seleccionados.contains(seleccionado)){
                seleccionados.add(seleccionado);
            }
            cantGenerar --;
        }
        return seleccionados;
    }
    
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
     
        generaciones.add(Individuo.generarPrimeraPoblacion(200, 100, 100));
        Individuo.actualizarFitness(0);
        System.out.println(imprimirPoblaciones());
        System.out.println(sumaPuntosGeneracion(0));
        seleccion(0);
        
        ArrayList <Individuo> selected = seleccion (0);
        System.out.println("cantSelect " + selected.size());
        
    }

    
}
