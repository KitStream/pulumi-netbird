package tests

import (
	"context"
	"errors"
	"net/http"
	"os"
	"os/exec"
	"path/filepath"
	"strings"
	"testing"
	"time"

	"github.com/pulumi/pulumi/pkg/v3/engine"
	"github.com/pulumi/pulumi/pkg/v3/testing/integration"
)

const (
	localMgmtURL   = "http://localhost:8080"
	localSeededPAT = "nbp_apTmlmUXHSC4PKmHwtIZNaGr8eqcVI2gMURp"
)

func haveDocker() bool {
	cmd := exec.Command("docker", "compose", "version")
	if err := cmd.Run(); err != nil {
		return false
	}
	return true
}

func upLocalStack(t *testing.T) func() {
	t.Helper()
	if !haveDocker() {
		t.Skip("docker compose not available; skipping local stack test")
	}
	composeFile := filepath.Join("..", "upstream", "test", "compose.yml")
	cmd := exec.Command("docker", "compose", "-f", composeFile, "up", "-d", "--wait")
	cmd.Stdout, cmd.Stderr = os.Stdout, os.Stderr
	if err := cmd.Run(); err != nil {
		t.Fatalf("failed to start local netbird stack: %v", err)
	}
	deadline := time.Now().Add(2 * time.Minute)
	for time.Now().Before(deadline) {
		ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
		req, _ := http.NewRequestWithContext(ctx, http.MethodGet, localMgmtURL+"/", nil)
		resp, err := http.DefaultClient.Do(req)
		cancel()
		if err == nil && resp != nil {
			break
		}
		time.Sleep(2 * time.Second)
	}
	return func() {
		down := exec.Command("docker", "compose", "-f", composeFile, "down", "-v", "--remove-orphans")
		down.Stdout, down.Stderr = os.Stdout, os.Stderr
		_ = down.Run()
	}
}

func providerPluginPath(t *testing.T) string {
	t.Helper()
	cwd, err := os.Getwd()
	if err != nil {
		t.Fatal(err)
	}
	p := filepath.Join(cwd, "..", "bin")
	binaryPath := filepath.Join(p, "pulumi-resource-netbird")
	if _, err := os.Stat(binaryPath); err != nil {
		t.Fatalf("provider plugin not found at %s; build it first (cd provider && go build -o ../bin/pulumi-resource-netbird ./cmd/pulumi-resource-netbird)", binaryPath)
	}
	return p
}

func TestGo_Minimal_LocalStack(t *testing.T) {
	cleanup := upLocalStack(t)
	if cleanup != nil {
		defer cleanup()
	}

	opts := &integration.ProgramTestOptions{
		Dir:         filepath.Join("..", "examples", "go", "minimal"),
		Quick:       true,
		SkipRefresh: true,
		LocalProviders: []integration.LocalDependency{{
			Package: "netbird",
			Path:    providerPluginPath(t),
		}},
		Env: []string{
			"NB_MANAGEMENT_URL=" + localMgmtURL,
			"NB_PAT=" + localSeededPAT,
			"PULUMI_CONFIG_PASSPHRASE=test",
		},
		PrePrepareProject: func(proj *engine.Projinfo) error {
			absSdkPath, err := filepath.Abs(filepath.Join("..", "sdk", "go", "index"))
			if err != nil {
				return err
			}
			goModPath := filepath.Join(proj.Root, "go.mod")
			content, err := os.ReadFile(goModPath)
			if err != nil {
				return err
			}
			newContent := strings.Replace(string(content), "../../../sdk/go/index", absSdkPath, 1)
			return os.WriteFile(goModPath, []byte(newContent), 0644)
		},
		ExtraRuntimeValidation: func(t *testing.T, stack integration.RuntimeValidationStackInfo) {
			if v, ok := stack.Outputs["groupName"]; !ok || v == nil {
				t.Fatalf("expected output groupName, got: %v", stack.Outputs)
			}
		},
	}

	integration.ProgramTest(t, opts)
}

func Test_Skip_If_No_PlugIn(t *testing.T) {
	// Provides a clearer error if someone runs `go test` without building the plugin first.
	if _, err := os.Stat(filepath.Join("..", "bin", "pulumi-resource-netbird")); err != nil && errors.Is(err, os.ErrNotExist) {
		t.Skip("provider plugin missing; build it first: cd provider && go build -o ../bin/pulumi-resource-netbird ./cmd/pulumi-resource-netbird")
	}
}
