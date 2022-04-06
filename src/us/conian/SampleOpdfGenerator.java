package us.conian;

public class SampleOpdfGenerator {
	
	/* Note: When doing calculations, particularly when division is involved,
	 * we want to always be using doubles so that the results aren't being
	 * truncated. Regardless of the type of Number, we can safely use
	 * Number#doubleValue(), and return the results as doubles.
	 */
	
	private static double add(Number a, Number b) {
		if (a == null || b == null)
			throw new NullPointerException();
		return a.doubleValue() + b.doubleValue();
	}
	
	private static double divide(Number a, Number b) {
		if (a == null || b == null)
			throw new NullPointerException();
		return a.doubleValue() / b.doubleValue();
	}
	
	/*
	public static Number addNumbers(Number a, Number b) {
	    if(a instanceof Double || b instanceof Double) {
	        return a.doubleValue() + b.doubleValue();
	    } else if(a instanceof Float || b instanceof Float) {
	        return a.floatValue() + b.floatValue();
	    } else if(a instanceof Long || b instanceof Long) {
	        return a.longValue() + b.longValue();
	    } else {
	        return a.intValue() + b.intValue();
	    }
	}
	
	public static Number divideNumbers(Number a, Number b) {
	    if(a instanceof Double || b instanceof Double) {
	        return a.doubleValue() / b.doubleValue();
	    } else if(a instanceof Float || b instanceof Float) {
	        return a.floatValue() / b.floatValue();
	    } else if(a instanceof Long || b instanceof Long) {
	        return a.longValue() / b.longValue();
	    } else {
	        return a.intValue() / b.intValue();
	    }
	}
	*/
	
	public ArrayList<Number> getMean(ArrayList<Map<String,Number>> programData) {
		
		//Start with a List of Maps(Dictionaries)
		
		
		ArrayList<Number> means = new ArrayList<Number>();
		//Use if error gets thrown duringnew_val due to means needing to be initialized first
		//ArrayList<Number> means = new ArrayList<Number>(Collections.nCopies(programData.get(0).size(), 0));
				
		//Loops list on number of maps
		for (int i = 0; i < programData.size(); i++) {
			
			//temporarily stores a map
			Map<String,Number> sample = programData.get(i);
			
			
			//Sort Map
			//Code for sorting map
			
			//Loops along the map values, adding them to means
			Iterator<Map.Entry<String, Number>> itr = sample.entrySet().iterator();
			
			int count = 0;
			
			while(itr.hasNext())
	        {
	             Map.Entry<String, Number> entry = itr.next();
	             Number value = entry.getValue();
	             Number new_val = addNumbers(means.get(count),value);
	             means.set(count, new_val);
	             count++;
	        }
			
		}
		
		//changes value from a total to a mean
		for (int i = 0; i < means.size(); i++) {
			means.set(i, divideNumbers(means.get(i), means.size()));
		}
		
		return means;
	}
	
	public ArrayList<ArrayList<Number>> getCovMatrix(ArrayList<Map<String,Number>> programData){
		ArrayList<ArrayList<Number>> matrix = new ArrayList<>();
		
		ArrayList<Number> means = getMean(programData);
		
		
		
		return matrix;
	}
}