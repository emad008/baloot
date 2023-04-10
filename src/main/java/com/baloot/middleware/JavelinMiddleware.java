package com.baloot.middleware;

import com.baloot.exception.BalootException;
import io.javalin.http.Context;
import org.json.JSONObject;

import java.util.function.Function;

public class JavelinMiddleware {
    private final Function<JSONObject, JSONObject> toCall;

    private final String templatePath;

    public JavelinMiddleware(
        Function<JSONObject, JSONObject> toCall,
        String templatePath
    ) {
        this.toCall = toCall;
        this.templatePath = templatePath;
    }

    public Context middleware(Context context) {
        JSONObject data = new JSONObject();

        for (String pathParam: context.pathParamMap().keySet())
            data.put(pathParam, context.pathParam(pathParam));

        for (String pathParam: context.queryParamMap().keySet())
            data.put(pathParam, context.queryParam(pathParam));

        try {
            JSONObject body = context.bodyAsClass(JSONObject.class);

            for (String bodyParam : body.keySet())
                data.put(bodyParam, body.get(bodyParam));
        }
        catch (Exception ex) {

        }

        JSONObject res;
        try {
            res = this.toCall.apply(data);
        }
        catch (BalootException ex) {
            res = new JSONObject();
            res.put("exception", ex.getMessage());
            context.status(400);
            return context.render("templates/400.html");
        }
        catch (Exception ex) {
            context.status(500);
            return context.render("templates/500.html");
        }

        return context.render(this.templatePath, res.toMap());
    }
}
