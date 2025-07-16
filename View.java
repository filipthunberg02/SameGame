package SameGame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class View extends JFrame implements GameObserver {
	private Model model;
	private Controller controller;
	private Font buttonFont;
	private BallPanel ballPanel;
	private JLabel CycleCount;
	private JLabel scoreLabel;
	private JLabel curDiff;
	private int circleDiameter = 40;

	public void addModel(Model m) {
		model = m;
	}

	public void addController(Controller c) {
		controller = c;
	}

    public void requestFocusForBallPanel() {
        ballPanel.requestFocus();
    }

	public void build() {

		setBounds(10,10,700,600);

		setPreferredSize(new Dimension(model.getCols() * circleDiameter + 20, model.getRows() * circleDiameter + 110));

        addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		buttonFont = new Font("Monospaced", Font.BOLD, 14);
		setTitle("SameGame  (さめがめ)");
		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));

        buttonSetup(controls, controller.getStartButton(), "Start");
        buttonSetup(controls, controller.getDifficultyButton(), "Change Difficulty");
        buttonSetup(controls, controller.getExitButton(), "Exit");

        Container contentPane = getContentPane();
		contentPane.add(controls, "South");

		ballPanel = new BallPanel(model);
		ballPanel.setFocusable(true);
		ballPanel.addMouseListener(controller.getBallPanelMouseListener());
		ballPanel.addKeyListener(controller.getBallPanelKeyListener());
		contentPane.add(ballPanel, "Center");
		ballPanel.setPreferredSize(new Dimension(model.getCols() * circleDiameter, model.getRows() * circleDiameter));

        JPanel scoreboard = new JPanel();
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(buttonFont);
        buttonSetup(scoreboard, controller.getPrevButton(), "Previous move");
        buttonSetup(scoreboard, controller.getNextButton(), "Next move");
        scoreboard.add(scoreLabel);
        contentPane.add(scoreboard, BorderLayout.NORTH);

        curDiff = new JLabel("Difficulty: ");
        curDiff.setFont(buttonFont);
		controls.add(curDiff);

        CycleCount = new JLabel("Time ");
		CycleCount.setFont(buttonFont);
		controls.add(CycleCount);
		controller.timer();

		pack();
    }

	public void paint(Graphics g) {
		CycleCount.setText("  |  Time: " + model.getCycleCount());
		scoreLabel.setText(" Score: " + model.getScore() + "   |   High Score: " + model.getHighScore() + "(" + model.getDifficulty() +  ")");
		curDiff.setText("  Difficulty: " + model.getDifficulty());
		super.paint(g);
	}

	public void update() {
		repaint();
		ballPanel.repaint();
	}

	private void buttonSetup(JPanel panelForButtons, JButton b, String bLabel) {
		b.setText(bLabel);
		b.setFont(buttonFont);
		panelForButtons.add(b);
	}

	@Override
	public void gameState() {
		System.out.println("Move: " + model.getCount());
		System.out.println("Ball pressed at: " + "(" + model.getCurrX() + "," + model.getCurrY() + ")");
		System.out.println("Ball Color: " + model.getCurrColor().toString());
		System.out.println("Time: " + model.getCycleCount());
		System.out.println("Score this move: " + model.scoreRound());
		System.out.println();
	}
}
