<HEAD>
	<TITLE>Bienvenida a la aplicacion</TITLE>
</HEAD>
<BODY BGCOLOR="#CCCCCC">
<CENTER>
<TABLE BORDER=1 CELLPADDING=20>
<TR><TD ALIGN=CENTER><FONT FACE=arial SIZE=+5>
Bienvenidos a <BR>Cutre SAP</FONT></TD></TR>
<TR><TD ALIGN=CENTER>(c) CLopez 2005</TD></TR>
<TR><TD><FONT FACE=ARIAL SIZE=+1>En este momento est&aacute;n pendientes: 
<c:set var="pepito" value="cojon" />
<c:out value="${pepito}"/>
<BR>
${3 + 2}
</FONT></TD></TR>
</TABLE>
<P>
<FORM ACTION=Escoge>
<BUTTON  TYPE=SUBMIT><FONT FACE=Arial SIZE=+1 COLOR=RED>Comenzar</FONT></BUTTON>
</FORM>
</CENTER>
</BODY>
