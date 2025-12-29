import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main Application Class for the Student Management System.
 * Combines Student Management, Course Enrollment, and Grade Management.
 */
public class StudentManagementSystem extends JFrame {

    // Data Storage (In-Memory for this implementation)
    private List<Student> students = new ArrayList<>();
    private String[] courses = {"CS101 - Intro to Java", "CS202 - Data Structures", "MATH301 - Calculus", "ENG105 - Composition"};
    
    // GUI Components
    private JTabbedPane tabbedPane;
    private DefaultTableModel studentTableModel;
    private JTable studentTable;
    
    // Enrollment Components
    private JComboBox<String> enrollmentCourseBox;
    private DefaultListModel<Student> enrollmentStudentListModel;
    private JList<Student> enrollmentStudentList;

    // Grade Components
    private JComboBox<Student> gradeStudentBox;
    private DefaultListModel<String> enrolledCoursesListModel;
    private JList<String> enrolledCoursesList;
    private JTextField gradeInputField;

    public StudentManagementSystem() {
        setTitle("Elite Student Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize Data with some mocks
        seedData();

        // Layout Initialization
        initComponents();
    }

    private void seedData() {
        students.add(new Student("101", "Alice Johnson", 20));
        students.add(new Student("102", "Bob Smith", 22));
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));

        // 1. Student Management Tab
        tabbedPane.addTab("Student Records", createStudentPanel());

        // 2. Course Enrollment Tab
        tabbedPane.addTab("Course Enrollment", createEnrollmentPanel());

        // 3. Grade Management Tab
        tabbedPane.addTab("Grade Management", createGradePanel());

        add(tabbedPane);
    }

    // --- PANEL CREATORS ---

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table
        String[] columns = {"ID", "Name", "Age"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        studentTable = new JTable(studentTableModel);
        refreshStudentTable();

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add New Student");
        JButton updateBtn = new JButton("Update Selected");
        
        addBtn.addActionListener(e -> showAddStudentDialog());
        updateBtn.addActionListener(e -> showUpdateStudentDialog());

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);

        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEnrollmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Selection Area
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.add(new JLabel("Step 1: Select a Course"));
        enrollmentCourseBox = new JComboBox<>(courses);
        topPanel.add(enrollmentCourseBox);

        // Student List
        enrollmentStudentListModel = new DefaultListModel<>();
        enrollmentStudentList = new JList<>(enrollmentStudentListModel);
        refreshEnrollmentList();

        JButton enrollBtn = new JButton("Enroll Selected Student");
        enrollBtn.setBackground(new Color(70, 130, 180));
        enrollBtn.setForeground(Color.WHITE);
        enrollBtn.setOpaque(true);
        enrollBtn.setBorderPainted(false);

        enrollBtn.addActionListener(e -> handleEnrollment());

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(enrollmentStudentList), BorderLayout.CENTER);
        panel.add(enrollBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createGradePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Student Selector
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Student:"), gbc);
        
        gradeStudentBox = new JComboBox<>();
        updateGradeStudentCombo();
        gbc.gridx = 1;
        panel.add(gradeStudentBox, gbc);

        // Course List for specific student
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(new JLabel("Enrolled Courses & Current Grades:"), gbc);
        
        enrolledCoursesListModel = new DefaultListModel<>();
        enrolledCoursesList = new JList<>(enrolledCoursesListModel);
        gbc.gridy = 2; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(enrolledCoursesList), gbc);

        // Grade Input
        gbc.gridy = 3; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("New Grade (0-100):"));
        gradeInputField = new JTextField(5);
        inputPanel.add(gradeInputField);
        JButton assignBtn = new JButton("Assign Grade");
        inputPanel.add(assignBtn);
        panel.add(inputPanel, gbc);

        // Listeners
        gradeStudentBox.addActionListener(e -> updateEnrolledCoursesDisplay());
        assignBtn.addActionListener(e -> handleGradeAssignment());

        return panel;
    }

    // --- EVENT HANDLERS & LOGIC ---

    private void refreshStudentTable() {
        studentTableModel.setRowCount(0);
        for (Student s : students) {
            studentTableModel.addRow(new Object[]{s.id, s.name, s.age});
        }
    }

    private void refreshEnrollmentList() {
        enrollmentStudentListModel.clear();
        for (Student s : students) {
            enrollmentStudentListModel.addElement(s);
        }
    }

    private void updateGradeStudentCombo() {
        gradeStudentBox.removeAllItems();
        for (Student s : students) {
            gradeStudentBox.addItem(s);
        }
    }

    private void showAddStudentDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();

        Object[] message = {
            "Student ID:", idField,
            "Full Name:", nameField,
            "Age:", ageField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());

                if (id.isEmpty() || name.isEmpty()) throw new Exception("Fields cannot be empty.");

                students.add(new Student(id, name, age));
                triggerGlobalUpdates();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showUpdateStudentDialog() {
        int row = studentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table.");
            return;
        }

        Student s = students.get(row);
        JTextField nameField = new JTextField(s.name);
        JTextField ageField = new JTextField(String.valueOf(s.age));

        Object[] message = { "Name:", nameField, "Age:", ageField };

        int option = JOptionPane.showConfirmDialog(null, message, "Update Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                s.name = nameField.getText().trim();
                s.age = Integer.parseInt(ageField.getText().trim());
                triggerGlobalUpdates();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Update Failed: Check your inputs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEnrollment() {
        Student selectedStudent = enrollmentStudentList.getSelectedValue();
        String selectedCourse = (String) enrollmentCourseBox.getSelectedItem();

        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Select a student to enroll.");
            return;
        }

        if (selectedStudent.grades.containsKey(selectedCourse)) {
            JOptionPane.showMessageDialog(this, "Student is already enrolled in this course.");
        } else {
            selectedStudent.grades.put(selectedCourse, "N/A");
            JOptionPane.showMessageDialog(this, "Enrolled " + selectedStudent.name + " in " + selectedCourse);
            triggerGlobalUpdates();
        }
    }

    private void updateEnrolledCoursesDisplay() {
        enrolledCoursesListModel.clear();
        Student s = (Student) gradeStudentBox.getSelectedItem();
        if (s != null) {
            for (Map.Entry<String, String> entry : s.grades.entrySet()) {
                enrolledCoursesListModel.addElement(entry.getKey() + " | Current Grade: " + entry.getValue());
            }
        }
    }

    private void handleGradeAssignment() {
        Student s = (Student) gradeStudentBox.getSelectedItem();
        String selection = enrolledCoursesList.getSelectedValue();
        
        if (s == null || selection == null) {
            JOptionPane.showMessageDialog(this, "Select a student and an enrolled course.");
            return;
        }

        try {
            String grade = gradeInputField.getText().trim();
            double g = Double.parseDouble(grade);
            if (g < 0 || g > 100) throw new Exception();

            // Extract course name from list string (before the | )
            String courseName = selection.split(" \\| ")[0];
            s.grades.put(courseName, grade);
            
            gradeInputField.setText("");
            updateEnrolledCoursesDisplay();
            JOptionPane.showMessageDialog(this, "Grade assigned successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric grade between 0 and 100.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates all UI components to reflect changes in the data model.
     */
    private void triggerGlobalUpdates() {
        refreshStudentTable();
        refreshEnrollmentList();
        updateGradeStudentCombo();
        updateEnrolledCoursesDisplay();
    }

    // --- DATA CLASSES ---

    static class Student {
        String id;
        String name;
        int age;
        Map<String, String> grades; // Course Name -> Grade String

        public Student(String id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.grades = new HashMap<>();
        }

        @Override
        public String toString() {
            return name + " (ID: " + id + ")";
        }
    }

    public static void main(String[] args) {
        // Set Look and Feel to System Default for better aesthetics
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new StudentManagementSystem().setVisible(true);
        });
    }
}
