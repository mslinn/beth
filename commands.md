* `eth syncing`

      currentBlock: 745600, ighestBlock: 889152, startingBlock: 745553

* `blockNumber` - shows the block number as integer.
  [Here is the function documentation](https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethblocknumber). 
  For the block hash instead, use:

      web3.eth.getBlock(BLOCK_NUMBER).hash

  ...for the current block (atm) it will be:

      web3.eth.getBlock(887893).hash

  ...for the latest block:

      web3.eth.getBlock(web3.eth.blockNumber).hash
