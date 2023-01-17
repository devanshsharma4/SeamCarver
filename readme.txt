Programming Assignment 7: Seam Carving
 
 
/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */
findVerticalSeam: First store the width and height of the picture in local
variables. Initialize a double array called energy with width and height of
picture. Iterate through every pixel in the picture, giving each pixel its
correspoding energy in the energy double array. In a double array that stores
the distance from one pixel to another, set all the distances to infinity.
Store the respective energies of the pixels in the top row in distTo array.
Starting at
the second row, relax upper entries. Then find minimum of bottom row using the distTo
array and use the edgeTo array to find the path upwards of the vertical seam.
Store the corresponding x-values in temp array; using result array, store x-values
in reverse order so that they start from the top row. Return the result
array.
 
findHorizontalSeam: First we create a copy of the picture instance variable.
Then tranpose the picture instance variable and store the vertical seam to be
removed from the picture in an array called seam. Set picture instance variable
to be the same as the copy picture so that the picture instance variable
is the same as before (the original). Finally, return the array called seam.
 
 
/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */
An image is suitable to the seam-carving approach if it has general distinctions 
between "important" items (high energy) in the image and less important items 
(less energy). For example, a picture of the ocean with people surfing is a good
picture to seam-carve because the ocean has low energy pixels so removing
some of these "ocean" pixels is not noticeable. On the other hand, if a picture
has pixels with high energy levels all around, then the seam-carving approach
will not work well because removing seams from the image will be noticeable.
 
/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */
 
(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2
 
 H           time (seconds)      ratio       log ratio
------------------------------------------------------
500             0.363
1000            0.923           5.733       2.519
2000            1.523           1.65        0.722
4000            3.069           2.02        1.014
8000            7.411           2.414       1.270
16000           15.77           2.128       1.089
32000           40.571          2.572       1.363
 
 
(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2
 
 W           time (seconds)      ratio       log ratio
------------------------------------------------------
500          0.371
1000         0.704              1.898        0.924
2000         1.496              2.125        1.088
4000         2.836              1.896        0.923
8000         7.351              2.592        1.374
16000        16.878             2.296        1.991
32000        39.019             2.311        1.209
 
 
 
/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */
 
 
Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:
 
 
    ~  H^1.3 * W^1.3 * 2.9x10^-5 * 1.4x10^-4 
       _______________________________________
 
To find the exponents of H and W, we averaged the log ratio for both tables, which
is the value of b. To find a, we set up the equation running time = a*n^b, and solved
for a, so a = (running time) / n^b. We did this for the last row in each table
and multiplied both values together to get our value of a.
 
 
 
/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
NONE
 
/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */
Lab Ta's on Saturday, Sunday, and Monday
Monday office hours
 
/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
NONE
 
/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */
 
We both contributed to writing and debugging the code.
 