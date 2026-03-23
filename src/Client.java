import java.io.*;
import java.net.Socket;
/**
 * Client.java
 * Description: This class handles the client-side logic for connecting to a server, sending and receiving messages,
 *              and managing the communication between the client and the server. It also handles passing a username
 *              to the server and collecting special commands from the server
 * Name: Johan Antony
 * Date Created: January 14, 2025
 * Date last modified: January 14, 2025
 */
class Client implements Runnable {
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;
    private String hostname;
    private boolean running = true;
    private String outMessage = "";
    private String inMessage = "";
    private String specialCommand = "";
    private String username = "";

    /**
     * Description: this constructor sets up the attribute hostname to create a connection with the server
     * Pre-Condition:
     *  - A valid hostname (IP address or domain name) must be provided, pointing to the server that the client will connect to.
     *  - The server must be running and accepting connections on port 4444.
     * Post-Condition:
     *  - The `Client` object is initialized with the specified hostname and ready to initiate a connection with the server.
     *  - The input and output streams for communication are established.
     *  - A new thread is created for listening to incoming messages.
     * @param hostname A string representing the server's hostname (IP address or domain name). The client will attempt
     *                 to connect to the server at the specified address and port (4444).
     */
    public Client(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Description: checks if the provided hostname is valid by checking whether a connection can be established the hostname and port 4444
     * Pre-Condition: A valid hostname is passed to the method.
     * Post-Condition: The method attempts to connect to the server. If successful, it returns true, otherwise false.
     * @param hostname The server's hostname to connect to.
     * @return boolean Returns true if the connection is successful, otherwise false.
     */

    public static boolean checkConnection(String hostname) {
        try (Socket client = new Socket(hostname, 4444)) {
            return client.isConnected() && !client.isClosed();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Description: starts the client by connecting to server, setting up an input and output stream.
     *              Also, this method creates a new thread to handle incoming messages
     * Pre-Condition: A valid `hostname` is set when creating the `Client` object.
     * Post-Condition: The client is connected to the server, and threads are started for receiving and sending messages.
     * @return void
     */
    @Override
    public void run() {
        try {
            client = new Socket(hostname, 4444);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
            InputHandler inHandler = new InputHandler();
            Thread inputThread = new Thread(inHandler);
            inputThread.start();

            do {
                inMessage = input.readLine();
                if (inMessage != null) {
                    System.out.println("Received message: " + inMessage);
                    if (inMessage.contains("[System, " + username + "]" + username)) {
                        specialCommand = inMessage;
                    } else if (inMessage.contains("[System, " + username + "]INVALID")) {
                        username = null;
                    }
                }
            } while (inMessage != null);

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    /**
     * Description: Returns the username of the client.
     * Pre-Condition: The `username` field is set when the client logs in.
     * Post-Condition: Returns the client's username.
     * @return String The client's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Description: Sets the username of the client.
     * Pre-Condition: The method is called with a valid username.
     * Post-Condition: The client's `username` field is updated with the provided value.
     * @param username The new username for the client.
     * @return void
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Description: Returns the special command associated with the client.
     * Pre-Condition: The special command is set while processing incoming messages.
     * Post-Condition: Returns the special command string.
     * @return String The special command associated with the client.
     */
    public String getSpecialCommand() {
        return specialCommand;
    }

    /**
     * Description: Sets a special command for the client.
     * Pre-Condition: The method is called with a valid command string.
     * Post-Condition: The client's `specialCommand` field is updated with the provided command.
     * @param specialCommand The new special command for the client.
     * @return void
     */
    public void setSpecialCommand(String specialCommand) {
        this.specialCommand = specialCommand;
    }
    /**
     * Description: manages sending messages from client to server. Runs in a different thread to not block other
     *              functionality.
     * Pre-Condition: The `outMessage` field contains a message to be sent to the server.
     * Post-Condition: The message is sent to the server and the `outMessage` field is cleared.
     */
    class InputHandler implements Runnable {
        /**
         * Description: manages sending messages from client to server. Runs in a different thread to not block other
         *              functionality.
         * Pre-Condition: The `outMessage` field contains a message to be sent to the server.
         * Post-Condition: The message is sent to the server and the `outMessage` field is cleared.
         * @return void
         */
        @Override
        public void run() {
            while (running) {
                if (!outMessage.isEmpty()) {
                    System.out.println("Sending message: " + outMessage);
                    output.println(outMessage);
                    outMessage = "";  // Clear message after sending
                }
                try {
                    Thread.sleep(100);  // gives time for other threads to update `outMessage`
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // Setters and getters
    /**
     * Description: Sets the outgoing message for the client to send.
     * Pre-Condition: A message string is passed as input.
     * Post-Condition: The message is stored in the `outMessage` field to be sent later.
     * @param outMessage The message to be sent to the server.
     * @return void
     */
    public void setOutMessage(String outMessage) {
        this.outMessage = outMessage;
    }
    /**
     * Description: Returns the outgoing message stored for the client to send.
     * Pre-Condition: A message has been stored in the `outMessage` field.
     * Post-Condition: Returns the stored message.
     * @return String The outgoing message.
     */
    public String getOutMessage() {
        return outMessage;
    }
    /**
     * Description: Returns the incoming message received from the server.
     * Pre-Condition: The `inMessage` field is updated with the latest message from the server.
     * Post-Condition: Returns the most recent incoming message.
     * @return String The incoming message.
     */
    public String getInMessage() {
        return inMessage;
    }
    /**
     * Description: Sets the incoming message received from the server.
     * Pre-Condition: The `inMessage` field is updated with a new message from the server.
     * Post-Condition: The `inMessage` field is updated with the provided message.
     * @param inMessage The incoming message from the server.
     * @return void
     */
    public void setInMessage(String inMessage) {
        this.inMessage = inMessage;
    }
    /**
     * Description: Shuts down the client connection by closing all resources and stopping the thread.
     * Pre-Condition: The `Client` is connected to the server and the method is called to disconnect.
     * Post-Condition: The client is disconnected, and all resources (e.g., input, output, socket) are closed.
     * @return void
     */
    public void shutdown() {
        try {
            if (client != null && !client.isClosed()) {
                output.println("[System] " + username + " disconnected");
                client.close();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            running = false;
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
        // stop the current running thread
        Thread.currentThread().interrupt();
    }
    /**
     * Description: Returns a string representation of the Client object.
     * Pre-Condition: None.
     * Post-Condition: Returns a string that contains the class name and important properties of the Client object.
     * @return String The string representation of the Client object.
     */
    @Override
    public String toString() {
        return "Client:\n" + ", hostname='" + hostname + ", username='" + username + ", specialCommand='" + specialCommand;
    }

    /**
     * Description: Compares this Client object with another object to check for equality.
     * Pre-Condition: The object to compare must be a `Client` object.
     * Post-Condition: Returns true if both objects are equal, otherwise false.
     * @param obj The object to compare with this Client object.
     * @return boolean Returns true if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Objects are of different classes or the other is null
        }
        Client other = (Client) obj; // Cast the object to a Client
        return hostname.equals(other.hostname) && username.equals(other.username) && specialCommand.equals(other.specialCommand);
    }

}
