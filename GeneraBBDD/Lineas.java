import java.io.*;
import java.util.*;

//  Clase generica que genera un Arraylist en el que cada elemento es una linea
// del fichero pasado como parametro

public class Lineas {

	public ArrayList datos = new ArrayList();

	// Constructor, el parametro es el fichero del que se leen las lineas

	public Lineas(String fichero) {
		String line;
		BufferedReader fd;

		try {
			fd=new BufferedReader(new FileReader(fichero));
		} catch (FileNotFoundException e) {
			System.err.println("Fichero no encontrado");
			return;
		}

                try {
                        while((line = fd.readLine()) != null) 
                                datos.add(line);
                } catch (IOException e) {
                        System.err.println("Error de entrada/salida");
			return;
		}
	}
}
