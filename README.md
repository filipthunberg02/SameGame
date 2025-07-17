# SameGame

A Java implementation of the classic tile-matching puzzle SameGame, built using the Model–View–Controller pattern and the Observer design pattern.

---

## About

SameGame is a tile-based puzzle game where the player removes clusters of adjacent balls of the same color to earn points. The larger the cluster removed, the higher the score for that move. The game ends when no more adjacent clusters remain. This implementation provides both a graphical Swing view and console-based logging of game states.

---

## Features

- **Classic SameGame mechanics**: Click or press space on clusters of two or more same-colored balls to remove them.  
- **Scoring system**: Points awarded are _n × (n − 1)_ for a cluster of _n_ balls.  
- **Multiple difficulty levels**: Easy, Medium, Hard (different color counts).  
- **Replayability**: After game over, step through every move with “Previous” and “Next” buttons.  
- **MVC & Observer patterns**: Clear separation of concerns and easy extensibility.

---

## Requirements

- Java 8 or higher  
- No external libraries (uses only standard JDK & Swing)  

---

## How It Works

### Model (`SameGame.Model`)  
- Manages the grid of `Ball` objects, current score, difficulty, and game-states history.  
- Implements removal logic (flood fill), gravity (`shiftDown`), and column collapse (`shiftLeft`).  
- Notifies all registered `GameObserver`s on state changes via `update()` and `gameState()` callbacks.

### View (`SameGame.View` & `BallPanel`)  
- Renders the game grid in a Swing `JFrame` and custom `JPanel`.  
- Displays real-time score, time elapsed, and current difficulty.  
- Listens to model updates and repaints accordingly.

### Controller (`SameGame.Controller`)  
- Wires together the `Model` and `View`.  
- Handles mouse clicks, keyboard navigation (arrow keys + space), and button actions (Start, Change Difficulty, Previous/Next move, Exit).  
- Drives a Swing `Timer` to display elapsed time.

---

## Testing

- **Unit Testing**: Console-based checks for individual methods (e.g., button listeners, key events).  
- **Integration Testing**: Manual GUI tests—verifying clicks inside/outside the grid, difficulty changes, and end-game dialog behaviors.

---
