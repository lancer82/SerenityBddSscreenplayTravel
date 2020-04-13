package interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.RestInteraction;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Post extends RestInteraction {
    private final String resource;

    public Post(String resource) {
        this.resource = resource;
    }

    @Step("{0} executes a POST on the resource #resource")
    public <T extends Actor> void performAs(T actor) {
        this.rest().log().all().post(CallAnApi.as(actor).resolve(this.resource)).then().log().all();
    }

    public static Post to(String resource) {
        return instrumented(Post.class, resource);
    }
}
