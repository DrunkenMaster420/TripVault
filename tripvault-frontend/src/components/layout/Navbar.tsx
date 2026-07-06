import { AppBar, Toolbar, Typography, Button, Stack } from "@mui/material";
import { useNavigate, useLocation } from "react-router-dom";
import { removeToken } from "../../utils/AuthUtils";

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const logout = () => {
    removeToken();
    navigate("/");
  };

  return (
    <AppBar
      position="sticky"
      elevation={0}
      sx={{
        background: "rgba(10,15,30,.75)",
        backdropFilter: "blur(18px)",
        borderBottom: "1px solid rgba(255,255,255,.08)",
      }}
    >
      <Toolbar
        sx={{
          justifyContent: "space-between",
        }}
      >
        <Typography
          variant="h5"
          sx={{
            fontWeight: 700,
            cursor: "pointer",
          }}
          onClick={() => navigate("/dashboard")}
        >
          TripVault
        </Typography>

        <Stack direction="row" spacing={2}>
          <Button
            color={location.pathname === "/dashboard" ? "primary" : "inherit"}
            onClick={() => navigate("/dashboard")}
          >
            Dashboard
          </Button>

          <Button
            color={
              location.pathname === "/storage-accounts" ? "primary" : "inherit"
            }
            onClick={() => navigate("/storage-accounts")}
          >
            Storage
          </Button>

          <Button color="inherit">AI</Button>

          <Button variant="contained" color="error" onClick={logout}>
            Logout
          </Button>
        </Stack>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
