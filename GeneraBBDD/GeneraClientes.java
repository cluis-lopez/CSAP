import java.util.*;
import java.io.*;
import java.sql.*;

public class GeneraClientes {
	public static void main (String argv[]) {

		Persona per;
		Random r;
		int num, id, insertResults;

		Connection con=null;
		Statement stmt=null;

        	Lineas nombres = new Lineas("fichero_nombres");
        	Lineas apellidos = new Lineas("fichero_apellidos");
       		Lineas passwords = new Lineas("fichero_passwords");
		Lineas empresas = new Lineas("fichero_empresas");

        	int numNombres = nombres.datos.size();
        	int numApellidos = apellidos.datos.size();
        	int numPasswords = passwords.datos.size();
		int numEmpresas = empresas.datos.size();

		try {
			num=Integer.parseInt(argv[0]);
		} catch (NumberFormatException e) {
			System.err.println("Numero de lineas invalido");
			return;
		}

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

		System.out.println("Comienza la insercion de "+num+" registros en la tabla ...");

		try{
			stmt = con.createStatement();

			for(id=1; id<num+1; id++) {
				per = new Persona(nombres, numNombres, apellidos, numApellidos, passwords, numPasswords, empresas, numEmpresas);
				if (per.password.indexOf(' ')>1)
                                	per.password=per.password.substring(0,per.password.indexOf(' '));
				if (per.password.indexOf('\'')>1)
                                	per.password=per.password.substring(0,per.password.indexOf('\''));
				if (per.empresa.indexOf('\'') != -1) per.empresa="Apostrofe S.A.";
				r = new Random();
				int telef = r.nextInt(999999999);
				

				// System.out.println("INSERT INTO clientes (nombre, apellido, password, empresa, telefono, pedidos_pendientes) VALUES('"+per.nombre+"','"+per.apellido+"','"+per.password+"','"+per.empresa+"','"+telef+"', 0)");

				insertResults= stmt.executeUpdate("INSERT INTO clientes (id, nombre, apellido, password, empresa, telefono, pedidos_pendientes) VALUES('"+id+"','"+per.nombre+"','"+per.apellido+"','"+per.password+"','"+per.empresa+"','"+telef+"', 0)");
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
