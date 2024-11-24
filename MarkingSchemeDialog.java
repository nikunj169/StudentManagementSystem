public class MarkingSchemeDialog extends JDialog {
    private int courseId;
    private MarkingScheme markingScheme;
    private JTextField gradeAField, gradeAMinusField, gradeBPlusField, gradeBField, gradeBMinusField, gradeCField,
            gradeDField, gradeFField;
    private JButton saveButton;
}
private void initialize() {
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 2));

        add(new JLabel("Grade A:"));
        gradeAField = new JTextField();
        add(gradeAField);
  add(new JLabel("Grade A-:"));
        gradeAMinusField = new JTextField();
        add(gradeAMinusField);

        add(new JLabel("Grade B+:"));
        gradeBPlusField = new JTextField();
        add(gradeBPlusField);
 add(new JLabel("Grade B:"));
        gradeBField = new JTextField();
        add(gradeBField);

        add(new JLabel("Grade B-:"));
        gradeBMinusField = new JTextField();
        add(gradeBMinusField);
 add(new JLabel("Grade C:"));
        gradeCField = new JTextField();
        add(gradeCField);

        add(new JLabel("Grade D:"));
        gradeDField = new JTextField();
        add(gradeDField);
 add(new JLabel("Grade C:"));
        gradeCField = new JTextField();
        add(gradeCField);

        add(new JLabel("Grade D:"));
        gradeDField = new JTextField();
        add(gradeDField);
 add(new JLabel("Grade F:"));
        gradeFField = new JTextField();
        add(gradeFField);

        saveButton = new JButton("Save");
        add(saveButton);
  // Empty label to fill the grid
        add(new JLabel());

        // Action listener
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveMarkingScheme();
            }
        });
    }
private void saveMarkingScheme() {
        try {
            markingScheme = new MarkingScheme();
            markingScheme.setCourseId(courseId);
            markingScheme.setGradeA(Double.parseDouble(gradeAField.getText()));
            markingScheme.setGradeAMinus(Double.parseDouble(gradeAMinusField.getText()));
            markingScheme.setGradeBPlus(Double.parseDouble(gradeBPlusField.getText()));
            markingScheme.setGradeB(Double.parseDouble(gradeBField.getText()));
   markingScheme.setGradeBMinus(Double.parseDouble(gradeBMinusField.getText()));
            markingScheme.setGradeC(Double.parseDouble(gradeCField.getText()));
            markingScheme.setGradeD(Double.parseDouble(gradeDField.getText()));
            markingScheme.setGradeF(Double.parseDouble(gradeFField.getText()));
            DBHandler.saveMarkingScheme(markingScheme);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
        }
    }
