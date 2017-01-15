import java.util.*;
import java.io.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import java.sql.*;

import clopez.ClienteBean;

public class Escoge extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	

	Persona cliente;

	int coin=0, indice=0;
	Random r;

	Connection con=null;
	Statement stmt=null;

	RequestDispatcher disp=getServletContext().getRequestDispatcher("/format1.jsp");
	String path_apellidos=getServletContext().getRealPath("/WEB-INF/fichero_apellidos");

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
		// Escogemos un apellido de forma aleatoria del fichero 
		
		stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = null;

		while (coin == 0) { // OJO : mientras no encontremos un 
				// cliente valido en la BBDD no saldremos
				// del bucle !!!

		Lineas apellidos=new Lineas(path_apellidos);
		int numApellidos=apellidos.datos.size();
		cliente=new Persona(apellidos, numApellidos);

		rs = stmt.executeQuery("SELECT * from clientes WHERE apellido='"+cliente.apellido+"' ORDER by nombre");;
		while (rs.next()) {
			coin++;
		}

		if (coin >0) { // Existe al menos un cliente con este apellido
			       // asi que escogemos uno de forma aleatoria
			r = new Random();
			indice = r.nextInt(coin)+1;
			rs.absolute(indice);
			ClienteBean cust=new ClienteBean(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(5), rs.getInt(6), rs.getInt(7));
			request.setAttribute("bean", cust);

			HttpSession sesion=request.getSession(true);
			sesion.setAttribute("custid", new Integer(cust.getCustId()));
			request.setAttribute("coincidencias", new Integer(coin));
			break;

		} else { // No existe ningun cliente con este apellido !!
			System.out.println("No existe el cliente con apellido: "+cliente.apellido); // Volvemos a intentarlo con un nuevo cliente !!
		}
		}

		rs.close();
		stmt.close();
		con.close();
		disp.forward(request, response);
	} catch (SQLException e) {
		e.printStackTrace();
	}

    }
}
