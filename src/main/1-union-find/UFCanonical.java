/*
Question 2
Union-find with specific canonical element. Add a method find() to the union-find data type
so that find(i) returns the largest element in the connected component containing i.
The operations, union(), connected(), and find() should all take logarithmic time or better.

For example, if one of the connected components is {1,2,6,9},
then the find() method should return 9 for each of the four elements in the connected components.
{1,2,6,9}
find(1) -> 9
find(2) -> 9
find(6) -> 9
find(9) -> 9
 */

import edu.princeton.cs.algs4.StdIn;

class UFCanonical {

    public static void main(String[] args) {
        var n = StdIn.readInt();
        var ds = new SolutionUFCanonical(n);

        while (!StdIn.isEmpty()) {
            var p = StdIn.readInt();
            var q = StdIn.readInt();

            if (!ds.connected(p, q)) {
                ds.union(p,q);
            }
        }
    }
}

class SolutionUFCanonical implements UF {

    private int nodes;
    private final int[] id;
    private final int[] size;
    private final int[] maxId;

    public SolutionUFCanonical(int n) {
        nodes = n;
        id = new int[n];
        size = new int[n];
        maxId = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
            size[i] = 1;
            maxId[i] = i;
        }
    }

    public int find(int i) {
        var rootI = root(i);
        return maxId[rootI];
    }

    @Override
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    @Override
    public void union(int p, int q) {
        var rootP = root(p);
        var rootQ = root(q);
        if (rootP == rootQ) return;

        var max = (p < q) ? q : p;
        if (size[rootP] < size[rootQ]) {
            setRootAndAddSize(rootP, rootQ);
            maxId[rootQ] = max;
        } else {
            setRootAndAddSize(rootQ, rootP);
            maxId[rootP] = max;
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

class UFCanonicalTest {
    public static void main(String[] args) {
        var ds = new SolutionUFCanonical(10);

        int[] pair1 = {1, 2};
        int[] pair2 = {6, 9};
        int[] pair3 = {1, 9};
        int[][] input = {pair1, pair2, pair3};

        for (int i = 0; i < input.length; i++) {
            var pair = input[i];
            var p = pair[0];
            var q = pair[1];

            if (!ds.connected(p, q)) {
                ds.union(p,q);
            }
        }

        var i1 = ds.find(1);
        var i2 = ds.find(2);
        var i6 = ds.find(6);
        var i9 = ds.find(9);
        assert i1 == 9;
        assert i2 == 9;
        assert i6 == 9;
        assert i9 == 9;
    }
}
