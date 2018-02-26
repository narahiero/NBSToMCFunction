package net.fantasticfantasy.nhcmds.nbstomcfunc;

import java.util.ArrayList;
import java.util.List;

public class Tick {
	
	public final int tick;
	public final List<Note> notes;
	
	public Tick(int tick) {
		this.tick = tick;
		this.notes = new ArrayList<>();
	}
	
	public static class Note {
		
		public final short layer;
		public final byte type;
		public final byte key;
		
		Note(short layer, byte type, byte key) {
			this.layer = layer;
			this.type = type;
			this.key = key;
		}
	}
}
