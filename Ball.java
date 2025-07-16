package SameGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class Ball {
	private int x, y;
	private final Color color;
	public static Dimension ballDimensions = new Dimension(40, 40);

	Ball(int x, int y, Color color) {
		this.x = x * ballDimensions.width;
		this.y = y * ballDimensions.height;
		this.color = color;
	}

	void display(Graphics g, int x, int y) {
		g.setColor(color);
		g.fillOval(x, y, ballDimensions.width, ballDimensions.height);
	}

	public Color getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
