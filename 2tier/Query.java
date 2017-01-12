import java.sql.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class Query {

	static Random r = new Random();
	static Logger log;
	static int thread;
	static boolean debug=false;

	public Query (String logger, int thread, boolean debug) {
		log = new Logger(logger);
		this.thread=thread;
		this.debug=debug;
	}

	public static Connection AbreConexion(String driver, String url, String usuario, String password) {

		Connection con;

		try {
			Class.forName(driver).newInstance();
			con= DriverManager.getConnection(url, usuario, password);
			con.setAutoCommit(false);
			log.add("Thread: "+thread+": Abierta la conexion a la BBDD");
			return(con);
		} catch (ClassNotFoundException e) {
			System.err.println("No se puede cargar el driver ... classpath ???");
			e.printStackTrace();
			return(null);	
		} catch (InstantiationException e) {
			System.err.println("No se puede cargar el driver ... classpath ???");
			e.printStackTrace();
			return(null);
		} catch (IllegalAccessException e) {
			System.err.println("No se puede cargar el driver ... classpath ???");
			e.printStackTrace();
			return(null);
		} catch (SQLException e ) {
			System.err.println("No se puede cargar el driver ... classpath ???");
			e.printStackTrace();
			return(null);	
		}
		
	}

	/** Escoge un cliente aleatorio de la BBDD.
	* Para ello:
	* <BR>1- Escoge un apellido aleatorio del fichero de apellidos
	* <BR>2- Busca si existe un cliente con ese apellido en la BBDD
	* <BR>3- Si no existe, devuelve el entero "0"
	* <BR>4- Si existe uno o mas de uno, escoge a uno de ellos de forma
	* aleatoria y devuelve el identificador
	* <BR>5- Si se produce un error devuelve el entero "-1"
	*/


	public static int BuscaClienteUnico(Connection con) {
		Lineas apellidos = new Lineas("fichero_apellidos");
		int numApellidos = apellidos.datos.size();
		Persona cliente = new Persona (apellidos, numApellidos);
		if (debug)
			log.add("Thread: "+thread+" Buscando al cliente con apellido: "+cliente.apellido);
		try {
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rset = stmt.executeQuery("SELECT * FROM clientes WHERE apellido='"+cliente.apellido+"' ORDER BY nombre");
			rset.first();
			if (rset.getRow()==0){
				if (debug)
					log.add("Thread: "+thread+" No hay clientes con este apellido");
				return(0);
			} else { // Al menos existe un cliente con este apellido
				// Calculamos cuantas coincidencias hay
				rset.last();
				int coinc = rset.getRow();
				if (debug)
					log.add("Thread: "+thread+" Hay "+coinc+" clientes con este apellido");
				rset.absolute(r.nextInt(coinc)+1);
				int id=rset.getInt("id");
				if (debug)
					log.add("Thread: "+thread+" Escogido el cliente con identificador "+id);
				rset.close();
				stmt.close();
				return(id);
			}
		} catch (SQLException e) {
			System.err.println("Error en la operacion sobre la BBDD");
			e.printStackTrace();
			return(-1);
		}
	}


	// ActualizaPedidos : extrae los pedidos pendientes para este cliente
	// Si hay algun pedido pendiente, borra un numero aleatorio de ellos
	// Devuelve el numero de pedidos borrados

	public static int ActualizaPedidos(int cliente_id, Connection con){
		try {
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rset = stmt.executeQuery("SELECT * FROM pedidos WHERE cliente='"+cliente_id+"' FOR UPDATE");
			rset.first();
			if (rset.getRow() == 0) {
				if (debug)
					log.add("Thread: "+thread+" No hay pedidos pendientes para este cliente");
				rset.close();
				stmt.close();
				return (0);
			} else { // Al menos existe un pedido
				rset.last();
				int coinc = rset.getRow();
				if (debug)
					log.add("Thread: "+thread+" Existen "+coinc+" pedidos para este cliente");
				int borrar = r.nextInt(coinc);
				if (debug)
					log.add("Thread: "+thread+" Se cancelaran "+borrar+" pedidos");
				stmt.executeUpdate("DELETE FROM pedidos WHERE cliente="+cliente_id+" LIMIT "+borrar+"");
				stmt.executeUpdate("UPDATE clientes SET pedidos_pendientes='"+(coinc-borrar)+"' WHERE id='"+cliente_id+"'");
				con.commit();
				rset.close();
				stmt.close();
				return (borrar);
			}
		} catch (SQLException e) {
			//System.err.println("Error en la operacion sobre la BBDD");
			//System.err.println("Error en el acceso a la BBDD");
			//System.err.println("Mensaje:"+e.getMessage());
			//System.err.println("Estado :"+e.getSQLState());
			//System.err.println("Error code:"+e.getErrorCode());
			//e.printStackTrace();
			// Exclusivo MySQL ... probar con Oracle
			if (e.getErrorCode() == 1213) { //Deadlock detectado transaccion abortada 
				if (debug)
					log.add("Thread: "+thread+" Transaccion abortada !!!!");
				return(0);
			}
			return (-1);
		}

	}


	// GeneraPedidos : genera un numero aleatorio de pedidos (0<pedidos<5) 
	// para el cliente con identificador cliente_id
	// Devuelve un entero : numero de inserciones realizadas o 0 si no se generan pedidos

	public static int GeneraPedidos(int cliente_id, Connection con){
		try {
			String fecha_pedido=null;
			String fecha_entrega=null;
			int ultimoPedido=0;

			Lineas productos = new Lineas("fichero_productos");
			int numProductos = productos.datos.size();

			int numped = r.nextInt(4)+1; // El numero de pedidos a generar
			if (debug)
				log.add("Thread: "+thread+" Se generaran "+numped+" pedidos para este cliente");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

			// Vemos los pedidos pendientes que tiene este cliente
			ResultSet rset = stmt.executeQuery("SELECT pedidos_pendientes FROM clientes WHERE id='"+cliente_id+"'");
			rset.first();
			int pedidos_pendientes = rset.getInt("pedidos_pendientes");

			for (int i=0; i< numped; i++){
				// Genera fechas aleatorias para el pedido y la entrega
				fecha_pedido = (Integer.toString(r.nextInt(4)+2000)+"-"+Integer.toString(r.nextInt(12)+1)+"-"+Integer.toString(r.nextInt(28)+1));
				fecha_entrega = (Integer.toString(r.nextInt(4)+2000)+"-"+Integer.toString(r.nextInt(12)+1)+"-"+Integer.toString(r.nextInt(28)+1));

				// Seleccionamos un producto aleatorio
				int producto_aleatorio=r.nextInt(numProductos)+1;
				rset = stmt.executeQuery("SELECT id, proveedor, precio, descuento FROM productos WHERE id='"+producto_aleatorio+"'");
				rset.first();
				// Se genera(n) los pedidos
				stmt.executeUpdate("INSERT INTO pedidos VALUES(NULL,'"+cliente_id+"','"+rset.getInt("id")+"','"+rset.getInt("proveedor")+"','"+(r.nextInt(9)+1)+"','"+rset.getInt("descuento")+"','"+fecha_pedido+"','"+fecha_entrega+"')");
			}
			// Se actualiza el campo "pedidos_pendientes" de la tabla de clientes
			stmt.executeUpdate("UPDATE clientes SET pedidos_pendientes='"+(pedidos_pendientes+numped)+"' WHERE id='"+cliente_id+"'");
			con.commit();
			if (debug)
				log.add("Thread: "+thread+" Generados: "+numped+" pedidos");
			rset.close();
			stmt.close();
			return (numped);
		} catch (SQLException e) {
			System.err.println("Error en el acceso a la BBDD");
			System.err.println("Mensaje:"+e.getMessage());
			System.err.println("Estado :"+e.getSQLState());
			System.err.println("Error code:"+e.getErrorCode());
			e.printStackTrace();
			// Exclusivo MySQL ... probar con Oracle
			if (e.getErrorCode() == 1213) { //Deadlock detectado transaccion abortada 
				if (debug)
					log.add("Thread: "+thread+" Transaccion abortada !!!!");
				return(0);
			}
			return (-1);
		}
	}
}
