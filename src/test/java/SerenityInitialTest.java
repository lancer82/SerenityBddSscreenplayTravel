import model.user.Datum;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Consequence;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import questions.GetUsersQuestion;
import questions.ResponseCode;
import tasks.GetUsers;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;

@RunWith(SerenityRunner.class)
public class SerenityInitialTest {

    public static final String restApiUrl = "http://localhost:5000/api/";

    @Test
    public void getUsers() {
        Actor tom = Actor.named("tom the trainer")
                .whoCan(CallAnApi.at(restApiUrl));

        tom.attemptsTo(
                GetUsers.fromPage(1)
        );

        tom.should(
                seeThat("1st request has benn response as", new ResponseCode(),equalTo(200))
        );

        Datum user = new GetUsersQuestion().answeredBy(tom)
                .getData().stream().filter(x-> x.getId() == 1).findFirst().orElse(null);

        tom.should(
                seeThat("response body not empty",act -> user,notNullValue())
        );

        tom.should(
                seeThat("get email for user id 1", act -> user.getEmail(),equalTo("george.bluth@reqres.in")),
                seeThat("verify the avatar",act->user.getAvatar(),containsString("avatar"))
        );
    }
}
