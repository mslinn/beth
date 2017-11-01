# Notes
*Summary of the conversation between Steve and Mike*

*Mike:* Yesterday I started working on a command line shell (`beth`) that does not require SBT. 
You can see the [work in progress](https://github.com/mslinn/beth).
I'm encouraged by initial results.
I'd be interested in your feedback.

*Steve:* Looks ambitious! 
I piggy-back a lot on `sbt` for stuff like smart tab completions, etc., and of course the development side. 
But wth patience one could certainly write a lighter-weight interaction client.
I've written the [beginnings of a tutorial](https://mslinn.gitbooks.io/sbt-ethereum/content/gitbook/tutorial.html#tutorial).
Lots more do to, so far it's just read-only interaction; still need to fund an account, send ether, 
call methods that write state, etc.
Eventually a development tutorial would make sense too.

For me, I want to continue working on defining reproducible builds with a minimum of external dependencies 
and rich tooling that would form the basis of a publication medium for accessible DIY Ethereum experiments. 
I think `sbt-ethereum` is pretty good for that, or would be if I could get through my elaborate and growing list of TODOs.

## Solidity Compilers
Nashorn integration might offer one very significant benefit: cross-platform Solidity compilers. 
`solc`, [the original Solidity compiler](https://github.com/ethereum/solidity), is written in C++.
It has been compiled to [emscripten](https://en.wikipedia.org/wiki/Emscripten), 
and `solc-js`, the [Javascript version of solc](https://github.com/ethereum/solc-js), 
is the most widely used version of the Solidity compiler, via 
[Truffle](https://github.com/trufflesuite/truffle). 
Currently, `sbt-ethereum` users must either use a so-far terrible Javascript-solc-as-a-web-service 
[eth-netcompile](https://github.com/swaldman/eth-netcompile) 
or they must use the `ethSolidityInstallCompiler` SBT task to install a compiler binary, 
which strikes me as ugly and fragile. 

*Mike:* Why not call the C++ of `solc` version from Java?

*Steve:* The issue is trying hard not to require end-users to do a nontrivial install of solc. 
With solcJ, an install was just one sbt command (which I might have automated into plugin startup). 
That's exactly what I did, call out to the compiled c++ binaries. 
I could just demand end users put the version of `solc` they want in the path, doing that would be hard for the people 
I want to reach with my reproducible builds of experiments.
For now, I'll continue updating my [own fork](https://github.com/swaldman/solcJ) of 
[`solcJ`](https://github.com/ether-camp/solcJ).

*Mike:* The reference standard is `solc`, and the other versions lag a bit in terms of features, right?
The downloads are [here](https://github.com/ethereum/solidity/releases).
The Windows version provides 2 exe files and a dll. 
The Linux version is statically linked. 
I don't see a Mac version there, but brew works well

*Steve:* Really there is only one version, c++ `solc`, and the rest are just different packagings. 
`solc` literally compiles to `solc-js` emscripten, as it would to some different processor. 
`solcJ` just packages `solc` binaries; they all have feature parity with the `solc` binaries of the same version.

## Looking Back, Looking Forward
Initially, the Ethereum JSON-RPC api supported compilation directly, nodes were also compilers. 
But node developers objected to this, they did not want the maintenance burden and failed to standardize on what compilation 
responses should look like, so this is effectively gone.
On my TODO list is to make an sbt-ethereum installation an `eth-netcompile` server, 
a wrapper of whatever compiler binaries it has installed. 
This would allow me circumvent the scalability and performance problems of serving a very slow, CPU-bound task under Node.js' 
single-threaded-by-default computation model.

Alternatively, I could fix `eth-netcompile` to use one of the several newer features and libraries available in 
nodeland to support genuinely concurrent computation. 
But a perhaps better solution than all of these would be to let `solc-js` serve as the basis for a JVM-native solidity compiler via Nashorn. 
That's an experiment I've wanted to try for a long time, but it strikes me as risky whether it will work and perform acceptably, 
and so far I've opted to leave my existing compiler hacks in place and work on other things.

If this is something you are interested in, a `solc-jvm` would be a great project that would garner immediate interest from the Ethereum community. 
To my disappointment, as we've discussed already, the `ethereumj` community has slacked off on maintaining the `solcJ` project I rely upon, 
that embeds cross-platform binaries in a jar and serves as the basis for `ethSolidityInstallCompiler`.
(That command name is going to change very soon.)
The `EthereumJ` community would I think be really excited if a JVM-only solidity compiler became available. 

The JVM Ethereum community as a whole is small, alas, but for those of us who believe in middleware and integrations, 
it should grow larger, and the lack of a JVM solidity compiler is really a big roadblock.
If you are interested in that, I'd strongly recommend it as perhaps the most compelling JVM-related calling card into Ethereumland.
Again, in a static sense that's not saying so much, JVM Ethereum is small. 
But in a dynamic sense, it may not always be.

If what you'd like to do is work on a less clunky Ethereum command line than sbt-ethereum with JavaScript integration, 
that's great too, and I'd be glad to show you around `consuela` to help you understand how to work with that library for 
interacting with the Ethereum blockchain. 
The significant piece of `sbt-ethereum`s command line functionality that is not just a wrapper around consuela is the repository, 
which keeps track of deployed contract ABIs (in an `h2` database) and wallets (currently just as flat files, but I hope to change that). 
That functionality could be abstracted out into some kind of library, and I'd be open to that, but I think it's probably too early to make that choice. 
Since your command line won't be managing compilations and deployments, maybe a lighter weight, ABI-specific persistence store would make more sense.

## Consuela
The most basic piece of consuela is the [json-rpc client](https://github.com/swaldman/consuela/blob/master/src/main/scala/com/mchange/sc/v1/consuela/ethereum/jsonrpc/Client.scala).

### Synchronous IO
`Client.Simple(httpUrl)` is a somewhat incomplete Scala wrapper around `eth`
(see [JSON-RPC](https://github.com/ethereum/wiki/wiki/JSON-RPC)).
Note that, to mimic the eth `jsonrpc` methods, I define an object called `eth`, so `eth_call` becomes `eth.call`.

`Client.Simple` would be the easiest to get started with. 

### Asynchronous IO
If you want to use async IO, you need a factory.
There is one predefined and implicitly available as `Client.Factory.Default`. 
if you import this, `implicitly[Client.Factory]` should give you a factory, and you can use its `apply` function to a 
URL to get an async eth jsonrpc client.

A more complicated way of obtaining a factory for asynchronous IO, is to use `Client.Factory.createAsyncFactory()`, 
apply the `Factory` to the service URL to get the client, and eventually try to close the `Factory`. 

Jetty spawns tons of `Thread`s to manage its quasi-asynchronicity, thus the need for the otherwise annoying factory level.
Probably best to start with `Cient.Simple`.

### JSON-RPC
The `json-rpc` stuff itself is fairly trivial, but when doing the most complicated things you do, such as
deploying contracts, sending messages, etc, there's a fair amount of complexity about deciding how much to pay for gas 
(`gasPrice`), and how much gas to offer (`gas`, or `gasLimit`). 
Those get sent as transactions, which have to be signed; 
see [EthTransaction](https://github.com/swaldman/consuela/blob/master/src/main/scala/com/mchange/sc/v1/consuela/ethereum/EthTransaction.scala).

Transactions are easy to work with, but to sign them you've got to decide how access wallets (private keys). 
When passing arguments to functions or interpreting return values, you'll need 
[ethabi](https://github.com/swaldman/consuela/blob/master/src/main/scala/com/mchange/sc/v1/consuela/ethereum/EthTransaction.scala).

I'd definitely start with something more straightforward, like getting an account balance.
Some of the logic for dealing with transactions is abstracted into a construct called 
[Invoker](https://github.com/swaldman/consuela/blob/master/src/main/scala/com/mchange/sc/v1/consuela/ethereum/jsonrpc/Invoker.scala),
which is what Scala stubs are built from. 

If you just want to wrap `geth`, you can avoid some of the complexity by letting it handle the keystore. 
Instead of `eth_sendRawTransaction` you would use 
[eth_sendTransaction](https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendtransaction) 
and `geth` will manage signing the transaction if `geth` has that account's wallet and the account has been unlocked in `geth`. 
I don't do that at all, `sbt-ethereum` manages its own keystore and I don't like unlocking accounts directly within `geth`; 
it's okay if you are careful, but the combination of unlocking accounts and exporting `geth`'s RPC interface beyond 
`localhost` has cost people some money.
But letting `geth` handle `keystore` and signing would reduce the complexity of what you have to do.

## Random Thoughts
*Mike:* Perhaps `Keystore.{walletForAddress, listAddresses,addNew(passPhrase)}`?

    Client.call(from, to, gas, gasPrice, value, data, blockNumber)

