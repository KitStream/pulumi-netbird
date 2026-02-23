package myproject;

import com.pulumi.Pulumi;
import io.github.kitstream.netbird.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var group = new Group("example-group", GroupArgs.builder()
                .name("Example Java account_settings Group")
                .build());

            var res = new AccountSettings("test-account-settings", AccountSettingsArgs.builder()
                .peerApprovalEnabled(true)
                .build());

            ctx.export("resourceName", res.peerApprovalEnabled());
        });
    }
}
