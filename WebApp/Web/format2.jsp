<HTML>
<HEAD><TITLE>Segunda Respuesta: los pedidos</TITLE></HEAD>
<BODY BGCOLOR="#CCCCCC">
<%@page import="java.util.*"%>
<%@page import="clopez.PedidoBean"%>
<jsp:useBean id="pedido" class="clopez.PedidoBean"/>
<% Vector kk = (Vector) request.getAttribute("Pedidos");%>
<% Integer i = (Integer) request.getAttribute("NumPed"); %>
<H2>El cliente <%= request.getAttribute("Nombre")%> <%= request.getAttribute("Apellido")%> con identificador <%= request.getSession().getAttribute("custid")%> tiene <%= i%> pedidos pendientes:</H2>
<BR>
<CENTER>
<HR WIDTH=80%>
<TABLE WIDTH=100% BORDER=1 CELLPADDING=2>
	<TR>
	<TD>#</TD><TD><B>Num. de Pedido</B></TD>
	<TD><B>Producto</B></TD><TD><B>Proveedor</B></TD>
	<TD><B>Cantidad</B></TD><TD><B>Precio Unitario</B></TD>
	<TD><B>Descuento</B></TD><TD><B>Precio</B></TD>
	</TR>
<% int total=0;%>
<% for (int j=1;j<=i.intValue();j++) { %>
	<% pedido = (PedidoBean) kk.get(j-1); %>
	<TR>
	<TD><B><%= j%></B></TD>
	<TD><%= pedido.getOrderNumber()%></TD>
	<TD><%= pedido.getProducto()%></TD>
	<TD><%= pedido.getProveedor()%></TD>
	<TD ALIGN=CENTER><%= pedido.getCantidad()%></TD>
	<TD ALIGN=RIGHT><%= pedido.getPrecio() %></TD>
	<TD ALIGN=RIGHT><%= pedido.getDescuento()%>%</TD>
	<TD ALIGN=RIGHT><%= pedido.getSubTotal()%></TD>
	</TR>
	<% total=total+pedido.getSubTotal(); %></TD>
<% } %>
	<TR><TD COLSPAN=7 ALIGN=RIGHT><B>TOTAL</B></TD><TD ALIGN=RIGHT><%= total%></TD></TR>
</TABLE>
<HR WIDTH=80%>
<BR>
<FORM METHOD=get ACTION="Actualizar">
<INPUT TYPE=submit VALUE="Actualizar Pedidos" WIDTH=50>
</FORM>
</CENTER>
<BR>
PAGE OK for GRINDER
</BODY>
</HTML>
