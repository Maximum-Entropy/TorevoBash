import java.util.Scanner;

@SuppressWarnings("unused")
public class Demo {
	
	private static byte[][] headSlam = 
		{{4,4,3,4,1,2,2,2,3,2,2,1,2,4,4,3,1,3,1,1},
		 {3,3,4,1,3,4,1,2,1,2,3,2,3,3,3,2,2,2,2,4}};
	
	private static byte[][] rightChestPunch = 
		{{1,1,1,2,1,3,4,2,1,2,1,4,2,1,1,4,4,2,1,2},
		 {3,4,1,3,1,4,3,1,1,3,1,3,1,2,2,1,4,1,1,4}};
	
	private static byte[][] armSlash = 
		{{1,1,1,2,2,4,2,2,2,1,1,2,4,2,2,3,4,2,4,3},
		 {1,2,2,2,3,3,1,3,1,4,4,2,1,4,1,1,2,3,4,2}};
	
	private static byte[][] nwk1 = 
		{{2,4,2,3,2,3,4,4,1,1,4,1,3,1,3,4,2,3,4,1},
		 {4,4,3,2,3,4,3,2,4,3,3,4,2,2,2,4,3,3,4,3},
		 {3,4,4,1,3,4,1,3,2,2,3,1,2,1,2,4,3,4,2,3},
		 {1,1,1,1,3,1,2,1,2,1,4,2,1,2,4,4,2,4,2,3}};
	
	private static byte[][] amaze =
		{{3,2,1,3,1,2,3,3,3,3,3,1,3,1,3,3,3,2,2,2},
		 {3,3,2,3,1,2,3,3,3,3,2,1,3,2,3,3,2,1,1,1}};
	
	// Replace with choice of opener
	private static byte[][] opener = headSlam;
	private static boolean multi = false;
	
	public static void main(String[] args) throws InterruptedException {
		CERobot toriGod = new CERobot();
		
		if( !multi ) {
			System.out.println("Starting in 5 seconds.");
			Thread.sleep(5000);
			
			toriGod.newGame();
		}
		
		Scanner in = new Scanner(System.in);
		System.out.print("Enter q to continue :: ");
		String input = in.nextLine();
		
		int loopNum = 0;
		GAMELOOP: {
			while( input.equals("q") ) {
				System.out.println("Entering seq" + (loopNum+1) + " in 5 secs.");
				Thread.sleep(5000);
				
				toriGod.setState(opener[loopNum]);
				
				toriGod.endTurn();
				loopNum++;
				
				if( loopNum < opener.length ) {
					System.out.print("Enter q to continue :: ");
					input = in.nextLine();
				}
				else break GAMELOOP;
			}
		}
		
		System.out.println("<<< Opener finished >>>");
	}

}
