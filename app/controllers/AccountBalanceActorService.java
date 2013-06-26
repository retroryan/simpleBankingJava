package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.dispatch.Mapper;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import models.AccountBalances;
import static models.AccountBalances.*;
import scala.concurrent.Future;

import java.util.ArrayList;
import java.util.List;

import static akka.dispatch.Futures.sequence;
import static akka.pattern.Patterns.ask;
import static controllers.ActorAccountProxies.GetAccountBalances;

public class AccountBalanceActorService extends UntypedActor {


    ActorRef checkingAccountProxy;
    ActorRef savingsAccountProxy;
    ActorRef moneyMarketAccountProxy;

    public String mutableValue = "Real Value";

    public static Props mkProps(final ActorRef checkingAccountProxy, final ActorRef savingsAccountProxy, final ActorRef moneyMarketAccountProxy) {
         Props props = new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new AccountBalanceActorService(checkingAccountProxy, savingsAccountProxy, moneyMarketAccountProxy);
            }
        });
        return props;
    }

    public AccountBalanceActorService(ActorRef checkingAccountProxy, ActorRef savingsAccountProxy, ActorRef moneyMarketAccountProxy) {
        this.checkingAccountProxy = checkingAccountProxy;
        this.savingsAccountProxy = savingsAccountProxy;
        this.moneyMarketAccountProxy = moneyMarketAccountProxy;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof GetAccountBalances) {
            GetAccountBalances getAccountBalances = (GetAccountBalances) message;

            List<Future<Object>> accountProxyFuturesList = new ArrayList<>();

            Future<Object> checkingAccountBalancesFuture = ask(checkingAccountProxy, getAccountBalances, ActorUtils.ASK_TIMEOUT);
            accountProxyFuturesList.add(checkingAccountBalancesFuture);

            Future<Object> savingsAccountBalancesFuture = ask(savingsAccountProxy, getAccountBalances, ActorUtils.ASK_TIMEOUT);
            accountProxyFuturesList.add(savingsAccountBalancesFuture);

            Future<Object> moneyMarketAccountBalancesFuture = ask(moneyMarketAccountProxy, getAccountBalances, ActorUtils.ASK_TIMEOUT);
            accountProxyFuturesList.add(moneyMarketAccountBalancesFuture);

            Future<Iterable<Object>> iterableFuture = sequence(accountProxyFuturesList, context().dispatcher());
            Future<AccountBalances> accountBalancesFuture = iterableFuture.map(
                    getMapper(), context().dispatcher());

            final ActorRef originalSender = sender();
            accountBalancesFuture.onSuccess(new OnSuccess<AccountBalances>() {
                @Override public final void onSuccess(AccountBalances accountBalances) {
                    System.out.println("OnSuccess originalSender: " + originalSender);
                    System.out.println("OnSuccess sender(): " + sender());
                    System.out.println("mutableValue = " + mutableValue);
                    originalSender.tell(accountBalances, self());
                }
            }, context().dispatcher());


            //trick value is changed after the callback is created, showing what happens when you close over mutable data
            mutableValue = "Fake Value";

            accountBalancesFuture.onFailure(new OnFailure() {
                public void onFailure(Throwable failure) {
                    System.out.println("failure = " + failure);
                }
            }, context().dispatcher());
        }
    }

    private Mapper<Iterable<Object>, AccountBalances> getMapper() {
        return new Mapper<Iterable<Object>, AccountBalances>() {
            @Override
            public AccountBalances apply(Iterable<Object> futuresList) {

                CheckingAccountBalances checkingAccountBalances = null;
                SavingsAccountBalances savingsAccountBalances = null;
                MoneyMarketAccountBalances moneyMarketAccountBalances = null;

                for (Object obj : futuresList)  {
                    if (obj instanceof CheckingAccountBalances) {
                        checkingAccountBalances = (CheckingAccountBalances) obj;
                    }
                    else if (obj instanceof SavingsAccountBalances) {
                        savingsAccountBalances = (SavingsAccountBalances) obj;
                    }
                    else if (obj instanceof MoneyMarketAccountBalances) {
                        moneyMarketAccountBalances = (MoneyMarketAccountBalances) obj;
                    }
                }
                System.out.println("accountBalancesFuture sender(): " + sender());
                return new AccountBalances(checkingAccountBalances, savingsAccountBalances, moneyMarketAccountBalances);
            }
        };
    }

}
