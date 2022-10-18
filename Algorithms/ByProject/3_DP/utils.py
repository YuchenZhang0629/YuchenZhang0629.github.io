from __future__ import annotations
from abc import ABC, abstractmethod
from dataclasses import dataclass
from enum import Enum


class Color(Enum):
    BLACK = 30
    RED = 31
    GREEN = 32
    YELLOW = 33
    BLUE = 34
    PURPLE = 35
    CYAN = 36
    WHITE = 37


def color_string(string: str, color: Color) -> str:
    """Add escape characters around a string to print it in color through a terminal.

    Parameters
    ----------
    string : str
        The string to add color to.
    color : Color
        The color to use. Must be one of the colors defined in the Color enum.

    Returns
    -------
    str
        The colored string.
    """
    return f"\x1b[0;{color.value}m{string}\x1b[0;0m"


@dataclass
class LineNumbers(ABC):
    """An abstract class representing a selection of consecutive lines to be edited.
    """

    @property
    @abstractmethod
    def start_line(self) -> int:
        raise NotImplementedError

    @property
    @abstractmethod
    def end_line(self) -> int:
        raise NotImplementedError

    @property
    def num_lines_modified(self) -> int:
        return self.end_line - self.start_line + 1

    @property
    def lines_slice(self) -> slice:
        return slice(self.start_line - 1, self.end_line)


@dataclass
class LineNumberSingle(LineNumbers):
    """A concrete class representing a single line number to be edited.
    """

    line_numbers: int

    @property
    def start_line(self) -> int:
        return self.line_numbers

    @property
    def end_line(self) -> int:
        return self.line_numbers

    def __repr__(self) -> str:
        return str(self.line_numbers)


@dataclass
class LineNumberSingleBefore(LineNumberSingle):
    """A concrete class representing the line before an addition/deletion.
    """

    @property
    def end_line(self) -> int:
        return self.line_numbers - 1

    def __repr__(self) -> str:
        return str(self.line_numbers)


@dataclass
class LineNumberRange(LineNumbers):
    """A concrete class representing a range of line numbers to be edited.
    """

    line_numbers: tuple[int, int]

    @property
    def start_line(self) -> int:
        start_line, _ = self.line_numbers
        return start_line

    @property
    def end_line(self) -> int:
        _, end_line = self.line_numbers
        return end_line

    def __repr__(self) -> str:
        return ",".join(str(i) for i in self.line_numbers)


@dataclass
class Edit(ABC):
    """An abstract class representing an edit to a string.
    """

    original_line_nums: LineNumbers
    new_line_nums: LineNumbers

    def __init__(
        self,
        original_line_nums: int | tuple[int, int],
        new_line_nums: int | tuple[int, int],
    ):
        if isinstance(original_line_nums, int):
            self.original_line_nums = LineNumberSingle(original_line_nums)
        elif isinstance(original_line_nums, tuple):
            start, end = original_line_nums
            if start == end:
                self.original_line_nums = LineNumberSingle(start)
            else:
                self.original_line_nums = LineNumberRange(original_line_nums)
        else:
            raise Exception(f"Input is not an integer or a tuple: {original_line_nums}")
        if isinstance(new_line_nums, int):
            self.new_line_nums = LineNumberSingle(new_line_nums)
        elif isinstance(new_line_nums, tuple):
            start, end = new_line_nums
            if start == end:
                self.new_line_nums = LineNumberSingle(start)
            else:
                self.new_line_nums = LineNumberRange(new_line_nums)
        else:
            raise Exception(f"Input is not an integer or a tuple: {new_line_nums}")

    @property
    def num_lines_added(self) -> int:
        return self.original_line_nums.num_lines_modified

    @property
    def num_lines_deleted(self) -> int:
        return self.new_line_nums.num_lines_modified

    @property
    @abstractmethod
    def prefix_map_original(self) -> dict[str, str]:
        raise NotImplementedError

    @property
    @abstractmethod
    def prefix_map_new(self) -> dict[str, str]:
        raise NotImplementedError

    @property
    @abstractmethod
    def str_letter(self) -> str:
        raise NotImplementedError

    def original_lines(self, lines: list[str], format: str = "normal") -> list[str]:
        """Find and format the lines from the original file used in an edit. 

        Parameters
        ----------
        lines : list[str]
            A list of lines corresponding to the original file in a diff.
        format : str, optional
            The format to display the lines in, by default "normal"

        Returns
        -------
        list[str]
            The formatted lines used in the edit.
        """
        return [
            f"{self.prefix_map_original[format]} {line}"
            for line in lines[self.original_line_nums.lines_slice]
        ]

    def new_lines(self, lines: list[str], format: str = "normal") -> list[str]:
        """Find and format the lines from the new file used in an edit. 

        Parameters
        ----------
        lines : list[str]
            A list of lines corresponding to the new file in a diff.
        format : str, optional
            The format to display the lines in, by default "normal"

        Returns
        -------
        list[str]
            The formatted lines used in the edit.
        """
        return [
            f"{self.prefix_map_new[format]} {line}"
            for line in lines[self.new_line_nums.lines_slice]
        ]

    def __repr__(self):
        return f"{self.original_line_nums}{self.str_letter}{self.new_line_nums}"


class Addition(Edit):
    original_line_nums: LineNumberSingleBefore
    str_letter: str = "a"
    prefix_map_original: dict[str, str] = {}
    prefix_map_new: dict[str, str] = {
        "normal": ">",
        "contextual": "+",
        "unified": "+",
    }

    def __init__(
        self, original_line_ns: int, new_line_ns: int | tuple[int, int],
    ):
        super().__init__(original_line_ns, new_line_ns)
        self.original_line_nums = LineNumberSingleBefore(original_line_ns)


class Change(Edit):
    str_letter: str = "c"
    prefix_map_original: dict[str, str] = {
        "normal": "<",
        "contextual": "!",
        "unified": "-",
    }
    prefix_map_new: dict[str, str] = {
        "normal": ">",
        "contextual": "!",
        "unified": "+",
    }


class Deletion(Edit):
    new_line_nums: LineNumberSingleBefore
    str_letter: str = "d"
    prefix_map_original: dict[str, str] = {
        "normal": "<",
        "contextual": "-",
        "unified": "-",
    }
    prefix_map_new: dict[str, str] = {}

    def __init__(
        self, original_line_ns: int | tuple[int, int], new_line_ns: int,
    ):
        super().__init__(original_line_ns, new_line_ns)
        self.new_line_nums = LineNumberSingleBefore(new_line_ns)


def read_file_contents(filename: str) -> str:
    """Read the contents of a file into a string.

    Parameters
    ----------
    filename : str
        The file to read.

    Returns
    -------
    str
        The contents of the file in string form.
    """
    with open(filename, "r") as f:
        contents = f.read()
    return contents


def diffstr_normal(str1: str, str2: str, edits: list[Edit], color: bool = False) -> str:
    """Create a diff (normal output format) from a list of edits.

    Parameters
    ----------
    str1 : str
        The first (original) string to diff.
    str2 : str
        The second (new) string to diff.
    changes : list[Edit]
        The list of edits required to transform str1 to str2.

    Returns
    -------
    str
        The diff of str1 and str2 according to the list of edits.
    """

    lines1 = str1.splitlines()
    lines2 = str2.splitlines()

    diffstr_lines: list[str] = []

    for edit in edits:
        diffstr_lines.append(str(edit))

        original_lines = "\n".join(edit.original_lines(lines1))
        new_lines = "\n".join(edit.new_lines(lines2))

        if color:
            original_lines = color_string(original_lines, Color.RED)
            new_lines = color_string(new_lines, Color.GREEN)

        if isinstance(edit, Addition):
            diffstr_lines.append(new_lines)
        elif isinstance(edit, Deletion):
            diffstr_lines.append(original_lines)
        else:
            diffstr_lines.append(original_lines)
            diffstr_lines.append("---")
            diffstr_lines.append(new_lines)

    return "\n".join(diffstr_lines)


def check_edits(str1: str, str2: str, edits: list[Edit]) -> bool:
    """Transform str1 to str2 according to the provided list of edits
    and check whether they are equal.

    Parameters
    ----------
    str1 : str
        The string to transform.
    str2 : str
        The string to transform str1 into.
    edits : list[Edit]
        The list of edits used to transform str1.

    Returns
    -------
    bool
        True if transforming str1 by the list of edits produces str2. False otherwise.
    """
    lines1 = str1.splitlines()
    lines2 = str2.splitlines()

    # Apply changes in reverse order to maintain line numbers
    for edit in edits[::-1]:
        for _ in range(
            edit.original_line_nums.end_line,
            edit.original_line_nums.start_line - 1,
            -1,
        ):
            lines1.pop(edit.original_line_nums.start_line - 1)

        insert_index = edit.original_line_nums.start_line
        if not isinstance(edit.original_line_nums, LineNumberSingleBefore):
            insert_index -= 1
        for added_line_num in range(
            edit.new_line_nums.end_line, edit.new_line_nums.start_line - 1, -1
        ):
            lines1.insert(insert_index, lines2[added_line_num - 1])

    transformed_str = "\n".join(lines1)
    if str1[-1] == "\n":
        transformed_str += "\n"
    return transformed_str == str2
