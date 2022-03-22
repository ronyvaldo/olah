package com.olah.clients.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataUtil {

    private static String DATE_FORMAT = "dd/MM/yyyy";

    public static Integer getIdade(Date dataNascimento) {
        Calendar dateOfDataNascimento = new GregorianCalendar();
        dateOfDataNascimento.setTime(dataNascimento);
        Calendar dataHoje = new GregorianCalendar();
        // Obtem a idade baseado no ano
        int idade = dataHoje.get(Calendar.YEAR) - dateOfDataNascimento.get(Calendar.YEAR);
        dateOfDataNascimento.roll(Calendar.YEAR, idade);
        //se a data de hoje eh antes da data de Nascimento, entao diminui 1(um)
        if (dataHoje.before(dateOfDataNascimento)) {
            idade--;
        }
        return idade;
    }

    public static String converterDateToStringData(Date date) {
        String s = null;
        try {
            if (date == null) {
                s = new String();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                s =  sdf.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
