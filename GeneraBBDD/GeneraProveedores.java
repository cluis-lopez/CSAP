import java.util.*;
import java.io.*;
import java.sql.*;


//  Genera una tabla con la lista de proveedores: no se puede escoger el tamano 
// desde este programa, esta ligado al tamano del fichero "fichero_proveedores"

public class GeneraProveedores {
	public static void main (String argv[]) {

		Proveedor prov;
		Random r;
		int id, insertResults;

		Connection con=null;
		Statement stmt=null;

        	Lineas proveedores = new Lineas("fichero_proveedores");
        	Lineas pais = new Lineas("fichero_paises");

        	int numProveedores = proveedores.datos.size();
        	int numPaises = pais.datos.size();


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
			System.out.println("No se puede cargar el driver ... Classpath??");
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.out.println("No se puede carga el driver ... Classpath??");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("No se puede carga el driver ... Classpath??");
			e.printStackTrace();
		}

		System.out.println("Comienza la insercion de "+numProveedores+" registros en la tabla ...");

		try{
			stmt = con.createStatement();

			for(id=1; id<=numProveedores; id++) {
				prov = new Proveedor(proveedores, id-1, pais, numPaises);
				r = new Random();
				int telef = r.nextInt(999999999);

				insertResults= stmt.executeUpdate("INSERT INTO proveedores (id, nombre, pais, telefono) VALUES('"+id+"','"+prov.proveedor+"','"+prov.pais+"','"+telef+"')");
				if (id%100 == 0)
					System.out.println("Insertadas "+id+" filas");
			}

		System.out.println("Haciendo commit de la transaccion ...");
		stmt.close();
		con.commit();
		} catch (SQLException e) {
			System.err.println(e);
		}
	}
}
