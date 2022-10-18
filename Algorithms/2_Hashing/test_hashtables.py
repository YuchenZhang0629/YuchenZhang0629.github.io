from __future__ import annotations

import itertools
from collections import deque
from typing import Iterable, TypeVar

import pytest

from chaining import ChainingHashTable
from hashtable import BLANK
from open_addressing import LinearProbingHashTable, QuadraticProbingHashTable

T = TypeVar("T")


def take(n: int, iterable: Iterable[T]) -> list[T]:
    "Return first n items of the iterable as a list"
    return list(itertools.islice(iterable, n))


class TestLinearProbing:
    def test_generate_indices(self):
        table = LinearProbingHashTable(initial_capacity=8)
        indices = take(10, table._generate_indices(6))

        expected = [6, 7, 0, 1, 2, 3, 4, 5, 6, 7]
        assert indices == expected

    def test_insertions(self):
        table = LinearProbingHashTable(initial_capacity=4, maximum_load_factor=0.8)

        # Test basic insertions
        table[2] = 100
        assert table._buckets == [BLANK, BLANK, (2, 100), BLANK]
        assert len(table) == 1
        table[3] = 101
        assert table._buckets == [BLANK, BLANK, (2, 100), (3, 101)]
        assert len(table) == 2

        # Test open addressing
        table[6] = 102
        assert table._buckets == [(6, 102), BLANK, (2, 100), (3, 101)]

        # Test overwrites
        table[2] = 90
        assert table._buckets == [(6, 102), BLANK, (2, 90), (3, 101)]
        assert len(table) == 3
        table[6] = 201
        assert table._buckets == [(6, 201), BLANK, (2, 90), (3, 101)]
        assert len(table) == 3

        # Test resize when maximum load factor is exceeded
        table[5] = 75
        assert len(table._buckets) == 8
        assert table._buckets == [BLANK, BLANK, (2, 90), (3, 101), BLANK, (5, 75), (6, 201), BLANK]

    def test_lookups(self):
        table = LinearProbingHashTable(initial_capacity=4, maximum_load_factor=0.8)

        table[2] = 100
        assert table._buckets == [BLANK, BLANK, (2, 100), BLANK]
        assert table[2] == 100

        table[3] = 101
        assert table._buckets == [BLANK, BLANK, (2, 100), (3, 101)]
        assert table[3] == 101

        table[6] = 102
        assert table._buckets == [(6, 102), BLANK, (2, 100), (3, 101)]
        assert table[6] == 102

        # Test missing key raises KeyError
        with pytest.raises(KeyError):
            _ = table[0]

    def test_iter(self):
        table = LinearProbingHashTable(initial_capacity=4, maximum_load_factor=0.8)

        table[2] = 100
        table[3] = 101
        assert list(table) == [2, 3]


class TestQuadraticProbing:
    def test_generate_indices(self):
        table = QuadraticProbingHashTable(initial_capacity=8)
        indices = take(15, table._generate_indices(6))

        expected = [6, 7, 1, 4, 0, 5, 3, 2, 2, 3, 5, 0, 4, 1, 7]
        assert indices == expected

    def test_insertions(self):
        table = QuadraticProbingHashTable(initial_capacity=8, maximum_load_factor=0.49)

        # Test basic insertions
        table[6] = 100
        assert table._buckets == [BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, (6, 100), BLANK]
        assert len(table) == 1
        table[7] = 101
        assert table._buckets == [BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, (6, 100), (7, 101)]
        assert len(table) == 2

        # Test open addressing
        table[14] = 102
        assert table._buckets == [BLANK, (14, 102), BLANK, BLANK, BLANK, BLANK, (6, 100), (7, 101)]
        assert len(table) == 3

        # Test overwrites
        table[6] = 90
        assert table._buckets == [BLANK, (14, 102), BLANK, BLANK, BLANK, BLANK, (6, 90), (7, 101)]
        assert len(table) == 3
        table[14] = 201
        assert table._buckets == [BLANK, (14, 201), BLANK, BLANK, BLANK, BLANK, (6, 90), (7, 101)]
        assert len(table) == 3

        # Test resize when maximum load factor is exceeded
        table[5] = 75
        assert len(table._buckets) == 16

    def test_lookups(self):
        table = QuadraticProbingHashTable(initial_capacity=8, maximum_load_factor=0.49)

        table[6] = 100
        assert table._buckets == [BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, (6, 100), BLANK]
        assert table[6] == 100

        table[7] = 101
        assert table._buckets == [BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, (6, 100), (7, 101)]
        assert table[7] == 101

        table[14] = 102
        assert table._buckets == [BLANK, (14, 102), BLANK, BLANK, BLANK, BLANK, (6, 100), (7, 101)]
        assert table[14] == 102

        # Test missing key raises KeyError
        with pytest.raises(KeyError):
            _ = table[0]

    def test_iter(self):
        table = QuadraticProbingHashTable(initial_capacity=4, maximum_load_factor=0.8)

        table[2] = 100
        table[3] = 101
        assert list(table) == [2, 3]


class TestChaining:
    def test_insertions(self):
        table = ChainingHashTable(initial_capacity=4, maximum_load_factor=0.9)

        # Test basic insertions
        table[2] = 100
        assert table._buckets == [BLANK, BLANK, deque([(2, 100)]), BLANK]
        assert len(table) == 1
        table[3] = 101
        assert table._buckets == [BLANK, BLANK, deque([(2, 100)]), deque([(3, 101)])]
        assert len(table) == 2

        # Test collisions
        table[6] = 200
        assert table._buckets == [BLANK, BLANK, deque([(2, 100), (6, 200)]), deque([(3, 101)])]
        assert len(table) == 3

        # Test overwrites
        table[2] = 90
        assert table._buckets == [BLANK, BLANK, deque([(2, 90), (6, 200)]), deque([(3, 101)])]
        assert len(table) == 3
        table[6] = 115
        assert table._buckets == [BLANK, BLANK, deque([(2, 90), (6, 115)]), deque([(3, 101)])]
        assert len(table) == 3

        # Test resize when maximum load factor is exceeded
        table[0] = 25
        assert len(table._buckets) == 8

    def test_lookups(self):
        table = ChainingHashTable(initial_capacity=4, maximum_load_factor=0.9)

        table[2] = 100
        assert table._buckets == [BLANK, BLANK, deque([(2, 100)]), BLANK]
        assert table[2] == 100

        table[3] = 101
        assert table._buckets == [BLANK, BLANK, deque([(2, 100)]), deque([(3, 101)])]
        assert table[3] == 101

        table[6] = 200
        assert table._buckets == [BLANK, BLANK, deque([(2, 100), (6, 200)]), deque([(3, 101)])]
        assert table[6] == 200

        # Test missing key raises KeyError
        with pytest.raises(KeyError):
            _ = table[0]

    def test_iter(self):
        table = ChainingHashTable(initial_capacity=4, maximum_load_factor=0.8)

        table[2] = 100
        table[3] = 101
        assert list(table) == [2, 3]
