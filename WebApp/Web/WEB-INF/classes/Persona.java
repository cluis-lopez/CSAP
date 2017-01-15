import java.util.*;

public class Persona {

	Random r;
	String nombre, apellido, password, empresa;
	int i;

	// Constructor

	public Persona(Lineas nombres, int numNombres, Lineas apellidos, int numApellidos, Lineas passwords, int numPasswords, Lineas empresas, int numEmpresas){
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			return;
		}
		r=new Random();
		nombre= (String) nombres.datos.get(r.nextInt(numNombres));
		apellido= (String) apellidos.datos.get(r.nextInt(numApellidos));
		password= (String) passwords.datos.get(r.nextInt(numPasswords));	
		empresa= (String) empresas.datos.get(r.nextInt(numEmpresas));
	}

	// Constructor para generar apellidos aleatorios

	public Persona(Lineas apellidos, int numApellidos){
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			return;
		}
		r=new Random();
		apellido= (String) apellidos.datos.get(r.nextInt(numApellidos));
	}
}

	
