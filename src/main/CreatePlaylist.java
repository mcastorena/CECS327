package main;

import main.java.model.Playlist;
import main.java.model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CreatePlaylist extends JFrame {
    private JPanel mainPanel;
    private JPanel namePanel;
    private JPanel descriptionPanel;
    private JPanel buttonPanel;
    private JTextField titleInput;
    private JTextField descriptionInput;
    private JButton cancelButton;
    private JButton createButton;

    CreatePlaylist(User user, mp3Player player) {
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("MP3 Player");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleInput.getText();
                String desc = descriptionInput.getText();

                if (title.equals("")) {
                    // Alert user title is empty
                    System.out.println("Title is empty");
                    return;
                }

                if (desc.equals("")) {
                    // Alert user description is empty
                    System.out.println("Description is empty");
                    return;
                }

                Playlist newPlaylist = new Playlist(title);
                user.userProfile.addPlaylist(title, newPlaylist);

//                player.showPlaylists(user.userProfile.playlists.keySet().toArray());
                CreatePlaylist.super.dispose();
            }
        });

        cancelButton.addActionListener(e -> this.dispose());
    }
}
