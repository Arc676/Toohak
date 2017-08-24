//Written by Alessandro Vinciguerra <alesvinciguerra@gmail.com>
//Copyright (C) 2017  Arc676/Alessandro Vinciguerra

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation (version 3).

//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.
//See README.txt and LICENSE.txt for more details

package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import backend.Question;
import backend.QuestionListModel;
import backend.Quiz;

public class QuizEditor extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1125527090979758382L;

	private JTextField nameField;

	private JButton btnLoad;
	private JButton btnSave;

	private JTable questionList;
	private QuestionListModel qlistModel;

	private JTextField questionField;
	private JTextField ansA;
	private JTextField ansB;
	private JTextField ansC;
	private JTextField ansD;

	private JButton btnAddQuestion;
	private JButton btnRemoveQuestion;

	private JFileChooser jfc = new JFileChooser();

	private Quiz quiz;
	private boolean modified = false;
	private JTextField timeField;
	private JButton btnEditQuestion;

	public QuizEditor() {
		setTitle("Toohak Quiz Editor");
		setBounds(100, 100, 650, 440);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_9 = new JPanel();
		getContentPane().add(panel_9);
		panel_9.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_11 = new JPanel();
		panel_9.add(panel_11);
		panel_11.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel = new JPanel();
		panel_11.add(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		JLabel lblQuizName = new JLabel("Quiz Name");
		panel.add(lblQuizName);

		nameField = new JTextField();
		panel.add(nameField);
		nameField.setColumns(10);

		btnSave = new JButton("Save");
		btnSave.addActionListener(this);
		panel.add(btnSave);

		btnLoad = new JButton("Load");
		btnLoad.addActionListener(this);
		panel.add(btnLoad);

		JPanel panel_10 = new JPanel();
		panel_11.add(panel_10);
		panel_10.setLayout(new BorderLayout(0, 0));

		JLabel lblQuestion = new JLabel("Question");
		panel_10.add(lblQuestion, BorderLayout.WEST);

		questionField = new JTextField();
		panel_10.add(questionField);
		questionField.setColumns(10);

		JPanel panel_12 = new JPanel();
		panel_11.add(panel_12);
		panel_12.setLayout(new BorderLayout(0, 0));

		JLabel lblTime = new JLabel("Time");
		panel_12.add(lblTime, BorderLayout.WEST);

		timeField = new JTextField();
		panel_12.add(timeField, BorderLayout.CENTER);
		timeField.setColumns(10);

		JPanel panel_1 = new JPanel();
		panel_9.add(panel_1);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel_6.add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblA = new JLabel("A");
		panel_2.add(lblA);

		JLabel lblB = new JLabel("B");
		panel_2.add(lblB);

		JPanel panel_3 = new JPanel();
		panel_6.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));

		ansA = new JTextField();
		panel_3.add(ansA);
		ansA.setColumns(10);

		ansB = new JTextField();
		panel_3.add(ansB);
		ansB.setColumns(10);

		JPanel panel_7 = new JPanel();
		panel_1.add(panel_7);
		panel_7.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		panel_7.add(panel_4, BorderLayout.WEST);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblC = new JLabel("C");
		panel_4.add(lblC);

		JLabel lblD = new JLabel("D");
		panel_4.add(lblD);

		JPanel panel_5 = new JPanel();
		panel_7.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 1, 0, 0));

		ansC = new JTextField();
		panel_5.add(ansC);
		ansC.setColumns(10);

		ansD = new JTextField();
		panel_5.add(ansD);
		ansD.setColumns(10);

		JPanel panel_8 = new JPanel();
		panel_1.add(panel_8);
		panel_8.setLayout(new GridLayout(0, 1, 0, 0));

		btnAddQuestion = new JButton("Add Question");
		btnAddQuestion.addActionListener(this);
		panel_8.add(btnAddQuestion);

		btnRemoveQuestion = new JButton("Delete Selected Question");
		btnRemoveQuestion.addActionListener(this);
		panel_8.add(btnRemoveQuestion);

		btnEditQuestion = new JButton("Edit Selected Question");
		btnEditQuestion.addActionListener(this);
		panel_8.add(btnEditQuestion);

		qlistModel = new QuestionListModel();

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane);
		questionList = new JTable(qlistModel);
		scrollPane.setViewportView(questionList);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLoad) {
			if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					quiz = Quiz.read(jfc.getSelectedFile().getAbsolutePath());
					nameField.setText(quiz.quizName);
					for (Question q : quiz.getQuestionList()) {
						ArrayList<String> answers = q.getAnswers();
						qlistModel.addQuestion(q.getQ(), answers.get(0), answers.get(1), answers.get(2), answers.get(3),
								Integer.toString(q.getTimeLimit()));
					}
				} catch (ClassNotFoundException | IOException e1) {
					JOptionPane.showMessageDialog(null, "Failed to load quiz");
				}
			}
		} else if (e.getSource() == btnSave) {
			if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				if (modified) {
					int time = 0;
					try {
						time = Integer.parseInt(timeField.getText());
					} catch (NumberFormatException e1) {
						timeField.setText("Please enter an integer");
						return;
					}
					ArrayList<Question> questions = new ArrayList<Question>();
					for (String[] qs : qlistModel.getObjects()) {
						ArrayList<String> answers = new ArrayList<String>();
						for (int i = 1; i < qs.length; i++) {
							answers.add(qs[i]);
						}
						questions.add(new Question(questionField.getText(), time, answers));
					}
					quiz = new Quiz(nameField.getText(), questions);
					modified = false;
				}
				try {
					quiz.save(jfc.getSelectedFile().getAbsolutePath());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Failed to save quiz");
				}
			}
		} else if (e.getSource() == btnAddQuestion) {
			modified = true;
			qlistModel.addQuestion(questionField.getText(), ansA.getText(), ansB.getText(), ansC.getText(),
					ansD.getText(), timeField.getText());
		} else if (e.getSource() == btnRemoveQuestion) {
			removeSelectedQuestion();
		} else if (e.getSource() == btnEditQuestion) {
			int row = questionList.getSelectedRow();
			if (row >= 0) {
				questionField.setText((String) qlistModel.getValueAt(row, 0));
				ansA.setText((String) qlistModel.getValueAt(row, 1));
				ansB.setText((String) qlistModel.getValueAt(row, 2));
				ansC.setText((String) qlistModel.getValueAt(row, 3));
				ansD.setText((String) qlistModel.getValueAt(row, 4));
				timeField.setText((String) qlistModel.getValueAt(row, 5));
				removeSelectedQuestion();
			}
		}
	}

	private void removeSelectedQuestion() {
		int row = questionList.getSelectedRow();
		if (row >= 0) {
			modified = true;
			qlistModel.removeQuestion(row);
		}
	}

}