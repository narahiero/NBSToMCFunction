package net.fantasticfantasy.nhcmds.nbstomcfunc;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import net.fantasticfantasy.nhcmds.nbstomcfunc.Tick.Note;

public class NBSReader {
	
	private DataInputStream in;
	
	public NBSReader(FileInputStream in) {
		this.in = new DataInputStream(in);
	}
	
	public NBSFile read() throws IOException {
		NBSFile nbs = new NBSFile();
		
		nbs.length = this.readShort();
		nbs.layers = this.readShort();
		nbs.title = this.readString();
		nbs.author = this.readString();
		nbs.originalAuthor = this.readString();
		nbs.description = this.readString();
		nbs.tempo = this.readShort();
		
		this.readByte(); //Auto-save
		this.readByte(); //Auto-save delay
		this.readByte(); //Time Signature
		this.readInt(); //Time Spent
		this.readInt(); //Left Clicks
		this.readInt(); //Right Clicks
		this.readInt(); //Blocks Added
		this.readInt(); //Blocks Removed
		this.readString(); //Import file name
		
		int tick = -1;
		while (true) {
			short jumps = this.readShort();
			if (jumps == 0) {
				break;
			}
			tick += jumps;
			Tick t = new Tick(tick * (2000 / nbs.tempo));
			short layer = -1;
			while (true) {
				short ljumps = this.readShort();
				if (ljumps == 0) {
					break;
				}
				layer += ljumps;
				byte type = this.readByte();
				byte key = this.readByte();
				t.notes.add(new Note(layer, type, key));
			}
			nbs.ticks.add(t);
		}
		
		return nbs;
	}
	
	private byte readByte() throws IOException {
		return this.in.readByte();
	}
	
	private short readShort() throws IOException {
		return Short.reverseBytes(this.in.readShort());
	}
	
	private int readInt() throws IOException {
		return Integer.reverseBytes(this.in.readInt());
	}
	
	private String readString() throws IOException {
		int len = this.readInt();
		StringBuilder str = new StringBuilder();
		while (len-- > 0) {
			str.append((char) this.in.readByte());
		}
		return str.toString();
	}
}
