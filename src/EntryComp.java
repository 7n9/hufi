import java.util.Comparator;

public class EntryComp implements Comparator<Entry>{
    public int compare(Entry x, Entry y) 
	{ 
		return x.freq - y.freq; 
	} 
}
