package BlockChain;

import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

// Chain类实现

public class Chain {
    // 区块链
    Deque<Block> blockChain;
    // 交易池
    List<Transaction> transactionPool;
    // 矿工奖赏
    int minerReward = 50;
    // 挖矿难度
    int difficulty = 4;
    User system;
    // Constructor
    Chain() {
        // 区块队列
        this.blockChain = new ArrayDeque<>();
        // 添加祖先区块
        this.blockChain.addLast(this.generateGenesisBlock());
        // 交易队列
        this.transactionPool = new ArrayList<>();
        this.system = new User("");
    }

    // 生成祖先区块
    private Block generateGenesisBlock() {
        Block genesisBlock = new Block(null, "", "GenesisBlock");
        genesisBlock.hash = genesisBlock.computeHash();
        genesisBlock.time = new Timestamp(System.currentTimeMillis());
        return genesisBlock;
    }

    // 添加Transaction到transactionPool
    public void transaction2Pool(User from, User to, int amount) throws Exception {
        Transaction t = new Transaction(from, to, amount);
        if (t.isValid()) {
            this.transactionPool.add(t);
        }
    }

    // 挖矿并发放矿工奖励
    public void mineTransaction(User address, String data) throws Exception {
        this.validateTransactions();
        assert this.blockChain.peekLast() != null;
        Block newBlock = new Block(transactionPool, this.blockChain.peekLast().hash, data);
        newBlock.mine(this.difficulty);
        Transaction minerReward = new Transaction(this.system, address, this.minerReward);
        if (minerReward.isValid()) {
            this.transactionPool.add(minerReward);
        }
        pack(newBlock);
    }

    // 打包之前验证每个交易的合法性
    private void validateTransactions() throws Exception {
        for (Transaction t : transactionPool) {
            if (!t.isValid()) {
                transactionPool.remove(t);
            }
        }
    }

    // 验证区块链合法性
    // 重新计算每个区块的hash并对比是否一致
    // 检查每个区块的previousHash与其上一区块的hash是否一致
    private boolean validateChain() {
        String previousHash = "";
        for (Block block : this.blockChain) {
            if (!block.hash.equals(block.computeHash())) {
                System.out.println("数据被篡改");
                return false;
            }
            if (!block.previousHash.equals(previousHash)) {
                System.out.println("区块链接断裂");
                return false;
            }
            previousHash = block.hash;
        }
        return true;
    }

    // block打包
    private void pack(Block newBlock) {
        newBlock.time = new Timestamp(System.currentTimeMillis());
        newBlock.hash = newBlock.computeHash();
        this.blockChain.addLast(newBlock);
        if (!validateChain()) {
            blockChain.pollLast();
        }
        this.transactionPool = new ArrayList<>();
    }

    // 打印区块链
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("BlockChain:\n");
        for (Block block : this.blockChain) {
            ret.append(block.toString()).append("\n");
        }
        return ret.toString();
    }
}
