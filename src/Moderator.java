import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/**
 * Moderator.java
 * Description: This class manages the server from the moderator's side. It allows the moderator to close the server
 *              and broadcasts a message to all users when the server is shut down.
 * Name: Johan Antony
 * Date Created: January 14, 2025
 * Date last modified: January 14, 2025
 */
public class Moderator {
    private JFrame window = new JFrame("Server Running");
    private String header;

    /**
     * Description: This constructor opens the moderator window where the server can be closed.
     * Pre-Condition:
     *  - client, server and menu must be initialized and passed to the constructor
     *  - The server must be running for window to work
     * Post-Condition:
     *  - The `Moderator` window is displayed
     *  - The "Close Server" button will broadcast a message to all connected clients, shut down the server, and close the moderator's window.
     *  - The user is returned to the menu after the server is shut down.
     * @param client A `Client` object representing the client connected to the server.
     * @param server A `Server` object representing the server that the moderator controls.
     * @param menu A `Menu` object representing the main menu that is displayed when the server is shut down.
     */
    public Moderator(Client client, Server server, Menu menu) {
        header = "[Moderator]";
        window.setSize(500, 500);
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                server.broadcastMessage("[Server]Server has been closed");
                server.shutdown();
                window.dispose();
            }
        });

        //close Server
        JButton closeServer = new JButton("Close Server");
        closeServer.setBounds(180, 200, 150, 30);
        window.add(closeServer);
        closeServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                server.broadcastMessage("[System]Server has been closed");
                server.shutdown();
                window.dispose();
                menu.show();
            }
        });
    }
    /**
     * Description: Returns the current window associated with the chat.
     * Pre-Condition: The window field must be initialized before calling this method.
     * Post-Condition: Returns the window object representing the chat window.
     * @return JFrame The current chat window.
     */
    public JFrame getWindow() {
        return window;
    }

    /**
     * Description: Returns the current header string used for the chat messages.
     * Pre-Condition: The header field must be initialized before calling this method.
     * Post-Condition: Returns the header string that is used for the chat messages.
     * @return String The current header used in the chat.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Description: Sets the header string for the chat messages.
     * Pre-Condition: A valid header string is passed as an argument.
     * Post-Condition: The header field is updated with the provided header value.
     * @param header The header string to be set for the chat messages.
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Description: Sets the window for the chat.
     * Pre-Condition: A valid JFrame window object is passed as an argument.
     * Post-Condition: The window field is updated with the provided JFrame.
     * @param window The JFrame window to be set for the chat.
     */
    public void setWindow(JFrame window) {
        this.window = window;
    }

    /**
     * Description: This method makes the moderator's window visible.
     * Pre-Condition: The Moderator object has been initialized and the window components have been set up.
     * Post-Condition: The moderator's window is displayed on the screen.
     * @return: void
     */
    public void show() {
        window.setVisible(true);
    }

    /**
     * Description: Returns a string representation of the Moderator object.
     * Pre-Condition: None.
     * Post-Condition: Returns a string that contains the class name and important properties of the Moderator object.
     * @return String The string representation of the Moderator object.
     */
    @Override
    public String toString() {
        return "Moderator:\n" + "windowTitle: " + window.getTitle() +", header: " + header;
    }
    /**
     * Description: Compares this Moderator object with another object to check for equality.
     * Pre-Condition: The object to compare must be a `Moderator` object.
     * Post-Condition: Returns true if both objects are equal, otherwise false.
     * @param obj The object to compare with this Moderator object.
     * @return boolean Returns true if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Objects are of different classes or the other is null
        }
        Moderator other = (Moderator) obj; // Cast the object to a Moderator
        return window.getTitle().equals(other.window.getTitle()) && header.equals(other.header);
    }
}
