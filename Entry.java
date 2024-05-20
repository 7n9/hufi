public class Entry implements Comparable<Entry>{
    char c;
    int freq;

    Entry(){}

    Entry(Character c, int f){
        this.c = c;
        this.freq = f;
    }

    @Override
    public int compareTo(Entry e) {
        return (int) this.freq - e.freq;
    }

    public String print(){
        return c + " | " + freq;
    }

    Entry left;
    Entry right;
}