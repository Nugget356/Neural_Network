package mainPackage;

import java.util.Random;
import java.lang.Math;

public class main {
	
	public static Random randomNum = new Random();
	public static double Dealer = 0;
	public static double Player = 0;
	public static double pre_tan = 0;
	public static double b = Math.random()*.2-.1;
	public static double w1 = Math.random()*.2-.1;
	public static double w2 = Math.random()*.2-.1;
	
	
	public static void main(String[] args) {
		int x = 0;
		double win = 0;
		while (x != 50000) {
			boolean z = newgame();
			if (z) {
				System.out.printf("%nPlayer Won%n");
				win += 1;
			}
			else {
				System.out.printf("%nDealer Won%n");
			}
			
			x++;
		}
		double winpercent = (win / 50000) * 100;
		System.out.print(winpercent);
		
	}
	
	public static boolean newgame() {
		boolean con = true;
		Dealer = 2 + randomNum.nextInt(11);
		System.out.printf("%nDealer Has: ");
		System.out.print(Dealer);
		Player = 2 + randomNum.nextInt(20);
		System.out.printf("%nPlayer Has: ");
		System.out.print(Player);
		
		double x = pred(Dealer,Player,w1,w2,b);
		System.out.printf("%nGuess is: ");
		System.out.print(x);
		while (con) {
			if (x >= 0.5) {
				Player = Hit(Player);
				System.out.printf("%nPlayer Hit and now has: ");
				System.out.print(Player);
				if (Player < 22) {
					train(x,1);
					x = pred(Dealer,Player,w1,w2,b);
				}
				else {
					System.out.printf("%nPlayer Bust :(");
					//Should have stood
					train(x,0);
					con = false;
					return false;
				}
			}
			else {
				boolean dealerCon = true;
				while (dealerCon) {
					if (Dealer < 17) {
						Dealer = Hit(Dealer);
						System.out.printf("%nDealer Hit and now has: ");
						System.out.print(Dealer);
					}
					else if (Dealer > 21) {
						System.out.printf("%nDealer Bust :)");
						//Standing Was Correct
						train(x,0);
						dealerCon = false;
						con = false;
						return true;
					}
					else {
						dealerCon = false;
					}
				}
			con = false;
			}
			
		}
		if (Dealer > Player) {
			//Should have Hit
			train(x,1);
			return false;
		}
		else {
			//Standing Was Correct
			train(x,0);
			return true;
		}
		
	}
	
	public static double Hit(double player) {
		double newCard= 2 + randomNum.nextInt(11);
		player += newCard;
		return player;
	}
	
	public static double passedDealer = 0;
	public static double passedPlayer = 0;
	
	public static double tanh(double x) {
		return (1/(1+ Math.pow(Math.E,(-1*x))));
	}
	
	public static double pred (double m1 , double m2, double w1,double w2, double b) {
		double z = (m1 * w1) + (m2 * w2) + b;
		passedDealer = m1;
		passedPlayer = m2;
		pre_tan = z;
		return tanh(z);
	}
	
	public static double train(double b, double target) {
		double rate = 0.001;
		double cost =  Math.pow((b-target), 2);
		
		double dcost_dpred = 2 * (b - target);
		
		double dpred_dz = tanh(pre_tan);
		
		double dz_dw1 = passedDealer;
		double dz_dw2= passedPlayer;
		double dz_db = 1;
		
		double dcost_dw1 = dcost_dpred * dpred_dz * dz_dw1;
		double dcost_dw2 = dcost_dpred * dpred_dz * dz_dw2;
		double dcost_db = dcost_dpred * dpred_dz * dz_db;
		
		w1 -= rate * dcost_dw1;
		w2 -= rate * dcost_dw2;
		b -= rate * dcost_db;
		return 0;
	}
	

}
