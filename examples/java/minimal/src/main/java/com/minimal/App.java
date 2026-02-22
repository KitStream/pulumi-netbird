package com.minimal;

import com.pulumi.Pulumi;
import com.netbird.netbird.Group;
import com.netbird.netbird.GroupArgs;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var group = new Group("test-group", GroupArgs.builder()
                .name("Pulumi Java Test Group")
                .build());

            ctx.export("groupName", group.name());
        });
    }
}
