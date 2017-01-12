import java.sql.Connection;
import java.util.Random;

public class QueryThreads extends Thread {

	String driver, url, usuario, password, logfile;
	int thinktime, thread_id;
	long tinicio;
	float tintervalo;
	boolean debug;
	Contador cont;

	public void run() {
		// Definicion de variables y contadores

		Random r = new Random();
		Query q = new Query(logfile, thread_id, debug);

		// Se abre la conexion a la BBDD
		// CADA THREAD ABRE UNA CONEXION !!
		Connection con=q.AbreConexion(driver, url, usuario, password);
		if (con == null) {
			System.err.println("Thread "+thread_id+": Error al abrir la BBDD");
			return;
		}


		tinicio=System.currentTimeMillis();
		int k=1;

		while(true) {
		k++;
			int cliente = q.BuscaClienteUnico(con);
			if (cliente != 0) {
				q.ActualizaPedidos(cliente, con);
				q.GeneraPedidos(cliente,con);
			}
				
			if(k%50 ==0) {
				// Se para el contador de tiempo
				tintervalo=(float)(System.currentTimeMillis()-tinicio)/1000;
				tinicio=System.currentTimeMillis();
//				System.out.println("==================================================");
//				System.out.println("Thread: "+thread_id);
//				System.out.println("100 bucles ejecutados en : "+tintervalo+" segundos");
				cont.IncrementaBucle();
				cont.Actualiza(thread_id,tintervalo);
			}
			// Entra el thinktime
			try {
				Thread.sleep(thinktime/2+r.nextInt(thinktime)/2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // Fin del bucle infinito
	}



	// Constructor: se limita a inicializar las variables 
	// de conexion
	public QueryThreads(Contador cont,String driver,String url,String usuario,String password, int thinktime, int thread_id, String logfile, String debug){
		this.cont = cont;
		this.driver=driver;
		this.url=url;
		this.usuario=usuario;
		this.password=password;
		this.thinktime=thinktime;
		this.thread_id=thread_id;
		this.logfile=logfile;
		if (debug.equals("Y") || debug.equals("y")){
			this.debug=true;
		} else {
			this.debug=false;
		}
		//  El constructor no hace na de na, todo se deja en manos del
		// thread de inicializacion
	}
}
