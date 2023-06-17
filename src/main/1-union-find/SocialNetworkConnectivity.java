import edu.princeton.cs.algs4.StdIn;

/*
Question 1
Social network connectivity. Given
a social network containing n members and
a log file containing m timestamps
at which times pairs of members formed friendships, design an algorithm to determine the earliest time
at which all members are connected (i.e., every member is a friend of a friend of a friend ... of a friend).
Assume that the log file is sorted by timestamp and that friendship is an equivalence relation.
The running time of your algorithm should be m log n or better and use extra space proportional to n.
 */

class SocialNetworkConnectivity {

    public static void main(String[] args) {

        var n = StdIn.readInt();
        var ds = new SolutionWeightedQuickUnionPathCompression(n);

        while (!StdIn.isEmpty()) {
            var p = StdIn.readInt();
            var q = StdIn.readInt();
            var date = StdIn.readString();
            var time = StdIn.readString();

            if (!ds.connected(p, q)) {
                ds.union(p,q);

                int nodes = ds.count();
                if (nodes == 1) {
                    System.out.printf("--%n All members are connected at %s %s %n--%n", date, time);
                    break;
                }
            }
        }
    }
}

// in all cases It takes n^2 array accesses to process a sequence of N union commands on N objects
class SolutionQuickFind implements UF {

    private final int[] id;
    private int nodes;

    public SolutionQuickFind(int n) {
        id = new int[n];
        nodes = n;
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
    }

    @Override
    public boolean connected(int p, int q) {
        return id[p] == id[q];
    }

    // set all elements which have p's ids to q's ids
    // n^2
    @Override
    public void union(int p, int q) {
        var idP = id[p];
        var idQ = id[q];
        for (int i = 0; i < id.length; i++) {
            if (id[i] == idP) {
                id[i] = idQ;
            }
        }
        --nodes;
    }

    @Override
    public int count() {
        return nodes;
    }
}

// in worst case It takes n^2 array accesses to process a sequence of N union commands on N objects
class SolutionQuickUnion implements UF {

    private final int[] id;
    private int nodes;

    public SolutionQuickUnion(int n) {
        id = new int[n];
        nodes = n;
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
    }

    @Override
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    // set root of p to root of q
    // n^2 in worst case
    @Override
    public void union(int p, int q) {
        var rootP = root(p);
        var rootQ = root(q);
        id[rootP] = rootQ;
        --nodes;
    }

    @Override
    public int count() {
        return nodes;
    }

    private int root(int e) {
        while (id[e] != e) {
            e = id[e];
        }
        return e;
    }
}

// log * n
class SolutionWeightedQuickUnion implements UF {

    private final int[] id;
    private final int[] size;
    private int nodes;

    public SolutionWeightedQuickUnion(int n) {
        id = new int[n];
        size = new int[n];
        nodes = n;
        for (int i = 0; i < n; i++) {
            id[i] = i;
            size[i] = 1;
        }
    }

    @Override
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    // set lesser size root to greater size root, add size of lesser size to greater size root
    // log * n
    @Override
    public void union(int p, int q) {
        var rootP = root(p);
        var rootQ = root(q);
        if (rootP == rootQ) return;

        if (size[rootP] < size[rootQ]) {
            setRootAndAddSize(rootP, rootQ);
        } else {
            // size[rootP] > size[rootQ] || size[rootP] == size[rootQ]
            setRootAndAddSize(rootQ, rootP);
        }
        --nodes;
    }

    @Override
    public int count() {
        return nodes;
    }

    private int root(int e) {
        while (id[e] != e) {
            e = id[e];
        }
        return e;
    }

    private void setRootAndAddSize(int lesserRoot, int greaterRoot) {
        id[lesserRoot] = greaterRoot;
        size[greaterRoot] += size[lesserRoot];
    }
}

// log * n
class SolutionWeightedQuickUnionPathCompression implements UF {

    private final int[] id;
    private final int[] size;
    private int nodes;

    public SolutionWeightedQuickUnionPathCompression(int n) {
        id = new int[n];
        size = new int[n];
        nodes = n;
        for (int i = 0; i < n; i++) {
            id[i] = i;
            size[i] = 1;
        }
    }

    @Override
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    // set lesser size root to greater size root, add size of lesser size to greater size root
    // log * n
    @Override
    public void union(int p, int q) {
        var rootP = root(p);
        var rootQ = root(q);
        if (rootP == rootQ) return;

        if (size[rootP] < size[rootQ]) {
            setRootAndAddSize(rootP, rootQ);
        } else {
            // size[rootP] > size[rootQ] || size[rootP] == size[rootQ]
            setRootAndAddSize(rootQ, rootP);
        }
        --nodes;
    }

    @Override
    public int count() {
        return nodes;
    }

    private int root(int e) {
        while (id[e] != e) {
            id[e] = id[id[e]]; // path compression by halving
            e = id[e];
        }
        return e;
    }

    private void setRootAndAddSize(int lesserRoot, int greaterRoot) {
        id[lesserRoot] = greaterRoot;
        size[greaterRoot] += size[lesserRoot];
    }
}
