package com.itla.appblog.api;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Funciones {
    public static String FormatoFecha(Date date){
        SimpleDateFormat formatter=new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        return formatter.format(date);
    }

    public static String FormatoFechaTexto(Date date){
        SimpleDateFormat formatter=new SimpleDateFormat("MMMM dd, yyyy");
        return formatter.format(date);
    }
}
