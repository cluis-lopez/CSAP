import java.io.*;
import java.util.*;

public class Logger {

	public BufferedWriter fd;

	public Logger(String fichero) {
		// Constructor, para inicializar el fichero de log
		try {
			fd = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichero, true)));
		} catch (IOException e) {
			System.err.println("Error al abrir el fichero de log");
			e.printStackTrace();
		}
	}


	public boolean add (String mensaje) {
		// Para añadir una linea al fichero de log
		Date fecha = new Date();
		try {
		fd.write(fecha+" "+mensaje);
		fd.newLine();
		fd.flush();
		return true;
		} catch (IOException e) {
			System.err.println("Error al escribir en el fichero de log");
			e.printStackTrace();
			return false;
		}
	}
}
