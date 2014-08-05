/*
 * Peter Edge
 * CSCI 5481 Final Project
 * BWT
 *
 * This file contains the BWT class, with functions that use BWT to align reads to exons
*/

import java.lang.Math;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.io.IOException; 
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;

public class BWT {

	// this class only holds on to the necessary information for a transformed string

	// index of occurence of first A, C, G, T in first column of BWT matrix
	Integer firstA, firstC, firstG, firstT;
	Integer aCount, cCount, gCount, tCount;
	String lastCol; // last column of the BWT matrix
	String exonHeader;
	Integer[] offsets; // contains base's rankings with respect to bases of the same kind
	Integer exonIndex;

	// constructor : takes a fasta file and produces the first and last column of BWT
	// also takes the index of the exon with respect to other exons for simple identification
	BWT (Fasta fa, Integer ei){

		exonIndex = ei;
		List<Permutation> permutations = new ArrayList<Permutation>();
		String perm = fa.sequence + "$";
		exonHeader = fa.header;
		Integer len = perm.length();
		offsets = new Integer[len];
		int count = 0;

		do{
			permutations.add(new Permutation(perm, count++));
			perm = perm.substring(len - 1, len) + perm.substring(0, len - 1);
		}while(!perm.substring(len - 1, len).equals("$"));
		
		Collections.sort(permutations);
		lastCol = "";
		String firstBase, lastBase;
		Integer index = 0;
		firstA = -1;
		firstC = -1;
		firstG = -1;
		firstT = -1;
		aCount = 0;
		cCount = 0;
		gCount = 0;
		tCount = 0;

		for (Permutation p : permutations){

			firstBase = p.sequence.substring(0,1).toLowerCase();

			if ((firstA == -1)&&(firstBase.equals("a"))){
				firstA = index;
			}else if ((firstC == -1)&&(firstBase.equals("c"))){
				firstC = index;
			}else if ((firstG == -1)&&(firstBase.equals("g"))){
				firstG = index;
			}else if ((firstT == -1)&&(firstBase.equals("t"))){
				firstT = index;
			}

			lastBase = p.sequence.substring(len-1, len);

			switch(lastBase.charAt(0)){
				case 'a':
				case 'A':
					offsets[index] = aCount;
					aCount++;
					break;
				case 'c':
				case 'C':
					offsets[index] = cCount;
					cCount++;
					break;
				case 'g':
				case 'G':
					offsets[index] = gCount;
					gCount++;
					break;
				case 't':
				case 'T':
					offsets[index] = tCount;
					tCount++;
			}

			lastCol += lastBase;
			index ++;
		}
	}

	Integer getFirstIndex(char base){
		switch (base){
			case 'a':
			case 'A':
				return firstA;
			case 'c':
			case 'C':
				return firstC;
			case 'g':
			case 'G':
				return firstG;
			case 't':
			case 'T':
				return firstT;
		}
		return null;
	}

	PartialAlignment match(Fasta fa){

		Integer i, j, exonLen, readLen, overhang;
		exonLen = lastCol.length() - 1;
		readLen = fa.sequence.length();
		overhang = 0;

		if (lastCol == null){
			System.out.println("ERROR: Null last column; Transform has not been performed yet.");
		}else{
			Integer top = 0;
			Integer bot = 0;
			i = 0;
			j = 0;
			char qc;

			// set up the first iteration
			switch (fa.sequence.charAt(readLen - 1)){
				case 'a':
				case 'A':
				top = firstA;
				bot = firstA + aCount;
				break;
				case 'c':
				case 'C':
				top = firstC;
				bot = firstC + cCount;
				break;
				case 'g':
				case 'G':
				top = firstG;
				bot = firstG + gCount;
				break;
				case 't':
				case 'T':
				top = firstT;
				bot = firstT + tCount;
				break;
				default:
				System.out.println("" + fa.sequence.charAt(readLen - 1));
				System.out.println("Error");
			}

			Integer lastValidTop = 0;
			
			for (i = readLen - 2; (i >= 0) && (top < bot); i--){
				
				qc = fa.sequence.charAt(i);
				Integer oldTop = top;

				for (j = top; j <= exonLen; j++){
					if (lastCol.charAt(j) == qc){
						top = getFirstIndex(lastCol.charAt(j)) + offsets[j];
						break;
					}
				}

				for (j = bot; j > 0; j--){
					if (lastCol.charAt(j - 1) == qc){
						bot = getFirstIndex(lastCol.charAt(j - 1)) + offsets[j - 1] + 1;
						break;
					}
				}

				// if top is still less than bot, then update the safe value of top
				lastValidTop = (top < bot) ? top : oldTop;
			}

			overhang = readLen - i - 2;
		}

		return new PartialAlignment(exonHeader, exonIndex, fa.header, fa.isReversed, overhang);
	}
}