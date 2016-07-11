package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
 

public class Client {

	private ServerSocket ss;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	public static ArrayList<Socket> socketList = new ArrayList<Socket>();
 
	 
	public Client() {
		new ServerThread2().start();
	}

	public static void main(String[] args) {
		new Client();
	}

	public class ServerThread2 extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				ss = new ServerSocket(50000);//实例化服务器端套接字对象
				System.out.println("服务器已经启动");
				while (true) {
					socket = ss.accept();//阻塞端口，等待客户机连接
					socketList.add(socket);//将每个客户机保存到list集合中
					System.out.println("有客户端连接服务器！");
					new Thread(new mServerThread(socket)).start();
				}
			} catch (Exception e) {
				System.out.println("读写错误" + e.toString());
			} finally {
				try {
					System.out.println("socket服务器已经关闭");
					dis.close();
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	 
}
