package SameGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Model {
	private int countMove;
	private int maxStateIndex = -1;
	private int stateIndex = -1;
	private int xKey = 1;
	private int yKey = 1;
	private int currX = 0;
	private int currY = 0;
	private Color currColor;
	private int recurrent;
	private int highScoreEasy;
	private int highScoreMedium;
	private int highScoreHard;
	private int score;
	private int cycleCount;
	private boolean running = false;
	private int diffC = 2;
	private int rows;
	private int cols;
	private int colsShiftLeft;
	private View view;
	private int used;
	private Ball[][] balls;
	private List<GameObserver> observers = new ArrayList<>();
	private List<Ball[][]> gameStates = new ArrayList<>();



	public Model(int rows, int cols) {
		this.cols = cols;
		this.rows = rows;
		balls = new Ball[rows][cols];
		this.colsShiftLeft = cols;
	}

	public void addView(View v) {
		view = v;
	}

	public void addObserver(GameObserver observer) {
		observers.add(observer);
	}

	private void notifyObservers() {
		for(GameObserver observer : observers) {
			observer.update();
		}
	}

	private void gameStateObserver() {
		for(GameObserver observer : observers) {
			observer.gameState();
		}
	}

	public void start() {
		running = true;
		countMove = 0;
		colsShiftLeft = cols;
		gameStates.removeAll(gameStates);
		createBalls();
		gameStates.add(saveState());
		notifyObservers();
	}

	public void reset() {
		running = false;
		cycleCount = 0;
		score = 0;
		gameStates.removeAll(gameStates);
		colsShiftLeft = cols;
		maxStateIndex = -1;
		stateIndex = 0;
		resetBalls();
		notifyObservers();
	}


	public void resetBalls() {
		for(int x = 0; x < rows; x++) {
        	for(int y = 0; y < cols; y++) {
        		balls[x][y] = null;
                used++;
        	}
        }
        notifyObservers();
	}

	public void diff(int dif) {
		switch(dif) {
		case 1: diffC = 2; break;
		case 2: diffC = 4; break;
		case 3: diffC = 6; break;
		}
		notifyObservers();
	}

	public Color randomColor(int d) {
		Color randomColor = Color.WHITE;
        int randomNumber = (int)((Math.random() * d) + 1);

        switch(randomNumber) {
        case 1: randomColor = Color.BLUE; break;
        case 2: randomColor = Color.RED; break;
        case 3: randomColor = Color.YELLOW; break;
        case 4: randomColor = Color.GREEN; break;
        case 5: randomColor = Color.PINK; break;
        case 6: randomColor = Color.WHITE; break;
        default: randomColor = Color.BLACK;
        }

        return randomColor;
	}

	public void createBalls() {
        for(int x = 0; x < rows; x++) {
        	for(int y = 0; y < cols; y++) {
        		balls[x][y] = new Ball(y, x, randomColor(diffC));
                used++;
        	}
        }
        notifyObservers();
    }

	int getBallCount() {
		return used;
	}

	int getCurrX() {
		return currX;
	}

	int getCurrY() {
		return currY;
	}

	int getCount() {
		return countMove;
	}

	String getCurrColor() {

		if(currColor.equals(Color.RED)) {
			return "Red";
		}

		else if(currColor.equals(Color.BLUE)) {
			return "Blue";
		}

		else if(currColor.equals(Color.GREEN)) {
			return "Green";
		}

		else if(currColor.equals(Color.YELLOW)) {
			return "Yellow";
		}

		else if(currColor.equals(Color.PINK)) {
			return "Pink";
		}

		else if(currColor.equals(Color.WHITE)) {
			return "White";
		}

		return "No Color";
	}

	public void selectBall() {
        int col = xKey / Ball.ballDimensions.width;
        int row = yKey / Ball.ballDimensions.height;
        currX = col;
        currY = row;
        if (running && removeBalls(row, col)) {
        	updateState();
        }
    }

	public void moveLeft() {
        if (xKey > 2) {
            xKey -= Ball.ballDimensions.width;
            notifyObservers();
        }
    }

    public void moveRight() {
        if (xKey < (cols - 1) * Ball.ballDimensions.width) {
            xKey += Ball.ballDimensions.width;
            notifyObservers();
        }
    }

    public void moveUp() {
        if (yKey > 2) {
            yKey -= Ball.ballDimensions.height;
            notifyObservers();
        }
    }

    public void moveDown() {
        if (yKey < (rows - 1) * Ball.ballDimensions.height) {
            yKey += Ball.ballDimensions.height;
            notifyObservers();
        }
    }

	void mouseClick(int x, int y) {

		int col = x / Ball.ballDimensions.width;
		int row = y / Ball.ballDimensions.height;

		if(col < 0 || col > 15 || row < 0 || row > 9) {
			return;
		}

		currX = col;
		currY = row;
		if (running && removeBalls(row, col)) {
			updateState();
		}

	}

	public void keyPressed(int key) {
		switch (key) {
        case 37: // Left key
            moveLeft();
            break;
        case 38: // Up key
            moveUp();
            break;
        case 39: // Right key
            moveRight();
            break;
        case 40: // Down key
            moveDown();
            break;
        case 32: // Space bar
            selectBall();
            break;
    }
}

	void updateState() {
		countMove++;
		score += recurrent * (recurrent - 1);
        shiftDown();
        shiftLeft();
        gameStates.add(saveState());
		stateIndex++;
        gameOver();
        notifyObservers();
        gameStateObserver();
	}

	int scoreRound() {
		return recurrent * (recurrent - 1);
	}

	void nextState() {
		if(running) {
			return;
		}

		if(maxStateIndex == stateIndex) {
			return;
		}
        stateIndex++;
        balls = gameStates.get(stateIndex);
        notifyObservers();
    }

    void previousState() {
    	if(running) {
			return;
		}

    	if(stateIndex < 1) {
    		return;
    	}
        stateIndex--;
        balls = gameStates.get(stateIndex);
        notifyObservers();
    }

	public Ball[][] saveState() {
	    Ball[][] state = new Ball[rows][cols];
	    for (int i = 0; i < rows; i++) {
	        for (int j = 0; j < cols; j++) {
	            if (balls[i][j] != null) {
	                state[i][j] = new Ball(balls[i][j].getX(), balls[i][j].getY(), balls[i][j].getColor());
	            }
	            else {
	                state[i][j] = null;
	            }
	        }
	    }
	    return state;
	}

	int getKeyX() {
		return xKey;
	}

	int getKeyY() {
		return yKey;
	}

	public void gameOver() {
		boolean over = false;

		for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
            	Ball ball = getBallDetails(i,j);

            	if(ball != null) {
            		Color targetColor = ball.getColor();
            		over = hasMoves(i, j, targetColor);
            	}

            	if(over) {
            		break;
            	}
            }
            if(over) {
        		break;
        	}
		}
		if(!over) {
			running = false;
			maxStateIndex = stateIndex;
			System.out.println("Game Over");
			String[] options = {"Restart", "View Game", "Exit"};
			int choice = JOptionPane.showOptionDialog(view, "Score: " + getScore() + "\n\nChoose Option: ", "Game Over",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);

			switch(choice) {
			case 0: reset(); start(); break;
			case 1: balls = gameStates.get(stateIndex); break;
			case 2: System.exit(0); break;
			}

    	}
		notifyObservers();
	}

	public boolean hasMoves(int row, int column, Color color) {
        if (row < 0 || row >= rows || column < 0 || column >= cols) {
            return false;
            }

        if(balls[row][column] == null || !balls[row][column].getColor().equals(color)) {
            return false;
            }

        boolean moveFound = false;
        if (row > 0 && balls[row - 1][column] != null && balls[row - 1][column].getColor().equals(color)) {
        	moveFound = true;
        	}

        if (row < rows - 1 && balls[row + 1][column] != null && balls[row + 1][column].getColor().equals(color)) {
        	moveFound = true;
        	}

        if (column > 0 && balls[row][column - 1] != null && balls[row][column - 1].getColor().equals(color)) {
        	moveFound = true;
        	}

        if (column < cols - 1 && balls[row][column + 1] != null && balls[row][column + 1].getColor().equals(color)) {
        	moveFound = true;
        	}

        return moveFound;
    }

	public Ball getBallDetails(int row, int col) {
		return balls[row][col];
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public void shiftDown() {
        for (int col = 0; col < cols; col++) {
            int writeRow = rows - 1;
            for (int readRow = rows - 1; readRow >= 0; readRow--) {
                if (balls[readRow][col] != null) {
                    if (writeRow != readRow) {
                        balls[writeRow][col] = balls[readRow][col];
                        balls[readRow][col] = null;
                    }
                    writeRow--;
                }
            }
        }
    }

	public void shiftLeft() {

	    for (int column = 0; column < colsShiftLeft - 1; column++) {
	        if (isColumnEmpty(column)) {
	            for (int col = column; col < colsShiftLeft - 1; col++) {
	                for (int row = 0; row < rows; row++) {
	                    balls[row][col] = balls[row][col + 1];
	                    balls[row][col + 1] = null;
	                }
	            }
	            colsShiftLeft--;
	            column--;
	        }
	    }
	}

	private boolean isColumnEmpty(int col) {
	    for (int row = 0; row < rows; row++) {
	        if (balls[row][col] != null) {
	            return false;
	        }
	    }
	    return true;
	}

	public boolean removeBalls(int row, int column) {
        if(balls[row][column] == null) {
            return false;
            }

        Ball ball = getBallDetails(row, column);
        Color targetColor = ball.getColor();
        currColor = targetColor;
        recurrent = 0;
        removeBallsRecursive(row, column, targetColor, 0);
        return recurrent > 0;
    }

	public void removeBallsRecursive(int row, int column, Color color, int flag) {

        if (row < 0 || row >= rows || column < 0 || column >= cols) {
            return;
            }
        if(balls[row][column] == null || !balls[row][column].getColor().equals(color)) {
            return;
            }
        if(flag == 1) {
        	balls[row][column] = null;
        	recurrent++;
        }
        removeBallsRecursive(row + 1, column, color, 1);
        removeBallsRecursive(row - 1, column, color, 1);
        removeBallsRecursive(row, column + 1, color, 1);
        removeBallsRecursive(row, column - 1, color, 1);
    }

	int getCycleCount() {
		return cycleCount;
	}

	int getScore() {
		return score;
	}

	int getHighScore() {
		if(diffC == 2) {
			if(score > highScoreEasy) {
				highScoreEasy = score;
			}
			return highScoreEasy;
			}

		if(diffC == 4) {
			if(score > highScoreMedium) {
				highScoreMedium = score;
			}
			return highScoreMedium;
			}

		if(diffC == 6) {
			if(score > highScoreHard) {
				highScoreHard = score;
			}
			return highScoreHard;
			}
		return 0;
	}

	String getDifficulty() {
		switch(diffC) {
		case 2: return "Easy";
		case 4: return "Medium";
		case 6: return "Hard";
		}
		return "Easy";
	}

	void updateAll() {
		if (!running) {
			return;
		}
		cycleCount++;
		notifyObservers();
	}
}
