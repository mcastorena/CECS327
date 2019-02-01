import javax.swing.*;
import java.awt.*;

public class SearchListCellRenderer implements ListCellRenderer {
    private JPanel p;
    private JPanel iconPanel;
    private JLabel l;
    private JTextArea ta;

    public SearchListCellRenderer() {
        p = new JPanel();
        p.setLayout(new BorderLayout());

        // icon
        //iconPanel = new JPanel(new BorderLayout());
        //l = new JLabel("icon");
        // text
        //iconPanel.add(l, BorderLayout.NORTH);
        //p.add(iconPanel, BorderLayout.WEST);

        // text
        ta = new JTextArea();
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        p.add(ta, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(final JList list,
                                                  final Object value, final int index, final boolean isSelected,
                                                  final boolean hasFocus) {

        ta.setText(value.toString());
        int width = list.getWidth();
        // this is just to lure the ta's internal sizing mechanism into action
        if (width > 0)
            ta.setSize(width, Short.MAX_VALUE);
        if(hasFocus)
        {
            ta.setBackground(Color.lightGray);
        }
        else
            ta.setBackground(Color.white);
        return p;

    }
}

