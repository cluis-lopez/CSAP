import java.util.*;
import java.io.*;
import java.sql.*;


//  Genera una tabla con la lista de pedidos 

public class GeneraOrdenes {
	public static void main (String argv[]) {

		Connection con=null;
		Statement stmt=null;

		// Se cargan las propiedades

		
                Properties props = new Properties();
                BufferedInputStream fd;

                try {
                        fd=new BufferedInputStream(new FileInputStream("properties"));
                } catch (FileNotFoundException e) {
                        System.err.println("Fichero no encontrado");
                        return;
                }

                try {
                        props.load(fd);
                } catch (IOException e) {
                        System.err.println("No puedo acceder al fichero de propiedades");
			return;
                }


		// Abrimos la conexion a la BBDD


		try {

			Class.forName(props.getProperty("driver")).newInstance();
			con = DriverManager.getConnection(props.getProperty("DBurl"), props.getProperty("user"), props.getProperty("password"));
		} catch (SQLException e) {
			System.out.println("No me puedo conectar ...");
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException e) {
			System.out.println("No se puede carga el driver ... Classpath??");
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.out.println("No se puede carga el driver ... Classpath??");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("No se puede carga el driver ... Classpath??");
			e.printStackTrace();
		}

		System.out.println("En este caso no se insertan registros en la tabla...");

	}
}
