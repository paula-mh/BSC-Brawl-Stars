/**
* @author paulamh
*/
package bsc;

import static bsc.Equipo.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class Resultados {
    private static ArrayList <Resultados> listaResultados = new ArrayList <>(); //arraylist para almacenar objetos de tipo Resultado
    private ArrayList<Equipo> listaEquipo = Equipo.getEquipos();  //coge la información del arraylist de Equipos
    private Iterator<Resultados> iteradorResultados = listaResultados.iterator();
    private Equipo equipo = new Equipo(0, null, 0);
    private static Conexion conexionBD = Conexion.getInstancia();
    private Connection conexion = conexionBD.getConexion();

    //variables de resultados
    Resultados resultados;
    private int registro = 1;
    private int finalMensual;
    private int posicion;
    private int puntos;
    private int region;
    private int fecha;
    private int id;
    private String nombre_equipo;

    //variables del programa
    private static int resultadosImportadosDB;
    private static int resultadosExportadosDB;
    private final String[] meses = {"Febrero", "Marzo", "Abril", "Junio", "Julio", "Agosto"};
    private final Icon icono = new ImageIcon(getClass().getResource("/images/bsc_100x100.png"));
    private final Icon iconoError = new ImageIcon(getClass().getResource("/images/error_bsc.png"));
    private final Icon iconoWarning = new ImageIcon(getClass().getResource("/images/warning_bsc.png"));

    public Resultados(){};

    public Resultados(int registro, int fecha, int finalMensual, int region, int id, int posicion, int puntos) {
        this.registro = registro;
        this.finalMensual = finalMensual;
        this.fecha = fecha;
        this.region = region;
        this.posicion = posicion;
        this.puntos = puntos;
        this.id = id;
    }

    //getters y setters
    public static ArrayList<Resultados> getResultados() {
        return listaResultados;
    }

    public int getRegistro() {
        return registro;
    }

    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

    public int getFinalMensual() {
        return finalMensual;
    }

    public void setFinalMensual(int finalMensual) {
        this.finalMensual = finalMensual;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getId() {
        return id;
    }

    //métodos de la clase
    public void introducirResultados() {
        String finalMensualS = "";
        String regionS = "";
        try{
            finalMensualS = (String) JOptionPane.showInputDialog(null, "Selecciona a qué final mensual corresponde el resultado", "Añadir resultados", JOptionPane.QUESTION_MESSAGE, icono, meses, meses[0]);
            finalMensual = getFinalMensual(finalMensualS);
            regionS = (String) JOptionPane.showInputDialog(null, "Escribe a qué región corresponde la Final Mensual", "Añadir resultados", JOptionPane.QUESTION_MESSAGE, icono, Equipo.regiones, Equipo.regiones[0]);
            region = Equipo.getRegiones(regionS);
        }catch(NullPointerException npe){
            return;
        }

        if (resultadosExistentes(region, finalMensual)){
            posicion = 0;
            for (int i=0; i<8; i++){
                registro++;
                posicion++;
                try{
                    String arrayEquipos[] = equipo.cargarEquipoRegionArray(region);
                    nombre_equipo = (String) JOptionPane.showInputDialog(null, "Selecciona el equipo para la posición " + (i+1), "Añadir resultados", JOptionPane.QUESTION_MESSAGE, icono, arrayEquipos, arrayEquipos[0]);
                    for (Equipo e: listaEquipo) {
                        if (nombre_equipo.equals(e.getNombre())){
                            id = e.getId();
                        }
                    }
                }catch(NullPointerException npe){
                    return;
                }catch (ArrayIndexOutOfBoundsException aiobe){
                    return;
                }
                fecha = getFechas(finalMensual, region);
                puntos = getPuntos(posicion);
                resultados = new Resultados(registro, fecha, finalMensual, region, id, posicion, puntos);
                listaResultados.add(resultados);
                exportaDatosResultados(fecha, finalMensual, region, id, posicion, puntos);
            }
            JOptionPane.showMessageDialog(null, "Resultados almacenados correctamente", "Añadir resultados", JOptionPane.INFORMATION_MESSAGE, icono);
        }else{
            JOptionPane.showMessageDialog(null, "Ya hay unos resultados almacenados para esa región y final mensual. Vuelve a intentarlo", "Error", JOptionPane.ERROR_MESSAGE, iconoError);
            return;
        }
    }


    public int getPuntos(int posicion){  //devuelve el número de puntos dependiendo de la posición
        switch (posicion) {
            case 1:puntos = 100; break;
            case 2:puntos = 70; break;
            case 3:case 4:puntos = 50; break;
            case 5:case 6:case 7:case 8:puntos = 35; break;
            default:puntos = 0; JOptionPane.showMessageDialog(null, "Error al calcular los puntos", "Error", JOptionPane.ERROR_MESSAGE, iconoError); break;
        }
        return puntos;
    }


    public int getFinalMensual(String finalMensualS){
        if(finalMensualS.equals("Febrero")){finalMensual = 1;
        }else if(finalMensualS.equals("Marzo")){finalMensual = 2;
        }else if(finalMensualS.equals("Abril")){finalMensual = 3;
        }else if(finalMensualS.equals("Junio")){finalMensual = 4;
        }else if(finalMensualS.equals("Julio")){finalMensual = 5;
        }else if(finalMensualS.equals("Agosto")){finalMensual = 6;}
        return finalMensual;
    }



    public int getFechas(int finalMensual, int region) {  //devuelve el número de puntos dependiendo de la final mensual y la posición
        switch(finalMensual){
            case 1: //febrero
                switch(region){
                    case 1: fecha = 1; break; case 2: fecha = 2; break; case 3: fecha = 3; break; case 4: fecha = 4; break;
                    case 5: fecha = 5; break; case 6: fecha = 6; break; case 7: fecha = 7; break; case 8: fecha = 8; break;
                    default: fecha = 0; break;}
            break;
            case 2: //marzo
                switch(region){
                    case 1: fecha = 9; break; case 2: fecha = 10; break; case 3: fecha = 11; break; case 4: fecha = 12; break;
                    case 5: fecha = 13; break; case 6: fecha = 14; break; case 7: fecha = 15; break; case 8: fecha = 16; break;
                    default: fecha = 0; break;}
            break;
            case 3: //abril
                switch(region){
                    case 1: fecha = 17; break; case 2: fecha = 18; break; case 3: fecha = 19; break; case 4: fecha = 20; break;
                    case 5: fecha = 21; break; case 6: fecha = 22; break; case 7: fecha = 23; break; case 8: fecha = 24; break;
                    default: fecha = 0; break;}
            break;
            case 4: //junio
                switch(region){
                    case 1: fecha = 25; break; case 2: fecha = 26; break; case 3: fecha = 27; break; case 4: fecha = 28; break;
                    case 5: fecha = 29; break; case 6: fecha = 30; break; case 7: fecha = 31; break; case 8: fecha = 32; break;
                    default: fecha = 0; break;}
            break;
            case 5: //julio
                switch(region){
                    case 1: fecha = 33; break; case 2: fecha = 34; break; case 3: fecha = 35; break; case 4: fecha = 36; break;
                    case 5: fecha = 37; break; case 6: fecha = 38; break; case 7: fecha = 39; break; case 8: fecha = 40; break;
                    default: fecha = 0; break;}
            break;
            case 6: //agosto
                switch(region){
                    case 1: fecha = 41; break; case 2: fecha = 42; break; case 3: fecha = 43; break; case 4: fecha = 44; break;
                    case 5: fecha = 45; break; case 6: fecha = 46; break; case 7: fecha = 47; break; case 8: fecha = 48; break;
                    default: fecha = 0; break;}
            break;
            default: fecha = 0; JOptionPane.showMessageDialog(null, "Error al calcular la fecha", "Error", JOptionPane.ERROR_MESSAGE, iconoError); break;
        }
        return fecha;
    }



    public void borrarResultados(){ //Elimina uno o varios registros
        JOptionPane.showMessageDialog(null, "Esto eliminará todos los resultados de una región en una misma fecha", "Atención", JOptionPane.WARNING_MESSAGE, iconoWarning);
        String finalMensualS = "";
        String regionS = "";
        try {
            finalMensualS = (String) JOptionPane.showInputDialog(null, "Selecciona a qué final mensual corresponden los resultados que quieres eliminar", "Eliminar resultados", JOptionPane.QUESTION_MESSAGE, icono, meses, meses[0]);
            finalMensual = getFinalMensual(finalMensualS);
            regionS = (String) JOptionPane.showInputDialog(null, "Selecciona a qué región corresponde la Final Mensual que quieres eliminar", "Eliminar resultados", JOptionPane.QUESTION_MESSAGE, icono, Equipo.regiones, Equipo.regiones[0]);
            region = Equipo.getRegiones(regionS);
        }catch(NullPointerException npe){
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(null, "Se van a eliminar los resultados de " + finalMensualS + " para la región de " + regionS + ". ¿Estás seguro?" , "Eliminar resultados", JOptionPane.INFORMATION_MESSAGE, 0, icono);

        try {
            if(confirmacion == 0){
                eliminaResultadosBD(region, finalMensual);
                for (Resultados r: listaResultados){
                    if(r.getFinalMensual() == finalMensual){
                        if(r.getRegion() == region){
                            iteradorResultados.next();
                            iteradorResultados.remove();
                       }
                   }
                }
                JOptionPane.showMessageDialog(null, "Los registros se han eliminado correctamente", "Eliminar resultados", JOptionPane.INFORMATION_MESSAGE, icono);
            }
        }catch(ConcurrentModificationException cme){
            importaDatosResultados();
            JOptionPane.showMessageDialog(null, "Los registros se han eliminado correctamente", "Eliminar resultados", JOptionPane.INFORMATION_MESSAGE, icono);
        }
    }



    //métodos que se comunican con la BD
     public void mostrarResultados(){
        String datosEquipo[] = new String[4];
        String finalMensualS = "";
        String regionS = "";
        String instruccionSQL = "";
        try {
            finalMensualS = (String) JOptionPane.showInputDialog(null, "Selecciona a qué final mensual corresponden los resultados que quieres visualizar", "Mostrar resultados", JOptionPane.QUESTION_MESSAGE, icono, meses, meses[0]);
            finalMensual = getFinalMensual(finalMensualS);
            regionS = (String) JOptionPane.showInputDialog(null, "Selecciona a qué región corresponde la Final Mensual que quieres visualizar", "Mostrar resultados", JOptionPane.QUESTION_MESSAGE, icono, Equipo.regiones, Equipo.regiones[0]);
            region = Equipo.getRegiones(regionS);

        }catch(NullPointerException npe){
            return;
        }

        instruccionSQL = "SELECT e.id_equipo, e.nombre_equipo, re.nombre_region, SUM(r.puntos) AS puntos_totales FROM resultados r JOIN equipo e ON r.id_equipo = e.id_equipo " +
                        "JOIN region re ON re.id_region = r.region_id WHERE r.region_id = ? AND r.mf = ? GROUP BY e.id_equipo, e.nombre_equipo, re.nombre_region ORDER BY puntos_totales DESC;";

        try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)) {
            pstmt.setInt(1, region);
            pstmt.setInt(2, finalMensual);
            ResultSet ejecucion = pstmt.executeQuery();

            while (ejecucion.next()) {
                finalMensualS = ejecucion.getString(1);
                regionS = ejecucion.getString(2);
                String fecha = ejecucion.getString(3);
                String hora = ejecucion.getString(4);

                //imprimir datos en la tabla
                datosEquipo[0] = finalMensualS;
                datosEquipo[1] = regionS;
                datosEquipo[2] = fecha;
                datosEquipo[3] = hora;
                BSC.setTablaResultados(datosEquipo, 8);
            }
            ejecucion.close();

            }catch(SQLException sqle){
             JOptionPane.showMessageDialog(null, "Error al mostrar los resultados: " + sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
    }
    
    
    public void eliminaResultadosBD(int region, int finalMensual){
        String instruccionSQL = "DELETE FROM resultados WHERE region_id = ? AND mf = ?";   //sentencia SQL para eliminar resultados
        try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)){
            pstmt.setInt(1, region);
            pstmt.setInt(2, finalMensual);
            pstmt.executeUpdate();

        }catch (ConcurrentModificationException cme){
        }catch (SQLException sqle){
                JOptionPane.showMessageDialog(null, "Error al eliminar los resultados: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
    }



    public void exportaDatosResultados(int fecha, int finalMensual, int region, int id, int posicion, int puntos) { //exporta los resultados a la BD
        String instruccionSQL = "INSERT INTO resultados (fecha, mf, region_id, id_equipo, posicion, puntos) VALUES (?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL);
            pstmt.setInt(1, fecha);
            pstmt.setInt(2, finalMensual);
            pstmt.setInt(3, region);
            pstmt.setInt(4, id);
            pstmt.setInt(5, posicion);
            pstmt.setInt(6, puntos);
            pstmt.executeUpdate();

            resultadosImportadosDB++;
            resultadosExportadosDB++;
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al exportar los resultados: " + sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
    }



    public void importaDatosResultados(){ //importa los resultados de la BD
        String instruccionSQL = "SELECT * FROM resultados";  //sentencia SQL
        try (PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL);
             ResultSet ejecucion = pstmt.executeQuery()) {

            while (ejecucion.next()){
                registro = ejecucion.getInt(1);
                fecha = ejecucion.getInt(2);
                finalMensual = ejecucion.getInt(3);
                region = ejecucion.getInt(4);
                id = ejecucion.getInt(5);
                posicion = ejecucion.getInt(6);
                puntos = ejecucion.getInt(7);

                resultados = new Resultados(registro, fecha, finalMensual, region, id, posicion, puntos);  //cargar todos los datos en el ArrayList
                listaResultados.add(resultados);
            }
            ejecucion.close();
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al importar los resultados: " + sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
    }


    public void verResultadosRegiones(){ //hace una llamada a la BD y muestra la tabla de todos los equipos de una región con sus puntos
        String datosEquipo[] = new String[4];
        String regionS = "";
        String instruccionSQL = "";
        try{
            regionS = (String) JOptionPane.showInputDialog(null, "Selecciona la región de la que quieres ver los resultados", "Mostrar resultados", JOptionPane.QUESTION_MESSAGE, icono, Equipo.regiones, Equipo.regiones[0]);
            region = Equipo.getRegiones(regionS);
        }catch(NullPointerException npe){
            return;
        }
        //sentencias SQL. Cada elemento del array carga la información de cada región
        String arrayInstruccionSQL[] = new String[8];
        arrayInstruccionSQL[0] = "SELECT * FROM `resultados_sea`;";
        arrayInstruccionSQL[1] = "SELECT * FROM `resultados_india`;";
        arrayInstruccionSQL[2] = "SELECT * FROM `resultados_ea`;";
        arrayInstruccionSQL[3] = "SELECT * FROM `resultados_emea`;";
        arrayInstruccionSQL[4] = "SELECT * FROM `resultados_sa_east`;";
        arrayInstruccionSQL[5] = "SELECT * FROM `resultados_sa_west`;";
        arrayInstruccionSQL[6] = "SELECT * FROM `resultados_na_east`;";
        arrayInstruccionSQL[7] = "SELECT * FROM `resultados_na_west`;";

        try{
        instruccionSQL = arrayInstruccionSQL[region-1];
        }catch (ArrayIndexOutOfBoundsException aiobe){
            return;
        }
        try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)){
            ResultSet ejecucion = pstmt.executeQuery();

            while (ejecucion.next()){
                id = ejecucion.getInt(1);
                nombre_equipo = ejecucion.getString(2);
                regionS = ejecucion.getString(3);
                puntos = ejecucion.getInt(4);

                //imprimir datos en la tabla
                datosEquipo[0] = Integer.toString(id);
                datosEquipo[1] = nombre_equipo;
                datosEquipo[2] = regionS;
                datosEquipo[3] = Integer.toString(puntos);
                BSC.setTablaEquipoRegiones(datosEquipo, region);
            }
            ejecucion.close();

        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al mostrar los resultados: " + sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }catch (ArrayIndexOutOfBoundsException aiobe){
            return;
        }
    }



    public void verEquiposClasificadosWorlds(){ //hace una llamada a la BD y muestra la tabla de los equipos con más puntos de su región
        //sentencias SQL. Cada elemento del array carga la información de cada región. El limit es el número de plazas de cada región
        String regionS = "";
        String datosEquipo[] = new String[4];
        String arrayInstruccionSQL[] = new String[9];
        arrayInstruccionSQL[0] = "SELECT * FROM `resultados_sea` LIMIT 0;";  //0 plazas SEA
        arrayInstruccionSQL[1] = "SELECT * FROM `resultados_india` LIMIT 0;"; //0 plazas INDIA
        arrayInstruccionSQL[2] = "SELECT * FROM `resultados_ea` LIMIT 1;";  //1 plazas EA
        arrayInstruccionSQL[3] = "SELECT * FROM `resultados_emea` LIMIT 3;";  //3 plazas EMEA
        arrayInstruccionSQL[4] = "SELECT * FROM `resultados_sa_east` LIMIT 1;"; //1 plazas SA EAST
        arrayInstruccionSQL[5] = "SELECT * FROM `resultados_sa_west` LIMIT 0;";  //0 plaxas SA WEST
        arrayInstruccionSQL[6] = "SELECT * FROM `resultados_na_east` LIMIT 1;";  //1 plazas NA EAST
        arrayInstruccionSQL[7] = "SELECT * FROM `resultados_na_west` LIMIT 1;";  //1 plazas NA WEST
        arrayInstruccionSQL[8] = "SELECT * FROM `resultados_mainland_china` LIMIT 1;";  //1 plazas Mainland China

        try{
            for (int i = 0; i < arrayInstruccionSQL.length; i++){
                String instruccionSQL = arrayInstruccionSQL[i];
                PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL);
                ResultSet ejecucion = pstmt.executeQuery();

                while (ejecucion.next()){
                    id = ejecucion.getInt(1);
                    nombre_equipo = ejecucion.getString(2);
                    regionS = ejecucion.getString(3);
                    puntos = ejecucion.getInt(4);
                    int total = 8; //suma de todas las plazas en total

                    //imprimir datos en la tabla
                    datosEquipo[0] = Integer.toString(id);
                    datosEquipo[1] = nombre_equipo;
                    datosEquipo[2] = regionS;
                    datosEquipo[3] = Integer.toString(puntos);
                    BSC.setTablaResultados(datosEquipo, total);
                }
                ejecucion.close();
            }
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al mostrar los equipos: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
    }



    public void verEquiposClasificadosLCQ(){
        String regionS = "";
        String datosEquipo[] = new String[4];
        String arrayInstruccionSQL[] = new String[9];
        arrayInstruccionSQL[0] = "SELECT * FROM `resultados_sea` LIMIT 2;";  //2 plazas SEA
        arrayInstruccionSQL[1] = "SELECT * FROM `resultados_india` LIMIT 2;"; //2 plazas INDIA
        arrayInstruccionSQL[2] = "SELECT * FROM `resultados_ea` LIMIT 1, 1;";  //2 plazas EA
        arrayInstruccionSQL[3] = "SELECT * FROM `resultados_emea` LIMIT 3, 4;";  //4 plazas EMEA
        arrayInstruccionSQL[4] = "SELECT * FROM `resultados_sa_east` LIMIT 1, 1;"; //1 plazas SA EAST
        arrayInstruccionSQL[5] = "SELECT * FROM `resultados_sa_west` LIMIT 2;";  //2 plaxas SA WEST
        arrayInstruccionSQL[6] = "SELECT * FROM `resultados_na_east` LIMIT 1, 1;";  //1 plazas NA EAST
        arrayInstruccionSQL[7] = "SELECT * FROM `resultados_na_west` LIMIT 1, 1;";  //1 plazas NA WEST
        arrayInstruccionSQL[8] = "SELECT * FROM `resultados_mainland_china` LIMIT 1, 1;";  //1 plazas Mainland China

        try{
            for (int i = 0; i < arrayInstruccionSQL.length; i++){
                String instruccionSQL = arrayInstruccionSQL[i];
                PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL);
                ResultSet ejecucion = pstmt.executeQuery();

                while (ejecucion.next()){
                    id = ejecucion.getInt(1);
                    nombre_equipo = ejecucion.getString(2);
                    regionS = ejecucion.getString(3);
                    puntos = ejecucion.getInt(4);
                    int total = 16; //suma de todas las plazas en total

                    //imprimir datos en la tabla
                    datosEquipo[0] = Integer.toString(id);
                    datosEquipo[1] = nombre_equipo;
                    datosEquipo[2] = regionS;
                    datosEquipo[3] = Integer.toString(puntos);
                    BSC.setTablaResultados(datosEquipo, total);
                }
                ejecucion.close();
            }
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al mostrar los equipos: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
    }


    //muestra las fechas en la tabla
    public void mostrarFechas(){
        String finalMensualS = "";
        String regionS = "";
        String datosEquipo[] = new String[4];
        try{
            regionS = (String) JOptionPane.showInputDialog(null, "Selecciona la región", "Mostrar fechas", JOptionPane.QUESTION_MESSAGE, icono, regiones, regiones[0]);
            region = getRegiones(regionS);
            finalMensualS = (String) JOptionPane.showInputDialog(null, "Selecciona la final mensual", "Mostrar fechas", JOptionPane.QUESTION_MESSAGE, icono, meses, meses[0]);
            finalMensual = getFinalMensual(finalMensualS);
        }catch(NullPointerException npe){
            return;
        }
        String instruccionSQL = "SELECT r.nombre_region, mf.mes, f.fecha, f.hora FROM fechas f JOIN region r ON " +
        "r.id_region = f.region_id JOIN Monthly_Final mf ON f.mf_id = mf.id_mf WHERE r.id_region = ? AND mf.id_mf = ?;";

        try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)) {
            pstmt.setInt(1, region);
            pstmt.setInt(2, finalMensual);
            ResultSet ejecucion = pstmt.executeQuery();

            while (ejecucion.next()) {
                finalMensualS = ejecucion.getString(1);
                regionS = ejecucion.getString(2);
                String fecha = ejecucion.getString(3);
                String hora = ejecucion.getString(4);

                //imprimir datos en la tabla
                datosEquipo[0] = finalMensualS;
                datosEquipo[1] = regionS;
                datosEquipo[2] = fecha;
                datosEquipo[3] = hora;
                BSC.setTablaFechas(datosEquipo);
            }
            ejecucion.close();
        }catch(SQLException sqle){
             JOptionPane.showMessageDialog(null, "Error al mostrar las fechas: " + sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
    }


    //comprueba que no existen 8 resultados para una región y MF
    public boolean resultadosExistentes(int region, int finalMensual){
        String instruccionSQL = "SELECT COUNT(*) FROM resultados where region_id = ? AND mf = ?;";
        int contador = 0;
        boolean valido = false;
        try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)) {
            pstmt.setInt(1, region);
            pstmt.setInt(2, finalMensual);
            ResultSet ejecucion = pstmt.executeQuery();

            while(ejecucion.next()) {
                contador = ejecucion.getInt(1);
            }
            ejecucion.close();
        }catch(SQLException sqle){
             JOptionPane.showMessageDialog(null, "Se ha producido un error: " + sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }

        if(contador == 0){
             valido = true;
        }else if(contador > 0 && contador <8){
            String deleteSQL = "DELETE FROM resultados where region_id = ? AND mf = ?;";
            try(PreparedStatement delete = conexion.prepareStatement(deleteSQL)) {
                delete.setInt(1, region);
                delete.setInt(2, finalMensual);
                delete.executeUpdate();
                delete.close();
            }catch(SQLException sqle){
                JOptionPane.showMessageDialog(null, "Se ha producido un error: " + sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, iconoError);
            }
            valido = true;
        }else if(contador == 8){
            valido = false;
        }
        return valido;
    }


    public void buscarEquipo(String textoBuscador){
        if (textoBuscador.equals("")){
            JOptionPane.showMessageDialog(null, "El campo no debe estar vacío ", "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }else{
            String datosEquipo[] = new String[4];
            int contadorEq = 0;
            String contadorSQL = "SELECT COUNT(*) FROM equipo e JOIN resultados r ON r.id_equipo = e.id_equipo WHERE e.nombre_equipo LIKE ? AND r.puntos > 0";
            String instruccionSQL = "SELECT e.id_equipo, e.nombre_equipo, e.region, sum(r.puntos) FROM equipo e JOIN resultados r ON " +
            "r.id_equipo = e.id_equipo WHERE e.nombre_equipo LIKE ? GROUP BY e.nombre_equipo, e.id_equipo ORDER BY sum(r.puntos) DESC;";

            try(PreparedStatement pscont = conexion.prepareStatement(contadorSQL)){
                pscont.setString(1, "%" + textoBuscador + "%");
                ResultSet ejecont = pscont.executeQuery();
                while (ejecont.next()){
                    contadorEq = ejecont.getInt(1);
                }
                ejecont.close();

            }catch(SQLException sqle){
                JOptionPane.showMessageDialog(null, "Se ha producido un error: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
            }

            if(contadorEq == 0){
                String instruccionAlternativa = "SELECT * FROM equipo WHERE nombre_equipo LIKE ?";
                try(PreparedStatement pstmtAlternativa = conexion.prepareStatement(instruccionAlternativa)){
                pstmtAlternativa.setString(1, "%" + textoBuscador + "%");
                ResultSet ejecucionAlternativa = pstmtAlternativa.executeQuery();
                    while (ejecucionAlternativa.next()){
                        id = ejecucionAlternativa.getInt(1);
                        nombre_equipo = ejecucionAlternativa.getString(2);
                        region = ejecucionAlternativa.getInt(3);

                        //imprimir datos en la tabla
                        datosEquipo[0] = Integer.toString(id);
                        datosEquipo[1] = nombre_equipo;
                        datosEquipo[2] = regiones[region-1];
                        BSC.setTablaBuscadorAlt(datosEquipo);
                    }
                }catch(SQLException sqle){
                    JOptionPane.showMessageDialog(null, "Se ha producido un error: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
                }
            }

            try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)){
                pstmt.setString(1, "%" + textoBuscador + "%");
                ResultSet ejecucion = pstmt.executeQuery();

                while (ejecucion.next()){
                    id = ejecucion.getInt(1);
                    nombre_equipo = ejecucion.getString(2);
                    region = ejecucion.getInt(3);
                    puntos = ejecucion.getInt(4);

                    //imprimir datos en la tabla
                    datosEquipo[0] = Integer.toString(id);
                    datosEquipo[1] = nombre_equipo;
                    datosEquipo[2] = regiones[region-1];
                    datosEquipo[3] = Integer.toString(puntos);
                    BSC.setTablaBuscador(datosEquipo, contadorEq);
                }
                ejecucion.close();
            }catch(SQLException sqle){
                JOptionPane.showMessageDialog(null, "Se ha producido un error: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
            }
        }
    }
}
