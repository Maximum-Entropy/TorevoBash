import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class CERobot {

	private Robot bot;
	private String currentProgram;
	
	public CERobot() {
		try {
			bot = new Robot();
		} catch (AWTException e) {
			System.out.println("Unable to initialize robot.");
			e.printStackTrace();
		}
		
		// Manual switch to CE required
		currentProgram = "ce";
	}
	
	public void setState(byte[] sequence) {
		switchTo("ce");
		
		// Manual left-half CE positioning required
		for( int i = 0; i < sequence.length; i++ ) {
			bot.mouseMove(358, 448 + (int)(i*15.9));
			
			bot.mousePress(InputEvent.BUTTON1_MASK);
			bot.mouseRelease(InputEvent.BUTTON1_MASK);
			bot.mousePress(InputEvent.BUTTON1_MASK);
			bot.mouseRelease(InputEvent.BUTTON1_MASK);
			
			type(Byte.toString(sequence[i]));
			
			bot.keyPress(KeyEvent.VK_ENTER);
			bot.keyRelease(KeyEvent.VK_ENTER);
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.out.println("Unable to pause thread.");
				e.printStackTrace();
			}
		}
	}
	
	public void endTurn() {
		switchTo("tori");
		
		bot.keyPress(KeyEvent.VK_SPACE);
	    bot.keyRelease(KeyEvent.VK_SPACE);
	}
	
	public double getAdvantage() {
		switchTo("ce");
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("Unable to pause thread.");
			e.printStackTrace();
		}
		
		double[] scores = new double[2];
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		
		for( int i = 0; i < 2; i++ ) {
			try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
				System.out.println("Unable to pause thread.");
				e.printStackTrace();
			}
			bot.mouseMove(358, 769 + 15*i);
			bot.mousePress(InputEvent.BUTTON1_MASK);
			bot.mouseRelease(InputEvent.BUTTON1_MASK);
			bot.mousePress(InputEvent.BUTTON1_MASK);
			bot.mouseRelease(InputEvent.BUTTON1_MASK);
			bot.keyPress(KeyEvent.VK_CONTROL);
			bot.keyPress(KeyEvent.VK_C);
			bot.keyRelease(KeyEvent.VK_C);
			bot.keyRelease(KeyEvent.VK_CONTROL);
			bot.keyPress(KeyEvent.VK_ENTER);
		    bot.keyRelease(KeyEvent.VK_ENTER);
		    try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
				System.out.println("Unable to pause thread.");
				e.printStackTrace();
			}
			try {
				scores[i] = Double.parseDouble((String) clipboard.getData(DataFlavor.stringFlavor));
			} catch (NumberFormatException | UnsupportedFlavorException | IOException e) {
				System.out.println("Unable to read from clipboard.");
				e.printStackTrace();
			}
		}
		return scores[1] - scores[0];
	}
	public void newGame() {
		switchTo("tori");
		
		bot.keyPress(KeyEvent.VK_R);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			System.out.println("Unable to pause thread.");
			e.printStackTrace();
		}
		
		bot.keyPress(KeyEvent.VK_SPACE);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			System.out.println("Unable to pause thread.");
			e.printStackTrace();
		}
	}
	
	private void switchTo(String program) {
		if( currentProgram.equals(program) )
			return;
		
		if( program.equals("ce") ) {
			bot.keyPress(KeyEvent.VK_ALT);
		    bot.keyPress(KeyEvent.VK_TAB);
		    bot.keyRelease(KeyEvent.VK_ALT);
		    bot.keyRelease(KeyEvent.VK_TAB);
		    
		    try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("Unable to pause thread.");
				e.printStackTrace();
			}
		    
		    currentProgram = "ce";
		} else if( program.equals("tori") ) {
			bot.keyPress(KeyEvent.VK_ALT);
		    bot.keyPress(KeyEvent.VK_TAB);
		    bot.keyRelease(KeyEvent.VK_ALT);
		    bot.keyRelease(KeyEvent.VK_TAB);
		    
		    try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("Unable to pause thread.");
				e.printStackTrace();
			}
		    
		    currentProgram = "tori";
		}
		
		else System.out.println("Invalid program choice.");
	}
	
	private void type(String characters) {
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    StringSelection stringSelection = new StringSelection( characters );
	    clipboard.setContents(stringSelection, stringSelection);

	    bot.keyPress(KeyEvent.VK_CONTROL);
	    bot.keyPress(KeyEvent.VK_V);
	    bot.keyRelease(KeyEvent.VK_V);
	    bot.keyRelease(KeyEvent.VK_CONTROL);
	}
}
