package RegLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SimplePOS extends JDialog {
    private final JFrame frame; // The main application frame
    private final JTextArea billArea;  // Area to display the selected items and bill
    private final JTextField balanceField; // Field to enter the balance amount
    private double totalAmount; // Total amount of the bill
    private final ArrayList<PurchaseItem> purchaseHistory; // List to store the purchased items

    public SimplePOS() {
        frame = new JFrame("Simple POS"); // Create the main frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Welcome to the Restaurant Ordering System");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titleLabel, BorderLayout.NORTH);

        String[] foodItems = {
                "Burger",
                "Pizza",
                "Pasta",
                "Grilled Sandwich",
                "5 piece Fried Chicken",
                "1 slice of Apple Pie",
                "1 slice of Chocolate Cake",
                "Ice Cream Sundae",
                "Ice Lemon Tea",
                "Water"
        };
        JPanel itemPanel = new JPanel(new GridLayout(foodItems.length, 1, 0, 10));
        JButton[] itemButtons = new JButton[foodItems.length];

        for (int i = 0; i < foodItems.length; i++) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel itemLabel = new JLabel(foodItems[i]);
            itemLabel.setHorizontalAlignment(SwingConstants.CENTER);

            itemButtons[i] = new JButton();
            String[] foodImages = {
                    "Food/1.jpg",
                    "Food/2.jpg",
                    "Food/3.jpg",
                    "Food/4.jpg",
                    "Food/5.jpg",
                    "Food/6.jpg",
                    "Food/7.jpg",
                    "Food/8.jpg",
                    "Food/9.jpg",
                    "Food/10.jpg"
            };
            itemButtons[i].setIcon(new ImageIcon(foodImages[i]));
            itemButtons[i].setVerticalTextPosition(SwingConstants.BOTTOM);
            itemButtons[i].setHorizontalTextPosition(SwingConstants.CENTER);
            double[] foodPrices = {
                    5.99,
                    9.99,
                    8.99,
                    6.49,
                    12.99,
                    2.49,
                    3.99,
                    4.99,
                    1.99,
                    0.99
            };
            itemButtons[i].addActionListener(new AddItemButtonListener(foodItems[i], foodPrices[i]));

            JButton minusButton = new JButton("-");
            minusButton.addActionListener(new MinusItemButtonListener(foodItems[i], foodPrices[i]));

            panel.add(itemButtons[i], BorderLayout.CENTER);
            panel.add(itemLabel, BorderLayout.SOUTH);
            panel.add(minusButton, BorderLayout.EAST);

            itemPanel.add(panel);
        }

        billArea = new JTextArea();
        billArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(billArea);

        JButton printButton = new JButton("Print Bill");
        printButton.addActionListener(new PrintButtonListener());

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ResetButtonListener());

        balanceField = new JTextField(10);

        JComboBox<String> deliveryOptionComboBox = new JComboBox<>(new String[]{"Self Pickup", "Delivery"});
        deliveryOptionComboBox.addActionListener(new DeliveryOptionComboBoxListener());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(printButton);
        buttonPanel.add(resetButton);

        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        balancePanel.add(new JLabel("Balance: $"));
        balancePanel.add(balanceField);
        balancePanel.add(new JLabel("Delivery Option:"));
        balancePanel.add(deliveryOptionComboBox);

        frame.add(itemPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(balancePanel, BorderLayout.EAST);

        frame.setVisible(true);

        purchaseHistory = new ArrayList<>();
    }

    // ActionListener for adding an item to the bill
    private class AddItemButtonListener implements ActionListener {
        private final String itemName;
        private final double itemPrice;

        public AddItemButtonListener(String itemName, double itemPrice) {
            this.itemName = itemName;
            this.itemPrice = itemPrice;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            totalAmount += itemPrice; // Add the item price to the total amount
            billArea.append(itemName + "\t\t$" + itemPrice + "\n"); // Append the item to the bill area

            PurchaseItem purchaseItem = new PurchaseItem(itemName, itemPrice);
            purchaseHistory.add(purchaseItem); // Add the item to the purchase history
        }
    }
    // ActionListener for removing an item from the bill
    private class MinusItemButtonListener implements ActionListener {
        private final String itemName;
        private final double itemPrice;

        public MinusItemButtonListener(String itemName, double itemPrice) {
            this.itemName = itemName;
            this.itemPrice = itemPrice;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (totalAmount >= itemPrice) {
                totalAmount -= itemPrice; // Subtract the item price from the total amount
                billArea.setText(billArea.getText().replace(itemName + "\t\t$" + itemPrice + "\n", "")); // Remove the item from the bill area

                PurchaseItem removedItem = null;
                for (PurchaseItem item : purchaseHistory) {
                    if (item.itemName().equals(itemName) && item.itemPrice() == itemPrice) {
                        removedItem = item;
                        break;
                    }
                }
                if (removedItem != null) {
                    purchaseHistory.remove(removedItem); // Remove the item from the purchase history
                }
            }
        }
    }

    // ActionListener for printing the bill
    private class PrintButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (totalAmount > 0) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String dateTime = dateFormat.format(new Date());

                double balance;
                try {
                    balance = Double.parseDouble(balanceField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid balance amount!");
                    return;
                }

                if (balance >= totalAmount) {
                    double change = balance - totalAmount;
                    String billText = "========== Bill ==========\n" +
                            "Date/Time: " + dateTime + "\n\n" +
                            billArea.getText() +
                            "\nTotal Amount: $" + totalAmount + "\n" +
                            "==========================\n" +
                            "Balance: $" + balance + "\n" +
                            "Change: $" + change + "\n";

                    JOptionPane.showMessageDialog(frame, billText); // Display the bill in a dialog
                    billArea.setText(""); // Clear the bill area
                    totalAmount = 0; // Reset the total amount
                    balanceField.setText(""); // Clear the balance field

                    Purchase purchase = new Purchase(dateTime, purchaseHistory, totalAmount, balance, change);
                    savePurchase(purchase);
                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient balance!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No items to print.");
            }
        }
    }
    // ActionListener for resetting the bill
    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            billArea.setText(""); // Clear the bill area
            totalAmount = 0; // Reset the total amount
            balanceField.setText(""); // Clear the purchase history
            purchaseHistory.clear(); // Clear the balance field
        }
    }

    // ActionListener for selecting the delivery option
    private class DeliveryOptionComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            String selectedOption = (String) comboBox.getSelectedItem();
            JOptionPane.showMessageDialog(frame, "Selected Option: " + selectedOption);
        }
    }

    private void savePurchase(Purchase purchase) {
        // Code to save the purchase object to a database or file can be added here
        // For demonstration purposes, let's just print the purchase details
        System.out.println("Purchase Saved: " + purchase.toString());
    }

    private record PurchaseItem(String itemName, double itemPrice) {
    }

    private record Purchase(String dateTime, ArrayList<PurchaseItem> purchasedItems, double totalAmount, double balance,
                            double change) {

        @Override
        public String toString() {
            return "Purchase{" +
                    "dateTime='" + dateTime + '\'' +
                    ", purchasedItems=" + purchasedItems +
                    ", totalAmount=" + totalAmount +
                    ", balance=" + balance +
                    ", change=" + change +
                    '}';
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimplePOS::new);
    }
}
