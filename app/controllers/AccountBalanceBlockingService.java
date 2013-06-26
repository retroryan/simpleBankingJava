package controllers;

import models.AccountBalances;

import static models.AccountBalances.*;

public class AccountBalanceBlockingService {

    public static AccountBalances getCustomerAccountBalances(Long accountId) throws InterruptedException {
        CheckingAccountBalances checkingAccountBalances = CheckingAccountProxy.getCustomerAccountBalances(accountId);
        SavingsAccountBalances savingsAccountBalances = SavingsAccountProxy.getCustomerAccountBalances(accountId);
        MoneyMarketAccountBalances moneyMarketAccountBalances = MoneyMarketAccountProxy.getCustomerAccountBalances(accountId);

        return new AccountBalances(checkingAccountBalances, savingsAccountBalances, moneyMarketAccountBalances);

    }

    public static class CheckingAccountProxy {
        public static CheckingAccountBalances getCustomerAccountBalances(Long accountId) throws InterruptedException {
            Thread.sleep(400);
            return new CheckingAccountBalances(checkingAccountData.get(accountId));
        }
    }

    public static class SavingsAccountProxy {
        public static SavingsAccountBalances getCustomerAccountBalances(Long accountId) throws InterruptedException {
            Thread.sleep(400);
            return new SavingsAccountBalances(savingsAccountData.get(accountId));
        }
    }

    public static class MoneyMarketAccountProxy {
        public static MoneyMarketAccountBalances getCustomerAccountBalances(Long accountId) throws InterruptedException {
            Thread.sleep(400);
            return new MoneyMarketAccountBalances(moneyMarketAccountData.get(accountId));
        }
    }
}
