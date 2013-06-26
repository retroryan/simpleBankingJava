package models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountBalances {

    public CheckingAccountBalances checkingAccountBalances;
    public SavingsAccountBalances savingsAccountBalances;
    public MoneyMarketAccountBalances moneyMarketAccountBalances;

    public AccountBalances(CheckingAccountBalances checkingAccountBalances, SavingsAccountBalances savingsAccountBalances, MoneyMarketAccountBalances moneyMarketAccountBalances) {
        this.checkingAccountBalances = checkingAccountBalances;
        this.savingsAccountBalances = savingsAccountBalances;
        this.moneyMarketAccountBalances = moneyMarketAccountBalances;
    }

    public static class AccountBalanceTuple {
        public Long accountId;
        public BigDecimal balance;

        public AccountBalanceTuple(Long accountId, BigDecimal balance) {
            this.accountId = accountId;
            this.balance = balance;
        }
    }

    public static class CheckingAccountBalances {
        public List<AccountBalanceTuple> checkingAccountBalances;

        public CheckingAccountBalances(List<AccountBalanceTuple> accountBalanceTuples) {
            this.checkingAccountBalances = accountBalanceTuples;
        }
    }

    public static class SavingsAccountBalances {
        public List<AccountBalanceTuple> savingsAccountBalances;

        public SavingsAccountBalances(List<AccountBalanceTuple> accountBalanceTuples) {
            this.savingsAccountBalances = accountBalanceTuples;
        }
    }

    public static class MoneyMarketAccountBalances {
        public List<AccountBalanceTuple> moneyMarketAccountBalances;

        public MoneyMarketAccountBalances(List<AccountBalanceTuple> accountBalanceTuples) {
            this.moneyMarketAccountBalances = accountBalanceTuples;
        }
    }

    public static Map<Long, List<AccountBalanceTuple>> checkingAccountData;

    static {
        checkingAccountData = new HashMap<>();

        List<AccountBalanceTuple> oneAccountBalance = new ArrayList<>();
        oneAccountBalance.add(new AccountBalanceTuple(1l, new BigDecimal(15000)));
        checkingAccountData.put(1l, oneAccountBalance);

        List<AccountBalanceTuple> twoAccountBalance = new ArrayList<>();
        twoAccountBalance.add(new AccountBalanceTuple(2l, new BigDecimal(5000)));
        twoAccountBalance.add(new AccountBalanceTuple(2l, new BigDecimal(15000)));
        twoAccountBalance.add(new AccountBalanceTuple(2l, new BigDecimal(25000)));
        checkingAccountData.put(2l, twoAccountBalance);
    }

    public static Map<Long, List<AccountBalanceTuple>> savingsAccountData;

    static {
        savingsAccountData = new HashMap<>();

        List<AccountBalanceTuple> oneAccountBalance = new ArrayList<>();
        oneAccountBalance.add(new AccountBalanceTuple(1l, new BigDecimal(12002)));
        savingsAccountData.put(1l, oneAccountBalance);

        List<AccountBalanceTuple> twoAccountBalance = new ArrayList<>();
        twoAccountBalance.add(new AccountBalanceTuple(2l, new BigDecimal(7003)));
        twoAccountBalance.add(new AccountBalanceTuple(2l, new BigDecimal(18003)));
        twoAccountBalance.add(new AccountBalanceTuple(2l, new BigDecimal(24003)));
        savingsAccountData.put(2l, twoAccountBalance);
    }

    public static Map<Long, List<AccountBalanceTuple>> moneyMarketAccountData;

    static {
        moneyMarketAccountData = new HashMap<>();

        List<AccountBalanceTuple> oneAccountBalance = new ArrayList<>();
        oneAccountBalance.add(new AccountBalanceTuple(1l, new BigDecimal(19000)));
        moneyMarketAccountData.put(1l, oneAccountBalance);

        List<AccountBalances.AccountBalanceTuple> twoAccountBalance = new ArrayList<>();
        twoAccountBalance.add(new AccountBalanceTuple(2l, new BigDecimal(3005)));
        twoAccountBalance.add(new AccountBalanceTuple(2l, new BigDecimal(12005)));
        twoAccountBalance.add(new AccountBalanceTuple(2l, new BigDecimal(15005)));
        moneyMarketAccountData.put(2l, twoAccountBalance);
    }

}
