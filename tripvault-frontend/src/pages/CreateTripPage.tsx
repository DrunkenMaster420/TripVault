import { Box, Button, Paper, TextField, Typography } from '@mui/material';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createTrip } from '../services/TripService';
import PageLayout from '../components/layout/PageLayout';


//
const CreateTripPage = () => {


    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const navigate = useNavigate();

    const handleCreateTrip = async () => {

        try {

           await createTrip(name, description);
            navigate("/dashboard");
            
        } catch (error) {
            console.error(error);
        }


    };

  return (
    <PageLayout>
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          minHeight: "80vh",
        }}
      >
        <Paper
          elevation={0}
          sx={{
            width: 550,
            p: 5,
            borderRadius: 5,
            border: "1px solid #E5E7EB",
            backdropFilter: "blur(20px)",
            boxShadow: "0 20px 60px rgba(0,0,0,0.08)",
          }}
        >
          <Typography variant="h3" fontWeight={700} sx={{ mb: 1 }}>
            Create a Trip
          </Typography>

          <Typography color="text.secondary" sx={{ mb: 4 }}>
            Create a shared workspace where everyone can upload, organize and
            access memories together.
          </Typography>

          <TextField
            label="Trip Name"
            fullWidth
            value={name}
            onChange={(e) => setName(e.target.value)}
            sx={{
              mb: 3,
              "& .MuiOutlinedInput-root": {
                borderRadius: 3,
              },
            }}
          />

          <TextField
            label="Description"
            multiline
            rows={4}
            fullWidth
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            sx={{
              "& .MuiOutlinedInput-root": {
                borderRadius: 3,
              },
            }}
          />

          <Button
            variant="contained"
            fullWidth
            size="large"
            sx={{
              mt: 4,
              py: 1.6,
              borderRadius: 3,
              textTransform: "none",
              fontSize: "1rem",
              fontWeight: 600,
              background: "linear-gradient(135deg,#2563eb,#7c3aed)",

              "&:hover": {
                background: "linear-gradient(135deg,#1d4ed8,#6d28d9)",
              },
            }}
            onClick={handleCreateTrip}
          >
            Create Trip
          </Button>
        </Paper>
      </Box>
    </PageLayout>
  );
}

export default CreateTripPage
