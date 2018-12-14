package net.fantasticfantasy.nbs2mcfunc.nbs;

public enum Instrument {
	
	HARP, BASS, BASEDRUM, SNARE, HAT, GUITAR, FLUTE, BELL, CHIME, XYLOPHONE;
	
	public String getName() {
		return name().toLowerCase();
	}
}
