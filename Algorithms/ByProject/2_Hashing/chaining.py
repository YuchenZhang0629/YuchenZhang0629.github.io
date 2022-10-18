from __future__ import annotations

from collections import deque
from typing import Iterable

from hashtable import BLANK, KT, VT, HashTable


class ChainingHashTable(HashTable):
    """HashTable implementation which resolves collisions by chaining."""

    def __init__(self, initial_capacity: int = 8, maximum_load_factor: float = 1.0) -> None:
        super().__init__(initial_capacity, maximum_load_factor)

    def items(self) -> Iterable[tuple[KT, VT]]:
        for bucket in self._buckets:
            if bucket is BLANK:
                continue
            yield from bucket

    def __getitem__(self, search_key: KT) -> VT:
        index = hash(search_key) % self._capacity
        
        # My Code
        for tuples in self._buckets[index]:
            if tuples[0] == search_key:
                return tuples[1]
        raise KeyError("KeyError: search key not found.")

        # TODO: Loop over the deque to find the value matching the search key
        # Raise KeyError if the search key is not present
        ...

    def __setitem__(self, input_key: KT, input_value: VT):
        index = hash(input_key) % self._capacity

        if self._buckets[index] is BLANK:
            self._buckets[index] = deque()
            self._buckets[index].append((input_key,input_value))
            self._num_elements += 1
        else:
            exist = False
            for i in range(len(self._buckets[index])):
                if self._buckets[index][i][0] == input_key:
                    self._buckets[index][i] = (input_key,input_value)
                    exist = True
                    break
            if exist == False:
                self._buckets[index].append((input_key,input_value))
                self._num_elements += 1
        # TODO: Insert input_key and input_value, or update if already present.
        # Update self._num_elements as needed
        ...

        if self._load_factor > self._maximum_load_factor:
            # Maximum load factor exceeded, increase size of table
            self._resize()
