package main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import backend.View;

public class MainMenu extends JFrame implements ActionListener {

	private static final long serialVersionUID = 4074500268726614700L;
	private JButton btnQuizEditor;
	private JButton btnHostGame;
	private JButton btnJoinGame;
	
	private Main main;

	public MainMenu(Main main) {
		setTitle("Toohak");
		setBounds(100, 100, 300, 150);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		btnQuizEditor = new JButton("Quiz Editor");
		btnQuizEditor.addActionListener(this);
		getContentPane().add(btnQuizEditor);
		
		btnHostGame = new JButton("Host Game");
		btnHostGame.addActionListener(this);
		getContentPane().add(btnHostGame);
		
		btnJoinGame = new JButton("Join Game");
		btnJoinGame.addActionListener(this);
		getContentPane().add(btnJoinGame);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnQuizEditor) {
			main.showView(View.QUIZ_EDITOR);
		} else if (e.getSource() == btnHostGame) {
			main.showView(View.SERVER_MODE);
		} else {
			main.showView(View.CLIENT_MODE);
		}
		setVisible(false);
	}

}