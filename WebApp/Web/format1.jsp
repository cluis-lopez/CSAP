<%@page import="clopez.ClienteBean"%>
<jsp:useBean id="cliente" class="clopez.ClienteBean"/>
<% cliente= (clopez.ClienteBean) request.getAttribute("bean"); %>
<HTML>
<HEAD><TITLE>Primera respuesta</TITLE></HEAD>
<BODY BGCOLOR="#CCCCCC">
<H2>El apellido <%= cliente.getClienteApellido()%> aparece <%= request.getAttribute("coincidencias")%> veces en la BBDD</H2>
<BR>
<FONT SIZE=+1>Se escoge al siguiente cliente:</FONT>
<BR>
<HR WIDTH=80%>
<BR>
<!--
DEBUG (sesion id) <%= request.getSession()%>
<BR>
DEBUG: <%= request.getRequestedSessionId() %>
<BR>
DEBUG: <%= request.getSession().getAttribute("custid") %>
-->
<CENTER>
<TABLE BORDER=1 CELLPADDING=5>
<TR><TD><B>Cust ID</B></TD><TD><%= cliente.getCustId()%></TD></TR>
<TR><TD><B>Nombre</B></TD><TD><%= cliente.getClienteNombre()%></TD></TR>
<TR><TD><B>Apellido</B></TD><TD><%= cliente.getClienteApellido()%></TD></TR>
<TR><TD><B>Empresa</B></TD><TD><%= cliente.getEmpresa()%></TD></TR>
<TR><TD><B>Telefono</B></TD><TD><%= cliente.getTelefono()%></TD></TR>
<TR><TD><B>Pedidos pendientes</B></TD><TD><%= cliente.getPedidosPend()%></TD></TR>
</TABLE>
</CENTER>
<% if (cliente.getPedidosPend()>0) { %>
	<CENTER><HR WIDTH=80%>
	<FORM METHOD=get ACTION="Pedidos">
	<INPUT TYPE=submit VALUE="Mostrar Pedidos" WIDTH=30>
	<HR WIDTH=80%></CENTER>
<%} else { %>
	<CENTER><HR WIDTH=80%>
	<H1> No hay pedidos para este cliente </H1>
	<FORM METHOD=get ACTION="Pedidos">
	<INPUT TYPE=submit VALUE="Generar Pedidos" WIDTH=30>
	<HR WIDTH=80%></CENTER>
<%} %>
PAGE OK for GRINDER
</BODY>
</HTML>
