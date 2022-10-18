from __future__ import annotations

import itertools
import os
import time
import warnings
from collections import defaultdict
from typing import Any, Hashable, Iterable, Type

import matplotlib.pyplot as plt
import numpy as np

from chaining import ChainingHashTable
from hashtable import HashTable
from open_addressing import LinearProbingHashTable, QuadraticProbingHashTable
from utils import deep_getsizeof

# Keep this below 3 million. Keys are generated as permutations of 10 characters.
KEY_COUNT = 1000000


def benchmark(
    hashtable: dict | HashTable,
    test_keys: Iterable[Hashable],
    test_values: Iterable[Any],
) -> tuple[float, float, float]:
    """Benchmarks the given hashtable using the provided keys and values.

    Parameters
    ----------
    hashtable : dict | HashTable
        Hashtable to benchmark.
    test_keys : Iterable[Hashable]
        Keys used in benchmark.
    test_values : Iterable[Any]
        Values used in benchmark.

    Returns
    -------
    insertion_time_s : float
        Time taken to complete insertion benchmark.
    lookup_time_s : float
        Time taken to complete lookup benchmark.
    memory_usage_MB : float
        Memory usage in megabytes.

    Notes
    -----
    The function also checks that the hashtable is working successfully.
    An AssertionError is thrown if the hashtable has the wrong values in it, after all insertions.
    """
    hashtable_cls = hashtable.__class__
    hashtable_description = hashtable_cls.__name__
    if isinstance(hashtable, HashTable):
        hashtable_description += f"(maximum_load_factor={hashtable._maximum_load_factor})"

    kv_pairs = list(zip(test_keys, test_values))

    # Insert
    start_time = time.perf_counter()
    for key, value in kv_pairs:
        hashtable[key] = value
    insertion_time_s = time.perf_counter() - start_time
    print(f"{hashtable_description} completed insertion benchmark in {insertion_time_s:.2f} s")

    # Lookup

    # Generate answer_kv_pairs
    answer_kv_pairs = []
    extra_kv_pairs = []
    seen_keys = set()
    for key, value in reversed(kv_pairs):
        if key in seen_keys:
            extra_kv_pairs.append((key, value))
            continue
        seen_keys.add(key)
        answer_kv_pairs.append((key, value))

    start_time = time.perf_counter()

    # Iterate over answer_kv_pairs, and check correctness (admittedly has some overhead)
    for key, value in answer_kv_pairs:
        v = hashtable[key]
        assert v == value, f"Value {v!r} for key {key!r} did not match expected value {value!r}"

    # Iterate over extra pairs to keep runtime accurate in duplicate runs
    for key, value in extra_kv_pairs:
        v = hashtable[key]
        # no assert: v likely differs from value

    lookup_time_s = time.perf_counter() - start_time
    print(f"{hashtable_description} completed lookup benchmark in {lookup_time_s:.2f} s")

    # Memory
    memory = deep_getsizeof(hashtable)
    memory_usage_MB = memory / 1e6
    print(f"{hashtable_description} used {memory_usage_MB:.2f} MB")

    return insertion_time_s, lookup_time_s, memory_usage_MB


def run_benchmarks(
    trial_name: str,
    maximum_load_factors: dict[Type, list[float]],
    duplicate_keys: bool = False,
    plot_insert: bool = False,
    plot_lookup: bool = False,
    plot_memory: bool = False,
):
    """Runs benchmarks for hashtables with the provided maximum load factors.

    Prints benchmark results to standard output, and saves created plots to the `plots` directory.

    Parameters
    ----------
    trial_name: str
        A name for the trial. Used in the filenames of output plots.
    maximum_load_factors: dict[Type, list[float]]
        Maximum load factors to be used by the custom hashtables.
    duplicate_keys: bool = False
        If True, keys are created with duplicates via a resampling process.
        The set of distinct keys will be approximately 63% as large as the key list.
        (1 - 1/e is about 63%)
    plot_insert: bool = False
        If True, a plot is generated with insert times.
    plot_lookup: bool = False
        If True, a plot is generated with lookup times.
    plot_memory: bool = False
        If True, a plot is generated with used memory.

    Notes
    -----
    plot_insert, plot_lookup, and plot_memory are NOT mutually exclusive.
    If a plot is turned off, its statistic will still be computed and printed.
    """

    # Make plots subdirectory (if it doesn't exist)
    try:
        os.mkdir("plots")
    except FileExistsError:
        pass

    # Ensure that load factors are sorted.
    all_load_factors: set[float] = set()
    for hashtable_cls, load_factors in maximum_load_factors.items():
        all_load_factors.update(load_factors)
        maximum_load_factors[hashtable_cls] = sorted(load_factors)
    # Add dict with all possible load factors for baseline comparison
    maximum_load_factors[dict] = sorted(all_load_factors)

    print(f'____Beginning trial "{trial_name}"____')

    # Generate keys and values.
    test_string = "abcdefghij"  # 3,628,800 possible permutations
    test_keys = list(map("".join, itertools.islice(itertools.permutations(test_string), KEY_COUNT)))
    np.random.shuffle(test_keys)

    if duplicate_keys:
        # Resample with replacement to cause some duplicate keys.
        test_keys = np.random.choice(test_keys, len(test_keys), replace=True)

    test_values = np.random.random(size=len(test_keys))

    insert_results: dict[type, list[float]] = defaultdict(list)
    lookup_results: dict[type, list[float]] = defaultdict(list)
    memory_results: dict[type, list[float]] = defaultdict(list)

    # Perform Tests
    for hashtable_cls, load_factors in maximum_load_factors.items():
        for load_factor in load_factors:

            if hashtable_cls == dict:
                hashtable = hashtable_cls()
            else:
                hashtable = hashtable_cls(maximum_load_factor=load_factor)

            insertion_time, lookup_time, memory_usage = benchmark(hashtable, test_keys, test_values)
            insert_results[hashtable_cls].append(insertion_time)
            lookup_results[hashtable_cls].append(lookup_time)
            memory_results[hashtable_cls].append(memory_usage)

    # Plot Results

    info_str = ""
    if duplicate_keys:
        info_str += " with duplicates"

    with warnings.catch_warnings():
        # Suppress matplotlib warnings
        warnings.simplefilter("ignore")

        if plot_insert:
            plt.title("Insertion Time" + info_str)
            plt.xlabel("Maximum Load Factor")
            plt.ylabel("Time Elapsed (seconds)")

            for hashtable_cls, results in insert_results.items():
                plt.plot(maximum_load_factors[hashtable_cls], results, label=hashtable_cls.__name__)

            plt.legend()
            plt.savefig(f"plots/{trial_name}_insert.png")
            plt.close()

        if plot_lookup:
            plt.title("Lookup Time" + info_str)
            plt.xlabel("Maximum Load Factor")
            plt.ylabel("Time Elapsed (seconds)")

            for hashtable_cls, results in lookup_results.items():
                plt.plot(maximum_load_factors[hashtable_cls], results, label=hashtable_cls.__name__)

            plt.legend()
            plt.savefig(f"plots/{trial_name}_lookup.png")
            plt.close()

        if plot_memory:
            plt.title("Memory Usage" + info_str)
            plt.xlabel("Maximum Load Factor")
            plt.ylabel("Memory Used (MB)")

            for hashtable_cls, results in memory_results.items():
                plt.plot(maximum_load_factors[hashtable_cls], results, label=hashtable_cls.__name__)

            plt.legend()
            plt.savefig(f"plots/{trial_name}_memory.png")
            plt.close()


if __name__ == "__main__":
    # TODO: Select and run benchmarks, then look at the figures in the plots/ directory.

    # You can delete/change this if you want. It is just an example.
    maximum_load_factors = {
        # Chaining load factor may exceed 1.0
        ChainingHashTable: [0.1,0.2,0.3,0.4,0.5,0.6],
        # Open addressing load factors must be less than 1.0
        LinearProbingHashTable: [0.1,0.2,0.3,0.4,0.5,0.6],
        QuadraticProbingHashTable: [0.1,0.2,0.3,0.4,0.5,0.6]
    }

    run_benchmarks(
        trial_name="Insert1MNoDupe",
        maximum_load_factors=maximum_load_factors,
        duplicate_keys=False,
        plot_insert=True,
        plot_lookup=True,
        plot_memory=True,
    )
