from typing import Callable


def bisect_iterative(f: Callable[[float], float], a: float, b: float, tolerance: float = 1e-8):
    assert b > a, "b must be greater than a!"
    f_a = f(a)
    f_b = f(b)
    assert f_a * f_b < 0, "f(a) and f(b) must have opposite sign!"
    while True:
        mid = (a + b) / 2
        f_mid = f(mid)
        if abs(f_mid) < tolerance:
            return mid
        if f_mid * f_a > 0:
            a = mid
        else:
            b = mid


def bisect_recursive(f: Callable[[float], float], a: float, b: float, tolerance: float = 1e-8):
    assert b > a, "b must be greater than a!"
    f_a = f(a)
    f_b = f(b)
    assert f_a * f_b < 0, "f(a) and f(b) must have opposite sign!"
    def iter(func,a,b,tol):
        if abs(func(a)) < tol:
            return a
        if abs(func(b)) < tol:
            return b
        if func((a+b)/2)*f(a) > 0:
            return iter(f,(a+b)/2,b,tol)
        return iter(f,a,(a+b)/2,tol)
    return iter(f,a,b,tolerance)
    