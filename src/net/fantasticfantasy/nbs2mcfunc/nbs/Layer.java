package net.fantasticfantasy.nbs2mcfunc.nbs;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	
	private List<Note> notes;
	private String name; //Unused
	private float volume;
	
	private boolean empty;
	
	public Layer() {
		notes = new ArrayList<>();
		empty = true;
	}
	
	public void add(Note note) {
		notes.add(note);
		empty = false;
	}
	
	public void skip(int amount) {
		while (amount-- > 0) notes.add(null);
	}
	
	public Note get(int index) {
		return notes.get(index);
	}
	
	public List<Note> get() {
		return notes;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}
	
	public String getName() {
		return name;
	}
	
	public float getVolume() {
		return volume;
	}
	
	public boolean isEmpty() {
		return empty;
	}
}
