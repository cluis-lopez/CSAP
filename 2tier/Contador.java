import java.util.ArrayList;

public class Contador {

	ArrayList tiempos;
	Float f= new Float(0);
	int launched_threads=0;
	int bucles=0;

	public Contador(int n) {
		tiempos = new ArrayList();
		launched_threads=0;
		for (int i=0; i< n; i++){
			tiempos.add(i, f);
		}
	}

	public void Actualiza (int i, float t){
		tiempos.set(i, ((Float) tiempos.get(i) +  t)/2);
	}

	public void Incrementa(){
		launched_threads++;
	}

	public int getThreads(){
		return launched_threads;
	}

	public float getTime(int i){
		return (Float) tiempos.get(i);
	}
	public void IncrementaBucle(){
		bucles++;
	}
	public int getBucles(){
		return bucles;
	}
}
