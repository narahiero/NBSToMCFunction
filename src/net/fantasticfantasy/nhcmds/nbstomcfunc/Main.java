package net.fantasticfantasy.nhcmds.nbstomcfunc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
		if (args.length < 3) {
			error("Missing arguments (input, output, name)", 2);
		}
		File file = new File(args[0]);
		if (!file.isFile()) {
			error("Could not find file " + file, 3);
		}
		FileInputStream in;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			error("Could not create input stream: " + e.getMessage(), 4);
			in = null;
		}
		NBSReader reader = new NBSReader(in);
		NBSFile nbs;
		try {
			nbs = reader.read();
		} catch (IOException e) {
			error("Could not read input: " + e.getMessage(), 5);
			nbs = null;
		}
		
		short tempo = nbs.tempo;
		if (tempo == 2000 || tempo == 1000 || tempo == 500 || tempo == 250 || tempo == 125) {
			nbs.tickMultiplier = (short) (2000 / tempo);
		} else {
			error("Unsupported song tempo: " + tempo, 201);
		}
		
		McFuncGenerator gen = new McFuncGenerator(nbs);
		try {
			gen.generate(new File(args[1]), args[2]);
		} catch (IOException e) {
			error("Could not generate mcfunction files: " + e.getMessage(), 6);
		}
	}
	
	private static void error(String msg, int code) {
		System.err.println(msg);
		System.err.println("Error Code: " + code);
		System.exit(code);
	}
	
	static void printNBS(NBSFile nbs) {
		System.out.println("Length: " + nbs.length);
		System.out.println("Layers: " + nbs.layers);
		System.out.println("Title: " + nbs.title);
		System.out.println("Author: " + nbs.author);
		System.out.println("Original Author: " + nbs.originalAuthor);
		System.out.println("Description: " + nbs.description);
		System.out.println("Tempo: " + nbs.tempo);
		
		for (Tick tick : nbs.ticks) {
			System.out.print("Tick " + tick.tick + ":");
			for (Tick.Note note : tick.notes) {
				System.out.print(" [" + note.layer + "," + note.type + "," + note.key + "]");
			}
			System.out.println();
		}
	}
}