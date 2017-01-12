import java.util.*;

public class Proveedor {

	Random r;
	String proveedor, pais;
	int i;

	// Constructor

	public Proveedor(Lineas proveedores, int posicion, Lineas paises, int numPaises){
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			return;
		}
		r=new Random();
		proveedor= (String) proveedores.datos.get(posicion);
		pais= (String) paises.datos.get(r.nextInt(numPaises));
	}

}

	
