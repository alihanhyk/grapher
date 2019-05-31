# GRAPHER

Grapher, as the name implies, is a graphing software. It can plot any equation or any inequality composed of:

- Arithmetic operations (+, -, *, /),
- Parenthesis,
- Exponential function (^),
- Logarithmic functions (ln, log),
- Trigonometric functions (sin, cos, tan, asin, acos, atan),
- Comparisons (max, min, =, <, >),
- Absolute value function (abs),
- Mathematical constants (e, p).

It is possible to draw multiple graphs at once with different colors, move and zoom those graphs, and save them as an image file.

## Syntax

- The variables are x and y.
- There is an operator priority:
    1. Parenthesis and functions,
    2. Exponentiation (^),
    3. Multiplication (*),
    4. Division (/),
    5. Addition and subtraction (+, -),
    6. Equalities and inequalities (=, <, >).
- Multiplication operators cannot be omitted, i.e., 2x is not a valid input while 2*x is.
- Function parentheses cannot be omitted, e.g., sin(x). Multiple arguments must be separated by semicolons, e.g., log(10;x).

## Marching Squares

In its current state, Grapher draws graphs on a pixel by pixel basis by evaluating the input expression for each individual pixel. A significant improvement to this approach would be using marching squares ([Marching Squares Wikipedia Page](https://en.wikipedia.org/wiki/Marching_squares)), which is an algorithm to approximate contours for two-dimensional scalar fields that are sampled in a grid pattern. With this new approach, evaluating the input expression only for a grid of pixels would be enough.
