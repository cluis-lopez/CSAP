import java.util.*;
import java.io.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import java.sql.*;

import clopez.PedidoBean;

public class Actualizar extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	
	Connection con=null;
	Statement stmt=null, stmt1=null;
	ResultSet rs;

	int numped=0; // Los pedidos actuales
	int borrar=0; // Los pedidos que borramos de la tablas
	int crear=0;  // Los pedidos nuevos que generamos 
	int descuento;
	
	Vector v1 = new Vector();
	Vector v2 = new Vector();
	
	String fecha_entrega=null;
	String fecha_pedido=null;

	Random r;

	String path_productos=getServletContext().getRealPath("/WEB-INF/fichero_productos");
	Lineas productos = new Lineas(path_productos);
	int numProductos = productos.datos.size();

	RequestDispatcher disp=getServletContext().getRequestDispatcher("/format3.jsp");
	HttpSession sesion = request.getSession();

	// Se abre la conexion a la BBDD

	Properties props = new Properties();
	BufferedInputStream fd;

	try {
		fd = new BufferedInputStream(new FileInputStream(
				getServletContext().getRealPath("/WEB-INF/properties")));
	} catch (FileNotFoundException e) {
		System.err.println("Fichero de propiedades no encontrado");
		return;
	}

	try {
		props.load(fd);
	} catch (IOException e) {
		System.err.println("No se puede abrir el fichero de propiedades");
		return;
	}

	if (props.getProperty("pool").equals("N") || props.getProperty("pool").equals("n")) {
		try {
			Class.forName(props.getProperty("driver")).newInstance();
			con = DriverManager.getConnection(props.getProperty("DBurl"), props
					.getProperty("user"), props.getProperty("password"));
			con.setAutoCommit(false);
		} catch (SQLException e) {
			System.err
					.println("Problemas al abrir la conexión con la BBDD");
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.err
					.println("Problemas al abrir la conexión con la BBDD");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err
					.println("Problemas al abrir la conexión con la BBDD");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err
					.println("No se puede cargar el driver ... classpath ???");
			e.printStackTrace();
		}
		} else {
			try {
				// System.err.println("Nombre JNDI. "+props.getProperty("JNDI_Name"));
				Hashtable ht = new Hashtable();
				Context ctx = null;
				if (! props.getProperty("CONTEXT_INIT").equals(""))
				{
					ht.put(Context.INITIAL_CONTEXT_FACTORY,props.getProperty("CONTEXT_INIT"));
					ht.put(Context.PROVIDER_URL,props.getProperty("CONTEXT_URL"));
				}
				ctx = new InitialContext(ht);
				DataSource ds = (DataSource) ctx.lookup(props.getProperty("JNDI_Name"));
				con = ds.getConnection();
				con.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NamingException e) {
				e.printStackTrace();
			}

		}
	
	try {
		// Mostramos todos los pedidos pendientes para el cliente

		int cliente = ((Integer)sesion.getAttribute("custid")).intValue();
		stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		stmt1=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

		//  Buscamos el numero de pedidos correspondientes 
		// al cliente identificado por custdid

		rs = stmt.executeQuery("SELECT * FROM pedidos WHERE cliente='"+cliente+"'FOR UPDATE");
		rs.first();
		if (rs.getRow()==0) {
			numped=0;
		} else {
			rs.last();
			numped=rs.getRow();
		}

		// System.out.println("El cliente con ID "+cliente+" tiene "+numped+" pedidos pendientes");
		request.setAttribute("numPedidosBorrados", new Integer(borrar));

		if (numped>0) { // Como hay pedidos, seleccionamos un numero
				// aleatorio de ellos y los borramos

				r = new Random();
				borrar = r.nextInt(numped)+1;
				rs = stmt.executeQuery("SELECT pedidos.order_number, clientes.nombre, clientes.apellido, productos.nombre, proveedores.nombre, pedidos.cantidad, productos.precio, pedidos.descuento, pedidos.fecha_entrada, pedidos.fecha_salida FROM pedidos, clientes, productos, proveedores WHERE pedidos.cliente='"+cliente+"' AND clientes.id='"+cliente+"' AND productos.id=pedidos.producto AND pedidos.proveedor=proveedores.id");
				
				for (int j=1; j<=borrar; j++) {
					rs.absolute(j);
					// Generamos el vector v1 de Beans pasar al JSP
					PedidoBean ped = new PedidoBean(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), (java.util.Date) rs.getDate(9), (java.util.Date) rs.getDate(10));
					v1.add(ped);
					stmt1.executeUpdate("DELETE FROM pedidos WHERE cliente="+cliente+" AND order_number="+rs.getInt(1)+"");
				}
				request.setAttribute("PedidosBorrados", v1);
				request.setAttribute("numPedidosBorrados", new Integer(borrar));
				//System.err.println("Voy a ejecutar: DELETE FROM pedidos WHERE cliente="+cliente+" LIMIT "+borrar+"");
				//stmt.executeUpdate("DELETE FROM pedidos WHERE cliente="+cliente+" LIMIT "+borrar+"");
				stmt.executeUpdate("UPDATE clientes SET pedidos_pendientes="+(numped-borrar)+" WHERE id='"+cliente+"'");
		}

		con.commit();
		
		// Ahora generamos "n" pedidos pendientes para este cliente

		r = new Random();
		crear = r.nextInt(6);
		request.setAttribute("numPedidosCreados", new Integer(crear));

		for (int i=0;i<crear;i++) { // Como maximo 5 pedidos

			r = new Random();
			rs = stmt.executeQuery("SELECT * FROM productos WHERE id='"+(r.nextInt(numProductos)+1)+"'"); // se selecciona un producto al azar
			rs.first();

			if (rs.getInt(5)!=0) {
				rs.first();
				descuento=r.nextInt(rs.getInt(5));
			} else {
				descuento=0;
			} // Asignamos un descuento al azar al producto

			int cantidad=r.nextInt(5)+1; //Maximo 5 items en 
						// mismo pedido

			// Se generan fechas aleatorias de salida y entrega
			fecha_pedido = (Integer.toString(r.nextInt(4)+2000)+"-"+Integer.toString(r.nextInt(11)+1)+"-"+Integer.toString(r.nextInt(27)+1));
			fecha_entrega = (Integer.toString(r.nextInt(4)+2000)+"-"+Integer.toString(r.nextInt(11)+1)+"-"+Integer.toString(r.nextInt(27)+1));

			rs.first(); // Volvemos a inicializar el puntero

			try { // Inserciones en la tabla de pedidos
				stmt.executeUpdate("INSERT INTO pedidos VALUES(NULL,"+cliente+","+rs.getInt(1)+","+rs.getInt(3)+","+cantidad+","+descuento+",'"+fecha_pedido+"','"+fecha_entrega+"')");
			} catch (SQLException e) {
				System.err.println("Error en el INSERT");
				e.printStackTrace();
				return;
			}
			// Actualiza el campo de pedidos pendientes en la tabla
			// de clientes
			stmt.executeUpdate("UPDATE clientes SET pedidos_pendientes="+(numped-borrar+crear)+" WHERE id='"+cliente+"'");
			//  Commit de la transaccion
			con.commit();
		}
				
		// Finalmente le pasamos al JSP un vector compuesto de JavaBeans
		// conteniendo el listado actualizado de pedidos

		rs = stmt.executeQuery("SELECT pedidos.order_number, clientes.nombre, clientes.apellido, productos.nombre, proveedores.nombre, pedidos.cantidad, productos.precio, pedidos.descuento, pedidos.fecha_entrada, pedidos.fecha_salida FROM pedidos, clientes, productos, proveedores WHERE pedidos.cliente='"+cliente+"' AND clientes.id='"+cliente+"' AND productos.id=pedidos.producto AND pedidos.proveedor=proveedores.id");
		numped=0;
		while (rs.next()) {
			PedidoBean ped = new PedidoBean(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), (java.util.Date) rs.getDate(9), (java.util.Date) rs.getDate(10));
			numped++;
			v2.add(ped);
		}

		request.setAttribute("PedidosActuales", v2);
		request.setAttribute("numPedidosActuales", new Integer(numped));

		rs.close();
		stmt.close();
		con.close();
		disp.forward(request, response);

	} catch (SQLException e) {
		System.err
		.println("Error en el acceso a la BBDD: posible deadlock");
		System.err.println("Mensaje: " + e.getMessage());
		System.err.println("Estado: " + e.getSQLState());
		System.err.println("Error core: " + e.getErrorCode());
		e.printStackTrace();
	}
}
}

