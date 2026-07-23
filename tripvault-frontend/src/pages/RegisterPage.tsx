import {
  Box,
  Button,
  Paper,
  TextField,
  Typography,
  Link,
  IconButton,
  Tooltip,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { register } from "../services/AuthService";
import ArrowBackRoundedIcon from "@mui/icons-material/ArrowBackRounded";

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
        minHeight: "100vh",
        p: 2,
        py: { xs: 8, sm: 4 },
        background: "linear-gradient(135deg,#020617,#0f172a,#1e293b)",
        position: "relative",
      }}
    >
      <Tooltip title="Back to Home">
        <IconButton
          onClick={() => navigate("/")}
          sx={{
            position: "absolute",
            top: { xs: 16, sm: 24 },
            left: { xs: 16, sm: 24 },
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
          p: { xs: 3, sm: 4 },
          width: "100%",
          maxWidth: 400,
          borderRadius: 4,
          backdropFilter: "blur(20px)",
          background: "rgba(255,255,255,.08)",
          border: "1px solid rgba(255,255,255,.1)",
        }}
      >
        <Typography
          variant="h4"
          gutterBottom
          sx={{ fontSize: { xs: "1.75rem", sm: "2.125rem" } }}
        >
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
          slotProps={{
            htmlInput: {
              style: { fontSize: "16px" },
            },
          }}
          onChange={(e) => setName(e.target.value)}
        />

        <TextField
          fullWidth
          margin="normal"
          label="Username"
          value={username}
          slotProps={{
            htmlInput: {
              style: { fontSize: "16px" },
            },
          }}
          onChange={(e) => setUsername(e.target.value)}
        />

        <TextField
          fullWidth
          margin="normal"
          label="Email"
          type="email"
          value={email}
          slotProps={{
            htmlInput: {
              style: { fontSize: "16px" },
            },
          }}
          onChange={(e) => setEmail(e.target.value)}
        />

        <TextField
          fullWidth
          margin="normal"
          label="Password"
          type="password"
          value={password}
          slotProps={{
            htmlInput: {
              style: { fontSize: "16px" },
            },
          }}
          onChange={(e) => setPassword(e.target.value)}
        />

        <TextField
          fullWidth
          margin="normal"
          label="Confirm Password"
          type="password"
          value={confirmPassword}
          slotProps={{
            htmlInput: {
              style: { fontSize: "16px" },
            },
          }}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />

        <Button
          fullWidth
          variant="contained"
          size="large"
          sx={{ mt: 3, py: 1.2 }}
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
