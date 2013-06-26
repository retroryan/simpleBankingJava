package controllers;

import akka.dispatch.Mapper;
import akka.dispatch.Recover;
import models.AccountBalances;
import org.codehaus.jackson.JsonNode;
import play.libs.Akka;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.Await;
import scala.concurrent.Future;
import views.html.getAccountBalances;
import views.html.index;

import static akka.pattern.Patterns.ask;
import static models.AccountBalances.CheckingAccountBalances;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result getAccountBalances() {
        return ok(getAccountBalances.render());
    }


    public static String mutableValue = "Real Value";

    @BodyParser.Of(BodyParser.Json.class)
    public static Result asyncGetAccountBalancesOne() throws Exception {
        JsonNode json = request().body().asJson();
        final Long accountId = Long.parseLong(json.findPath("accountId").getTextValue());
        if (accountId == null) {
            return badRequest("Missing parameter [accountId]");
        } else {
            Future<Object> futureCheckingBalances = ask(ActorUtils.checkingAccountProxy, new ActorAccountProxies.GetAccountBalances(accountId), ActorUtils.ASK_TIMEOUT);

            mutableValue = "Real Value";

            Future<Status> statusFuture = futureCheckingBalances.map(new Mapper<Object, Status>() {
                public Status apply(Object obj) {
                    CheckingAccountBalances checkingAccountBalances = (CheckingAccountBalances) obj;
                    AccountBalances accountBalances = new AccountBalances(checkingAccountBalances, null, null);
                    JsonNode jsonNode = Json.toJson(accountBalances);
                    System.out.println("mutableValue = " + mutableValue);
                    return ok(jsonNode);
                }

            }, Akka.system().dispatcher());

            statusFuture = statusFuture.recover(new Recover<Status>() {
                public Status recover(Throwable problem) throws Throwable {
                    if (problem instanceof ActorAccountProxies.AccountNotFoundException) {
                        return badRequest("Account not found");
                    }
                    else {
                        return badRequest(problem.getMessage());
                    }
                }
            }, Akka.system().dispatcher());

            mutableValue = "Fake Value";

            return Await.result(statusFuture, ActorUtils.ASK_DURATION);
        }
    }


    @BodyParser.Of(BodyParser.Json.class)
    public static Result asyncGetAccountBalancesTwo() throws Exception {
        JsonNode json = request().body().asJson();
        Long accountId = Long.parseLong(json.findPath("accountId").getTextValue());
        if (accountId == null) {
            return badRequest("Missing parameter [accountId]");
        } else {
            Future<Object> future = ask(ActorUtils.accountBalanceActorService, new ActorAccountProxies.GetAccountBalances(accountId), ActorUtils.ASK_TIMEOUT);
            AccountBalances accountBalances = (AccountBalances) Await.result(future, ActorUtils.ASK_DURATION);
            JsonNode jsonNode = Json.toJson(accountBalances);
            return ok(jsonNode);
        }
    }


    @BodyParser.Of(BodyParser.Json.class)
    public static Result getAccountBalancesBlocking() throws InterruptedException {
        JsonNode json = request().body().asJson();
        Long accountId = Long.parseLong(json.findPath("accountId").getTextValue());
        if (accountId == null) {
            return badRequest("Missing parameter [accountId]");
        } else {
            AccountBalances customerAccountBalances = AccountBalanceBlockingService.getCustomerAccountBalances(Long.valueOf(accountId));
            JsonNode jsonNode = Json.toJson(customerAccountBalances);
            return ok(jsonNode);
        }
    }

}
