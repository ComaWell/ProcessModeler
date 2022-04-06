package us.conian;

import java.text.*;
import java.util.*;

import be.ac.ulg.montefiore.run.jahmm.Observation;

public class SampleObservation extends Observation {
	
	private final Sample sample;
	
	public SampleObservation(Sample sample) {
		if (sample == null)
			throw new NullPointerException();
		this.sample = sample;
	}

	@Override
	public String toString(NumberFormat numberFormat) {
		StringJoiner sj = new StringJoiner(";");
		for (Sample.Reading reading : sample) {
			sj.add(numberFormat.format(n));
		}
		return sj.toString();
	}

}
