package myproject;

import com.pulumi.Pulumi;
import io.github.kitstream.netbird.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var group = new Group("example-group", GroupArgs.builder()
                .name("Example Java network_resource Group")
                .build());

            var network = new Network("example-network", NetworkArgs.builder()
                .name("Example Java network_resource Network")
                .build());

            var res = new NetworkResource("test-network-resource", NetworkResourceArgs.builder()
                .name("Pulumi Java Net Res")
                .address("10.20.0.0/24")
                .networkId(network.id())
                .groups(group.id().applyValue(List::of))
                .build());

            ctx.export("resourceName", res.name());
        });
    }
}
