package seamfinding;

import seamfinding.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        int width = picture.width();
        int height = picture.height();

        double[][] dp = new double[width][height];

        for(int y = 0; y < height; y++) {
            dp[0][y] = f.apply(picture, 0, y);
        }

        for(int x = 1; x < width; x++) {
            for(int y = 0; y < height; y++) {

                double bestPrev = dp[x - 1][y];

                if (y > 0) {
                    bestPrev = Math.min(bestPrev, dp[x - 1][y - 1]);
                }
                if (y < height - 1) {
                    bestPrev = Math.min(bestPrev, dp[x - 1][y + 1]);
                }

                dp[x][y] = bestPrev + f.apply(picture, x, y);
            }
        }

        int minY = 0;
        double minCost = dp[width - 1][0];

        for ( int y = 1; y < height; y++) {
            if (dp[width - 1][y] < minCost) {
                minCost = dp[width - 1][y];
                minY = y;
            }
        }

        List<Integer> seam = new ArrayList<>();
        seam.add(minY);

        int currY = minY;

        for(int x = width - 1; x > 0; x--) {
            int bestY = currY;
            double bestPrev = dp[x - 1][currY];

            if(currY > 0 && dp[x - 1][currY - 1] < bestPrev) {
                bestPrev = dp[x - 1][currY - 1];
                bestY = currY - 1;
            }

            if(currY < height - 1 && dp[x - 1][currY + 1] < bestPrev) {
                bestPrev = dp[x - 1][currY + 1];
                bestY = currY + 1;
            }

            currY = bestY;
            seam.add(currY);
        }
        Collections.reverse(seam);
        return seam;
    }
}
