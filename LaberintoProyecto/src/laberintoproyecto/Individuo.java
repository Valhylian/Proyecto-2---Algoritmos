/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laberintoproyecto;

import java.util.ArrayList;

/**
 *
 * @author valeria
 */
public class Individuo {
    int x;
    int y;
    int puntosColor;
    int puntosCercanos;
    int puntosUbicacion;
    int puntosFitness;
    
    public Individuo (int x, int y){
        this.x = x;
        this.y = y;
    }
    public void asignarPuntos(){
        
    }
    
    public static ArrayList<Individuo> generarPoblacion (int cant, int largeX, int largeY, int color){
        ArrayList<Individuo> poblacion = new ArrayList<Individuo>();
        for (int i=0; i<cant; i++){
            int x = (int)(Math.random()*largeX);
            int y = (int)(Math.random()*largeY);
            
            Individuo ind = new Individuo (x,y);
            ind.asignarPuntos();
            poblacion.add(ind);
            
            ProcesamientoImagenes.setPixel(x,y,1);
            
        }
        return poblacion;
    }
    
}
