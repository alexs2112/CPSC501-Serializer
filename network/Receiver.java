package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;
import deserializer.Deserializer;

public class Receiver {
    private static final int port = 6666;
    private ServerSocket server;
    private Document document;
    public boolean print;
    public ArrayList<String> messages;

    public Receiver() {
        messages = new ArrayList<String>();
    }

    public Receiver(boolean printOutput) {
        messages = new ArrayList<String>();
        print = printOutput;
    }

    public void start() {
        // Start the socket
        try {
            server = new ServerSocket(port);
        } catch(IOException e) {
            System.out.println(e);
            messages.add("Error: Receiver failed to start.");
            return;
        }

        messages.add("Receiver started on port " + Integer.toString(port) + ". Waiting for connection.");
        Socket sender;
        try {
            sender = server.accept();
        } catch(IOException e) {
            System.out.println(e);
            messages.add("Error: Receiver failed to connect to sender.");
            return;
        }

        // Receive the serialized document
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(sender.getInputStream()));
            SAXBuilder saxBuilder = new SAXBuilder();
            document = saxBuilder.build(in);
            messages.add("Document received!");
        } catch(IOException e) {
            System.out.println(e);
            messages.add("Error: Unable to receive document.");
            return;
        } catch(JDOMException e) {
            System.out.println(e);
            messages.add("Error: Failed to construct document.");
            return;
        }

        // Close the sockets
        try {
            server.close();
            sender.close();
            in.close();
        } catch(IOException e) {
            System.out.println(e);
            messages.add("Warning: Failed to close sockets.");
        }

        if (print) {
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            String xmlString = xmlOutputter.outputString(document);
            System.out.println(xmlString);
        }
    }

    public Object deserialize() {
        Object o = new Deserializer().deserialize(document);
        return o;
    }
}
