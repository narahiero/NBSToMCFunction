package net.fantasticfantasy.nbs2mcfunc.nbs;

public class Note {
	
	private static final float[] PITCH_TABLE = {
			0.5f, 0.53f, 0.561f, 0.595f, 0.63f, 0.667f, 0.707f, 0.749f, 0.794f, 0.841f, 0.891f, 0.944f, 1, 1.059f,
			1.122f, 1.189f, 1.26f, 1.335f, 1.414f, 1.498f, 1.587f, 1.682f, 1.782f, 1.888f, 2
	};
	
	public final Layer layer;
	public final int tick;
	public final Instrument instrument;
	public final float pitch;
	
	public Note(Layer layer, int iid, byte raw) {
		if (iid < 0 || iid > 9) throw new IllegalArgumentException("Invalid instrument ID");
		if (raw < 33 || raw > 57) throw new IllegalArgumentException("Pitch out of octave");
		
		this.layer = layer;
		this.tick = layer.get().size();
		instrument = Instrument.values()[iid];
		pitch = PITCH_TABLE[raw - 33];
	}
}
