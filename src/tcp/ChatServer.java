package tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ChatServer {

	ArrayList clientOutputStreams;

		public class ClientHandler implements Runnable {
			BufferedReader reader;
			Socket sock;
	
			public ClientHandler(Socket clientSocket) {
				try {
					sock = clientSocket;
					InputStreamReader isReader = new InputStreamReader(
					sock.getInputStream());
					reader = new BufferedReader(isReader);
	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	
			@Override
			public void run() {
				String msg;
	
				try {
					while ((msg = reader.readLine()) != null) {
						System.out.println("read " + msg);
						tellEveryone(msg);
	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	
			}
		}

	public static void main(String[] args) {
		new ChatServer().go();

	}
	public void go(){
		clientOutputStreams = new ArrayList();
		try{
			ServerSocket serve = new ServerSocket(4500);
			while(true){
				Socket clientSocket = serve.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a connection");
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void tellEveryone (String msg){
	   java.util.Iterator it = clientOutputStreams.iterator();
	   
		while(it.hasNext()){
			try{
				
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(msg);
				writer.flush();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/*public void tellEveryone2 (String msg){
		int it = clientOutputStreams.size();
		while (next>0 ){
			try{
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(msg);
				writer.flush();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/

}
