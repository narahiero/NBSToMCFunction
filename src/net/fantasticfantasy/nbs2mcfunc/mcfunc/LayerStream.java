package net.fantasticfantasy.nbs2mcfunc.mcfunc;

import java.util.ArrayList;
import java.util.List;
import net.fantasticfantasy.nbs2mcfunc.nbs.Layer;
import net.fantasticfantasy.nbs2mcfunc.nbs.Note;

public class LayerStream {
	
	private List<Layer> layers;
	private int cap;
	private int pos;
	
	public LayerStream(List<Layer> layers, int count) {
		this.layers = new ArrayList<>();
		this.cap = count;
		for (Layer layer : layers) {
			this.layers.add(layer);
		}
	}
	
	public List<Note> get() {
		List<Note> list = new ArrayList<>();
		for (Layer layer : layers) {
			Note note = layer.get(pos);
			if (note != null) list.add(note);
		}
		pos++;
		return list;
	}
	
	public boolean hasRemaining() {
		return pos < cap;
	}
}
