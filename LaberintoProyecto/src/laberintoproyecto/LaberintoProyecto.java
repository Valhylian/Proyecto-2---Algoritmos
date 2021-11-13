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
     * @param args the command line arguments
     */
    //arreglo que almacena las generaciones
    public static ArrayList <ArrayList <Individuo> > generaciones= new ArrayList <ArrayList <Individuo> > ();
    
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
     
        generaciones.add(Individuo.generarPrimeraPoblacion(50, 100, 100));
        Individuo.actualizarFitness(0);
        System.out.println(imprimirPoblaciones());
        System.out.println(sumaPuntosGeneracion(0));
        
    }
    
}
