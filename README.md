# Beth - A Better Command Line Interface to Ethereum

<img src='https://raw.githubusercontent.com/mslinn/beth/gh-pages/images/queenElizabeth.jpg' align='right' width='33%'>
[![Build Status](https://travis-ci.org/mslinn/beth.svg?branch=master)](https://travis-ci.org/mslinn/beth)
[![GitHub version](https://badge.fury.io/gh/mslinn%2Fbeth.svg)](https://badge.fury.io/gh/mslinn%2Fbeth)

https://raw.githubusercontent.com/mslinn/beth/gh-pages/images/queenElizabeth.jpg

Beth is a command-line for Ethereum, like eth and geth, but written in Scala.
Beth stands for "Better ETH"; it features auto-completion and colored help information.

## Running the Program
The `bin/run` Bash script assembles this project into a fat jar and runs it.
Sample usage, which runs the `Beth` entry point in `src/main/scala/Beth.scala`:

```
$ bin/run
```

The `-j` option forces a rebuild of the fat jar.
Use it after modifying the source code.

```
$ bin/run -j
```

## Published Binary Executables
Yes, this is the plan.
Wanna help make it happen?
Jump in and submit a pull request!