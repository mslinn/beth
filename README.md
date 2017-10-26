# Beth - A Better Command Line Interface to Ethereum

[![License](https://licensebuttons.net/p/zero/1.0/88x31.png)](https://creativecommons.org/share-your-work/public-domain/cc0/)
[![Build Status](https://travis-ci.org/mslinn/sbtTemplate.svg?branch=master)](https://travis-ci.org/mslinn/sbtTemplate)
[![GitHub version](https://badge.fury.io/gh/mslinn%2FsbtTemplate.svg)](https://badge.fury.io/gh/mslinn%2FsbtTemplate)

Beth is a command-line for Ethereum, like eth and geth, but written in Scala.
Beth stands for "Better ETH".

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
