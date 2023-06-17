import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champ = "";
        int i = 0;
        while (!StdIn.isEmpty()) {
            ++i;
            String challenger = StdIn.readString();

            if (StdRandom.bernoulli((double) 1 / i)) {
                champ = challenger;
            }
        }
        StdOut.print(champ);
    }
}

// java-algs4 RandomWord
// heads <Enter> tails <enter> <ctrl + d>
// java-algs4 RandomWord < animals8.txt
// Run/Debug Configurations -> Modify Options -> Redirect input from: src/main/resources/animals8.txt
