import java.util.Random;

public class pcPlayer {
    private int C = 8;// N pezzi PC
    private int point = 0;
    public int pos[];
    public char[] chessboard = new char[100];

    pcPlayer(char[] chessboard) {
        this.chessboard = chessboard;
        pos = new int[8];
        int j = 0;
        for (int i = 11; i <= 17; i = i + 2) {// prima schiera di pedine PC
            pos[j] = i;
            j++;
        }
        for (int i = 22; i <= 28; i = i + 2) {// seconda schiera di pedine PC
            pos[j] = i;
            j++;
        }

    }

    /**
     * restituisce la casella che viene mossa. Nell'ordine si seleziona la prima
     * mossa valida che
     * 1. consente una cattura posizionando la pedina in una casella non pericolosa
     * 2. salva un pezzo a rischio cattura
     * 3. pone la pedina in una posizione sicura, ma di minaccia verso l'avversario
     * 4. effettua una cattura, ma esponendosi al rischio di cattura
     * 5. in caso di fallimento delle precedenti genera una mossa casuale
     * 
     * @return int x
     */
    public int Mossa() {
        int x;
        x = CatturaBuonaESicura();
        if (x == 0) {
            x = PezzoARischio();
        }

        if (x == 0) {
            x = Minaccia();
        }
        if (x == 0) {
            x = CatturaARischio();
        }
        if (x == 0) {
            x = Casuale();
        }
        return x;
    }

    /**
     * metodo richiamato da una mossa di cattura dell'avversario.
     * Effettua la cancellazione del pezzo catturato dalla scacchiera,
     * aggiorna il numero di pezzi in gioco e compatta l'array dei pezzi
     * del PC
     * 
     * @param x: casella di partenza della mossa avversaria
     * @param y: casella di arrivo della mossa avversaria
     */
    public void Catturato(int x, int y) {
        if (y == x - 18)
            y = x - 9;// posizione della pedina catturata
        if (y == x - 22)
            y = x - 11;
        for (int i = 0; i < 8; i++) {
            if (pos[i] == y) {// cerca nell'1 array la pedina catturata
                for (int j = i; j < C - 1; j++)
                    pos[j] = pos[j + 1];// compatta l'array togliendo il riferimento
            }
        }
        chessboard[y] = ' ';// cancella la pedina catturata
        C--;// diminuisce il numero di pedine in gioco
    }

    public int getPoints() {
        return point;
    }

    private boolean IsDangerous(int x) {// individua una casella sotto attacco
        boolean T = false;
        if (chessboard[x + 11] == 'H' && chessboard[x - 9] == ' ')
            T = true;
        if (chessboard[x + 9] == 'H' && chessboard[x - 11] == ' ')
            T = true;
        return T;
    }

    private boolean IsLast(int x) {
        boolean T = false;
        if (x == 82)
            T = true;
        else if (x == 84)
            T = true;
        else if (x == 86)
            T = true;
        else if (x == 88)
            T = true;
        return T;
    }

    private boolean IsFree(int x) {
        boolean casellaDisponibile = IsValid(x);
        if (chessboard[x] == ' ' && casellaDisponibile) {
            return true;
        } else {
            return false;
        }
    }

    private boolean IsValid(int x) {
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
        /*
         * in questa condizione e nell'altra ho chiesto se in quella posizione della
         * scacchiera ci fosse un muro, perché
         * il progrmma mi dava errore perché se trovava uno spazio vuoto lo convertiva
         * in 32(da char a intero lo spazio vuoto
         * corrisponde a 32 e quindi mi dava errore).
         */
        for (int i = 32; i < 39; i = i + 2)
            if (x == i && chessboard[x] == '■')
                // if (x == i && chessboard[x] == '■')
                T = false;
        for (int i = 41; i < 48; i = i + 2)
            if (x == i)
                T = false;
        for (int i = 52; i < 59; i = i + 2)
            if (x == i)
                T = false;
        for (int i = 61; i < 68; i = i + 2)
            if (x == i && chessboard[x] == '■')
                T = false;
        for (int i = 72; i < 79; i = i + 2)
            if (x == i)
                T = false;
        for (int i = 81; i < 88; i = i + 2)
            if (x == i)
                T = false;
        return T;
    }

    private int Casuale() {
        Random R = new Random();
        int x2 = 0, i, y, contatore = 0;
        while (x2 == 0) {
            i = R.nextInt(C);// decide la pedina da spostare
            y = pos[i];// y è la posizione della pedina da spostare
            // ora controlla se quella pedina può essere mossa...
            if (IsFree(y + 9)) {// prima a sinistra
                chessboard[y + 9] = 'C'; // copia il simbolo nella nuova posizione
                chessboard[y] = ' ';// lo cancella dalla vecchia
                pos[i] = y + 9;// aggiorna l’array delle posizioni
                if (IsLast(y + 9))
                    point++;// se la casella destinazione è
                // nell’ultima fila aggiunge un punto
                x2 = y;
                break;
            } // la mossa selezionata viene restituita dal metodo
            else if (IsFree(y + 11)) {// poi a destra
                chessboard[y + 11] = 'C';
                chessboard[y] = ' ';
                pos[i] = y + 11;
                if (IsLast(y + 11))
                    point++;
                x2 = y;
                break;
            }
            if (contatore == 5) {
                break;
            }
            contatore++;
        }
        return x2;
    }

    private int CatturaARischio() {
        int x = 0;
        int y;// mossa da provare
        for (int i = 0; i < C; i++) {// per tutti i pezzi nell'array pos verifica la mossa
            y = pos[i] + 18;// prova a destra
            if (IsValid(chessboard[y])) {
                if (chessboard[y - 9] == 'H' && IsFree(y)) {// la casella adiacente è una H //e quella dopo è libera
                    chessboard[pos[i]] = ' ';
                    chessboard[y - 9] = ' ';// mangia sulla diagonale destra
                    point++;
                    pos[i] = y;
                    if (IsLast(y))
                        point++;
                    x = y - 18;// comunico la casella di arrivo
                    chessboard[y] = 'C';
                    break;
                }
            } // se trova una mossa valida esce dal ciclo.

            y = pos[i] + 22;// prova a sinistra
            if (IsValid(chessboard[y])) {
                if (chessboard[y - 11] == 'H' && IsFree(y)) {
                    chessboard[pos[i]] = ' ';
                    chessboard[y - 11] = ' ';// mangia sulla diagonale sinistra
                    point++;
                    pos[i] = y;
                    if (IsLast(y))
                        point++;
                    x = y - 22;
                    chessboard[y] = 'C';
                    break;
                }
            }
        }
        return x;
    }

    private int Minaccia() {// la mossa va a minacciare un pezzo avversario (ma
        // potrebbe comportare qualche rischio)
        int x = 0;
        int y;// mossa da provare
        for (int i = 0; i < C; i++) {// per tutti i pezzi nell'array pos verifica la mossa
            y = pos[i] + 9;// prova a sinistra
            if (IsFree(y) && chessboard[y - 11] == 'C' && chessboard[y + 11] == 'H') {
                // davanti alla pedina c’è uno spazio che porterebbe vicino alla pedina umana
                chessboard[pos[i]] = ' ';// si sposta a sinistra
                chessboard[y] = 'C';
                pos[i] = y;
                x = y - 9;
                break;
            }
            y = pos[i] + 11;// prova a destra
            if (IsFree(y) && chessboard[y - 9] == 'C' && chessboard[y + 9] == 'H') {
                chessboard[pos[i]] = ' ';// si sposta a sinistra
                chessboard[y] = 'C';
                pos[i] = y;
                x = y - 11;
                break;
            }
        }
        return x;
    }

    private int PezzoARischio() {
        int x = 0;
        int y;// mossa da provare
        for (int i = 0; i < C; i++) {// per tutti i pezzi nell'array pos verifica la mossa
            y = pos[i];
            if (IsDangerous(y)) {// la posizione è pericolosa
                if (IsFree(y + 9) && !IsDangerous(y + 9) && chessboard[y + 18] != 'H') {
                    // esiste una mossa non pericolosa
                    chessboard[y] = ' ';// si sposta a sinistra
                    chessboard[y + 9] = 'C';
                    pos[i] = y + 9;
                    if (IsLast(y + 9))
                        point++;
                    x = y;
                    break;
                }
                if (IsFree(y + 11) && !IsDangerous(y + 11) && chessboard[y + 22] != 'H') {
                    chessboard[y] = ' ';// si sposta a destra
                    chessboard[y + 11] = 'C';
                    pos[i] = y + 11;
                    if (IsLast(y + 11))
                        point++;
                    x = y;
                    break;
                }
            }
        }
        return x;
    }

    private int CatturaBuonaESicura() {
        int x = 0;
        int y;// mossa da provare
        for (int i = 0; i < C; i++) {// per tutti i pezzi nell'array "pos"(posizione) verifica la mossa
            y = pos[i] + 18;// prova a destra
            if (IsValid(chessboard[y])) {
                if (chessboard[y - 9] == 'H' && IsFree(y) && !IsDangerous(y) && chessboard[y + 9] != 'H') {
                    chessboard[pos[i]] = ' ';// la casella di partenza si cancella
                    chessboard[y - 9] = ' ';// mangia sulla diagonale destra
                    point++;
                    pos[i] = y;// aggiorno la posizione nell'array
                    if (IsLast(y))
                        point++;
                    x = y - 18;
                    chessboard[y] = 'C';// simbolo nella casella di arrivo
                    break;
                }
            } // se trova una mossa valida esce dal ciclo.

            y = pos[i] + 22;// prova a sinistra
            if (IsValid(chessboard[y])) {
                if (chessboard[y - 11] == 'H' && IsFree(y) && !IsDangerous(y) && chessboard[y + 11] != 'H') {
                    chessboard[pos[i]] = ' ';
                    chessboard[y - 11] = ' ';// mangia sulla diagonale destra
                    point++;
                    pos[i] = y;
                    if (IsLast(y))
                        point++;// se ha raggiunto l'ultima fila
                    x = y - 22;
                    chessboard[y] = 'C';
                    break;
                }
            }

        }
        return x;
    }
}