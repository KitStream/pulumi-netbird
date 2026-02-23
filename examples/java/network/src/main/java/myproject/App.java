package myproject;

import com.pulumi.Pulumi;
import io.github.kitstream.netbird.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var group = new Group("example-group", GroupArgs.builder()
                .name("Example Java network Group")
                .build());

            var res = new Network("test-network", NetworkArgs.builder()
                .name("Pulumi Java Network")
                .build());

            ctx.export("resourceName", res.name());
        });
    }
}
