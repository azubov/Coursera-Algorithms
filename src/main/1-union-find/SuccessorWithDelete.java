/*
Successor with delete. Given a set of n integers S={0,1,...,n−1} and a sequence of requests of the following form:
-Remove x from S
-Find the successor of x: the smallest y in S such that y≥x.
design a data type so that all operations (except construction) take logarithmic time or better in the worst case.
 */

class SuccessorWithDeleteTest {

    public static void main(String[] args) {

        var ds = new SuccessorWithDelete(10);

        assert ds.successor(4) == 5;
        ds.remove(5);
        ds.remove(6);
        assert ds.successor(4) == 7;

        assert ds.successor(1) == 2;
        ds.remove(2);
        ds.remove(3);
        ds.remove(4);
        assert ds.successor(1) == 7;
    }
}

class SuccessorWithDelete implements UF {

    private final int[] id;
    private final int[] size;
    private final int[] nodeArray;
    private int nodes;

    public SuccessorWithDelete(int n) {
        id = new int[n];
        size = new int[n];
        nodeArray = new int[n];
        nodes = n;
        for (int i = 0; i < n; i++) {
            id[i] = i;
            size[i] = 1;
            nodeArray[i] = i;
        }
    }

    public void remove(int x) {
        union(x, x + 1);
    }

    public int successor(int x) {
        return nodeArray[x + 1];
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

        if (size[rootP] < size[rootQ]) {
            setRootAndAddSize(rootP, rootQ);
            nodeArray[rootQ] = nodeArray[rootP];
        } else {
            setRootAndAddSize(rootQ, rootP);
            nodeArray[rootP] = nodeArray[rootQ];
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

