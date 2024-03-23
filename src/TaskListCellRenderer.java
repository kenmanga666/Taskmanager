package src;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.SwingConstants;

public class TaskListCellRenderer  extends DefaultListCellRenderer {
    private static final Color HIGH_PRIORITY_COLOR = Color.RED;
    private static final Color MEDIUM_PRIORITY_COLOR = Color.ORANGE;
    private static final Color LOW_PRIORITY_COLOR = Color.GREEN;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Task task = (Task) value;
        switch (task.getPriority()) {
            case HIGH:
                setBackground(HIGH_PRIORITY_COLOR);
                break;
            case MEDIUM:
                setBackground(MEDIUM_PRIORITY_COLOR);
                break;
            case LOW:
                setBackground(LOW_PRIORITY_COLOR);
                break;
        }
        setText(task.getTitle()); // Seul le titre sera affiché dans la JList
        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}