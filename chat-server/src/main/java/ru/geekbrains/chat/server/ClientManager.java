package ru.geekbrains.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.AbstractList;
import java.util.ArrayList;

public class ClientManager implements Runnable {
    private final Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String name;
    public static AbstractList<ClientManager> clients = new ArrayList<>();

    public ClientManager(Socket socket) {
        this.socket = socket;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            name = bufferedReader.readLine();
            clients.add(this);
            System.out.println(name+" подключение к чату.");
            broadcastMessage("Server: "+name+" присоединился к чату.");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient == null){
                       // для  macOS
                       closeEverything(socket, bufferedReader, bufferedWriter);
                       break;
                   }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
            //<editor-fold desc="Description">
//            try {
//                messageFromClient = bufferedReader.readLine();
//                broadcastMessage(messageFromClient);
//            } catch (IOException e) {
//                closeEverything(socket, bufferedReader, bufferedWriter);
//                break;
//            }
            //</editor-fold>
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
            removeClient();
        try {
            if (bufferedReader != null) {bufferedReader.close();}
            if (bufferedWriter != null) {bufferedWriter.close();}
            if (socket != null) {socket.close();}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void broadcastMessage(String message) {
        for (ClientManager client : clients) {
            if (!client.name.equals(name)) try {
                {
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    private void broadcastMessage() {
        String message = "Client leaved the chat.";
        for (ClientManager client : clients) {
            if (!client.name.equals(name)) try {
                {
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }


    private void removeClient(){
        clients.remove(this);
        System.out.println(name + " покинул чат.");
        broadcastMessage("Server: " + name + " покинул чат.");
    }
}
