package myproject;

import com.pulumi.Pulumi;
import io.github.kitstream.netbird.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var group = new Group("example-group", GroupArgs.builder()
                .name("Example Java network_router Group")
                .build());

            var network = new Network("example-network", NetworkArgs.builder()
                .name("Example Java network_router Network")
                .build());

            var res = new NetworkRouter("test-network-router", NetworkRouterArgs.builder()
                .networkId(network.id())
                .peerGroups(group.id().applyValue(List::of))
                .build());

            ctx.export("resourceName", res.networkId());
        });
    }
}
