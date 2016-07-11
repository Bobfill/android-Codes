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
			//定义当前线程所处理的Socket
	     private Socket socket = null;
	     //该线程所处理的Socket对应的输入流
	     private BufferedReader bufferedReader = null;
	     
	     public mServerThread(Socket socket) throws IOException {
	         this.socket = socket;
	         //获取该socket对应的输入流
	         bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	         dos = new DataOutputStream(socket.getOutputStream());
	     }
	     
	     
	     public void run() {
	    	 
	         try {
	        	System.out.println("成功创建客户服务线程 .----");
	        
	    		 String ss="[连接服务器成功]"+"\n";
	        
					dos.writeUTF(ss);
					dos.flush();
					System.out.println("成功发送消息："+ss);
	        	
	             String content = null;
	             //采用循环不断地从Socket中读取客户端发送过来的数据
	             content=bufferedReader.readLine();
	             System.out.println("buffer："+bufferedReader.readLine());
	             while ((content = bufferedReader.readLine()) != null) {
	                 //将读到的内容向每个Socket发送一次
	                 for(Socket socket : Client.socketList) {
	                     //获取该socket对应的输出流
	                     PrintStream printStream = new PrintStream(socket.getOutputStream());
	                     //向该输出流中写入要广播 的内容
	                     System.out.println("循环广播的内容："+content);
	                     printStream.println(packMessage(content));
	                    // printStream.println(content);
   	                 }
	             }
	         } catch(IOException e) {
	             e.printStackTrace();
	         }
	     }
	     
	    
	     private String packMessage(String content) {
	    	 //将消息包装后发出
	         String result = content;
	         SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");    //设置日期格式
	       
	           
	         return  df.format(new Date())+"  "+result+"";
	     }
	 
	 }
