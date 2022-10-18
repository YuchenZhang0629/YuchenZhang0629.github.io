from __future__ import annotations

import sys
import numpy as np
import collections

from utils import (
    Edit,
    Addition,
    Change,
    Deletion,
    read_file_contents,
    diffstr_normal,
    check_edits,
)


def diff(str1: str, str2: str) -> list[Edit]:
    """Finds a diff between two strings.

    Parameters
    ----------
    str1 : str
        First string to compare, with lines separated by newline (\n) characters.
    str2 : str
        Second string to compare, with lines separated by newline (\n) characters,

    Returns
    -------
    list[Edit]
        A list of edits required to transform str1 to str2.
        An Edit must be an Addition, Deletion, or Change defined by
        the starting and ending lines in each string.
        The edits should be in the same order as they would appear
        in the diff output.

    Examples
    ------
    We use the Addition, Deletion, and Change classes defined in utils.py
    to represent an edit. All three can be instantiated by providing the numbers
    that go before/after the letter in a single edit. An edit's string
    representation is the same format that appears in a diff output string.

    >>> addition = Addition(2, (2, 3))
    >>> change = Change((6, 9), 7)
    >>> deletion = Deletion(1, 0)
    >>> edits = [deletion, addition, change]
    >>> edits
    [1d0, 2a2,3, 6,9c7]

    Applying our list of edits to str1 should produce str2.
    >>> str1 = read_file_contents("example_original.txt")
    >>> str2 = read_file_contents("example_new.txt")
    >>> diff_edits = diff(str1, str2)
    >>> check_edits(str1, str2, diff_edits)
    True

    We can use the diffstr_normal helper function to compare our list of edits
    to the ones produced by the real diff tool. Note that there may be slight
    differences if there are multiple ways to minimize the number of modified lines.

    >>> print(diffstr_normal(str1, str2, diff_edits))
    1d0
    < # TODO: Refactor and add a docstring.
    2a2,3
    >     '''Calculate the nth Fibonacci number.
    >     '''
    6,9c7
    <         if i % 2 == 1:
    <             fib_nums[1] = fib_nums[0] + fib_nums[1]
    <         else:
    <             fib_nums[0] = fib_nums[0] + fib_nums[1]
    ---
    >         fib_nums[i % 2] = fib_nums[0] + fib_nums[1]

    If you want to add your own tests, just add new lines to the docstring below.
    The doctest module will treat lines prefixed with ">>>" as inputs to a
    Python REPL. If a line produces some output, doctest will check that the output
    is equal to the value specified on the line(s) below (without a ">>>");
    if all output matches the expected values, doctest will exit without any errors.
    Doctest will parse out leading whitespace if it is consistent across lines.

    >>> test_abc_input_1 = read_file_contents("abc_original.txt")
    >>> test_abc_input_2 = read_file_contents("abc_new.txt")
    >>> test_abc_output = diff(test_abc_input_1, test_abc_input_2)
    >>> check_edits(test_abc_input_1, test_abc_input_2, test_abc_output)
    True

    >>> print(diffstr_normal(test_abc_input_1, test_abc_input_2, test_abc_output))
    4a5
    > e
    7c8
    < h
    ---
    > i
    9c10,13
    < q
    ---
    > k
    > r
    > x
    > y

    """
    lines1: list[str] = str1.splitlines()
    lines2: list[str] = str2.splitlines()

    # First off: creating a DP table that tracks the edits
    dp = np.zeros((len(lines1)+1,len(lines2)+1)).astype(int)
    # Now we find the minimum edit distance
    for i in range(len(dp)):
        dp[i][0] = i
    for i in range(len(dp[0])):
        dp[0][i] = i
    for i in range(1,len(dp)):
        for j in range(1,len(dp[0])):
            if lines1[i-1] == lines2[j-1]:
                dp[i][j] = dp[i-1][j-1]
            else:
                dp[i][j] = min(dp[i-1][j-1]+2,dp[i][j-1]+1,dp[i-1][j]+1)
    # Now we start from the lower right corner and trace back
    
    curr = (len(dp)-1,len(dp[0])-1)
    path = [curr]
    directions = [(-1,0),(-1,-1),(0,-1)]
    while curr != (0,0):
        dict_temp = {}
        for dx,dy in directions:
            dict_temp[(curr[0]+dx,curr[1]+dy)] = dp[curr[0]+dx][curr[1]+dy]
        nxt = min(dict_temp, key=dict_temp.get)
        path.append(nxt)
        curr = nxt
    path = collections.deque(path[::-1])

    ans = []
    while len(path) > 1:
        # When there are matches: pop until encountering a change
        if (len(path) > 1 and path[1][1]-path[0][1] == 1 and path[1][0]-path[0][0] == 1 
            and dp[path[1][0]][path[1][1]] == dp[path[0][0]][path[0][1]]):
            while (len(path) > 1 and path[1][1]-path[0][1] == 1 and path[1][0]-path[0][0] == 1 
                   and dp[path[1][0]][path[1][1]] == dp[path[0][0]][path[0][1]]):
                path.popleft()
        else:
            start = path[0]
            while len(path) > 1 and not (path[1][1]-path[0][1] == 1 and path[1][0]-path[0][0] == 1 
                       and dp[path[1][0]][path[1][1]] == dp[path[0][0]][path[0][1]]):
                path.popleft()
            end = path[0]
            # This corresponds to an Add Operation
            if start[0] == end[0]:
                ans.append(Addition(start[0],(start[1]+1,end[1])) if start[1]+1 != end[1] else Addition(start[0],end[1]))
            # This is a Delete Operation
            elif start[1] == end[1]:
                ans.append(Deletion(start[0],(start[1]+1,end[1])) if start[1]+1 != end[1] else Addition(start[0],end[1]))
            # This is a Change Operation
            else:
                c1 = (start[0]+1,end[0]) if start[0]+1 != end[0] else end[0]
                c2 = (start[1]+1,end[1]) if start[1]+1 != end[1] else end[1]
                ans.append(Change(c1,c2))
    
    return ans

if __name__ == "__main__":
    if len(sys.argv) == 3:
        str1 = read_file_contents(sys.argv[1])
        str2 = read_file_contents(sys.argv[2])
        edits = diff(str1, str2)
        print(diffstr_normal(str1, str2, edits, color=True))
    else:
        import doctest

        doctest.testmod()
