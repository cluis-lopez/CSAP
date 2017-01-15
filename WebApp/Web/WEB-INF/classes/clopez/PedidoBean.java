package clopez;

import java.util.*;

public class PedidoBean {

		protected int order_number;
		protected String cliente_nombre;
		protected String cliente_apellido;
		protected String producto;
		protected String proveedor;
		protected int cantidad;
		protected int precio;
		protected int descuento;
		protected Date fecha_entrada;
		protected Date fecha_salida;

	// Constructor

	public PedidoBean (int i, String nom, String ap, String prod, String prov, int cant, int prec, int desc, Date entrada, Date salida) {
		order_number=i;
		cliente_nombre=nom;
		cliente_apellido=ap;
		producto=prod;
		proveedor=prov;
		cantidad=cant;
		precio=prec;
		descuento=desc;
		fecha_entrada=entrada;
		fecha_salida=salida;
	}

	// Constructor vacio (para JavaBeans)

	public PedidoBean () {
		// Do nothing
	}

	// Getters/Setters

	public int getOrderNumber (){
		return order_number;
	}
	public String getClienteNombre (){
		return cliente_nombre;
	}
	public String getClienteApellido(){
		return cliente_apellido;
	}
	public String getProducto (){
		return producto;
	}
	public String getProveedor (){
		return proveedor;
	}
	public int getCantidad (){
		return cantidad;
	}
	public int getPrecio (){
		return precio;
	}
	public int getDescuento (){
		return descuento;
	}
	public Date getEntrada() {
		return fecha_entrada;
	}
	public Date getSalida() {
		return fecha_salida;
	}
	public int getSubTotal() {
		return getCantidad()*(getPrecio()-(getPrecio()*getDescuento()/100));
	}
}
