package laberintoproyecto;

import java.util.ArrayList;

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
        this.sumaPuntos = 1;
    }
    
    //ASIGNA PUNTOS POR EL COLOR DONDE CAEN-------------------------------------
    public void asignarPuntosColor (int color, int generacion){
        //0=blanco
        if (color == 0){
            this.puntosColor = 5;
        }
        //1 = negro
        else if (color == 1){
            this.puntosColor = 0;
        }
        //2 = rojo o azul (inicio - final)
        else if (color == 2 ){
            this.puntosColor = 3;
        }
    }
    
    public void puntosUbicacion (){
        int ancho = LaberintoProyecto.width;
        int largo = LaberintoProyecto.height;
        
        //arriba----------------------------------------------
        for (int i=1; i<6; i++){
            if (y-i > 0){
                int color = ProcesamientoImagenes.getPixel (x, y-i, "laberinto");
                //blanco
                if (color == 0){
                    this.puntosUbicacion += 2;
                    break;
                }
                if (color == 2){
                    this.puntosUbicacion += 3;
                    break;
                }
            }
        }
        //abajo---------------------------------------------
        for (int i=1; i<6; i++){
            if (y+i < largo){
                int color = ProcesamientoImagenes.getPixel (x, y+i, "laberinto");
                //blanco
                if (color == 0){
                    this.puntosUbicacion += 2;
                    break;
                }
                if (color == 2){
                    this.puntosUbicacion += 3;
                    break;
                }
            }
        }
        
        //izquierda---------------------------------------------
        for (int i=1; i<6; i++){
            if (x-i > 0){
                int color = ProcesamientoImagenes.getPixel (x-i, y, "laberinto");
                //blanco
                if (color == 0){
                    this.puntosUbicacion += 2;
                    break;
                }
                if (color == 2){
                    this.puntosUbicacion += 3;
                    break;
                }
            }
        }
        //derecha---------------------------------------------
        for (int i=1; i<6; i++){
            if (x+i < ancho){
                int color = ProcesamientoImagenes.getPixel (x+i, y, "laberinto");
                //blanco
                if (color == 0){
                    this.puntosUbicacion += 2;
                    break;
                }
                if (color == 2){
                    this.puntosUbicacion += 3;
                    break;
                }
            }
        }
        actSumaPuntos();
    }
    
    //ACTUALIZA LA SUMA DE PUNTOS-----------------------------------------------
    public void actSumaPuntos(){
        this.sumaPuntos = this.puntosColor + this.puntosCercanos + this.puntosUbicacion +1;
    }
    public void asignarPuntosCercanos(){
        int generation = this.generacion;
        ArrayList <Individuo> gen = LaberintoProyecto.generaciones.get(generation);
        for (int i=0; i<gen.size(); i++){
            Individuo ind = gen.get(i);
            double dist = Math.hypot(this.x- ind.x, this.y - ind.y);
            if ((ind.sumaPuntos >= 10 && dist <= 5) && dist!=0){
                //gana un 10% de los puntos del vecino
                this.puntosCercanos += (int)(ind.sumaPuntos * 0.10);
                System.out.println("this: x: "+this.x + " y: "+this.y);
                System.out.println("entra: "+i);
            }
        }
    }
    
    public static void actPuntosCercanosGeneracion(int generation){
        ArrayList <Individuo> gen = LaberintoProyecto.generaciones.get(generation);
        for (int i=0; i<gen.size(); i++){
            Individuo ind = gen.get(i);
            ind.asignarPuntosCercanos();
        }
    }
  
    
    //CALCULA LOS PUNTOS DE FITNESS DE GENERACION-----------------------------
    public static void actualizarFitness(int generation){
        //lista de individuos de la generacion
        ArrayList <Individuo> gen = LaberintoProyecto.generaciones.get(generation);
        //actualiza la suma de puntos
        for (int j=0; j<gen.size(); j++){
            Individuo ind = gen.get(j);
            ind.actSumaPuntos();
        }  
        //suma de puntos de toda la generacion
        int puntosGeneracion = LaberintoProyecto.sumaPuntosGeneracion(generation);
        //asigna la nota fitness a cada individuo
        for (int i=0; i<gen.size(); i++){
            Individuo ind = gen.get(i);
            ind.puntosFitness = ((double)ind.sumaPuntos) / ((double)puntosGeneracion);
        }   
    }

    //GENERA PRIMERA GENERACION DE INDIVIDUOS------------------------------------
    public static ArrayList<Individuo> generarPrimeraPoblacion (int cant, int largeX, int largeY){
        ArrayList<Individuo> poblacion = new ArrayList<Individuo>();
        
        //PRIMER INDIVIDUOs
        //genera posicion
        int x = (int)(Math.random()*largeX);
        int y = (int)(Math.random()*largeY);
        //crea indivuo
        Individuo ind = new Individuo (x,y,"Generado aleatoriamente en la primera generacion",0);
        //puntos por color
        int color = ProcesamientoImagenes.getPixel(x,y,"Laberinto");
        ind.asignarPuntosColor(color,0);
        //puntos por ubicacion
        ind.puntosUbicacion ();
        //puntos por cercania
        //--FALTAAAA pero no va a servir aqui
        
        ind.actSumaPuntos(); //actualiza la suma total
        poblacion.add(ind);  //se agrega a la poblacion
        
        //agrega pixel a la imagen
        ProcesamientoImagenes.setPixel(x,y,1,"laberinto","PrimeraGeneracion");
            
        //resto de individuos
        for (int i=1; i<cant; i++){
            //genera posicion
            x = (int)(Math.random()*largeX);
            y = (int)(Math.random()*largeY);
            
            ind = new Individuo (x,y,"Generado aleatoriamente en la primera generacion",0);
            //puntos por color
            color = ProcesamientoImagenes.getPixel(x,y,"Laberinto");
            ind.asignarPuntosColor(color,0);
            //puntos por ubicacion
            ind.puntosUbicacion ();
            //puntos por cercania
            //--FALTAAAA--------------------
        
            ind.actSumaPuntos(); //actualiza la suma total
            poblacion.add(ind);  //se agrega a la poblacion
            
            //agrega pixel a la imagen
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

