import * as netbird from "@pulumi/netbird";

const group = new netbird.Group("test-group", {
    name: "Pulumi NodeJS Test Group",
});

export const groupName = group.name;
