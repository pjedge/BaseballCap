# Peter Edge
# makefile for BaseballCap tool
# the general template for this makefile was taken from:
# http://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html

JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Fasta.java \
	PartialAlignment.java \
	Permutation.java \
	Junction.java \
	Utilities.java \
	BWT.java \
	BaseballCap.java 

default: classes

classes: $(CLASSES:.java=.class)

# run BaseballCap on provided toy dataset
toy:
	java BaseballCap data/toy/Reads.fasta data/toy/Exons.fasta

# run BaseballCap on NM_002024 simulated reads, read length 25
real25:
	java BaseballCap data/NM_002024/NM_002024SimulatedReadsLen25.fasta data/NM_002024/NM_002024Exons.fasta

# run BaseballCap on NM_002024 simulated reads, read length 50
real50:
	java BaseballCap data/NM_002024/NM_002024SimulatedReadsLen50.fasta data/NM_002024/NM_002024Exons.fasta

# run BaseballCap on NM_002024 simulated reads, read length 75
real75:
	java BaseballCap data/NM_002024/NM_002024SimulatedReadsLen75.fasta data/NM_002024/NM_002024Exons.fasta

# run BaseballCap on NM_002024 simulated reads, read length 100
real100:
	java BaseballCap data/NM_002024/NM_002024SimulatedReadsLen100.fasta data/NM_002024/NM_002024Exons.fasta

clean:
	$(RM) *.class