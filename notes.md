# Notes
*Summary of the conversation between Steve and Mike*

*Mike:* Yesterday I started working on a command line shell (beth) that does not require SBT. 
You can see the [work in progress](https://github.com/mslinn/beth).
I'm encouraged by initial results.
I'd be interested in your feedback.

*Steve:* Looks ambitious! 
I piggy-back a lot on `sbt` for stuff like smart tab completions, etc., and of course the development side. 
But wth patience one could certainly write a lighter-weight interaction client.
I've written the [beginnings of a tutorial](https://mslinn.gitbooks.io/sbt-ethereum/content/gitbook/tutorial.html#tutorial).
Lots more do to, so far it's just read-only interaction, need to funding an account, sending ether, calling methods that write state. 
Eventually a development tutorial would make sense too.

The most basic piece of consuela is the [json-rpc client](https://github.com/swaldman/consuela/blob/master/src/main/scala/com/mchange/sc/v1/consuela/ethereum/jsonrpc/Client.scala).

`Client.Simple(httpUrl)` is a somewhat incomplete Scala wrapper around `eth`
(see [JSON-RPC](https://github.com/ethereum/wiki/wiki/JSON-RPC)).
Note that, to mimic the eth `jsonrpc` methods, I define an object called `eth`, so `eth_call` becomes `eth.call`.

`Client.Simple` would be the easiest to get started with. 
If you want fancy asynchronous IO, use `Client.Factory.createAsyncFactory()`, apply the `Factory` to the service URL to get the client, 
and eventually try to close the `Factory`. 
Jetty spawns tons of `Thread`s to manage its quasi-asynchronicity, thus the need for the otherwise annoying factory level.
Probably best to start with `Cient.Simple`.

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

*Mike:* Perhaps `Keystore.{walletForAddress, listAddresses,addNew(passPhrase)}`?

    Client.call(from, to, gas, gasPrice, value, data, blockNumber)

