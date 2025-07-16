package SameGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Controller {
	private Model model;
	private View view;
	public int diffCounter = 1;

	public void addModel(Model m) {
		model = m;
	}

	public void addView(View v) {
		view = v;
	}

	JButton getStartButton() {
		JButton b = new JButton();

		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.start();
				view.requestFocusForBallPanel();
			}
		});

		return b;
	}

	JButton getDifficultyButton() {
		JButton b = new JButton();

		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String[] options = {"Easy", "Medium", "Hard"};
				int choice = JOptionPane.showOptionDialog(view, "Select Difficulty:", "Change Difficulty",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);

				model.reset();

				switch(choice) {
				case 0: model.diff(1); break;
				case 1: model.diff(2); break;
				case 2: model.diff(3); break;
				}
				diffCounter++;
				if(diffCounter == 4) {
					diffCounter = 1;
				}
				view.requestFocusForBallPanel();
			}
		});

		return b;
	}

	JButton getExitButton() {
		JButton b = new JButton();

		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		return b;
	}

	JButton getNextButton() {
        JButton b = new JButton();

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.nextState();
                view.requestFocusForBallPanel();
            }
        });

        return b;
    }

    JButton getPrevButton() {
        JButton b = new JButton();

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.previousState();
                view.requestFocusForBallPanel();
            }
        });
        return b;
    }

	MouseListener getBallPanelMouseListener() {
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				model.mouseClick(evt.getX(), evt.getY());
				view.requestFocusForBallPanel();
			}
		};
	}

	KeyListener getBallPanelKeyListener() {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				model.keyPressed(evt.getKeyCode());
			}
		};
	}

    void timer() {
        Timer tempTimer = new Timer(1000, e -> model.updateAll());
        tempTimer.start();
    }
}
