import { useEffect, useState } from "react";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Snackbar,
  Stack,
  Typography,
} from "@mui/material";

import PageLayout from "../components/layout/PageLayout";

import {
  getMyInvitations,
  acceptInvitation,
  rejectInvitation,
} from "../services/InvitationService";

import type { Invitation } from "../types/Invitation";
import { useNavigate } from "react-router-dom";

const InvitationsPage = () => {
  const [invitations, setInvitations] = useState<Invitation[]>([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const loadInvitations = async () => {
    try {
      const data = await getMyInvitations();
      setInvitations(data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadInvitations();
  }, []);

  const handleAccept = async (id: number) => {
    try {
      await acceptInvitation(id);

      setMessage("Invitation accepted.");

      navigate("/dashboard");

      setSnackbarOpen(true);

      loadInvitations();
    } catch (err) {
      console.error(err);
    }
  };

  const handleReject = async (id: number) => {
    try {
      await rejectInvitation(id);

      setMessage("Invitation rejected.");

      setSnackbarOpen(true);

      loadInvitations();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <PageLayout>
      <Box
        sx={{
          maxWidth: 800,
          mx: "auto",
        }}
      >
        <Typography
          sx={{
            mb: 4,
            mt: 2,
          }}
        >
          Trip Invitations
        </Typography>

        {invitations.length === 0 && (
          <Typography color="text.secondary">
            No pending invitations.
          </Typography>
        )}

        <Stack spacing={3}>
          {invitations.map((invitation) => (
            <Card key={invitation.id}>
              <CardContent>
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: 700,
                  }}
                >
                  {invitation.tripName}
                </Typography>

                <Typography color="text.secondary" sx={{ mt: 1 }}>
                  {invitation.senderName} invited you
                </Typography>

                <Typography sx={{ mt: 2 }}>
                  💾 {invitation.allocatedQuota} GB allocated
                </Typography>

                <Typography variant="caption" color="text.secondary">
                  {new Date(invitation.createdAt).toLocaleString()}
                </Typography>

                <Stack
                  sx={{
                    mt: 3,
                  }}
                >
                  <Button
                    variant="contained"
                    onClick={() => handleAccept(invitation.id)}
                  >
                    Accept
                  </Button>

                  <Button
                    variant="outlined"
                    color="error"
                    onClick={() => handleReject(invitation.id)}
                  >
                    Reject
                  </Button>
                </Stack>
              </CardContent>
            </Card>
          ))}
        </Stack>

        <Snackbar
          open={snackbarOpen}
          autoHideDuration={3000}
          onClose={() => setSnackbarOpen(false)}
        >
          <Alert severity="success">{message}</Alert>
        </Snackbar>
      </Box>
    </PageLayout>
  );
};

export default InvitationsPage;
