# Rainfall

Rainfall is the compiler for the Thrice language.

Transpiles the Thrice source to C.

This is the version of the Rainfall written in Java. Eventually, it will be
rewritten in Thrice.

---

## Usage

Loads all the Thrice packages in the workspace directory to a single C file with
an entry point. There should be one and only one entry point in the workspace.

`rainfall (-(-<option>|<option_shortcut>) <value>?)* (<subcommand>|<subcommand_shortcut>)+`

After the executable name, there should be zero or more options, which come
after two dashes and might expect a value after them. The shortcut for an option
can only be used with a single dash before it.

Then, there should be one or more subcommands. When multiple subcommands are
given, they are executed one after another in the order they are provided. The
shortcut for a subcommand can be directly used in its place.

The results of subcommands might be cached in or between runs and reused later.

### Subcommands

| Subcommand | Shortcut | Description                                     |
| ---------- | -------- | ----------------------------------------------- |
| new        | n        | Creates a new package.                          |
| check      | c        | Checks the validity of all the packages.        |
| build      | b        | Generates the C file of the executable package. |
| run        | r        | Runs the executable package.                    |
| test       | t        | Runs the tests of all the packages.             |

### Options

| Option    | Type | Shortcut | Description                                       |
| --------- | ---- | -------- | ------------------------------------------------- |
| directory | Path | d        | Change the workspace directory to the given path. |

## License

Licensed under GPL 3.0 or later.

---

Copyright (C) 2022 Cem Geçgel <gecgelcem@outlook.com>
