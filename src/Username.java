import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;

/**
 * Username.java
 * Description: This class handles the username input and validation for the chat application.
 *              It allows users to input their username, which is then verified by the server.
 * Name: Johan Antony
 * Date Created: January 14, 2025
 * Date last modified: January 14, 2025
 */
public class Username {
    private JFrame window;
    private Menu menu;

    /**
     * Description: This constructor creates a username window where the program prompts you for a username
     *              if the username is taken then it will give you a pop window with an error.Else it will direct you to the chat window
     * Pre-Condition:
     *  - The client must be running and connected to server
     *  - The menu object should already be running
     * Post-Condition:
     *  - The username input screen is displayed, allowing the user to input and validate their username.
     *  - If the username is valid, the user is directed to the chat window.
     *  - If the username is invalid, the user is notified, and the input field is reset and a pop window will show up with error
     *  - There is a back button to return to the menu
     * @param client A `Client` object representing the connection to the server.
     * @param menu A `Menu` object representing the main menu from which the user can return after input.
     */
    public Username(Client client, Menu menu) {
        this.menu = menu;
        window = new JFrame();
        window.setSize(500, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setVisible(true);


        //Username
        JTextField username = new JTextField("Username");
        username.setBounds(100, 100, 100, 30);
        window.add(username);

        JButton back = new JButton("⬅️");
        back.setBounds(5, 5, 80, 30);
        window.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.show();
                window.dispose();
            }
        });

        //action listener for username
        username.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = username.getText();

                client.setOutMessage("[Username]" + input);
                client.setUsername(input);
                try {
                    Thread.sleep(100);//added to give buffer time
                }catch (InterruptedException e1) {
                    System.out.println(e1.getMessage());
                }

                if (client.getSpecialCommand().equals("[System, " + input + "]" + input)) {
                    new Chat(client, menu).show();
                    window.dispose();
                }else {
                    client.setUsername(null);
                    username.setText("");
                    JOptionPane.showMessageDialog(window, client.getInMessage());
                }
            }
        });

    }
    /**
     * Description: Gets the JFrame window associated with the class.
     * Pre-Condition: The window attribute should be initialized.
     * Post-Condition: Returns the current JFrame window.
     * @return JFrame The JFrame window object.
     */
    public JFrame getWindow() {
        return window;
    }
    /**
     * Description: Gets the Menu object associated with the class.
     * Pre-Condition: The menu attribute should be initialized.
     * Post-Condition: Returns the current Menu object.
     * @return Menu The Menu object associated with the class.
     */
    public Menu getMenu() {
        return menu;
    }
    /**
     * Description: Sets the JFrame window for the class.
     * Pre-Condition: A valid JFrame window object is passed to the method.
     * Post-Condition: The window attribute is updated with the provided JFrame object.
     * @param window The new JFrame window to be set.
     */
    public void setWindow(JFrame window){
        this.window = window;
    }
    /**
     * Description: Sets the Menu object for the class.
     * Pre-Condition: A valid Menu object is passed to the method.
     * Post-Condition: The menu attribute is updated with the provided Menu object.
     * @param menu The new Menu object to be set.
     */
    public void setMenu(Menu menu){
        this.menu = menu;
    }

    /**
     * Description: This method makes the username input window visible, allowing users to input and validate their
     *              username for the chat application.
     * Pre-Condition: The Username window has been initialized.
     * Post-Condition: The username input window is displayed on the screen.
     */
    public void show() {
        window.setVisible(true);
    }
    /**
     * Description: Returns a string representation of the Username object.
     * Pre-Condition: None.
     * Post-Condition: Returns a string that contains the class name and important properties of the Username object.
     * @return String The string representation of the Username object.
     */
    @Override
    public String toString() {
        return "Username:\n" + "window: " + window.getTitle() + ", menu=" + menu;
    }
    /**
     * Description: Compares this Username object with another object to check for equality.
     * Pre-Condition: The object to compare must be a `Username` object.
     * Post-Condition: Returns true if both objects are equal, otherwise false.
     * @param obj The object to compare with this Username object.
     * @return boolean Returns true if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Objects are of different classes or the other is null
        }
        Username other = (Username) obj; // Cast the object to a Username
        return window.getTitle().equals(other.window.getTitle()) && menu.equals(other.menu);
    }

}
