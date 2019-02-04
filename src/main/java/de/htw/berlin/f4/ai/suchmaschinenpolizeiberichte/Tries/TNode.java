package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.Tries;

public class TNode{
    char letter;
    TNode links[];
    boolean isEnd;
    int value;


    public TNode(char letter){
        this.letter = letter;
        this.links = new TNode[160]; //26
        this.isEnd = false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString(){
        return (new String(letter+""));
    }

}