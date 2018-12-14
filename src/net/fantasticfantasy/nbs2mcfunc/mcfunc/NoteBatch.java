package net.fantasticfantasy.nbs2mcfunc.mcfunc;

import java.util.ArrayList;
import java.util.List;
import net.fantasticfantasy.nbs2mcfunc.nbs.Note;

public class NoteBatch {
	
	private boolean superbatch;
	private int cap;
	
	private List<Note> notes;
	private List<NoteBatch> batches;
	
	public NoteBatch(boolean superbatch, int cap) {
		this.superbatch = superbatch;
		this.cap = cap;
		if (superbatch) batches = new ArrayList<>();
		else notes = new ArrayList<>();
	}
	
	public void put(Note note) {
		notes.add(note);
	}
	
	public void put(List<Note> notes) {
		this.notes.addAll(notes);
	}
	
	public void put(NoteBatch batch) {
		batches.add(batch);
	}
	
	public Note get(int index) {
		return notes.get(index);
	}
	
	public NoteBatch subbatch(int index) {
		return batches.get(index);
	}
	
	public Note first() {
		if (superbatch) return batches.isEmpty() ? null : batches.get(0).first();
		else return notes.isEmpty() ? null : notes.get(0);
	}
	
	public Note last() {
		if (superbatch) return batches.isEmpty() ? null : batches.get(batches.size() - 1).last();
		else return notes.isEmpty() ? null : notes.get(notes.size() - 1);
	}
	
	public int size() {
		return superbatch ? batches.size() : notes.size();
	}
	
	public boolean isEmpty() {
		return superbatch ? batches.isEmpty() : notes.isEmpty();
	}
	
	public boolean isFull() {
		return superbatch ? batches.size() >= cap : notes.size() >= cap;
	}
	
	public boolean isSuperbatch() {
		return superbatch;
	}
	
	public static NoteBatch fromLayerStream(LayerStream stream) {
		return batch(stream, false);
	}
	
	private static NoteBatch batch(LayerStream stream, boolean fromSuper) {
		NoteBatch batch = new NoteBatch(false, 32);
		
		while (stream.hasRemaining()) {
			batch.put(stream.get());
			if (batch.isFull()) {
				if (fromSuper || !stream.hasRemaining()) break;
				else return batch(stream, batch);
			}
		}
		return batch;
	}
	
	private static NoteBatch batch(LayerStream stream, NoteBatch sub) {
		NoteBatch batch = new NoteBatch(true, 16);
		boolean fromSub = sub != null;
		if (fromSub) batch.put(sub);
		
		while (!batch.isFull()) {
			if (stream.hasRemaining()) {
				if (fromSub && sub.isSuperbatch()) batch.put(batch(stream, null));
				else batch.put(batch(stream, true));
			} else break;
		}
		if (stream.hasRemaining() && fromSub) return batch(stream, batch);
		else return batch;
	}
}
