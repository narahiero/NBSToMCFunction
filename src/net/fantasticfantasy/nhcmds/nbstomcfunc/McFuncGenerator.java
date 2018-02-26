package net.fantasticfantasy.nhcmds.nbstomcfunc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class McFuncGenerator {
	
	private static final String SOUND_PATTERN = "execute @s[score_nhc_ntm_songTime_min=%1$s,"
			+ "score_nhc_ntm_songTime=%1$s] ~ ~ ~ playsound block.note.%2$s neutral @s ~ ~ ~ 1 %3$s 1";
	private static final String[] NOTE_SOUND_NAME = {
			"harp", "bass", "basedrum", "snare", "hat", "guitar", "flute", "bell", "chime",
			"xylophone", "pling"
	};
	
	private NBSFile nbs;
	
	public McFuncGenerator(NBSFile nbs) {
		this.nbs = nbs;
	}
	
	public void generate(File output, String name) throws IOException {
		if (!output.isDirectory()) {
			output.mkdirs();
		}
		File internal = new File(output, "nhcmds/-/nbsToMcfunc/" + name);
		if (!internal.isDirectory()) {
			internal.mkdirs();
		}
		File func = new File(output, name + ".mcfunction");
		if (!func.isFile()) {
			func.createNewFile();
		}
		this.generateMainFunc(func, name);
		this.generateSubFuncs(internal, name);
	}
	
	private void generateMainFunc(File func, String name) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(func))) {
			this.writeHeader(writer);
			writer.write("function nhcmds:-/nbsToMcfunc/" + name + "/" + name);
			writer.newLine();
			this.writeOptions(writer);
		}
	}
	
	private void generateSubFuncs(File internal, String name) throws IOException {
		File mainIntern = new File(internal, name + ".mcfunction");
		this.createIfNotFile(mainIntern);
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(mainIntern))) {
			this.writeSubHeader(name, writer);
			this.writeHeader(writer);
			
			for (Tick tick : this.nbs.ticks) {
				for (Tick.Note note : tick.notes) {
					writer.write(String.format(SOUND_PATTERN, tick.tick,
							NOTE_SOUND_NAME[note.type], (note.key - 33) / 12f));
					writer.newLine();
				}
			}
		}
	}
	
//	private void generateSubFuncs(File internal, String name) throws IOException {
//		File mainIntern = new File(internal, name + ".mcfunction");
//		this.createIfNotFile(mainIntern);
//		
//		double sqrt = Math.sqrt(this.nbs.length * this.nbs.tickMultiplier);
//		int numFile = (int) sqrt;
//		int perFile = (int) sqrt;
//		if ((sqrt - (int) sqrt) > 0) {
//			numFile++;
//		}
//		
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(mainIntern))) {
//			this.writeSubHeader(name, writer);
//			this.writeHeader(writer);
//			
//			int off = 0;
//			for (int file = 0; file < numFile; file++) {
//				writer.write("function nhcmds:-/nbsToMcfunc/" + name + "/" + name + file
//						+ " if @s[score_nhc_ntm_songTime_min=" + off + ",score_nhc_ntm_songTime="
//								+ (off + perFile - 1) + "]");
//				writer.newLine();
//				
//				File moreIntern = new File(internal, name + file + ".mcfunction");
//				this.createIfNotFile(moreIntern);
//				try (BufferedWriter subWriter = new BufferedWriter(new FileWriter(moreIntern))) {
//					this.writeSubHeader(name, subWriter);
//					this.writeHeader(subWriter);
//					for (int i = 0; i < perFile; i++) {
//						if (i + off == this.nbs.length * this.nbs.tickMultiplier) {
//							return;
//						}
//						for (Tick tick : this.nbs.ticks) {
//							if (tick.tick == i + off) {
//								for (Tick.Note note : tick.notes) {
//									subWriter.write(String.format(SOUND_PATTERN, tick.tick,
//											NOTE_SOUND_NAME[note.type], (note.key - 33) / 12f));
//									subWriter.newLine();
//								}
//							}
//						}
//					}
//				}
//				off += perFile;
//			}
//		}
//	}
	
	private void createIfNotFile(File file) throws IOException {
		if (!file.isFile()) {
			file.createNewFile();
		}
	}
	
	private void writeHeader(BufferedWriter writer) throws IOException {
		writer.write("#McFunction Song file generated using NbsToMcFunc");
		writer.newLine();
		writer.newLine();
		writer.write("#Song title: " + this.nbs.title);
		writer.newLine();
		writer.write("#Song NBS file author: " + this.nbs.author);
		writer.newLine();
		writer.write("#Original song author: " + this.nbs.originalAuthor);
		writer.newLine();
		writer.newLine();
		String[] descLines = this.nbs.description.split("\n");
		for (String descLine : descLines) {
			writer.write("#" + descLine);
			writer.newLine();
		}
		writer.newLine();
	}
	
	private void writeSubHeader(String name, BufferedWriter writer) throws IOException {
		writer.write("#This mcfunction is a sub-function of " + name);
		writer.newLine();
		writer.newLine();
	}
	
	private void writeOptions(BufferedWriter writer) throws IOException {
		writer.write("scoreboard players add @s[score_nhc_ntm_autoPlay_min=1]"
				+ " nhc_ntm_songTime 1");
		writer.newLine();
		writer.write("scoreboard players set @s[score_nhc_ntm_autoRset_min=1,"
				+ "score_nhc_ntm_songTime_min=" + (this.nbs.length * this.nbs.tickMultiplier)
				+ "] nhc_ntm_songTime"
				+ " -1");
		writer.newLine();
	}
}
