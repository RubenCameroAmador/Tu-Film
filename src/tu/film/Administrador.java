/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tu.film;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Adriana Arango y Ruben Camero
 */
public class Administrador extends javax.swing.JFrame {
   ImageIcon imagen;
   String proovedorPeli = "";
   String estadoPeli="";
   String tipoPeliculas="";
   String rutaImagen;
   Nodo ptr;
   Estado ptr2;
   tipoPeli ptr3;
   File archivoProovedor;
   File archivoEstado;
   File archivoTipoPeli;
   File archivoPelicula;
   sala ptrSala;
    /**
     * Creates new form Administrador
     */
    public Administrador() {
        initComponents();
        ptr= null;
        ptr2= null;
        ptr3=null;
        
        archivoProovedor = new File("./archivoProovedor.txt");
        archivoEstado = new File("./archivoEstado.txt");
        archivoTipoPeli = new File("./archivoTipoPeli.txt");
     
        DefaultListModel model = new DefaultListModel();
        proovedorList.setModel(model);
        extraerArchivo("archivoProovedor.txt",proovedor);
        extraerArchivo("archivoEstado.txt",Estado);
        extraerArchivo("archivoTipoPeli.txt",tipoPeliculaBox);
        
        String[] columnas =new String[11];
        for (int i = 0; i < 11 ; i++) {
            columnas[i]=Peliculas.getModel().getColumnName(i);
        }
        try {
            Peliculas.setModel(TuFilm.Mostrar("archivoPelicula.txt", columnas));
        } catch (Exception e) {
            System.out.println("Error en mostrar el archivo existente"+e.getMessage());
        }
        
        //Gestion Salas
        ptrSala=null;
        getPelicula("archivoPelicula.txt");
        listaAgregarFuncion.setModel(new DefaultListModel());
        ptrSala = insertarSala( ptrSala, "Sala 1");
        ptrSala = insertarSala( ptrSala, "Sala 2");
        mostrarLista(ptrSala);
    }
    class Estado{
        Estado linkEstado;
        String estado;
        String numeroEstado;
    }
    class tipoPeli{
        tipoPeli linkT;
        String tipo;
        String numTipo;
    }
    class Nodo{
    Nodo link;
    String Proovedor;
    String numero;
}
    
    public Nodo crearLista(Nodo ptr, String nombreProovedor, String num){
        Nodo p = new Nodo();
        p.numero= num;
        p.Proovedor= nombreProovedor;
        p.link=ptr;
        ptr=p;
        return ptr;
    }
    public tipoPeli crearLista(tipoPeli ptr, String nombreTipo, String numTipo){
        tipoPeli p = new tipoPeli();
        p.numTipo= numTipo;
        p.tipo= nombreTipo;
        p.linkT=ptr;
        ptr=p;
        return ptr;
    }
    public Estado crearLista(Estado ptr, String estado, String num){
        Estado p = new Estado();
        p.estado= estado;
        p.numeroEstado= num;
        p.linkEstado=ptr;
        ptr=p;
        return ptr;
    }
    
    public void mostrarLista(Nodo ptr){
        DefaultListModel model = (DefaultListModel) proovedorList.getModel();
        model.clear();
        Nodo p = ptr;
        while( p!=null ){
            model.addElement(p.numero+","+p.Proovedor);
            
            p = p.link;
        }
    }
    public void mostrarLista(tipoPeli ptr){
        DefaultListModel model = (DefaultListModel) proovedorList.getModel();
        model.clear();
        tipoPeli p = ptr;
        while( p!=null ){
            model.addElement(p.numTipo+","+p.tipo);
            
            p = p.linkT;
        }
    }
    public void mostrarLista(Estado ptr){
         DefaultListModel model = (DefaultListModel) proovedorList.getModel();
         model.clear();
         Estado p = ptr;
         while(p!=null){
             model.addElement(p.numeroEstado+","+p.estado);
             p=p.linkEstado;
         }
    }
    
    public void agregarArchivo(Nodo ptr, File archivo){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))){
            Nodo p= ptr;
            while(p!=null){
                bw.write(p.numero+","+p.Proovedor);
                bw.newLine();
                p=p.link;
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error grabar archivo:" + e.getMessage());
        }
    }
    public void agregarArchivo(tipoPeli ptr, File archivo){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))){
            tipoPeli p= ptr;
            while(p!=null){
                bw.write(p.numTipo+","+p.tipo);
                bw.newLine();
                p=p.linkT;
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error grabar archivo:" + e.getMessage());
        }
    }
    public void agregarArchivo(Estado ptr, File archivo){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))){
            Estado p= ptr;
            while(p!=null){
                bw.write(p.numeroEstado+","+p.estado);
                bw.newLine();
                p=p.linkEstado;
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error grabar archivo:" + e.getMessage());
        }
    }
    public void extraerArchivo(String archivo, JComboBox box){
        try {
            FileReader fr= new FileReader(archivo);
            BufferedReader br= new BufferedReader(fr);
            String linea;
            linea = br.readLine();
            String[] campos;
            int cont=0;
            boolean entro = false;
            while(linea!=null){
                entro = true;
                campos= linea.split(",");
                box.insertItemAt(campos[1], cont);
                cont++;
                linea = br.readLine();
            }
            if(entro==false){
                JOptionPane.showMessageDialog(null, "No se encontraró");
            }
        } catch (Exception e) {
            System.out.println("No extrajo la información de el archivo"+e.getMessage());
        }
    }
    
    public void getEstado(String nombreVariable){
    estadoPeli= nombreVariable;
    System.out.println("entra");
    }
    public void getProovedor(String nombreVariable){
    proovedorPeli= nombreVariable;
    System.out.println("entra");
    }
    public void getTipoPelicula(String nombreVariable){
    tipoPeliculas= nombreVariable;
    System.out.println("entra");
    }
    
    public String mostrarNumero(String archivo, String informacion){
        try {
            FileReader fr= new FileReader(archivo);
            BufferedReader br= new BufferedReader(fr);
            String linea;
            linea = br.readLine();
            String[] campos;
            while(linea!=null){
                campos=linea.split(",");
                if(informacion.equalsIgnoreCase(campos[1])){
                  return campos[0];  
                }else{
                    linea = br.readLine();
                }
            }
            br.close();
            fr.close();
            return null;
        } catch (Exception e) {
            System.out.println("Error al buscar el numero"+e.getMessage());
        }
        return null;
    }
   
   //////////////////////////////////////////////////////////////////////////////
    //Gestion Salas de Cine/////
    
    public void getPelicula(String archivo){
        try{
            FileReader fr= new FileReader(archivo);
            BufferedReader br= new BufferedReader(fr);
            String linea;
            linea = br.readLine();
            String[] campos;
            int pos=0;
            while(linea!=null){
                campos= linea.split(",");
                BoxPelicula.insertItemAt(campos[2], pos);
                pos++;
                linea = br.readLine();
            }
            br.close();
            fr.close();
        }catch(Exception e){}
    }
    
    class sala{
        String nombre;
        sala LinkSala;
        diaSemana linkdDaySala;
    }
    class diaSemana{
        String dia;
        diaSemana linkDay;
    }
    public void buscarSala(sala ptr, String nombreSala, sala p, sala antp){
        p=ptr;
        antp=null;
        while(!p.nombre.equals(nombreSala)&&p.LinkSala!=null){
            antp=p;
            p=p.LinkSala;
        }
        if(p.nombre.equalsIgnoreCase(nombreSala) && p.LinkSala==null){
            p=null;
        }
    }
    sala insertarSala(sala ptr, String nombre){
          sala p = new sala();
          p.nombre=nombre;
        if (ptr == null) {
          return p;
        }
        sala q = ptr;
        while(q.LinkSala!=null){
            q=q.LinkSala;
        }
        q.LinkSala=p;
        return ptr;
    }
     //Hacer bien el algoritmo para ingresar correctamente el dia de la semana 
    void insertarDiaPelicula(sala ptr, String nombreSala, String dia){
        sala p=null;
        sala antp=null;
        buscarSala(ptr,nombreSala,p,antp);
        if(p!=null){
            if(p.linkdDaySala==null){
                diaSemana q= new diaSemana();
                q.dia=dia;
                p.linkdDaySala=q;
                q.linkDay=null;
            }else{
                diaSemana r= new diaSemana();
                r.dia=dia;
                p.linkdDaySala=r;
                
                
            }
        }
    
    }
    
    public void mostrarLista(sala ptr){
        DefaultListModel model = (DefaultListModel) listaAgregarFuncion.getModel();
        model.clear();
        sala p = ptr;
        while( p!=null ){
            model.addElement(p.nombre);
            
            p = p.LinkSala;
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gestionPelicula = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        codigoPelicula = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        nombrePelicula = new javax.swing.JTextField();
        codigoINCAA = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        abreviatura = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        formato = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        proovedorNum = new javax.swing.JTextField();
        proovedor = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        tipoPelicula = new javax.swing.JTextField();
        tipoPeliculaBox = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        estado = new javax.swing.JTextField();
        Estado = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        duracion = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        condicion = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        foto = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Peliculas = new javax.swing.JTable();
        agregar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        insertarImagen = new javax.swing.JButton();
        proovedorLabel = new javax.swing.JLabel();
        showInformation = new javax.swing.JButton();
        tipoPeliLabel = new javax.swing.JLabel();
        estadoLabel = new javax.swing.JLabel();
        eliminarButton = new javax.swing.JButton();
        Listas = new javax.swing.JFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        proovedorList = new javax.swing.JList<>();
        jLabel12 = new javax.swing.JLabel();
        proovedorname = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        proovedorNumber = new javax.swing.JTextField();
        proovedorSave = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        estadoName = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        estadoNumber = new javax.swing.JTextField();
        estadoSave = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        tipoName = new javax.swing.JTextField();
        label = new javax.swing.JLabel();
        tipoNumber = new javax.swing.JTextField();
        tipoSave = new javax.swing.JButton();
        gestionFunciones = new javax.swing.JFrame();
        jLabel27 = new javax.swing.JLabel();
        BoxSala = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        BoxPelicula = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        BoxHorario = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        buttonGestion = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaAgregarFuncion = new javax.swing.JList<>();
        boletas = new javax.swing.JFrame();
        jPanel5 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        buscar = new javax.swing.JFileChooser();
        jPanel4 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jComboBox7 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jLabel2.setText("Código:");

        jLabel4.setText("Código INCAA");

        jLabel3.setText("Nombre: ");

        jLabel5.setText("Abreviatura:");

        jLabel6.setText("Formato:");

        formato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formatoActionPerformed(evt);
            }
        });

        jLabel7.setText("Proovedor:");

        proovedorNum.setEnabled(false);
        proovedorNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proovedorNumActionPerformed(evt);
            }
        });

        proovedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proovedorActionPerformed(evt);
            }
        });

        jLabel8.setText("Tipo Pelicula:");

        tipoPelicula.setEnabled(false);
        tipoPelicula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoPeliculaActionPerformed(evt);
            }
        });

        jLabel9.setText("Estado:");

        estado.setEnabled(false);
        estado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                estadoActionPerformed(evt);
            }
        });

        jLabel10.setText("Duración:");

        duracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                duracionActionPerformed(evt);
            }
        });

        jLabel11.setText("Condición:");

        condicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                condicionActionPerformed(evt);
            }
        });

        foto.setText("JLabel2");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(foto, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(foto, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );

        Peliculas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "CodigoINCAA", "Nombre", "Abreviatura", "Formato", "Proovedor", "TipoPelicula", "Estado", "Duracion", "Condicion", "Foto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(Peliculas);
        if (Peliculas.getColumnModel().getColumnCount() > 0) {
            Peliculas.getColumnModel().getColumn(0).setResizable(false);
            Peliculas.getColumnModel().getColumn(1).setResizable(false);
            Peliculas.getColumnModel().getColumn(2).setResizable(false);
            Peliculas.getColumnModel().getColumn(3).setResizable(false);
            Peliculas.getColumnModel().getColumn(4).setResizable(false);
            Peliculas.getColumnModel().getColumn(5).setResizable(false);
            Peliculas.getColumnModel().getColumn(6).setResizable(false);
            Peliculas.getColumnModel().getColumn(7).setResizable(false);
            Peliculas.getColumnModel().getColumn(8).setResizable(false);
            Peliculas.getColumnModel().getColumn(9).setResizable(false);
            Peliculas.getColumnModel().getColumn(10).setResizable(false);
        }

        agregar.setText("Agregar");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        jLabel13.setText("Foto:");

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/flecha_ant_.gif"))); // NOI18N
        jButton8.setBorder(null);
        jButton8.setBorderPainted(false);
        jButton8.setContentAreaFilled(false);
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setIconTextGap(-3);
        jButton8.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        insertarImagen.setText("Insertar Imagen");
        insertarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertarImagenActionPerformed(evt);
            }
        });

        showInformation.setText("Mostrar Información");
        showInformation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showInformationActionPerformed(evt);
            }
        });

        tipoPeliLabel.setText("jLabel27");

        estadoLabel.setText("jLabel27");

        eliminarButton.setText("Eliminar");
        eliminarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(proovedorNum, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(codigoPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(codigoINCAA, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(34, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(abreviatura, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(formato, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(proovedor, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(proovedorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tipoPeliLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nombrePelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(insertarImagen, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                            .addComponent(jLabel10)
                                                            .addGap(18, 18, 18)
                                                            .addComponent(duracion, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(Estado, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                            .addGap(32, 32, 32)
                                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(jLabel13)
                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                    .addComponent(jLabel11)
                                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                    .addComponent(condicion, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                                            .addComponent(estadoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(eliminarButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(showInformation)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(agregar)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tipoPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tipoPeliculaBox, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton8)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(codigoPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(codigoINCAA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(nombrePelicula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(abreviatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(formato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(proovedorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proovedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(proovedorNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tipoPeliLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tipoPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(tipoPeliculaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(estadoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(duracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(condicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(insertarImagen))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel13)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(agregar)
                            .addComponent(showInformation)
                            .addComponent(eliminarButton))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout gestionPeliculaLayout = new javax.swing.GroupLayout(gestionPelicula.getContentPane());
        gestionPelicula.getContentPane().setLayout(gestionPeliculaLayout);
        gestionPeliculaLayout.setHorizontalGroup(
            gestionPeliculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gestionPeliculaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        gestionPeliculaLayout.setVerticalGroup(
            gestionPeliculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gestionPeliculaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        proovedorList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(proovedorList);

        jLabel12.setText("Proovedores");

        jLabel14.setText("Nombre Proovedor");

        jLabel15.setText("Numero Proovedor");

        proovedorSave.setText("SAVE");
        proovedorSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proovedorSaveActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/flecha_ant_.gif"))); // NOI18N
        jButton6.setBorder(null);
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setIconTextGap(-3);
        jButton6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel21.setText("Estado");

        jLabel22.setText("Tipo de estado: ");

        jLabel23.setText("Numero Estado:");

        estadoSave.setText("SAVE");
        estadoSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                estadoSaveActionPerformed(evt);
            }
        });

        jLabel24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel25.setText("Tipo de Pelicula");

        jLabel26.setText("Nombre Tipo:");

        label.setText("Numero Tipo:");

        tipoSave.setText("SAVE");
        tipoSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ListasLayout = new javax.swing.GroupLayout(Listas.getContentPane());
        Listas.getContentPane().setLayout(ListasLayout);
        ListasLayout.setHorizontalGroup(
            ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListasLayout.createSequentialGroup()
                .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ListasLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(proovedorname)
                            .addComponent(proovedorNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)))
                    .addGroup(ListasLayout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addGap(58, 58, 58)
                        .addComponent(jLabel12))
                    .addGroup(ListasLayout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(proovedorSave, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ListasLayout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ListasLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(estadoName, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                            .addComponent(estadoNumber))))
                .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(ListasLayout.createSequentialGroup()
                        .addGap(205, 205, 205)
                        .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ListasLayout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addComponent(jLabel25)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ListasLayout.createSequentialGroup()
                                .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tipoName, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(261, 261, 261))))
                    .addGroup(ListasLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tipoNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(261, 261, 261))))
            .addGroup(ListasLayout.createSequentialGroup()
                .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ListasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ListasLayout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(estadoSave, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(173, 173, 173)
                .addComponent(tipoSave, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ListasLayout.setVerticalGroup(
            ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ListasLayout.createSequentialGroup()
                .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(jLabel25))
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(proovedorname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(tipoName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(proovedorNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tipoNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ListasLayout.createSequentialGroup()
                        .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ListasLayout.createSequentialGroup()
                                .addComponent(proovedorSave)
                                .addGap(16, 16, 16)
                                .addComponent(jLabel24))
                            .addComponent(tipoSave))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(estadoName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ListasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(estadoNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addComponent(estadoSave))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(393, Short.MAX_VALUE))
        );

        jLabel27.setText("Sala: ");

        BoxSala.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sala 1 ", "Sala 2" }));

        jLabel28.setText("Pelicula: ");

        jLabel29.setText("Día : ");

        BoxHorario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo", " " }));
        BoxHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BoxHorarioActionPerformed(evt);
            }
        });

        jLabel30.setText("Hora: ");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        buttonGestion.setText("Agregar Función");
        buttonGestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGestionActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(listaAgregarFuncion);

        javax.swing.GroupLayout gestionFuncionesLayout = new javax.swing.GroupLayout(gestionFunciones.getContentPane());
        gestionFunciones.getContentPane().setLayout(gestionFuncionesLayout);
        gestionFuncionesLayout.setHorizontalGroup(
            gestionFuncionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gestionFuncionesLayout.createSequentialGroup()
                .addGroup(gestionFuncionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gestionFuncionesLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(buttonGestion))
                    .addGroup(gestionFuncionesLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(gestionFuncionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(gestionFuncionesLayout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(18, 18, 18)
                                .addComponent(BoxPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(gestionFuncionesLayout.createSequentialGroup()
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(BoxSala, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gestionFuncionesLayout.createSequentialGroup()
                                .addGroup(gestionFuncionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel29)
                                    .addComponent(jLabel30))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(gestionFuncionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(BoxHorario, 0, 118, Short.MAX_VALUE)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(461, Short.MAX_VALUE))
        );
        gestionFuncionesLayout.setVerticalGroup(
            gestionFuncionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gestionFuncionesLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(gestionFuncionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(BoxSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gestionFuncionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28)
                    .addComponent(BoxPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gestionFuncionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(BoxHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gestionFuncionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(buttonGestion)
                .addGap(147, 147, 147))
        );

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/flecha_ant_.gif"))); // NOI18N
        jButton9.setBorder(null);
        jButton9.setBorderPainted(false);
        jButton9.setContentAreaFilled(false);
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setIconTextGap(-3);
        jButton9.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton9)
                .addContainerGap(451, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jButton9)
                .addContainerGap(326, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout boletasLayout = new javax.swing.GroupLayout(boletas.getContentPane());
        boletas.getContentPane().setLayout(boletasLayout);
        boletasLayout.setHorizontalGroup(
            boletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        boletasLayout.setVerticalGroup(
            boletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/flecha_ant_.gif"))); // NOI18N
        jButton7.setBorder(null);
        jButton7.setBorderPainted(false);
        jButton7.setContentAreaFilled(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jButton7.setIconTextGap(-3);
        jButton7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel1.setText("Sala:");

        jLabel16.setText("Pelicula:");

        jLabel17.setText("Modelo Boleto:");

        jLabel18.setText("Horarios:");

        jLabel19.setText("Fecha Inicial:");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "7:30", "9:00", "10:30", "12:00", "13:30", "15:00", "16:30", "18:00", "19:30", "21:00", "22:30", "0:00", "1:30" }));

        jLabel20.setText("Venta Via:");

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Boleteria ", "Sitio Web ", "Ambas" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField5))
                    .addComponent(jButton7)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel17)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox3, 0, 276, Short.MAX_VALUE)
                            .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(112, 112, 112))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton7)
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(183, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(6, 5, 5, 5, new java.awt.Color(0, 0, 0)));

        jButton1.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/icono_.jpg"))); // NOI18N
        jButton1.setText("Gestion de Peliculas");
        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setIconTextGap(-3);
        jButton1.setInheritsPopupMenu(true);
        jButton1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/iconosfuciones_.jpg"))); // NOI18N
        jButton2.setText("Gestion de Funciones");
        jButton2.setBorder(null);
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setIconTextGap(-3);
        jButton2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/icono_ticket_.jpg"))); // NOI18N
        jButton3.setText("Boletas de Cine");
        jButton3.setBorder(null);
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setIconTextGap(-3);
        jButton3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/admin_._.png"))); // NOI18N
        jButton4.setText("Proveedores");
        jButton4.setBorder(null);
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setIconTextGap(-3);
        jButton4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/flecha_ant_.gif"))); // NOI18N
        jButton5.setBorder(null);
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setIconTextGap(-3);
        jButton5.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(81, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(61, 61, 61))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(20, 20, 20)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(jButton1)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jButton3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jButton4)))
                .addGap(83, 83, 83))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        gestionPelicula.setVisible(true);
        gestionPelicula.setLocationRelativeTo(null);
        gestionPelicula.setBounds(500, 150, 750, 850);
        this.setVisible(false);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formatoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_formatoActionPerformed

    private void proovedorNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proovedorNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_proovedorNumActionPerformed

    private void tipoPeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoPeliculaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoPeliculaActionPerformed

    private void estadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_estadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_estadoActionPerformed

    private void duracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_duracionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_duracionActionPerformed

    private void condicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_condicionActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_condicionActionPerformed

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        // TODO add your handling code here:
        getEstado(Estado.getSelectedItem().toString());
        getProovedor(proovedor.getSelectedItem().toString());
        getTipoPelicula(tipoPeliculaBox.getSelectedItem().toString());
            DefaultTableModel model = (DefaultTableModel) Peliculas.getModel();
            String estado1= estadoPeli;
            String proovedor1 = proovedorPeli;
            String tipoPeli1 = tipoPeliculas;
            proovedorNum.setText(mostrarNumero("archivoProovedor.txt",proovedor1));
            estado.setText(mostrarNumero("archivoEstado.txt",estado1));
            tipoPelicula.setText(mostrarNumero("archivoTipoPeli.txt",tipoPeli1));
            model.addRow(new Object[]{codigoPelicula.getText(), codigoINCAA.getText(),
            nombrePelicula.getText(),abreviatura.getText(),formato.getText(),proovedorPeli,
            tipoPeli1,estado1,duracion.getText(),condicion.getText(),rutaImagen} );

             ImageIcon imagen1 = new ImageIcon(rutaImagen);
             Icon icono= new ImageIcon(imagen1.getImage().getScaledInstance(foto.getWidth(), foto.getHeight(), Image.SCALE_DEFAULT));
             foto.setIcon(icono);

            
        int desicion = JOptionPane.showConfirmDialog(null, "¿Deseas guardar la pelicula?");
        if(desicion==JOptionPane.YES_OPTION){
            
            
        archivoPelicula = new File("./archivoPelicula.txt");
        DefaultTableModel model2 = (DefaultTableModel) Peliculas.getModel();
        /*try {
            PrintWriter writer = new PrintWriter(archivoPelicula);
            writer.print("");
            writer.close();
        } catch (Exception e) {
            System.out.println("Error Limpiar archivo: " + e.getMessage());
        }*/
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoPelicula, true))) {
            int filas = Peliculas.getRowCount();
            for (int i = 0; i < filas; i++) {
                Object codigoP = model2.getValueAt(i, 0);
                Object codigoIP = model2.getValueAt(i, 1);
                Object nombrep = model2.getValueAt(i, 2);
                Object abreviaturap = model2.getValueAt(i, 3);
                Object formatop = model2.getValueAt(i, 4);
                Object proovedorp = model2.getValueAt(i, 5);
                Object tipoPeliculap = model2.getValueAt(i, 6);
                Object estadop = model2.getValueAt(i, 7);
                Object duracionp = model2.getValueAt(i, 8);
                Object condicionp = model2.getValueAt(i, 9);
                Object fotop = model2.getValueAt(i, 10);
                bw.write(codigoP+","+codigoIP+","+nombrep+","+abreviaturap+","+formatop+","+proovedorp+","+tipoPeliculap+","+estadop+","+duracionp+","+condicionp+","+fotop);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error guardar archivo");
        }
        }
    }//GEN-LAST:event_agregarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        Listas.setVisible(true);
        Listas.setLocationRelativeTo(null);
        Listas.setBounds(700, 300, 500, 400);
        this.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void proovedorSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proovedorSaveActionPerformed
        // TODO add your handling code here:
     String provedor = proovedorname.getText();
     String numero = proovedorNumber.getText();
    
     ptr= crearLista(ptr, provedor, numero);
     mostrarLista(ptr);
     agregarArchivo(ptr,archivoProovedor);
     
    }//GEN-LAST:event_proovedorSaveActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        gestionFunciones.setVisible(true);
        gestionFunciones.setBounds(700, 300, 550, 500);
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        boletas.setVisible(true);
        boletas.setBounds(600, 300, 500, 400);
        this.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        Administrador a = new Administrador();
        a.setVisible(true);
        a.setBounds(570, 300, 560, 530);
        Listas.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        Administrador a = new Administrador();
            a.setVisible(true);
            a.setLocationRelativeTo(null);
            a.setBounds(570, 300, 560, 530);
            gestionPelicula.setVisible(false);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        Administrador a = new Administrador();
            a.setVisible(true);
            a.setLocationRelativeTo(null);
            a.setBounds(570, 300, 560, 530);
            gestionFunciones.setVisible(false);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        Administrador a= new Administrador();
        a.setVisible(true);
        a.setBounds(570, 300, 560, 530);
        boletas.setVisible(false);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void proovedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proovedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_proovedorActionPerformed

    private void estadoSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_estadoSaveActionPerformed
        // TODO add your handling code here:
     ptr2= crearLista(ptr2, estadoName.getText(), estadoNumber.getText());
     mostrarLista(ptr2);
     agregarArchivo(ptr2,archivoEstado);
        
    }//GEN-LAST:event_estadoSaveActionPerformed

    private void tipoSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoSaveActionPerformed
        // TODO add your handling code here:
     ptr3= crearLista(ptr3, tipoName.getText(), tipoNumber.getText());
     mostrarLista(ptr3);
     agregarArchivo(ptr3,archivoTipoPeli);
    }//GEN-LAST:event_tipoSaveActionPerformed

    private void insertarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertarImagenActionPerformed
        // TODO add your handling code here:
         FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imagenes", "jpg", "gif", "png");
         buscar.setFileFilter(filtro);
          int a = buscar.showOpenDialog(this);
          if( a== JFileChooser.APPROVE_OPTION){
            File archivo = buscar.getSelectedFile();
            String nombre2 = archivo.getName();
            String ruta = archivo.getParent();
            rutaImagen = ruta+ "\\"+nombre2; 
          }
    }//GEN-LAST:event_insertarImagenActionPerformed

    private void showInformationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showInformationActionPerformed
        // TODO add your handling code here:
        String cod = (String) Peliculas.getModel().getValueAt(Peliculas.getSelectedRow(), 0);
        String codIN = (String) Peliculas.getModel().getValueAt(Peliculas.getSelectedRow(), 1);
        String nom = (String) Peliculas.getModel().getValueAt(Peliculas.getSelectedRow(), 2);
        String abr = (String) Peliculas.getModel().getValueAt(Peliculas.getSelectedRow(), 3);
        String form = (String) Peliculas.getModel().getValueAt(Peliculas.getSelectedRow(), 4);
        String prov = (String) Peliculas.getModel().getValueAt(Peliculas.getSelectedRow(), 5);
        String tipop= (String) Peliculas.getModel().getValueAt(Peliculas.getSelectedRow(), 6);
        String est = (String) Peliculas.getModel().getValueAt(Peliculas.getSelectedRow(), 7);
        String dura = (String) Peliculas.getModel().getValueAt(Peliculas.getSelectedRow(), 8);
        String condi = (String) Peliculas.getModel().getValueAt(Peliculas.getSelectedRow(), 9);
        String picture = (String) Peliculas.getModel().getValueAt(Peliculas.getSelectedRow(), 10);
        codigoPelicula.setText(cod);
        codigoINCAA.setText(codIN);
        nombrePelicula.setText(nom);
        abreviatura.setText(abr);
        formato.setText(form);
        proovedorNumber.setText(mostrarNumero("archivoProovedor.txt",prov));
        proovedorLabel.setText(prov);
        tipoPelicula.setText(mostrarNumero("archivoTipoPeli.txt",tipop));
        tipoPeliLabel.setText(tipop);
        estado.setText(mostrarNumero("archivoEstado.txt",est));
        estadoLabel.setText(est);
        duracion.setText(dura);
        condicion.setText(condi);
        ImageIcon imagen1 = new ImageIcon(picture);
        Icon icono= new ImageIcon(imagen1.getImage().getScaledInstance(foto.getWidth(), foto.getHeight(), Image.SCALE_DEFAULT));
        foto.setIcon(icono);
    }//GEN-LAST:event_showInformationActionPerformed

    private void eliminarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarButtonActionPerformed
        // TODO add your handling code here:
         DefaultTableModel model = (DefaultTableModel) Peliculas.getModel();
        int elim = Peliculas.getSelectedRow();
        if(elim>=0){
            model.removeRow(elim);
            JOptionPane.showMessageDialog(null, "Eliminacion Exitosa");
        }else{
            JOptionPane.showMessageDialog(null, "No se pudo eliminar la Pelicula");
        }
    }//GEN-LAST:event_eliminarButtonActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void BoxHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BoxHorarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BoxHorarioActionPerformed

    private void buttonGestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGestionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonGestionActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Administrador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> BoxHorario;
    private javax.swing.JComboBox<String> BoxPelicula;
    private javax.swing.JComboBox<String> BoxSala;
    private javax.swing.JComboBox<String> Estado;
    private javax.swing.JFrame Listas;
    private javax.swing.JTable Peliculas;
    private javax.swing.JTextField abreviatura;
    private javax.swing.JButton agregar;
    private javax.swing.JFrame boletas;
    private javax.swing.JFileChooser buscar;
    private javax.swing.JButton buttonGestion;
    private javax.swing.JTextField codigoINCAA;
    private javax.swing.JTextField codigoPelicula;
    private javax.swing.JTextField condicion;
    private javax.swing.JTextField duracion;
    private javax.swing.JButton eliminarButton;
    private javax.swing.JTextField estado;
    private javax.swing.JLabel estadoLabel;
    private javax.swing.JTextField estadoName;
    private javax.swing.JTextField estadoNumber;
    private javax.swing.JButton estadoSave;
    private javax.swing.JTextField formato;
    private javax.swing.JLabel foto;
    private javax.swing.JFrame gestionFunciones;
    private javax.swing.JFrame gestionPelicula;
    private javax.swing.JButton insertarImagen;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel label;
    private javax.swing.JList<String> listaAgregarFuncion;
    private javax.swing.JTextField nombrePelicula;
    private javax.swing.JComboBox<String> proovedor;
    private javax.swing.JLabel proovedorLabel;
    private javax.swing.JList<String> proovedorList;
    private javax.swing.JTextField proovedorNum;
    private javax.swing.JTextField proovedorNumber;
    private javax.swing.JButton proovedorSave;
    private javax.swing.JTextField proovedorname;
    private javax.swing.JButton showInformation;
    private javax.swing.JTextField tipoName;
    private javax.swing.JTextField tipoNumber;
    private javax.swing.JLabel tipoPeliLabel;
    private javax.swing.JTextField tipoPelicula;
    private javax.swing.JComboBox<String> tipoPeliculaBox;
    private javax.swing.JButton tipoSave;
    // End of variables declaration//GEN-END:variables
}
