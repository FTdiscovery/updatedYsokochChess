package reinforcechess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

public class EvalTests {

	static String PGN_GAME_LOG = "[White: Random Chess AI]\n[Black: Random Chess AI]\n\n";

	static String[] colNames = "abcdefgh".split("");
	static String[][] chessBoard = {
			//A   B   C   D   E   F   G   H
			{"r","n","b","q","k","b","n","r"},  //8
			{"p","p","p","p","p","p","p","p"},  //7
			{" "," "," "," "," "," "," "," "},  //6
			{" "," "," "," "," "," "," "," "},  //5
			{" "," "," "," "," "," "," "," "},  //4
			{" "," "," "," "," "," "," "," "},  //3
			{"P","P","P","P","P","P","P","P"},  //2
			{"R","N","B","Q","K","B","N","R"}};	//1


	//First 8 are for white pawns, first 8 are for black pawns
	static boolean[] pawnDoubleMove = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};
	static int[] timePawnMoved = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	static int totalMoves = 0;
	static int whiteKingMoved = 0;
	static int blackKingMoved = 0;
	static int kwRookMoved = 0;
	static int qwRookMoved = 0;
	static int kbRookMoved = 0;
	static int qbRookMoved = 0;

	static String pureLog = "";
	static boolean debugOn = false;

	//Helps with Evaluation.
	static int pawnBoard[][]={
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{50, 50, 50, 50, 50, 50, 50, 50},
			{10, 15, 25, 40, 40, 25, 15, 10},
			{ 5,  5, 10, 35, 35, 10,  5,  5},
			{ 0,  0,  0, 25, 25,-10,  0,  0},
			{ 5, -5,-10,  0,  0,-10, -5,  5},
			{ 5, 10, 10,-20,-20, 15, 10,  5},
			{ 0,  0,  0,  0,  0,  0,  0,  0}};
	static int rookBoard[][]={
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{10, 20, 20, 20, 20, 20, 20, 10},
			{-10, 0,  0,  0,  0,  0,  0,-10},
			{-10, 0,  0,  0,  0,  0,  0,-10},
			{-10, 0,  0,  0,  0,  0,  0,-10},
			{-10, 0,  0,  0,  0,  0,  0,-10},
			{-10, 0,  0,  0,  0,  0,  0,-10},
			{ 0,  0,  0, 10, 10,  0,  0, 0}};
	static int knightBoard[][]={
			{-50,-40,-30,-30,-30,-30,-40,-50},
			{-40,-20,  0,  0,  0,  0,-20,-40},
			{-30,  0, 10, 15, 15, 10,  0,-30},
			{-30,  5, 15, 20, 20, 15,  5,-30},
			{-30,  0, 15, 20, 20, 15,  0,-30},
			{-30,  5, 10, 15, 15, 10,  5,-30},
			{-40,-20,  0,  5,  5,  0,-20,-40},
			{-50,-40,-30,-30,-30,-30,-40,-50}};
	static int bishopBoard[][]={
			{-20,-10,-10,-10,-10,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5, 10, 10,  5,  0,-10},
			{-10,  5,  5, 10, 10,  5,  5,-10},
			{-10,  0, 10, 10, 10, 10,  0,-10},
			{-10, 10, 10, 10, 10, 10, 10,-10},
			{-10,  5,  0,  0,  0,  0,  5,-10},
			{-20,-10,-10,-10,-10,-10,-10,-20}};
	static int queenBoard[][]={
			{-20,-10,-10, -5, -5,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5,  5,  5,  5,  0,-10},
			{ -5,  0,  5,  5,  5,  5,  0, -5},
			{  0,  0,  5,  5,  5,  5,  0, -5},
			{-10,  5,  5,  5,  5,  5,  0,-10},
			{-10,  0,  5,  0,  0,  0,  0,-10},
			{-20,-10,-10, -5, -5,-10,-10,-20}};
	static int kingBoard[][]={
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-20,-30,-30,-40,-40,-30,-30,-20},
			{-10,-20,-20,-20,-20,-20,-20,-10},
			{ 20, 20,  0,  0,  0,  0, 20, 20},
			{ 20, 30, 10,  0,  0, 10, 30, 20}};
	static int kingEndBoard[][]={ 
			{-50,-40,-30,-20,-20,-30,-40,-50},
			{-30,-20,-10,  0,  0,-10,-20,-30},
			{-30,-10, 20, 30, 30, 20,-10,-30},
			{-30,-10, 30, 40, 40, 30,-10,-30},
			{-30,-10, 30, 40, 40, 30,-10,-30},
			{-30,-10, 20, 30, 30, 20,-10,-30},
			{-30,-30,  0,  0,  0,  0,-30,-30},
			{-50,-30,-30,-30,-30,-30,-30,-50}};

	static ArrayList<double[]> STATES = new ArrayList<>(); 
	static ArrayList<double[]> RATINGS = new ArrayList<>();
	static ArrayList<String> MOVES = new ArrayList<>();

	static ArrayList<double[]> ENDGAME_STATES = new ArrayList<>(); 
	static ArrayList<double[]> ENDGAME_RATINGS = new ArrayList<>();
	static ArrayList<String> ENDGAME_MOVES = new ArrayList<>();

	static double[][] input = new double[1][chessBoardToState().length];
	static double[][] endInput = new double[1][endBoardToState().length];
	static double[][] output = new double[1][tanRating(0).length];
	static double[][] endOutput = new double[1][tanRating(0).length];
	static int nodesPerLayer = 300; //TESTED: [150; 175; 200]
	static double learningRate = 0.000025; //TESTED: [0.001; 0.001/0.002; 0.0005]
	static MomentumChessNeural brain = new MomentumChessNeural(input,output,nodesPerLayer,learningRate);

	static int printInterval = 20;
	static int nodesPerLayer2 = 100; //TESTED: [150; 175; 200]
	static double learningRate2 = 0.000022; //TESTED: [0.001; 0.001/0.002; 0.0005]
	static MomentumChessNeural endGameBrain = new MomentumChessNeural(endInput,endOutput,nodesPerLayer2,learningRate2);

	static double enoughTrainingBenchmark = 0.25;

	public static double[] tanRating(double a) {
		double[] rating = new double[1];
		rating[0]=((Math.atan(a)+(Math.PI/2))/(Math.PI));
		return rating;
	}

	public static double revertTanRating(double[] a) {
		return Math.tan(Math.PI*(a[0]-0.5));
	}

	public static double[] ratingForNN(double a) {
		double[] rating = new double[5];
		rating[0]=0.5;
		if (a>=-3 && a<=3) {
			rating[0] += (a/6);
		}
		if (a>=3) {
			rating[0]=1;
			rating[1] = Math.min(1,((a-2.5)/5));
		}
		if (a<=-3) {
			rating[0]=0;
			rating[2] = Math.min(1,((-3-a)/5));
		}
		if (a>=8) {
			rating[3] = Math.min(1,((a-8)/5));
		}
		if (a<=-8) {
			rating[4] = Math.min(1,((-8-a)/5));
		}
		return rating;

	}

	public static double revertRating(double[] a) {
		double value=0;
		value += (a[0]*6)-3;
		value += a[1]*5+a[3]*5;
		value -= (a[2]*5+a[4]*5);

		return value;

	}

	public static void addStateRating(String a, double b) {
		STATES.add(chessBoardToState());
		RATINGS.add(tanRating(b));
		MOVES.add(a);
	}

	public static void addEndGameStateRating(String a, double b) {
		ENDGAME_STATES.add(endBoardToState());
		ENDGAME_RATINGS.add(tanRating(b));
		ENDGAME_MOVES.add(a);
	}



	static String[] books = {"yikarjakin2017","f4sucks","ammonia","a4sucks","h4sucks","sodiumsucks"};
	static String[] bookAccuracyCheck = {"yikarjakin2017ratings","f4sucksrating","ammoniarating","a4sucksrating","h4sucksrating","sodiumsucksrating"};
	static String documents = System.getProperty ("user.home") + "/Documents/Procrastination Box/Chess Books/";
	static ArrayList<String[]> library = new ArrayList<>();
	static ArrayList<double[]> ratingLib = new ArrayList<>();

	public static void main(String[] args) throws IOException {

		//This is the initial position.
		STATES.add(chessBoardToState());
		RATINGS.add(tanRating(0.23));

		for (int i = 0;i<books.length;i++) {
			FileReader book = new FileReader (documents + books[i]+".txt");
			BufferedReader buffer = new BufferedReader(book);
			//Read the files...
			String word;
			ArrayList<String> game = new ArrayList<>();
			while ((word = buffer.readLine()) != null) {
				game.add(word);
			}
			String [] addToLibrary = game.toArray(new String[game.size()]);
			library.add(addToLibrary);
		}
		for (int i = 0;i<bookAccuracyCheck.length;i++) {
			FileReader book = new FileReader (documents + bookAccuracyCheck[i]+".txt");
			BufferedReader buffer = new BufferedReader(book);
			//Read the files...
			String number;
			ArrayList<Double> game = new ArrayList<>();
			while ((number = buffer.readLine()) != null) {
				game.add(Double.parseDouble(number));
			}
			double[] addToLibrary = new double[game.size()];
			for (int j = 0;j<game.size();j++) {
				addToLibrary[j]=game.get(j);
			}
			ratingLib.add(addToLibrary);
		}
		if (debugOn) {
			for (int i = 0;i<library.size();i++) {
				System.out.println(Arrays.toString(library.get(i)));
			}
		}

		//play the games
		for (int i = 0;i<library.size();i++) {
			for(int j = 0;j<library.get(i).length;j++) {
				String move = library.get(i)[j];
				double rating = ratingLib.get(i)[j];

				makeMove(move);

				if (!mxjava.stateInDatabase(STATES, chessBoardToState()) && !mxjava.stateInDatabase(ENDGAME_STATES, chessBoardToState())) {
					if (totalMaterial()>3500) { addStateRating(move,rating); }
					else { addEndGameStateRating(move,rating); }
				}
				//printBoard();
			}
			resetBoard();
		}

		//TRAIN STATES
		brain.momentum=0.6;
		endGameBrain.momentum=0.6;

		//They should all be the same.
		System.out.println(STATES.size()==RATINGS.size());
		System.out.println(ENDGAME_STATES.size()==ENDGAME_RATINGS.size());


		double[][] tIn = new double[STATES.size()][chessBoardToState().length];
		double[][] tOut = new double[RATINGS.size()][tanRating(0).length];
		for (int i = 0;i<STATES.size();i++) {
			tIn[i]=STATES.get(i);
			tOut[i]=RATINGS.get(i);
		}
		brain.INPUT_VALUES = tIn;
		brain.OUTPUT_VALUES = tOut;


		double[][] etIn = new double[ENDGAME_STATES.size()][endBoardToState().length];
		double[][] etOut = new double[ENDGAME_RATINGS.size()][tanRating(0).length];
		for (int i = 0;i<ENDGAME_STATES.size();i++) {
			etIn[i]=ENDGAME_STATES.get(i);
			etOut[i]=ENDGAME_RATINGS.get(i);
		}
		endGameBrain.INPUT_VALUES = etIn;
		endGameBrain.OUTPUT_VALUES = etOut;
		
		double threshold = 5;
		int i = 0;
		while (threshold>enoughTrainingBenchmark) {
			int trainsPerRun = 5;
			int runs = trainsPerRun*(i+1);
			i++;
			System.out.println("RUN #"+runs);

			brain.trainNetwork(trainsPerRun);
			double error = 0;
			if (i%printInterval==0) { showAllPossibleWhiteMoves(); }
			for (int j = 0;j<STATES.size();j++) {
				if (i%printInterval==0) {
					/*
					System.out.println("EXPECTED: " + Arrays.toString(RATINGS.get(j)) + " ("+ round(revertTanRating(RATINGS.get(j)),2) + ").");
					System.out.println("NN THOUGHT: " + revertTanRating(brain.predict(chessBoardToState())) + " " +Arrays.toString(brain.predict(chessBoardToState())));
					 */
				}
				error += Math.abs(revertTanRating(brain.predict(chessBoardToState()))-revertTanRating(RATINGS.get(j)));
				if (j<MOVES.size()) makeMove(MOVES.get(j));
			}
			error /= STATES.size();
			if (error>4.5) brain.newSynapseWeights();
			System.out.println("ERROR: " + round(error,4));
			if (error<0.5) brain.momentum=0.8;
			if (error<0.2) brain.momentum=1;
			if (error<0.01) brain.momentum=0.1;

			threshold = error;

			double error2 = 0;

			// TRAIN THE END GAME COMPUTER
			endGameBrain.trainNetwork(trainsPerRun);
			for (int j = 0;j<ENDGAME_STATES.size();j++) {
				if (i%printInterval==0) {
					/*
					System.out.println("EXPECTED: " + Arrays.toString(ENDGAME_RATINGS.get(j)) + " ("+ round(revertTanRating(ENDGAME_RATINGS.get(j)),2) + ").");
					System.out.println("NN THOUGHT: " + revertTanRating(endGameBrain.predict(endBoardToState())) + " " +Arrays.toString(endGameBrain.predict(endBoardToState())));
					 */
				}
				error2 += Math.abs(revertTanRating(endGameBrain.predict(endBoardToState()))-revertTanRating(ENDGAME_RATINGS.get(j)));
				if (j<ENDGAME_MOVES.size()) makeMove(ENDGAME_MOVES.get(j));
			}
			error2 /= ENDGAME_STATES.size();
			System.out.println("ERROR: " + round(error2,4));
			if (error2>4.5) endGameBrain.newSynapseWeights();
			if (error2<0.8) endGameBrain.momentum=0.9;
			if (error2<0.4) endGameBrain.momentum=0.6;
			if (error2<0.1) endGameBrain.momentum=0.1;

			resetBoard();
			System.out.println("-------NEW RUN-------\n");
		}
		
		//PLAY OUT A GAME
		String gameLog = "";
		int moves = 60;
		totalMoves = 0;
		while (totalMoves<moves) {
			
			if (totalMoves%2==0) {
				String chosen = computerChosenWhiteMove();
				System.out.println("WHITE MOVES");
				makeMove(chosen);
				gameLog+=chosen+"\n";
				
			}
			else {
				String chosen = computerChosenBlackMove();
				System.out.println("BLACK MOVES");
				makeMove(chosen);
				gameLog+=chosen+"\n";
			}
			//printBoard();
			totalMoves++;
		}
		System.out.println(gameLog);
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	//PIECE PROTECTION (# OF DEFENDERS) FOR EACH PIECE. [UNPROTECTED PIECES...ETC]
	//SIMPLE EVALUATION FUNCTION THAT THE ROOK, BISHOP, KNIGHT, AND QUEEN CONTROL (PAWN OUTPOSTS)
	//ATTACKING PIECES
	//DEFENDERS
	//ROOK ATTACKING ENEMY PAWNS (FOR ENDGAME)


	public static double[] rateAttack() {
		double[] attack = new double[10];
		for (int i=0;i<64;i++) {
			switch (chessBoard[i/8][i%8]) {
			case "P": if (!whiteKingSafeCheck(i)) {attack[0]+=1/8;};
			break;
			case "R": if (!whiteKingSafeCheck(i)) {attack[1]+=1/3;};
			break;
			case "N": if (!whiteKingSafeCheck(i)) {attack[2]+=1/3;};
			break;
			case "B": if (!whiteKingSafeCheck(i)) {attack[3]+=1/3;};
			break;
			case "Q": if (!whiteKingSafeCheck(i)) {attack[4]+=1/2;};
			break;
			case "p": if (!blackKingSafeCheck(i)) {attack[5]+=1/8;};
			break;
			case "r": if (!blackKingSafeCheck(i)) {attack[6]+=1/3;};
			break;
			case "n": if (!blackKingSafeCheck(i)) {attack[7]+=1/3;};
			break;
			case "b": if (!blackKingSafeCheck(i)) {attack[8]+=1/3;};
			break;
			case "q": if (!blackKingSafeCheck(i)) {attack[9]+=1/2;};
			break;
			}
		}
		return attack;
	}

	
	public static int[] whiteKingZone() {
		ArrayList<Integer> places = new ArrayList<>();
		//do stuff here.
		int directory = findWhiteKing();
		int r = directory/8, c = directory%8;
		for (int i = 0;i<9;i++) {
			try {
				if (chessBoard[r-1+i/3][c-1+i%3].length()>0 && i!=4) {
					places.add((r-1+i/3)*8+(c-1+i%3));
				}
			}
			catch (Exception e) {}
		}
		int temp = 2;
		for (int i=-1;i<=1;i++) {
			while (temp<=4) {
				try {
					if (chessBoard[r-temp][c+(temp*i)].length()>0) {
						places.add(8*(r-temp)+(c+(temp*i)));
					}

				}
				catch (Exception e) {}
				temp++;
			}
			temp=2;
		}

		int[] newArray = new int[places.size()];
		for (int i =0;i<places.size();i++) {
			newArray[i]=places.get(i);
		}

		/*//PRINTS OUT THE ZONE 
		for (int x=0;x<newArray.length;x++) {
			int re = newArray[x]/8,ce=newArray[x]%8;
			chessBoard[re][ce]="X";
		}
		 */
		return newArray;
	}

	public static int[] blackKingZone() {
		ArrayList<Integer> places = new ArrayList<>();
		//do stuff here.
		int directory = findBlackKing();
		int r = directory/8, c = directory%8;
		for (int i = 0;i<9;i++) {
			try {
				if (chessBoard[r-1+i/3][c-1+i%3].length()>0 && i!=4) {
					places.add((r-1+i/3)*8+(c-1+i%3));
				}
			}
			catch (Exception e) {}
		}
		int temp = 2;
		for (int i=-1;i<=1;i++) {
			while (temp<=4) {
				try {
					if (chessBoard[r+temp][c+temp*i].length()>0) {
						places.add(8*(r+temp)+(c+temp*i));
					}
				}
				catch (Exception e) {}
				temp++;
			}
			temp=2;
		}

		int[] newArray = new int[places.size()];
		for (int i =0;i<places.size();i++) {
			newArray[i]=places.get(i);
		}

		/*///PRINTS OUT THE ZONE
		for (int x=0;x<newArray.length;x++) {
			int re = newArray[x]/8,ce=newArray[x]%8;
			chessBoard[re][ce]="x";
		}
		 */
		return newArray;
	}

	public static double[] control() {
		//First two check if the King can control 8 squares [maximizes the potential in the end game].
		//The next two check the protection of white and black pieces. 
		double[] array = new double[2];  
		if (findWhiteKing()/8!=7 && findWhiteKing()%8!=-7) array[0]=1;
		if (findBlackKing()/8!=7 && findBlackKing()%8!=-7) array[1]=1;		

		return array;

	}

	public static double[] kingSafety() {
		double[] newArray = new double[14]; //WHITE MOBILITY, WHITE KING IN CHECK, then checks the Pawns, Knights, Bishops, Rooks, and Queens in the King's area. Same for black.
		newArray[0]=(legalWK(findWhiteKing()).length())/5;
		newArray[1]=(whiteKingSafe())? 1:0;
		newArray[7]=(legalBK(findBlackKing()).length())/5;
		newArray[8]=(blackKingSafe())? 1:0;
		String[] pieceTypes1 = {"p","n","b","r","q"};
		String[] pieceTypes2 = {"P","N","B","R","Q"};
		int[] whiteKingZone = whiteKingZone();
		int[] blackKingZone = blackKingZone();
		for (int i = 0;i<whiteKingZone.length;i++) {
			for (int j = 0;j<pieceTypes1.length;j++) {
				if (chessBoard[whiteKingZone[i]/8][whiteKingZone[i]%8].equals(pieceTypes1[j])) {
					newArray[2+j]+=0.25;
				}
			}
		}
		for (int i = 0;i<blackKingZone.length;i++) {
			for (int j = 0;j<pieceTypes2.length;j++) {
				if (chessBoard[blackKingZone[i]/8][blackKingZone[i]%8].equals(pieceTypes2[j])) {
					newArray[2+j]+=0.25;
				}
			}
		}
		return newArray;
	}

	public static double[] pawnStructure() {
		double[] newArray = new double[16]; //If the pawn does not exist in that file, 0. If file contains passed pawn, 1. //HAVE TO CHECK HOW MANY PASSED PAWNS THERE ARE.
		for (int i = 0;i<64;i++) {
			switch(chessBoard[i/8][i%8]) {
			case "P": newArray[i%8]=0.5; 
			break;
			case "p": newArray[8+(i%8)]=0.5;
			break;
			}
		}
		for (int j = 0;j<8;j++) {
			if (newArray[j]>=0.5 && newArray[8+j]==0) newArray[j]=1;
			if (newArray[8+j]>=0.5 && newArray[j]==0) newArray[j]=1;
		}
		return newArray;
	}

	public static double[] possibleCaptures() {
		double[] newArray = new double[10]; //WP,WN,WB,WR,WQ,BP,BN,BB,BR,BQ
		String[] wMoves = legalWMoves();
		String[] wCanCapture = {"p","n","b","r","q"};
		for (int j = 0;j<wMoves.length;j++) {
			for (int i = 0;i<wCanCapture.length;i++) {
				if (wMoves[j].length()>4) {
					if (wMoves[j].substring(4,5).equals(wCanCapture[i])) {
						newArray[i]+=1;
					}
				}
			}
		}
		String[] bMoves = legalBMoves();
		String[] bCanCapture = {"P","N","B","R","Q"};
		for (int j = 0;j<bMoves.length;j++) {
			for (int i = 0;i<bCanCapture.length;i++) {
				if (bMoves[j].indexOf(bCanCapture[i])>3) {
					newArray[5+i]+=1;
				}
			}
		}
		for (int i = 0;i<newArray.length;i++) {
			newArray[i]/=2;
		}
		newArray[0]/=4;
		newArray[5]/=4;
		return newArray;

	}


	public static void print2DArrayList(ArrayList<double[]> ab, int c) {
		for (int i = 0;i<Math.min(c, ab.size());i++) {
			double[] newArray = ab.get(i);
			System.out.println(Arrays.toString(newArray));
		}
	}

	public static void print2DArray(double[][] ab, int c) {
		for (int i = 0;i<Math.min(c, ab.length);i++) {
			double[] newArray = ab[i];
			System.out.println(Arrays.toString(newArray));
		}
	}

	public static void resetBoard() {
		boolean[] a = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};
		int[] b= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		pawnDoubleMove = a;
		timePawnMoved = b;
		whiteKingMoved = 0;
		blackKingMoved = 0;
		kwRookMoved = 0;
		qwRookMoved = 0;
		kbRookMoved = 0;
		qbRookMoved = 0;
		String[][] reset = {
				//A   B   C   D   E   F   G   H
				{"r","n","b","q","k","b","n","r"},  //8
				{"p","p","p","p","p","p","p","p"},  //7
				{" "," "," "," "," "," "," "," "},  //6
				{" "," "," "," "," "," "," "," "},  //5
				{" "," "," "," "," "," "," "," "},  //4
				{" "," "," "," "," "," "," "," "},  //3
				{"P","P","P","P","P","P","P","P"},  //2
				{"R","N","B","Q","K","B","N","R"}};	//1
		chessBoard = reset;
		totalMoves=0;
	}


	public static void showAllPossibleWhiteMoves() {
		String[] legalWMoves = legalWMoves();
		if (legalWMoves[0].equals("")) System.out.println("No possible moves.");
		else {
			for (int i = 0;i<legalWMoves().length;i++) {
				makeMove(legalWMoves[i]);
				System.out.println(legalWMoves[i] + ": " + round(revertTanRating(brain.predict(chessBoardToState())),2));
				undoMove(legalWMoves[i]);
			}
		}
		System.out.println("COMPUTER SUGGESTS: " + computerChosenWhiteMove());
	}

	public static String computerChosenWhiteMove() {
		String[] legalWMoves = legalWMoves();
		double[] ratingScores = new double[legalWMoves.length];
		if (legalWMoves.length>0) {
			for (int i = 0;i<legalWMoves.length;i++) {
				makeMove(legalWMoves[i]);
				ratingScores[i]=round(revertTanRating(brain.predict(chessBoardToState())),2);
				undoMove(legalWMoves[i]);
			}
		}
		return legalWMoves[mxjava.numberDirectory(ratingScores, legalWMoves)];
	}
	
	public static String computerChosenBlackMove() {
		String[] legalBMoves = legalBMoves();
		double[] ratingScores = new double[legalBMoves.length];
		if (legalBMoves.length>0) {
			for (int i = 0;i<legalBMoves.length;i++) {
				makeMove(legalBMoves[i]);
				ratingScores[i]=round(revertTanRating(brain.predict(chessBoardToState())),2)*-1;
				undoMove(legalBMoves[i]);
			}
		}
		return legalBMoves[mxjava.numberDirectory(ratingScores, legalBMoves)];
	}


	public static void showAllPossibleBlackMoves() {
		String[] legalBMoves = legalBMoves();
		if (legalBMoves[0].equals("")) System.out.println("No possible moves.");
		else {
			for (int i = 0;i<legalWMoves().length;i++) {
				makeMove(legalBMoves[i]);
				System.out.println(legalBMoves[i]);
				moveStatusBoardPrint();
				undoMove(legalBMoves[i]);
			}
		}
	}

	public static int gameStatus() {
		//1 is a white win, 0 is a tie, -1 is a black win, and 5 is ongoing.
		if(legalWMoves()[0].equals("")) { 
			if (!whiteKingSafe()) { return -1; }
			else { return 0;} 
		}
		if(legalBMoves()[0].equals("")) { 
			if (!blackKingSafe()) { return 1; }
			else { return 0;} 
		}
		return 5;
	}

	public static void moveStatusBoardPrint() {
		System.out.println("Move #" + ((totalMoves-1)/2+1));
		String nextToMove = (totalMoves%2 == 0) ? "White" : "Black";
		System.out.println("Next to move: " + nextToMove + "\n");
		printBoard();
	}

	public static String compReadableMove(String a) {
		String move = a;
		String[] files = "abcdefgh".split("");
		if (a.length()==2) {
			for (int i = 0;i<files.length;i++) {
				move = move.replace(files[i], Integer.toString(i));
			}
		}
		if (a.length()>3) {
			if (!a.substring(4).equals("=") ) {
				for (int i = 0;i<files.length;i++) {
					move = move.replace(files[i], Integer.toString(i+1));
					if (move.substring(4).equals("2")) move = move.substring(0,4)+"b";
				}
			}
			else {
				for (int i = 0;i<files.length;i++) {
					move = move.substring(0,1).replace(files[i], Integer.toString(i+1))+move.substring(1,2).replace(files[i], Integer.toString(i+1))+move.substring(2);
					if (move.substring(4).equals("2")) move = move.substring(0,4)+"b";
				}
			}
			
		}
		return move.toString();
	}

	public static void undoMove(String a) {
		PGN_GAME_LOG = PGN_GAME_LOG.replace(moveUpdatePGN(a), "");
		a = compReadableMove(a);
		if (a.equals("O-O")) {
			if (totalMoves%2!=0) {
				chessBoard[7][4] = "K";
				chessBoard[7][7] = "R";
				chessBoard[7][6] = " ";
				chessBoard[7][5] = " ";
				whiteKingMoved--;
				kwRookMoved--;
			}
			if (totalMoves%2!=1) {
				chessBoard[0][4] = "k";
				chessBoard[0][7] = "r";
				chessBoard[0][6] = " ";
				chessBoard[0][5] = " ";
				blackKingMoved--;
				kbRookMoved--;
			}
		}
		if (a.length()>3 && a.length()<=4) {
			String temp = chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1];
			chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1] = chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1];
			chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1] = " ";
			if (chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1].equals("")) chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1]=" ";
			//CHECKING EN PASSANT
			if (temp.equals("P") && a.substring(1, 2).equals("2")) {pawnDoubleMove[Integer.parseInt(a.substring(0,1))-1] = false; timePawnMoved[Integer.parseInt(a.substring(0,1))-1] = 0;}
			if (temp.equals("p") && a.substring(1, 2).equals("7")) {pawnDoubleMove[Integer.parseInt(a.substring(0,1))+7] = false; timePawnMoved[Integer.parseInt(a.substring(0,1))+7] = 0;}
			//CHECKING CASTLE LEGALITY
			if (temp.equals("K")) whiteKingMoved--;
			if (temp.equals("k")) blackKingMoved--;
			if (temp.equals("R") && a.substring(0,2).equals("a1")) qwRookMoved--;
			if (temp.equals("R") && a.substring(0,2).equals("h1")) kwRookMoved--;
			if (temp.equals("r") && a.substring(0,2).equals("a8")) qbRookMoved--;
			if (temp.equals("r") && a.substring(0,2).equals("h8")) kbRookMoved--;

		}
		else if (a.length()>4) {
			if (a.substring(4).equals("=")) {
				if (totalMoves%2!=0) {
					chessBoard[1][Character.getNumericValue(a.charAt(0))-1] = "P";
					chessBoard[0][Character.getNumericValue(a.charAt(1))-1] = (a.substring(2, 3).equals("/")) ? " " : a.substring(2, 3);
				}
				else if (totalMoves%2!=1) {
					chessBoard[6][Character.getNumericValue(a.charAt(0))-1] = "p";
					chessBoard[7][Character.getNumericValue(a.charAt(1))-1] = (a.substring(2, 3).equals("/")) ? " " : a.substring(2, 3);
				}
			}
			else if (a.substring(4,5).equals("s")) {
				chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1] = " ";
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1] = (totalMoves%2==0)? "p":"P";
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(2))-1] = (totalMoves%2==0)? "P":"p";

			}
			else if (a.equals("O-O-O")) {
				if (totalMoves%2!=0) {
					chessBoard[7][4] = "K";
					chessBoard[7][0] = "R";
					chessBoard[7][2] = " ";
					chessBoard[7][3] = " ";
					whiteKingMoved--;
					qwRookMoved--;
				}
				if (totalMoves%2!=1) {
					chessBoard[0][4] = "k";
					chessBoard[0][0] = "r";
					chessBoard[0][2] = " ";
					chessBoard[0][3] = " ";
					blackKingMoved--;
					qbRookMoved--;
				}
			}
			else {
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1] = chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1];
				chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1] = String.valueOf(a.charAt(4));
			}
		}
	}

	public static String moveUpdatePGN(String raw) {
		String a = compReadableMove(raw);
		String moveMarker = (totalMoves%2==0) ? (totalMoves/2 + 1) + "." : "";
		if (a.equals("O-O") || a.equals("O-O-O")) {
			return moveMarker+a;
		}
		else if (a.length()>4) {
			if (!a.substring(4).equals("=")) {
				if (chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1].toUpperCase().equals("P")) {
					return moveMarker+raw.substring(0,1) + "x" + raw.substring(2,4);
				}

				else {
					if (totalMoves%2==0) {
						if (mxjava.specifyInitial(legalWMoves(), chessBoard, a)) return moveMarker+(chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1].toUpperCase() + raw.substring(0,1)+"x"+raw.substring(2,4)).replace("P", "");
						else return moveMarker+(chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1].toUpperCase() + "x"+raw.substring(2,4)).replace("P", "");
					}
					else if (totalMoves%2==1) {
						if (mxjava.specifyInitial(legalBMoves(), chessBoard, a)) return moveMarker+(chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1].toUpperCase() + raw.substring(0,1)+"x"+raw.substring(2,4)).replace("P", "");
						else return moveMarker+(chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1].toUpperCase() + "x"+raw.substring(2,4)).replace("P", "");
					}}
			}
			else {
				if (totalMoves%2==0) { 
					if (raw.substring(0,1).equals(raw.substring(1,2))) {
						return moveMarker+raw.substring(1,2) +"8="+raw.substring(3,4);
					}
					else {
						return moveMarker+raw.substring(0,1) + "x" + raw.substring(1,2) +"8="+raw.substring(3,4);
					}
				}
				else if (totalMoves%2==1) { 
					if (raw.substring(2,3) == "/") {
						return moveMarker+raw.substring(1,2) +"1="+raw.substring(3,4).toUpperCase();
					}
					else {
						return moveMarker+raw.substring(0,1) + "x" + raw.substring(1,2) +"1="+raw.substring(3,4).toUpperCase();
					}
				}
			}
		}
		else if (raw.length()==4) {	
			if (totalMoves%2==0) {
				if (mxjava.specifyInitial(legalWMoves(), chessBoard, a)) return moveMarker+(chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1].toUpperCase() + raw.substring(0,1)+raw.substring(2,4)).replace("P", "");
				else return moveMarker+(chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1].toUpperCase() + raw.substring(2,4)).replace("P", "");
			}
			else if (totalMoves%2==1) {
				if (mxjava.specifyInitial(legalBMoves(), chessBoard, a)) return moveMarker+(chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1].toUpperCase() + raw.substring(0,1)+raw.substring(2,4)).replace("P", "");
				else return moveMarker+(chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1].toUpperCase() + raw.substring(2,4)).replace("P", "");
			}
		}
		return "";
	}

	public static void makeMove(String a) {
		String raw = a;
		PGN_GAME_LOG += moveUpdatePGN(raw);
		a = compReadableMove(a);

		if (a.length()>3) {
			if (a.substring(4).equals("=")) {
				if (totalMoves%2==0) {
					chessBoard[1][Character.getNumericValue(a.charAt(0))-1] = " ";
					chessBoard[0][Character.getNumericValue(a.charAt(1))-1] = a.substring(3,4);
				}
				else if (totalMoves%2==1) {
					chessBoard[6][Character.getNumericValue(a.charAt(0))-1] = " ";
					chessBoard[7][Character.getNumericValue(a.charAt(1))-1] = a.substring(3,4);
				}

			}
			else if (a.substring(4).equals("s")) {
				String temp = chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1];
				chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1] = temp;
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1] = " ";
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(2))-1] = " ";
			}
			else if (a.equals("O-O-O")) {
				if (totalMoves%2==0) {
					chessBoard[7][4] = " ";
					chessBoard[7][0] = " ";
					chessBoard[7][2] = "K";
					chessBoard[7][3] = "R";
					whiteKingMoved++;
					qwRookMoved++;
				}
				else if (totalMoves%2==1) {
					chessBoard[0][4] = " ";
					chessBoard[0][0] = " ";
					chessBoard[0][2] = "k";
					chessBoard[0][3] = "r";
					blackKingMoved++;
					qbRookMoved++;
				}
			}
			else {
				String temp = chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1];
				chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1] = temp;
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1] = " ";
				//CHECKING EN PASSANT LEGALITY
				if (temp.equals("P") && a.substring(1, 2).equals("2") && a.substring(3, 4).equals("4")) {pawnDoubleMove[Integer.parseInt(a.substring(0,1))-1] = true; timePawnMoved[Integer.parseInt(a.substring(0,1))-1] = totalMoves+1;}
				if (temp.equals("p") && a.substring(1, 2).equals("7") && a.substring(3, 4).equals("5")) {pawnDoubleMove[Integer.parseInt(a.substring(0,1))+7] = true; timePawnMoved[Integer.parseInt(a.substring(0,1))+7] = totalMoves+1;}
				if (temp.equals("K")) whiteKingMoved++;
				if (temp.equals("k")) blackKingMoved++;
				if (temp.equals("R") && a.substring(0,2).equals("a1")) { qwRookMoved++; chessBoard[0][0]=" ";}
				if (temp.equals("R") && a.substring(0,2).equals("h1")) { kwRookMoved++; chessBoard[7][0]=" ";}
				if (temp.equals("r") && a.substring(0,2).equals("a8")) {qbRookMoved++; chessBoard[0][7]=" ";}
				if (temp.equals("r") && a.substring(0,2).equals("h8")) {kbRookMoved++; chessBoard[7][7]=" ";}
			}
		}
		else {
			if (a.equals("O-O")) {
				if (totalMoves%2==0) {
					chessBoard[7][4] = " ";
					chessBoard[7][7] = " ";
					chessBoard[7][6] = "K";
					chessBoard[7][5] = "R";
					whiteKingMoved++;
					kwRookMoved++;
				}
				else if (totalMoves%2==1) {
					chessBoard[0][4] = " ";
					chessBoard[0][7] = " ";
					chessBoard[0][6] = "k";
					chessBoard[0][5] = "r";
					blackKingMoved++;
					kbRookMoved++;
				}
			}
		}
		if ((!whiteKingSafe() || !blackKingSafe()) && gameStatus()==5) PGN_GAME_LOG+= "+";
		if ((!whiteKingSafe() || !blackKingSafe()) && (gameStatus()==1 || gameStatus()==-1)) PGN_GAME_LOG+= "# ";
		else {
			PGN_GAME_LOG+=" ";
		}

	}

	public static void printBoard() {
		System.out.println(" A  B  C  D  E  F  G  H");
		System.out.println("------------------------");
		for (int i = 0;i<8;i++) {
			System.out.println(Arrays.toString(chessBoard[i]) + " |" + Integer.valueOf(8-(i)) + "| ");
		}
		System.out.println("------------------------");
	}
	

	//VERSION 2
	public static double[] chessBoardToState() {
		double[] newArray = mxjava.appendArrays(materialArray(), castlePossible());
		newArray = mxjava.appendArrays(newArray, fPawn());
		newArray = mxjava.appendArrays(newArray, simplePositionalEval());
		newArray = mxjava.appendArrays(newArray, simplePositionalEvalSummed());
		newArray = mxjava.appendArrays(newArray, piecesInMiddle());
		//newArray = mxjava.appendArrays(newArray, mobilitySafe());
		newArray = mxjava.appendArrays(newArray, mobility());
		newArray = mxjava.appendArrays(newArray, wherePiecesAre());
		newArray = mxjava.appendArrays(newArray, possibleCaptures());
		newArray = mxjava.appendArrays(newArray, kingSafety());
		newArray = mxjava.appendArrays(newArray, rateAttack());
		newArray = mxjava.appendArrays(newArray, castlePossible());
		newArray = mxjava.appendArrays(newArray, pawnStructure());
		newArray = mxjava.appendArrays(newArray, materialPoints());
		return newArray;
	}

	public static double[] endBoardToState() {
		double[] newArray = mxjava.appendArrays(materialArray(), pawnStructure());
		newArray = mxjava.appendArrays(newArray, simplePositionalEval());
		newArray = mxjava.appendArrays(newArray, simplePositionalEvalSummed());
		newArray = mxjava.appendArrays(newArray, piecesInMiddle());
		//newArray = mxjava.appendArrays(newArray, mobilitySafe());
		newArray = mxjava.appendArrays(newArray, mobility());
		newArray = mxjava.appendArrays(newArray, wherePiecesAre());
		newArray = mxjava.appendArrays(newArray, possibleCaptures());
		newArray = mxjava.appendArrays(newArray, kingSafety());
		newArray = mxjava.appendArrays(newArray, rateAttack());
		newArray = mxjava.appendArrays(newArray, control());
		newArray = mxjava.appendArrays(newArray, materialPoints());
		return newArray;
	}
	
	//This checks the mobilitySafe of each piece. It uses the list of moves, and finds where each piece originates from.
	public static double[] mobilitySafe() {
		double[] mobilitySafe = new double[24]; //WNX2,WBX2,WRX2,WQ,BNX2,BBX2,BBX2,BQ, SPARESx2, Total Knight Mobilityx2, Total Bishop Mobilityx2, Total Rook Mobilityx2, Total Queen Mobilityx2
		String[] piecePositions = new String[18]; 
		for (int i = 0;i<piecePositions.length;i++) {
			piecePositions[i]="";
		}

		String[] allWhite = legalNonKingWMoves();
		String[] allBlack = legalNonKingBMoves();

		for (int i = 0;i<allWhite.length;i++) {
			if (allWhite[i].length()>0) {
				String startingPoint=compReadableMove(allWhite[i]);
				String pieceAtStartingPoint = chessBoard[8-(Character.getNumericValue(startingPoint.charAt(1)))][Character.getNumericValue(startingPoint.charAt(0))-1];
				if(!pieceAtStartingPoint.equals("P") && !pieceAtStartingPoint.equals("K") && mxjava.sumOfArrayParts(controlSquareB(allWhite[i].substring(2,4)))==0) {
					String[] typesOfPieces={"N","B","R"};
					for (int j = 0;j<typesOfPieces.length;j++) {
						if (pieceAtStartingPoint.equals(typesOfPieces[j])) {
							if (piecePositions[2*j].equals("")) { piecePositions[2*j]=startingPoint.substring(0, 2); mobilitySafe[2*j]++; mobilitySafe[16+2*j]++;}
							else if (startingPoint.substring(0, 2).equals(piecePositions[2*j])) { mobilitySafe[2*j]++; mobilitySafe[16+2*j]++;}
							else if (startingPoint.substring(0, 2).equals(piecePositions[2*j+1])) { mobilitySafe[2*j+1]++; mobilitySafe[16+2*j]++;}
							else {
								if (piecePositions[2*j+1].equals("")) { piecePositions[2*j+1]=startingPoint.substring(0,2); mobilitySafe[2*j+1]++; mobilitySafe[16+2*j]++;}
								else {
									mobilitySafe[14]++;
									mobilitySafe[16+2*j]++;
								}
							}
						}
					}	
				}
				//Check Queen Mobility
				if (pieceAtStartingPoint.equals("Q") && mxjava.sumOfArrayParts(controlSquareB(allWhite[i].substring(2,4)))==0) {
					if (piecePositions[6].equals("")) {	piecePositions[6]=startingPoint.substring(0, 2); mobilitySafe[6]++; mobilitySafe[22]++;}
					else if (startingPoint.substring(0,2).equals(piecePositions[6])) {mobilitySafe[6]++; mobilitySafe[22]++;}
					else {
						mobilitySafe[14]++;
						mobilitySafe[22]++;
					}

				}
			}
		}
		for (int i = 0;i<allBlack.length;i++) {
			if (allBlack[i].length()>0) {
				String startingPoint=compReadableMove(allBlack[i]);
				String pieceAtStartingPoint = " ";
				pieceAtStartingPoint = chessBoard[Math.max(0, Math.min(7,8-(Character.getNumericValue(startingPoint.charAt(1)))))][Math.max(0, Math.min(7, Character.getNumericValue(startingPoint.charAt(0))-1))];
				if(!pieceAtStartingPoint.equals("p") && !pieceAtStartingPoint.equals("k") && mxjava.sumOfArrayParts(controlSquareW(allBlack[i].substring(2,4)))==0) {
					//do stuff
					String[] typesOfPieces={"n","b","r"};
					for (int j = 0;j<typesOfPieces.length;j++) {
						if (pieceAtStartingPoint.equals(typesOfPieces[j])) {
							if (piecePositions[7+2*j].equals("")) { piecePositions[7+2*j]=startingPoint.substring(0, 2); mobilitySafe[7+2*j]++; mobilitySafe[17+2*j]++;}
							else if (startingPoint.substring(0, 2).equals(piecePositions[7+2*j])) { mobilitySafe[7+2*j]++; mobilitySafe[17+2*j]++;}
							else if (startingPoint.substring(0, 2).equals(piecePositions[7+2*j+1]))  { mobilitySafe[7+2*j+1]++; mobilitySafe[17+2*j]++;}
							else {
								if (piecePositions[7+2*j+1].equals("")) { piecePositions[7+2*j+1]=startingPoint.substring(0,2); mobilitySafe[7+2*j+1]++; mobilitySafe[17+2*j]++;}
								else {
									mobilitySafe[15]++;
									mobilitySafe[17+2*j]++;
								}
							}
						}
					}	
				}
				if (pieceAtStartingPoint.equals("q") && mxjava.sumOfArrayParts(controlSquareW(allBlack[i].substring(2,4)))==0) {
					if (piecePositions[13].equals("")) {	piecePositions[13]=startingPoint.substring(0, 2); mobilitySafe[13]++; mobilitySafe[23]++;}
					else if (startingPoint.substring(0,2).equals(piecePositions[13])) {mobilitySafe[13]++; mobilitySafe[23]++;}
					else {
						mobilitySafe[15]++;
						mobilitySafe[23]++;
					}
				}
			}
		}
		for (int i = 0;i<mobilitySafe.length;i++) {
			mobilitySafe[i]/=8;
			mobilitySafe[i]-=0.1;
		}
		return mobilitySafe;
	}


	public static int findWhiteKing() {
		for (int i = 0;i<64;i++) {
			if (chessBoard[i/8][i%8].equals("K")) return i;
		}
		return 0;
	}


	//Future additions = Passed Pawn, King Safety, Open File
	//HERE ARE NEW EVALUATION FUNCTIONS TO HELP WITH THE BOARD REPRESENTATION 
	public static double[] materialArray() {
		double[] material = new double[14]; //Pawn, Knight, Bishop, Rook, Queen [ White 5 : Black 5], White Double Knight, White Double Bishop, Black Double Knight, Black Double Bishop [3 = Yes, 0 = No]
		for (int i = 0;i<64;i++) {
			switch(chessBoard[i/8][i%8]) {
			case "P":material[0]+=1;
			break;
			case "N":material[1]+=1;
			break;
			case "B":material[2]+=1;
			break;
			case "R":material[3]+=1;
			break;
			case "Q":material[4]+=1;
			break;
			case "p":material[5]+=1;
			break;
			case "n":material[6]+=1;
			break;
			case "b":material[7]+=1;
			break;
			case "r":material[8]+=1;
			break;
			case "q":material[9]+=1;
			break;
			}
		}
		if(material[1]==2) material[10]=1;
		if(material[2]==2) material[11]=1;
		if(material[6]==2) material[12]=1;
		if(material[7]==2) material[13]=1;
		material[0]/=8;
		material[5]/=8;
		material[1]/=2;
		material[2]/=2;
		material[3]/=2;
		material[4]/=2;
		material[6]/=2;
		material[7]/=2;
		material[8]/=2;
		material[9]/=2;

		return material;
	}

	public static double[] materialPoints() {
		double[] newArray = new double[1];
		for (int i = 0;i<64;i++) {
			switch(chessBoard[i/8][i%8]) {
			case "P":newArray[0]+=100;
			break;
			case "N":newArray[0]+=320;
			break;
			case "B":newArray[0]+=335;
			break;
			case "R":newArray[0]+=500;
			break;
			case "Q":newArray[0]+=990;
			break;
			case "p":newArray[0]-=100;
			break;
			case "n":newArray[0]-=325;
			break;
			case "b":newArray[0]-=335;
			break;
			case "r":newArray[0]-=500;
			break;
			case "q":newArray[0]-=990;
			break;
			}
		}
		return newArray;
	}
	
	public static double totalMaterial() {
		double material = 0; //Pawn, Knight, Bishop, Rook, Queen [ White 5 : Black 5], White Double Knight, White Double Bishop, Black Double Knight, Black Double Bishop [3 = Yes, 0 = No]
		for (int i = 0;i<64;i++) {
			switch(chessBoard[i/8][i%8]) {
			case "P":material+=100;
			break;
			case "N":material+=320;
			break;
			case "B":material+=335;
			break;
			case "R":material+=500;
			break;
			case "Q":material+=990;
			break;
			case "p":material+=100;
			break;
			case "n":material+=325;
			break;
			case "b":material+=335;
			break;
			case "r":material+=500;
			break;
			case "q":material+=990;
			break;
			}
		}
		return material;
	}


	public static double[] castlePossible() {
		//WHITE SHORT, WHITE LONG, THEN BLACK SHORT, BLACK LONG. Yes is 1, No is 0.
		double[] possibleCastle = new double[4];
		if (kwRookMoved==0 && whiteKingMoved==0) possibleCastle[0]=1;
		if (qwRookMoved==0 && whiteKingMoved==0) possibleCastle[1]=1;
		if (kbRookMoved==0 && blackKingMoved==0) possibleCastle[2]=1;
		if (qbRookMoved==0 && blackKingMoved==0) possibleCastle[3]=1;
		return possibleCastle;
	}

	//The F File and its protection is crucial. 
	//The first two checks if a piece is on f2/f7. If there is no piece, then -1 will be shown.;
	//the second checks if a piece protects the f2/f7 square. It counts the number of defenders of that square not named the King.
	//the third checks if the f2/f7 square is attacked by an opponent. It counts the number of attackers of that square. 
	//The second and third checks only count for different ways of attack. Rooks and Queens in the same row are discounted.
	public static double[] fPawn() {
		double[] fPawnProtection = new double[6];
		if (chessBoard[6][5].equals(" ")) fPawnProtection[0]=-1;
		if (chessBoard[1][5].equals(" ")) fPawnProtection[1]=-1;
		fPawnProtection = mxjava.appendArrays(fPawnProtection, controlSquareW("f2"));
		fPawnProtection = mxjava.appendArrays(fPawnProtection, controlSquareW("f7"));
		fPawnProtection = mxjava.appendArrays(fPawnProtection, controlSquareB("f2"));
		fPawnProtection = mxjava.appendArrays(fPawnProtection, controlSquareB("f7"));
		return fPawnProtection;
	}

	public static double[] controlSquareW(String move) {
		double[] count = new double[5]; //Pawn,Knight,Bishop,Rook,Queen
		String place = compReadableMove(move);
		int directory = (8*(8-Character.getNumericValue(place.charAt(1))))+(Character.getNumericValue(place.charAt(0)));
		//check pawns.
		if (directory%8!=0) { //not on a file 
			int temp = directory+8;
			if (chessBoard[temp/8][temp%8].equals("P")) {count[0]+=0.5;}
		}
		if (directory%8!=7) { //not on h file 
			int temp = directory+6;
			if (chessBoard[temp/8][temp%8].equals("P")) {count[0]+=0.5;}
		}

		//check knights.
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					if ("N".equals(chessBoard[directory/8+i][directory%8+j*2])) {
						count[1]+=0.3;
					}
				} catch (Exception e) {}
				try {
					if ("N".equals(chessBoard[directory/8+i*2][directory%8+j])) {
						count[1]+=0.3;
					}
				} catch (Exception e) {}
			}
		}
		//check diagonals.
		int temp=1;
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					while(" ".equals(chessBoard[directory/8+temp*i][directory%8+temp*j]) || Character.isUpperCase(chessBoard[directory/8+temp*i][directory%8+temp*j].charAt(0))) {temp++;}
					if ("B".equals(chessBoard[directory/8+temp*i][directory%8+temp*j])) {
						count[2]+=0.3;
					}
					if ("Q".equals(chessBoard[directory/8+temp*i][directory%8+temp*j])) {
						count[4]+=0.3;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		//check files and ranks.
		for (int i=-1; i<=1; i+=2) {
			try {
				while(" ".equals(chessBoard[directory/8][directory%8+temp*i]) || Character.isUpperCase(chessBoard[directory/8][directory%8+temp*i].charAt(0))) {temp++;}
				if ("R".equals(chessBoard[directory/8][directory%8+temp*i])) {
					count[3]+=0.3;
				}
				if ("Q".equals(chessBoard[directory/8][directory%8+temp*i])) {
					count[4]+=0.3;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[directory/8+temp*i][directory%8])  || Character.isUpperCase(chessBoard[directory/8+temp*i][directory%8].charAt(0))) {temp++;}
				if ("R".equals(chessBoard[directory/8+temp*i][directory%8])) {
					count[3]+=0.3;
				}
				if ("Q".equals(chessBoard[directory/8+temp*i][directory%8])) {
					count[4]+=0.3;
				}
			} catch (Exception e) {}
			temp=1;
		}


		return count;
	}
	
	public static double[] controlSquareB(String move) {
		double[] count = new double[5]; //Pawn,Knight,Bishop,Rook,Queen
		String place = compReadableMove(move);
		int directory = (8*(8-Character.getNumericValue(place.charAt(1))))+(Character.getNumericValue(place.charAt(0)));
		//check pawns.
		if (directory%8!=0) { //not on a file 
			int temp = directory-8;
			if (chessBoard[temp/8][temp%8].equals("p")) {count[0]+=0.5;}
		}
		if (directory%8!=7) { //not on h file 
			int temp = directory-8;
			if (chessBoard[temp/8][temp%8].equals("p")) {count[0]+=0.5;}
		}

		//check knights.
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					if ("n".equals(chessBoard[directory/8+i][directory%8+j*2])) {
						count[1]+=0.3;
					}
				} catch (Exception e) {}
				try {
					if ("n".equals(chessBoard[directory/8+i*2][directory%8+j])) {
						count[1]+=0.3;
					}
				} catch (Exception e) {}
			}
		}
		//check diagonals.
		int temp=1;
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					while(" ".equals(chessBoard[directory/8+temp*i][directory%8+temp*j]) || Character.isLowerCase(chessBoard[directory/8+temp*i][directory%8+temp*j].charAt(0))) {temp++;}
					if ("b".equals(chessBoard[directory/8+temp*i][directory%8+temp*j])) {
						count[2]+=0.3;
					}
					if ("q".equals(chessBoard[directory/8+temp*i][directory%8+temp*j])) {
						count[4]+=0.3;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		//check files and ranks.
		for (int i=-1; i<=1; i+=2) {
			try {
				while(" ".equals(chessBoard[directory/8][directory%8+temp*i]) || Character.isLowerCase(chessBoard[directory/8][directory%8+temp*i].charAt(0))) {temp++;}
				if ("r".equals(chessBoard[directory/8][directory%8+temp*i])) {
					count[3]+=0.3;
				}
				if ("q".equals(chessBoard[directory/8][directory%8+temp*i])) {
					count[4]+=0.3;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[directory/8+temp*i][directory%8])  || Character.isLowerCase(chessBoard[directory/8+temp*i][directory%8].charAt(0))) {temp++;}
				if ("r".equals(chessBoard[directory/8+temp*i][directory%8])) {
					count[3]+=0.3;
				}
				if ("q".equals(chessBoard[directory/8+temp*i][directory%8])) {
					count[4]+=0.3;
				}
			} catch (Exception e) {}
			temp=1;
		}


		return count;
	}

	//WILL HAVE TO BE CHANGED TO ADD PAWN PROTECTION.
	public static int moveWAble(String move) {
		int count = 0;
		String place = compReadableMove(move);
		String temp = chessBoard[8-Character.getNumericValue(place.charAt(1))][Character.getNumericValue(place.charAt(0))];
		chessBoard[8-Character.getNumericValue(place.charAt(1))][Character.getNumericValue(place.charAt(0))]=" ";
		String[] destinations = destinationOfNonKingPiecesW();
		for (int i = 0;i<destinations.length;i++) {
			if (destinations[i].equals(move)) { 
				count++; }
		}
		chessBoard[8-Character.getNumericValue(place.charAt(1))][Character.getNumericValue(place.charAt(0))]=temp;
		return count;
	}

	public static int moveBAble(String move) {
		int count = 0;
		String place = compReadableMove(move);
		String temp = chessBoard[8-Character.getNumericValue(place.charAt(1))][Character.getNumericValue(place.charAt(0))];
		chessBoard[8-Character.getNumericValue(place.charAt(1))][Character.getNumericValue(place.charAt(0))]=" ";
		String[] destinations = destinationOfNonKingPiecesB();
		for (int i = 0;i<destinations.length;i++) {
			if (destinations[i].equals(move)) { 
				count++; }
		}
		chessBoard[8-Character.getNumericValue(place.charAt(1))][Character.getNumericValue(place.charAt(0))]=temp;
		return count;
	}

	public static double[] simplePositionalEvalSummed() {
		double[] counter=new double[2]; //Can be combined into two numbers. But this is the precaution for now.
		for (int i=0;i<64;i++) {
			switch (chessBoard[i/8][i%8]) {
			case "P": counter[0]+=pawnBoard[i/8][i%8];
			break;
			case "R": counter[0]+=rookBoard[i/8][i%8];
			break;
			case "N": counter[0]+=knightBoard[i/8][i%8];
			break;
			case "B": counter[0]+=bishopBoard[i/8][i%8];
			break;
			case "Q": counter[0]+=queenBoard[i/8][i%8];
			break;
			case "K": if (totalMaterial()>=3500) { counter[0]+=kingBoard[i/8][i%8]; } else { counter[0]+=kingEndBoard[i/8][i%8]; }counter[0]+=(legalWK(findWhiteKing()).length())/5;
			break;
			case "p": counter[1]+=pawnBoard[7-(i/8)][7-(i%8)];
			break;
			case "r": counter[1]+=rookBoard[7-(i/8)][7-(i%8)];
			break;
			case "n": counter[1]+=knightBoard[7-(i/8)][7-(i%8)];
			break;
			case "b": counter[1]+=bishopBoard[7-(i/8)][7-(i%8)];
			break;
			case "q": counter[1]+=queenBoard[7-(i/8)][7-(i%8)];
			break;
			case "k": if (totalMaterial()>=3500) { counter[0]+=kingBoard[7-(i/8)][7-(i%8)]; } else { counter[0] += kingEndBoard[7-(i/8)][7-(i%8)]; }counter[0]+=(legalBK(findBlackKing()).length())/5;
			break;
			}
		}
		counter[0]/=700;
		counter[1]/=700;
		return counter;
	}

	public static double[] simplePositionalEval() {
		double[] counter=new double[15]; //Can be combined into two numbers. But this is the precaution for now. 15th number is the player to move.
		for (int i=0;i<64;i++) {
			switch (chessBoard[i/8][i%8]) {
			case "P": counter[0]+=pawnBoard[i/8][i%8];
			break;
			case "R": counter[1]+=rookBoard[i/8][i%8];
			break;
			case "N": counter[2]+=knightBoard[i/8][i%8];
			break;
			case "B": counter[3]+=bishopBoard[i/8][i%8];
			break;
			case "Q": counter[4]+=queenBoard[i/8][i%8];
			break;
			case "K": if (totalMaterial()>=3500) { counter[5]+=kingBoard[i/8][i%8]; } else { counter[5]+=kingEndBoard[i/8][i%8]; }counter[5]+=(legalWK(findWhiteKing()).length())/5;
			break;
			case "p": counter[6]+=pawnBoard[7-(i/8)][7-(i%8)];
			break;
			case "r": counter[7]+=rookBoard[7-(i/8)][7-(i%8)];
			break;
			case "n": counter[8]+=knightBoard[7-(i/8)][7-(i%8)];
			break;
			case "b": counter[9]+=bishopBoard[7-(i/8)][7-(i%8)];
			break;
			case "q": counter[10]+=queenBoard[7-(i/8)][7-(i%8)];
			break;
			case "k": if (totalMaterial()>=3500) { counter[11]+=kingBoard[7-(i/8)][7-(i%8)]; } else { counter[11] += kingEndBoard[7-(i/8)][7-(i%8)]; }counter[11]+=(legalBK(findBlackKing()).length())/5;
			break;
			}
		}
		for (int i = 0;i<12;i++) {
			counter[i]/=100;
		}
		counter[1]*=8;
		counter[7]*=8;
		counter[3]*=5;
		counter[9]*=5;
		counter[4]*=2.5;
		counter[10]*=2.5;
		counter[5]*=2;
		counter[11]*=2;
		if (totalMaterial()<3500) {counter[0]*=2; counter[6]*=2;}
		counter[12]=counter[0]+counter[1]+counter[2]+counter[3]+counter[4]+counter[5];
		counter[13]=counter[6]+counter[7]+counter[8]+counter[9]+counter[10]+counter[11];
		counter[14]=totalMoves%2;
		return counter;
	}

	//Gives 6 numbers = Pieces of White in middle 4 squares [d4,d5,e4,e5], then in the corners c3,f3,c6 and f6, then in the adjacent squared of c4,c5, f4,f5. Same for Black
	//Gives another 8 numbers for the amount of attackers on e4, e5, d4 and d5 by White first, then Black.


	//TO DO: PAWN/KNIGHT/BISHOP IN CENTER (12) [trial: 6], WHITE KNIGHT AT C3 AND F3 (2), KNIGHT AT C6 AND F6 (2), W OR B BISHOP/KNIGHT/QUEEN AT C4,F4,C5,F5. (12) -> 28 [trial:22].
	//THEN, PIECES THAT ATTACK THE MIDDLE 4 SQUARES, AND THE PIECES THAT CAN CONTROL/PROTECT A MIDDLE SQUARE
	public static double[] piecesInMiddle() {
		double[] middlePieces = new double[22]; //
		String[] middleW={"N","B","P"};
		String[] middleB={"n","b","p"};
		for (int i = 1;i<=2;i++) {
			for (int j = 1;j<=2;j++) {
				int row = 2+i;
				int column = 2+j;
				for (int k=0;k<middleW.length;k++) {
					/*
					if (chessBoard[row][column].equals(middleW[k])) middlePieces[3*(2*(i-1)+(j-1))+k]=1;
					if (chessBoard[row][column].equals(middleB[k])) middlePieces[3*(2*(i-1)+(j-1))+k]=-1;
					*/
					if (chessBoard[row][column].equals(middleW[k])) middlePieces[k]=1;
					if (chessBoard[row][column].equals(middleB[k])) middlePieces[3+k]=-1;
				}
			}
		}
		String[] flankMiddleW={"N","B","Q"};
		String[] flankMiddleB={"n","b","q"};
		for (int i = 1;i<=2;i++) {
			for (int j = 1;j<=2;j++) {
				int row = 2+i;
				int column = (3*j)-1;
				for (int k=0;k<middleW.length;k++) {
					if (chessBoard[row][column].equals(flankMiddleW[k])) middlePieces[6+3*(2*(i-1)+(j-1))+k]=1;
					if (chessBoard[row][column].equals(flankMiddleB[k])) middlePieces[6+3*(2*(i-1)+(j-1))+k]=-1;
				}
			}
		}
		if (chessBoard[5][2].equals("N")) middlePieces[18]=1; //c3
		if (chessBoard[5][5].equals("N")) middlePieces[19]=1; //f3
		if (chessBoard[2][2].equals("n")) middlePieces[20]=1; //c6
		if (chessBoard[2][5].equals("n")) middlePieces[21]=1; //f6
		middlePieces = mxjava.appendArrays(middlePieces, controlSquareW("d4"));
		middlePieces = mxjava.appendArrays(middlePieces, controlSquareW("d5"));
		middlePieces = mxjava.appendArrays(middlePieces, controlSquareW("e4"));
		middlePieces = mxjava.appendArrays(middlePieces, controlSquareW("e5"));
		middlePieces = mxjava.appendArrays(middlePieces, controlSquareB("d4"));
		middlePieces = mxjava.appendArrays(middlePieces, controlSquareB("d5"));
		middlePieces = mxjava.appendArrays(middlePieces, controlSquareB("e4"));
		middlePieces = mxjava.appendArrays(middlePieces, controlSquareB("e5"));
		return middlePieces;
	}

	//This checks the mobility of each piece. It uses the list of moves, and finds where each piece originates from.
	public static double[] mobility() {
		double[] mobility = new double[24]; //WNX2,WBX2,WRX2,WQ,BNX2,BBX2,BBX2,BQ, SPARESx2, Total Knight Mobilityx2, Total Bishop Mobilityx2, Total Rook Mobilityx2, Total Queen Mobilityx2
		String[] piecePositions =new String[18]; 
		for (int i = 0;i<piecePositions.length;i++) {
			piecePositions[i]="";
		}

		String[] allWhite = legalNonKingWMoves();
		String[] allBlack = legalNonKingBMoves();

		for (int i = 0;i<allWhite.length;i++) {
			if (allWhite[i].length()>0) {
				String startingPoint=compReadableMove(allWhite[i]);
				String pieceAtStartingPoint = chessBoard[8-(Character.getNumericValue(startingPoint.charAt(1)))][Character.getNumericValue(startingPoint.charAt(0))-1];
				if(!pieceAtStartingPoint.equals("P") && !pieceAtStartingPoint.equals("K")) {
					//do stuff
					String[] typesOfPieces={"N","B","R"};
					for (int j = 0;j<typesOfPieces.length;j++) {
						if (pieceAtStartingPoint.equals(typesOfPieces[j])) {
							if (piecePositions[2*j].equals("")) { piecePositions[2*j]=startingPoint.substring(0, 2); mobility[2*j]++; mobility[16+2*j]++;}
							else if (startingPoint.substring(0, 2).equals(piecePositions[2*j])) { mobility[2*j]++; mobility[16+2*j]++;}
							else if (startingPoint.substring(0, 2).equals(piecePositions[2*j+1])) { mobility[2*j+1]++; mobility[16+2*j]++;}
							else {
								if (piecePositions[2*j+1].equals("")) { piecePositions[2*j+1]=startingPoint.substring(0,2); mobility[2*j+1]++; mobility[16+2*j]++;}
								else {
									mobility[14]++;
									mobility[16+2*j]++;
								}
							}
						}
					}	
				}
				//Check Queen Mobility
				if (pieceAtStartingPoint.equals("Q")) {
					if (piecePositions[6].equals("")) {	piecePositions[6]=startingPoint.substring(0, 2); mobility[6]++; mobility[22]++;}
					else if (startingPoint.substring(0,2).equals(piecePositions[6])) {mobility[6]++; mobility[22]++;}
					else {
						mobility[14]++;
						mobility[22]++;
					}

				}
			}
		}
		for (int i = 0;i<allBlack.length;i++) {
			if (allBlack[i].length()>0) {
				String startingPoint=compReadableMove(allBlack[i]);
				String pieceAtStartingPoint = " ";
				pieceAtStartingPoint = chessBoard[Math.max(0, Math.min(7,8-(Character.getNumericValue(startingPoint.charAt(1)))))][Math.max(0, Math.min(7, Character.getNumericValue(startingPoint.charAt(0))-1))];

				if(!pieceAtStartingPoint.equals("p") && !pieceAtStartingPoint.equals("k")) {
					//do stuff
					String[] typesOfPieces={"n","b","r"};
					for (int j = 0;j<typesOfPieces.length;j++) {
						if (pieceAtStartingPoint.equals(typesOfPieces[j])) {
							if (piecePositions[7+2*j].equals("")) { piecePositions[7+2*j]=startingPoint.substring(0, 2); mobility[7+2*j]++; mobility[17+2*j]++;}
							else if (startingPoint.substring(0, 2).equals(piecePositions[7+2*j])) { mobility[7+2*j]++; mobility[17+2*j]++;}
							else if (startingPoint.substring(0, 2).equals(piecePositions[7+2*j+1]))  { mobility[7+2*j+1]++; mobility[17+2*j]++;}
							else {
								if (piecePositions[7+2*j+1].equals("")) { piecePositions[7+2*j+1]=startingPoint.substring(0,2); mobility[7+2*j+1]++; mobility[17+2*j]++;}
								else {
									mobility[15]++;
									mobility[17+2*j]++;
								}
							}
						}
					}	
				}
				if (pieceAtStartingPoint.equals("q")) {
					if (piecePositions[13].equals("")) {	piecePositions[13]=startingPoint.substring(0, 2); mobility[13]++; mobility[23]++;}
					else if (startingPoint.substring(0,2).equals(piecePositions[13])) {mobility[13]++; mobility[23]++;}
					else {
						mobility[15]++;
						mobility[23]++;
					}
				}
			}
		}
		for (int i = 0;i<mobility.length;i++) {
			mobility[i]/=16;
		}
		return mobility;
	}

	public static double[] wherePiecesAre() {
		double[] piecePlacement = new double[16+8+8+8+8+16+8+8+8+8]; //PAWN, KNIGHT, ROOK, BISHOP, QUEEN, BPAWN, BKNIGHT, BBISHOP, BROOK, BQUEEN
		ArrayList<Double> coor1 = new ArrayList<>();
		ArrayList<Double> coor2 = new ArrayList<>();
		ArrayList<Double> coor3 = new ArrayList<>();
		ArrayList<Double> coor4 = new ArrayList<>();
		ArrayList<Double> coor5 = new ArrayList<>();
		ArrayList<Double> coor6 = new ArrayList<>();
		ArrayList<Double> coor7 = new ArrayList<>();
		ArrayList<Double> coor8 = new ArrayList<>();
		ArrayList<Double> coor9 = new ArrayList<>();
		ArrayList<Double> coor10 = new ArrayList<>();
		for (int i=0; i<64; i++) {
			switch (chessBoard[i/8][i%8]) {
			case "P": coor1.add((double) (i/8)); coor1.add((double) (i%8));
			break;
			case "N": coor2.add((double) (i/8)); coor2.add((double) (i%8));
			break;
			case "B": coor3.add((double) (i/8)); coor3.add((double) (i%8));
			break;
			case "R": coor4.add((double) (i/8)); coor4.add((double) (i%8));
			break;
			case "Q": coor5.add((double) (i/8)); coor5.add((double) (i%8));
			break;
			case "p": coor6.add((double) (i/8)); coor6.add((double) (i%8));
			break;
			case "n": coor7.add((double) (i/8)); coor7.add((double) (i%8));
			break;
			case "b": coor8.add((double) (i/8)); coor8.add((double) (i%8));
			break;
			case "r": coor9.add((double) (i/8)); coor9.add((double) (i%8));
			break;
			case "q": coor10.add((double) (i/8)); coor10.add((double) (i%8));
			break;
			}
		}
		for (int i = 0;i<coor1.size();i++) {
			piecePlacement[i]=coor1.get(i);
		}
		for (int i = 0;i<coor2.size();i++) {
			piecePlacement[16+i]=coor2.get(i);
		}
		for (int i = 0;i<coor3.size();i++) {
			piecePlacement[24+i]=coor3.get(i);
		}
		for (int i = 0;i<coor4.size();i++) {
			piecePlacement[32+i]=coor4.get(i);
		}
		for (int i = 0;i<coor5.size();i++) {
			piecePlacement[40+i]=coor5.get(i);
		}
		for (int i = 0;i<coor6.size();i++) {
			piecePlacement[48+i]=coor6.get(i);
		}
		for (int i = 0;i<coor7.size();i++) {
			piecePlacement[64+i]=coor7.get(i);
		}
		for (int i = 0;i<coor8.size();i++) {
			piecePlacement[72+i]=coor8.get(i);
		}
		for (int i = 0;i<coor9.size();i++) {
			piecePlacement[80+i]=coor9.get(i);
		}
		for (int i = 0;i<coor10.size();i++) {
			piecePlacement[88+i]=coor10.get(i);
		}
		for (int i = 0;i<piecePlacement.length;i++) {
			piecePlacement[i]/=8;
		}
		return piecePlacement;
	}

	public static String[] destinationOfNonKingPiecesW() {
		String moves="";
		for (int i=0; i<64; i++) {
			switch (chessBoard[i/8][i%8]) {
			case "R": moves+=legalWR(i);
			break;
			case "N": moves+=legalWN(i);
			break;
			case "B": moves+=legalWB(i);
			break;
			case "Q": moves+=legalWQ(i);
			break;
			}
		}
		String[] array = moves.split(" ");
		for (int i = 0;i<array.length;i++) {
			if (array[i].length()>3){
				array[i]=array[i].substring(2, 4);
			}
		}
		return array;
	}

	public static String[] destinationOfNonKingPiecesB() {
		String moves="";
		for (int i=0; i<64; i++) {
			switch (chessBoard[i/8][i%8]) {
			case "r": moves+=legalBR(i);
			break;
			case "n": moves+=legalBN(i);
			break;
			case "b": moves+=legalBB(i);
			break;
			case "q": moves+=legalBQ(i);
			break;
			}
		}
		String[] array = moves.split(" ");
		for (int i = 0;i<array.length;i++) {
			if (array[i].length()>3) {
				array[i]=array[i].substring(2, 4);
			}
		}
		return array;
	}


	public static String[] legalWMoves() {
		String moves="";
		for (int i=0; i<64; i++) {
			switch (chessBoard[i/8][i%8]) {
			case "P": moves+=legalWP(i);
			break;
			case "R": moves+=legalWR(i);
			break;
			case "N": moves+=legalWN(i);
			break;
			case "B": moves+=legalWB(i);
			break;
			case "Q": moves+=legalWQ(i);
			break;
			case "K": moves+=legalWK(i);
			break;
			}
		}
		return moves.split(" ");
	}

	public static String[] legalNonKingWMoves() {
		String moves="";
		for (int i=0; i<64; i++) {
			switch (chessBoard[i/8][i%8]) {
			case "P": moves+=legalWP(i);
			break;
			case "R": moves+=legalWR(i);
			break;
			case "N": moves+=legalWN(i);
			break;
			case "B": moves+=legalWB(i);
			break;
			case "Q": moves+=legalWQ(i);
			break;
			}
		}
		return moves.split(" ");
	}

	public static String legalWhiteEnPassant(int i) {
		int r=i/8, c=i%8;
		int displayR = 8-r;
		for (int j=-1; j<=1; j+=2) { 
			try {//en passant
				if(chessBoard[r][c+j].equals("p") && chessBoard[r-1][c+j].equals(" ") && displayR == 5 && (timePawnMoved[8+c+j]-totalMoves==0) && pawnDoubleMove[8+c+j]) {

					chessBoard[r][c] = " ";
					chessBoard[r][c+j] = " ";
					chessBoard[r-1][c+j] = "P";
					if (whiteKingSafe()) {
						chessBoard[r][c] = "P";
						chessBoard[r][c+j] = "p";
						chessBoard[r-1][c+j] = " ";
						return colNames[c]+displayR+colNames[c+j]+(displayR+1)+"s ";
					}
					else {
						chessBoard[r][c] = "P";
						chessBoard[r][c+j] = "p";
						chessBoard[r-1][c+j] = " ";
					}
				}
				else {

				}
			} catch (Exception e) {}
		}
		return "";
	}

	public static String legalBlackEnPassant(int i) {
		String moves="";
		int r=i/8, c=i%8;
		int displayR = 8-r;
		for (int j=-1; j<=1; j+=2) { 
			try {//en passant
				if(chessBoard[r][c+j].equals("P") && chessBoard[r+1][c+j].equals(" ") && displayR == 4 && (timePawnMoved[c+j]-totalMoves==0) && pawnDoubleMove[c+j]) {
					chessBoard[r][c] = " ";
					chessBoard[r][c+j] = " ";
					chessBoard[r+1][c+j] = "p";
					if (whiteKingSafe()) {
						chessBoard[r][c] = "p";
						chessBoard[r][c+j] = "P";
						chessBoard[r+1][c+j] = " ";
						return colNames[c]+displayR+colNames[c+j]+(displayR-1)+"s ";
					}
					else {
						chessBoard[r][c] = "p";
						chessBoard[r][c+j] = "P";
						chessBoard[r+1][c+j] = " ";
					}
				}
				else {

				}
			} catch (Exception e) {}
		}
		return "";
	}

	public static String legalWP(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		for (int j=-1; j<=1; j+=2) {
			try {//capture
				if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i>=16) {
					oldPiece=chessBoard[r-1][c+j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r-1][c+j]="P";
					if (whiteKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+j)]+(displayR+1)+oldPiece+" ";
					}
					chessBoard[r][c]="P";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r-1][c+j]=oldPiece;
				}
			} catch (Exception e) {}
			try {//promotion && capture
				if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i<16) {
					String[] temp={"Q","R","B","N"};
					for (int k=0; k<4; k++) {
						oldPiece=chessBoard[r-1][c+j];
						chessBoard[r][c]=" ";
						chessBoard[r-1][c+j]=temp[k];
						if (whiteKingSafe()) {
							//column1,column2,captured-piece,new-piece,=
							moves=moves+colNames[c]+colNames[(c+j)]+oldPiece+temp[k]+"="+" ";
						}
						chessBoard[r][c]="P";
						chessBoard[r-1][c+j]=oldPiece;
					}
				}
			} catch (Exception e) {}
		}
		try {//move one up
			if (" ".equals(chessBoard[r-1][c]) && i>=16) {
				oldPiece=chessBoard[r-1][c];
				chessBoard[r][c]=" ";
				chessBoard[r-1][c]="P";
				if (whiteKingSafe()) {
					moves=moves+colNames[c]+displayR+colNames[c]+(displayR+1)+oldPiece;
				}
				chessBoard[r][c]="P";
				if(oldPiece.equals("")) oldPiece = " ";
				chessBoard[r-1][c]=oldPiece;
			}
		} catch (Exception e) {}
		try {//promotion && no capture
			if (" ".equals(chessBoard[r-1][c]) && i<16) {
				String[] temp={"Q","R","B","N"};
				for (int k=0; k<4; k++) {
					oldPiece=chessBoard[r-1][c];
					chessBoard[r][c]=" ";
					chessBoard[r-1][c]=temp[k];
					if (whiteKingSafe()) {
						//column1,column2,captured-piece,new-piece,P
						moves=moves+colNames[c]+colNames[c]+"/"+temp[k]+"="+" ";
					}
					chessBoard[r][c]="P";
					chessBoard[r-1][c]=oldPiece;
				}
			}
		} catch (Exception e) {}
		try {//move two up
			if (" ".equals(chessBoard[r-1][c]) && " ".equals(chessBoard[r-2][c]) && i>=48 && r==6) {
				oldPiece=chessBoard[r-2][c];
				chessBoard[r][c]=" ";
				chessBoard[r-2][c]="P";
				if (whiteKingSafe()) {
					moves=moves+colNames[c]+displayR+colNames[c]+(displayR+2)+oldPiece;
				}
				chessBoard[r][c]="P";
				if(oldPiece.equals("")) oldPiece = " ";
				chessBoard[r-2][c]=oldPiece;
			}
		} catch (Exception e) {}
		return moves+legalWhiteEnPassant(i);
	}

	public static String legalWR(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j+=2) {
			try {
				while(" ".equals(chessBoard[r][c+temp*j]))
				{
					oldPiece=chessBoard[r][c+temp*j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r][c+temp*j]="R";
					if (whiteKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+temp*j)]+displayR+oldPiece+" ";
					}
					chessBoard[r][c]="R";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r][c+temp*j]=oldPiece;
					temp++;
				}
				if (Character.isLowerCase(chessBoard[r][c+temp*j].charAt(0))) {
					oldPiece=chessBoard[r][c+temp*j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r][c+temp*j]="R";
					if (whiteKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+temp*j)]+displayR+oldPiece+" ";
					}
					chessBoard[r][c]="R";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r][c+temp*j]=oldPiece;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[r+temp*j][c]))
				{
					oldPiece=chessBoard[r+temp*j][c];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r+temp*j][c]="R";
					if (whiteKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[c]+(displayR-temp*j)+oldPiece+" ";
					}
					chessBoard[r][c]="R";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r+temp*j][c]=oldPiece;
					temp++;
				}
				if (Character.isLowerCase(chessBoard[r+temp*j][c].charAt(0))) {
					oldPiece=chessBoard[r+temp*j][c];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r+temp*j][c]="R";
					if (whiteKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[c]+(displayR-temp*j)+oldPiece+" ";
					}
					chessBoard[r][c]="R";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r+temp*j][c]=oldPiece;
				}
			} catch (Exception e) {}
			temp=1;
		}
		return moves;
	}
	public static String legalWN(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		for (int j=-1; j<=1; j+=2) {
			for (int k=-1; k<=1; k+=2) {
				try {
					if (Character.isLowerCase(chessBoard[r+j][c+k*2].charAt(0)) || " ".equals(chessBoard[r+j][c+k*2])) {
						oldPiece=chessBoard[r+j][c+k*2];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+j][c+k*2] = "N";
						if (whiteKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+k*2)]+(displayR-j)+oldPiece+" ";
						}
						chessBoard[r][c]="N";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+j][c+k*2]=oldPiece;
					}
				} catch (Exception e) {}
				try {
					if (Character.isLowerCase(chessBoard[r+j*2][c+k].charAt(0)) || " ".equals(chessBoard[r+j*2][c+k])) {
						oldPiece=chessBoard[r+j*2][c+k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+j*2][c+k]="N";
						if (whiteKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+k)]+(displayR-j*2)+oldPiece+" ";
						}
						chessBoard[r][c]="N";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+j*2][c+k]=oldPiece;
					}
				} catch (Exception e) {}
			}
		}
		return moves;
	}

	public static String legalWB(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j+=2) {
			for (int k=-1; k<=1; k+=2) {
				try {
					while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
					{
						oldPiece=chessBoard[r+temp*j][c+temp*k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+temp*j][c+temp*k]="B";
						if (whiteKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
						}
						chessBoard[r][c]="B";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+temp*j][c+temp*k]=oldPiece;
						temp++;
					}
					if (Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
						oldPiece=chessBoard[r+temp*j][c+temp*k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+temp*j][c+temp*k]="B";
						if (whiteKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
						}
						chessBoard[r][c]="B";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+temp*j][c+temp*k]=oldPiece;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		return moves;
	}
	public static String legalWQ(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j++) {
			for (int k=-1; k<=1; k++) {
				if (j!=0 || k!=0) {
					try {
						while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
						{
							oldPiece=chessBoard[r+temp*j][c+temp*k];
							if (oldPiece.equals(" ")) oldPiece="";
							chessBoard[r][c]=" ";
							chessBoard[r+temp*j][c+temp*k]="Q";
							if (whiteKingSafe()) {
								moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
							}
							chessBoard[r][c]="Q";
							if (oldPiece.equals("")) oldPiece=" ";
							chessBoard[r+temp*j][c+temp*k]=oldPiece;
							temp++;
						}
						if (Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
							oldPiece=chessBoard[r+temp*j][c+temp*k];
							if (oldPiece.equals(" ")) oldPiece="";
							chessBoard[r][c]=" ";
							chessBoard[r+temp*j][c+temp*k]="Q";
							if (whiteKingSafe()) {
								moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
							}
							chessBoard[r][c]="Q";
							if (oldPiece.equals("")) oldPiece=" ";
							chessBoard[r+temp*j][c+temp*k]=oldPiece;
						}
					} catch (Exception e) {}
					temp=1;
				}
			}
		}
		return moves;
	}
	public static String legalWK(int i) {
		int whiteKingPos = findWhiteKing();
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		if (chessBoard[7][4].equals("K") && chessBoard[7][7].equals("R") && chessBoard[7][5].equals(" ") && chessBoard[7][6].equals(" ")) {
			if (whiteKingMoved==0 && kwRookMoved==0) {
				chessBoard[7][4] = " ";
				chessBoard[7][7] = " ";
				chessBoard[7][5] = "R";
				chessBoard[7][6] = "K";
				if (whiteKingSafe()) {
					moves=moves+"O-O ";
				}
				chessBoard[7][5] = " ";
				chessBoard[7][6] = " ";
				chessBoard[7][7] = "R";
				chessBoard[7][4] = "K";
			}
		}
		if (chessBoard[7][4].equals("K") && chessBoard[7][0].equals("R") && chessBoard[7][1].equals(" ") && chessBoard[7][2].equals(" ") && chessBoard[7][3].equals(" ")) {
			if (whiteKingMoved==0 && qwRookMoved==0) {
				chessBoard[7][4] = " ";
				chessBoard[7][0] = " ";
				chessBoard[7][3] = "R";
				chessBoard[7][2] = "K";
				if (whiteKingSafe()) {
					moves=moves+"O-O-O ";
				}
				chessBoard[7][4] = "K";
				chessBoard[7][0] = "R";
				chessBoard[7][3] = " ";
				chessBoard[7][2] = " ";
			}
		}
		for (int j=0; j<9; j++) {
			if (j!=4) {
				try {
					if (Character.isLowerCase(chessBoard[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(chessBoard[r-1+j/3][c-1+j%3])) {
						oldPiece=chessBoard[r-1+j/3][c-1+j%3];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r-1+j/3][c-1+j%3]="K";
						int kingTemp=whiteKingPos;
						whiteKingPos=i+(j/3)*8+j%3-9;
						if (whiteKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c-1+j%3)]+(displayR+1-j/3)+oldPiece+" ";
						}
						chessBoard[r][c]="K";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r-1+j/3][c-1+j%3]=oldPiece;
						whiteKingPos=kingTemp;
					}
				} catch (Exception e) {}
			}
		}
		//need to add casting later
		return moves;
	}

	public static boolean whiteKingSafeCheck(int whiteKingPos) {
		//bishop/queen
		int temp=1;
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					while(" ".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8+temp*j])) {temp++;}
					if ("b".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8+temp*j]) ||
							"q".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8+temp*j])) {
						return false;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		//rook/queen
		for (int i=-1; i<=1; i+=2) {
			try {
				while(" ".equals(chessBoard[whiteKingPos/8][whiteKingPos%8+temp*i])) {temp++;}
				if ("r".equals(chessBoard[whiteKingPos/8][whiteKingPos%8+temp*i]) ||
						"q".equals(chessBoard[whiteKingPos/8][whiteKingPos%8+temp*i])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8])) {temp++;}
				if ("r".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8]) ||
						"q".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
		}
		//knight
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					if ("n".equals(chessBoard[whiteKingPos/8+i][whiteKingPos%8+j*2])) {
						return false;
					}
				} catch (Exception e) {}
				try {
					if ("n".equals(chessBoard[whiteKingPos/8+i*2][whiteKingPos%8+j])) {
						return false;
					}
				} catch (Exception e) {}
			}
		}
		//pawn
		if (whiteKingPos>=16) {
			try {
				if ("p".equals(chessBoard[whiteKingPos/8-1][whiteKingPos%8-1])) {
					return false;
				}
			} catch (Exception e) {}
			try {
				if ("p".equals(chessBoard[whiteKingPos/8-1][whiteKingPos%8+1])) {
					return false;
				}
			} catch (Exception e) {}
			//king
			for (int i=-1; i<=1; i++) {
				for (int j=-1; j<=1; j++) {
					if (i!=0 || j!=0) {
						try {
							if ("k".equals(chessBoard[whiteKingPos/8+i][whiteKingPos%8+j])) {
								return false;
							}
						} catch (Exception e) {}
					}
				}
			}
		}
		return true;
	}

	public static boolean whiteKingSafe() {
		int whiteKingPos = findWhiteKing();
		//bishop/queen
		int temp=1;
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					while(" ".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8+temp*j])) {temp++;}
					if ("b".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8+temp*j]) ||
							"q".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8+temp*j])) {
						return false;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		//rook/queen
		for (int i=-1; i<=1; i+=2) {
			try {
				while(" ".equals(chessBoard[whiteKingPos/8][whiteKingPos%8+temp*i])) {temp++;}
				if ("r".equals(chessBoard[whiteKingPos/8][whiteKingPos%8+temp*i]) ||
						"q".equals(chessBoard[whiteKingPos/8][whiteKingPos%8+temp*i])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8])) {temp++;}
				if ("r".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8]) ||
						"q".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
		}
		//knight
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					if ("n".equals(chessBoard[whiteKingPos/8+i][whiteKingPos%8+j*2])) {
						return false;
					}
				} catch (Exception e) {}
				try {
					if ("n".equals(chessBoard[whiteKingPos/8+i*2][whiteKingPos%8+j])) {
						return false;
					}
				} catch (Exception e) {}
			}
		}
		//pawn
		if (whiteKingPos>=16) {
			try {
				if ("p".equals(chessBoard[whiteKingPos/8-1][whiteKingPos%8-1])) {
					return false;
				}
			} catch (Exception e) {}
			try {
				if ("p".equals(chessBoard[whiteKingPos/8-1][whiteKingPos%8+1])) {
					return false;
				}
			} catch (Exception e) {}
			//king
			for (int i=-1; i<=1; i++) {
				for (int j=-1; j<=1; j++) {
					if (i!=0 || j!=0) {
						try {
							if ("k".equals(chessBoard[whiteKingPos/8+i][whiteKingPos%8+j])) {
								return false;
							}
						} catch (Exception e) {}
					}
				}
			}
		}
		return true;
	}

	public static int findBlackKing() {
		for (int i = 0;i<64;i++) {
			if (chessBoard[i/8][i%8].equals("k")) return i;
		}
		return 0;
	}

	public static String[] legalBMoves() {
		String moves="";
		for (int i=0; i<64; i++) {
			switch (chessBoard[i/8][i%8]) {
			case "p": moves+=legalBP(i);
			break;
			case "r": moves+=legalBR(i);
			break;
			case "n": moves+=legalBN(i);
			break;
			case "b": moves+=legalBB(i);
			break;
			case "q": moves+=legalBQ(i);
			break;
			case "k": moves+=legalBK(i);
			break;
			}
		}
		return moves.split(" ");
	}

	public static String[] legalNonKingBMoves() {
		String moves="";
		for (int i=0; i<64; i++) {
			switch (chessBoard[i/8][i%8]) {
			case "p": moves+=legalBP(i);
			break;
			case "r": moves+=legalBR(i);
			break;
			case "n": moves+=legalBN(i);
			break;
			case "b": moves+=legalBB(i);
			break;
			case "q": moves+=legalBQ(i);
			break;
			}
		}
		return moves.split(" ");
	}


	public static String legalBP(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		for (int j=-1; j<=1; j+=2) {
			try {//capture
				if (Character.isUpperCase(chessBoard[r+1][c+j].charAt(0)) && i<48) {
					oldPiece=chessBoard[r+1][c+j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r+1][c+j]="p";
					if (blackKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+j)]+(displayR-1)+oldPiece+" ";
					}
					chessBoard[r][c]="p";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r+1][c+j]=oldPiece;
				}
			} catch (Exception e) {}
			try {//promotion && capture
				if (Character.isUpperCase(chessBoard[r+1][c+j].charAt(0)) && i>=48) {
					String[] temp={"q","r","b","n"};
					for (int k=0; k<4; k++) {
						oldPiece=chessBoard[r+1][c+j];
						chessBoard[r][c]=" ";
						chessBoard[r+1][c+j]=temp[k];
						if (blackKingSafe()) {
							//column1,column2,captured-piece,new-piece,=
							moves=moves+colNames[c]+colNames[(c+j)]+oldPiece+temp[k]+"="+" ";
						}
						chessBoard[r][c]="p";
						chessBoard[r+1][c+j]=oldPiece;
					}
				}
			} catch (Exception e) {}
		}
		try {//move one up
			if (" ".equals(chessBoard[r+1][c]) && i<48) {
				oldPiece=chessBoard[r+1][c];
				chessBoard[r][c]=" ";
				chessBoard[r+1][c]="p";
				if (blackKingSafe()) {
					moves=moves+colNames[c]+displayR+colNames[c]+(displayR-1)+oldPiece;
				}
				chessBoard[r][c]="p";
				if(oldPiece.equals("")) oldPiece = " ";
				chessBoard[r+1][c]=oldPiece;
			}
		} catch (Exception e) {}
		try {//promotion && no capture
			if (" ".equals(chessBoard[r+1][c]) && i>=48) {
				String[] temp={"q","r","b","n"};
				for (int k=0; k<4; k++) {
					oldPiece=chessBoard[r+1][c];
					chessBoard[r][c]=" ";
					chessBoard[r+1][c]=temp[k];
					if (blackKingSafe()) {
						//column1,column2,captured-piece,new-piece,P
						moves=moves+colNames[c]+colNames[c]+"/"+temp[k]+"="+" ";
					}
					chessBoard[r][c]="p";
					chessBoard[r+1][c]=oldPiece;
				}
			}
		} catch (Exception e) {}
		try {//move two up
			if (" ".equals(chessBoard[r+1][c]) && " ".equals(chessBoard[r+2][c]) && i<=16 && r==1) {
				oldPiece=chessBoard[r+2][c];
				chessBoard[r][c]=" ";
				chessBoard[r+2][c]="p";
				if (blackKingSafe()) {
					moves=moves+colNames[c]+displayR+colNames[c]+(displayR-2)+oldPiece;
				}
				chessBoard[r][c]="p";
				if(oldPiece.equals("")) oldPiece = " ";
				chessBoard[r+2][c]=oldPiece;
			}
		} catch (Exception e) {}
		return moves+legalBlackEnPassant(i);
	}


	public static String legalBR(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j+=2) {
			try {
				while(" ".equals(chessBoard[r][c+temp*j]))
				{
					oldPiece=chessBoard[r][c+temp*j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r][c+temp*j]="r";
					if (blackKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+temp*j)]+displayR+oldPiece+" ";
					}
					chessBoard[r][c]="r";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r][c+temp*j]=oldPiece;
					temp++;
				}
				if (Character.isUpperCase(chessBoard[r][c+temp*j].charAt(0))) {
					oldPiece=chessBoard[r][c+temp*j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r][c+temp*j]="r";
					if (blackKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+temp*j)]+displayR+oldPiece+" ";
					}
					chessBoard[r][c]="r";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r][c+temp*j]=oldPiece;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[r+temp*j][c]))
				{
					oldPiece=chessBoard[r+temp*j][c];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r+temp*j][c]="r";
					if (blackKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[c]+(displayR-temp*j)+oldPiece+" ";
					}
					chessBoard[r][c]="r";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r+temp*j][c]=oldPiece;
					temp++;
				}
				if (Character.isUpperCase(chessBoard[r+temp*j][c].charAt(0))) {
					oldPiece=chessBoard[r+temp*j][c];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r+temp*j][c]="r";
					if (blackKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[c]+(displayR-temp*j)+oldPiece+" ";
					}
					chessBoard[r][c]="r";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r+temp*j][c]=oldPiece;
				}
			} catch (Exception e) {}
			temp=1;
		}
		return moves;
	}


	public static String legalBN(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		for (int j=-1; j<=1; j+=2) {
			for (int k=-1; k<=1; k+=2) {
				try {
					if (Character.isUpperCase(chessBoard[r+j][c+k*2].charAt(0)) || " ".equals(chessBoard[r+j][c+k*2])) {
						oldPiece=chessBoard[r+j][c+k*2];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+j][c+k*2]="n";
						if (blackKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+k*2)]+(displayR-j)+oldPiece+" ";
						}
						chessBoard[r][c]="n";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+j][c+k*2]=oldPiece;
					}
				} catch (Exception e) {}
				try {
					if (Character.isUpperCase(chessBoard[r+j*2][c+k].charAt(0)) || " ".equals(chessBoard[r+j*2][c+k])) {
						oldPiece=chessBoard[r+j*2][c+k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+j*2][c+k]="n";
						if (blackKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+k)]+(displayR-j*2)+oldPiece+" ";
						}
						chessBoard[r][c]="n";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+j*2][c+k]=oldPiece;
					}
				} catch (Exception e) {}
			}
		}
		return moves;
	}


	public static String legalBB(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j+=2) {
			for (int k=-1; k<=1; k+=2) {
				try {
					while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
					{
						oldPiece=chessBoard[r+temp*j][c+temp*k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+temp*j][c+temp*k]="b";
						if (blackKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
						}
						chessBoard[r][c]="b";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+temp*j][c+temp*k]=oldPiece;
						temp++;
					}
					if (Character.isUpperCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
						oldPiece=chessBoard[r+temp*j][c+temp*k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+temp*j][c+temp*k]="b";
						if (blackKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
						}
						chessBoard[r][c]="b";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+temp*j][c+temp*k]=oldPiece;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		return moves;
	}


	public static String legalBQ(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j++) {
			for (int k=-1; k<=1; k++) {
				if (j!=0 || k!=0) {
					try {
						while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
						{
							oldPiece=chessBoard[r+temp*j][c+temp*k];
							if (oldPiece.equals(" ")) oldPiece="";
							chessBoard[r][c]=" ";
							chessBoard[r+temp*j][c+temp*k]="q";
							if (blackKingSafe()) {
								moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
							}
							chessBoard[r][c]="q";
							if (oldPiece.equals("")) oldPiece=" ";
							chessBoard[r+temp*j][c+temp*k]=oldPiece;
							temp++;
						}
						if (Character.isUpperCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
							oldPiece=chessBoard[r+temp*j][c+temp*k];
							if (oldPiece.equals(" ")) oldPiece="";
							chessBoard[r][c]=" ";
							chessBoard[r+temp*j][c+temp*k]="q";
							if (blackKingSafe()) {
								moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
							}
							chessBoard[r][c]="q";
							if (oldPiece.equals("")) oldPiece=" ";
							chessBoard[r+temp*j][c+temp*k]=oldPiece;
						}
					} catch (Exception e) {}
					temp=1;
				}
			}
		}
		return moves;
	}
	public static String legalBK(int i) {
		int blackKingPos = findBlackKing();
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		if (chessBoard[0][4].equals("k") && chessBoard[0][7].equals("r") && chessBoard[0][5].equals(" ") && chessBoard[0][6].equals(" ")) {
			if (blackKingMoved==0 && kbRookMoved==0) {
				chessBoard[0][4] = " ";
				chessBoard[0][7] = " ";
				chessBoard[0][5] = "r";
				chessBoard[0][6] = "k";
				if (blackKingSafe()) {
					moves=moves+"O-O ";
				}
				chessBoard[0][5] = " ";
				chessBoard[0][6] = " ";
				chessBoard[0][7] = "r";
				chessBoard[0][4] = "k";
			}
		}
		if (chessBoard[0][4].equals("k") && chessBoard[0][0].equals("r") && chessBoard[0][1].equals(" ") && chessBoard[0][2].equals(" ") && chessBoard[0][3].equals(" ")) {
			if (whiteKingMoved==0 && qwRookMoved==0) {
				chessBoard[0][4] = " ";
				chessBoard[0][0] = " ";
				chessBoard[0][3] = "r";
				chessBoard[0][2] = "k";
				if (whiteKingSafe()) {
					moves=moves+"O-O-O ";
				}
				chessBoard[0][4] = "k";
				chessBoard[0][0] = "r";
				chessBoard[0][3] = " ";
				chessBoard[0][2] = " ";
			}
		}
		for (int j=0; j<9; j++) {
			if (j!=4) {
				try {
					if (Character.isUpperCase(chessBoard[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(chessBoard[r-1+j/3][c-1+j%3])) {
						oldPiece=chessBoard[r-1+j/3][c-1+j%3];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r-1+j/3][c-1+j%3]="k";
						int kingTemp=blackKingPos;
						blackKingPos=i+(j/3)*8+j%3-9;
						if (blackKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c-1+j%3)]+(displayR+1-j/3)+oldPiece+" ";
						}
						chessBoard[r][c]="k";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r-1+j/3][c-1+j%3]=oldPiece;
						blackKingPos=kingTemp;
					}
				} catch (Exception e) {}
			}
		}
		//need to add casting later
		return moves;
	}

	public static boolean blackKingSafeCheck(int blackKingPos) {
		//bishop/queen
		int temp=1;
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					while(" ".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8+temp*j])) {temp++;}
					if ("B".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8+temp*j]) ||
							"Q".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8+temp*j])) {
						return false;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		//rook/queen
		for (int i=-1; i<=1; i+=2) {
			try {
				while(" ".equals(chessBoard[blackKingPos/8][blackKingPos%8+temp*i])) {temp++;}
				if ("R".equals(chessBoard[blackKingPos/8][blackKingPos%8+temp*i]) ||
						"Q".equals(chessBoard[blackKingPos/8][blackKingPos%8+temp*i])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8])) {temp++;}
				if ("R".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8]) ||
						"Q".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
		}
		//knight
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					if ("N".equals(chessBoard[blackKingPos/8+i][blackKingPos%8+j*2])) {
						return false;
					}
				} catch (Exception e) {}
				try {
					if ("N".equals(chessBoard[blackKingPos/8+i*2][blackKingPos%8+j])) {
						return false;
					}
				} catch (Exception e) {}
			}
		}
		//pawn
		if (blackKingPos<=48) {
			try {
				if ("P".equals(chessBoard[blackKingPos/8+1][blackKingPos%8-1])) { 
					return false;
				}
			} catch (Exception e) {}
			try {
				if ("P".equals(chessBoard[blackKingPos/8+1][blackKingPos%8+1])) {
					return false;
				}
			} catch (Exception e) {}
			//king
			for (int i=-1; i<=1; i++) {
				for (int j=-1; j<=1; j++) {
					if (i!=0 || j!=0) {
						try {
							if ("K".equals(chessBoard[blackKingPos/8+i][blackKingPos%8+j])) {
								return false;
							}
						} catch (Exception e) {}
					}
				}
			}
		}
		return true;
	}

	public static boolean blackKingSafe() {
		int blackKingPos = findBlackKing();
		//bishop/queen
		int temp=1;
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					while(" ".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8+temp*j])) {temp++;}
					if ("B".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8+temp*j]) ||
							"Q".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8+temp*j])) {
						return false;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		//rook/queen
		for (int i=-1; i<=1; i+=2) {
			try {
				while(" ".equals(chessBoard[blackKingPos/8][blackKingPos%8+temp*i])) {temp++;}
				if ("R".equals(chessBoard[blackKingPos/8][blackKingPos%8+temp*i]) ||
						"Q".equals(chessBoard[blackKingPos/8][blackKingPos%8+temp*i])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8])) {temp++;}
				if ("R".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8]) ||
						"Q".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
		}
		//knight
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					if ("N".equals(chessBoard[blackKingPos/8+i][blackKingPos%8+j*2])) {
						return false;
					}
				} catch (Exception e) {}
				try {
					if ("N".equals(chessBoard[blackKingPos/8+i*2][blackKingPos%8+j])) {
						return false;
					}
				} catch (Exception e) {}
			}
		}
		//pawn
		if (blackKingPos<=48) {
			try {
				if ("P".equals(chessBoard[blackKingPos/8+1][blackKingPos%8-1])) { 
					return false;
				}
			} catch (Exception e) {}
			try {
				if ("P".equals(chessBoard[blackKingPos/8+1][blackKingPos%8+1])) {
					return false;
				}
			} catch (Exception e) {}
			//king
			for (int i=-1; i<=1; i++) {
				for (int j=-1; j<=1; j++) {
					if (i!=0 || j!=0) {
						try {
							if ("K".equals(chessBoard[blackKingPos/8+i][blackKingPos%8+j])) {
								return false;
							}
						} catch (Exception e) {}
					}
				}
			}
		}
		return true;
	}
}
