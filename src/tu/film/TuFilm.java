/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tu.film;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;

public class TuFilm {

     public static DefaultTableModel Mostrar(String nomArchivo, String[] VectornumColumnas)throws IOException{
     DefaultTableModel model = new DefaultTableModel();
     
     FileReader fr= new FileReader(nomArchivo);
     BufferedReader br= new BufferedReader(fr);
     String linea;
     linea = br.readLine();
     int cont=0;
     while(linea != null){
         cont++;
         linea= br.readLine();
     }
     br.close();
     fr.close();
     
     
     model.setColumnIdentifiers(VectornumColumnas);
     model.setRowCount(cont);
     
     //System.out.println("numero de filas"+cont);
     
     FileReader fr2=new FileReader(nomArchivo);
     BufferedReader br2= new BufferedReader(fr2);
     linea= br2.readLine();
     String[] campo;
     int cont2=0;
     
     //System.out.println("antes while");
     
     while(linea!=null){
         campo = linea.split(",");
         
         //System.out.println(campo[1]);
         
         int i=0;
         
         for (String ca : campo) {
             
             model.setValueAt(ca, cont2, i);
         
             i++;
         }
         
         linea=br2.readLine();
         cont2++;
     }
     
     System.out.println("finaliza");
     br2.close();
     fr2.close();
     
     return model;
     }
      

   
     
     
    public static void main(String[] args) {
        // TODO code application logic here
       Entrada a= new Entrada();
        a.setVisible(true);
        a.setBounds(500, 300,900, 425);
        
 
    }
    
}
