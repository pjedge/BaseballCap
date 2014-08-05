/*
 * Peter Edge
 * CSCI 5481 Final Project
 * 
 * This file primarily contains a fasta scanning helper function
*/

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

public class Utilities {

    // utility for scanning fasta file format.
    // simply takes a fasta filename
    // returns a list of fasta objects
    public static ArrayList<Fasta> fastaScan (String fastaFileName) throws FileNotFoundException{

        File fasta = new File(fastaFileName);
        Scanner scanner = new Scanner(fasta);
        scanner.useDelimiter("\n");
        String fileExtension = fastaFileName.substring(fastaFileName.lastIndexOf('.'));
        // the return list
        ArrayList<Fasta> seqList = new ArrayList<Fasta>();
        String line = "";
        String header = "";
        String seq = "";
        boolean readingSequence = false;

        if (fileExtension.equals(".fasta")||fileExtension.equals(".fa")){
            
            while(scanner.hasNext()){

                line = scanner.nextLine();

                // if a new entry
                if (line.contains(">")){
                    if (readingSequence){
                        seqList.add(new Fasta(header, seq, false));
                    }
                    header = line;
                    seq = "";
                    readingSequence = true;

                // else if continuing a previous entry
                }else if (readingSequence){
                    seq += line;
                }
            }
        }else{
            System.out.println("Error: non-Fasta input");
        }

        // add in the last fasta
        seqList.add(new Fasta(header, seq, false));
        return seqList;
    }
}
