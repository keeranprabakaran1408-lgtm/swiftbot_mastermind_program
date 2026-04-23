package task1_Mastermind_Swiftbot;
import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import swiftbot.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class task1 {
	static SwiftBotAPI swiftbot;
	static final String RESET = "\u001B[0m";//for colour for mastermind title
	static final String CYAN = "\u001B[36m";
	static final String YELLOW = "\u001B[33m";
	static final String GREEN = "\u001B[32m";
	static final String WHITE = "\u001B[37m";
	static final String BOLD = "\u001B[1m";

	public static void main(String[] args) throws Exception {//throws error via try and catch for swiftbot issues

		swiftbot = SwiftBotAPI.INSTANCE; //initialise swiftbot api

		System.out.printf("""
				%1$s%2$s-------------------------------------------------------------------------
				-----------------------MASTERMIND----------------------------------------
				-------------------------------------------------------------------------%3$s
				""", CYAN, BOLD, RESET); // header

		Scanner guess = new Scanner(System.in); //input
		int player_score = 0; //score
		int swiftbot_score = 0; //score
		int round_number = 1; //rounds

		File folder = new File("logs"); //folder
		folder.mkdirs(); // create the folder or any missing parent folders in system

		File log = new File(folder, "Mastermind_log_file.txt"); // logfile

		while(true) { // menu
			System.out.println();
			System.out.println("A = PLAY");
			System.out.println("B = CALIBRATION");

			Button menuBtn = waitForButton(Button.A, Button.B); //menu selection via buttons

			if(menuBtn == Button.A) { // play selection by pressing A
				boolean play_again = false; // loop
				while(!play_again) { // game loop

					ArrayList <String> colour_array = new ArrayList<String>(); // colours
					colour_array.add("R");
					colour_array.add("G");
					colour_array.add("B");
					colour_array.add("Y");
					colour_array.add("O");
					colour_array.add("P");

					System.out.println("PRESS A FOR DEFAULT OR B FOR CUSTOMISED"); // mode selection displayed
					Button modeBtn = waitForButton(Button.A, Button.B); // mode select via button
					String mode = (modeBtn == Button.A) ? "A" : "B"; // mode selection statement

					int secret_code_length = 4; // default length
					int attempts = 6; // default attempts

					if(mode.equals("B")) { // customised mode
						while(true) { // settings for customised mode chosen by user
							System.out.print("ENTER THE SECRET CODE YOU WANT TO GENERATE BETWEEN 3 - 6: ");
							String secret_code_input = guess.nextLine();//reads input
							try {
								secret_code_length = Integer.parseInt(secret_code_input);//turns input into int
								if(secret_code_length < 3 || secret_code_length > 6) {
									System.out.println("ENTER A VALID NUMBER BETWEEN 3 - 6");
								} else {
									break;
								}
							} catch (NumberFormatException e) {//checking valid integer
								System.out.println("ERROR INVALID NUMBER");
							}
						}

						while(true) { // attempts loop for customised mode 
							System.out.print("ENTER NUMBER OF GUESS THAT YOU WANT: ");
							String max_atempts = guess.nextLine();
							try {
								attempts = Integer.parseInt(max_atempts);//turns input into int
								if(attempts <= 0 ) {
									System.out.println("INVALID NUMBER OF GUESSES");
								}
								else {
									break;
								}
							} catch (NumberFormatException e) {
								System.out.println("INVALID NUMBER");
							}
						}
					}

					Random random = new Random(); //Random initialisation
					String s = ""; // secret
					for(int i = 0; i< secret_code_length;i++) { //build secret code
						int num = random.nextInt(colour_array.size());
						String colour = colour_array.get(num);
						s = s + colour;
						colour_array.remove(num);//removes repeats colour in secret code
					}

					boolean game = false; // win flag
					while(attempts > 0 && !game){ // attempts loop
						int count = 0; // plus
						int minus = 0; // minus
						String secret_code = s; // secret code

						String guess_input = image_capture(secret_code_length); // scan guess
						if (repeat(guess_input)) { // no repeats
							System.out.println("ERROR REPEAT COLOUR");
							continue;
						}

						for(int j = 0; j< secret_code.length(); j++ ) { // count plus
							if(secret_code.charAt(j) == guess_input.charAt(j)) {
								count+=1;
							}
						}

						if(count == secret_code.length()) { // win
							game = true;
							System.out.println("YOU WIN");
							player_score+=1;
							celebrateWin(); // celebration
						}
						else { // minus + attempts
							for(int n = 0; n < guess_input.length(); n++) {
								if(guess_input.charAt(n) == secret_code.charAt(n)) {
									continue;
								}
								if (secret_code.indexOf(String.valueOf(guess_input.charAt(n))) != -1) {
									minus+=1;
								}
							}
							attempts-=1;
							System.out.println("YOU HAVE " + attempts + " ATTEMPTS LEFT");
						}

						for(int k = 0; k < count; k++) System.out.print("+"); // print +
						for(int k = 0; k< minus; k++) System.out.print("-"); // print -
						System.out.println();
						System.out.println("guess: " + guess_input); // show guess
						
						System.out.println("PRESS B FOR HINT OR A TO CONTINUE");
						Button hintBtn = waitForButton(Button.A, Button.B);

						if (hintBtn == Button.B) {//displays hints
						    System.out.println("HINT: " + secret_code.charAt(0));
						}

						if(!game && attempts == 0) { // lose
							swiftbot_score +=1;
							System.out.println(" YOU LOSE SECRET CODE: " + secret_code);
						}

						try(PrintWriter out = new PrintWriter(new FileWriter(log, true))){ // log file saving after each round
							out.println("ROUND " + round_number + " --> SECRET CODE: " + secret_code + " --> GUESS: " + guess_input + " --> +: " + count + " --> -: " + minus + " --> SCORE: PLAYER " + player_score + " - SWIFTBOT " + swiftbot_score + " --> ATTEMPTS: " + attempts);
						}catch (IOException e) {
							System.out.println("ERROR WITH LOG FILE " + e.getMessage());
						}
					}

					System.out.print("SCORE: PLAYER " +  player_score + " - SWIFTBOT " +  swiftbot_score); //scoreboard after each round
					System.out.println();

					System.out.println("PRESS Y TO PLAY AGAIN OR X TO QUIT"); //expression if want to play again or not
					Button endBtn = waitForButton(Button.Y, Button.X); // y/x

					if (endBtn == Button.Y) { //play again
						play_again = false;
						round_number+=1;
					}
					else { //displays back to main menu
						play_again = true;
					}
				}
			}
			else if(menuBtn == Button.B) { // calibration
				calibration();
			}
		}
	}

	static boolean repeat(String guess_input) { //  remove duplicates of colours
		for (int i = 0; i < guess_input.length(); i++) {
			for (int j = i + 1; j < guess_input.length(); j++) {
				if (guess_input.charAt(i) == guess_input.charAt(j)) {
					return true;
				}
			}
		}
		return false;
	}

	static Button waitForButton(Button b1, Button b2) throws InterruptedException { // button wait until user presses either swiftbot button b1 or 2 then returns of that was pressed
		final Button[] pressed = { null };//create temp variable that can be changed when button is called

		swiftbot.enableButton(b1, () -> pressed[0] = b1);//turns button 1 on and sets pressed[0]  = button 1
		swiftbot.enableButton(b2, () -> pressed[0] = b2);

		while (pressed[0] == null) {//loops until button is called
			Thread.sleep(50);
		}

		swiftbot.disableButton(b1);//turns buttons off so don't become active later
		swiftbot.disableButton(b2);

		return pressed[0];//returns button user pressed
	}

	static String image_capture(int secret_code_length) throws Exception { // scan cards
		String guess_input = "";
		int[] green = {0,255,0};
		int[] red = {255,0,0};
		for (int m = 1; m <= secret_code_length; m++) {

			System.out.println("SHOW CARD " + m + " TO THE SWIFTBOT");
			Thread.sleep(1500);

			BufferedImage img = swiftbot.takeStill(ImageSize.SQUARE_480x480);//takes image size of 480 pixels wide and 480 pixels tall and it is square

			if (img == null) {//capture failed
			    System.out.println("CAMERA ERROR (NULL IMAGE) - TRY AGAIN");
			    swiftbot.fillUnderlights(new int[]{255,0,0});//flashes red
			    Thread.sleep(800);
			    swiftbot.disableUnderlights();
			    m -= 1;//moves back to try again with scan
			    continue;
			}

			String filename = String.format("/data/home/pi/TestImage_%02d.jpg", m);//saves a different photo per scan
			ImageIO.write(img, "jpg", new File(filename));

			String image_to_colour = rgb_colour(img);//rgb method 

			if (image_to_colour.equals("ERROR")) {
				swiftbot.fillUnderlights(red);//flashes red for error
			    System.out.println("FAILED ON CARD " + m + " - RESCAN SAME CARD");
			    Thread.sleep(800);
			    swiftbot.disableUnderlights();
			    Thread.sleep(1500);
			    m -= 1;
			    continue;
			}

			guess_input += image_to_colour;
			swiftbot.fillUnderlights(green);//flashes green
			System.out.println("VALID " + image_to_colour);
			Thread.sleep(500);
			swiftbot.disableUnderlights();
			Thread.sleep(700);
		}

		return guess_input;
	}


	static String rgb_colour(BufferedImage img) {//rgb values 
			int r_sum = 0;
			int b_sum = 0;
			int g_sum = 0;
			int total_rgb = 0;
			int step = 10;//loops every 10 pixels for faster processing of each scan

			int xStart = img.getWidth() / 4;;//to ensure the centre area of the square colour card is scanned to ensure that it is scanned and captured properly
			int xEnd = img.getWidth() * 3 / 4;
			int yStart = img.getHeight() / 4;
			int yEnd = img.getHeight() * 3 / 4;

			for (int x = xStart; x < xEnd; x += step) {
				for (int y = yStart; y < yEnd; y += step) {
					int p = img.getRGB(x, y);//get rgb values of image

					int r = (p >> 16) & 0xFF;;//extracts red, green, blue from image through shifting
					int g = (p >> 8) & 0xFF;
					int b = p & 0xFF;

					r_sum += r;
					b_sum += b;
					g_sum += g;
					total_rgb += 1;
				}
			}

			int r_avg = (int) (r_sum / total_rgb);
			int b_avg = (int) (b_sum / total_rgb);
			int g_avg = (int) (g_sum / total_rgb);

			if (r_avg > b_avg + 40 && r_avg > g_avg + 40) return "R";//colours in between the primary colours
			if (b_avg > r_avg + 40 && b_avg > g_avg + 40) return "B";
			if (g_avg > r_avg + 40 && g_avg > b_avg + 40) return "G";
			if (r_avg > 140 && g_avg > 140 && b_avg < 120) return "Y";
			if (r_avg > 150 && b_avg > 100 && g_avg < r_avg && g_avg < b_avg) return "P";
			if (r_avg > 160 && g_avg > 80 && g_avg < r_avg && b_avg < 110) return "O";

			System.out.println("ERROR AVG RGB = (" + r_avg + "," + g_avg + "," + b_avg + ")");
			return "ERROR";
		}

		static void celebrateWin() throws InterruptedException { // lights for celebration
			int[] green = {0,255,0};
			for (int i = 0; i < 5; i++) {
				swiftbot.fillUnderlights(green);
				Thread.sleep(200);
				swiftbot.disableUnderlights();
				Thread.sleep(200);
			}
		}

		static void calibration() throws Exception { // rgb average
			System.out.println("SHOW A CARD FOR CALIBRATION");
			Thread.sleep(1500);
			BufferedImage img = swiftbot.takeStill(ImageSize.SQUARE_480x480);
			int[] avg = averageRGB(img);
			System.out.println("AVG RGB = (" + avg[0] + "," + avg[1] + "," + avg[2] + ")");//prints avg rgb
		}

		static int[] averageRGB(BufferedImage img) { // compute avg for calibration
			int r=0;
			int g=0;
			int b=0;
			int total=0;
			int step = 10;

			for(int y=0;y<img.getHeight();y+=step){
				for(int x=0;x<img.getWidth();x+=step){
					int p = img.getRGB(x,y);
					r += (p>>16)&255;
					g += (p>>8)&255;
					b += p&255;
					total++;
				}
			}
			return new int[]{(int)(r/total),(int)(g/total),(int)(b/total)};//avg rgb
		}
	}

	
