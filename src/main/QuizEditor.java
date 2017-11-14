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
import javax.swing.JCheckBox;
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

	private JCheckBox aOK;
	private JCheckBox bOK;
	private JCheckBox cOK;
	private JCheckBox dOK;

	private JButton btnAddQuestion;
	private JButton btnRemoveQuestion;

	private JFileChooser jfc = new JFileChooser();

	private Quiz quiz;
	private boolean modified = false;
	private JTextField timeField;
	private JButton btnEditQuestion;
	private JTextField pointsField;

	public QuizEditor() {
		setTitle("Toohak Quiz Editor");
		setBounds(100, 100, 800, 550);
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

		JPanel panel_13 = new JPanel();
		panel_11.add(panel_13);
		panel_13.setLayout(new BorderLayout(0, 0));

		JLabel lblPoints = new JLabel("Max Points");
		panel_13.add(lblPoints, BorderLayout.WEST);

		pointsField = new JTextField();
		panel_13.add(pointsField, BorderLayout.CENTER);
		pointsField.setColumns(10);

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

		JPanel panel_ab = new JPanel();
		panel_ab.setLayout(new GridLayout(0, 1, 0, 0));

		aOK = new JCheckBox();
		panel_ab.add(aOK);
		bOK = new JCheckBox();
		panel_ab.add(bOK);

		panel_6.add(panel_ab, BorderLayout.EAST);

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

		JPanel panel_cd = new JPanel();
		panel_cd.setLayout(new GridLayout(0, 1, 0, 0));

		cOK = new JCheckBox();
		panel_cd.add(cOK);
		dOK = new JCheckBox();
		panel_cd.add(dOK);

		panel_7.add(panel_cd, BorderLayout.EAST);

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
						qlistModel.addQuestion(q);
					}
					modified = false;
				} catch (ClassNotFoundException | IOException e1) {
					JOptionPane.showMessageDialog(null, "Failed to load quiz");
				}
			}
		} else if (e.getSource() == btnSave) {
			if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				if (modified) {
					quiz = new Quiz(nameField.getText(), qlistModel.getObjects());
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
			ArrayList<String> answers = new ArrayList<String>(4);
			answers.add(ansA.getText());
			answers.add(ansB.getText());
			answers.add(ansC.getText());
			answers.add(ansD.getText());
			try {
				qlistModel.addQuestion(new Question(questionField.getText(), Integer.parseInt(timeField.getText()),
						Integer.parseInt(pointsField.getText()), answers,
						new boolean[] { aOK.isSelected(), bOK.isSelected(), cOK.isSelected(), dOK.isSelected() }));
				questionField.setText("");
				timeField.setText("");
				pointsField.setText("");
				ansA.setText("");
				ansB.setText("");
				ansC.setText("");
				ansD.setText("");
				aOK.setSelected(false);
				bOK.setSelected(false);
				cOK.setSelected(false);
				dOK.setSelected(false);
			} catch (NumberFormatException e1) {}
		} else if (e.getSource() == btnRemoveQuestion) {
			removeSelectedQuestion();
		} else if (e.getSource() == btnEditQuestion) {
			int row = questionList.getSelectedRow();
			if (row >= 0) {
				Question q = qlistModel.getObjects().get(row);
				ArrayList<String> answers = q.getAnswers();
				questionField.setText(q.getQ());
				ansA.setText(answers.get(0));
				ansB.setText(answers.get(1));
				ansC.setText(answers.get(2));
				ansD.setText(answers.get(3));
				timeField.setText(Integer.toString(q.getTimeLimit()));
				pointsField.setText(Integer.toString(q.getPoints()));
				aOK.setSelected(q.acceptAnswer(0));
				bOK.setSelected(q.acceptAnswer(1));
				cOK.setSelected(q.acceptAnswer(2));
				dOK.setSelected(q.acceptAnswer(3));
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