import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Server.java
 * Description: This class implements the server-side logic of the chat application. It listens for client connections,
 *              manages individual client threads, handles user authentication, broadcasts messages, and shuts down the server.
 * Name: Johan Antony
 * Date Created: January 14, 2025
 * Date last modified: January 14, 2025
 */
class Server implements Runnable {//runnable has to be implement so that the class can be passed as a thread and ran concurrently
    private int portNumber = 4444;// default port is 4444 can be set otherwise I just chose 4444
    private ArrayList<User> users;
    private ExecutorService threadPool;
    private boolean running = true;
    private HashSet<String> Usernames;
    private ServerSocket server;

    /**
     * Description: This constructor initializes the socket arraylist and the list of usernames for the server
     * Pre-Condition: None.
     * Post-Condition: The `Server` object is initialized with empty user lists and a ready state for connections.
     * @return void
     */
    public Server() {
        users = new ArrayList<>();
        Usernames = new HashSet<>();
    }

    /**
     * Description: This method listens for incoming client connections, accepts them, and assigns
     *              a new `User` object to manage the communication. It then adds the user to the
     *              list of connected users and assigns them to a thread for further handling. It will verify usernames and save them as well.
     * Pre-Condition: The server must be initialized and ready to accept connections.
     * Post-Condition: A client connection is accepted, and the client is assigned a thread for communication.
     * @return void
     */
    @Override
    public void run() {//this method needs to be overwritten to implement runnable
        try{
            //make system listen on a specific port
            System.out.println("Waiting for connection...");
            server = new ServerSocket(portNumber);
            //initialize the thread pool
            threadPool = Executors.newCachedThreadPool();
            //create the list for usernames
            System.out.println("Usernames list initialized");

            //accept the socket connection to the server
            while (running) {
                Socket client = server.accept();
                System.out.println("Client connected");
                //add the client to list of users
                User currentUser = new User(client);
                //add the User to array list for broadcast method
                this.users.add(currentUser);
                //for each iteration add to thread pool
                threadPool.execute(currentUser);
            }

        }catch (IOException e) {
            shutdown();
        }
    }
    /**
     * Description: This method shuts down the server by stopping all resources connected with the server such as thread pool and server socket.
     * Pre-Condition: The server is running.
     * Post-Condition: The server is stopped, and all resources are cleaned up, including closing client connections.
     * @return void
     */
    public void shutdown(){
        running = false;
        threadPool.shutdown();
        try {
            if ( server!= null && !server.isClosed()) {
                server.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing server");
        }
    }
    /**
     * Description: This method sends a given message to each output stream.
     * Pre-Condition: The server must have active users connected.
     * Post-Condition: The message is sent to all connected users.
     * @param message The message to be broadcast to all connected users.
     * @return void
     */
    public void broadcastMessage(String message) {
        for (User user : this.users) {
            if (user != null) {
                user.sendMessage(message);
            }
        }
    }


    /**
     * User.java (Inner Class)
     * Description: This inner class handles individual client connections. It manages reading and writing to the client's
     *              input/output streams, verifies usernames, broadcasts messages, and handles the client's disconnection.
     * Name: Johan Antony
     * Date Created: January 14, 2025
     * Date last modified: January 14, 2025
     */
    class User implements Runnable {
        private Socket client;//this is the individual socket connection
        private BufferedReader input;//managing the output stream to the individual connection
        private PrintWriter output;//managing the input stream to the individual connection
        private String username;

        /**
         * Description: This method initializes user to handle connections with the server
         * Pre-Condition: The `Socket` object representing the client's connection must be provided.
         * Post-Condition: A new `User` object is created, and the user is ready to handle communication with the server.
         * @param client The `Socket` representing the client connection.
         * @return void
         */
        public User (Socket client) {
            this.client = client;
        }

        /**
         * Description: This method handles communication with a single client such as verifying usernames, receiving messages and sending messages to this user.
         * Pre-Condition: A client is connected and authenticated.
         * Post-Condition: The client is authenticated, and communication proceeds with the user until disconnection.
         * @return void
         */
        @Override
        public void run() {
            //main try block just run all code in the try block makes it efficient
            try {


                this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                this.output = new PrintWriter(client.getOutputStream(), true);
                //capture the username from socket before starting
                while (true) {
                    this.username = input.readLine();
                    if (this.username != null) {
                        this.username = this.username.substring(username.indexOf("]") + 1);
                        System.out.println("Input: " + this.username);
                        if (verifyUsername(this.username)) {
                            output.println("[System, " + this.username + "]" + this.username);
                            System.out.println("[System, " + this.username + "]" + this.username);
                            break;
                        } else {
                            output.println("[Username]INVALID");
                        }
                    }
                }
                broadcastMessage("[System]" + this.username + " logged in");
                String message = null;
                while ((message = input.readLine()) != null) {
                    if (message.equals("[System]QUIT")) {
                        shutdown();
                    }else {
                        broadcastMessage(message);
                    }
                }



            } catch (IOException e) {
                shutdown();
            }

        }

        //getter and setters
        /**
         * Description: Gets the `Socket` representing the user's client connection.
         * Pre-Condition: None.
         * Post-Condition: Returns the `Socket` representing the client's connection.
         * @return Socket The `Socket` representing the client's connection.
         */
        public Socket getClient() {
            return client;
        }
        /**
         * Description: Sets the `Socket` representing the client's connection.
         * Pre-Condition: A valid `Socket` object representing the client must be provided.
         * Post-Condition: The user's client connection is updated.
         * @param client The `Socket` representing the client's connection.
         * @return void
         */
        public void setClient(Socket client) {
            this.client = client;
        }
        /**
         * Description: Gets the `BufferedReader` used for reading input from the client.
         * Pre-Condition: None.
         * Post-Condition: Returns the `BufferedReader` used for reading input from the client.
         * @return BufferedReader The `BufferedReader` used for reading input from the client.
         */
        public BufferedReader getInput() {
            return input;
        }
        /**
         * Description: Sets the `BufferedReader` used for reading input from the client.
         * Pre-Condition: A valid `BufferedReader` must be provided.
         * Post-Condition: The user's input stream is updated to the new `BufferedReader`.
         * @param input The `BufferedReader` to be used for reading input from the client.
         * @return void
         */
        public void setInput(BufferedReader input) {
            this.input = input;
        }
        /**
         * Description: Gets the `PrintWriter` used for sending messages to the client.
         * Pre-Condition: None.
         * Post-Condition: Returns the `PrintWriter` used for sending messages to the client.
         * @return PrintWriter The `PrintWriter` used for sending messages to the client.
         */
        public PrintWriter getOutput() {
            return output;
        }
        /**
         * Description: Sets the `PrintWriter` used for sending messages to the client.
         * Pre-Condition: A valid `PrintWriter` must be provided.
         * Post-Condition: The user's output stream is updated to the new `PrintWriter`.
         * @param output The `PrintWriter` to be used for sending messages to the client.
         * @return void
         */
        public void setOutput(PrintWriter output) {
            this.output = output;
        }
        /**
         * Description: Gets the username of the client.
         * Pre-Condition: None.
         * Post-Condition: Returns the username of the user.
         * @return String The username of the client.
         */
        public String getUsername() {
            return username;
        }
        /**
         * Description: Sets the username for the client.
         * Pre-Condition: A valid username must be provided.
         * Post-Condition: The user's username is updated to the new value.
         * @param username The username to be set for the user.
         * @return void
         */
        public void setUsername(String username) {
            this.username = username;
        }
        /**
         * Description: This method verifies if the username is valid and unique by checking against
         *              the set of already existing usernames.
         * Pre-Condition: The username must be provided.
         * Post-Condition: The username is validated and either accepted or rejected based on uniqueness.
         * @param username The username to be verified.
         * @return boolean Returns `true` if the username is valid and unique, otherwise `false`.
         */
        private boolean verifyUsername(String username) {
            if (Usernames.contains(username)) {
                return false;
            }else {
                Usernames.add(username);
                return true;
            }
        }

        /**
         * Description: This method sends a message to the client through the client's output stream.
         * Pre-Condition: The user must be connected and authenticated.
         * Post-Condition: The message is sent to the connected client.
         * @param message The message to be sent to the client.
         * @return void
         */
        public void sendMessage(String message) {
            output.println(message);
        }
        /**
         * Description: This method shuts down the individual client shutting down the input output streams and removing the username from the server.
         * Pre-Condition: The user must be connected and authenticated.
         * Post-Condition: The user is disconnected, and resources are cleaned up.
         * @return void
         */
        public void shutdown() {
            try {
                input.close();
                output.close();
                if (!this.client.isClosed()) {
                    this.client.close();
                }
                users.remove(this);
            } catch (IOException e) {
                System.out.println("Error closing thread");
            }
        }


    }


    //setters and getters
    /**
     * Description: Gets the current port number the server is listening on.
     * Pre-Condition: None.
     * Post-Condition: Returns the current port number for the server.
     * @return int The current port number the server is listening on.
     */
    public int getPortNumber() {
        return portNumber;
    }
    /**
     * Description: Sets the port number for the server to listen on.
     * Pre-Condition: The port number must be a valid integer.
     * Post-Condition: The server's port number is updated to the new value.
     * @param portNumber The port number to be set for the server.
     * @return void
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
    /**
     * Description: Gets the list of users currently connected to the server.
     * Pre-Condition: None.
     * Post-Condition: Returns the list of connected users.
     * @return ArrayList<User> The list of users connected to the server.
     */
    public ArrayList<User> getUsers() {
        return users;
    }
    /**
     * Description: Sets the list of users connected to the server.
     * Pre-Condition: A valid list of `User` objects must be provided.
     * Post-Condition: The server's list of users is updated to the new list.
     * @param users The list of connected users.
     * @return void
     */
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
    /**
     * Description: Returns a string representation of the server, including the port number,
     *              the number of connected users, and the list of usernames. This method uses the toString method of the hashset Usernames.
     * Pre-Condition: The server should have been initialized with a valid port number
     *                and a list of connected users.
     * Post-Condition: A string representation of the server's details is returned.
     * @return String A string representing the server's details.
     */
    @Override
    public String toString() {
        return "Server Details:\n" + "Port Number: " + portNumber + "Number of Connected Users: " + users.size() + ", Usernames: " + Usernames.toString();
    }
    /**
     * Description: Compares this Server object to another object to determine if they are equal.
     *              Equality is based on the port number and the set of connected users.
     * Pre-Condition: The object being compared must be of the `Server` type.
     * Post-Condition: Returns `true` if both Server objects have the same port number and the same set of users, otherwise `false`.
     * @param obj The object to compare this `Server` to.
     * @return boolean Returns `true` if the `Server` objects are equal, otherwise `false`.
     */
    @Override
    public boolean equals(Object obj) {
        //check for whether they are of the same object type
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Server otherServer = (Server) obj; // Cast the object to a `Server`.
        return portNumber == otherServer.portNumber && users.equals(otherServer.users); // Compare the port number and the list of users.
    }


}