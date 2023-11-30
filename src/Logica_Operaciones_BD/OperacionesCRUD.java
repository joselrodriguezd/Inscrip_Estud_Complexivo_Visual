/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica_Operaciones_BD;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author harol
 */
public final class OperacionesCRUD 
{
    private Connection conexion;
    //----- patron de diseño Singleton --------------------------------------------
    private static OperacionesCRUD lainstance = new OperacionesCRUD();
    

    private OperacionesCRUD() 
    {         
    }
    
    public static OperacionesCRUD getInstance()
    {
        return lainstance;
    }
    //-----------------------------------------------------------------------------
    
    private void iniciarConexionBD()//INICIAR LA CONEXIONA LA BD
    {
        this.conexion = ConexionBD.iniciarConexion();
    }
    
    private void cerrarConexionBD() throws SQLException  //CERRAR LA CONEXION A LA BD
    {
        if (this.conexion != null && this.conexion.isClosed() == false) //valida si aun está habierta la conexion BD
        {
            this.conexion.close();
        }
    }    
    //inciso 1.
    public String obtenerCantCarreras() throws SQLException //CONSULTA 1, PARTE 1.
    {
        //1. Conectar a la base de datos
        this.iniciarConexionBD();            
        //2. Variable para almacenar el total de carreras almacenadas en la base de datos.
        String cant_carreras = "0";  //****varia
        //3. definir espacio de trabajo para la declaración y ejecución de la consulta sql
        Statement stm = this.conexion.createStatement();
        //4. Definir el texto String de la consulta SQL.
        String sql = "select count(*) as total_carreras from carreras c";   //***varia
        //String sql = "select * from vista_total_carreras";
        //5. Ejecutar la consulta y amacenar en el objeto ResultSet
        ResultSet tabla = stm.executeQuery(sql);
        //6. Recorrer el objeto ResultSet mediante un while y para cada iteración resolver:
        while(tabla.next()) { 
            //***varia
            cant_carreras = tabla.getString("total_carreras");
        }
        //7. Cerrar la conexion a la base de datos    
        this.cerrarConexionBD();
        //8. Retornar el total de carreras que está almancenado en la base de datos
        return cant_carreras; //***varia
    }
    //inciso 2.
    public Vector<String> obtenerListadoCarreras() throws SQLException //CONSULTA 2
    {
        //1. Conectar a la base de datos
        this.iniciarConexionBD();
        //2. Crear el vector para almacenar la lista de nombres de carreras resultante de la consulta SQL a la base de datos.
        Vector<String> lista_carreras = new Vector<>();
        //complete la lógica del bloque de código que resuelve: 
        //3. definir espacio de trabajo para la declaración y ejecución de la consulta sql
        Statement stm = this.conexion.createStatement();
        //4. Definir el texto String de la consulta SQL.
        String sql = "select c.nombre as carreras from carreras c"; 
        //String sql = "select * from vista_nombres_carreras"; 
        //5. Ejecutar la consulta y amacenar en el objeto ResultSet
        ResultSet tabla = stm.executeQuery(sql);
        //6. Recorrer el objeto ResultSet mediante un while y para cada iteración resolver:
        while(tabla.next()){
            String carrera = tabla.getString("carreras");
            lista_carreras.add(carrera);
        }
        //7. Cerrar la conexion a la base de datos       
        this.cerrarConexionBD();
        //8. Retornar el objeto vector con la lista de nombres de carreras almacenados en la base de datos
        return lista_carreras;
    }  
    //OK
    public Vector<String> obtenerListaParalelos() throws SQLException
    {
        //1. Conectar a la base de datos
        this.iniciarConexionBD();
        //2. Crear el vector para almacenar la lista de códgidos de paralelos resultante de la consulta SQL a la base de datos.
        Vector<String> lista_paralelos = new Vector<>();
        
        //complete la lógica del bloque de código que resuelve:
        //3. definir espacio de trabajo para la declaración y ejecución de la consulta sql
        Statement stm = this.conexion.createStatement();            
        //4. Definir el texto String de la consulta SQL.
        String sql = "select p.nombre as PARALELOS from paralelos p";                
        //5. Ejecutar la consulta y amacenar en el objeto ResultSet
        ResultSet tabla = stm.executeQuery(sql); 
        //6. Recorrer el objeto ResultSet mediante un while y para cada iteración resolver:
        while(tabla.next())
        {                    
            //6.1. Obtener el valor de columna de la fila actual del objeto ResultSet
            String valorColumna = tabla.getString("PARALELOS");                    
            //6.2. Almacenar en el vector creado en el paso 2. el valor de columna de la fila actual del objeto ResultSet
            lista_paralelos.add(valorColumna);
        }  
        //7. Cerrar la conexion a la base de datos
        this.cerrarConexionBD();
        //8. Retornar el objeto vector con la lista de códigos de paralelos almacenados en la base de datos
        return lista_paralelos;        
    }
    //Inciso 3
    public Vector<String> obtenerListaParalelosCarrera(String p_nombre_carrera) throws SQLException
    {
        //1. Conectar a la base de datos
        this.iniciarConexionBD();        
        //2. Crear el vector para almacenar la lista de códgidos de paralelos resultante de la consulta SQL a la base de datos.
        Vector<String> lista_paralelos = new Vector<>();
        //complete la lógica del bloque de código que resuelve:
        //3. definir espacio de trabajo para la declaración y ejecución de la consulta sql
        Statement stm = this.conexion.createStatement();
        //4. Definir el texto String de la consulta SQL.
        String sql = "select\n"
                + " p.nombre as paralelos\n"
                + " from \n"
                + " paralelos p\n"
                + " join carreras c on p.cod_carrera = c.cod_carrera\n"
                + " where \n"
                + " c.nombre = '" + p_nombre_carrera + "'";
        //5. Ejecutar la consulta y amacenar en el objeto ResultSet
        ResultSet tabla = stm.executeQuery(sql);
        //6. Recorrer el objeto ResultSet mediante un while y para cada iteración resolver:
        while(tabla.next()) {
            String nombre_paralelo = tabla.getString("paralelos");
            lista_paralelos.add(nombre_paralelo);
        }
        //7. Cerrar la conexion a la base de datos
        this.cerrarConexionBD();
        //8. Retornar el objeto vector con la lista de códigos de paralelos almacenados en la base de datos
        return lista_paralelos;
    }
    //Inciso 4
    public ArrayList<Vector<String>> obtenerEstudInscritosPorCarrera(String p_nombre_carrera) throws SQLException
    {
        //1. Conectar a la base de datos
        this.iniciarConexionBD();        
        //2. Crear la matriz de datos para almacenar la consulta SQL, cada fila es un Vector de String
        ArrayList<Vector<String>> matriz_registros_estuds = new ArrayList<>();
        //3. definir espacio de trabajo para la declaración y ejecución de la consulta sql
        Statement stm = this.conexion.createStatement();
        //4. Definir el texto String de la consulta SQL.
        String sql = "select\n"
                + " i.cedula as cedula_est,\n"
                + " e.nombres as nombres_est,\n"
                + " e.apellidos as apellidos_est\n"
                + " from\n"
                + " inscripciones_estudiantes i\n"
                + " join estudiantes e on i.cedula = e.cedula\n"
                + " join carreras c on i.cod_carrera = c.cod_carrera\n"
                + " where \n"
                + " c.nombre = '"+p_nombre_carrera+"'";
        //5. Ejecutar la consulta y amacenar en el objeto ResultSet
        ResultSet tabla = stm.executeQuery(sql);
        //6. Recorrer el objeto ResultSet mediante un while y para cada iteración resolver:
        while(tabla.next()){
            Vector<String> registro_fila = new Vector<>();
            String cedula, nombres, apellidos;
            cedula = tabla.getString("cedula_est");
            nombres = tabla.getString("nombres_est");
            apellidos = tabla.getString("apellidos_est");
            registro_fila.add(cedula);
            registro_fila.add(nombres);
            registro_fila.add(apellidos);
            matriz_registros_estuds.add(registro_fila);
        }                      
        //7. Cerrar la conexion a la base de datos
        this.cerrarConexionBD();
        //8. Retornar la matriz de datos con los resultados de la consulta SQL.
        return matriz_registros_estuds;
    } 
    //Inciso 5
    public ArrayList<Vector<String>> obtenerEstudInscritosPorParalelo(String p_nombre_paralelo) throws SQLException
    {
        //1. Conectar a la base de datos
        this.iniciarConexionBD();
        //2. Crear la matriz de datos para almacenar la consulta SQL, cada fila es un Vector de String
        ArrayList<Vector<String>> matriz_registros_est = new ArrayList<>();
        //3. definir espacio de trabajo para la declaración y ejecución de la consulta sql
        Statement stm = this.conexion.createStatement();
        //4. Definir el texto String de la consulta SQL.
        String sql = "select\n"
                + " i.cedula as cedula_est,\n"
                + " e.nombres as nombres_est,\n"
                + " e.apellidos as apellidos_est\n"
                + " from\n"
                + " inscripciones_estudiantes i\n"
                + " join estudiantes e on i.cedula = e.cedula\n"
                + " join paralelos p on p.cod_paralelo = i.cod_paralelo\n"
                + " where\n"
                + " p.nombre = '"+p_nombre_paralelo+"'";
        //5. Ejecutar la consulta y amacenar en el objeto ResultSet
        ResultSet tabla = stm.executeQuery(sql);
        //6. Recorrer el objeto ResultSet mediante un while y para cada iteración resolver:
        while(tabla.next()){
            String cedula = tabla.getString("cedula_est");
            String nombres = tabla.getString("nombres_est");
            String apellidos = tabla.getString("apellidos_est");
            Vector<String> fila_est = new Vector<>();
            fila_est.add(cedula);
            fila_est.add(nombres);
            fila_est.add(apellidos);
            matriz_registros_est.add(fila_est);            
        }        
        //7. Cerrar la conexion a la base de datos
        this.cerrarConexionBD();
        //8. Retornar la matriz de datos con los resultados de la consulta SQL.
        return matriz_registros_est;
    }
    
}
