/*
 * Peter Edge
 * CSCI 5481 Final Project
 *
 * BaseballCap : the poor man's TopHat
 *
 * This project is the main function for BaseballCap.
 * It performs mapping of junction reads to exons. 
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.ArrayList;

public class BaseballCap {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException{
        try {

            // require exactly two arguments: the name of the read file, and the name of the exon file
            if (args.length != 2){

                System.out.println("\nUsage:\nBaseballCap <Reads Fasta File> <Exons Fasta File>");
                System.out.println("\nOr, for included toy and real (NM_002024) demos:\nmake toy\n*OR*\nmake real");

            } else{

                // read in arguments, scan in fasta files
                String readsFileName = args[0];
                String exonsFileName = args[1];
                ArrayList<Fasta> reads = Utilities.fastaScan(readsFileName);
                ArrayList<Fasta> exons = Utilities.fastaScan(exonsFileName);

                int exonCount = exons.size();
                ArrayList<PartialAlignment> alignmentList = new ArrayList<PartialAlignment>();
                HashMap<String, Integer> readLenDict = new HashMap<String, Integer>();
                int index = 1;

                // for each exon, perform burrow-wheeler transform of forward and reverse sequence
                // then attempt to map each each read to the transformed exon,
                // and attempt to map the reverse of each read to the transformed reverse exon.
                for (Fasta exon : exons){

                    BWT forwardBWT = new BWT(exon, index);
                    BWT reverseBWT = new BWT(exon.reverse(), index++);
                    for (Fasta read: reads){

                        readLenDict.put(read.header, read.sequence.length());
                        PartialAlignment partialForward = forwardBWT.match(read);
                        PartialAlignment partialReverse = reverseBWT.match(read.reverse());
                        int readLen = read.sequence.length();

                        // only keep the partial alignment if the overhang is between 1/4 and 3/4 read length
                        if ((partialForward.overhang > readLen / 4)&&(partialForward.overhang < readLen * 3 / 4)){
                            alignmentList.add(partialForward);
                        }

                        if ((partialReverse.overhang > readLen / 4)&&(partialReverse.overhang < readLen * 3 / 4)){
                            alignmentList.add(partialReverse);
                        }
                    }
                }

                // variables for the partial alignments
                PartialAlignment partial1;
                PartialAlignment partial2;
                Integer readLen, summedLen;

                // let the garbage collector know that reads & exons no longer needed.
                reads = null;
                exons = null;

                ArrayList<Junction> Junctions = new ArrayList<Junction>();

                for (int i = 0; i < alignmentList.size(); i++){

                    partial1 = alignmentList.get(i);

                    if (partial1.isReversed){
                        // partial1 is a forward partial alignment
                        // partial2 is expected to be a reverse partial alignment
                        for (int j = i + 1; j < alignmentList.size(); j++){

                            partial2 = alignmentList.get(j);
                            readLen = readLenDict.get(partial2.readHeader);
                            summedLen = partial1.overhang + partial2.overhang;

                            if (!partial2.isReversed // partial alignments are from opposite sides of an exon
                            && (partial1.readHeader.equals(partial2.readHeader)) // partial alignments are from same read
                            && (readLen == summedLen)){    // check if length is correct

                                // found a valid junction to add to list
                                Junctions.add(new Junction(partial1.exonIndex, partial2.exonIndex, partial1.overhang, partial2.overhang));

                            }
                        }
                    }
                }

                // matrices to keep track of how many of each junction seen (from which exon to which exon)
                // also keep track of total overhang in order to return an average value, although this doesn't matter too much
                int[][] junctionReadCounts = new int[exonCount + 1][exonCount + 1];
                int[][] totalLeftOverhang = new int[exonCount + 1][exonCount + 1];
                int[][] totalRightOverhang = new int[exonCount + 1][exonCount + 1];

                // indexing at 1 to match exon indexing
                // less space efficient but less confusing
                for (int i = 1; i <= exonCount; i++){
                    for (int j = 1; j <= exonCount; j ++){
                        junctionReadCounts[i][j] = 0;
                        totalLeftOverhang[i][j] = 0;
                        totalLeftOverhang[i][j] = 0;
                    }
                }

                // go through all junctions and count the numbers of specific junctions and the overhangs
                for (Junction junction : Junctions){
                    junctionReadCounts[junction.leftExon][junction.rightExon] += 1;
                    totalLeftOverhang[junction.leftExon][junction.rightExon] += junction.leftOverhang;
                    totalRightOverhang[junction.leftExon][junction.rightExon] += junction.rightOverhang;
                }

                // print everything to console column style
                System.out.println("LeftExon\tRightExon\tAvgLeftOverhang\tAvgRightOverhang\tCount");
                for (Integer i = 1; i <= exonCount; i++){
                    for (Integer j = 1; j <= exonCount; j ++){
                        if (junctionReadCounts[i][j] != 0){
                            Integer count = junctionReadCounts[i][j];
                            Float avgLeftOverhang = (float)totalLeftOverhang[i][j] / count;
                            Float avgRightOverhang = (float)totalRightOverhang[i][j] / count;
                            System.out.println(i + "\t" + j + "\t" + avgLeftOverhang + "\t" + avgRightOverhang + "\t" + count);
                        }
                    }
                }

            }
        }catch(FileNotFoundException f){
            System.out.println("File not found\n");
        }
    }
}