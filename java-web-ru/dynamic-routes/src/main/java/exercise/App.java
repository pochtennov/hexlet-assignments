package exercise;

import io.javalin.Javalin;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// BEGIN

// END

public final class App {

    private static final List<Map<String, String>> COMPANIES = Data.getCompanies();

    public static Javalin getApp() {

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/companies/{id}", ctx -> {
            var id = ctx.pathParam("id");
            Optional<Map<String, String>> company = COMPANIES.stream().filter(comp -> comp.get("id").equals(id)).findFirst();
            if (company.isPresent()) {
                ctx.json(company.get());
            } else {
                ctx.result("Company not found");
                ctx.status(404);
            }

        });

        app.get("/companies", ctx -> {
            ctx.json(COMPANIES);
        });

        app.get("/", ctx -> {
            ctx.result("open something like (you can change id): /companies/5");
        });

        return app;

    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(7070);
    }
}
