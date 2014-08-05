/*
 * Peter Edge
 * CSCI 5481 Final Project
 * 
 * This file contains the Junction class.
 * It holds information about when two partial alignment objects
 * are found to be the result of a junction read.
*/

public class Junction {

    // index of the left (upstream) and right (downstream) exon
    Integer leftExon;
    Integer rightExon;

    // amount of bases that the read has on the edge of exon
    Integer leftOverhang;
    Integer rightOverhang;

    Junction(){};

    Junction(Integer le, Integer re, Integer lo, Integer ro){
        
        leftExon = le;
        rightExon = re;
        leftOverhang = lo;
        rightOverhang = ro;
    }

    @Override
    public String toString(){
        // returns a string that resembles a single row of sample output for toy dataset
        return (leftExon + "\t" + rightExon + "\t" + leftOverhang + "\t" + rightOverhang);
    }
}