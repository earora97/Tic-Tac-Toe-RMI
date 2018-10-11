import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.io.*;

public class Client extends UnicastRemoteObject implements TicTacToeInterface{
    private static final long serialVersionUID = 1L;
    public TicTacToeInterface server;
    private static BufferedReader input_ ;
    public static boolean canplaymore=false;

    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, IOException {
        input_ = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("The client is now running");
        System.out.println("Enter 1 if you want to play");
        String wish = input_.readLine();
        new Client();
    }

    protected Client() throws MalformedURLException, RemoteException, NotBoundException, IOException  {
        System.out.println("Connecting to Server");
        this.server = (TicTacToeInterface) Naming.lookup("rmi://localhost/RMIServer");
        System.out.println("Conected to Server");
        input_ = new BufferedReader(new InputStreamReader(System.in));
        this.server.joinGame(this);
    }

    public int sendMessage(String message) throws RemoteException, IOException {
        System.out.println(message);
        if(message.contains("|"))
        {
            try {
                System.out.println("Your Turn");
                String s=input_.readLine();
                int moveCell = Integer.parseInt(s);
                return moveCell;
            }catch(Exception e){}
        }
        else if(message.contains("again"))
        {
            String s = input_.readLine();
            return Integer.parseInt(s);
        }
        else if(message.contains("Quit"))
        {
            System.exit(0);
        }
        return 0;
    }

    public void joinGame(TicTacToeInterface client) throws RemoteException {}
}
