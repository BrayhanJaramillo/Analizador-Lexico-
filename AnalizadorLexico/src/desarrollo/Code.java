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
    public Code() {
        llenarArrayNumeros();
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

    private final ArrayList<Integer> numeros = new ArrayList<>();

    public void llenarArrayNumeros() {
        for (int i = 0; i < 10; i++) {
            this.numeros.add(i);
        }
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
            add("ascente");
            add("todos");
            add("los");
            add("el");
            add("datos");
            add("\"");
            add(".");
            add(",");
        }
    };

    private final int[] digitos = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public ArrayList<String> lectura = new ArrayList<>();
    public int contador = 0;
    public boolean vamosBien;
    public ArrayList<String> palabrasNormalizadas;
    DefaultTableModel modeloTabla = new DefaultTableModel();

    public boolean verificarPalabraNormal(JTextArea error) {
        boolean retorno = true;
        if (!noEsPalabraReservada()) {
            error.setText("Despues de una palabra reservada debe ir un identificador");
            System.err.println("Despues de una palabra reservada debe ir un identificador");
            return false;
        }
        return retorno;
    }

    /**
     * Metodo que nos permite validar
     *
     * @param palabras
     * @param error
     * @param jtDatos
     */
    public void analizarPalabras(String[] palabras, JTextArea error, JTable jtDatos) {
        modeloTabla = (DefaultTableModel) jtDatos.getModel();
        this.palabrasNormalizadas = normalizarArray(palabras);
        this.vamosBien = true;
        String palabra = obtenerPalabra(this.palabrasNormalizadas, this.contador);
        if (obtenerPalabra(this.palabrasNormalizadas, this.contador).indexOf(".") == obtenerPalabra(this.palabrasNormalizadas, this.contador).lastIndexOf(".")) {
            if (palabrasIniciales(palabra, error)) {
                aumentarContador();
                if (acompaniantePalabraInicial(obtenerPalabra(this.palabrasNormalizadas, this.contador), error)) {
                    aumentarContador();
                    if (contador >= 1) {
                        for (int i = 0; i < this.palabrasNormalizadas.size() - 1; i++) {
                            if (estadoFinalizar()) {
                                if (vamosBien) {
                                    if (verificarPalabraNormal(error)) {
                                        if (!estadoFinalizar()) {
                                            parteFinal(error);
                                        }
                                        this.vamosBien = false;

                                    }
                                } else if (esPalabraReservada()) {
                                    if (!estadoFinalizar()) {
                                        parteFinal(error);
                                    }

                                    if ((this.palabrasNormalizadas.lastIndexOf(",") != -1)) {
                                        if (this.palabrasNormalizadas.lastIndexOf("donde") != -1) {
                                            System.out.println(obtenerPalabra(this.palabrasNormalizadas, this.contador));
                                            if ((this.palabrasNormalizadas.lastIndexOf("donde") < this.palabrasNormalizadas.lastIndexOf(","))
                                                    || (this.palabrasNormalizadas.lastIndexOf(",") > this.palabrasNormalizadas.lastIndexOf("."))
                                                    || (this.palabrasNormalizadas.lastIndexOf(",") > this.palabrasNormalizadas.lastIndexOf("."))) {

                                                error.setText("Sintaxis error la palabra " + obtenerPalabra(this.palabrasNormalizadas, this.contador) + " no puede ir despues del separador");
                                                System.err.println("Sintaxis error la palabra " + obtenerPalabra(this.palabrasNormalizadas, this.contador) + " no puede ir despues del separador");
                                            }

                                            if (obtenerPalabra(this.palabrasNormalizadas, this.contador).equals("no")) {
                                                aumentarContador();
                                                System.out.println(obtenerPalabra(this.palabrasNormalizadas, this.contador));
                                                if (!validarEste(error)) {
                                                    error.setText("Despues de");
                                                    System.err.println("Despues de un este debe de ir un entre");
                                                    continue;
                                                }
                                                break;
                                            }

                                            if (this.palabrasReservadas.contains("este")) {
                                                System.out.println(obtenerPalabra(this.palabrasNormalizadas, this.contador));
                                                if (obtenerPalabra(this.palabrasNormalizadas, this.contador).equals("este")) {
                                                    aumentarContador();
                                                    if (!obtenerPalabra(this.palabrasNormalizadas, this.contador).equals("entre")) {
                                                        error.setText("Despues de un este debe de ir un entre");
                                                        System.err.println("Despues de un este debe de ir un entre");
                                                        continue;
                                                    }
                                                }
                                            }

                                        } else if (this.palabrasNormalizadas.lastIndexOf(",") != -1) {
                                            if (this.palabrasNormalizadas.lastIndexOf("donde") > this.palabrasNormalizadas.lastIndexOf(",")) {
                                                error.setText("Sintaxis error la palabra " + obtenerPalabra(this.palabrasNormalizadas, this.contador) + " no puede ir sino esta el donde");
                                                System.err.println("Sintaxis error la palabra " + obtenerPalabra(this.palabrasNormalizadas, this.contador) + " no puede ir sino esta el donde");
                                            }
                                            if (this.palabrasNormalizadas.lastIndexOf(".") > this.palabrasNormalizadas.lastIndexOf(",")) {
                                                error.setText("Sintaxis error la palabra " + obtenerPalabra(this.palabrasNormalizadas, this.contador) + " no puede ir sino esta el donde");
                                                System.err.println("Sintaxis error la palabra " + obtenerPalabra(this.palabrasNormalizadas, this.contador) + " no puede ir sino esta el donde");
                                            }

                                        }
                                    }
                                    this.vamosBien = true;
                                } else {
                                    error.setText("Despues de un identificador debe de ir una palabra reservada");
                                    System.err.println("Despues de un identificador debe de ir una palabra reservada");
                                }
                                aumentarContador();
                            } else {
                                parteFinal(error);
                            }
                        }

                    }
                }
            }
        } else {
            error.setText("Solo debe de contener una  palabra rreservada '.'");
            System.err.println("Solo debe de contener una  palabra rreservada '.'");
        }

        this.contador = 0;
    }

    /**
     * con este metodo garantizamos que la primera palabra sea Traer, Elimine o
     * Actualice
     *
     * @param palabra, la palabra a analizar
     * @param error, text area donde se ponen el error
     * @return verdadero si inicia con una de las palabras anteriores, de lo
     * contrario muestra el error en la caja de texto
     */
    public boolean palabrasIniciales(String palabra, JTextArea error) {
        boolean retorno = false;
        if (palabra.equals(this.palabrasReservadas.get(0))
                || palabra.equals(this.palabrasReservadas.get(2))
                || palabra.equals(this.palabrasReservadas.get(3))
                || palabra.equals(this.palabrasReservadas.get(5))) {
            this.lectura.add(palabra);
            retorno = true;
        } else {
            error.setText("Debe de empezar con Traer, Elimine, Ingresar o Actualice");
            System.err.println("Debe de empezar con Traer, Elimine o Actualice");
        }
        return retorno;
    }

    /**
     * metodo que nos permite saber si despues de una palabra reservada como lo
     * son Traer, Elimine, Actualice, siguen con su respectivo acompañante ya
     * que es indispensable saberlo.
     *
     * @param palabra, la siguente palabra despues de la primera
     * @param error, el error que se le va a mostrar al usuario
     * @return verdadero si la palabra(acompañante) es la correcta de lo
     * contrario falso
     */
    public boolean acompaniantePalabraInicial(String palabra, JTextArea error) {
        boolean retorno = false;
        if ((this.lectura.contains("Traer")) && palabra.equals(this.palabrasReservadas.get(1))) {
            this.lectura.add(palabra);
            retorno = true;
        } else if ((this.lectura.contains("Actualice")
                || this.lectura.contains("Elimine")
                || this.lectura.contains("Ingresar"))
                && (palabra.equals(this.palabrasReservadas.get(4)))) {
            this.lectura.add(palabra);
            retorno = true;
        } else {
            error.setText(mensaje(lectura.get(0)));
            System.err.println(mensaje(lectura.get(0)));
        }
        this.lectura.clear();
        return retorno;
    }

    /**
     * Retorna la palabra obtenida en cada iteracion
     *
     * @param palabras lista de palabras
     * @param i, posicion de la palabra que se quiere obtener
     * @return la palabra obtenida
     */
    public String obtenerPalabra(ArrayList<String> palabras, int i) {
        String palabra = "";
        for (int j = 0; j < palabras.get(i).length(); j++) {
            palabra += palabras.get(i).charAt(j);
        }
        return palabra;
    }

    /**
     * metodo que nos permite quitar los espacion vacios, en un array, se guarda
     * en un arreglo para normalizarlo
     *
     * @param array, el array con las palabras obtenidas del textArea
     * @return un arraylist con solo las palabras sin los espacios
     */
    public ArrayList<String> normalizarArray(String[] array) {
        ArrayList<String> retorno = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (array[i].length() > 0) {
                if (i == array.length - 1) {
                    if (array[i].lastIndexOf(".") != -1) {
                        if (array[i].substring(0, array[i].lastIndexOf(".")).length() > 0) {
                            retorno.add(array[i].substring(0, array[i].lastIndexOf(".")));
                            retorno.add(".");
                        } else {
                            retorno.add(".");
                        }

                        continue;
                    }
                }
                if (array[i].lastIndexOf(",") != -1) {
                    retorno.add(array[i].substring(0, array[i].indexOf(",")));
                    retorno.add(",");
                    continue;
                }

                retorno.add(array[i]);
            }
        }
        return retorno;
    }

    /**
     * metodo que nos permite escribir un mensaje para que sea claro para el
     * usuario
     *
     * @param palabra, la palabra para ponerla en el mensaje
     * @return el mensaje bien estructurado
     */
    public String mensaje(String palabra) {
        String mesaje = "";
        if (palabra.equals("Actualice") || palabra.equals("Elimine") || palabra.equals("Ingresar")) {
            mesaje = "la palabra " + palabra + " debe ir acompañada de 'en'";
        } else {
            mesaje = "la palabra " + palabra + " debe ir acompañada de 'de'";
        }
        return mesaje;
    }

    /**
     * metodo que nos permite saber si podemos aumentar el contador en base a el
     * arreglo de palabras que tenemos
     */
    public void aumentarContador() {
        if (this.contador < this.palabrasNormalizadas.size() - 1) {
            this.contador += 1;
        }
    }

    public void parteFinal(JTextArea error) {
        if (!obtenerPalabra(this.palabrasNormalizadas, this.contador).equals(".")) {
            error.setText("Debe de terminar con '.'");
            System.err.println("Debe de terminar con '.'");
        }
    }

    public boolean palabraSiguiente(String primeraPalabra, String siguiente) {
        boolean retorno = false;
        if ((primeraPalabra.equals("Traer") && (siguiente.equals("y") || siguiente.equals("el")))
                || (primeraPalabra.equals("Actualice") && siguiente.equals("el"))
                || primeraPalabra.equals("Ingresar") && siguiente.equals("el")) {
            retorno = true;
        } else if (primeraPalabra.equals("Elimine") && (siguiente.equals("todos") || siguiente.equals("los"))) {
            retorno = true;
        }
        return retorno;
    }

    public boolean estadoFinalizar() {
        boolean retorno = false;
        if (this.contador < (this.palabrasNormalizadas.size() - 1)) {
            retorno = true;
        }
        return retorno;
    }

    public boolean esPalabraReservada() {
        return this.palabrasReservadas.contains(obtenerPalabra(this.palabrasNormalizadas, this.contador));
    }

    public boolean noEsPalabraReservada() {
        return !this.palabrasReservadas.contains(obtenerPalabra(this.palabrasNormalizadas, this.contador));
    }

    public void mensajeOtros(String primeraPalabra, String siguiente, JTextArea error) {
        if (!(primeraPalabra.equals("Traer") && (siguiente.equals("y") || siguiente.equals("el")))) {
            error.setText("La palabra " + primeraPalabra + " va en su estructura con" + " 'el' o 'y'");
            System.err.println("La palabra " + primeraPalabra + " va en su estructura con" + " 'el' o 'y'");
        } else if ((primeraPalabra.equals("Actualice") && siguiente.equals("el"))
                || primeraPalabra.equals("Ingresar") && siguiente.equals("el")) {
            error.setText("La palabra " + primeraPalabra + " va en su estructura con" + " 'y'");
            System.err.println("La palabra " + primeraPalabra + " va en su estructura con" + " 'y'");
        } else if (!primeraPalabra.equals("Elimine") && (siguiente.equals("todos") || siguiente.equals("los"))) {
            error.setText("La palabra " + primeraPalabra + " va en su estructura con" + " todos o el");
            System.err.println("La palabra " + primeraPalabra + " va en su estructura con" + " 'todos' o 'el'");
        }
    }

    public boolean validarEste(JTextArea error) {
        boolean retorno = false;
        if (obtenerPalabra(this.palabrasNormalizadas, this.contador).equals("este")) {
            aumentarContador();
            if (!obtenerPalabra(this.palabrasNormalizadas, this.contador).equals("entre")) {
                retorno = true;
            }
        }
        return retorno;
    }

}
