# `diff`

[`diff`](https://en.wikipedia.org/wiki/Diff) is a command-line utility found on Unix and Unix-like operating systems (e.g. MacOS, Linux) that compares two files to each other.
It is typically used to identify changes between two versions of the same file, making it a critical component of modern source control systems such as `git`.

`diff` takes two files as input (`diff file1 file2`) and outputs a string (also called a "diff") describing how to transform `file1` to `file2` through a series of line additions, deletions, and changes.

As an example, take the following two files, both of which are Python programs implementing a dynamic programming algorithm to calculate the *n*th Fibonacci number. The first is an early draft of the file, and the second is a revised version.


<table>
<tr>
<th>
</th>
<th>

`original.py`
</th>
<th>
</th>
<th>

`new.py`
</th>
</tr>
<tr>
<td style="vertical-align: top, margin: 0px, padding: 0px">

```
 1
 2
 3
 4
 5
 6
 7
 8
 9
10
```

</td>
<td style="vertical-align: top, margin: 0px, padding: 0px">

```python
# TODO: Refactor and add a docstring.
def fibonacci(n: int) -> int:
    assert n >= 0
    fib_nums = [0, 1]
    for i in range(2, n + 1):
        if i % 2 == 1:
            fib_nums[1] = fib_nums[0] + fib_nums[1]
        else:
            fib_nums[0] = fib_nums[0] + fib_nums[1]
    return fib_nums[n % 2]
```

</td>
<td style="vertical-align: top, margin: 0px, padding: 0px">

```
 1
 2
 3
 4
 5
 6
 7
 8
```

</td>
<td style="vertical-align: top, margin: 0px, padding: 0px">

```python
def fibonacci(n: int) -> int:
    """Calculate the nth Fibonacci number.
    """
    assert n >= 0
    fib_nums = [0, 1]
    for i in range(2, n + 1):
        fib_nums[i % 2] = fib_nums[0] + fib_nums[1]
    return fib_nums[n % 2]
```

</td>
</tr>
</table>

Below is the output of the terminal command `diff original.py new.py`. Each modification is described using line numbers from the two files and a letter representing the type of modification: `a` for addition, `d` for deletion, and `c` for change (deletion followed by addition).

```
1d0
< # TODO: Refactor and add a docstring.
2a2,3
>     """Calculate the nth Fibonacci number.
>     """
6,9c7
<         if i % 2 == 1:
<             fib_nums[1] = fib_nums[0] + fib_nums[1]
<         else:
<             fib_nums[0] = fib_nums[0] + fib_nums[1]
---
>         fib_nums[i % 2] = fib_nums[0] + fib_nums[1]
```

- `1d0` indicates that line 1 in `original.py` was deleted; the next line comes *after* line 0 in `new.py`.
- `2a2,3` indicates an addition *after* line 2 in `original.py`; the newly inserted lines are lines 2-3 in `new.py`.
- `6,9c7` indicates that lines 6-9 in `original.py` have been changed to line 7 in `new.py`.

## Assignment
`diff` produces a set of modifications that minimizes the number of modified **lines** (*not* the number of modified characters!) by solving the *longest common subsequence problem*. By identifying the longest sequence of lines common to both files, we can take all the lines that are *not* common and treat them as our set of modifications. Note that there may be multiple ways to create a diff that minimizes the number of modified lines.

Your task is to identify this set of edits when given two strings, an original version (`str1`) and a new version (`str2`).
Complete the function marked `TODO` in `diff.py` using a dynamic programming algorithm. Your algorithm must run in `O(nm)` time , where `n` and `m` are the number of characters in `str1` and `str2` respectively.

## Testing
Two test cases are provided in the docstring of the function you need to implement, but passing them will not guarantee full credit. These [doctest](https://docs.python.org/3/library/doctest.html)s are runnable by executing the `diff.py` file with no arguments:
```
$ python3 diff.py
```
If all tests pass, this command should produce no output. You can add additional test cases by modifying the docstring.

In addition, you can also execute `diff.py` with two arguments to mimic the behavior of the actual diff tool:
```
$ python3 diff.py file1 file2
```
If implemented correctly, the terminal output should be identical to running `diff file1 file2` on a Mac or Linux machine.
