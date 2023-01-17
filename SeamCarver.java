import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
 
import java.util.Arrays;
 
public class SeamCarver {
 
    private Picture picture; // original picture
 
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }
 
    // current picture
    public Picture picture() {
        Picture copy = new Picture(picture);
        return copy;
    }
 
    // width of current picture
    public int width() {
        return picture.width();
    }
 
    // height of current picture
    public int height() {
        return picture.height();
    }
 
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width() || y < 0 || y > height())
            throw new IllegalArgumentException();
 
        int width = width();
        int height = height();
 
        int prevRow = (y + height - 1) % height;
        int prevCol = (x + width - 1) % width;
        int nextRow = (y + height + 1) % height;
        int nextCol = (x + width + 1) % width;
 
 
        double gradiantx = energyGradiant(prevCol, y, nextCol, y);
        double gradianty = energyGradiant(x, prevRow, x, nextRow);
 
        return Math.sqrt(gradiantx + gradianty);
    }
 
    // helps calculate energy of pixels.
    private double energyGradiant(int x1, int y1, int x2, int y2) {
        int rgb1 = picture.getRGB(x1, y1);
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = (rgb1) & 0xFF;
 
        int rgb2 = picture.getRGB(x2, y2);
        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >> 8) & 0xFF;
        int b2 = (rgb2) & 0xFF;
 
        int r = r2 - r1;
        int g = g2 - g1;
        int b = b2 - b1;
 
        double result = Math.pow(r, 2) + Math.pow(g, 2) + Math.pow(b, 2);
        return result;
    }
 
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture copy = new Picture(picture);
        picture = transpose(picture);
        int[] seam = findVerticalSeam();
        picture = copy;
        return seam;
    }
 
    // tranpose given image
    private Picture transpose(Picture pic) {
        Picture result = new Picture(pic.height(), pic.width());
        for (int col = 0; col < pic.width(); col++) {
            for (int row = 0; row < pic.height(); row++) {
                // switch pixels
                result.setRGB(row, col, pic.getRGB(col, row));
            }
        }
        return result;
    }
 
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int width = width();
        int height = height();
 
        double[][] energy = new double[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                energy[i][j] = energy(i, j);
 
        double[][] distTo = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
 
        int[][] edgeTo = new int[width][height];
        int[] result = new int[height];
 
        // initialize top row in distTo array
        for (int col = 0; col < width; col++)
            distTo[col][0] = energy[col][0];
 
        // start at second row, relax with upper entries
        for (int row = 1; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (col > 0)
                    relax(col - 1, row - 1, col, row, distTo, edgeTo, energy); 
                relax(col, row - 1, col, row, distTo, edgeTo, energy);
                if (col < width - 1)
                    relax(col + 1, row - 1, col, row, distTo, edgeTo, energy);
            }
        }
 
        // find minimum of bottom row of distTo, and use edgeTo to find path upwards
        double minimum = Double.POSITIVE_INFINITY;
        int champCol = 0;
        for (int col = 0; col < width; col++) {
            if (distTo[col][height - 1] < minimum) {
                minimum = distTo[col][height - 1];
                champCol = col;
            }
        }
 
        int[] temp = new int[height]; // x-coordinates in reverse order
        int i = 0;
        int row = height;
        int col = champCol;
        while (row > 0) {
            temp[i] = col;
            row--;
            col = edgeTo[col][row];
            i++;
        }
 
        int count = 0;
        for (int k = height - 1; k >= 0; k--) {
            result[count] = temp[k];
            count++;
        }
 
        return result;
    }
 
    // finds shortest paths (lowest energy) between pixels
    private void relax(int col1, int row1, int col2, int row2,
                       double[][] distTo, int[][] edgeTo, double[][] energy) {
        // pixel in lower row
        double energy2 = energy[col2][row2];
 
        if (distTo[col2][row2] > distTo[col1][row1] + energy2) {
            distTo[col2][row2] = distTo[col1][row1] + energy2;
            edgeTo[col2][row2] = col1;
        }
    }
 
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (height() == 1) throw new IllegalArgumentException();
        if (seam.length != width()) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) throw new
                    IllegalArgumentException();
        }
 
        picture = transpose(picture);
        removeVerticalSeam(seam);
        picture = transpose(picture);
 
    }
 
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (width() == 1) throw new IllegalArgumentException();
        if (seam.length != height()) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) throw new
                    IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++)
            if (seam[i] < 0 || seam[i] > width()) throw new IllegalArgumentException();
 
        // make a new picture with width w-1, copy everything over and skip over
        // the pixels in given vertical seam
        Picture copy = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            int count = 0;
            for (int col = 0; col < width(); col++) {
                if (col != seam[row]) {
                    copy.setRGB(count, row, picture.getRGB(col, row));
                    count++;
                }
            }
        }
        picture = copy;
    }
 
 
    //  unit testing (required)
    public static void main(String[] args) {
        Picture pic = new Picture("3x4.png");
        SeamCarver test = new SeamCarver(pic); 
        test.picture();
        StdOut.println("width 3? --> " + test.width());
        StdOut.println("height 4? --> " + test.height());
        StdOut.println("energy should be 228.09 --> " + test.energy(1, 2));
        StdOut.println("array1: " + Arrays.toString(test.findVerticalSeam()));
        int[] array1 = test.findVerticalSeam();
        StdOut.println("removes vertical seam from array1 from pic");
        test.removeVerticalSeam(array1);
        pic.show();
 
        StdOut.println("array2: " + Arrays.toString(test.findHorizontalSeam()));
        StdOut.println("removes horizontal seam from array2 from pic");
        int[] array2 = test.findHorizontalSeam();
        test.removeHorizontalSeam(array2);
        pic.show();
 
        Picture pic2 = SCUtility.randomPicture(16000, 2000);
        SeamCarver test2 = new SeamCarver(pic2);
        Stopwatch time = new Stopwatch();
        test2.removeVerticalSeam(test2.findVerticalSeam());
        test2.removeHorizontalSeam(test2.findHorizontalSeam());
        StdOut.println("elaped time: " + time.elapsedTime());
    }
 
}