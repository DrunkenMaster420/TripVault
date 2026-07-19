import { useEffect, useState } from "react";
import {
  Container,
  Button,
  Card,
  CardContent,
  Chip,
  LinearProgress,
  Stack,
  Typography,
} from "@mui/material";

import PageLayout from "../components/layout/PageLayout";

import type { StorageAccount } from "../types/StorageAccount";
import {
  getGoogleLoginUrl,
  getStorageAccounts,
} from "../services/StorageAccountService";

const StorageAccountsPage = () => {
  const [storageAccounts, setStorageAccounts] = useState<StorageAccount[]>([]);

  const fetchStorageAccounts = async () => {
    try {
      const accounts = await getStorageAccounts();
      setStorageAccounts(accounts);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchStorageAccounts();
  }, []);

  const handleConnectGoogle = async () => {
    try {
      const url = await getGoogleLoginUrl();
      window.location.href = url;
    } catch (err) {
      console.error(err);
    }
  };

  const formatBytes = (bytes: number) => {
    if (bytes >= 1024 ** 4) return `${(bytes / 1024 ** 4).toFixed(2)} TB`;
    if (bytes >= 1024 ** 3) return `${(bytes / 1024 ** 3).toFixed(2)} GB`;
    if (bytes >= 1024 ** 2) return `${(bytes / 1024 ** 2).toFixed(2)} MB`;
    if (bytes >= 1024) return `${(bytes / 1024).toFixed(2)} KB`;
    return `${bytes} B`;
  };

  return (
    <PageLayout>
      <Container maxWidth="lg">
        <Stack
          direction="row"
          sx={{
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Typography
            sx={{
              fontWeight: 700,
            }}
          >
            Google Drive Accounts
          </Typography>

          <Button
            variant="contained"
            size="large"
            onClick={handleConnectGoogle}
          >
            Connect Google Account
          </Button>
        </Stack>

        <Stack spacing={3}>
          {storageAccounts.map((account) => {
            const percentage =
              account.totalQuota > 0
                ? (account.usedQuota / account.totalQuota) * 100
                : 0;

            return (
              <Card
                key={account.id}
                sx={{
                  borderRadius: 4,
                  boxShadow: 3,
                }}
              >
                <CardContent>
                  <Stack
                    direction="row"
                    sx={{
                      justifyContent: "space-between",
                      alignItems: "center",
                    }}
                  >
                    <Typography variant="h6">{account.googleEmail}</Typography>

                    <Chip
                      label={account.active ? "Active" : "Inactive"}
                      color={account.active ? "success" : "default"}
                    />
                  </Stack>

                  <LinearProgress
                    variant="determinate"
                    value={percentage}
                    sx={{
                      height: 10,
                      borderRadius: 10,
                      mb: 2,
                    }}
                  />

                  <Typography color="text.secondary">
                    {formatBytes(account.usedQuota)} /{" "}
                    {formatBytes(account.totalQuota)}
                  </Typography>

                  <Typography color="text.secondary">
                    {percentage.toFixed(2)}% Used
                  </Typography>
                </CardContent>
              </Card>
            );
          })}
        </Stack>
      </Container>
    </PageLayout>
  );
};

export default StorageAccountsPage;
