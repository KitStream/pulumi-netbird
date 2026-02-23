package myproject;

import com.pulumi.Pulumi;
import io.github.kitstream.netbird.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var group = new Group("example-group", GroupArgs.builder()
                .name("Example Java route Group")
                .build());

            var res = new Route("test-route", RouteArgs.builder()
                .description("Pulumi Java Route")
                .enabled(true)
                .network("10.0.0.0/24")
                .networkId("test-route")
                .peerGroups(group.id().applyValue(List::of))
                .groups(group.id().applyValue(List::of))
                .build());

            ctx.export("resourceName", res.description());
        });
    }
}
