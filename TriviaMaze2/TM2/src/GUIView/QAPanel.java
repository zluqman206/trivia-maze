package GUIView;

import Model.Maze;
import Model.Room;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class QAPanel extends JPanel {

    private JPanel myQAPanel;
    private Maze myCurrentMaze;
    private JLabel myRoomLabel;
    private JLabel myQuestionLabel;
    private JPanel myAnswerBoxPanel;
   private Room currentRoom;

    public QAPanel(final Maze theMaze) {
        myCurrentMaze = theMaze;
        myQAPanel = new JPanel();
        myQAPanel.setPreferredSize(new Dimension(150, 150));
        myQAPanel.setBackground(new Color(255, 221, 153)); // Light Orange

        myRoomLabel = new JLabel();
        myRoomLabel.setText(Character.toString('A'));

        currentRoom= myCurrentMaze.getMyCurrentRoom();

        myQuestionLabel = new JLabel("Question: ");
        updateQuestionLabel(myCurrentMaze.getCurrentQuestion());

        myAnswerBoxPanel = new JPanel();  // Use a JPanel instead of a JTextField
        setMyAnswerBox(currentRoom.getCurrentQuestionType());
        setupLayout();
    }


    private void setupLayout() {
        myQAPanel.setLayout(new BorderLayout());

        // Center Panel for Question and Answer
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(255, 221, 153));

        //centerPanel.add(Box.createVerticalStrut(20)); // Add spacing
        centerPanel.add(myQuestionLabel);
        centerPanel.add(Box.createVerticalStrut(10)); // Add spacing
        centerPanel.add(myAnswerBoxPanel);  // Use myAnswerBoxPanel instead of myAnswerBox

        myQAPanel.add(myRoomLabel, BorderLayout.NORTH);
        myQAPanel.add(centerPanel, BorderLayout.CENTER);
    }

    public void changeRoomLetter(final Room theRoom) {
        myRoomLabel.setText(Character.toString(theRoom.getLetter()));
        myQuestionLabel.setText("Question: " + myCurrentMaze.getCurrentQuestion());
    }

    // Add a method to display a reaction based on the answer correctness
    private void displayAnswerReaction(boolean isAnswerCorrect) {
        if (isAnswerCorrect) {
            JOptionPane.showMessageDialog(this, "Correct! The door is now unlocked.");
        } else {
            JOptionPane.showMessageDialog(this, "Wrong! The door is locked.");
        }
    }

    // Modify the handleAnswerSelection method to call the answerQuestion method and display a reaction
    private void handleAnswerSelection(String selectedAnswer) {
        boolean isAnswerCorrect = myCurrentMaze.answerQuestion(selectedAnswer);
        displayAnswerReaction(isAnswerCorrect);

        if (isAnswerCorrect) {
            // If the answer is correct, update the maze panel
            //try {
                //NEED TO UPDATE MAZE
                updateContent();
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
        }
    }

    // Modify the updateContent method to set the answer box based on the current question type
    public void updateContent() {
        currentRoom = myCurrentMaze.getMyCurrentRoom();
        myRoomLabel.setText(Character.toString(currentRoom.getLetter()));
        String currentQuestionType = currentRoom.getCurrentQuestionType();
        updateQuestionLabel(currentRoom.getQuestion());
        setMyAnswerBox(currentQuestionType);
    }

    // Add a method to set the answer box based on the question type
    private void setMyAnswerBox(String questionType) {
        // Clear the components inside myAnswerBoxPanel
        myAnswerBoxPanel.removeAll();

        // Modify the existing code based on the question type
        if (questionType.equalsIgnoreCase("M")) {
            String[] options = {"A", "B", "C"};
            for (String option : options) {
                JButton optionButton = new JButton(option);
                optionButton.addActionListener(e -> handleAnswerSelection(option));
                myAnswerBoxPanel.add(optionButton);
            }
        } else if (questionType.equalsIgnoreCase("TF")) {
            String[] options = {"True", "False"};
            for (String option : options) {
                JButton optionButton = new JButton(option);
                optionButton.addActionListener(e -> handleAnswerSelection(option));
                myAnswerBoxPanel.add(optionButton);
            }
        } else {
            JLabel answerLabel = new JLabel("Answer: ");
            JTextField answerTextField = new JTextField();
            answerTextField.setPreferredSize(new Dimension(80, 40));
            JButton submitButton = new JButton("Submit");
            submitButton.addActionListener(e -> handleAnswerSelection(answerTextField.getText())); // Replace with your actual method

            myAnswerBoxPanel.add(answerLabel);
            myAnswerBoxPanel.add(answerTextField);
            myAnswerBoxPanel.add(submitButton);
        }

        myAnswerBoxPanel.revalidate();
        myAnswerBoxPanel.repaint();
    }

    private void updateQuestionLabel(String currentQuestion) {
        int maxCharactersPerLine = 30; // Adjust this value as needed
        String formattedText = "<html>" + currentQuestion.replaceAll("(.{" +
                maxCharactersPerLine + "})", "$1<br>") + "</html>";
        myQuestionLabel.setText(formattedText);
        //myQuestionLabel.setText("Question: " + currentQuestion);
    }

    public JPanel getQAPanel() {
        return myQAPanel;
    }
}
