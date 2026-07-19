import { Box, Button, Paper, TextField, Typography, Link } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { register } from "../services/AuthService";
import ArrowBackRoundedIcon from "@mui/icons-material/ArrowBackRounded";
import { IconButton, Tooltip } from "@mui/material";

function RegisterPage() {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleRegister = async () => {
    if (password !== confirmPassword) {
      alert("Passwords do not match");
      return;
    }

    try {
      await register({
        name,
        username,
        email,
        password,
      });

      alert("Registration successful!");

      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Registration failed");
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
        background: "linear-gradient(135deg,#020617,#0f172a,#1e293b)",
        position: "relative",
      }}
    >
      <Tooltip title="Back to Home">
        <IconButton
          onClick={() => navigate("/")}
          sx={{
            position: "absolute",
            top: 24,
            left: 24,
            color: "white",
            background: "rgba(255,255,255,0.08)",
            backdropFilter: "blur(12px)",
            border: "1px solid rgba(255,255,255,0.1)",
            "&:hover": {
              background: "rgba(255,255,255,0.16)",
              transform: "translateX(-2px)",
            },
            transition: "all .25s ease",
          }}
        >
          <ArrowBackRoundedIcon />
        </IconButton>
      </Tooltip>
      <Paper
        elevation={0}
        sx={{
          p: 4,
          width: 400,
          borderRadius: 4,
          backdropFilter: "blur(20px)",
          background: "rgba(255,255,255,.08)",
          border: "1px solid rgba(255,255,255,.1)",
        }}
      >
        <Typography variant="h4" gutterBottom>
          TripVault
        </Typography>

        <Typography color="text.secondary" sx={{ mb: 2 }}>
          Create your free account
        </Typography>

        <TextField
          fullWidth
          margin="normal"
          label="Full Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <TextField
          fullWidth
          margin="normal"
          label="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <TextField
          fullWidth
          margin="normal"
          label="Email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <TextField
          fullWidth
          margin="normal"
          label="Password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <TextField
          fullWidth
          margin="normal"
          label="Confirm Password"
          type="password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />

        <Button
          fullWidth
          variant="contained"
          sx={{ mt: 3 }}
          onClick={handleRegister}
        >
          Create Account
        </Button>

        <Typography align="center" sx={{ mt: 3 }}>
          Already have an account?{" "}
          <Link
            component="button"
            underline="hover"
            onClick={() => navigate("/login")}
          >
            Login
          </Link>
        </Typography>
      </Paper>
    </Box>
  );
}

export default RegisterPage;
