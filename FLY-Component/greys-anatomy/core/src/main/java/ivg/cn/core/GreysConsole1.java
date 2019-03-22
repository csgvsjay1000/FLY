package ivg.cn.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;


import jline.console.ConsoleReader;
import jline.console.KeyMap;

public class GreysConsole1 {

	ConsoleReader console;
	Writer out;
	boolean isRunning = true;
	Socket socket;
	BufferedReader socketReader;
	BufferedWriter socketWriter;
	
	public GreysConsole1() throws UnknownHostException, IOException {
		try {
			console = new ConsoleReader(System.in, System.out);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		socket = new Socket("127.0.0.1", 6530);
		socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		
		console.getKeys().bind(""+KeyMap.CTRL_D, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GreysConsole1.this.shutdown();
				
			}
		});
		this.out = console.getOutput();
		String line;
		while (isRunning) {
			try {
				line = console.readLine();
				socketWriter.write(line+"\n");
				socketWriter.flush();
				System.out.println("input "+line);
//				Thread.sleep(1000);
//				out.write("output "+line+" hello");
//				out.flush();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	private void shutdown() {
		isRunning = false;
		console.shutdown();
	}
	
	public static void main(String[] args) {
		try {
			new GreysConsole1();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
