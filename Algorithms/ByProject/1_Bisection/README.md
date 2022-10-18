## Bisection Search

Given an arbitrary function *f* which takes a real number and outputs a real number,
as well two values *a* and *b* such that:
- *f* is continuous over the interval *[a, b]*
- *f(a)* and *f(b)* have opposite sign

we can find some root *x* such that *f(x) ≈ 0* (within some tolerance) 
using the same idea behind binary search.
This is known as bisection search, or the 
[bisection method](https://en.wikipedia.org/wiki/Bisection_method).

Implement this algorithm in `bisection_search.py`. 
As with binary search, the bisection method can be implemented using either iteration or recursion.
Please complete both provided function signatures: `bisect_iterative` and `bisect_recursive`.

### Unit Tests

Several unit tests are provided in `test_bisection_search.py`.
You can run them using [pytest](https://docs.pytest.org/en/7.1.x/).
Follow the installation instructions and then run `python3 -m pytest` in this directory.

The provided test cases are not necessarily exhaustive, 
and passing them all is not a guarantee of full credit. 
You are welcome to write your own test cases.
