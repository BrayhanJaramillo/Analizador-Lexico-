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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
 * @author PC
 */
public class IdentificarPalabras {

    public ArrayList<String> palabrasNormalizadas;
    public ArrayList<String> token = new ArrayList<>();
    public ArrayList<String> lexema = new ArrayList<>();

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
            Logger.getLogger(IdentificarPalabras.class.getName()).log(Level.SEVERE, null, ex);
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
    public void llenarTabla(JTable jtPersona, ArrayList<String> reservadas, ArrayList<String> identificador) {

        limpiarTabla(jtPersona);

        DefaultTableModel modelo = (DefaultTableModel) jtPersona.getModel();
        String[] fila = new String[3];

        int contador = 0;
        while (contador < reservadas.size()) {

            fila[0] = reservadas.get(contador);
            fila[1] = identificador.get(contador);
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

    private final ArrayList<String> palabrasReservadas = new ArrayList<String>() {
        {
            add("Traer");
            add("de");
            add("Elimine");
            add("Actualice");
            add("en");
            add("Ingresar");
            add("donde");
            add("sea");
            add("igual");
            add("a");
            add("o");
            add("diferente");
            add("-");
            add("como");
            add("agrupado");
            add("por");
            add("este");
            add("entre");
            add("y");
            add("no");
            add("{");
            add("}");
            add("ordenado");
            add("descendente");
            add("ascendente");
            add("todos");
            add("los");
            add("el");
            add("datos");
            add("[");
            add("]");
            add("->");
        }
    };

    private final ArrayList<String> terminalDeLinea = new ArrayList<String>() {
        {
            add(".");
        }

    };

    private final ArrayList<String> separador = new ArrayList<String>() {
        {
            add(",");
        }

    };

    public void analizadorLexico(String[] palabras, JTable jtpersona, JTextArea error) {

        for (int i = 0; i < palabras.length; i++) {
            if (!palabras[i].equals("")) {

                if (palabrasReservadas.contains(palabras[i])) {
                    System.out.println("Palabra Reservada");
                    System.out.println(palabras[i]);
                    token.add("Palabra Reservada");
                    lexema.add(palabras[i]);
                } else if (terminalDeLinea.contains(palabras[i])) {
                    System.out.println("Terminal de linea");
                    System.out.println(".");
                    token.add("Terminal de linea");
                    lexema.add(".");
                } else if (separador.contains(palabras[i])) {
                    System.out.println("Separador");
                    System.out.println(",");
                    token.add("Separador");
                    lexema.add(",");
                } else if (palabras[i].startsWith("\"") && palabras[i].endsWith("\"")) {
                    System.out.println("Cadena");
                    System.out.println(palabras[i]);
                    token.add("Cadena");
                    lexema.add(palabras[i]);
                } else if (palabras[i].startsWith("-") && palabras[i].substring(0, 1).equals("-")) {
                    System.out.println("comentario");
                    System.out.println(palabras[i]);
                    token.add("Comentario");
                    lexema.add(palabras[i]);
                } else if (isNumeric(palabras[i])) {
                    System.out.println("Numero");
                    System.out.println(palabras[i]);
                    token.add("Numero");
                    lexema.add(palabras[i]);
                } else {
                    String regex = "[A-Za-z]";
                    Pattern patron = Pattern.compile(regex);
                    Matcher emparejador = patron.matcher(palabras[i]);
                    boolean esCoincidente = emparejador.find();
                    if (esCoincidente) {
                        System.out.println("Identificador");
                        System.out.println(palabras[i]);
                        token.add("Identificador");
                        lexema.add(palabras[i]);
                    } else {
                        System.out.println("Error");
                        error.append("Error: " + palabras[i] + "\n");
                    }
                }

            }
        }

        llenarTabla(jtpersona, token, lexema);
    }

    private static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
