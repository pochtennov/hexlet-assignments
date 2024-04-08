package exercise;

import exercise.dto.users.UsersPage;
import io.javalin.Javalin;
import java.util.List;
import io.javalin.http.NotFoundResponse;
import exercise.model.User;
import exercise.dto.users.UserPage;
import static io.javalin.rendering.template.TemplateUtil.model;
import io.javalin.rendering.template.JavalinJte;

public final class App {

    // Каждый пользователь представлен объектом класса User
    private static final List<User> USERS = Data.getUsers();

    public static Javalin getApp() {

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get("/users/{id}", ctx -> {
            var id = ctx.pathParamAsClass("id", Long.class);
            var user = USERS.stream().filter(u -> id.get() == u.getId()).findFirst();/* Курс извлекается из базы данных. Как работать с базами данных мы разберем в следующих уроках */
            if (user.isEmpty()) {
                throw new NotFoundResponse("User not found");
            }
            var page = new UserPage(user.get());
            ctx.render("users/show.jte", model("page", page));
        });

        app.get("/users", ctx -> {
            var page = new UsersPage(USERS);
            ctx.render("users/index.jte", model("page", page));
        });

        app.get("/", ctx -> {
            ctx.render("index.jte");
        });

        return app;
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(7070);
    }
}
