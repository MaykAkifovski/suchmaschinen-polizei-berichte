package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.Tries;
/**
 * Write a description of class Trie here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;

public class Trie
{
    //Root Node is empty in a Trie
    TNode root = new TNode('\0');

    private Map<String, Integer> predictedWords = new HashMap<>();

    //Method to take a word and insert into trie
    public void insert(String word, int value){
        char a[] = word.toCharArray();
        TNode focusNode = root;
        TNode n;
        int offset;
        for(int i=0;i<a.length;i++){
            //getting array index such that 0<=index<26
            offset = ((int)a[i])-97;
            n = new TNode(a[i]);

            if(focusNode.links[offset] == null){
                //Case where the letter does not exist
                focusNode.links[offset] = n;
                focusNode = n;
            }else if(focusNode.links[offset].letter == a[i]){
                //Case where letter previously inserted into that node
                focusNode = focusNode.links[offset];
            }
        }
        //This si where the word ends so setting node isEnd to true
        focusNode.isEnd = true;
        focusNode.setValue(value);
    }

    public boolean find(String word){
        char array[] = word.toCharArray();
        int offset;
        TNode focusNode = root;
        for(int i=0;i<word.length();i++){
            offset = ((int)array[i])-97;
            if(focusNode.links[offset] == null){
                return false;
            }else if(focusNode.links[offset].letter == array[i]){
                focusNode = focusNode.links[offset];
            }
        }
        if(focusNode.isEnd == false){
            return false;
        }

        return true;
    }

    public void autoComplete(String partial){
        TNode focusNode = root;
        char array[] = partial.toCharArray();
        int offset;

        for(int i=0;i<partial.length();i++){
            offset = ((int)array[i])-97;
            if(focusNode.links[offset] == null){
                return ;
            }else if(focusNode.links[offset].letter == array[i]){
                focusNode = focusNode.links[offset];
            }
        }
        complete(partial,focusNode);

    }

    public void complete(String sb, TNode focusNode){
        ArrayList<Integer> list = new ArrayList<>();
        if(focusNode.isEnd == true){
            //System.out.println(sb);
            predictedWords.put(sb, focusNode.getValue());
        }

        for(int i=0;i<160;i++){   //26
            if(focusNode.links[i]!=null){
                list.add(i);
            }
        }

        Iterator<Integer> it = list.iterator();
        TNode n;
        int index;
        String strNew;
        while(it.hasNext()){
            index = it.next().intValue();
            n = focusNode.links[index];
            strNew = sb+n.toString();

            complete(strNew,n);
        }

    }

    public Map<String, Integer> getSuggestions(String sb) { //eher schlecht
        autoComplete(sb);
        Map<String,Integer> predictedWordsLocal = this.predictedWords;
        predictedWords = new HashMap<>();
        return predictedWordsLocal;
    }




}
