package controllers;

import models.AccountBalances;
import org.codehaus.jackson.JsonNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.Await;
import scala.concurrent.Future;
import views.html.getAccountBalances;
import views.html.index;

import static akka.pattern.Patterns.ask;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result getAccountBalances() {
        return ok(getAccountBalances.render());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result asyncGetAccountBalances() throws Exception {
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
