# Beth - A Better Command Line Interface to Ethereum

[![Build Status](https://travis-ci.org/mslinn/beth.svg?branch=master)](https://travis-ci.org/mslinn/beth)
[![GitHub version](https://badge.fury.io/gh/mslinn%2Fbeth.svg)](https://badge.fury.io/gh/mslinn%2Fbeth)
[![Contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/dwyl/esta/issues)

Beth is a command-line for Ethereum, like eth and geth, but written in Scala.
Beth stands for "Better ETH"; it features auto-completion and colored help information.
This project uses [EthereumJ](https://github.com/ethereum/ethereumj).

[![YouTube Demo](https://www.micronauticsresearch.com/images/bethIntro.png)](https://www.youtube.com/watch?v=HGPFR1gzrXs)

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

<img src='https://raw.githubusercontent.com/mslinn/beth/gh-pages/images/queenElizabeth.jpg' align='right' width='33%'>

## Published Binary Executables
Yes, this is the plan.
Wanna help make it happen?
Jump in and submit a pull request!

## Developers
The `bin/rerun` script is handy when you want to see the changes made to the running program.
Press `Control-d` to exit the program, and it will automatically be rebuilt with the latest changes and rerun.

Both the `bin/run` and `bin/rerun` scripts enable remote debugging on port 5005, 
which is IntelliJ IDEA's default remote debugging port.
The debug setup persists each time `rerun` relaunches the program.

## Sponsorship and Proofs of Concept
<img src='https://www.micronauticsresearch.com/images/robotCircle400shadow.png' align='right' width='15%'>

To date this project has been sponsored by [Micronautics Research Corporation](http://www.micronauticsresearch.com/),
the company that delivers online Scala training via [ScalaCourses.com](http://www.ScalaCourses.com).

The only way to provide value is to serve customers.
We actively seek opportunities to develop blockchain-related prototypes and proofs of concept.
We would be happy to [present our work and discuss sponsorship opportunities](https://www.micronauticsresearch.com/portfolio.html#ethereum) for our open-source libraries.
Please [contact us](mailto:sales@micronauticsresearch.com) to discuss.

## License
This software is published under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
