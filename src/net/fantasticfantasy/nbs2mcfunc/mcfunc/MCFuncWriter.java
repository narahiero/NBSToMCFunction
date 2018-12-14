package net.fantasticfantasy.nbs2mcfunc.mcfunc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.fantasticfantasy.nbs2mcfunc.nbs.NBS;
import net.fantasticfantasy.nbs2mcfunc.nbs.Note;

public class MCFuncWriter {
	
	private File outdir, intOutdir;
	private String name;
	
	public MCFuncWriter(String out, String name) {
		outdir = new File(out);
		intOutdir = new File(outdir, name + "_");
		this.name = name;
	}
	
	public void write(NBS nbs) {
		makeFiles(nbs);
		
		NoteBatch batch = NoteBatch.fromLayerStream(new LayerStream(nbs.layers, nbs.count));
		writeBatch(batch, nbs.tempo, 0);
	}
	
	private void makeFiles(NBS nbs) {
		if (!outdir.exists() && !outdir.mkdirs())
			throw new IllegalStateException("Could not create output directory");

		if (!intOutdir.exists() && !intOutdir.mkdirs())
			throw new IllegalStateException("Could not create internal output directory");

		File main = new File(outdir, name + ".mcfunction");
		try {
			main.createNewFile();
		} catch (IOException e) {
			throw new IllegalStateException("Cound not create main file", e);
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(main))) {
			writer.write("###################################################################");
			writer.newLine();
			writer.write("##                                                               ##");
			writer.newLine();
			writer.write("##  .MCFUNCTION FILE GENERATED USING NaraHiero's NBS2MCFunction  ##");
			writer.newLine();
			writer.write("##    -- DO NOT EDIT                                             ##");
			writer.newLine();
			writer.write("##                                                               ##");
			writer.newLine();
			writer.write("###################################################################");
			writer.newLine();
			writer.newLine();
			writer.write("###################################################################");
			writer.newLine();
			writer.write("#");
			writer.newLine();
			writer.write("#  SONG INFORMATION");
			writer.newLine();
			writer.write("#");
			writer.newLine();
			writer.write("# Song name: " + nbs.name);
			writer.newLine();
			writer.write("# NBS author: " + nbs.author);
			writer.newLine();
			writer.write("# Original song author: " + nbs.origAuthor);
			writer.newLine();
			writer.write("# Description: " + nbs.desc.replaceAll("\n", " "));
			writer.newLine();
			writer.write("#");
			writer.newLine();
			writer.write("###################################################################");
			writer.newLine();
			writer.newLine();
			writer.newLine();
			writer.write("function nbs2mcfunc:" + name + "_/main");
			writer.newLine();
			
		} catch (IOException e) {
			throw new IllegalStateException("Could not write main file", e);
		}
	}
	
	private void writeBatch(NoteBatch batch, int tempo, int level) {
		int first = batch.first().tick * tempo;
		int last = batch.last().tick * tempo;
		String name = level == 0 ? "main" : first + "-" + last;
		
		StringBuilder sb = new StringBuilder();
		while (sb.length() < level) sb.append("_");
		String pre = sb.toString();
		
		File file = new File(intOutdir, pre + name + ".mcfunction");
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new IllegalStateException("Could not create internal file " + name, e);
		}
		
		if (batch.isSuperbatch()) {
			writeSuper(batch, tempo, file, pre);
			for (int i = 0; i < batch.size(); i++) {
				writeBatch(batch.subbatch(i), tempo, level + 1);
			}
		}
		else writeNotes(batch, tempo, file);
	}
	
	private void writeSuper(NoteBatch batch, int tempo, File out, String pre) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {
			writer.write("###################################################################");
			writer.newLine();
			writer.write("##                                                               ##");
			writer.newLine();
			writer.write("##  .MCFUNCTION FILE GENERATED USING NaraHiero's NBS2MCFunction  ##");
			writer.newLine();
			writer.write("##    -- DO NOT EDIT                                             ##");
			writer.newLine();
			writer.write("##                                                               ##");
			writer.newLine();
			writer.write("###################################################################");
			writer.newLine();
			writer.newLine();
			writer.write("# More information at main file " + name + ".mcfunction");
			writer.newLine();
			writer.newLine();
			
			for (int i = 0; i < batch.size(); i++) {
				NoteBatch sbatch = batch.subbatch(i);
				int first = sbatch.first().tick * tempo;
				int last = sbatch.last().tick * tempo;
				
				writer.write("execute if score @s _nbs2mcfunc_time matches " + first + ".." + last
						+ " run function nbs2mcfunc:" + name + "_/" + pre + "_" + first + "-" + last);
				writer.newLine();
			}
			
		} catch (IOException e) {
			throw new IllegalStateException("Could not write internal file " + out, e);
		}
	}
	
	private void writeNotes(NoteBatch batch, int tempo, File out) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {
			writer.write("###################################################################");
			writer.newLine();
			writer.write("##                                                               ##");
			writer.newLine();
			writer.write("##  .MCFUNCTION FILE GENERATED USING NaraHiero's NBS2MCFunction  ##");
			writer.newLine();
			writer.write("##    -- DO NOT EDIT                                             ##");
			writer.newLine();
			writer.write("##                                                               ##");
			writer.newLine();
			writer.write("###################################################################");
			writer.newLine();
			writer.newLine();
			writer.write("# More information at main file " + name + ".mcfunction");
			writer.newLine();
			writer.newLine();
			
			for (int i = 0; i < batch.size(); i++) {
				Note note = batch.get(i);
				int tick = note.tick * tempo;
				
				writer.write("execute if score @s _nbs2mcfunc_time matches " + tick + " run playsound block.note_block."
						+ note.instrument.getName() + " neutral @s ~ ~ ~ " + note.layer.getVolume() + " " + note.pitch);
				writer.newLine();
			}
			
		} catch (IOException e) {
			throw new IllegalStateException("Could not write internal file " + out, e);
		}
	}
}
