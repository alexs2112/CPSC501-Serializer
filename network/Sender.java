package network;

import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Sender {
    private static final String host = "localhost";
    private static final int port = 6666;
    public ArrayList<String> messages;

    public Sender() {
        messages = new ArrayList<String>();
    }

    public void sendDocument(Document doc) {
        Socket socket = null;
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            System.out.println(e);
            messages.add("Error: Could not open socket.");
            close(socket, null, null);
            return;
        }

        BufferedOutputStream bufferedStream = null;
        try {
            bufferedStream = new BufferedOutputStream(socket.getOutputStream());
        } catch(IOException e) {
            System.out.println(e);
            messages.add("Error: Failed to construct output stream.");
            close(socket, bufferedStream, null);
            return;
        }

        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        try {
            out.output(doc, byteOutputStream);
            byte[] byteList = byteOutputStream.toByteArray();
            bufferedStream.write(byteList);
            bufferedStream.flush();
            messages.add("Document successfully sent.");
        } catch(IOException e) {
            System.out.println(e);
            messages.add("Error: Failed to construct document.");
        }

        close(socket, bufferedStream, byteOutputStream);
    }

    private void close(Socket socket, BufferedOutputStream bufferedStream, ByteArrayOutputStream byteOutputStream) {
        try {
            if (bufferedStream != null)
                bufferedStream.close();

            if (byteOutputStream != null)
                byteOutputStream.close();

            if (socket != null)
                socket.close();
        } catch(IOException e) {
            System.out.println(e);
            messages.add("Warning: Failed to close socket.");
        }
    }
}
