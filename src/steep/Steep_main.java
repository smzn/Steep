package steep;

public class Steep_main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		double init_x[] = {-1.0, 0.5};
		int roop_max = 500;
		double convergence = 0.001;
		double alpha = 0.5;
		
		NLP_lib lib = new NLP_lib(init_x, alpha, roop_max, convergence, 0.1, 0.5); //double xi1, double tau
		lib.callSTEEP();
		double obj[] = lib.getObj();
		for(int i = 0; i < obj.length; i++){
			System.out.println(i + " : obj= " + obj[i]);
		}
		
	}

}
