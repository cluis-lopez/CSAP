
public class Imprimidor extends Thread {

	Contador cont;

	public void run() {
		float meantime;
		int launched_threads;

		while (true){
		// Imprime cada 5 segundos ...
			try{
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		meantime=0;
		launched_threads=0;

	
		for (int i=0; i<cont.getThreads(); i++){
			meantime=meantime+cont.getTime(i);
			launched_threads++;
		}

		System.out.println("Threads : "+launched_threads+"   Bucles completados (50): "+cont.getBucles()+"  Tiempo medio: "+meantime/launched_threads);

		}
	}

	//Constructor

	public Imprimidor (Contador cont){
		this.cont = cont;
	}
}
