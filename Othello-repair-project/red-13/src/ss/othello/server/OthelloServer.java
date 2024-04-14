package ss.othello.server;

import ss.othello.commonUtil.Mission;
import ss.othello.commonUtil.Protocol;
import ss.othello.commonUtil.Util;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class OthelloServer implements Runnable {
    /**
     * The server of running the Othello game
     */
    private ServerSocket serverSocket;

    private static HashMap<String, Socket> clientSocketMap = new HashMap<>();

    /**
     * A run method to accept connection from clients, create a corresponding clientHandler thread,
     * store the clientHandler into Vector list, and starts the clientHandler thread.
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(44444);
            while (!(serverSocket.isClosed())) {
                System.out.println("The server is listening at the Port 44444...");
                //a socket is created when it is connected with a client
                Socket socket = serverSocket.accept();
                System.out.println("The server is connected to the client");
                establishConnection(socket);
                String username = checkCredentials(socket);
                if(username != null){
                    ClientHandler clientHandler = new ClientHandler(socket);
                    clientHandler.setClientName(username);
                    clientSocketMap.put(username, socket);
                    ClientOnline.addClientHandler(username, clientHandler);
                    clientHandler.start();
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

    }
    public void establishConnection(Socket socket){
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Mission response = (Mission) ois.readObject();
            Mission helloRespond = new Mission();

            if(response.getProtocol().equals(Protocol.HELLO)){
                helloRespond.setProtocol(Protocol.HELLO);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(helloRespond);
            }else{
                helloRespond.setProtocol(Protocol.ERROR);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(helloRespond);
                //TODO: not sure if I should close the socket here
//                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public String checkCredentials(Socket socket) throws IOException {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Mission response = (Mission) ois.readObject();

            if (response.getProtocol().equals(Protocol.LOGIN)) {

                if (Util.playerExists(response.getPlayer().getPlayerName(), "red-13/file/nameAndPassword")) {
                    if (ClientOnline.getClientHandler(response.getPlayer().getPlayerName()) != null) {
                        response.setProtocol(Protocol.ALREADAYLOGGEDIN);
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(response);
                        return null;
                    }
                    if (Util.checkPassword(response.getPlayer().getPlayerName(), response.getPlayer().getPwd(), "red-13/file/nameAndPassword")) {
                        response.setProtocol(Protocol.LOGIN);
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(response);
                        return response.getPlayer().getPlayerName();
                    } else {
                        response.setProtocol(Protocol.WRONGPASSWORD);
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(response);
                        return null;
                    }
                } else {
                    Util.addUserAndPassword(response.getPlayer().getPlayerName(), response.getPlayer().getPwd(), "red-13/file/nameAndPassword");
                    response.setProtocol(Protocol.NEWPLAYER);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(response);
                    return response.getPlayer().getPlayerName();
                }
            }

        } catch (IOException e) {
            System.out.println("Client disconnected");
            socket.close();
            System.out.println(ClientOnline.returnOnlineUsers());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void addSocketToMap(String playerName, Socket socket){
        clientSocketMap.put(playerName, socket);
    }

    public static Socket getSocketFromMap(String playerName){
        return clientSocketMap.get(playerName);
    }

    public static void removeSocketFromMap(String playerName){
        clientSocketMap.remove(playerName);
    }




}

