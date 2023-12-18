package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer  {
    private ServerSocket serverSocket;
    static int clientCount = 0;

    private static List<ClientHandler> clients = new ArrayList<>();
    public GameServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("서버가 " + port + " 포트에서 대기 중입니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                // 클라이언트를 위한 새로운 스레드 시작
                ClientHandler clientHandler = new ClientHandler(clientSocket,clientCount+"번");
                Thread ti=new Thread(clientHandler);

                clients.add(clientHandler);
                ti.start();
                clientCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        int port = 12345; // 포트 번호를 필요에 따라 변경하세요.

        GameServer server = new GameServer(port);
        server.startServer();
    }

    private class ClientHandler implements  Runnable {// 서버 스레드 클래스
        private Socket clientSocket;
        private String name;
        BufferedReader is ;
        BufferedWriter os ;


        public ClientHandler(Socket socket,String name) {
            this.clientSocket = socket;
            try {

                is= new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // 소켓 입력 스트림ㅑㄴ
                os = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); // 소켓 출력 스트림
                String username = is.readLine();
                System.out.println(username+" "+"서버연결성공");
                String arr[ ]=username.split(" ");

                this.name=arr[0];

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                while (true) {
                    String msg = is.readLine();
                    System.out.println("server received : "+msg);
                    for (ClientHandler t : GameServer.clients) {//스레드 클래스 반환 리스트 개수만큼
                        System.out.println(name+" 클라이언트 에게 전송 ");

                        //담아놓은 arraylist 에서 ServerThread객체들 하나씩 꺼내서
                        //즉 서버가 연결되어있는 모든 클라이언트 들에게 메세지 전송한다
                        //is 로 받은 어떤 클라이언트가 전송했던 서버에서 받아서 다시 모든 클라이언트에게 재전송
                        t.os.write(name+" "+msg+"\n");//연결되어있는 클라이언트 에게 write
                        t.os.flush();
                    }

                    // 받은 메시지를 모든 클라이언트에게 브로드캐스트합니다.
                    }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
