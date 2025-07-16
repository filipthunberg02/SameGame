package SameGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class BallPanel extends JPanel {
	private Model model;

	public BallPanel(Model m) {
		model = m;
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(500, 500));
	}

	public void paint(Graphics g) {
		super.paint(g);
		setFocusable(true);
		setBackground(Color.BLACK);
		super.paintComponent(g);
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getCols(); j++) {
                Ball ball = model.getBallDetails(i, j);
                if(ball != null) {
                    ball.display(g, j * Ball.ballDimensions.width , i * Ball.ballDimensions.height);
                    }
            }
        }
        g.setColor(Color.WHITE);
        g.drawLine(model.getKeyX(), model.getKeyY(), Ball.ballDimensions.width + model.getKeyX(), model.getKeyY());
        g.drawLine(Ball.ballDimensions.width + model.getKeyX(), model.getKeyY(), Ball.ballDimensions.width + model.getKeyX(), Ball.ballDimensions.height + model.getKeyY());
        g.drawLine(model.getKeyX(), Ball.ballDimensions.height + model.getKeyY(), Ball.ballDimensions.width + model.getKeyX(), Ball.ballDimensions.height + model.getKeyY());
        g.drawLine(model.getKeyX(), Ball.ballDimensions.height + model.getKeyY(), model.getKeyX(), model.getKeyY());
	}
}
