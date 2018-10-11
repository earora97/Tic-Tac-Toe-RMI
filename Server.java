import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.io.* ;
import java.net.* ;
import java.util.* ;
import java.awt.* ;

public class Server extends UnicastRemoteObject implements TicTacToeInterface {
    public static final long serialVersionUID = 1L ;
    public Vector<TicTacToeInterface> clientList;
    public Vector<TicTacToeInterface> waitList;
    private Vector<Game> Games;
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        Naming.rebind("RMIServer" ,new Server());
    }
    public Server() throws RemoteException
    {
        clientList = new Vector<TicTacToeInterface>();
        waitList = new Vector<TicTacToeInterface>();
        Games = new Vector<Game>();
    }

    public void joinGame(TicTacToeInterface client) {
        this.clientList.add(client);
        this.checkGamePlay(client);
    }

    public void checkGamePlay(TicTacToeInterface client) {
        if(this.waitList.size()>0)
        {
            TicTacToeInterface pl=this.waitList.elementAt(0);
            this.waitList.remove(this.waitList.elementAt(0));
            this.startGame(pl,client);
        }
        else
        {
            this.waitList.add(client);
            try {
                client.sendMessage("Wait");
            } catch (Exception e){}
        }
    }

    public void startGame(TicTacToeInterface player1, TicTacToeInterface player2)
    {
        try {
            Game game = new Game(player1,player2);
            this.Games.add(game);
            player1.sendMessage("Game started\nYou are player 1\nYour marker is : x"); player2.sendMessage("Game started\nYou are player 2\nYour marker is : o");
            this.play(game);
        } catch(Exception e){}
    }

    public void play(Game game) {
        try {
            boolean turn1=true;
            while(!(game.checkGameover()))
            {
                if(turn1)game.turnOf(1);
                else game.turnOf(2);
                turn1=!turn1;
            }
            if(game.winner==0)
            {
                game.player1.sendMessage("Draw"); game.player2.sendMessage("Draw");
            }
            else if(game.winner==1)
            {
                game.player1.sendMessage("You Win"); game.player2.sendMessage("You Lose");
            }
            else
            {
                game.player1.sendMessage("You Lose"); game.player2.sendMessage("You Win");
            }

            int w1=game.player1.sendMessage("Enter 1 if you want to play again\nEnter 0 if you want to quit"),w2= game.player2.sendMessage("Enter 1 if you want to play again\nEnter 0 if you want to quit");
            if(w1==1) this.checkGamePlay(game.player1);
            else { game.player1.sendMessage("You Quit the game"); this.clientList.remove(game.player1);}
            if(w2==1)  this.checkGamePlay(game.player2);
            else {game.player2.sendMessage("You Quit the game");this.clientList.remove(game.player2);}
            return ;
        }catch(Exception e){}
    }

    public int sendMessage (String message) throws RemoteException, IOException {return 0;}

}

class Game {
    Map<Integer,String> board = new HashMap<Integer,String>();
    TicTacToeInterface player1, player2;
    int winner;
    boolean gamerunning=true;
    int ans1=-1,ans2=-1;

    Game(TicTacToeInterface player1_, TicTacToeInterface player2_) {
        player1=player1_; player2=player2_;
        for(Integer i=1;i<=9;i++) board.put(i,Integer.toString(i));
    }

    public void turnOf(int pl)
    {
        try {
                TicTacToeInterface playing,waiting;
                if(pl==1)
                {
                    playing = this.player1; waiting = this.player2;
                }
                else
                {
                    playing = this.player2; waiting = this.player1;
                }
                waiting.sendMessage("\nWaiting for player "+pl+"'s move\n");
                int moveCell=playing.sendMessage(this.boardAsString());
                if(pl==1) this.move(moveCell,"x");
                else this.move(moveCell,"o");
        }catch(Exception e){}
    }

    public void move(int cell, String marker)
    {
        this.board.put(cell,marker);
        //check if game is over
        if(this.board.get(1)==this.board.get(2) && this.board.get(1)==this.board.get(3))
            this.gameOver(1,true);
        else if(this.board.get(4)==this.board.get(5) && this.board.get(4)==this.board.get(6))
            this.gameOver(4,true);
        else if(this.board.get(7)==this.board.get(8) && this.board.get(7)==this.board.get(9))
            this.gameOver(7,true);
        else if(this.board.get(1)==this.board.get(4) && this.board.get(1)==this.board.get(7))
            this.gameOver(1,true);
        else if(this.board.get(2)==this.board.get(5) && this.board.get(2)==this.board.get(8))
            this.gameOver(2,true);
        else if(this.board.get(3)==this.board.get(6) && this.board.get(3)==this.board.get(9))
            this.gameOver(3,true);
        else if(this.board.get(1)==this.board.get(5) && this.board.get(1)==this.board.get(9))
            this.gameOver(1,true);
        else if(this.board.get(3)==this.board.get(5) && this.board.get(3)==this.board.get(7))
            this.gameOver(3,true);
        else
        {
            for(int i=1;i<=9;i++)
            {
                if(this.board.get(i).equals(Integer.toString(i))) return;
            }
            this.gameOver(0,false);
        }
    }

    public void gameOver(int cell, boolean isWin) {
        if(isWin)
        {
            if(this.board.get(cell).equals("x"))
                this.winner=1;
            else
                this.winner=2;
        }
        else this.winner=0;
        this.gamerunning=false;
    }

    public boolean checkGameover() {
        return !gamerunning;
    }

    public String boardAsString() {
        String boardS="";
        for(int i=1;i<=9;i++)
        {
            if(i%3==1) boardS+="\n| "+this.board.get(i)+" |";
            else if(i%3==2) boardS+=" "+this.board.get(i)+" |";
            else boardS+=" "+this.board.get(i)+" |";
        }
        return boardS;
    }
}
