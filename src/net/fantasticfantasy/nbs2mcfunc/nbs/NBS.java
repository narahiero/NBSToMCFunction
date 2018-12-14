package net.fantasticfantasy.nbs2mcfunc.nbs;

import java.util.ArrayList;
import java.util.List;

public class NBS {
	
	public String name;
	public String author;
	public String origAuthor;
	public String desc;
	
	public List<Layer> layers;
	public int tempo;
	public int count;
	
	public NBS() {
		layers = new ArrayList<>();
	}
}
