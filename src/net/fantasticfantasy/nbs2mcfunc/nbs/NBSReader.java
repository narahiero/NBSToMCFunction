package net.fantasticfantasy.nbs2mcfunc.nbs;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NBSReader {
	
	private DataInputStream in;
	
	public NBSReader(String file) {
		try {
			in = new DataInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("File not found!");
		}
	}
	
	public NBS read() {
		NBS nbs = new NBS();
		try {
			readShort(); //Length
			int layerCount = readShort();
			nbs.name = readString();
			nbs.author = readString();
			nbs.origAuthor = readString();
			nbs.desc = readString();
			nbs.tempo = 2000 / readShort();
			readByte(); //Auto-save
			readByte(); //Auto-save delay
			readByte(); //Time signature
			readInt(); //Minutes spent
			readInt(); //Left clicks
			readInt(); //Right clicks
			readInt(); //Blocks added
			readInt(); //Blocks removed
			readString(); //Import file name
			
			while (nbs.layers.size() < layerCount) nbs.layers.add(new Layer());
			
			while (true) {
				int jumps = readShort();
				if (jumps == 0) break;
				
				for (Layer layer : nbs.layers) layer.skip(jumps - 1);
				
				int index = -1;
				while (true) {
					int ljumps = readShort();
					if (ljumps == 0) break;
					
					for (int i = 1; i < ljumps; i++) nbs.layers.get(index + i).skip(1);
					index += ljumps;
					
					byte iid = readByte();
					byte pitch = readByte();
					
					Layer layer = nbs.layers.get(index);
					layer.add(new Note(layer, iid, pitch));
				}
				
				while (++index < layerCount) nbs.layers.get(index).skip(1);
			}
			
			for (Layer layer : nbs.layers) {
				layer.setName(readString());
				layer.setVolume(readByte() / 100f);
			}
			for (int i = nbs.layers.size() - 1; i >= 0; i--)
				if (nbs.layers.get(i).isEmpty()) nbs.layers.remove(i);
			
			nbs.count = nbs.layers.get(0).get().size();
			
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return nbs;
	}
	
	public void dispose() {
		try {
			in.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private byte readByte() throws IOException {
		return in.readByte();
	}
	
	private short readShort() throws IOException {
		return Short.reverseBytes(in.readShort());
	}
	
	private int readInt() throws IOException {
		return Integer.reverseBytes(in.readInt());
	}
	
	private String readString() throws IOException {
		int len = readInt();
		StringBuilder str = new StringBuilder();
		while (len-- > 0) {
			str.append((char) readByte());
		}
		return str.toString();
	}
}
