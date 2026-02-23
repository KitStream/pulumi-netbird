package myproject;

import com.pulumi.Pulumi;
import io.github.kitstream.netbird.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var group = new Group("example-group", GroupArgs.builder()
                .name("Example Java user Group")
                .build());

            var res = new User("test-user", UserArgs.builder()
                .email("pulumi-Java@example.com")
                .name("Pulumi Java User")
                .isServiceUser(true)
                .build());

            ctx.export("resourceName", res.email());
        });
    }
}
