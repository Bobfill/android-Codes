package Socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mServerThread implements Runnable {
	private DataOutputStream dos;
			//���嵱ǰ�߳��������Socket
	     private Socket socket = null;
	     //���߳��������Socket��Ӧ��������
	     private BufferedReader bufferedReader = null;
	     
	     public mServerThread(Socket socket) throws IOException {
	         this.socket = socket;
	         //��ȡ��socket��Ӧ��������
	         bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	         dos = new DataOutputStream(socket.getOutputStream());
	     }
	     
	     
	     public void run() {
	    	 
	         try {
	        	System.out.println("�ɹ������ͻ������߳� .----");
	        
	    		 String ss="[���ӷ������ɹ�]"+"\n";
	        
					dos.writeUTF(ss);
					dos.flush();
					System.out.println("�ɹ�������Ϣ��"+ss);
	        	
	             String content = null;
	             //����ѭ�����ϵش�Socket�ж�ȡ�ͻ��˷��͹���������
	             content=bufferedReader.readLine();
	             System.out.println("buffer��"+bufferedReader.readLine());
	             while ((content = bufferedReader.readLine()) != null) {
	                 //��������������ÿ��Socket����һ��
	                 for(Socket socket : Client.socketList) {
	                     //��ȡ��socket��Ӧ�������
	                     PrintStream printStream = new PrintStream(socket.getOutputStream());
	                     //����������д��Ҫ�㲥 ������
	                     System.out.println("ѭ���㲥�����ݣ�"+content);
	                     printStream.println(packMessage(content));
	                    // printStream.println(content);
   	                 }
	             }
	         } catch(IOException e) {
	             e.printStackTrace();
	         }
	     }
	     
	    
	     private String packMessage(String content) {
	    	 //����Ϣ��װ�󷢳�
	         String result = content;
	         SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");    //�������ڸ�ʽ
	       
	           
	         return  df.format(new Date())+"  "+result+"";
	     }
	 
	 }
