package net.fantasticfantasy.nhcmds.nbstomcfunc;

import java.util.ArrayList;
import java.util.List;

public class NBSFile {
	
	public short length;
	public short layers;
	public String title;
	public String author;
	public String originalAuthor;
	public String description;
	public short tempo;
	
	//Actually important stuff below
	public short tickMultiplier;
	public final List<Tick> ticks;
	
	public NBSFile() {
		this.ticks = new ArrayList<>();
	}
}
