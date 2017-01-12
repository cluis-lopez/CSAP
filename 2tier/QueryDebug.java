import java.sql.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class QueryDebug {

	public static void main (String argv[]) {

		int numtran;

		// Parse de los argumentos de entrada

		try{
			numtran = Integer.parseInt(argv[0]);
		} catch (NumberFormatException e) {
			System.err.println("Usage: java Query <numero de transacciones tipo>");
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Usage: java Query <numero de transacciones tipo>");
			return;
		}

		// Lectura de properties

                Properties props = new Properties();
                BufferedInputStream fd;

                try {
                        fd=new BufferedInputStream(new FileInputStream("properties"));
                } catch (FileNotFoundException e) {
                        System.err.println("Fichero de propiedades no encontrado");
                        return;
                }

                try {
                        props.load(fd);
                } catch (IOException e) {
                        System.err.println("No puedo acceder al fichero de propiedades");
                        return;
                }

		// Se crea la query y se inicializa el logger

		Query q = new Query(props.getProperty("log"), 1, true);

		// Se abre la conexion a la BBDD

		Connection con=q.AbreConexion(props.getProperty("driver"), props.getProperty("DBurl"),props.getProperty("user"), props.getProperty("password"));
		if (con == null){
			System.err.println("Error al abrir la BBDD");
			return;
		}

		System.out.println("Abierta la Conexion a la BBDD");

		// Comenzamos el bucle de transacciones


		for (int i=0; i< numtran; i++) {
			System.out.println("===============================================================================");
			System.out.println("Transaccion # "+i);
			int cliente = q.BuscaClienteUnico(con);
			if (cliente != 0) {
				System.out.println("Escogido el cliente con id: "+cliente);
				System.out.println("Borrados "+q.ActualizaPedidos(cliente,con)+" pedidos de este cliente");
				System.out.println("Generados "+q.GeneraPedidos(cliente,con)+" pedidos para este cliente");
			} else {
				System.out.println("No hay clientes con el apellido escogido al azar");
			}
		}

		try {
			con.close();
		} catch (SQLException e) {
			System.err.println("Error al cerra la conexion");
			e.printStackTrace();
		}
			
	}
}
