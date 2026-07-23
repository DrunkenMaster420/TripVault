import {
  Box,
  Button,
  Card,
  CardContent,
  Grid,
  Stack,
  Typography,
} from "@mui/material";
import { Add, Folder, Storage, CloudDone } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getTrips } from "../services/TripService";
import PageLayout from "../components/layout/PageLayout";
import type { User } from "../types/User";
import { getCurrentUser } from "../services/UserService";

function DashboardPage() {
  const navigate = useNavigate();

  const [trips, setTrips] = useState<any[]>([]);
  const [user, setUser] = useState<User | null>(null);

  const loadTrips = async () => {
    try {
      const [tripData, userData] = await Promise.all([
        getTrips(),
        getCurrentUser(),
      ]);

      setTrips(tripData);
      setUser(userData);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    loadTrips();
  }, []);

  return (
    <PageLayout>
      {/* Hero Section */}
      <Box
        sx={{
          mb: { xs: 3, sm: 6 },
          display: "flex",
          flexDirection: { xs: "column", sm: "row" },
          justifyContent: "space-between",
          alignItems: { xs: "flex-start", sm: "center" },
          gap: 2,
        }}
      >
        <Box>
          <Typography
            variant="h3"
            sx={{
              fontWeight: 700,
              fontSize: { xs: "1.75rem", sm: "2.5rem", md: "3rem" },
            }}
          >
            Welcome{user ? `, ${user.username}` : ""} 👋
          </Typography>

          <Typography
            variant="h6"
            color="text.secondary"
            sx={{
              mt: 1,
              maxWidth: 600,
              fontSize: { xs: "0.95rem", sm: "1.25rem" },
            }}
          >
            Your memories, intelligently distributed across multiple cloud
            accounts.
          </Typography>
        </Box>

        <Button
          variant="contained"
          size="large"
          startIcon={<Add />}
          onClick={() => navigate("/create-trip")}
          sx={{
            width: { xs: "100%", sm: "auto" },
            py: 1.2,
          }}
        >
          Create Trip
        </Button>
      </Box>

      {/* Statistics */}
      <Grid container spacing={{ xs: 2, sm: 3 }} sx={{ mb: { xs: 4, sm: 6 } }}>
        <Grid size={{ xs: 12, md: 4 }}>
          <Card sx={{ p: 1, height: "100%" }}>
            <CardContent>
              <Folder fontSize="large" color="primary" />
              <Typography
                variant="h3"
                sx={{
                  mt: 2,
                  fontWeight: 700,
                  fontSize: { xs: "2rem", sm: "3rem" },
                }}
              >
                {trips.length}
              </Typography>
              <Typography color="text.secondary">Total Trips</Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid size={{ xs: 12, md: 4 }}>
          <Card sx={{ p: 1, height: "100%" }}>
            <CardContent>
              <Storage fontSize="large" color="primary" />
              <Typography
                variant="h3"
                sx={{
                  mt: 2,
                  fontWeight: 700,
                  fontSize: { xs: "2rem", sm: "3rem" },
                }}
              >
                ∞
              </Typography>
              <Typography color="text.secondary">
                Distributed Storage
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid size={{ xs: 12, md: 4 }}>
          <Card sx={{ p: 1, height: "100%" }}>
            <CardContent>
              <CloudDone fontSize="large" color="primary" />
              <Typography
                variant="h3"
                sx={{
                  mt: 2,
                  fontWeight: 700,
                  fontSize: { xs: "2rem", sm: "3rem" },
                }}
              >
                Active
              </Typography>
              <Typography color="text.secondary">Cloud Sync</Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Trips Section */}
      <Typography
        variant="h4"
        sx={{
          mb: 3,
          fontWeight: 700,
          fontSize: { xs: "1.5rem", sm: "2.125rem" },
        }}
      >
        My Trips
      </Typography>

      <Grid container spacing={{ xs: 2, sm: 3 }}>
        {trips.map((trip: any) => (
          <Grid key={trip.id} size={{ xs: 12, md: 6, lg: 4 }}>
            <Card
              sx={{
                height: "100%",
                cursor: "pointer",
                transition: ".3s",

                "&:hover": {
                  transform: "translateY(-4px)",
                  boxShadow: "0 20px 40px rgba(91,140,255,.25)",
                },
              }}
            >
              <CardContent>
                <Typography variant="h5" sx={{ fontWeight: 700 }}>
                  {trip.name}
                </Typography>

                <Typography
                  color="text.secondary"
                  sx={{
                    mt: 2,
                    minHeight: { xs: "auto", sm: 60 },
                  }}
                >
                  {trip.description}
                </Typography>

                <Stack
                  direction="row"
                  sx={{
                    mt: 3,
                    justifyContent: "space-between",
                    alignItems: "center",
                  }}
                >
                  <Typography variant="body2" color="text.secondary">
                    Distributed Storage
                  </Typography>

                  <Button
                    variant="contained"
                    onClick={() => navigate(`/trips/${trip.id}`)}
                  >
                    Open →
                  </Button>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {trips.length === 0 && (
        <Card
          sx={{
            mt: 4,
            p: { xs: 3, sm: 6 },
            textAlign: "center",
          }}
        >
          <Typography variant="h5" gutterBottom>
            No trips yet
          </Typography>

          <Typography color="text.secondary" sx={{ mb: 3 }}>
            Create your first trip and start storing memories across multiple
            Google Drive accounts.
          </Typography>

          <Button
            variant="contained"
            startIcon={<Add />}
            onClick={() => navigate("/create-trip")}
            sx={{ width: { xs: "100%", sm: "auto" } }}
          >
            Create Your First Trip
          </Button>
        </Card>
      )}
    </PageLayout>
  );
}

export default DashboardPage;
