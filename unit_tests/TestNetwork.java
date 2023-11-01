package unit_tests;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.fail;
import org.jdom2.Document;
import org.jdom2.Element;
import network.Receiver;
import network.Sender;

public class TestNetwork {
    private clientThread sender;
    private serverThread receiver;

    @Before
    public void setup() {
        this.sender = new clientThread();
        this.receiver = new serverThread();
    }

    private Document blankDoc() {
        Element root = new Element("serialized");
        return new Document(root);
    }

    @Test
    public void testConection() {
        sender.toSend = blankDoc();
        Thread client = new Thread(sender);
        Thread server = new Thread(receiver);
        server.start();
        client.start();
        try {
            client.join();
            server.join();
        } catch(InterruptedException e) {
            fail();
        }
    }

    private class serverThread extends Thread {
        private Receiver receiver;

        public serverThread() {
            receiver = new Receiver(false);
        }

        @Override
        public void run() {
            receiver.start();
            receiver.deserialize();
        }
    }

    private class clientThread extends Thread {
        private Sender sender;
        public Document toSend;

        public clientThread() {
            sender = new Sender();
        }

        @Override
        public void run() {
            sender.sendDocument(toSend);
        }
    }
}
