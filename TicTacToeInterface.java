import java.rmi.RemoteException;
import java.io.IOException;

public interface TicTacToeInterface extends java.rmi.Remote {
    public void joinGame(TicTacToeInterface client) throws RemoteException ;
    public int sendMessage(String message) throws RemoteException, IOException ;
}
