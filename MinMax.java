import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Node {		
	
	int val;
	int row;
	int col;
	String op;
	String state;
	
	public Node (int val, int row, int col, String op, String state) {
		this.val = val;
		this.row = row;
		this.col = col;
		this.op = op;
		this.state = state;
	}
}

public class MinMax {
	
	static int N, DEPTH;
	static String MODE, YOUPLAY;
	static String res;
	static String[][] boardStateBefore;
	static int[][] cellValue;
	static String[][] boardState;
	static boolean isAlphaBeta = false;
	static int bestCol = 0;
	static int bestRow = 0;
	static String bestOp = "";

	public static void main(String[] args) throws IOException {
		/*
		 * part1, read the file and data initialize
		 */
		
		String inFileName = "input.txt";
		readFile(inFileName);
		initialData(boardStateBefore);

		/*
		 * part2, execute search
		 */

		isAlphaBeta = MODE.equals("ALPHABETA");
		maxValue(boardState, cellValue, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, YOUPLAY, true);
		Node ans = new Node(0, bestRow, bestCol, bestOp, YOUPLAY);

		/*
		 * part3, process returned data 
		 */

		String output = process(ans);
		
		/*
		 * part4, output into the file
		 */
		
		String outFileName = "output.txt";
		writeFile(outFileName, output);
	}
	
	public static int maxValue(String[][] boardState, int[][] cellValue, int a, int b, int depth, String nowPlayer, boolean flag) {
		String nextPlayer = nowPlayer.equals("O") ? "X" : "O";
		if (depth == DEPTH) {
			String NEXTPLAY = YOUPLAY.equals("O") ? "X" : "O";
			int val = 0;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (boardState[i + 1][j + 1].equals(YOUPLAY)) {
						val += cellValue[i][j];
					}
					if (boardState[i + 1][j + 1].equals(NEXTPLAY)) {
						val -= cellValue[i][j];
					}
				}	
			}
			return val;
		}
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {	
				if (boardState[i + 1][j + 1].equals(".")) {
					// stake
					boardState[i + 1][j + 1] = nowPlayer;
					int temp = minValue(boardState, cellValue, a, b, depth + 1, nextPlayer);
					if (max < temp) {
						max = temp;
						if (flag) {
							bestRow = i + 1;
							bestCol = j + 1;
							bestOp = "Stake";
						}
					}
					boardState[i + 1][j + 1] = ".";
					a = max > a ? max : a;
					if (isAlphaBeta && a >= b) {
						return b;
					}
				}
			}
		}
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {	
				if (boardState[i + 1][j + 1].equals(".")) {
					// raid
					boolean f1 = false, f2 = false, f3 = false, f4 = false;
					if (boardState[i + 1][j].equals(nowPlayer) || boardState[i][j + 1].equals(nowPlayer) || boardState[i + 1][j + 2].equals(nowPlayer) || boardState[i + 2][j + 1].equals(nowPlayer)) {
						if (boardState[i + 1][j].equals(nextPlayer)) {
							f1 = true;
							boardState[i + 1][j] = nowPlayer;
						}
						if (boardState[i][j + 1].equals(nextPlayer)) {
							f2 = true;
							boardState[i][j + 1] = nowPlayer;
						}
						if (boardState[i + 1][j + 2].equals(nextPlayer)) {
							f3 = true;
							boardState[i + 1][j + 2] = nowPlayer;
						}
						if (boardState[i + 2][j + 1].equals(nextPlayer)) {
							f4 = true;
							boardState[i + 2][j + 1] = nowPlayer;
						}
						boardState[i + 1][j + 1] = nowPlayer;
						int temp = minValue(boardState, cellValue, a, b, depth + 1, nextPlayer);
						boardState[i + 1][j + 1] = ".";
						if (f1) {
							boardState[i + 1][j] = nextPlayer;
						}
						if (f2) {
							boardState[i][j + 1] = nextPlayer;
						}
						if (f3) {
							boardState[i + 1][j + 2] = nextPlayer;
						}
						if (f4) {
							boardState[i + 2][j + 1] = nextPlayer;
						}
						if (temp > max) {
							max = temp;
							if(flag) {
								bestRow = i + 1;
								bestCol = j + 1;
								bestOp = "Raid";
							}
						}
						a = a > max ? a : max;
						if (isAlphaBeta && a >= b) {
							return b;
						}
					}
				}
				
			}
		}
		if (isAlphaBeta)
			return a;
		else
			return max;
	}
	
	public static int minValue(String[][] boardState, int[][] cellValue, int a, int b, int depth, String nowPlayer) {
		String nextPlayer = nowPlayer.equals("O") ? "X" : "O";
		if (depth == DEPTH) { 
			String NEXTPLAY = YOUPLAY.equals("O") ? "X" : "O";
			int val = 0;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (boardState[i + 1][j + 1].equals(YOUPLAY)) {
						val += cellValue[i][j];
					}
					if (boardState[i + 1][j + 1].equals(NEXTPLAY)) {
						val -= cellValue[i][j];
					}
				}	
			}
			return val;
		}
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (boardState[i + 1][j + 1].equals(".")) {
					// stake
					boardState[i + 1][j + 1] = nowPlayer;
					int temp = maxValue(boardState, cellValue, a, b, depth + 1, nextPlayer, false);
					if (temp < min) {
						min = temp;
					}
					boardState[i + 1][j + 1] = ".";
					b = min < b ? min : b;
					if (isAlphaBeta && b <= a) {
						return a;
					}
				}
			}
		}
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (boardState[i + 1][j + 1].equals(".")) {
					// raid
					boolean f1 = false, f2 = false, f3 = false, f4 = false;
					if (boardState[i + 1][j].equals(nowPlayer) || boardState[i][j + 1].equals(nowPlayer) || boardState[i + 1][j + 2].equals(nowPlayer) || boardState[i + 2][j + 1].equals(nowPlayer)) {
						if (boardState[i + 1][j].equals(nextPlayer)) {
							f1 = true;
							boardState[i + 1][j] = nowPlayer;
						}
						if (boardState[i][j + 1].equals(nextPlayer)) {
							f2 = true;
							boardState[i][j + 1] = nowPlayer;
						}
						if (boardState[i + 1][j + 2].equals(nextPlayer)) {
							f3 = true;
							boardState[i + 1][j + 2] = nowPlayer;
						}
						if (boardState[i + 2][j + 1].equals(nextPlayer)) {
							f4 = true;
							boardState[i + 2][j + 1] = nowPlayer;
						}
						boardState[i + 1][j + 1] = nowPlayer;
						int temp = maxValue(boardState, cellValue, a, b, depth + 1, nextPlayer, false);
						boardState[i + 1][j + 1] = ".";
						if (f1) {
							boardState[i + 1][j] = nextPlayer;
						}
						if (f2) {
							boardState[i][j + 1] = nextPlayer;
						}
						if (f3) {
							boardState[i + 1][j + 2] = nextPlayer;
						}
						if (f4) {
							boardState[i + 2][j + 1] = nextPlayer;
						}
						if (temp < min) {
							min = temp;
						}
						b = min < b ? min : b;
						if (isAlphaBeta && b <= a) {
							return a;
						}
					}
				}
			}
		}
		if (isAlphaBeta)
			return b;
		else
			return min;
	}
	
	public static void readFile(String fileName) throws NumberFormatException, IOException {
		FileInputStream fis = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		int lineIndex = 0;
		while ((line = br.readLine()) != null) {
			if (lineIndex == 0) {
				N = Integer.parseInt(line);
				cellValue = new int[N][N];
				boardStateBefore = new String[N][N];
				boardState = new String[N + 2][N + 2];
			}
			else if (lineIndex == 1) {
				MODE = line;
			}
			else if (lineIndex == 2) {
				YOUPLAY = line;
			}
			else if (lineIndex == 3) {
				DEPTH = Integer.parseInt(line);
			}
			else if (lineIndex >= 4 && lineIndex < 4 + N) {
				String[] value = line.split("\\s+");
				for (int i = 0; i < N; i++) {
					cellValue[lineIndex - 4][i] = Integer.valueOf(value[i]);
				}
			}
			else {
				for (int i = 0; i < N; i++) {
					boardStateBefore[lineIndex - 4 - N][i] = line.charAt(i) + "";
				}
			}
			lineIndex++;
		}
		br.close();	
	}
	
	public static void initialData(String[][] boardStateBefore) {
		int number = 0;
		for (int i = 0; i < N + 2; i++) {
			for (int j = 0; j < N + 2; j++) {
				boardState[i][j] = "*";
			}
		}
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (!boardStateBefore[i][j].equals(".")) {
					number++;
				}
				boardState[i + 1][j + 1] = boardStateBefore[i][j];
			}
		}
		DEPTH = Math.min(DEPTH, N * N - number);
	}
	
	public static String process(Node ans) {
		String res = "";
		res += (char)(ans.col + 64) + "" + ans.row + " " + ans.op + "\n";
		boardStateBefore[ans.row - 1][ans.col - 1] = ans.state;
		String next = ans.state.equals("X") ? "O" : "X";
		if (boardState[ans.row - 1][ans.col].equals(next) && ans.op.equals("Raid")) {
			boardStateBefore[ans.row - 2][ans.col - 1] = ans.state;
		}
		if (boardState[ans.row + 1][ans.col].equals(next) && ans.op.equals("Raid")) {
			boardStateBefore[ans.row][ans.col - 1] = ans.state;
		}
		if (boardState[ans.row][ans.col - 1].equals(next) && ans.op.equals("Raid")) {
			boardStateBefore[ans.row - 1][ans.col - 2] = ans.state;
		}
		if (boardState[ans.row][ans.col + 1].equals(next) && ans.op.equals("Raid")) {
			boardStateBefore[ans.row - 1][ans.col] = ans.state;
		}
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				res += boardStateBefore[i][j];
			}
			res += "\n";
		}
		return res;
	}
	
	public static void writeFile(String fileName, String ans) throws IOException {
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(ans);
		bw.close();
	}
}
