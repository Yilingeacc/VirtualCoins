package BlockChain;

public class mainTest {

    public static void main(String[] args) throws Exception {
        // register
        Chain chain = new Chain();
        User system = new User("");
        User u1 = new User("a");
        User u2 = new User("b");
        User u3 = new User("c");

        // transaction
        chain.mineTransaction(u1, "miner1");
        chain.transaction2Pool(u1, u2, 5);
        chain.mineTransaction(u3, "miner2");
        System.out.println(chain);
    }
}
