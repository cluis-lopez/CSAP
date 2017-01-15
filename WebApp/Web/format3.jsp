<HTML>
<HEAD><TITLE>Tercera Respuesta: Los pedidos actualizados</TITLE></HEAD>
<BODY BGCOLOR="#CCCCCC">
<%@page import="java.util.*"%>
<%@page import="clopez.PedidoBean"%>
<jsp:useBean id="pedido" class="clopez.PedidoBean"/>
<% Vector v1 = (Vector) request.getAttribute("PedidosBorrados");%>
<% Vector v2 = (Vector) request.getAttribute("PedidosActuales");%>
<% Integer borrados = (Integer) request.getAttribute("numPedidosBorrados"); %>
<% Integer creados = (Integer) request.getAttribute("numPedidosCreados"); %>
<% Integer actual = (Integer) request.getAttribute("numPedidosActuales"); %>
<H2>Hemos cancelado <%= borrados%> pedidos al cliente con identificador <%= request.getSession().getAttribute("custid")%></H2>
<BR>
<H2>Pedidos cancelados:</H2>
<CENTER>
<HR WIDTH=80%>
<TABLE WIDTH=100% BORDER=1 CELLPADDING=2>
	<TR>
	<TD>#</TD><TD><B>Num. de Pedido</B></TD>
	<TD><B>Producto</B></TD><TD><B>Proveedor</B></TD>
	<TD><B>Cantidad</B></TD><TD><B>Precio Unitario</B></TD>
	<TD><B>Descuento</B></TD><TD><B>Precio</B></TD>
	</TR>
<% for (int j=1;j<=borrados.intValue();j++) { %>
	<% pedido = (PedidoBean) v1.get(j-1); %>
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
<% } %>
</TABLE>
<HR WIDTH=80%>
</CENTER>
<BR>
<H2>Se han a&ntilde;adido <%=  creados%> pedidos</H2>
<BR>
<H2>Los pedidos finales son:</H2>
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
<% for (int j=1;j<=actual.intValue();j++) { %>
	<% pedido = (PedidoBean) v2.get(j-1); %>
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
<% } %>
</TABLE>
<HR WIDTH=80%>
<% request.getSession().invalidate(); %>
<FORM METHOD=get ACTION="Escoge">
<INPUT TYPE=submit VALUE="Comenzar de nuevo " WIDTH=50>
</FORM>
</CENTER>
<BR>
PAGE OK for GRINDER
</BODY>
</HTML>
