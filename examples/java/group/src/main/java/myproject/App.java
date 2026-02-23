package myproject;

import com.pulumi.Pulumi;
import io.github.kitstream.netbird.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var res = new Group("test-group", GroupArgs.builder()
                .name("Pulumi Java Group")
                .build());

            ctx.export("resourceName", res.name());
        });
    }
}
