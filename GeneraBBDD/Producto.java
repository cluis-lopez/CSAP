import java.util.*;

public class Producto {

	Random r;
	String producto;
	int proveedor_id;
	int precio, descuento;

	// Constructor

	public Producto (Lineas productos, int posicion, int numProveedores){
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			return;
		}
		r=new Random();
		producto= (String) productos.datos.get(posicion);
		proveedor_id= r.nextInt(numProveedores)+1;
		precio=r.nextInt(100000);
		descuento=r.nextInt(50);
	}

}

	
