package me.bannock.capstone.loader.ui.frames;

import com.google.inject.Inject;
import me.bannock.capstone.loader.account.AccountDTO;
import me.bannock.capstone.loader.account.AccountManager;
import me.bannock.capstone.loader.ui.UiUtils;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AboutAccountFrame extends JFrame {

    @Inject
    public AboutAccountFrame(AccountManager accountManager){
        this.accountManager = accountManager;
    }

    private final AccountManager accountManager;

    /**
     * Populates the frame and then displays it, using the passed in frame
     * as a parent frame
     * @param parent The parent frame
     */
    public void showAndCreateWithParent(JFrame parent){
        populateFrame();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Populates this frame with the needed components. Does not clear or remove old components
     */
    public void populateFrame(){
        if (!accountManager.isLoggedIn() || !accountManager.getUser().isPresent()){
            UiUtils.displayErrorDialog("Not logged in", "You need to be logged in to do this action");
            return;
        }
        AccountDTO user = accountManager.getUser().get();

        setTitle("My account");
        setSize(275, 225);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(content);

        content.add(new JLabel("<html><h3>My account:</h3></html>"));

        JLabel uid = new JLabel(String.format("User ID: %s", user.getUid()));
        content.add(uid);

        JLabel email = new JLabel(String.format("Email: %s", user.getEmail()));
        content.add(email);

        JLabel username = new JLabel(String.format("Username: %s", user.getUsername()));
        content.add(username);

    }
}
