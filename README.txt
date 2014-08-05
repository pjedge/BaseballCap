Peter Edge
Final Project

Required: Java 7
recommended: make

To clean everything up:
make clean

To compile code:
make

To run on given toy sample:
make toy

To run on real data (replace XX with 25, 50, 75, 100 for read len)
make realXX

To compile (if make is not installed):
javac BaseballCap.java BWT.java Utilities.java Junction.java Permutation.java PartialAlignment.java Fasta.java

To run on toy data (if make is not installed):
java BaseballCap data/toy/Reads.fasta data/toy/Exons.fasta

To run on real data (if make is not installed):
java BaseballCap data/NM_002024/NM_002024SimulatedReadsLen25.fasta data/NM_002024/NM_002024Exons.fasta
java BaseballCap data/NM_002024/NM_002024SimulatedReadsLen50.fasta data/NM_002024/NM_002024Exons.fasta
java BaseballCap data/NM_002024/NM_002024SimulatedReadsLen75.fasta data/NM_002024/NM_002024Exons.fasta
java BaseballCap data/NM_002024/NM_002024SimulatedReadsLen100.fasta data/NM_002024/NM_002024Exons.fasta



Files and directories:

BaseballCap.java
Contains main function for BaseballCap tool.

BWT.java
Contains code related to performing BWT

data
contains data files

Fasta.java
contains a class for representing Fasta data

Final_Project_Report.pdf
the report for the project

Junction.java
class for representing Junctions

makefile
the makefile for the project

PartialAlignment.java
class for representing Partial Alignments of reads to exons

Permutation.java
class for representing Permutations of input sequence for use in BWT matrix

Reads.fasta

results
contains result files from TopHat and BaseballCap run on NM_002024 data

Utilities.java
contains a fasta scanning function, mostly.
