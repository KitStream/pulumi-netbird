# Releasing the NetBird Pulumi Provider

Releases are automated via GitHub Actions. A full release is triggered by pushing a version tag.

## 1. GitHub Secrets

The following secrets must be configured in **Settings → Secrets → Actions**:

| Secret | Description |
| :--- | :--- |
| `NUGET_USERNAME` | NuGet account username (used for OIDC login via `NuGet/login@v1`) |
| `OSSRH_USERNAME` | Sonatype Central Portal user-token username |
| `OSSRH_TOKEN` | Sonatype Central Portal user-token password |
| `JAVA_SIGNING_KEY` | Armored GPG private key block (for Maven Central artifact signing) |
| `JAVA_SIGNING_PASSWORD` | Passphrase for the GPG key |

> **Note:** npm and PyPI use OIDC trusted publishing — no static tokens are needed.
> `GITHUB_TOKEN` is provided automatically by GitHub Actions.

## 2. Triggering a Release

1. Ensure all changes are pushed to `main`.
2. Tag and push:
   ```bash
   git tag v0.0.1
   git push origin v0.0.1
   ```
3. Monitor progress in the [Actions tab](https://github.com/KitStream/netbird-pulumi-provider/actions).
