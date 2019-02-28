package main;

import javax.swing.*;
import java.awt.*;
import main.java.model.Collection;

// Enables multiline list cells and creates cell border
public class SearchListCellRenderer implements ListCellRenderer {
    private JPanel panel;
    private JTextArea textArea;

    SearchListCellRenderer() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        panel.add(textArea, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(final JList list,
                                                  final Object collection, final int index, final boolean isSelected,
                                                  final boolean hasFocus) {
        Collection c = (Collection) collection;
        textArea.setText(String.format("%s \n - %s", c.getSongTitle(), c.getArtistName()));

        // Change list item color when selected
        if(hasFocus)
            textArea.setBackground(Color.lightGray);
        else
            textArea.setBackground(Color.white);

        // Set border between list items
        textArea.setBorder(BorderFactory.createEtchedBorder());

        return panel;
    }
}

