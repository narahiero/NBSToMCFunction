package net.fantasticfantasy.nbs2mcfunc.mcfunc;

import java.util.ArrayList;
import java.util.List;
import net.fantasticfantasy.nbs2mcfunc.nbs.Layer;
import net.fantasticfantasy.nbs2mcfunc.nbs.Note;

public class NoteStream {
	
	private List<Note> notes;
	private int pos;
	
	public NoteStream(List<Layer> layers, int count) {
		notes = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			for (Layer layer : layers) {
				Note note = layer.get(i);
				if (note != null) layer.add(note);
			}
		}
	}
	
	public Note get() {
		return notes.get(pos++);
	}
	
	public boolean hasRemaining() {
		return pos < notes.size();
	}
}
