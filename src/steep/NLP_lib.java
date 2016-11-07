package steep;

public class NLP_lib implements NLP_if{
	
	double init_x[];
	double alpha[];
	int roop_max;
	double conv;
	double x[][], obj[];
	double norm[], dx[][];
	private double xi1, tau;//0 <xi1 <1, 0 < xi1, 0 < tau <1
	long time_lim = 0, time_conv = 0;
	
	public NLP_lib(double[] init_x, double alpha, int roop_max, double conv, double xi1, double tau) {
		this.init_x = init_x;
		this.alpha = new double[roop_max];
		this.alpha[0] = alpha;
		this.roop_max = roop_max;
		this.conv = conv;
		this.x = new double[roop_max][roop_max];
		this.obj = new double[roop_max];
		this.norm = new double[roop_max];
		this.dx = new double[roop_max][roop_max];
		this.xi1 = xi1;
		this.tau = tau;
	}
	
	public void callSTEEP(){
		
		long start = System.currentTimeMillis();
		
		//0回目
		x[0][0] = this.init_x[0];
		x[0][1] = this.init_x[1];
		obj[0] = this.getRx(0);
		dx[0][0] = 0;
		dx[0][1] = 0;
		norm[0] = 0;
		int flg = 0;
		
		for(int i = 1; i < roop_max; i++){
			
			//探索方向を求める
			dx[i][0] = this.getDx1(i-1);
			dx[i][1] = this.getDx2(i-1);
			 
			//Armjo
			alpha[i] = this.getArmijo(i);
			
			//次の点を求める
			x[i][0] = x[i-1][0] + alpha[i]*dx[i][0]*(-1);
			x[i][1] = x[i-1][1] + alpha[i]*dx[i][1]*(-1);
			
			//目的関数の計算
			obj[i] = this.getRx(i);
			
			//収束条件判定
			norm[i] = Math.sqrt(Math.pow(dx[i][0], 2) + Math.pow(dx[i][1], 2));
			if( norm[i] < conv && flg == 0){
				long stop = System.currentTimeMillis();
				this.time_conv = stop - start;
				flg = 1;
			}
			
		}
		
		long stop = System.currentTimeMillis();
		time_lim = stop - start;	
		
	}
	
	public double getArmijo(int i){
		double left, right, beta = 1.0, tmp = 0;
		for(int j = 0; j < 100; j++){
			left = getArmijo_left(i, beta);
			right = getArmijo_right(i, beta);;
			
			if(left <= right +0.01){
				tmp = beta;
				break;
			}else {
				beta = tau * beta;
			}
		}
		return tmp;
	}
	
	public double[] getAlpha() {
		return alpha;
	}

	public double[][] getX() {
		return x;
	}

	public double[] getObj() {
		return obj;
	}

	public double[] getNorm() {
		return norm;
	}

	@Override
	public double getRx(int i) {
		// TODO Auto-generated method stub
		return Math.pow(this.x[i][0], 2) + Math.pow(this.x[i][1]-1, 4) + this.x[i][0];
	}

	@Override
	public double getDx1(int i) {
		// TODO Auto-generated method stub
		return 2*x[i][0] + 1;
	}

	@Override
	public double getDx2(int i) {
		// TODO Auto-generated method stub
		return 4*(Math.pow(x[i][1]-1,3));
	}

	@Override
	public double getArmijo_left(int i, double beta) {
		// TODO Auto-generated method stub
		return Math.pow(x[i-1][0] + beta * dx[i][0], 2) + Math.pow(x[i-1][1] + beta * dx[i][1]-1, 4) + x[i-1][0] + beta * dx[i][0];
	}

	@Override
	public double getArmijo_right(int i, double beta) {
		// TODO Auto-generated method stub
		return Math.pow(x[i-1][0], 2) + Math.pow(x[i-1][1]-1, 4) + x[i-1][0] +xi1 * beta * (-1) * (Math.pow(dx[i][0], 2) + Math.pow(dx[i][1], 2));
	}
	
}
