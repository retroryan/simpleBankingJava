package controllers;

import akka.actor.Props;
import akka.actor.UntypedActor;
import models.AccountBalances;

import java.util.List;

import static models.AccountBalances.*;

public class ActorAccountProxies {

    public static class GetAccountBalances {
        public Long accountId;

        public GetAccountBalances(Long accountId) {
            this.accountId = accountId;
        }
    }

    public static class CheckingAccountActor extends UntypedActor {

        public static Props mkProps() {
            return new Props(CheckingAccountActor.class);
        }

        @Override
        public void onReceive(Object message) throws Exception {
            if (message instanceof GetAccountBalances) {
                GetAccountBalances getAccountBalances = (GetAccountBalances) message;
                Thread.sleep(400);
                CheckingAccountBalances checkingAccountBalances = new CheckingAccountBalances(checkingAccountData.get(getAccountBalances.accountId));
                sender().tell(checkingAccountBalances, self());
            }
        }
    }

    public static class SavingsAccountActor extends UntypedActor {

        public static Props mkProps() {
            return new Props(SavingsAccountActor.class);
        }

        @Override
        public void onReceive(Object message) throws Exception {
            if (message instanceof GetAccountBalances) {
                GetAccountBalances getAccountBalances = (GetAccountBalances) message;
                Thread.sleep(400);
                List<AccountBalanceTuple> accountBalanceTuples = savingsAccountData.get(getAccountBalances.accountId);
                SavingsAccountBalances savingsAccountBalances = new SavingsAccountBalances(accountBalanceTuples);
                sender().tell(savingsAccountBalances, self());
            }
        }
    }

    public static class MoneyMarketAccountActor extends UntypedActor {

        public static Props mkProps() {
            return new Props(MoneyMarketAccountActor.class);
        }

        @Override
        public void onReceive(Object message) throws Exception {
            if (message instanceof GetAccountBalances) {
                GetAccountBalances getAccountBalances = (GetAccountBalances) message;
                Thread.sleep(400);
                MoneyMarketAccountBalances moneyMarketAccountBalances = new MoneyMarketAccountBalances(moneyMarketAccountData.get(getAccountBalances.accountId));
                sender().tell(moneyMarketAccountBalances, self());
            }
        }
    }
}
