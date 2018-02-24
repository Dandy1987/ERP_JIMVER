package org.me.gj.util;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class Utilitarios {

    public Utilitarios() {

    }

    /**
     * Metodo que convierte un fecha de tipo Date a cadena de tipo String en el
     * formato dd/MM/yyyy
     *
     * @param fecha parametro de tipo Date
     * @return Retorna la fecha en formato cadena String
     */
    public static String parseString(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = sdf.format(fecha);
        return fechaActual;
    }

    /**
     * Metodo para valida solo el ingreso de numeros de un Textbox
     *
     * @param txt_campo Textbox del archivo zul llega como parametro
     * @param mensaje Cadena que se mostrara en el mensaje Constrain
     */
    public static void validarSoloIngresoNumero(Textbox txt_campo, String mensaje) {
        txt_campo.setConstraint(new OnlyEmptyAndIntegerConstraint(mensaje));
    }
    
    /**
     * Metodo para valida solo el ingreso de numeros de un Textbox
     *
     * @param txt_campo Textbox del archivo zul llega como parametro
     * @param mensaje Cadena que se mostrara en el mensaje Constrain
     */
    public static void validarSoloIngresoNumerosYDecimal(Textbox txt_campo, String mensaje) {
        txt_campo.setConstraint(new OnlyEmptyAndStringConstraint(mensaje));
    }

    public static void deshabilitarLista(boolean validar, Listbox lst_lista) {
        int cantidad = lst_lista.getItemCount();
        for (int i = 0; i < cantidad; i++) {
            Listitem lista = lst_lista.getItemAtIndex(i);
            lista.setDisabled(validar);
        }
    }

    public static String hoyAsString() {
        Date fecha = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = sdf.format(fecha);
        return fechaActual;
    }

    public static String hoyAsString1() {
        Date fecha = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        String fechaActual = sdf.format(fecha);
        return fechaActual;
    }

    public static String hoyAsString2() {
        Date fecha = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fechaActual = sdf.format(fecha);
        return fechaActual;
    }

    public static Date hoyAsFecha() {
        Date fecha = new Date();
        return fecha;
    }

    public static String periodoActual() {
        Date fecha = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String periodo = sdf.format(fecha);
        return periodo;
    }

    public static String periodoActualxFecha(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String periodo = sdf.format(fecha);
        return periodo;
    }

    public static String anioActual() {
        Date fecha = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String periodo = sdf.format(fecha);
        return periodo;
    }

    public static String lpad(String valor, int longitud, String patron) {
        String numero = String.format("%" + patron + "" + longitud + "d", Integer.parseInt(valor));
        return numero;
    }

    public static Comboitem valorPorTexto(Combobox cb_seleccion, long codigo) {
        int i = 0;
        boolean verifica = true;
        Comboitem item = null;
        while (i < cb_seleccion.getItemCount() && verifica) {
            item = (Comboitem) cb_seleccion.getItems().get(i);
            if (codigo == Long.parseLong(item.getValue().toString())) {
                verifica = false;
            }
            i++;
        }
        return item;
    }

    public static Comboitem valorPorTexto1(Combobox cb_seleccion, int codigo) {
        int i = 0;
        boolean verifica = true;
        Comboitem item = null;
        while (i < cb_seleccion.getItemCount() && verifica) {
            item = (Comboitem) cb_seleccion.getItems().get(i);
            if (codigo == Long.parseLong(item.getValue().toString())) {
                verifica = false;
            }
            i++;
        }
        return item;
    }

    public static Comboitem textoPorTexto(Combobox cb_seleccion, String consulta) {
        int i = 0;
        boolean verifica = true;
        Comboitem item = null;
        while (i < cb_seleccion.getItemCount() && verifica) {
            item = (Comboitem) cb_seleccion.getItems().get(i);
            if (consulta.equalsIgnoreCase(item.getValue().toString())) {
                verifica = false;
            }
            i++;
        }
        return item;
    }

    public String extraerDescripcion(String pro_des) {
        int contador = 0;
        boolean valor = false;
        while (contador < pro_des.length() && !valor) {
            if (pro_des.substring(contador, contador + 1).equalsIgnoreCase("*") || pro_des.substring(contador, contador + 1).equalsIgnoreCase("/")) {
                valor = true;
            }
            contador++;
        }
        return pro_des.substring(0, contador - 1);
    }

    //Metodo para sumar "n" dias a la "fechaX" 
    public static Date sumaDias(Date fechaX, int dias) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaX);
        calendario.add(Calendar.DATE, dias);
        return calendario.getTime();
    }

    /**
     * Metodo para calcular credito y documentos por utilizar
     *
     * @param cred
     * @param doc
     * @param credcor
     * @param doccor
     * @param credprop
     * @param docprop
     * @return
     */
    public String CalculaDirencia(Long cred, int doc, Long credcor, int doccor, Long credprop, int docprop) {
        int i_saldodoc;
        long l_saldocred;
        String s_sms;
        if (doc > doccor) {
            s_sms = "Documentos en empresas es mayor a la Cantidad designada Coorporativamente";
        } else if (cred > credcor) {
            s_sms = "Credito en empresas es mayor a la Cantidad designada Coorporativamente";
        } else {
            i_saldodoc = doccor - doc;
            l_saldocred = credcor - cred;
            if (credprop > l_saldocred || docprop > i_saldodoc) {
                s_sms = "El credito disponible es : " + l_saldocred + " y cuenta con documentos disponibles :" + i_saldodoc;
            } else {
                s_sms = "";
            }
        }
        return s_sms;
    }

    //Metodo para calcular credito y documentos por utilizar cuando editamos
    public String CalculaDirenciaEdit(Long cred, int doc, Long credcor, int doccor, Long credprop, int docprop) {
        int i_saldodoc;
        long l_saldocred;
        String s_sms;
        doc = doc - docprop;
        cred = cred - credprop;
        if (doc > doccor) {
            s_sms = "Documentos en empresas es mayor a la Cantidad designada Coorporativamente";
        } else if (cred > credcor) {
            s_sms = "Credito en empresas es mayor a la Cantidad designada Coorporativamente";
        } else {
            i_saldodoc = doccor - doc;
            l_saldocred = credcor - cred;
            if (credprop > l_saldocred || docprop > i_saldodoc) {
                s_sms = "El credito disponible es : " + l_saldocred + " y cuenta con documentos disponibles :" + i_saldodoc;
            } else {
                s_sms = "";
            }
        }
        return s_sms;
    }

    //Metodo para calcular credito y documentos por utilizar cuando editamos
    public String CalculaDirenciaEdit2(Long cred, int doc, Long credcor, int doccor, Long credprop, int docprop, Long credpar, int docpart) {
        int i_saldodoc;
        long l_saldocred;
        String s_sms;
        doc = doc - docpart;
        cred = cred - credpar;
        if (doc > doccor) {
            s_sms = "Documentos en empresas es mayor a la Cantidad designada Coorporativamente";
        } else if (cred > credcor) {
            s_sms = "Credito en empresas es mayor a la Cantidad designada Coorporativamente";
        } else {
            i_saldodoc = doccor - doc;
            l_saldocred = credcor - cred;
            if (credprop > l_saldocred || docprop > i_saldodoc) {
                s_sms = "El credito disponible es : " + l_saldocred + " y cuenta con documentos disponibles :" + i_saldodoc;
            } else {
                s_sms = "";
            }
        }
        return s_sms;
    }

    public double FormatoNumero(double numero, int cantenteros) {
        double numero_formateado;
        //numero convertido a texto
        String ancho = String.valueOf(numero);
        //sacamos la posicion del punto
        int posicion_punto = ancho.indexOf(".");
        //sacamos la cantidad de enteros
        ancho = ancho.substring(0, posicion_punto - 1);
        if (ancho.length() >= cantenteros) {
            if (ancho.contains(".")) {
                numero_formateado = numero;
            } else {
                String peso_cortado = ancho.substring(0, cantenteros);
                String nuevo_peso = peso_cortado + ".0";
                double peso_ideal = Double.parseDouble(nuevo_peso);
                numero_formateado = peso_ideal;
            }
        } else {
            numero_formateado = numero;
        }
        return numero_formateado;
    }

    public static double formatoDecimal(double numero, int cantDecimales) {
        double prec = Math.pow(10, cantDecimales);
        int integerPart = (int) numero;
        double fractionalPart = numero - integerPart;
        fractionalPart *= prec;
        int fractPart = (int) fractionalPart;
        fractionalPart = (double) (integerPart) + (double) (fractPart) / prec;
        return fractionalPart;
    }

    public static String formatoDecimal(double numero, String formato) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat(formato, dfs);
        return df.format(numero);
    }

    public static String compararFechas(Date fecha1, Date fecha2) {
        String resultado;
        //Si fecha1 < fecha2
        if (fecha1.before(fecha2)) {
            resultado = "F1<";
        } else {
            // Si fecha1 > fecha2 
            if (fecha2.before(fecha1)) {
                resultado = "F1>";
            } else {
                //Fechas son iguales
                resultado = "=";
            }
        }
        return resultado;
    }

    public static String compareFechas(Date fechaini, Date fechafin) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int fecha1 = Integer.parseInt(sdf.format(fechaini));
        int fecha2 = Integer.parseInt(sdf.format(fechafin));
        String resultado;
        //Si fecha1 < fecha2
        if (fecha1 < fecha2) {
            resultado = "F1<";
        } else if (fecha1 > fecha2) {
            // Si fecha1 > fecha2 
            resultado = "F1>";
        } else {
            //Fechas son iguales
            resultado = "=";
        }
        return resultado;
    }

    public int validaEdadMayor(Date fecnac) {
        SimpleDateFormat anio = new SimpleDateFormat("yyyy");
        SimpleDateFormat mes = new SimpleDateFormat("MM");
        SimpleDateFormat dia = new SimpleDateFormat("dd");
        int anioedad = Integer.parseInt(anio.format(hoyAsFecha())) - Integer.parseInt(anio.format(fecnac));
        int mesedad = Integer.parseInt(mes.format(hoyAsFecha())) - Integer.parseInt(mes.format(fecnac));
        int diaedad = Integer.parseInt(dia.format(hoyAsFecha())) - Integer.parseInt(dia.format(fecnac));
        if (mesedad < 0 || (mesedad == 0 && diaedad < 0)) {
            anioedad--;
        }
        return anioedad;
    }

    public int validaSobreEdad(Date fecnac) {
        SimpleDateFormat anio = new SimpleDateFormat("yyyy");
        SimpleDateFormat mes = new SimpleDateFormat("MM");
        SimpleDateFormat dia = new SimpleDateFormat("dd");
        int anioedad = Integer.parseInt(anio.format(fecnac)) - Integer.parseInt(anio.format(hoyAsFecha()));
        int mesedad = Integer.parseInt(mes.format(fecnac)) - Integer.parseInt(mes.format(hoyAsFecha()));
        int diaedad = Integer.parseInt(dia.format(fecnac)) - Integer.parseInt(dia.format(hoyAsFecha()));
        if (mesedad < 0 || (mesedad == 0 && diaedad < 0)) {
            anioedad--;
        }
        return anioedad;
    }

    public static Calendar DateCalendar(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static void sumaFecha(Datebox datebox, Date date, int numdias) {
        Calendar c = DateCalendar(sumaDias(date, numdias));

        int nrodia = c.get(Calendar.DAY_OF_WEEK);
        if (nrodia == 1) {
            datebox.setValue(sumaDias(date, numdias + 1));
        } else {
            datebox.setValue(sumaDias(date, numdias));
        }
    }

    public static Date RestaDias(Date fecha, int ndias) throws SQLException {
        Date d_fec;
        String s_fec = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
        d_fec = new DaoUtil().restaDias(s_fec, ndias);
        return d_fec;
        // Validar Letras y Número, incluido ESPACIOS( ) Y GUION MEDIO(-)
    }

    public static boolean validarLetrasYNumeros(String s_var) {
        int i_c = 0, i_d = 0;
        String s_numeros = "", s_letras = "", s_otros = "";
        char c_espacio = ' ', c_guion = '-';
        for (char arg : s_var.toCharArray()) {
            if (Character.isDigit(arg)) {
                s_numeros += arg;
            } else if (Character.isLetter(arg)) {
                s_letras += arg;
            } else {
                s_otros += arg;
            }
        }

        for (int j = 0; j < s_otros.length(); j++) {
            Character s = s_otros.charAt(j);
            if (s == c_espacio || s == c_guion) {
                i_c++;
            } else {
                i_d++;
            }
        }

        if (i_c >= 0 && i_d == 0) {
            if (s_numeros.length() >= 0) {
                return true;//System.out.println("Son Números, Letras, o ambas");
            } else {
                return false;//System.out.println("NO es Número y Letra");
            }
        } else {
            return false;//System.out.println("NO es Número y Letra");
        }
    }

    //Validar Letras, incluido ESPACIOS( )
    public static boolean validasLetras(String s_var) {
        int i_c = 0, i_d = 0;
        String s_numeros = "", s_letras = "", s_otros = "";
        char c_espacio = ' ';
        for (char arg : s_var.toCharArray()) {
            if (Character.isDigit(arg)) {
                s_numeros += arg;
            } else if (Character.isLetter(arg)) {
                s_letras += arg;
            } else {
                s_otros += arg;
            }
        }

        for (int j = 0; j < s_otros.length(); j++) {
            Character s = s_otros.charAt(j);
            if (s == c_espacio) {
                i_c++;
            } else {
                i_d++;
            }
        }

        if (i_c >= 0 && i_d == 0) {
            if (s_numeros.length() == 0) {
                return true;//System.out.println("Son Letras");
            } else {
                return false;//System.out.println("NO son Letras");
            }
        } else {
            return false;//System.out.println("NO son Letras");
        }
    }

    public static boolean isMes(String s_mes) {
        if (s_mes.equals("01")) {//ENERO
            return true;
        } else if (s_mes.equals("02")) {//FEBRERO    
            return true;
        } else if (s_mes.equals("03")) {//MARZO
            return true;
        } else if (s_mes.equals("04")) {//ABRIL    
            return true;
        } else if (s_mes.equals("05")) {//MAYO    
            return true;
        } else if (s_mes.equals("06")) {//JUNIO
            return true;
        } else if (s_mes.equals("07")) {//JULIO
            return true;
        } else if (s_mes.equals("08")) {//AGOSTO
            return true;
        } else if (s_mes.equals("09")) {//SETIEMBRE
            return true;
        } else if (s_mes.equals("10")) {//OCTUBRE
            return true;
        } else if (s_mes.equals("11")) {//NOVIEMBRE
            return true;
        } else if (s_mes.equals("12")) {//DICIEMBRE
            return true;
        } else {
            return false;
        }
    }
}

