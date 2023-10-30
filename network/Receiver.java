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

public class Receiver {
    private static final int port = 6666;
    private ServerSocket server;
    public ArrayList<String> messages;

    public Receiver() {
        messages = new ArrayList<String>();
    }

    public static void main(String[] args) {
        new Receiver().start();
    }

    public void start() {
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

        BufferedReader in;
        Document doc;
        try {
            in = new BufferedReader(new InputStreamReader(sender.getInputStream()));
            SAXBuilder saxBuilder = new SAXBuilder();
            doc = saxBuilder.build(in);
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

        try {
            server.close();
            sender.close();
            in.close();
        } catch(IOException e) {
            System.out.println(e);
            messages.add("Warning: Failed to close sockets.");
        }

        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = xmlOutputter.outputString(doc);
        System.out.println(xmlString);

        // Deserialize here
    }
}
