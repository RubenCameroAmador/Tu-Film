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
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tu.film.Administrador.pelicula;
import tu.film.Administrador.sala;

/**
 *
 * @author Adriana Arango y Ruben Camero
 */
public class Usuario extends javax.swing.JFrame {

    registroUsuario ptr;
    File archivoUsuario;
    File archivoPelicula;
    File Registro;
    reservaPeli ptrPeli;
    String nombrePeliculaGeneral = ",";
    String fotoPeliculaGeneral = ",";
    /**
     * Creates new form Usuario
     */

    sala ptrSala;

    public Usuario(sala ptrSala) {
        initComponents();
        this.ptrSala = ptrSala;
        ptr = null;
        mostrartablaPeliculas("archivoPelicula.txt");
        Registro = new File("./registro.txt");
        DefaultListModel model = new DefaultListModel();
        mostrarPelicula.setModel(model);
        list.setModel(model);
        horarioList.setModel(model);
        //escribir();

        ///reserva
        ptrPeli = null;
        /*try {
           ptrPeli = insertarPelicula( ptrPeli); 
        } catch (Exception e) {
            System.out.println("Error al crear la lista de peliculas disponibles para la reserva "+e.getMessage());
        }*/

    }

    class registroUsuario {

        registroUsuario link;
        String usuario;
        String contraseña;
        String numeroTarjeta;
    }

    public registroUsuario crearLista(registroUsuario ptr, String contraseña, String usuario, String num) {
        registroUsuario p = new registroUsuario();
        p.numeroTarjeta = num;
        p.usuario = usuario;
        p.contraseña = contraseña;
        p.link = ptr;
        ptr = p;
        return ptr;
    }

    public void agregarArchivo(registroUsuario ptr, File archivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            registroUsuario p = ptr;
            while (p != null) {
                bw.write(p.numeroTarjeta + "," + p.usuario + "," + p.contraseña);
                bw.newLine();
                p = p.link;
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error grabar archivo:" + e.getMessage());
        }
    }

    /////////////////////////////////RESERVA DE ENTRADAS NO TARJETA////////////////////////////////////////////////////////////
    public void mostrartablaPeliculas(String archivo) {
        try {

            DefaultTableModel model = (DefaultTableModel) mostrarPeliculas.getModel();
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            linea = br.readLine();
            String[] campos;
            while (linea != null) {
                campos = linea.split(",");
                model.addRow(new Object[]{campos[2]});
                linea = br.readLine();
            }
            br.close();
            fr.close();
        } catch (Exception e) {
        }
    }

    public void mostrarInformacionPelicula(String archivo, String nombrePelicula) {
        try {
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            linea = br.readLine();
            String[] campos;
            boolean entro = false;
            while (linea != null && entro == false) {
                campos = linea.split(",");
                if (campos[2].equals(nombrePelicula)) {
                    nombrePeliculaGeneral = campos[2];
                    fotoPeliculaGeneral = campos[10];
                    entro = true;
                    System.out.println(campos[10]);
                    ImageIcon imagen1 = new ImageIcon(campos[10]);
                    Icon icono = new ImageIcon(imagen1.getImage().getScaledInstance(imagenPelicula.getWidth(), imagenPelicula.getHeight(), Image.SCALE_DEFAULT));
                    imagenPelicula.setIcon(icono);
                    nombreMovie.setText(campos[2]);
                    formatoPelicula.setText(campos[4]);
                    generoPelicula.setText(campos[6]);
                    duracionPelicula.setText(campos[8]);
                } else {
                    linea = br.readLine();
                }
            }
            br.close();
            fr.close();
        } catch (Exception e) {
        }
    }

    public void buscarInformacionPelicula(sala ptr, String nombrePelicula) {
        DefaultListModel model = (DefaultListModel) mostrarPelicula.getModel();
        model.clear();
        sala p = ptr;

        while (p != null) {
            System.out.println(p.nombre);
            pelicula q = p.linkPeli;
            while (q != null) {
                System.out.println(q.peliculaNombre);
                if (q.peliculaNombre.equalsIgnoreCase(nombrePelicula)) {
                    model.addElement("Dia: " + q.diaPelicula + " , " + "Hora: " + q.horaPelicula);
                }
                q = q.linkPelicula;
            }
            p = p.LinkSala;
        }
    }

    ////Reserva //////////////////////////////**********************************************************************
    class reservaPeli {

        String nombrePelicula;
        String sala;
        String dia;
        int hora;
        reservaPeli link;
        silla linksi;
    }

    class silla {

        String referencia;
        silla linkSilla;
    }

    reservaPeli insertarPelicula(reservaPeli ptr2) {
        sala p = ptrSala;
        while (p != null) {
            pelicula q = p.linkPeli;
            while (q != null) {
                ptr2 = agregarPeliculaLista(ptr2, q.peliculaNombre, p.nombre, q.diaPelicula, q.horaPelicula);
                q = q.linkPelicula;
            }
            p = p.LinkSala;
        }
        return ptr2;
    }

    reservaPeli insetarSilla(reservaPeli ptr, String nombrePelicula, String dia, int hora, String referencia) {
        reservaPeli p = ptr;
        boolean entro = false;
        while (p != null && entro == false) {
            if (p.nombrePelicula.equalsIgnoreCase(nombrePelicula) &&  p.dia.equalsIgnoreCase(dia) && p.hora == hora) {
                entro = true;
            } else {
                p = p.link;
            }
        }
        silla q = p.linksi;
        silla r = new silla();
        r.referencia = referencia;
        if (q != null) {
            p.linksi = r;
        } else {
            while(q.linkSilla!=null){
                q=q.linkSilla;
            }
            if(q.referencia.equals(referencia)){
                 JOptionPane.showMessageDialog(null, "Ya existe un asiento con esa referencia");
            }else{
                q.linkSilla=r;
            }
        }
        return ptr;
    }

    reservaPeli agregarPeliculaLista(reservaPeli ptr, String nombrePelicula, String sala, String dia, int hora) {
        reservaPeli r = new reservaPeli();
        r.nombrePelicula = nombrePelicula;
        r.sala = sala;
        r.dia = dia;
        r.hora = hora;
        if (ptr == null) {
            return r;
        }
        reservaPeli k = ptr;
        while (k.link != null) {
            k = k.link;
        }
        k.link = r;
        return ptr;
    }

    public void mostrarMultilista(reservaPeli ptr) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.clear();
        reservaPeli p = ptr;
        while (p != null) {
            model.addElement(p.nombrePelicula + " , " + p.sala + " , " + p.dia + " , " + p.hora);
            p = p.link;
        }
    }
    public void mostrarMultilistaCompleta(reservaPeli ptr){
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.clear();
         reservaPeli p = ptr;
         while (p != null) {
            model.addElement(p.nombrePelicula + " , " + p.sala + " , " + p.dia + " , " + p.hora);
            silla q = p.linksi;
            while(q!=null){
                model.addElement("==> "+q.referencia);
                q=q.linkSilla;
            }
            p = p.link;
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

        Registrarse = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        nombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        contraseña = new javax.swing.JPasswordField();
        validacion = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        codigo = new javax.swing.JTextField();
        ingresarButton = new javax.swing.JButton();
        Ingresar = new javax.swing.JFrame();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        nombreIngreso = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        MirarPeliculasNO = new javax.swing.JFrame();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        mostrarPeliculas = new javax.swing.JTable();
        Informacion = new javax.swing.JButton();
        imagenPelicula = new javax.swing.JLabel();
        nombreMovie = new javax.swing.JLabel();
        formatoPelicula = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        generoPelicula = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        duracionPelicula = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        mostrarPelicula = new javax.swing.JList<>();
        jLabel12 = new javax.swing.JLabel();
        atras = new javax.swing.JButton();
        reservar = new javax.swing.JButton();
        registroFrame = new javax.swing.JFrame();
        jScrollPane3 = new javax.swing.JScrollPane();
        registro = new javax.swing.JTable();
        Reserva = new javax.swing.JFrame();
        listaReserva = new javax.swing.JScrollPane();
        list = new javax.swing.JList<>();
        extraerLista = new javax.swing.JButton();
        reservarButton = new javax.swing.JButton();
        nombrePelicula = new javax.swing.JLabel();
        horarios = new javax.swing.JScrollPane();
        horarioList = new javax.swing.JList<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        dia = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        hora = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        referenciaAsiento = new javax.swing.JTextField();
        reservaLogin = new javax.swing.JFrame();
        jLabel13 = new javax.swing.JLabel();
        nombreUsuario = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        contraseñaUsuario = new javax.swing.JPasswordField();
        reservaButton = new javax.swing.JButton();
        fotoPelicula = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        jPanel2.setBackground(new java.awt.Color(51, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(6, 6, 6, 6, new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Lucida Sans Unicode", 2, 16)); // NOI18N
        jLabel2.setText("Digite su nombre de usuario:");

        jLabel3.setFont(new java.awt.Font("Lucida Sans Unicode", 2, 16)); // NOI18N
        jLabel3.setText("Digite su contraseña:");

        jLabel4.setFont(new java.awt.Font("Lucida Sans Unicode", 2, 16)); // NOI18N
        jLabel4.setText("Digite su nueva contraseña:");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/flecha_ant_.gif"))); // NOI18N
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

        jLabel5.setFont(new java.awt.Font("Lucida Sans Unicode", 2, 16)); // NOI18N
        jLabel5.setText("Digite codigo de tarjeta Tu-Film:");

        ingresarButton.setText("Registrar");
        ingresarButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ingresarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingresarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(35, 35, 35))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(codigo)
                            .addComponent(validacion)
                            .addComponent(contraseña)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(42, 42, 42))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(188, 188, 188)
                .addComponent(ingresarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4)
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(validacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(ingresarButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout RegistrarseLayout = new javax.swing.GroupLayout(Registrarse.getContentPane());
        Registrarse.getContentPane().setLayout(RegistrarseLayout);
        RegistrarseLayout.setHorizontalGroup(
            RegistrarseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        RegistrarseLayout.setVerticalGroup(
            RegistrarseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(0, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(6, 6, 6, 6, new java.awt.Color(0, 0, 0)));
        jPanel3.setForeground(new java.awt.Color(51, 153, 255));

        jLabel6.setFont(new java.awt.Font("Lucida Sans Unicode", 2, 18)); // NOI18N
        jLabel6.setText("Digite su nombre de Usuario");

        jLabel7.setFont(new java.awt.Font("Lucida Sans Unicode", 2, 18)); // NOI18N
        jLabel7.setText("Digite Contraseña:");

        jButton5.setFont(new java.awt.Font("Lucida Sans Unicode", 2, 16)); // NOI18N
        jButton5.setText("Ingresar");
        jButton5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(211, 211, 211)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nombreIngreso, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                            .addComponent(password))))
                .addContainerGap(152, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton6))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 140, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton6)
                .addGap(24, 24, 24)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(nombreIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jButton5)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout IngresarLayout = new javax.swing.GroupLayout(Ingresar.getContentPane());
        Ingresar.getContentPane().setLayout(IngresarLayout);
        IngresarLayout.setHorizontalGroup(
            IngresarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        IngresarLayout.setVerticalGroup(
            IngresarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mostrarPeliculas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pelicula"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(mostrarPeliculas);
        if (mostrarPeliculas.getColumnModel().getColumnCount() > 0) {
            mostrarPeliculas.getColumnModel().getColumn(0).setResizable(false);
        }

        Informacion.setText("Mirar Informacion");
        Informacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InformacionActionPerformed(evt);
            }
        });

        imagenPelicula.setText("jLabel8");

        nombreMovie.setText("jLabel8");

        formatoPelicula.setText("jLabel8");

        jLabel8.setText("Nombre Pelicula:");

        jLabel9.setText("Formato Pelicula:");

        jLabel10.setText("Genero de Pelicula:");

        generoPelicula.setText("jLabel11");

        jLabel11.setText("Duracion:");

        duracionPelicula.setText("jLabel12");

        mostrarPelicula.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(mostrarPelicula);

        jLabel12.setText("Horarios de Pelicula: ");

        atras.setText("atras");
        atras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atrasActionPerformed(evt);
            }
        });

        reservar.setText("Reservar Entrada");
        reservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reservarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(imagenPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nombreMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(formatoPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generoPelicula)
                    .addComponent(jLabel11)
                    .addComponent(duracionPelicula))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(atras)
                .addGap(69, 69, 69)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(277, Short.MAX_VALUE)
                .addComponent(Informacion)
                .addGap(271, 271, 271))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(jLabel12)
                        .addGap(42, 42, 42)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(175, 175, 175)
                        .addComponent(reservar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(atras)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(Informacion)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nombreMovie)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(formatoPelicula)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(generoPelicula)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(duracionPelicula))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(imagenPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(reservar)
                        .addContainerGap(25, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addGap(158, 158, 158))))
        );

        javax.swing.GroupLayout MirarPeliculasNOLayout = new javax.swing.GroupLayout(MirarPeliculasNO.getContentPane());
        MirarPeliculasNO.getContentPane().setLayout(MirarPeliculasNOLayout);
        MirarPeliculasNOLayout.setHorizontalGroup(
            MirarPeliculasNOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MirarPeliculasNOLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        MirarPeliculasNOLayout.setVerticalGroup(
            MirarPeliculasNOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        registro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Contraseña", "Tarjeta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(registro);
        if (registro.getColumnModel().getColumnCount() > 0) {
            registro.getColumnModel().getColumn(0).setResizable(false);
            registro.getColumnModel().getColumn(1).setResizable(false);
            registro.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout registroFrameLayout = new javax.swing.GroupLayout(registroFrame.getContentPane());
        registroFrame.getContentPane().setLayout(registroFrameLayout);
        registroFrameLayout.setHorizontalGroup(
            registroFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registroFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        registroFrameLayout.setVerticalGroup(
            registroFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, registroFrameLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        list.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listaReserva.setViewportView(list);

        extraerLista.setText("ExtraerLista");
        extraerLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extraerListaActionPerformed(evt);
            }
        });

        reservarButton.setText("Reservar Pelicula");
        reservarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reservarButtonActionPerformed(evt);
            }
        });

        nombrePelicula.setText("jLabel16");

        horarioList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        horarios.setViewportView(horarioList);

        jLabel15.setText("Al iniciar una Click en Extraer Lista");

        jLabel16.setText("Seleccione el dia de la reserva: ");

        jLabel17.setText("Seleccione la hora de la Pelicula: ");

        jLabel18.setText("Verifique en la parte de arriba si el asiento no esta reservado");

        jLabel19.setText("Digite la referencia del asiento");

        javax.swing.GroupLayout ReservaLayout = new javax.swing.GroupLayout(Reserva.getContentPane());
        Reserva.getContentPane().setLayout(ReservaLayout);
        ReservaLayout.setHorizontalGroup(
            ReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReservaLayout.createSequentialGroup()
                .addGroup(ReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReservaLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(ReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombrePelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(ReservaLayout.createSequentialGroup()
                                .addGap(171, 171, 171)
                                .addGroup(ReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(horarios)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dia)
                                    .addComponent(hora)
                                    .addGroup(ReservaLayout.createSequentialGroup()
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(referenciaAsiento))))
                            .addGroup(ReservaLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(listaReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(ReservaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(extraerLista)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15))
                    .addGroup(ReservaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(reservarButton)))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        ReservaLayout.setVerticalGroup(
            ReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReservaLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(ReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(extraerLista)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(listaReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(nombrePelicula, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(horarios, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addGap(9, 9, 9)
                .addComponent(hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReservaLayout.createSequentialGroup()
                        .addComponent(reservarButton)
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReservaLayout.createSequentialGroup()
                        .addGroup(ReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(referenciaAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel18)
                        .addGap(19, 19, 19))))
        );

        jLabel13.setText("Digite su nombre de Usuario");

        jLabel14.setText("Digite su contraseña:");

        reservaButton.setText("Reservar Pelicula ");
        reservaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reservaButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout reservaLoginLayout = new javax.swing.GroupLayout(reservaLogin.getContentPane());
        reservaLogin.getContentPane().setLayout(reservaLoginLayout);
        reservaLoginLayout.setHorizontalGroup(
            reservaLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reservaLoginLayout.createSequentialGroup()
                .addGroup(reservaLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reservaLoginLayout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(reservaLoginLayout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(reservaLoginLayout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(reservaLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nombreUsuario)
                            .addComponent(contraseñaUsuario)
                            .addComponent(reservaButton, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))))
                .addContainerGap(162, Short.MAX_VALUE))
        );
        reservaLoginLayout.setVerticalGroup(
            reservaLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reservaLoginLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addComponent(contraseñaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(reservaButton)
                .addContainerGap(85, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(0, 0, 0)));
        jPanel1.setForeground(new java.awt.Color(204, 0, 51));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/tu_film_2.jpg"))); // NOI18N

        jButton1.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/icono_ingresar_.png"))); // NOI18N
        jButton1.setText("Ingresar");
        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setIconTextGap(-3);
        jButton1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Trebuchet MS", 0, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/icono_registrarse_.png"))); // NOI18N
        jButton2.setText("Registrarse");
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

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tu/film/imagenes/flecha_ant_.gif"))); // NOI18N
        jButton3.setBorder(null);
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setIconTextGap(-3);
        jButton3.setInheritsPopupMenu(true);
        jButton3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton7.setText("Mirar Peliculas");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(jButton1)
                .addGap(53, 53, 53)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(54, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(53, 53, 53))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addGap(107, 107, 107)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(25, 25, 25))
        );

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Ingresar.setVisible(true);
        Ingresar.setBounds(400, 300, 500, 400);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        LoginUsuario a = new LoginUsuario(ptrSala);
        a.setVisible(true);
        a.setBounds(500, 300, 900, 425);
        this.setVisible(false);


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Registrarse.setVisible(true);
        Registrarse.setBounds(600, 300, 500, 500);
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        Usuario s = new Usuario(ptrSala);
        s.setVisible(true);
        s.setLocationRelativeTo(null);
        s.setBounds(560, 300, 460, 480);
        Registrarse.setVisible(false);

    }//GEN-LAST:event_jButton4ActionPerformed

    public boolean registroExitoso() {
        return false;
    }

    public void leerRegistro(String archivo, String usuario, boolean confirmar) {
        try {
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String[] campos;
            while (line != null) {
                campos = line.split(",");
                if (usuario.equals(campos[0])) {
                    confirmar = true;
                } else {
                    line = br.readLine();
                }
            }
            br.close();
            fr.close();
        } catch (Exception e) {
        }
    }

    public void grabarRegistro(String Archivo, String Usuario, String Clave, boolean confirmar, String codigoTarjeta) {
        if (confirmar == true) {
            JOptionPane.showMessageDialog(null, "EL nombre de Usuario ya existe");
            nombre.setText("");
        } else {
            try {
                DefaultTableModel model = (DefaultTableModel) registro.getModel();
                model.addRow(new Object[]{Usuario, Clave, codigoTarjeta});
            } catch (Exception e) {
            }
            //RegistroUsuario = new File("./registro.txt");
            DefaultTableModel model2 = (DefaultTableModel) registro.getModel();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(Registro, true))) {
                int filas = registro.getRowCount();
                for (int i = 0; i < filas; i++) {
                    Object nombreUsuario = model2.getValueAt(i, 0);
                    Object security = model2.getValueAt(i, 1);
                    Object codigoTarget = model2.getValueAt(i, 2);
                    bw.write(nombreUsuario + "," + security + "," + codigoTarget);
                    bw.newLine();
                }
                bw.close();
            } catch (Exception e) {
            }
        }
    }

    public boolean verificarLogin(String Archivo, String nombre, String contraseña) {
        System.out.println("ENTRÓ A VERIFICAR LOGIN");
        try {
            FileReader fr = new FileReader(Archivo);
            System.out.println("creó el file");
            BufferedReader br = new BufferedReader(fr);
            System.out.println("entro al buffer");
            String Linea = br.readLine();
            String[] campos;
            while (Linea != null) {
                System.out.println("Entro al while");
                campos = Linea.split(",");
                System.out.println(nombre);
                System.out.println(contraseña);
                if (nombre.equals(campos[0]) && contraseña.equals(campos[1])) {
                    return true;
                } else {
                    Linea = br.readLine();
                }
            }
            br.close();
            fr.close();
        } catch (Exception e) {
        }
        return false;
    }

    public boolean verificarUsuario(String archivo, String nombre) {
        try {
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            String[] campos;
            while (linea != null) {
                campos = linea.split(",");
                if (nombre.equals(campos[0])) {
                    return true;
                } else {
                    linea = br.readLine();
                }
            }
            br.close();
            fr.close();

        } catch (Exception e) {
        }
        return false;
    }

    public boolean verificarIngreso(String Archivo, String nombre, String contraseña) {
        System.out.println("ENTRÓ A VERIFICAR LOGIN");
        try {
            FileReader fr = new FileReader(Archivo);
            System.out.println("creó el file");
            BufferedReader br = new BufferedReader(fr);
            System.out.println("entro al buffer");
            String Linea = br.readLine();
            String[] campos;
            while (Linea != null) {
                campos = Linea.split(",");
                if (nombre.equals(campos[0]) && contraseña.equals(campos[1])) {
                    return true;
                } else {
                    Linea = br.readLine();
                }
            }
            br.close();
            fr.close();
        } catch (Exception e) {
        }
        return false;
    }

    private void ingresarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingresarButtonActionPerformed
        // TODO add your handling code here:
        boolean login = false;
        String archivo = "./registro.txt";
        String contraseña1 = new String(contraseña.getPassword());
        String contraseña2 = new String(validacion.getPassword());
        if (verificarUsuario(archivo, nombre.getText()) == false) {
            if (contraseña1.equals(contraseña2)) {
                leerRegistro(archivo, nombre.getText(), login);
                grabarRegistro(archivo, nombre.getText(), contraseña1, login, codigo.getText());
                JOptionPane.showMessageDialog(null, "Registro exitoso");
                //crearLista(ptr, contraseña1, nombre.getText(), codigo.getText());
                //agregarArchivo(ptr, archivoUsuario);
                Ingresar.setVisible(true);
                Ingresar.setBounds(400, 300, 500, 400);
                Registrarse.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Claves no coinciden");
                validacion.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Usuario ya existente");
            nombre.setText("");
        }
    }//GEN-LAST:event_ingresarButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        String archivo = "./registro.txt";
        String pass = new String(password.getPassword());
        boolean register = verificarIngreso(archivo, nombreIngreso.getText(), pass);
        if (register == true) {
            System.out.println("INGRESÓ");
            MirarPeliculasNO.setVisible(true);
            MirarPeliculasNO.setBounds(500, 500, 685, 667);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario ó contraseña invalida");
            nombreIngreso.setText("");
            password.setText("");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        Usuario u = new Usuario(ptrSala);
        u.setVisible(true);
        u.setBounds(620, 300, 460, 480);
        Ingresar.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        MirarPeliculasNO.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void InformacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InformacionActionPerformed
        // TODO add your handling code here:
        String nombrePelicula = (String) mostrarPeliculas.getModel().getValueAt(mostrarPeliculas.getSelectedRow(), 0);
        mostrarInformacionPelicula("archivoPelicula.txt", nombrePelicula);
        try {
            buscarInformacionPelicula(ptrSala, nombrePelicula);
        } catch (Exception e) {
            System.out.println("Error al mostrar la lista de horarios" + e.getMessage());
        }

    }//GEN-LAST:event_InformacionActionPerformed

    private void atrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_atrasActionPerformed
        // TODO add your handling code here:
        LoginUsuario lu = new LoginUsuario(ptrSala);
        lu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_atrasActionPerformed

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton5MouseClicked

    private void reservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reservarActionPerformed
        // TODO add your handling code here:
        reservaLogin.setVisible(true);


    }//GEN-LAST:event_reservarActionPerformed

    private void reservaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reservaButtonActionPerformed
        // TODO add your handling code here:
        String archivo = "./registro.txt";
        String pass = new String(contraseñaUsuario.getPassword());
        boolean register = verificarIngreso(archivo, nombreUsuario.getText(), pass);
        if (register == true) {
            System.out.println("INGRESÓ");
            Reserva.setVisible(true);
            Reserva.setBounds(500, 500, 685, 667);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario ó contraseña invalida");
            nombreIngreso.setText("");
            password.setText("");
        }
    }//GEN-LAST:event_reservaButtonActionPerformed

    private void extraerListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extraerListaActionPerformed
        // TODO add your handling code here:
        try {
            ptrPeli = insertarPelicula(ptrPeli);
            mostrarMultilista(ptrPeli);
        } catch (Exception e) {
            System.out.println("Error al crear la lista de peliculas disponibles para la reserva " + e.getMessage());
        }
        ImageIcon imagen1 = new ImageIcon(fotoPeliculaGeneral);
        Icon icono = new ImageIcon(imagen1.getImage().getScaledInstance(fotoPelicula.getWidth(), fotoPelicula.getHeight(), Image.SCALE_DEFAULT));
        fotoPelicula.setIcon(icono);
        nombrePelicula.setText(nombrePeliculaGeneral);
        buscarInformacionPelicula(ptrSala, nombrePeliculaGeneral);
    }//GEN-LAST:event_extraerListaActionPerformed

    private void reservarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reservarButtonActionPerformed
        // TODO add your handling code here:
        try {
        ptrPeli =  insetarSilla(ptrPeli, nombrePeliculaGeneral, dia.getText() , Integer.parseInt(hora.getText()) ,referenciaAsiento.getText());
        mostrarMultilistaCompleta(ptrPeli);
        } catch (Exception e) {
        System.out.println("Error al guardar las sillas "+e.getMessage());
        }
        
    }//GEN-LAST:event_reservarButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Usuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Usuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Usuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Usuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Usuario(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Informacion;
    private javax.swing.JFrame Ingresar;
    private javax.swing.JFrame MirarPeliculasNO;
    private javax.swing.JFrame Registrarse;
    private javax.swing.JFrame Reserva;
    private javax.swing.JButton atras;
    private javax.swing.JTextField codigo;
    private javax.swing.JPasswordField contraseña;
    private javax.swing.JPasswordField contraseñaUsuario;
    private javax.swing.JTextField dia;
    private javax.swing.JLabel duracionPelicula;
    private javax.swing.JButton extraerLista;
    private javax.swing.JLabel formatoPelicula;
    private javax.swing.JLabel fotoPelicula;
    private javax.swing.JLabel generoPelicula;
    private javax.swing.JTextField hora;
    private javax.swing.JList<String> horarioList;
    private javax.swing.JScrollPane horarios;
    private javax.swing.JLabel imagenPelicula;
    private javax.swing.JButton ingresarButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
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
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> list;
    private javax.swing.JScrollPane listaReserva;
    private javax.swing.JList<String> mostrarPelicula;
    private javax.swing.JTable mostrarPeliculas;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField nombreIngreso;
    private javax.swing.JLabel nombreMovie;
    private javax.swing.JLabel nombrePelicula;
    private javax.swing.JTextField nombreUsuario;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField referenciaAsiento;
    private javax.swing.JTable registro;
    private javax.swing.JFrame registroFrame;
    private javax.swing.JButton reservaButton;
    private javax.swing.JFrame reservaLogin;
    private javax.swing.JButton reservar;
    private javax.swing.JButton reservarButton;
    private javax.swing.JPasswordField validacion;
    // End of variables declaration//GEN-END:variables
}
