import java.util.*;
import java.io.*;
import java.sql.*;


//  Genera una tabla con la lista de productos: no se puede escoger el tamano 
// desde este programa, esta ligado al tamano del fichero "fichero_productos"

public class GeneraProductos {
	public static void main (String argv[]) {

		Producto proc;
		Proveedor prov;
		Random r;
		int id, insertResults;

		Connection con=null;
		Statement stmt=null;

        	Lineas productos = new Lineas("fichero_productos");
        	Lineas proveedores = new Lineas("fichero_proveedores");

        	int numProductos = productos.datos.size();
		int numProveedores = proveedores.datos.size();


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

		System.out.println("Comienza la insercion de "+numProductos+" registros en la tabla ...");

		try{
			stmt = con.createStatement();

			for(id=1; id<=numProductos; id++) {
				proc = new Producto(productos, id-1, numProveedores);
				//System.err.println("Voy a insertar: id="+id+" producto="+proc.producto+" proveedor="+proc.proveedor_id);
				insertResults= stmt.executeUpdate("INSERT INTO productos (id, nombre, proveedor, precio, descuento) VALUES('"+id+"','"+proc.producto+"','"+proc.proveedor_id+"','"+proc.precio+"','"+proc.descuento+"')");
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
