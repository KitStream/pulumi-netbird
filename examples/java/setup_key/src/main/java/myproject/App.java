package myproject;

import com.pulumi.Pulumi;
import io.github.kitstream.netbird.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var group = new Group("example-group", GroupArgs.builder()
                .name("Example Java setup_key Group")
                .build());

            var res = new SetupKey("test-setup-key", SetupKeyArgs.builder()
                .name("Pulumi Java Setup Key")
                .type("reusable")
                .expirySeconds(86400)
                .autoGroups(group.id().applyValue(List::of))
                .build());

            ctx.export("resourceName", res.name());
        });
    }
}
