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
				ss = new ServerSocket(50000);//ʵ�������������׽��ֶ���
				System.out.println("�������Ѿ�����");
				while (true) {
					socket = ss.accept();//�����˿ڣ��ȴ��ͻ�������
					socketList.add(socket);//��ÿ���ͻ������浽list������
					System.out.println("�пͻ������ӷ�������");
					new Thread(new mServerThread(socket)).start();
				}
			} catch (Exception e) {
				System.out.println("��д����" + e.toString());
			} finally {
				try {
					System.out.println("socket�������Ѿ��ر�");
					dis.close();
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	 
}
