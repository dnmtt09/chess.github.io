public class Game {
    private int humanPoints = 0;
    private final pcPlayer pcPlayer;
    public char[] chessboard = new char[100];

    Game() {
        for (int i = 0; i < 100; i++)
            this.chessboard[i] = ' ';// riempie la chessboard di spazi
        for (int i = 11; i <= 17; i = i + 2)
            this.chessboard[i] = 'C'; // dispone le pedine del PC
        for (int i = 22; i <= 28; i = i + 2)
            this.chessboard[i] = 'C';
        for (int i = 71; i <= 77; i = i + 2)
            this.chessboard[i] = 'H';// dispone le pedine umane
        for (int i = 82; i <= 88; i = i + 2)
            this.chessboard[i] = 'H';
        pcPlayer = new pcPlayer(chessboard);
    }

    public int play(int x, int y, int flag) {
        int turnOfPC;
        boolean turnOfHuman;
        if (flag == 0) {
            turnOfHuman = humanPlay(x, y);
            if (turnOfHuman)
                return 0;
            return 1;
        } else {
            turnOfPC = pcPlayer.Mossa();
            return turnOfPC;
        }

    }

    public int pcPoints() {
        return pcPlayer.getPoints();
    }

    public int humanPoints() {
        return humanPoints;
    }

    private boolean isValidMove(int x) {
        boolean T = true;
        if (x < 11 || x > 88 || x % 10 == 0)
            T = false;
        for (int i = 19; i < 100; i = i + 10)
            if (x == i)
                T = false;
        for (int i = 12; i < 19; i = i + 2)
            if (x == i)
                T = false;
        for (int i = 21; i < 28; i = i + 2)
            if (x == i)
                T = false;
        for (int i = 32; i < 39; i = i + 2)
            if (x == i)
                T = false;
        for (int i = 41; i < 48; i = i + 2)
            if (x == i)
                T = false;
        for (int i = 52; i < 59; i = i + 2)
            if (x == i)
                T = false;
        for (int i = 61; i < 68; i = i + 2)
            if (x == i)
                T = false;
        for (int i = 72; i < 79; i = i + 2)
            if (x == i)
                T = false;
        for (int i = 81; i < 88; i = i + 2)
            if (x == i)
                T = false;
        return T;
    }

    private boolean Verify(int x) {
        boolean casellaUmanoCorretta = isValidMove(x);// verifica se il pezzo selezionato è quello dell'umano
        // all'interno della scacchiera
        if (this.chessboard[x] == 'H' && casellaUmanoCorretta)
            return true;
        return false;
    }

    private boolean IsFree(int x) {
        boolean casellaDisponibile = isValidMove(x);
        if (this.chessboard[x] == ' ' && casellaDisponibile)
            return true;
        return false;
    }

    public boolean humanPlay(int x, int y) {
        boolean humanPiece;
        boolean slotArrived = false;
        humanPiece = Verify(x);// controlla che in x ci sia una pedina dell'uomo
        if (humanPiece) {
            if (y == x - 9 || y == x - 11) {
                slotArrived = IsFree(y);// controlla che la casella di arrivo sia valida
                if (slotArrived) {
                    this.chessboard[x] = ' ';
                    this.chessboard[y] = 'H';
                }
                if (y > 10 && y < 19)
                    humanPoints++;// la pedina è nell'ultima riga.
            } else if ((y == x - 18 || y < x - 22) &&
                    (this.chessboard[x - 9] == 'C' && this.chessboard[x - 18] == ' ')
                    || (this.chessboard[x - 11] == 'C' && this.chessboard[x - 22] == ' ')) {
                slotArrived = IsFree(y);// controlla che la casella di arrivo sia valida
                if (slotArrived) {
                    pcPlayer.Catturato(x, y); // PC registra cattura della sua pedina
                    humanPoints++;// incrementa i punti
                    this.chessboard[x] = ' ';
                    this.chessboard[y] = 'H';
                }
                if (y > 10 && y < 19)
                    humanPoints++;// la pedina è nell'ultima riga.
            }
        }
        return humanPiece && slotArrived;
    }

    public char getSlot(int n) {
        return this.chessboard[n];
    }

}
