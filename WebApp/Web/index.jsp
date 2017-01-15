<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<HEAD>
	<TITLE>Bienvenida a la aplicacion</TITLE>
</HEAD>
<BODY BGCOLOR="#003366">
<CENTER>
<TABLE BORDER=0 WIDTH=90%>
<TR><TD><IMG SRC=hplogo.gif></TD><TD ALIGN=right><IMG SRC=javalogo.gif></TD></TR>
</TABLE>
<TABLE BORDER=1 CELLPADDING=20>
<TR><TD ALIGN=CENTER><FONT FACE=arial SIZE=+5 COLOR=FFFFFF>
Bienvenidos a <BR>Cutre SAP</FONT></TD></TR>
<TR><TD ALIGN=CENTER><FONT COLOR=FFFFFF>(c) CLopez 2005</FONT></TD></TR>
<TR><TD ALIGN=CENTER><FONT FACE=Helvetica SIZE=+1 COLOR=#FFFFFF>
Soy el JSP
</FONT></TD></TR>
<TR><TD><FONT FACE=ARIAL SIZE=+1 COLOR=FFFFFF>En este momento est&aacute;n pendientes de procesar: 
<%
	Connection con=null;
        Statement stmt=null;
        int pedidos=0;

        final String dbUrl="jdbc:mysql://localhost:3306/CBS";

        try {

        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        con = DriverManager.getConnection(dbUrl,"root","hp");
        } catch (SQLException e) {
                System.out.println("Fallo al abrir la conexion");
                e.printStackTrace();
        }

        try {
               stmt=con.createStatement();
               ResultSet rs = stmt.executeQuery("SELECT count(*) from pedidos;");
               rs.absolute(1);
               pedidos=rs.getInt(1);
               System.out.println("Pedidos: "+rs);
        } catch (SQLException e) {
               e.printStackTrace();
        }

        try {
             stmt.close();
             con.close();
        } catch (SQLException e) {
             e.printStackTrace();
        }
%>
<%= pedidos%> pedidos
</FONT></TD></TR>
</TABLE>
<P>
<FORM ACTION=Escoge>
<BUTTON  TYPE=SUBMIT><FONT FACE=Arial SIZE=+1 COLOR=RED>Comenzar</FONT></BUTTON>
</FORM>
</CENTER>
</BODY>
