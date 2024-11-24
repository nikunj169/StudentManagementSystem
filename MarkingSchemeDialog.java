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
