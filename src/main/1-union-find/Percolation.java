import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/*
       0
  /  / | \  \
 1  2  3  4  5
 6  7  8  9 10
11 12 13 14 15
16 17 18 19 20
21 22 23 24 25
  \  \ | /  /
      26
 */

public class Percolation {

    private final int n;
    private final WeightedQuickUnionUF grid;
    private final int top;
    private final int bottom;
    private final boolean[] openSite;
    private int numberOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        checkGridBoundaries(isValid(n));
        this.n = n;
        var gridSize = n * n;
        grid = new WeightedQuickUnionUF(gridSize + 2);
        top = 0;
        bottom = gridSize + 1;
        unionTop();
        unionBottom();

        openSite = new boolean[gridSize];
        numberOfOpenSites = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) return;

        var openedIdx = calculateGridIdx(row, col);
        openSite[openedIdx] = true;
        ++numberOfOpenSites;

        connectAdjacentSites(row, col, gridAdjust(openedIdx));
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkGridBoundaries(isValid(row, col));
        var idx = calculateGridIdx(row, col);
        return openSite[idx];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkGridBoundaries(isValid(row, col));
        var idx = calculateGridIdx(row, col);
        return openSite[idx] && grid.find(top) == grid.find(gridAdjust(idx));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return grid.find(top) == grid.find(bottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        var percolation = new Percolation(5);
        percolation.open(1, 1);
        percolation.open(2, 1);
        percolation.open(3, 1);
        percolation.open(4, 1);
        var percolatesTry1 = percolation.percolates();
        percolation.open(5, 1);
        var percolatesTry2 =percolation.percolates();
        assert !percolatesTry1;
        assert percolatesTry2;
    }

    private void unionTop() {
        for (int i = 1; i <= n; i++) {
            grid.union(top, i);
        }
    }

    private void unionBottom() {
        for (int i = bottom - 1; i >= bottom - n; i--) {
            grid.union(bottom, i);
        }
    }

    private void checkGridBoundaries(boolean isValid) {
        if (!isValid) throw new IllegalArgumentException();
    }

    private boolean isValid(int row, int col) {
        return row > 0 && col > 0 && row <= n && col <= n;
    }

    private boolean isValid(int n) {
        return n > 0;
    }

    private int calculateGridIdx(int row, int col) {
        return n * (row - 1) + col - 1;
    }

    private void connectAdjacentSites(int row, int col, int openedIdx) {
        var topRow = row - 1;
        var bottomRow = row + 1;
        var leftCol = col - 1;
        var rightCol = col + 1;
        union(topRow, col, openedIdx);
        union(bottomRow, col, openedIdx);
        union(row, leftCol, openedIdx);
        union(row, rightCol, openedIdx);
    }

    private void union(int row, int col, int openedIdx) {
        if (validateSite(row, col)) {
            var mergingIdx = calculateGridIdx(row, col);
            grid.union(gridAdjust(mergingIdx), openedIdx);
        }
    }

    private int gridAdjust(int idx) {
        return idx + 1;
    }

    private boolean validateSite(int row, int col) {
        return isValid(row, col) && isOpen(row, col);
    }
}
