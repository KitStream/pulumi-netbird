using System.Collections.Generic;
using Pulumi;
using KitStream.Pulumi.Netbird;
using KitStream.Pulumi.Netbird.Inputs;

return await Deployment.RunAsync(() => 
{
    var group = new Group("example-group", new GroupArgs
    {
        Name = "Example DotNet account_settings Group",
    });

    var res = new AccountSettings("test-account-settings", new AccountSettingsArgs
    {
        PeerApprovalEnabled = true,
    });

    return new Dictionary<string, object?>
    {
        ["resourceName"] = res.PeerApprovalEnabled,
    };
});
