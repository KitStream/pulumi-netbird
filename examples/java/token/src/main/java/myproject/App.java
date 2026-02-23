package myproject;

import com.pulumi.Pulumi;
import io.github.kitstream.netbird.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var group = new Group("example-group", GroupArgs.builder()
                .name("Example Java token Group")
                .build());

            var user = new User("example-user", UserArgs.builder()
                .email("pulumi-java-token-test@example.com")
                .name("Pulumi Token Test User")
                .isServiceUser(true)
                .build());

            var res = new PersonalAccessToken("test-token", PersonalAccessTokenArgs.builder()
                .name("Pulumi Java Token")
                .expirationDays(30)
                .userId(user.id())
                .build());

            ctx.export("resourceName", res.name());
        });
    }
}
