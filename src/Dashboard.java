import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Dashboard extends JFrame implements ActionListener {

    // Object for performing database operations.
    private SubscriptionService service;

    // Buttons for all project operations.
    private JButton addButton, viewButton, spendingButton,
            unusedButton, searchButton, updateButton,
            deleteButton, savingsButton;

    // Labels for displaying spending information.
    private JLabel monthlyLabel, yearlyLabel;

    // Colors used in the dashboard.
    private final Color NAVY = new Color(25, 45, 85);
    private final Color BLUE = new Color(55, 105, 190);
    private final Color LIGHT_BLUE = new Color(235, 243, 255);
    private final Color BACKGROUND = new Color(247, 249, 253);
    private final Color TEXT = new Color(45, 55, 75);

    // Constructor: creates the complete dashboard window.
    public Dashboard() {

        service = new SubscriptionService();

        setTitle("Subscription Waste Detector");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 550));

        // Main background panel.
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);

        // Create the left sidebar.
        mainPanel.add(createSidebar(), BorderLayout.WEST);

        // Create the main content area.
        mainPanel.add(createContent(), BorderLayout.CENTER);

        add(mainPanel);

        // Load spending information from the database.
        refreshSummary();

        setVisible(true);
    }

    // Creates the left navigation sidebar.
    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(210, 600));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 20, 25, 20));

        JLabel logo = new JLabel("SWD");
        logo.setFont(new Font("Arial", Font.BOLD, 30));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        //Creates Subscription title
        JLabel logoText = new JLabel("SUBSCRIPTION");
        logoText.setFont(new Font("Arial", Font.BOLD, 13));
        logoText.setForeground(Color.WHITE);
        logoText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoText2 = new JLabel("WASTE DETECTOR");
        logoText2.setFont(new Font("Arial", Font.BOLD, 13));
        logoText2.setForeground(Color.WHITE);
        logoText2.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(logoText);
        sidebar.add(logoText2);

        sidebar.add(Box.createVerticalStrut(45));

        JLabel menuTitle = new JLabel("MAIN MENU");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 12));
        menuTitle.setForeground(new Color(170, 190, 225));
        menuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(menuTitle);
        sidebar.add(Box.createVerticalStrut(18));

        String[] menuItems = {
                "Dashboard",
                "Add Subscription",
                "View Subscriptions",
                "Monthly Spending",
                "Unused Subscriptions",
                "Search Subscription",
                "Update Subscription",
                "Delete Subscription",
                "Potential Savings"
        };

        for (String item : menuItems) {

            JLabel menuLabel = new JLabel(item);
            menuLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            menuLabel.setForeground(Color.WHITE);
            menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            sidebar.add(menuLabel);
            sidebar.add(Box.createVerticalStrut(19));
        }

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("Java Swing | MySQL");
        footer.setFont(new Font("Arial", Font.PLAIN, 11));
        footer.setForeground(new Color(180, 195, 220));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(footer);

        return sidebar;
    }

    // Creates the main dashboard content.
    private JPanel createContent() {

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBackground(BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Welcome heading.
        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));
        headingPanel.setBackground(BACKGROUND);

        JLabel heading = new JLabel("Welcome to Your Dashboard");
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(NAVY);

        JLabel description = new JLabel(
                "Manage your subscriptions and track unnecessary expenses."
        );
        description.setFont(new Font("Arial", Font.PLAIN, 13));
        description.setForeground(Color.GRAY);

        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(8));
        headingPanel.add(description);

        content.add(headingPanel, BorderLayout.NORTH);

        // Center area contains summary cards and feature buttons.
        JPanel centerPanel = new JPanel(new BorderLayout(15, 20));
        centerPanel.setBackground(BACKGROUND);

        centerPanel.add(createSummaryPanel(), BorderLayout.NORTH);
        centerPanel.add(createFeaturePanel(), BorderLayout.CENTER);

        content.add(centerPanel, BorderLayout.CENTER);

        return content;
    }

    // Creates monthly and yearly spending summary cards.
    private JPanel createSummaryPanel() {

        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        summaryPanel.setBackground(BACKGROUND);

        monthlyLabel = new JLabel("Rs. 0.00");
        yearlyLabel = new JLabel("Rs. 0.00");

        summaryPanel.add(createSummaryCard(
                "MONTHLY SPENDING", monthlyLabel));

        summaryPanel.add(createSummaryCard(
                "ESTIMATED YEARLY SPENDING", yearlyLabel));

        return summaryPanel;
    }

    // Creates one spending summary card.
    private JPanel createSummaryCard(String title, JLabel amountLabel) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 232, 242)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(Color.GRAY);

        amountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        amountLabel.setForeground(BLUE);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(amountLabel);

        return card;
    }

    // Creates the eight feature buttons.
    private JPanel createFeaturePanel() {

        JPanel featureArea = new JPanel(new BorderLayout(10, 12));
        featureArea.setBackground(BACKGROUND);

        JLabel featureHeading = new JLabel("Subscription Management");
        featureHeading.setFont(new Font("Arial", Font.BOLD, 18));
        featureHeading.setForeground(NAVY);

        featureArea.add(featureHeading, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        buttonPanel.setBackground(BACKGROUND);

        addButton = createButton("＋  Add Subscription");
        viewButton = createButton("▤  View Subscriptions");
        spendingButton = createButton("₹  Monthly Spending");
        unusedButton = createButton("◷  Unused Subscriptions");
        searchButton = createButton("⌕  Search Subscription");
        updateButton = createButton("✎  Update Subscription");
        deleteButton = createButton("✕  Delete Subscription");
        savingsButton = createButton("₹  Potential Savings");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(spendingButton);
        buttonPanel.add(unusedButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(savingsButton);

        featureArea.add(buttonPanel, BorderLayout.CENTER);

        return featureArea;
    }

    // Creates and styles each feature button.
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(NAVY);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(215, 226, 242)),
                BorderFactory.createEmptyBorder(15, 12, 15, 12)
        ));

        button.addActionListener(this);

        return button;
    }

    // Handles button clicks.
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        if (source == addButton) {
            addSubscription();

        } else if (source == viewButton) {
            viewSubscriptions();

        } else if (source == spendingButton) {
            showMonthlySpending();

        } else if (source == unusedButton) {
            showUnusedSubscriptions();

        } else if (source == searchButton) {
            searchSubscription();

        } else if (source == updateButton) {
            updateSubscription();

        } else if (source == deleteButton) {
            deleteSubscription();

        } else if (source == savingsButton) {
            showPotentialSavings();
        }
    }

    // Displays a form to enter subscription details.
    private Subscription getSubscriptionDetails(int id) {

        JTextField nameField = new JTextField();
        JTextField costField = new JTextField();
        JTextField dateField = new JTextField();

        JComboBox<BillingCycle> cycleBox =
                new JComboBox<>(BillingCycle.values());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Subscription Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Cost:"));
        panel.add(costField);

        panel.add(new JLabel("Billing Cycle:"));
        panel.add(cycleBox);

        panel.add(new JLabel("Last Used Date (YYYY-MM-DD):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Subscription Details",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {

            String name = nameField.getText().trim();
            double cost = Double.parseDouble(
                    costField.getText().trim());

            BillingCycle cycle =
                    (BillingCycle) cycleBox.getSelectedItem();

            LocalDate date =
                    LocalDate.parse(dateField.getText().trim());

            if (name.isEmpty()) {
                throw new IllegalArgumentException(
                        "Name cannot be empty.");
            }

            if (!Double.isFinite(cost) || cost < 0) {
                throw new IllegalArgumentException(
                        "Enter a valid non-negative cost.");
            }

            if (date.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "Date cannot be in the future.");
            }

            return new Subscription(id, name, cost, cycle, date);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Enter a valid cost.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this, "Enter date in YYYY-MM-DD format.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this, e.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    // Adds a new subscription to the database.
    private void addSubscription() {

        Subscription subscription = getSubscriptionDetails(0);

        if (subscription == null) {
            return;
        }

        try {

            service.addSubscription(subscription);

            JOptionPane.showMessageDialog(
                    this, "Subscription added successfully!");

            refreshSummary();

        } catch (SQLException e) {
            showError(e);
        }
    }

    // Displays subscription details in a table.
    private void displayTable(ArrayList<Subscription> subscriptions) {

        String[] columns = {
                "ID", "Name", "Cost", "Billing Cycle", "Last Used Date"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {

            // Prevents direct editing of table values.
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Subscription s : subscriptions) {

            Object[] row = {
                    s.getId(),
                    s.getName(),
                    s.getCost(),
                    s.getBillingCycle(),
                    s.getLastUsedDate()
            };

            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650, 250));

        JOptionPane.showMessageDialog(
                this, scrollPane,
                "Subscription Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Displays all saved subscriptions.
    private void viewSubscriptions() {

        try {

            ArrayList<Subscription> subscriptions =
                    service.getAllSubscriptions();

            if (subscriptions.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this, "No subscriptions found.");

            } else {
                displayTable(subscriptions);
            }

        } catch (SQLException e) {
            showError(e);
        }
    }

    // Displays monthly and estimated yearly spending.
    private void showMonthlySpending() {

        try {

            double monthly = service.getTotalMonthlyCost();
            double yearly = service.getYearlySpending();

            JOptionPane.showMessageDialog(
                    this,
                    "Total Monthly Cost: Rs. "
                            + String.format("%.2f", monthly)
                            + "\nEstimated Yearly Spending: Rs. "
                            + String.format("%.2f", yearly),
                    "Spending Summary",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refreshSummary();

        } catch (SQLException e) {
            showError(e);
        }
    }

    // Finds subscriptions unused for 30 days or more.
    private void showUnusedSubscriptions() {

        try {

            ArrayList<Subscription> unused =
                    service.getUnusedSubscriptions(30);

            if (unused.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this, "No unused subscriptions found.");

            } else {
                displayTable(unused);
            }

        } catch (SQLException e) {
            showError(e);
        }
    }

    // Searches subscriptions by name.
    private void searchSubscription() {

        String name = JOptionPane.showInputDialog(
                this, "Enter subscription name to search:");

        if (name == null || name.trim().isEmpty()) {
            return;
        }

        try {

            ArrayList<Subscription> matches =
                    service.searchSubscriptions(name.trim());

            if (matches.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this, "No matching subscription found.");

            } else {
                displayTable(matches);
            }

        } catch (SQLException e) {
            showError(e);
        }
    }

    // Updates an existing subscription.
    private void updateSubscription() {

        String input = JOptionPane.showInputDialog(
                this, "Enter subscription ID to update:");

        if (input == null) {
            return;
        }

        try {

            int id = Integer.parseInt(input.trim());

            if (id <= 0) {
                throw new NumberFormatException();
            }

            ArrayList<Subscription> all =
                    service.getAllSubscriptions();

            boolean found = false;

            for (Subscription s : all) {

                if (s.getId() == id) {
                    found = true;
                    break;
                }
            }

            if (!found) {

                JOptionPane.showMessageDialog(
                        this, "Subscription not found.");

                return;
            }

            Subscription updated = getSubscriptionDetails(id);

            if (updated == null) {
                return;
            }

            if (service.updateSubscription(updated)) {

                JOptionPane.showMessageDialog(
                        this, "Subscription updated successfully!");

                refreshSummary();

            } else {

                JOptionPane.showMessageDialog(
                        this, "Subscription was not updated.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Enter a valid positive ID.");

        } catch (SQLException e) {
            showError(e);
        }
    }

    // Deletes a subscription using its ID.
    private void deleteSubscription() {

        String input = JOptionPane.showInputDialog(
                this, "Enter subscription ID to delete:");

        if (input == null) {
            return;
        }

        try {

            int id = Integer.parseInt(input.trim());

            if (id <= 0) {
                throw new NumberFormatException();
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this subscription?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            if (service.deleteSubscription(id)) {

                JOptionPane.showMessageDialog(
                        this, "Subscription deleted successfully!");

                refreshSummary();

            } else {

                JOptionPane.showMessageDialog(
                        this, "Subscription not found.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this, "Enter a valid positive ID.");

        } catch (SQLException e) {
            showError(e);
        }
    }

    // Calculates possible monthly savings.
    private void showPotentialSavings() {

        try {

            double savings =
                    service.getPotentialMonthlySavings(30);

            JOptionPane.showMessageDialog(
                    this,
                    "Potential Monthly Savings: Rs. "
                            + String.format("%.2f", savings)
                            + "\n\nBased on subscriptions unused for 30 days."
                            + "\nThis is an estimate, not an automatic cancellation.",
                    "Potential Savings",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {
            showError(e);
        }
    }

    // Updates the spending cards using current database values.
    private void refreshSummary() {

        try {

            double monthly = service.getTotalMonthlyCost();
            double yearly = service.getYearlySpending();

            monthlyLabel.setText(
                    "Rs. " + String.format("%.2f", monthly));

            yearlyLabel.setText(
                    "Rs. " + String.format("%.2f", yearly));

        } catch (SQLException e) {
            showError(e);
        }
    }

    // Displays database error messages.
    private void showError(SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Database operation failed:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}