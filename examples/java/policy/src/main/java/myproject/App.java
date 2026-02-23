package myproject;

import com.pulumi.Pulumi;
import io.github.kitstream.netbird.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var group = new Group("example-group", GroupArgs.builder()
                .name("Example Java policy Group")
                .build());

            var res = new Policy("test-policy", PolicyArgs.builder()
                .name("Pulumi Java Policy")
                .enabled(true)
                .rule(PolicyRuleArgs.builder().action("accept").enabled(true).name("rule1").sources(List.of(group.id())).destinations(List.of(group.id())).build())
                .build());

            ctx.export("resourceName", res.name());
        });
    }
}
