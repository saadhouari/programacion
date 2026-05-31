package hello;

import javax.swing.JOptionPane;

public class tarea {

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            String choice = JOptionPane.showInputDialog(
                    "Game Menu:\n1 --> Odds/Evens\n2 --> Rock, Paper, Scissors\n3 --> Exit Game"
            );

            if (choice == null || choice.equals("3")) {
                JOptionPane.showMessageDialog(null, "See you!");
                exit = true;
            } else if (choice.equals("1")) {
                oddsEvens();
            } else if (choice.equals("2")) {
                rockPaperScissors();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid choice");
            }
        }
    }

    // Odds/Evens game
    public static void oddsEvens() {
        try {
            String[] options = {"pares", "nones"};

            String p1 = (String) JOptionPane.showInputDialog(
                    null,
                    "Player 1, choose:",
                    "Odds or Evens",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            String p2 = (String) JOptionPane.showInputDialog(
                    null,
                    "Player 2, choose:",
                    "Odds or Evens",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            int num1 = Integer.parseInt(JOptionPane.showInputDialog("Player 1, number:"));
            int num2 = Integer.parseInt(JOptionPane.showInputDialog("Player 2, number:"));

            int sum = num1 + num2;
            boolean isEven = sum % 2 == 0;

            String winner;
            if ((isEven && p1.equals("pares")) || (!isEven && p1.equals("nones"))) {
                winner = "Player 1 wins";
            } else {
                winner = "Player 2 wins";
            }

            JOptionPane.showMessageDialog(null, "Sum: " + sum + "\n" + winner);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Enter numbers only");
        }
    }
    // i did this with a help from the net 
    // Rock, Paper, Scissors game
    public static void rockPaperScissors() {
        String[] options = {"Piedra", "Papel", "Tijera"};

        String p1 = (String) JOptionPane.showInputDialog(
                null,
                "Player 1, choose:",
                "Piedra, Papel o Tijera",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        String p2 = (String) JOptionPane.showInputDialog(
                null,
                "Player 2, choose:",
                "Piedra, Papel o Tijera",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        String winner;
        if (p1.equals(p2)) {
            winner = "Draw";
        } else if (
                (p1.equals("Piedra") && p2.equals("Tijera")) ||
                (p1.equals("Papel") && p2.equals("Piedra")) ||
                (p1.equals("Tijera") && p2.equals("Papel"))
        ) {
            winner = "Player 1 wins";
        } else {
            winner = "Player 2 wins";
        }

        JOptionPane.showMessageDialog(null, "P1: " + p1 + "\nP2: " + p2 + "\n" + winner);
    }
}
