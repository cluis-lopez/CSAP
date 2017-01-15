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

public class Pedidos extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	
	Connection con=null;
	Statement stmt=null;
	ResultSet rs1, rs2;

	int coin=0;
	Vector v = new Vector();

	RequestDispatcher disp=getServletContext().getRequestDispatcher("/format2.jsp");
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

		// Primero buscamos el nombre y apellido del cliente empleando
		// como clave el identificador que se pasa como dato de sesion

		// System.out.println("Buscando a :"+cliente);
		rs1 = stmt.executeQuery("SELECT nombre, apellido FROM clientes WHERE id='"+cliente+"'");
		rs1.next();
		request.setAttribute("Nombre", rs1.getString(1));
		request.setAttribute("Apellido", rs1.getString(2));

		// y ahora buscamos los pedidos asociados a este cliente

		rs2 = stmt.executeQuery("SELECT pedidos.order_number, clientes.nombre, clientes.apellido, productos.nombre, proveedores.nombre, pedidos.cantidad, productos.precio, pedidos.descuento, pedidos.fecha_entrada, pedidos.fecha_salida FROM pedidos, clientes, productos, proveedores WHERE pedidos.cliente='"+cliente+"' AND clientes.id='"+cliente+"' AND productos.id=pedidos.producto AND pedidos.proveedor=proveedores.id");

		while (rs2.next()) {
			coin++;
			PedidoBean ped = new PedidoBean(rs2.getInt(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getInt(6), rs2.getInt(7), rs2.getInt(8), (java.util.Date) rs2.getDate(9), (java.util.Date) rs2.getDate(10));
			v.add(ped);
			
		}

		request.setAttribute("Pedidos", v);
		request.setAttribute("NumPed", new Integer(coin));

		rs1.close();
		rs2.close();
		stmt.close();
		con.close();
		disp.forward(request, response);
	} catch (SQLException e) {
		e.printStackTrace();
	}

    }
}
