package ss.othello.game;

import java.io.Serializable;

public enum Mark{

    EMPTY, XX, OO, PP;

    /**
     * Returns the other mark.
     *
     * @return the other mark is this mark is not EMPTY or EMPTY
     */
    //@ ensures this == XX ==> \result == OO && this == OO ==> \result == XX;
    public Mark other() {
        if (this == XX) {
            return OO;
        } else if (this == OO) {
            return XX;
        } else {
            return EMPTY;
        }
    }

}
