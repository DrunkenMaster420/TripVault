import { useState } from "react";
import { Box, Button, Paper, TextField, Typography } from "@mui/material";
import { login } from "../services/AuthService";
import { saveToken } from "../utils/AuthUtils";
import { useNavigate } from "react-router-dom";
import { Link } from "@mui/material";
import ArrowBackRoundedIcon from "@mui/icons-material/ArrowBackRounded";
import { IconButton, Tooltip } from "@mui/material";
import { Alert } from "@mui/material";

function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async () => {
    setError("");

    if (!username.trim() || !password.trim()) {
      setError("Username and password are required.");
      return;
    }

    try {
      const response = await login(username, password);
      saveToken(response.token);
      navigate("/dashboard");
    } catch (err: any) {
      console.error(err);

      if (err.response?.status === 401) {
        setError("Invalid username or password.");
      } else {
        setError("Something went wrong. Please try again.");
      }
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
          TripVault Login
        </Typography>

        <TextField
          label="Username"
          fullWidth
          margin="normal"
          value={username}
          onChange={(e) => {
            setUsername(e.target.value);
            setError("");
          }}
        />

        <TextField
          label="Password"
          type="password"
          fullWidth
          margin="normal"
          value={password}
          onChange={(e) => {
            setPassword(e.target.value);
            setError("");
          }}
        />

        {error && (
          <Alert severity="error" sx={{ mt: 2 }}>
            {error}
          </Alert>
        )}
        <Button
          variant="contained"
          fullWidth
          sx={{ mt: 2 }}
          onClick={handleLogin}
        >
          Login
        </Button>
        <Typography align="center" sx={{ mt: 2 }}>
          Don't have an account?{" "}
          <Link
            component="button"
            underline="hover"
            onClick={() => navigate("/register")}
          >
            Create Account
          </Link>
        </Typography>
      </Paper>
    </Box>
  );
}

export default LoginPage;
