# Beth - A Better Command Line Interface to Ethereum

<img src='https://raw.githubusercontent.com/mslinn/beth/gh-pages/images/queenElizabeth.jpg' align='right' width='33%'>

[![Build Status](https://travis-ci.org/mslinn/beth.svg?branch=master)](https://travis-ci.org/mslinn/beth)
[![GitHub version](https://badge.fury.io/gh/mslinn%2Fbeth.svg)](https://badge.fury.io/gh/mslinn%2Fbeth)

Beth is a command line shell for Ethereum, like [eth and geth](https://www.ethereum.org/cli), 
but written in Scala, with Java/JavaScript integration. 
Beth stands for "Better ETH"; it features auto-completion and colored help information.

## Running the Program
The `bin/run` Bash script assembles this project into a fat jar and runs it.
Sample usage, which runs the `Beth` entry point in `src/main/scala/com.micronautics.Main.scala`:

```
$ bin/run
```

The `-j` option forces a rebuild of the fat jar.
Use it after modifying the source code.

```
$ bin/run -j
```

## Developers
### Automating the Edit / Compile / Debug Loop
The `bin/rerun` script is handy when you want to see the changes made to the running program.
Press `Control-d` to exit the program, and it will automatically be rebuilt with the latest changes and rerun.

Both the `bin/run` and `bin/rerun` scripts enable remote debugging on port 5005, 
which is IntelliJ IDEA's default remote debugging port.
The debug setup persists each time `rerun` relaunches the program.

```
1. Waiting for source changes... (press enter to interrupt)
Micronautics Research Ethereum Shell v0.1.0
Commands are: account, bindkey, exit/^d, help/?, javascript, password, set, testkey and tput
cli-loop [master] shell> javascript
Entering the javascript subshell. Press Control-d to exit the subshell.

cli-loop [master] javascript> var x = 1
cli-loop [master] javascript> x
1

cli-loop [master] javascript> var y = x * 33 + 2
cli-loop [master] javascript> y
35.0

cli-loop [master] javascript> x
1

cli-loop [master] javascript>
Returning to ethereum.

Commands are: account, bindkey, exit/^d, help/?, javascript, password, set, testkey and tput
cli-loop [master] shell>
[success] Total time: 318 s, completed Nov 1, 2017 2:53:25 AM

2. Waiting for source changes... (press enter to interrupt)
Micronautics Research Ethereum Shell v0.1.0
Commands are: account, bindkey, exit/^d, help/?, javascript, password, set, testkey and tput
cli-loop [master] shell> javascript
Entering the javascript sub-shell. Press Control-d to exit the sub-shell.

cli-loop [master] javascript> var x = 1
cli-loop [master] javascript> x
1

cli-loop [master] javascript> x + 2
3.0

cli-loop [master] javascript> var y = x + 2
cli-loop [master] javascript> y
3.0

cli-loop [master] javascript> Listening for transport dt_socket at address: 5005

Returning to javascript.

Commands are: account, bindkey, exit/^d, help/?, javascript, password, set, testkey and tput
cli-loop [master] shell>
^C
mslinn@kaiju cli-loop (master)
```

### Upstream: `cli-loop`
Some of the work for this project is done in the [cli-loop](https://github.com/mslinn/cli-loop) project.
That means the [issues](https://github.com/mslinn/cli-loop/issues) filed with that project might also apply to this project.

Sometimes when that project is updated, you might want to import those changes into this project.
Some changes are likely to be specific to that project, so take care sorting out the relevant changes.

    git remote add upstream https://github.com/mslinn/cli-loop
    git fetch upstream
    git checkout master
    
    # Figure out what changes you want to keep or discard
    
    git merge upstream/master
    git push origin master
    
For more information on syncing forks, [see this](https://help.github.com/articles/syncing-a-fork/).
    
## Published Binary Executables
Yes, [this is the plan](https://github.com/mslinn/beth/issues/3).
Wanna help make it happen?
Jump in and submit a pull request!
