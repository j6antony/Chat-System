import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.*;
/**
 * Chat.java
 * Description: This class allows users to chat. It has a text box and a scroll pane
 *              where message will be displayed along with a back button to return to the menu.
 * Name: Johan Antony
 * Date Created: January 14, 2025
 * Date last modified: January 14, 2025
 */
public class Chat {
    private JFrame window = new JFrame("Chat");
    private String receivedMessage = "";
    private String header;

    /**
     * Description: This constructor initializes the chat window, sets up its layout, and adds the necessary components
     *              such as a message input field, a text display area, and buttons for sending messages and returning
     *              to the menu. Also, it integrates the client method and handles incoming and outgoing message.
     *              It also processes the messages and displays them on screen
     * Pre-Condition:
     *  - A valid `Client` object must be passed, representing the connected user.
     *  - A valid `Menu` object must be passed to allow for navigation back to the main menu.
     *  - The client object must be properly initialized with a username before calling this constructor.
     * Post-Condition:
     *  - The chat window will be created and displayed with a message input field and text area.
     *  - The back button shows the menu, and the send button is configured to send the input message.
     *  - A timer is set up to regularly check for incoming messages from the client.
     * @param client The `Client` object representing the user participating in the chat. This object is responsible for managing
     *               the connection, sending, and receiving messages.
     * @param menu The `Menu` object used for navigating back to the main menu when the back button is clicked.
     */
    public Chat(Client client, Menu menu) {
        header = "[" + client.getUsername() + "] ";
        window.setSize(500, 500);
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.shutdown();
            }

        });

        // Chat box
        JTextArea message = new JTextArea();
        message.setBounds(5, 350, 490, 80);
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(message);
        scrollPane.setBounds(5, 350, 490, 80);
        window.add(scrollPane);

        // Define the text area
        JTextPane textArea = new JTextPane();
        textArea.setEditable(false);
        textArea.setBounds(5, 5, 490, 345);
        JScrollPane scrollPanel = new JScrollPane(textArea);
        scrollPanel.setBounds(5, 5, 490, 345);
        window.add(scrollPanel);


        //exit chat button
        JTextField username = new JTextField("Username");
        username.setBounds(100, 100, 100, 30);
        window.add(username);
        JButton back = new JButton("⬅️");
        back.setBounds(5, 430, 80, 30);
        window.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.show();
                window.dispose();
                client.shutdown();
            }
        });

        // Send button
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(415, 430, 80, 30);
        window.add(sendButton);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = header + message.getText();
                client.setOutMessage(msg);
                message.setText(""); // Clear the input field
            }
        });
        message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendButton.doClick();
                    e.consume();
                }
            }
        });


        // Timer to check for new messages
        Timer messageChecker = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//this idea of implementing the action listener here did come from ChatGPT
                String newMessage = client.getInMessage();
                if (newMessage != null && !newMessage.isEmpty() && !newMessage.equals(receivedMessage)) {
                    receivedMessage = newMessage;
                    if (newMessage.contains("[System]")) {
                        addAlignedText(textArea, newMessage, StyleConstants.ALIGN_CENTER);
                    } else if (newMessage.contains("[" + client.getUsername() + "]")) {
                        addAlignedText(textArea, newMessage, StyleConstants.ALIGN_LEFT);
                    } else {
                        addAlignedText(textArea, newMessage, StyleConstants.ALIGN_RIGHT);
                    }
                    textArea.setCaretPosition(textArea.getDocument().getLength()); // Scroll to the bottom
                }
            }
        });
        messageChecker.start();
    }
    /**
     * Description: Displays the chat window.
     * Pre-Condition: The `Chat` object has been initialized and the GUI components are set up.
     * Post-Condition: The chat window is shown to the user.
     * @return void
     */
    public void show() {
        window.setVisible(true);
    }
    /**
     * Description: Adds a new message to the text pane and aligns it based on the specified alignment.
     *              The text is styled with a font size, font family, and bold style.
     * Pre-Condition: A valid `JTextPane`, text, and alignment value are provided.
     * Post-Condition: The message is appended to the text area with the specified alignment and style.
     * @param textPane The `JTextPane` to which the text will be added.
     * @param text The text to be inserted into the `JTextPane`.
     * @param alignment The alignment for the text (left, right, center).
     * @return void
     */
    public static void addAlignedText(JTextPane textPane, String text, int alignment) {
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setAlignment(attributes, alignment);
        StyleConstants.setFontSize(attributes, 14);
        StyleConstants.setFontFamily(attributes, "Arial");
        StyleConstants.setBold(attributes, true);

        try {
            doc.insertString(doc.getLength(), text + "\n", null);
            doc.setParagraphAttributes(doc.getLength() - text.length() - 1, text.length() + 1, attributes, false);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: Returns the current window associated with the chat.
     * Pre-Condition: The `window` must be initialized before calling this method.
     * Post-Condition: Returns the `window` object representing the chat window.
     * @return JFrame The current chat window.
     */
    public JFrame getWindow() {
        return window;
    }

    /**
     * Description: Returns the header of the chat.
     * Pre-Condition: The `header' must be initialized before calling this method.
     * Post-Condition: Returns the `header` string.
     * @return String The header of the chat.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Description: Sets the window for the chat.
     * Pre-Condition: A valid `JFrame` window object is passed as an argument.
     * Post-Condition: The window is updated with the provided JFrame.
     * @param window The `JFrame` window to be set for the chat.
     */
    public void setWindow(JFrame window) {
        this.window = window;
    }

    /**
     * Description: Sets the header for the chat.
     * Pre-Condition: A valid `String` header is passed as an argument.
     * Post-Condition: The header is updated with the provided String.
     * @param header The new header for the chat.
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Description: Returns the current received message.
     * Pre-Condition: The receivedMessage must be initialized before calling this method.
     * Post-Condition: Returns the receivedMessage string.
     * @return String The current received message.
     */
    public String getReceivedMessage() {
        return receivedMessage;
    }

    /**
     * Description: Sets the received message for the chat.
     * Pre-Condition: A valid String message is passed as an argument.
     * Post-Condition: The receivedMessage is updated with the provided String.
     * @param receivedMessage The new received message to be set.
     */
    public void setReceivedMessage(String receivedMessage) {
        this.receivedMessage = receivedMessage;
    }

    /**
     * Description: Returns a string representation of the Chat object.
     *              The string includes the header and receivedMessage of the chat.
     * Pre-Condition: The Chat object has been initialized with a valid header and receivedMessage.
     * Post-Condition: A string representation of the Chat object is returned.
     * @return A string representing the Chat object in the format "Chat: header, receivedMessage: received message"
     */
    @Override
    public String toString() {
        return "Chat:\nheader: " + header + ", receivedMessage: " + receivedMessage;
    }

    /**
     * Compares this Chat object to another object for equality. Two Chat objects are considered equal if they have the same header and receivedMessage.
     * Pre-Condition: The Chat object has been initialized with a valid header and receivedMessage.
     * Post-Condition: Returns true if both Chat objects have the same header and receivedMessage, false otherwise.
     * @param obj The object to be compared with this Chat object.
     * @return true if the two Chat objects have the same header and receivedMessage, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;  // Different class or null object
        }
        Chat other = (Chat) obj;
        return header != null && header.equals(other.header) && receivedMessage != null && receivedMessage.equals(other.receivedMessage);
    }
}

