package net.fantasticfantasy.nbs2mcfunc;

import net.fantasticfantasy.nbs2mcfunc.mcfunc.MCFuncWriter;
import net.fantasticfantasy.nbs2mcfunc.nbs.NBS;
import net.fantasticfantasy.nbs2mcfunc.nbs.NBSReader;

public class Main {
	
	public static void main(String... args) {
		if (args.length < 2) throw new IllegalStateException("Not enough arguments!");
		NBSReader reader = new NBSReader(args[0]);
		NBS nbs = reader.read();
		reader.dispose();
		
		String name = args.length >= 3 ? args[2] : "untitled";
		MCFuncWriter writer = new MCFuncWriter(args[1], name);
		writer.write(nbs);
	}
}
