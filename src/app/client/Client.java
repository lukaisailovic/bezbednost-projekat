package app.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final int SERVER_PORT = 2021;
    private static final String SERVER_HOST = "localhost";


    public Client() throws Exception {
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);


        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader tin = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        System.out.println("Connected to server on port "+SERVER_PORT);

        String msg = in.readLine();
        System.out.println(msg);

        while (true) {
            String tastatura = tin.readLine();
            out.println(tastatura);

            if (tastatura.equals("exit"))
                break;

        }

        socket.close();
        System.out.println("Socket je zatvoren");

    }

    public static void main(String[] args) {
        try {
            new Client();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
