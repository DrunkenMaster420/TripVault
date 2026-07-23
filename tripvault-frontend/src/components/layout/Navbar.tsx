import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Stack,
  IconButton,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Badge,
  Box,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import CloseIcon from "@mui/icons-material/Close";
import { useNavigate, useLocation } from "react-router-dom";
import { removeToken } from "../../utils/AuthUtils";
import { useEffect, useState } from "react";
import { getMyInvitations } from "../../services/InvitationService";

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [invitationCount, setInvitationCount] = useState(0);
  const [mobileOpen, setMobileOpen] = useState(false);

  useEffect(() => {
    const loadCount = async () => {
      try {
        const invitations = await getMyInvitations();
        setInvitationCount(invitations.length);
      } catch (error) {
        console.error(error);
      }
    };

    loadCount();
  }, []);

  const logout = () => {
    removeToken();
    navigate("/");
  };

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const handleNavClick = (path: string) => {
    navigate(path);
    setMobileOpen(false);
  };

  const navItems = [
    { label: "Dashboard", path: "/dashboard" },
    { label: "Storage", path: "/storage-accounts" },
    { label: "AI", path: "#" },
  ];

  return (
    <>
      <AppBar
        position="sticky"
        elevation={0}
        sx={{
          background: "rgba(10,15,30,.75)",
          backdropFilter: "blur(18px)",
          borderBottom: "1px solid rgba(255,255,255,.08)",
        }}
      >
        <Toolbar sx={{ justifyContent: "space-between", px: { xs: 2, sm: 3 } }}>
          <Typography
            variant="h5"
            sx={{
              fontWeight: 700,
              cursor: "pointer",
              fontSize: { xs: "1.25rem", sm: "1.5rem" },
            }}
            onClick={() => navigate("/dashboard")}
          >
            TripVault
          </Typography>

          {/* Desktop Navigation */}
          {/* Desktop Navigation */}
          <Stack
            direction="row"
            spacing={2}
            sx={{
              display: { xs: "none", md: "flex" },
              alignItems: "center", // Moved inside sx
            }}
          >
            {navItems.map((item) => (
              <Button
                key={item.label}
                color={location.pathname === item.path ? "primary" : "inherit"}
                onClick={() => item.path !== "#" && navigate(item.path)}
              >
                {item.label}
              </Button>
            ))}

            <Badge
              badgeContent={invitationCount}
              color="error"
              invisible={invitationCount === 0}
            >
              <Button
                variant="contained"
                onClick={() => navigate("/invitations")}
              >
                Invitations
              </Button>
            </Badge>

            <Button variant="contained" color="error" onClick={logout}>
              Logout
            </Button>
          </Stack>

          {/* Mobile Menu Icon Button */}
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="end"
            onClick={handleDrawerToggle}
            sx={{ display: { md: "none" } }}
          >
            <Badge
              badgeContent={invitationCount}
              color="error"
              invisible={invitationCount === 0}
            >
              <MenuIcon />
            </Badge>
          </IconButton>
        </Toolbar>
      </AppBar>

      {/* Mobile Slide-out Drawer */}
      {/* Mobile Slide-out Drawer */}
      <Drawer
        anchor="right"
        open={mobileOpen}
        onClose={handleDrawerToggle}
        slotProps={{
          paper: {
            sx: {
              width: "80%",
              maxWidth: 300,
              bgcolor: "rgba(10,15,30,.95)",
              backdropFilter: "blur(18px)",
              color: "white",
              p: 2,
            },
          },
        }}
      >
        <Box sx={{ display: "flex", justifyContent: "flex-end", mb: 2 }}>
          <IconButton color="inherit" onClick={handleDrawerToggle}>
            <CloseIcon />
          </IconButton>
        </Box>

        <List>
          {navItems.map((item) => (
            <ListItem key={item.label} disablePadding>
              <ListItemButton onClick={() => handleNavClick(item.path)}>
                <ListItemText primary={item.label} />
              </ListItemButton>
            </ListItem>
          ))}

          <ListItem disablePadding sx={{ mt: 1 }}>
            <ListItemButton onClick={() => handleNavClick("/invitations")}>
              <ListItemText primary={`Invitations (${invitationCount})`} />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding sx={{ mt: 3 }}>
            <Button
              fullWidth
              variant="contained"
              color="error"
              onClick={logout}
            >
              Logout
            </Button>
          </ListItem>
        </List>
      </Drawer>
    </>
  );
};

export default Navbar;
