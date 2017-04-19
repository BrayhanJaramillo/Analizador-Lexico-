/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desarrollo;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import net.sf.jcarrierpigeon.WindowPosition;
import net.sf.jtelegraph.Telegraph;
import net.sf.jtelegraph.TelegraphQueue;
import net.sf.jtelegraph.TelegraphType;

/**
 *
 * @author BRAYHAN JARAMILLO
 */
public class Code {

    /**
     * *
     * Metodo que permite guardar el contacto en un archivo plano
     *
     * @param personas
     * @param nombre
     * @return true si es posible guardar los contactos, false si no es posible
     */
    public boolean guardarArchivoContacto(String palabras, String nombreArchivo) {

        Charset utf = StandardCharsets.UTF_8;
        Path lector = Paths.get(nombreArchivo + ".ssql");
        try (BufferedWriter escribir = Files.newBufferedWriter(lector, utf)) {
            escribir.write(palabras);
            escribir.close();
        } catch (Exception e) {
            mensajeDialog("Error Archivo", "Error al guardaren el archivo", TelegraphType.NOTIFICATION_ERROR);
            System.out.println("Error al escribir");
            return false;
        }
        return true;

    }

    /**
     * *
     * Metodo que permite cargar los contactos contenidos en un txt
     *
     * @param nombreArchivo
     * @return Una lista con los contactos
     */
    public String cargarContenido(String nombreArchivo, JTextArea area) {

        String temp;
        Charset utf = StandardCharsets.UTF_8;
        String cadena = "";
        Path lector = Paths.get(nombreArchivo);
        BufferedReader r;

        try {
            r = Files.newBufferedReader(lector, utf);
            int i = 0;
            String simbolo = "";
            while ((temp = r.readLine()) != null) {
                System.out.println(temp);
                if (temp.length() == 0) {
                    area.append("\n");

                } else {

                    area.append(temp + "\n");
                }

                // area.append(persona);
                i++;
            }

            System.out.println("lineas: " + i);
            r.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Code.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            mensajeDialog("Error Archivo", "Error al cargar el archivo", TelegraphType.NOTIFICATION_ERROR);
            System.out.println("Archivo vacio");
        }
        return cadena;
    }

    /**
     * *
     * Metodo que permite llenar una tabla con los contactos
     *
     * @param jtPersona
     * @param personas
     */
    public void llenarTabla(JTable jtPersona, String palabras[], String filtro) {

        limpiarTabla(jtPersona);

        DefaultTableModel modelo = (DefaultTableModel) jtPersona.getModel();
        String[] fila = new String[9];

        int contador = 1;
        while (contador <= palabras.length) {

            fila[0] = palabras[contador];
            fila[1] = palabras[contador];
            fila[2] = palabras[contador];

            modelo.addRow(fila);
            contador++;
        }

        jtPersona.setModel(modelo);
    }

    /**
     * *
     * Metodo que permite cargar todos los contactos directamente a una tabla
     *
     * @param jtPersona
     * @param personas
     */
    public void limpiarTabla(JTable tabla) {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            int filas = tabla.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            mensajeDialog("Error Tabla", "Error al limpiar la tabla", TelegraphType.NOTIFICATION_ERROR);

        }
    }

    /**
     * *
     * Metodo que nos permite refrescar la tabla
     *
     * @param jtPersona
     * @param personas
     * @param filtro
     */
    public void refrescarTabla(JTable jtPersona, String palabras[], String filtro) {

        limpiarTabla(jtPersona);
        DefaultTableModel modelo = (DefaultTableModel) jtPersona.getModel();
        Object[] fila = new Object[9];

        int contador = 1;
        while (contador <= palabras.length) {
            fila[0] = palabras[contador];
            fila[1] = palabras[contador];
            fila[2] = palabras[contador];

            modelo.addRow(fila);
            contador++;
        }
        jtPersona.setModel(modelo);
    }

    /**
     * *
     * Metodo que nos permite seleccionar un archivo de nuestro computador
     *
     * @return ruta del archivo
     */
    public String guardarArchivo() {
        String nombre;
        FileDialog d;
        d = new FileDialog((Frame) null, "Guardar Archivo", FileDialog.SAVE);
        d.setFile("Analizador Lexico");
        d.setLocation(100, 100);
        d.setVisible(true);
        nombre = d.getDirectory() + d.getFile().concat(".ssql");

        if (nombre != null) {
            mensajeDialog("Información", "El archivo fue guardado con exito", TelegraphType.NOTIFICATION_DONE);
        } else {
            mensajeDialog("Error", "Ha cancelado el guardado del archivo", TelegraphType.NOTIFICATION_WARNING);
        }

        return nombre;
    }

    /**
     * *
     * Metodo que nos permite obtener la ruta de un archivo
     *
     * @return
     */
    public String abrirArchivo() {
        String nombre;
        FileDialog d;
        d = new FileDialog((Frame) null, "Cargar Archivo", FileDialog.LOAD);
        d.setFile("");
        d.setLocation(100, 100);
        d.setVisible(true);

        nombre = d.getDirectory() + d.getFile();
        //  nombre = nombre.replace(".ssql", "");
        System.out.println("el nombre es: " + nombre);

        if (nombre.endsWith(".ssql")) {
            JOptionPane.showMessageDialog(null, "El archivo ha sido cargado con exito", "Archivo Cargado", JOptionPane.OK_OPTION);
            nombre = nombre;
        } else {
            JOptionPane.showMessageDialog(null, "No se puede abrir esta extension de archivos", "Error al Abrir el Archivo", JOptionPane.OK_OPTION);
        }

        return nombre;
    }

    /**
     * *
     * Metodo que nos permite mostrar un mensaje al usuario
     *
     * @param titulo
     * @param mensaje
     * @param type
     */
    public void mensajeDialog(String titulo, String mensaje, TelegraphType type) {
        Telegraph tele = new Telegraph(titulo, mensaje, type, WindowPosition.TOPRIGHT, 4000);
        TelegraphQueue q = new TelegraphQueue();
        q.add(tele);
    }

    
    
    private final String[] palabrasReservadasIniciales = {"Traer de", "Elimine de", "Actualice en"};

    private final String[] palabrasReservadas = {"donde",
        "sea", "igual", "a", "o", "diferente", "-", "como", "agrupado", "por",
        "este", "entre", "y", "no", "{", "}", "ordenado", "descendente", "ascente",
        "todos", "los", "datos", "\"", ".", ","};

    private final int[] digitos = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    private boolean reservado;

    public void analizarPalabras(String[] cadena) {
        for (int i = 0; i < cadena.length; i++) {
            if (!cadena[i].isEmpty()) {
                ArrayList<String> palabrasReservadas = buscarPalabraReservada(cadena[i]);
                if (this.reservado) {
                    if (palabrasReservadas.size() == 0) {
                        System.out.println("vamos bien");
                        if (buscarPalabraReservada(cadena[i]).size() > 0) {
                            System.out.println("Va mejor");
                        }
                    }
                }
                for (int j = 0; j < palabrasReservadas.size(); j++) {
                    if (palabrasReservadas.get(j).equals(cadena[i])) {

                        if (cadena[i].equals("Traer") || (cadena[i].equals("Elimine"))) {
                            try {
                                if (cadena[i + 1].equals("de")) {
                                    System.out.println("Palabra Reservada " + cadena[i] + " " + cadena[i + 1]);

                                    this.reservado = true;
                                } else {
                                    System.out.println("Error debe ir acompañada de la palabra de en la linea");
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                System.out.println("palabra normal" + cadena[i]);
                            }

                        } else if (cadena[i].equals("Actualice")) {
                            try {
                                if (cadena[i + 1].equals("en")) {
                                    System.out.println("Palabra Reservada " + cadena[i] + " " + cadena[i + 1]);
                                    this.reservado = true;
                                } else {
                                    System.out.println("palabra normal" + cadena[i]);
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                System.out.println("palabra normal" + cadena[i]);
                            }
                        } else {
                            System.out.println("Palabra Reservada " + palabrasReservadas.get(j));
                            this.reservado = true;
                        }

                    }
                }
            }
        }
    }

    public ArrayList<String> buscarPalabraReservada(String letra) {
        ArrayList<String> retorno = new ArrayList<>();
        for (int i = 0; i < palabrasReservadas.length; i++) {
            if (palabrasReservadas[i].equals(letra)) {
                retorno.add(palabrasReservadas[i]);
            }
        }
        return retorno;
    }

}
