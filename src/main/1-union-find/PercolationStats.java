import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_INTERVAL = 1.96;

    private final int trials;
    private final double[] percolationResults;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validate(n, trials);
        this.trials = trials;
        percolationResults = new double[trials];

        for (int i = 0; i < trials; i++) {
            var percolation = new Percolation(n);

            while (!percolation.percolates()) {
                var row = StdRandom.uniformInt(n) + 1;
                var col = StdRandom.uniformInt(n) + 1;

                percolation.open(row, col);
            }

            var openSites = percolation.numberOfOpenSites();
            var gridSize = (double) (n * n);
            percolationResults[i] = openSites / gridSize;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percolationResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percolationResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_INTERVAL * stddev()) / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_INTERVAL * stddev()) / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {

        var n = Integer.parseInt(args[0]);
        var t = Integer.parseInt(args[1]);

        var percolationStats = new PercolationStats(n, t);
        System.out.printf("mean                    = %s%n", percolationStats.mean());
        System.out.printf("stddev                  = %s%n", percolationStats.stddev());
        System.out.printf("95%% confidence interval = [%s, %s]%n", percolationStats.confidenceLo(), percolationStats.confidenceHi());
    }

    private void validate(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
