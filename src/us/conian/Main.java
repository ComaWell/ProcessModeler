package us.conian;

import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;

public class Main {
	
	private static final long POLLING_RATE = 2_000_000_000L;//2000ms or 2 seconds
	
	private static final ProcessHandle CURRENT_PROCESS = ProcessHandle.current();
	
	/*
	private static final File CLUSTERS_FOLDER;
	
	static {
		try {
			CLUSTERS_FOLDER = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI().getPath()).getParent(), "clusters");
			if (!CLUSTERS_FOLDER.exists())
				CLUSTERS_FOLDER.mkdirs();
		} catch(URISyntaxException e) {
			throw new InternalError("Failed to find or create cluster directory: " + e.getMessage());
		}
	}
	*/
	
	//private static final String STOP = "stop";
	
	public static void main(String[] args) throws IOException {
		/*
		LocalDateTime start = LocalDateTime.now();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		AttachMode mode = determineAttachMode(in, (args == null || args.length == 0 ? null : args[0]));
		ProcessHandle process = attachToVM(in, mode);
		int sampleInterval = CLIUtils.determineSampleInterval(in, (args == null || args.length < 2 ? null : args[1]));
		int numSamples = CLIUtils.determineNumSamples(in, (args == null || args.length < 3 ? null : args[2]));
		
		List<Sample> samples = new ArrayList<>();
		//SampleStream stream = null;// = new SampleStream(process, sampleInterval, numSamples);
		//BufferedReader stopChecker = new BufferedReader(new InputStreamReader(System.in));
		while (stream.isAlive() || stream.hasNext()) {
			if (stream.hasNext()) {
				samples.add(stream.next());
				System.out.println("Sample Received!");
			}
			/*
			if (stopChecker.ready() && stopChecker.readLine().equalsIgnoreCase(STOP)) {
				System.out.println("Stopping early");
				break;
			}
			*
		}
		//stopChecker.close();
		stream.close();
		System.out.println("Done! Number of samples collected: " + samples.size());
		/*
		for (Sample sample : samples) {
			System.out.println(SampleUtils.sortReadings(sample));
			SampleObservation obs = new SampleObservation(sample);
			System.out.println(obs.toString());
		}
		*/
		//}
		
		Map<String, List<SampleSet>> sampleSets = SampleUtils.loadSampleSets(new File("C:\\Users\\Connor\\Documents\\ProcessTracker\\data"));
		/*
		//System.out.println(sampleSets.size());
		SampleSet samples = sampleSets.get("_total").get(0);
		for (Sample s : samples) {
			System.out.println(SampleUtils.toCSVString(s));
		}
		System.out.println(SampleUtils.toCSVString(samples.meta().meanSample()));
		//int readingsCount = samples.get(0).numReadings();
		//for (Sample s : samples) {
		//	double covVal = (s.get)
		//}
		for (double[] c : samples.meta().getCovMatrix()) {
			StringJoiner sj = new StringJoiner("\t", "[", "]");
			for (double d : c) {
				sj.add(String.valueOf(d));
			}
			System.out.println(sj.toString());
		}
		*/
		Hmm<ObservationVector> hmm = HmmUtils.generate(sampleSets);
		System.out.println(hmm.toString());
		System.out.println("\nExiting...");
	}
	
	private static List<ProcessHandle> pollProcesses() {
		return ProcessHandle.allProcesses()
				.filter((p) -> p.info().command().isPresent())
				.toList();
	}
	
	private static AttachMode determineAttachMode(BufferedReader in, String arg) throws IOException {
		AttachMode mode = null;
		if (arg != null) {
			for (AttachMode m : AttachMode.values()) {
				if (m.name().equalsIgnoreCase(arg)
						|| m.toString().equalsIgnoreCase(arg)) {
					mode = m;
					break;
				}
			}
			if (mode == null)
				System.err.println("Invalid attach mode argument: \"" + arg + "\"");
		}
		while (mode == null) {
			System.out.println("Please specify the attach mode to use. Available options are: ");
			int index = 0;
			for (AttachMode m : AttachMode.values()) {
				System.out.println(++index + ": " + m.name() + " (" + m.description() + ")");
			}
			System.out.print("Enter a number (1-" + AttachMode.values().length + ") > ");
			while (!in.ready()) { }
			String input = in.readLine();
			try {
				int selection = Integer.parseInt(input) - 1;
				mode = AttachMode.values()[selection];
			} catch (NumberFormatException e1) {
				System.err.println("Uh-oh, it looks like you didn't enter an integer: \'"
						+ input + "\'");
			} catch (IndexOutOfBoundsException e2) {
				System.err.println("Invalid option. Must be between 1 and " + AttachMode.values().length);
			}
		}
		System.out.println("Using attach mode " + mode);
		return mode;
	}
	
	private static ProcessHandle attachToVM(Scanner in, AttachMode mode) {
		List<ProcessHandle> processes = pollProcesses();
		ProcessHandle process = null;
		switch (mode) {
		case EXISTING: {
			if (processes.isEmpty())
				System.out.println("No other running JVMs found. Switching modes");
			else {
				System.out.println("Current JVMs:");
				for (int i = 0; i < processes.size(); i++) {
					ProcessHandle p = processes.get(i);
					System.out.println((i + 1) + ": " + p.pid() + " " + (p.equals(CURRENT_PROCESS) ? "(This Program)" : ProcessUtils.getProcessName(p)));
				}
				while (process == null) {					
					System.out.print("Please select the JVM to track (enter a number 1-" + processes.size() + ") > ");
					String input = in.next();
					try {
						int index = Integer.parseInt(input) - 1;
						process = processes.get(index);
					} catch (NumberFormatException e1) {
						System.err.println("Uh-oh, it looks like you didn't enter an integer: \'"
								+ input + "\'");
					} catch (IndexOutOfBoundsException e2) {
						System.err.println("Invalid option. Must be between 1 and " + processes.size());
					}
				}
			}
			if (process != null)
				break;
			}
			case NEXT_PROCESS: {
				double lastPoll = System.nanoTime();
				while (process == null) {
					double now = System.nanoTime();
					if (now - lastPoll < POLLING_RATE)
						continue;
					lastPoll = now;
					System.out.println("Searching...");
					for (ProcessHandle p : pollProcesses()) {
						if (!processes.contains(p)) {
							System.out.println("Found new VM: " + p.pid());
							process = p;
							break;
						}
					}
				}
				break;
			}
			default: {
				in.close();
				throw new InternalError("Unimplemented execution mode: " + mode);
			}
		}
		System.out.println("Attach successful! JVM: " + Objects.toString(process));
		return  process;
	}
	
	/*
	private static ProgramState determineProgramState(Scanner in, String arg) {
		ProgramState state = null;
		if (arg != null) try {
			state = ProgramState.of(arg);
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid program state argument: " + e.getLocalizedMessage());
		}
		while (state == null) {
			System.out.print("Please enter the type of program being sampled > ");
			String input = in.next();
			try {
				state = ProgramState.of(input);
			} catch (IllegalArgumentException e) {
				System.err.println(e.getLocalizedMessage());
			}
		}
		System.out.println("Categorizing the Program State as " + state.toString());
		return state;
	}
	*/

}
