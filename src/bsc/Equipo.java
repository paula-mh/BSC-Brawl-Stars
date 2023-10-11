/**
 * @author paulamh
 */
package bsc;

import java.sql.*;
import java.util.*;
import javax.swing.*;

public class Equipo {
    
    private static ArrayList <Equipo> listaEquipo = new ArrayList <>();  //creo un arraylist para almacenar los objetos de tipo Equipo
    private ArrayList<Resultados> listaResultados = Resultados.getResultados();  //coge la información del arraylist de Equipos
    private static Conexion conexionBD = Conexion.getInstancia();
    private static Connection conexion = conexionBD.getConexion();
    
    //variables de equipo
    private Equipo equipo;
    private int id = 1;
    private String nombre_equipo;
    private static int region;
    
    //variables del programa
    private static int equiposImportadosDB;
    private static int equiposExportadosDB;
    public static final String[] regiones = {"Sudeste de asia (SEA)", "India", "Este de Asia (EA)",
        "Europa, Medio Oriente y África (EMEA)", "Sudamérica Este (SA EAST)", "Sudamérica Oeste (SA WEST)",
        "Norteamérica Este (NA EAST)", "Norteamérica Oeste (NA WEST)", "China Continental"};
    private final Icon icono = new ImageIcon(getClass().getResource("/images/bsc_100x100.png"));
    private final Icon iconoError = new ImageIcon(getClass().getResource("/images/error_bsc.png"));
    
    
    public static ArrayList<Equipo> getEquipos() {
        return listaEquipo;
    }
    
    public Equipo(){}
    
    public Equipo (int id, String nombre_equipo, int region){ //método constructor con los atributos
        this.id = id;
        this.nombre_equipo = nombre_equipo;
        this.region = region;
    }
    
    //getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre_equipo;
    }
    public void setNombre(String nombre_equipo) {
        this.nombre_equipo = nombre_equipo;
    }

    public int getRegion() {
        return region;
    }

    
    public static int getRegiones(String regionS){
        switch (regionS) {
            case "Sudeste de asia (SEA)": region = 1; break;
            case "India": region = 2; break;
            case "Este de Asia (EA)": region = 3; break;
            case "Europa, Medio Oriente y África (EMEA)": region = 4; break;
            case "Sudamérica Este (SA EAST)": region = 5; break;
            case "Sudamérica Oeste (SA WEST)": region = 6; break;
            case "Norteamérica Este (NA EAST)": region = 7; break;
            case "Norteamérica Oeste (NA WEST)": region = 8; break;
            case "China Continental": region = 9; break;
            default: break;
        }
        return region;
    }
    
    
    public void crearEquipo() { //método para crear un nuevo equipo
        importaDatosEquipo();
        String regionS = "";
        try{
            id++;
            nombre_equipo = (String) JOptionPane.showInputDialog(null, "Inserte el nombre del equipo", "Crear nuevo equipo", JOptionPane.QUESTION_MESSAGE, icono, null, null);
            if (nombre_equipo != null){
                regionS = (String) JOptionPane.showInputDialog(null, "Selecciona la región de " + nombre_equipo, "Crear nuevo equipo", JOptionPane.QUESTION_MESSAGE, icono, regiones, regiones[0]);
                region = getRegiones(regionS);
                if((nombre_equipo != null) && (region != 0)){
                    equipo = new Equipo(id, nombre_equipo, region);
                    listaEquipo.add(equipo);
                    exportaDatosEquipo(nombre_equipo, region);
                }
            }
        }catch(NullPointerException npe){
            return;
        }
    }
    
    
    public void mostrarEquiposRegiones(){  //muestra por pantalla todos los equipos almacenados
        importaDatosEquipo();
        String regionS = "";
        try{
            regionS = (String) JOptionPane.showInputDialog(null, "Selecciona una región", "Mostrar equipos", JOptionPane.QUESTION_MESSAGE, icono, regiones, regiones[0]);
            region = getRegiones(regionS);
            importaEquiposRegion(region);
        }catch(NullPointerException npe){
            return;
        }
    }
    
    
    public void modificarEquipo() { //modifica el nombre de un equipo existente
        importaDatosEquipo();
        String nuevoNombre = "";
        String regionS = "";
        try{
            regionS = (String) JOptionPane.showInputDialog(null, "Selecciona la región a la que pertenece", "Modificar equipo", JOptionPane.QUESTION_MESSAGE, icono, regiones, regiones[0]);
            region = getRegiones(regionS);
            String arrayEquipos[] = cargarEquipoRegionArray(region);
            nombre_equipo = (String) JOptionPane.showInputDialog(null, "Selecciona el equipo que deseas modificar", "Modificar equipo", JOptionPane.QUESTION_MESSAGE, icono, arrayEquipos, arrayEquipos[0]);
            if(nombre_equipo != null){
                nuevoNombre = (String) JOptionPane.showInputDialog(null, "Inserta el nuevo nombre para " + nombre_equipo, "Modificar equipo", JOptionPane.QUESTION_MESSAGE, icono, null, null);
            }
            for (Equipo e: listaEquipo) {
                if ((nombre_equipo.equals(e.getNombre())) && (nuevoNombre != null)){
                    id = e.getId();
                    e.setNombre(nuevoNombre);
                    if(modificarEquipoDB(nuevoNombre, id) == false){
                        return;
                    }
                }
            }
        }catch(NullPointerException npe){
            return;
        }catch(ArrayIndexOutOfBoundsException aiobe){
            return;
        }
    }
    

    public void eliminarEquipo(){ //elimina un equipo existente que no tenga resultados asociados
        importaDatosEquipo();
        String regionS = "";
        try{
            regionS = (String) JOptionPane.showInputDialog(null, "Selecciona la región a la que pertenece", "Eliminar equipo", JOptionPane.QUESTION_MESSAGE, icono, regiones, regiones[0]);
            if(regionS != null){
                region = getRegiones(regionS);
                String arrayEquipos[] = cargarEquipoRegionArray(region);
                nombre_equipo = (String) JOptionPane.showInputDialog(null, "Selecciona el equipo que deseas eliminar ", "Eliminar equipo", JOptionPane.QUESTION_MESSAGE, icono, arrayEquipos, arrayEquipos[0]);
                if (compruebaEquipoResultados(nombre_equipo)){
                    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro que deseas eliminar: " + nombre_equipo + "?", "Eliminar equipo", JOptionPane.YES_NO_OPTION, 0, icono);
                    if(confirmacion == 0){
                        for (Equipo e: listaEquipo) {
                            if (nombre_equipo.equals(e.getNombre())){
                                id = e.getId();
                                if(listaEquipo.remove(e)){
                                    eliminarEquipoDB(id);
                                }
                            }
                        }
                   } 
                }else{
                    JOptionPane.showMessageDialog(null, "No se puede eliminar un equipo que tiene resultados almacenados", "Error", JOptionPane.ERROR_MESSAGE, iconoError);
                }
            }   
        }catch(ConcurrentModificationException cme){
        }catch(NullPointerException npe){
            return;
        }catch (ArrayIndexOutOfBoundsException aiobe){
            return;
        }
    }
    

    //métodos que interactúan con la base de datos
    public String[] cargarEquipoRegionArray(int region){
        int i = -1;
        int totalEquiposRegion = contadorEquiposRegion(region);
        String equipos[] = new String[totalEquiposRegion];
        String instruccionSQL = "SELECT * FROM equipo WHERE region = ?";
        
        try{
            PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL);
            pstmt.setInt(1, region);
            ResultSet ejecucion = pstmt.executeQuery();
            while (ejecucion.next()){
                i++;
                nombre_equipo = ejecucion.getString(2);
                equipos[i] = nombre_equipo;
            }
            ejecucion.close();
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al cargar los equipos: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }catch (ArrayIndexOutOfBoundsException aiobe){}
        return equipos;
    }
    
    
        
    //para comprobar si los equipos tienen resultados almacenados antes de eliminarlos
    public boolean compruebaEquipoResultados(String nombre_equipo){
        boolean noExiste = false;
        int contador = 0;
        String instruccionSQL = "SELECT COUNT(*) FROM resultados r JOIN equipo e ON r.id_equipo = e.id_equipo WHERE e.nombre_equipo = ?;";  //sentencia SQL
        try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)){
            pstmt.setString(1, nombre_equipo);
            ResultSet ejecucion = pstmt.executeQuery();
            
            while (ejecucion.next()){
                contador = ejecucion.getInt(1);
            }
            ejecucion.close();
            if (contador == 0){
               noExiste = true; 
            }else{
                noExiste = false;
            }  
            
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Se ha producido un error: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
        return noExiste;  
    }
    
    
    
    public void exportaDatosEquipo(String nombre_equipo, int region) {  //exporta los datos de los equipos a la base de datos
        try{
            String instruccionSQL = "INSERT INTO equipo (nombre_equipo, region) VALUES (?, ?)";
            PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL);
            pstmt.setString(1, nombre_equipo);
            pstmt.setInt(2, region);
            pstmt.executeUpdate();

            equiposImportadosDB++;
            equiposExportadosDB++;
            JOptionPane.showMessageDialog(null, "Equipo: " + nombre_equipo + " añadido correctamente", "Crear nuevo equipo", JOptionPane.INFORMATION_MESSAGE, icono);
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al crear equipo: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
    }
    
    
    
    public void importaDatosEquipo() { //importa datos del equipo
        String instruccionSQL = "SELECT * FROM equipo";
        try (PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL);
             ResultSet ejecucion = pstmt.executeQuery()) {

            while (ejecucion.next()) {
                id = ejecucion.getInt(1);
                nombre_equipo = ejecucion.getString(2);
                region = ejecucion.getInt(3);

                equipo = new Equipo(id, nombre_equipo, region); 
                listaEquipo.add(equipo);
            }
            ejecucion.close();
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Error al importar datos de los equipos: " + sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
    }
    
    

    public void importaEquiposRegion(int region){  //importa los datos de los equipos por regiones
        String datosEquipo[] = new String[3];
        String instruccionSQL = "SELECT * FROM equipo WHERE region = ?"; //sentencia SQL
        try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)){
            pstmt.setInt(1, region);
            ResultSet ejecucion = pstmt.executeQuery();
            while (ejecucion.next()){
                id = ejecucion.getInt(1);
                nombre_equipo = ejecucion.getString(2);
                
                //imprimir datos en la tabla
                datosEquipo[0] = Integer.toString(id);
                datosEquipo[1] = nombre_equipo;
                datosEquipo[2] = regiones[region-1];
                BSC.setTablaEquipos(datosEquipo, region); 
            }
            ejecucion.close();
                    
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al importar datos de los equipos: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
        }
    }
    
    
    public boolean modificarEquipoDB(String nuevoNombre, int id){
        boolean valido;
        String instruccionSQL = "UPDATE equipo SET nombre_equipo = ? WHERE id_equipo = ?";  //sentencia SQL para actualizar nombre
    try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)){
            pstmt.setString(1, nuevoNombre);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            valido = true;
            JOptionPane.showMessageDialog(null, "Equipo modificado correctamente", "Modificar equipo", JOptionPane.INFORMATION_MESSAGE, icono);
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al modificar equipo: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
            valido = false;
        }
        return valido;
    }
    
    public void eliminarEquipoDB(int id){
        String instruccionSQL = "DELETE FROM equipo WHERE id_equipo = ?";   //sentencia SQL para eliminar equipo
        try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)){
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Equipo eliminado correctamente", "Eliminar equipo", JOptionPane.INFORMATION_MESSAGE, icono);
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al eliminar equipo: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE, iconoError);
            return;
        }catch (ArrayIndexOutOfBoundsException aiobe){
            return;
        }
    }
    
    
    
    public static int contadorEquiposRegion(int region){
        int totalEquiposRegion = 0;
        //contar el número de equipos de cada región
        String instruccionSQL = "SELECT COUNT(*) FROM equipo WHERE region = ?";
        try(PreparedStatement pstmt = conexion.prepareStatement(instruccionSQL)){
            pstmt.setInt(1, region);
            ResultSet ejecucion = pstmt.executeQuery();
            while (ejecucion.next()){
                totalEquiposRegion = ejecucion.getInt(1);
            }
            ejecucion.close();
            
            if (totalEquiposRegion == 0){
                JOptionPane.showMessageDialog(null, "No hay ningún equipo registrado en esa región" , "Error", JOptionPane.ERROR_MESSAGE);
                return 0;
            }
            
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(null, "Error al leer los equipos: " + sqle.getMessage() , "Error", JOptionPane.ERROR_MESSAGE);
        }
        return totalEquiposRegion;
    }
}