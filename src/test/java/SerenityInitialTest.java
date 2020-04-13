import facts.NetflixPlans;
import model.user.Datum;
import model.user.RegisterUserInfo;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import questions.GetUsersQuestion;
import questions.ResponseCode;
import tasks.GetUsers;
import tasks.RegisterUser;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.*;

@RunWith(SerenityRunner.class)
public class SerenityInitialTest {

    public static final String restApiUrl = "http://localhost:5000/api";

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
                seeThat("get email for user id 1", act -> user.getEmail(),equalTo("george.bluth@reqres.in"))
               // seeThat("verify the avatar",act->user.getAvatar(),containsString("avatar"))
        );
    }

    @Test
    public void registerUserTest() {
        Actor tom  = Actor.named("Tom")
                .whoCan(CallAnApi.at(restApiUrl));

        String registerUserInfo = "{\n" +
                "\t\"name\":\"tom\",\n" +
                "\t\"Job\":\"leader\",\n" +
                "\t\"email\":\"tracey.ramos@reqres.in\",\n" +
                "\t\"password\":\"serenity\"\n" +
                "}";
        tom.attemptsTo(
                RegisterUser.withInfo(registerUserInfo)
        );

        tom.should(
                seeThat("will response OK",new ResponseCode(),equalTo(200))
        );
    }

    @Test
    public void registerUserTest2() {
        Actor tom = Actor.named("tom")
                .whoCan(CallAnApi.at(restApiUrl));

        RegisterUserInfo registerUserInfo = new RegisterUserInfo();

        registerUserInfo.setName("James");
        registerUserInfo.setJob("leader");
        registerUserInfo.setEmail("michael.lawson@reqres.in");
        registerUserInfo.setPassword("serenity");

        tom.attemptsTo(
                RegisterUser.withInfo(registerUserInfo)
        );

        tom.should(
                seeThat("verify response code",new ResponseCode(),equalTo(200))
        );
    }

    @Test
    public void factTest(){
        Actor tom = Actor.named("Tom").whoCan(CallAnApi.at(restApiUrl));
        tom.has(NetflixPlans.toViewSeries());
    }
}
