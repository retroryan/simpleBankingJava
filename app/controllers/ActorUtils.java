package controllers;


import akka.actor.ActorRef;
import akka.util.Timeout;
import play.libs.Akka;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public class ActorUtils {

    public static final FiniteDuration ASK_DURATION = FiniteDuration.create(2, TimeUnit.SECONDS);
    public static final Timeout ASK_TIMEOUT = Timeout.durationToTimeout(ASK_DURATION);

    public static ActorRef checkingAccountProxy = Akka.system().actorOf(ActorAccountProxies.CheckingAccountActor.mkProps());
    public static ActorRef savingsAccountProxy = Akka.system().actorOf(ActorAccountProxies.SavingsAccountActor.mkProps());
    public static ActorRef moneyMarketAccountProxy = Akka.system().actorOf(ActorAccountProxies.MoneyMarketAccountActor.mkProps());
    public static ActorRef accountBalanceActorService = Akka.system().actorOf(AccountBalanceActorService.mkProps(checkingAccountProxy, savingsAccountProxy, moneyMarketAccountProxy));

}
