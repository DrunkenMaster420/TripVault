import { useEffect, useState } from "react";
import type { StorageAccount } from "../types/StorageAccount";
import { getStorageAccounts, getGoogleLoginUrl } from "../services/StorageAccountService";

import {
  Card,
  CardContent,
  Chip,
  Container,
  LinearProgress,
  Stack,
  Typography,
  Button,
} from "@mui/material";
import PageLayout from "../components/layout/PageLayout";

const StorageAccountsPage = () => {
  const [storageAccounts, setStorageAccounts] = useState<StorageAccount[]>([]);

  const fetchStorageAccounts = async () => {
    try {
      const accounts = await getStorageAccounts();
      setStorageAccounts(accounts);
    } catch (error) {
      console.error(error);
    }
  };

  const handleConnectGoogle = async () => {
    try {
      const url = await getGoogleLoginUrl();

      window.location.href = url;
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchStorageAccounts();
  }, []);

  const formatBytes = (bytes: number) => {
    if (bytes >= 1024 ** 4) {
      return `${(bytes / 1024 ** 4).toFixed(2)} TB`;
    }

    if (bytes >= 1024 ** 3) {
      return `${(bytes / 1024 ** 3).toFixed(2)} GB`;
    }

    if (bytes >= 1024 ** 2) {
      return `${(bytes / 1024 ** 2).toFixed(2)} MB`;
    }

    if (bytes >= 1024) {
      return `${(bytes / 1024).toFixed(2)} KB`;
    }

    return `${bytes} B`;
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      <Stack
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        sx={{ mb: 4 }}
      >
        <Typography variant="h4">Google Drive Accounts</Typography>

        <Button variant="contained" onClick={handleConnectGoogle}>
          Connect Google Account
        </Button>
      </Stack>

      {storageAccounts.map((account) => {
        const percentage = (account.usedQuota / account.totalQuota) * 100;

        return (
          <PageLayout>
            <Card key={account.id} sx={{ mb: 3 }}>
              <CardContent>
                <Stack
                  direction="row"
                  justifyContent="space-between"
                  alignItems="center"
                  sx={{ mb: 2 }}
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
                    borderRadius: 5,
                    mb: 2,
                  }}
                />

                <Typography variant="body2" color="text.secondary">
                  {formatBytes(account.usedQuota)} /{" "}
                  {formatBytes(account.totalQuota)}
                </Typography>

                <Typography
                  variant="body2"
                  color="text.secondary"
                  sx={{ mt: 1 }}
                >
                  {percentage.toFixed(2)}% Used
                </Typography>
              </CardContent>
            </Card>
          </PageLayout>
        );
      })}
          </Container>
          
  );
};

export default StorageAccountsPage;
