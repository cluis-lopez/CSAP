package clopez;

public class ClienteBean {

		// Este Bean representa a un cliente contiene las mismas
		// definiciones que la tabla de la BBDD "clientes"
		// escepto la password

		protected int cust_id;
		protected String cliente_nombre;
		protected String cliente_apellido;
		protected String empresa;
		protected int telefono ;
		protected int pedidos_pendientes;

	// Constructor

	public ClienteBean (int i, String nom, String ap, String emp, int telf, int ped) {
		cust_id=i;
		cliente_nombre=nom;
		cliente_apellido=ap;
		empresa=emp;
		telefono=telf;
		pedidos_pendientes=ped;
	}

	// Constructor vacio (para JavaBeans)

	public ClienteBean () {
		// Do nothing
	}

	// Getters/Setters

	public int getCustId () {
		return cust_id;
	}
	public String getClienteNombre (){
		return cliente_nombre;
	}
	public String getClienteApellido(){
		return cliente_apellido;
	}
	public String getEmpresa (){
		return empresa;
	}
	public int getTelefono (){
		return telefono;
	}
	public int getPedidosPend (){
		return pedidos_pendientes;
	}
}
