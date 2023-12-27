package ru.geekbrains.chat.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println(" - Представтесь, пожалуйста! ");
            String name = scanner.nextLine();

            Socket socket = new Socket("localhost", 1400);
            Client client = new Client(socket, name);

            InetAddress inetAddress = socket.getInetAddress();
            System.out.println("Inetadress: "+ inetAddress);

            String remoteIp = inetAddress.getHostAddress();
            System.out.println("RomoteIp: "+ remoteIp);
            System.out.println("LocalPort: "+ socket.getLocalPort());

            client.listenForMessage();
            client.sendMessage();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
