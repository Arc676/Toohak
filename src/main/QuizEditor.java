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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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

/**
 * Quiz editor view in which users can create,
 * save, and load quizzes.
 * @author Ale
 *
 */
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

	private File loadedImage;
	private JButton btnLoadImage;
	private JButton btnClearImage;
	private JPanel imagePanel;

	private JFileChooser jfc = new JFileChooser();

	private Quiz quiz;
	private boolean modified = false;
	private JTextField timeField;
	private JButton btnEditQuestion;
	private JTextField pointsField;

	public QuizEditor() {
		setTitle("Toohak Quiz Editor");
		setBounds(100, 50, 800, 700);
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

		JPanel superpanel_img = new JPanel();
		superpanel_img.setLayout(new GridLayout(1, 0, 0, 0));
		panel_9.add(superpanel_img);

		JPanel panel_img = new JPanel();
		superpanel_img.add(panel_img);
		panel_img.setLayout(new GridLayout(0, 1, 0, 0));

		btnLoadImage = new JButton("Add Image");
		panel_img.add(btnLoadImage);
		btnLoadImage.addActionListener(this);

		btnClearImage = new JButton("Remove Image");
		panel_img.add(btnClearImage);
		btnClearImage.addActionListener(this);

		imagePanel = new JPanel();
		imagePanel.setLayout(new GridLayout(1, 0, 0, 0));
		superpanel_img.add(imagePanel);

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
			// ask user to select a file and load the quiz
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
			// ask user for a save location and save the file
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
			// obtain question data from UI
			modified = true;
			ArrayList<String> answers = new ArrayList<String>(4);
			answers.add(ansA.getText());
			answers.add(ansB.getText());
			answers.add(ansC.getText());
			answers.add(ansD.getText());
			int answerCount = 0;
			// question must contain at least two non-empty answers to be valid
			boolean isValid = false;
			for (String ans : answers) {
				if (!ans.equals("")) {
					answerCount++;
					if (answerCount >= 2) {
						isValid = true;
						break;
					}
				}
			}
			if (!isValid) {
				JOptionPane.showMessageDialog(null, "Questions need at least 2 answers", "Invalid question",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// only non-empty answers can be correct for the question to be valid
			// additionally, at least one answer must be correct
			isValid = false;
			boolean accepted[] = new boolean[] { aOK.isSelected(), bOK.isSelected(), cOK.isSelected(),
					dOK.isSelected() };
			for (int i = 0; i < 4; i++) {
				if (accepted[i]) {
					if (answers.get(i).equals("")) {
						isValid = false;
						break;
					} else {
						isValid = true;
					}
				}
			}
			if (!isValid) {
				JOptionPane.showMessageDialog(null, "Question either has no correct answers or accepts an empty answer",
						"Invalid question", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				Question question = new Question(questionField.getText(), Integer.parseInt(timeField.getText()),
						Integer.parseInt(pointsField.getText()), answers, accepted);

				// load image into question data and clear UI so next question can be entered
				if (loadedImage != null) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(ImageIO.read(loadedImage), "jpg", baos);
					question.setMultimediaDataForKey(Question.keyHasImage, true);
					question.setMultimediaDataForKey(Question.keyImageData, baos.toByteArray());
				}
				
				qlistModel.addQuestion(question);
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
				loadImage(null);
			} catch (NumberFormatException | IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == btnRemoveQuestion) {
			removeSelectedQuestion();
		} else if (e.getSource() == btnEditQuestion) {
			// load question data into UI and remove from question list
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
		} else if (e.getSource() == btnClearImage) {
			// remove image from question
			loadedImage = null;
			loadImage(null);
		} else if (e.getSource() == btnLoadImage) {
			// load an image from file
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				loadedImage = jfc.getSelectedFile();
				loadImage(loadedImage);
			}
		}
	}
	
	/**
	 * Load the specified image into the panel
	 * for previewing
	 * @param file File of the image
	 */
	private void loadImage(File file) {
		imagePanel.removeAll();
		if (file != null) {
			try {
				JLabel lbl = new JLabel(new ImageIcon(new ImageIcon(ImageIO.read(file)).getImage()
						.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_DEFAULT)));
				imagePanel.add(lbl);
				validate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		imagePanel.repaint();
	}

	private void removeSelectedQuestion() {
		int row = questionList.getSelectedRow();
		if (row >= 0) {
			modified = true;
			qlistModel.removeQuestion(row);
		}
	}

}