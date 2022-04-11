package us.conian;

import java.util.*;

import be.ac.ulg.montefiore.run.distributions.MultiGaussianDistribution;
import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussian;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;

public class HmmUtils {

	public static Hmm<ObservationVector> generate(Map<String, List<SampleSet>> samples) {
		if (samples == null)
			throw new NullPointerException();
		int dimension = 0;
		List<List<ObservationVector>> s = new ArrayList<>();
		for (List<SampleSet> sets : samples.values()) {
			List<ObservationVector> l = new ArrayList<>();
			for (SampleSet set : sets) {
				set = set.minusDeadSamples();
				if (!set.meta().isComplete())
					throw new IllegalStateException("Sample not complete for " + set.counterName());
				Sample mean = set.meta().meanSample().minusMetaReadings();
				if (dimension == 0)
					dimension = mean.numReadings();
				else if (mean.numReadings() != dimension)
					throw new IllegalStateException("Mismatched dimension. Expected " + dimension + ", received " + mean.numReadings());
				l.add(new ObservationVector(mean.asVector()));
			}
			s.add(l);
			/*
			for (SampleSet set : sets) {
				set = set.minusDeadSamples();
				if (!set.meta().isComplete())
					throw new IllegalStateException();
				if (set.meta().maxNumReadings() > dimension)
					dimension = set.meta().maxNumReadings();
				List<ObservationVector> l = new ArrayList<>();
				for (Sample sample : set) {
					l.add(new ObservationVector(sample.minusMetaReadings().asVector()));
				}
				s.add(l);
			}
			*/
		}
		int numStates = samples.size();
		
		
		KMeansLearner<ObservationVector> learner = new KMeansLearner<>(
				numStates,
				new OpdfMultiGaussianFactory(dimension),
				s
				);
		return learner.learn();
	}
	
}
