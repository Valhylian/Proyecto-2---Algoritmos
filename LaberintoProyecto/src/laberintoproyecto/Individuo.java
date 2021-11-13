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
    double puntosFitness;
    int sumaPuntos;
    int generacion;
    String descripcion;
    
    public Individuo (int x, int y, String descp, int generacion){
        this.x = x;
        this.y = y;
        this.descripcion = descp;
        this.generacion = generacion;
    }
    
    //ASIGNA PUNTOS POR EL COLOR DONDE CAEN
    public void asignarPuntosColor (int color, int generacion){
        //0=blanco
        if (color == 0){
            this.puntosColor = 5;
        }
        //1 = negro
        else if (color == 1){
            this.puntosColor = 0;
        }
        //2 = rojo o azul 
        else if (color == 2 ){
            this.puntosColor = 3;
        }

        
    }
    
    public void actSumaPuntos(){
        this.sumaPuntos = this.puntosColor+this.puntosCercanos+this.puntosUbicacion;
    }
    //CALCULA LOS PUNTOS DE FITNESS DE UN INDIVIDUO
    public static void actualizarFitness(int generation){
        ArrayList <Individuo> gen = LaberintoProyecto.generaciones.get(generation);
        
        int totalIndividuos = gen.size();
        int puntosGeneracion = LaberintoProyecto.sumaPuntosGeneracion(generation);
        
        for (int i=0; i<gen.size(); i++){
            
            Individuo ind = gen.get(i);
            ind.sumaPuntos = ind.puntosColor+ind.puntosCercanos+ind.puntosUbicacion;
            ind.puntosFitness = ((double)ind.sumaPuntos) / ((double)puntosGeneracion);
            
           
        }   
    }
    
    //GENERA PRIMERA GENERACION DE INDIVIDUOS
    public static ArrayList<Individuo> generarPrimeraPoblacion (int cant, int largeX, int largeY){
        ArrayList<Individuo> poblacion = new ArrayList<Individuo>();
       
        //primerIndividuo
        int x = (int)(Math.random()*largeX);
        int y = (int)(Math.random()*largeY);
            
        Individuo ind = new Individuo (x,y,"Generado aleatoriamente en la primera generacion",0);
        int color = ProcesamientoImagenes.getPixel(x,y,"Laberinto");
        ind.asignarPuntosColor(color,0);
        ind.actSumaPuntos();
        poblacion.add(ind);
        
        ProcesamientoImagenes.setPixel(x,y,1,"laberinto","PrimeraGeneracion");
            
        //resto de individuos
        for (int i=1; i<cant; i++){
            x = (int)(Math.random()*largeX);
            y = (int)(Math.random()*largeY);
            
            ind = new Individuo (x,y,"Generado aleatoriamente en la primera generacion",0);
            color = ProcesamientoImagenes.getPixel(x,y,"Laberinto");
            ind.asignarPuntosColor(color,0);
            ind.actSumaPuntos();
            poblacion.add(ind);
            
            ProcesamientoImagenes.setPixel(x,y,1,"PrimeraGeneracion","PrimeraGeneracion");
            
        }
        
        
        return poblacion;
    }
    
    public String imprimirIndividuo(){
        String info = "";
        info += this.descripcion+"\n";
        info += "x: "+this.x+"\n";
        info += "y: "+this.y+"\n";
        info += "suma: "+this.sumaPuntos+"\n";
        info += "Puntos: "+this.puntosFitness+"\n";
        return info;
    }
}
