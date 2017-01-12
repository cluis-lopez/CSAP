import java.io.*;
import java.util.*;

public class MultiThread {

	String driver, url, usuario, password;
	int thinktime;

	public static void main(String argv[]) {

		int numthreads=0;
		int thinktime=1000;
		int rampa=1000;
		Contador cont;

		// Parse de los argumentos de entrada

		try {
			numthreads=Integer.parseInt(argv[0]);
			thinktime=Integer.parseInt(argv[1]);
			rampa=Integer.parseInt(argv[2]);
		} catch (NumberFormatException e) {
			System.err.println("Usage: java QueryThreads <numero de threads> <thinktime en milisegundos> <rampa en milisegundos>");
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Usage: java QueryThreads <numero de threads> <thinktime en milisegundos> <rampa en milisegundos>");
			return;
		}

		// Inicializacion del fichero de propiedades

		Properties props = new Properties();
                BufferedInputStream fd;

                try {
                        fd=new BufferedInputStream(new FileInputStream("properties"));
                } catch (FileNotFoundException e) {
                        System.err.println("Fichero de propiedades no encontrado");
                        return;
                }

                try {
                        props.load(fd);
                } catch (IOException e) {
                        System.err.println("No puedo acceder al fichero de propiedades");
                        return;
		}

		// Se lanzan los threads 

		System.out.println("Se lanzan los "+numthreads+" threads.");
		System.out.println("A partir de ahora detalles en el log:"+props.getProperty("log"));
		System.out.println("==============================================================");
		cont = new Contador(numthreads);
		new Imprimidor(cont).start();

		for (int i=0; i< numthreads; i++) {

			new QueryThreads(cont, props.getProperty("driver"), props.getProperty("DBurl"), props.getProperty("user"), props.getProperty("password"), thinktime, i, props.getProperty("log"),props.getProperty("debug")).start();
			cont.Incrementa();
//			System.out.println("Lanzado el Thread #"+i);

			try {
				Thread.sleep(rampa);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
