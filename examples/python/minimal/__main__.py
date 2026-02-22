import pulumi
import pulumi_netbird as netbird

group = netbird.Group("test-group",
    name="Pulumi Python Test Group")

pulumi.export("groupName", group.name)
