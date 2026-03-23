import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Menu.java
 * Description: This class represents the main menu for the chat application. It allows users to either
 *              join an existing server, create a new server, or exit the application.
 * Name: Johan Antony
 * Date Created: January 14, 2025
 * Date last modified: January 14, 2025
 */
public class Menu {
    private JFrame window;

    /**
     * Description: This constructor initializes the menu which is where the users first land when the program is running.
     *              There is the option to exit the app, create a server and join a server. If joining a server you have to type
     *              in the device id of where the server is hosted. If it is on you computer it is localhost.
     * Pre-Condition:
     *  - The `Menu` window is initialized with three options: "Join A Server", "Create A Server", and "Exit".
     *  - The `Client` and `Server` classes must be available and functional for connecting or creating servers.
     * Post-Condition:
     *  - When the "Join A Server" button is clicked, the system attempts to connect to the server, prompting
     *    the user for a username if successful, or displaying an error if the server is unavailable.
     *  - When the "Create A Server" button is clicked, a new server and client are created, and the moderator view is displayed.
     *  - If the "Exit" button is clicked, the application is closed.
     */
    public Menu() {
        // Invoked on the event dispatching thread. Do any initialization here.
        this.window = new JFrame("Menu");
        window.setSize(500, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);

        //join a server field
        JTextField joinAServer = new JTextField("Join A Server");
        joinAServer.setBounds(185, 50, 120, 30);
        window.add(joinAServer);
        //action listener for join a server field
        joinAServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //try to connect with the host and if that work make a thread for the client
                String hostname = joinAServer.getText();
                System.out.println(hostname);
                System.out.println(Client.checkConnection(hostname));
                if (Client.checkConnection(hostname)) {
                    Client client = new Client(hostname);
                    Thread thread = new Thread(client);
                    thread.start();
                    window.setVisible(false);
                    Username username = new Username(client, Menu.this);
                    username.show();
                }else {
                    JOptionPane.showMessageDialog(window, "Server port not available");
                    joinAServer.setText("");
                }




            }
        });

        //create a server field
        JButton createAServer = new JButton("Create A Server");
        createAServer.setBounds(185, 80, 120, 30);
        window.add(createAServer);
        createAServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Server server = new Server();
                Thread serverThread = new Thread(server);
                Client client = new Client("localhost");
                Thread thread = new Thread(client);
                thread.start();
                serverThread.start();
                window.setVisible(false);
                new Moderator(client, server, Menu.this).show();
            }
        });
        //exit
        JButton exit = new JButton("Exit");
        exit.setBounds(185, 110, 120, 30);
        window.add(exit);
        //exit button program
        exit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

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
     * Description: Returns the current window associated with the chat.
     * Pre-Condition: The window must be initialized before calling this method.
     * Post-Condition: Returns the window object representing the chat window.
     * @return JFrame The current chat window.
     */
    public JFrame getWindow() {
        return window;
    }

    /**
     * Description: This method makes the menu window visible, allowing the user to select options to join a server,
     *              create a new server, or exit the application.
     * Pre-Condition: The Menu window has been initialized and contains the buttons for the available actions.
     * Post-Condition: The menu window becomes visible, and the user can interact with the options.
     */
    public void show() {
        // Show the UI.
        window.setVisible(true);
    }
    /**
     * Description: Returns a string representation of the Menu object.
     * Pre-Condition: None.
     * Post-Condition: Returns a string that contains the class name and important properties of the Menu object.
     * @return String The string representation of the Menu object.
     */
    @Override
    public String toString() {
        return "Menu:\n" + "windowTitle: " + window.getTitle();
    }

    /**
     * Description: Compares this Menu object with another object to check for equality.
     * Pre-Condition: The object to compare must be a `Menu` object.
     * Post-Condition: Returns true if both objects are equal, otherwise false.
     * @param obj The object to compare with this Menu object.
     * @return boolean Returns true if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Objects are of different classes or the other is null
        }
        Menu other = (Menu) obj; // Cast the object to a Menu
        return window.getTitle().equals(other.window.getTitle());
    }
}
