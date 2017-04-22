/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desarrollo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 *
 * @author BRAYHAN JARAMILLO
 */
public class Codigo {

    private int contador;
    private ArrayList<String> palabras;
    private ArrayList<String> aux;
    private String consulta;
    private int iterador;
    Code code = new Code();

    /**
     * Metodo constructor de la clase
     *
     * @param consulta Expresion de caracteres a comparar
     */
    public Codigo(String consulta) {

        palabras = new ArrayList<>();
        aux = new ArrayList<>();
        this.consulta = consulta;
        this.consulta = consulta.replace("\n", " ");
        palabras = normalizarArray(consulta.split(" "));
        this.contador = palabras.size();
        iterador = 0;
    }

    /**
     * *
     * Metodo para validar las expresiones que van resultado para ir comparando
     * si es una palabra reservada o un identificador
     *
     * @param area es para mostrar mensajes
     * @param tabla para mostrar los tokens
     */
    public void validando(JTextArea area, JTable tabla) {
        area.setText("");
        palabras = normalizarArray(consulta.split(" "));
        ArrayList<String> reservadas = new ArrayList<>();
        ArrayList<String> identificador = new ArrayList<>();
        boolean estado = false;

        if (palabras.get(0).equals("Traer") && palabras.get(1).equals("de")
                || palabras.get(0).equals("Actualice") && palabras.get(1).equals("en")
                || palabras.get(0).equals("Elimine") && palabras.get(1).equals("en")
                || palabras.get(0).equals("Ingresar") && palabras.get(1).equals("en")) {
            if (palabras.get(palabras.size() - 1).equals(".")) {
                estado = true;
            } else {
                System.out.println("Dede de terminar en punto");
                area.setText("Dede de terminar la sentencia con punto");
            }

        } else {
            System.out.println("Dede de inicar con Traer de");
            area.setText("Dede de inicar con Traer de, Actualice en, Elimine en o Ingresar en");

        }

        while (palabras.size() > 1 && estado) {

            palabrasReservadasCompletas(palabras.get(0));
            String parteUno = palabras.get(0);

            String parteDos = palabras.get(1);

            String compuesta = parteUno + " " + parteDos;

            System.out.println("uno: " + parteUno + " dos: " + parteDos);

            if (esReservada(palabrasReservadas, parteUno) && !esReservada(palabrasReservadas, parteDos)) {
                System.out.println("Es palabra reservada");
                reservadas.add(parteUno);
                palabras.remove(obtenerPosicion(parteUno));
            } else if (esReservada(palabrasReservadas, compuesta)) {
                palabras.remove(obtenerPosicion(parteUno));
                palabras.remove(obtenerPosicion(parteDos));
            } else {
                System.out.println("No se pueden poner dos identificadores seguidos o palabras reservadas");
                break;
            }

            if (!esReservada(palabrasReservadas, parteDos)) {
                identificador.add(parteDos);
                System.out.println("Es identificador");
                palabras.remove(obtenerPosicion(parteDos));
            }

        }

        code.llenarTabla(tabla, reservadas, identificador);
        System.out.println("--------------------------------");
    }

    /**
     * *
     * Metodo que permite obtener la posicion de una secuencia de letras
     *
     * @param buscar palabra a comparar
     * @return -1 si no lo encontro, y > 0 si lo encontro
     */
    private int obtenerPosicion(String buscar) {
        for (int i = 0; i < palabras.size(); i++) {
            if (palabras.get(i).equals(buscar)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * *
     * Metodo para comparar si son o no palabras reservadas
     *
     * @param palabra
     */
    public void palabrasReservadasCompletas(String palabra) {

        String cadena = "";
        int cont = 0;
        if (obtenerPosicion(palabra) != -1) {
            for (int i = obtenerPosicion(palabra); i < palabras.size(); i++) {
                if (esReservada(palabrasReservadas, palabras.get(i))) {
                    System.out.println(palabras.get(i));
                    cadena += palabras.get(i) + " ";
                    cont++;

                } else {
                    break;
                }
            }
        }

        if (cont > 1) {
            cadena = cadena.substring(0, cadena.length() - 1);
            System.out.println("Palabra arreglada: " + cadena + "tamano: " + cadena.length());
            if (esReservada(palabrasReservadas, cadena)) {
                palabras.set(obtenerPosicion(palabra), cadena);
                System.out.println("eldsf: " + cadena.length());
                eliminarPalabra(cadena);
            }

        }

        System.out.println(palabras.toString());

    }

    /**
     * *
     * Metodo para ir eliminando las palabras reservadas que sean compuestas
     *
     * @param palabra
     */
    public void eliminarPalabra(String palabra) {
        ArrayList<String> normalizar = normalizarArray(palabra.split(" "));

        for (int i = 0; i < normalizar.size(); i++) {
            for (int j = 0; j < palabras.size(); j++) {
                if (normalizar.get(i).equals(palabras.get(j))) {
                    palabras.remove(j);
                }
            }
        }

    }

    /**
     * *
     * Metodo que nos permite arreglar el array con todos los tokens que
     * encontremos
     *
     * @param array
     * @return arrayList organizado por tokens
     */
    private ArrayList<String> normalizarArray(String[] array) {
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
     * *
     * Metodo para retornar la cadena de diferentes tokens
     *
     * @param array
     * @return cadena lista con los tokens necesarios
     */
    public String retornarCadena(ArrayList<String> array) {

        String cadena = "";

        for (int i = 0; i < array.size(); i++) {
            cadena += array.get(i) + " ";
        }
        return cadena;

    }

//    private void metodo(String texto) {
//        String palabrasReservadas = "(Traer de) | ([a-zA-Z]+) | (y) | (el) | (donde) | (.)";
//        texto = "Traer de nombreTabla.";
//        Pattern p = Pattern.compile(palabrasReservadas);
//        Matcher matcher = p.matcher(texto);
//
//        while (matcher.find()) {
//            String token1 = matcher.group(1);
//            if (token1 != null) {
//                System.out.println("Palabra reservada: " + token1);
//            }
//
//            String token2 = matcher.group(2);
//            if (token1 != null) {
//                System.out.println("Identificador: " + token2);
//            }
//
//            String token3 = matcher.group(3);
//            if (token1 != null) {
//                System.out.println("Palabra reservada: " + token3);
//            }
//
//            String token4 = matcher.group(4);
//            if (token1 != null) {
//                System.out.println("Palabra reservada: " + token4);
//            }
//
//            String token5 = matcher.group(5);
//            if (token1 != null) {
//                System.out.println("Palabra reservada: " + token5);
//            }
//
//            String token6 = matcher.group(6);
//            if (token1 != null) {
//                System.out.println("Terminal de linea: " + token6);
//            }
//
//        }
//    }
    private ArrayList<String> palabrasReservadas = new ArrayList<String>() {
        {
            add("Traer de");
            add("Traer");
            add("de");
            add("Elimine");
            add("Elimine en");
            add("Actualice");
            add("Actualice en");
            add("en");
            add("Ingresar");
            add("Ingresar en");
            add("donde");
            add("sea igual a");
            add("no sea igual a");
            add("o");
            add("sea diferente a");
            add("--");
            add("como");
            add("agrupado por");
            add("este entre");
            add("no este entre");
            add("y");
            add("no sea");
            add("{");
            add("}");
            add("ordenado");
            add("descendente");
            add("ascente");
            add("todos los datos");
            add("el");
            add(".");
            add(",");
            add("sea");
            add("sea {");
            add("no sea {");
            add("sea {");
            add("} ordenado por");
            add("igual");
            add("por");
            add("a");
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
            add("descendente y");
            add("ascente");
            add("todos");
            add("los");
            add("el");
            add("datos");
            add("los datos donde");

        }
    };

    /**
     * *
     * Metodo para validar si una palabra es reservada o es un identificador
     *
     * @param palabrasReservadas
     * @param palabra
     * @return true si es reservada y false si es un identificador
     */
    private boolean esReservada(ArrayList<String> palabrasReservadas, String palabra) {

        if (palabrasReservadas.contains(palabra)) {
            return true;
        }
        return false;
    }

}
