package tcp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class SimpleChatClient {

	JTextArea incoming;
	JTextField outgoing;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;

	public static void main(String[] args) {
		SimpleChatClient client = new SimpleChatClient();
		client.go();
	}

	public void go() {
		JFrame frame = new JFrame("CHAT");
		JPanel panel = new JPanel();
		incoming = new JTextArea(20, 30);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane scroll = new JScrollPane(incoming);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new sendButtonListener());
		panel.add(scroll);
		panel.add(outgoing);
		panel.add(sendButton);
		setUpNetwork();

		Thread readerThread = new Thread(new incomingReader());
		readerThread.start();

		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setSize(400, 500);
		frame.setVisible(true);
	}

	private void setUpNetwork() {
		try {
			sock = new Socket("127.0.0.1", 4500);
			InputStreamReader streamreader = new InputStreamReader(
					sock.getInputStream());
			reader = new BufferedReader(streamreader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("network established");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class sendButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				writer.println(outgoing.getText());
				writer.flush();
			} catch (Exception ez) {
				ez.printStackTrace();
			}
			outgoing.setText("");
			outgoing.requestFocus();

		}

	}

	public class incomingReader implements Runnable {

		@Override
		public void run() {
			String msg;
			try {

				while ((msg = reader.readLine()) != null) {
					System.out.println("client" + msg);
					incoming.append(msg + "\n");

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

	}

}
