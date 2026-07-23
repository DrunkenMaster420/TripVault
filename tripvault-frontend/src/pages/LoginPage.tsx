import { useState } from "react";
import {
  Box,
  Button,
  Paper,
  TextField,
  Typography,
  Link,
  IconButton,
  Tooltip,
  Alert,
} from "@mui/material";
import { login } from "../services/AuthService";
import { saveToken } from "../utils/AuthUtils";
import { useNavigate } from "react-router-dom";
import ArrowBackRoundedIcon from "@mui/icons-material/ArrowBackRounded";

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
        minHeight: "100vh",
        p: 2,
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
          TripVault Login
        </Typography>

        <TextField
          label="Username"
          fullWidth
          margin="normal"
          value={username}
          slotProps={{
            htmlInput: {
              style: { fontSize: "16px" },
            },
          }}
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
          slotProps={{
            htmlInput: {
              style: { fontSize: "16px" },
            },
          }}
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
          size="large"
          sx={{ mt: 3, py: 1.2 }}
          onClick={handleLogin}
        >
          Login
        </Button>
        <Typography align="center" sx={{ mt: 3 }}>
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
