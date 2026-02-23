package myproject;

import com.pulumi.Pulumi;
import io.github.kitstream.netbird.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var group = new Group("example-group", GroupArgs.builder()
                .name("Example Java dns_settings Group")
                .build());

            var res = new DnsSettings("test-dns-settings", DnsSettingsArgs.builder()
                .disabledManagementGroups(group.id().applyValue(List::of))
                .build());

            ctx.export("resourceName", res.disabledManagementGroups());
        });
    }
}
