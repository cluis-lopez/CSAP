import java.io.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import java.sql.*;

public class index extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");

		Connection con = null;
		Statement stmt = null;
		int pedidos = 0;

		// final String dbUrl="jdbc:mysql://localhost:3306/CBS";

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
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("SELECT count(*) from pedidos");
			rs.absolute(1);
			//rs.first();
			pedidos = rs.getInt(1);
			System.out.println("Pedidos: " + rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String BBDD = "Base de datos desconocida";
		
		if (props.getProperty("DBurl").contains("mysql")) {
				BBDD="MySQL";
		} else {
			if (props.getProperty("DBurl").contains("oracle")) {
				BBDD="Oracle";
			}
		}
			
		String pool="No se est&aacute; usando pool de conexiones";
			
		if (props.getProperty("pool").equals("y") || props.getProperty("pool").equals("Y")) {
			pool = "Se está usando el pool de conexiones con nombre JNDI "+props.getProperty("JNDI_Name");
		}
		
		out.println("<HEAD>");
		out.println("	<TITLE>Bienvenida a la aplicacion</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY BGCOLOR=#003366>");
		out.println("<CENTER>");
		out.println("<TABLE BORDER=0 WIDTH=90%>");
		out.println("<TR><TD><IMG SRC=hplogo.gif></TD><TD ALIGN=right>");
		out.println("<IMG SRC=javalogo.gif></TD></TR></TABLE>");
		out.println("<TABLE BORDER=1 CELLPADDING=20>");
		out
				.println("<TR><TD ALIGN=CENTER><FONT FACE=arial SIZE=+5 COLOR=ffffff>");
		out.println("Bienvenidos a <BR>Cutre SAP</FONT></TD></TR>");
		out
				.println("<TR><TD ALIGN=CENTER><FONT COLOR=#FFFFFF>(c) CLopez 2005</FONT></TD></TR>");
		out
				.println("<TR><TD ALIGN=CENTER><FONT FACE=Helvetica SIZE=+1 COLOR=#FFFFFF>Soy el Servlet <I>index</I/>" +
						"<BR>Me estoy conectando a: <I>"+BBDD+"</I>" +
								"<BR>"+pool+"</BR></FONT></TD></TR>");
		out
				.println("<TR><TD ALIGN=CENTER><FONT FACE=ARIAL SIZE=+1 COLOR=ffffff>En este momento hay "+pedidos+" pedidos pendientes");
		out.println("<BR>");
		out.println("</FONT></TD></TR>");
		out.println("</TABLE>");
		out.println("<P>");
		out.println("<FORM ACTION=Escoge>");
		out
				.println("<BUTTON  TYPE=SUBMIT><FONT FACE=Arial SIZE=+1 COLOR=RED>Comenzar</FONT></BUTTON>");
		out.println("</FORM>");
		out.println("</CENTER>");
		out.println("</BODY>");
	}
}
