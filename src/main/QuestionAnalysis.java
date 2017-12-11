package main;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import backend.Question;

public class QuestionAnalysis extends JPanel {

	private static final long serialVersionUID = -4252183076285924502L;
	
	private static final int width = 700, height = 500;
	
	private Question question;
	private int answerCount[];
	
	public void loadData(Question q, int[] answers) {
		question = q;
		answerCount = answers;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		g.drawString(question.getQ(), 50, 100);
		g.drawString("Answers", 70, 100);
		
		int y = 120;
		int i = 0;
		for (String ans : question.getAnswers()) {
			boolean correct = question.acceptAnswer(i);
			g.drawString((correct ? "->" : "") + ans, (correct ? 85 : 90), y);
			y += 20;
			i++;
		}
		
		g.setColor(Color.RED);
		g.fillRect(200, 120, 20 * answerCount[0], 15);
		
		g.setColor(Color.BLUE);
		g.fillRect(200, 140, 20 * answerCount[1], 15);
		
		g.setColor(Color.GREEN);
		g.fillRect(200, 160, 20 * answerCount[2], 15);
		
		g.setColor(Color.ORANGE);
		g.fillRect(200, 180, 20 * answerCount[3], 15);
	}

}
