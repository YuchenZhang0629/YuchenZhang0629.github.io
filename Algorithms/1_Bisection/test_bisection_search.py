from math import exp, log10
import re
from typing import Callable

import pytest

from bisection_search import bisect_iterative, bisect_recursive


def func1(x: float) -> float:
    return x**3 - 4 * x**2 + 6 * x - 24


def hyperbolic_tangent(x: float) -> float:
    return (exp(x) - exp(-x)) / (exp(x) + exp(-x))


@pytest.mark.parametrize("bisect_func", [bisect_iterative, bisect_recursive])
def test_input_validation(bisect_func: Callable[[Callable[[float], float], float, float], float]):
    with pytest.raises(AssertionError, match="b must be greater than a!"):
        bisect_func(func1, 2, 1)

    expected_opposite_sign_error_msg = re.escape("f(a) and f(b) must have opposite sign!")
    with pytest.raises(AssertionError, match=expected_opposite_sign_error_msg):
        bisect_func(func1, 1, 2)
    with pytest.raises(AssertionError, match=expected_opposite_sign_error_msg):
        bisect_func(func1, 11, 12)


@pytest.mark.parametrize("bisect_func", [bisect_iterative, bisect_recursive])
@pytest.mark.parametrize(
    "f, a, b, tolerance",
    [
        pytest.param(func1, 2.0, 10.0, 1e-8, id="[x^3 - 4x^2 + 6x - 24] Start on right"),
        pytest.param(func1, 2.0, 4.5, 1e-8, id="[x^3 - 4x^2 + 6x - 24] Start on left"),
        pytest.param(func1, 2.0, 10.0, 1e-12, id="[x^3 - 4x^2 + 6x - 24] Low tolerance"),
        pytest.param(func1, 2.0, 10.0, 1e-3, id="[x^3 - 4x^2 + 6x - 24] High tolerance"),
        pytest.param(log10, 0.1, 1e8, 1e-8, id="log10"),
        pytest.param(hyperbolic_tangent, -10, 50, 1e-8, id="tanh"),
    ],
)
def test_bisection_search(
    bisect_func: Callable[[Callable[[float], float], float, float, float], float],
    f: Callable[[float], float],
    a: float,
    b: float,
    tolerance: float,
):
    result = bisect_func(f, a, b, tolerance)
    assert abs(f(result)) <= tolerance
