import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Stack,
  Container,
  Paper,
  IconButton,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import CloseIcon from "@mui/icons-material/Close";
import { useState } from "react";
import { useNavigate, Navigate } from "react-router-dom";
import "../styles/landing.css";
import { getToken } from "../utils/AuthUtils";

const drives = [
  {
    name: "Google Drive A",
    storage: "91% Free",
  },
  {
    name: "Google Drive B",
    storage: "63% Free",
  },
  {
    name: "Google Drive C",
    storage: "28% Free",
  },
];

export default function LandingPage() {
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  if (getToken()) {
    return <Navigate to="/dashboard" replace />;
  }

  const handleScrollTo = (id: string) => {
    setMobileMenuOpen(false);
    document.getElementById(id)?.scrollIntoView({ behavior: "smooth" });
  };

  return (
    <Box className="landing">
      {/* Aurora Background Blobs */}
      <div className="blob blob1"></div>
      <div className="blob blob2"></div>
      <div className="blob blob3"></div>

      {/* Navbar */}
      <AppBar
        position="fixed"
        elevation={0}
        sx={{
          backdropFilter: "blur(24px)",
          background: "rgba(5,10,25,.45)",
          borderBottom: "1px solid rgba(255,255,255,.08)",
        }}
      >
        <Toolbar sx={{ px: { xs: 2, sm: 4 } }}>
          <Typography
            variant="h5"
            sx={{
              fontWeight: 700,
              flexGrow: 1,
              cursor: "pointer",
              fontSize: { xs: "1.25rem", sm: "1.5rem" },
            }}
            onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
          >
            TripVault
          </Typography>

          {/* Desktop Navigation */}
          <Stack
            direction="row"
            spacing={2}
            sx={{
              display: { xs: "none", md: "flex" },
              alignItems: "center",
            }}
          >
            <Button color="inherit" onClick={() => handleScrollTo("features")}>
              Features
            </Button>

            <Button
              color="inherit"
              onClick={() => handleScrollTo("how-it-works")}
            >
              How it Works
            </Button>

            <Button color="inherit" onClick={() => handleScrollTo("ai")}>
              AI
            </Button>

            <Button variant="outlined" onClick={() => navigate("/login")}>
              Login
            </Button>

            <Button
              variant="contained"
              size="large"
              onClick={() => navigate("/register")}
              sx={{
                borderRadius: "999px",
                px: 3,
              }}
            >
              Get Started Free
            </Button>
          </Stack>

          {/* Mobile Hamburger Button */}
          <IconButton
            color="inherit"
            edge="end"
            onClick={() => setMobileMenuOpen(true)}
            sx={{ display: { md: "none" } }}
          >
            <MenuIcon />
          </IconButton>
        </Toolbar>
      </AppBar>

      {/* Mobile Menu Drawer */}
      <Drawer
        anchor="right"
        open={mobileMenuOpen}
        onClose={() => setMobileMenuOpen(false)}
        slotProps={{
          paper: {
            sx: {
              width: "80%",
              maxWidth: 300,
              bgcolor: "rgba(10,15,30,.96)",
              backdropFilter: "blur(18px)",
              color: "white",
              p: 2,
            },
          },
        }}
      >
        <Box sx={{ display: "flex", justifyContent: "flex-end", mb: 2 }}>
          <IconButton color="inherit" onClick={() => setMobileMenuOpen(false)}>
            <CloseIcon />
          </IconButton>
        </Box>

        <List>
          <ListItem disablePadding>
            <ListItemButton onClick={() => handleScrollTo("features")}>
              <ListItemText primary="Features" />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding>
            <ListItemButton onClick={() => handleScrollTo("how-it-works")}>
              <ListItemText primary="How it Works" />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding>
            <ListItemButton onClick={() => handleScrollTo("ai")}>
              <ListItemText primary="AI" />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding sx={{ mt: 2 }}>
            <Button
              fullWidth
              variant="outlined"
              onClick={() => {
                setMobileMenuOpen(false);
                navigate("/login");
              }}
            >
              Login
            </Button>
          </ListItem>

          <ListItem disablePadding sx={{ mt: 1.5 }}>
            <Button
              fullWidth
              variant="contained"
              onClick={() => {
                setMobileMenuOpen(false);
                navigate("/register");
              }}
            >
              Get Started Free
            </Button>
          </ListItem>
        </List>
      </Drawer>

      {/* Hero Section */}
      <Container
        maxWidth="xl"
        sx={{
          minHeight: "100vh",
          display: "flex",
          alignItems: "center",
          pt: { xs: 12, md: 8 },
          pb: { xs: 6, md: 8 },
        }}
      >
        <Box
          sx={{
            width: "100%",
            display: "grid",
            gridTemplateColumns: {
              xs: "1fr",
              md: "1.2fr 1fr",
            },
            gap: { xs: 5, md: 10 },
            alignItems: "center",
          }}
        >
          {/* Left Text Column */}
          <Box>
            <Typography
              variant="h1"
              className="fadeUp"
              sx={{
                fontWeight: 800,
                lineHeight: 1.1,
                mb: 3,
                fontSize: { xs: "2.25rem", sm: "3.5rem", md: "4.25rem" },
              }}
            >
              Store Every
              <Box
                component="span"
                sx={{
                  display: "block",
                  background: "linear-gradient(90deg,#7c3aed,#2563eb,#06b6d4)",
                  WebkitBackgroundClip: "text",
                  WebkitTextFillColor: "transparent",
                }}
              >
                Trip Memory
              </Box>
              Across Unlimited Storage
            </Typography>

            <Typography
              variant="h6"
              sx={{
                color: "#b6bfd2",
                maxWidth: 650,
                mb: 4,
                fontSize: { xs: "1rem", sm: "1.25rem" },
              }}
            >
              TripVault intelligently distributes files across multiple Google
              Drive accounts, giving your travel group virtually unlimited
              storage with one simple dashboard.
            </Typography>

            <Stack
              direction={{ xs: "column", sm: "row" }}
              spacing={{ xs: 2, sm: 3 }}
            >
              <Button
                size="large"
                variant="contained"
                sx={{
                  px: 5,
                  py: 1.5,
                  borderRadius: 3,
                  width: { xs: "100%", sm: "auto" },
                }}
                onClick={() => navigate("/register")}
              >
                Start Free
              </Button>

              <Button
                size="large"
                variant="outlined"
                sx={{
                  px: 5,
                  py: 1.5,
                  borderRadius: 3,
                  width: { xs: "100%", sm: "auto" },
                }}
              >
                Watch Demo
              </Button>
            </Stack>
          </Box>

          {/* Right Cards Visual Column */}
          <Box
            sx={{
              position: "relative",
              height: { xs: "auto", md: 500 },
              display: "flex",
              flexDirection: { xs: "column", md: "block" },
              gap: { xs: 2, md: 0 },
            }}
          >
            {drives.map((drive, index) => (
              <Paper
                key={index}
                className={`floating floating${index}`}
                sx={{
                  position: { xs: "relative", md: "absolute" },
                  width: { xs: "100%", md: 280 },
                  p: 3,
                  borderRadius: 4,
                  background: "rgba(255,255,255,.07)",
                  backdropFilter: "blur(20px)",
                  border: "1px solid rgba(255,255,255,.12)",
                  top: { md: index * 120 },
                  left: { md: index * 25 },
                }}
              >
                <Typography variant="h6" sx={{ fontWeight: 700 }}>
                  {drive.name}
                </Typography>

                <Typography sx={{ mt: 1, color: "#7dd3fc" }}>
                  {drive.storage}
                </Typography>

                <Box
                  sx={{
                    mt: 3,
                    height: 10,
                    borderRadius: 5,
                    background: "#162039",
                  }}
                >
                  <Box
                    sx={{
                      width: "70%",
                      height: "100%",
                      borderRadius: 5,
                      background: "linear-gradient(90deg,#06b6d4,#2563eb)",
                    }}
                  />
                </Box>
              </Paper>
            ))}
          </Box>
        </Box>
      </Container>

      {/* Stats Section */}
      <Container id="features" maxWidth="lg" sx={{ py: { xs: 6, md: 10 } }}>
        <Box
          sx={{
            display: "grid",
            gridTemplateColumns: {
              xs: "1fr 1fr",
              md: "repeat(4,1fr)",
            },
            gap: { xs: 2, sm: 4 },
          }}
        >
          {[
            ["∞", "Potential Storage"],
            ["100%", "Google Drive"],
            ["JWT", "Secure"],
            ["AI", "Coming Soon"],
          ].map(([value, label]) => (
            <Paper
              key={label}
              className="featureCard"
              sx={{
                p: { xs: 3, sm: 5 },
                textAlign: "center",
                background: "rgba(255,255,255,.05)",
                backdropFilter: "blur(20px)",
                border: "1px solid rgba(255,255,255,.08)",
              }}
            >
              <Typography
                variant="h2"
                sx={{
                  fontWeight: 800,
                  color: "#60a5fa",
                  fontSize: { xs: "2rem", sm: "3.5rem" },
                }}
              >
                {value}
              </Typography>

              <Typography
                sx={{
                  mt: 1,
                  color: "#b8c0d0",
                  fontSize: { xs: "0.85rem", sm: "1rem" },
                }}
              >
                {label}
              </Typography>
            </Paper>
          ))}
        </Box>
      </Container>

      {/* Why TripVault Section */}
      <Container maxWidth="xl" sx={{ py: { xs: 6, md: 10 } }}>
        <Typography
          variant="h3"
          sx={{
            fontWeight: 800,
            mb: 4,
            fontSize: { xs: "1.75rem", sm: "2.5rem" },
          }}
        >
          Why TripVault?
        </Typography>

        <Box
          sx={{
            display: "grid",
            gridTemplateColumns: {
              xs: "1fr",
              md: "repeat(3,1fr)",
            },
            gap: { xs: 3, sm: 5 },
          }}
        >
          {[
            {
              icon: "☁️",
              title: "Distributed Storage",
              desc: "Automatically spreads files across multiple Google Drive accounts.",
            },
            {
              icon: "👥",
              title: "Trip Collaboration",
              desc: "Invite friends with dedicated storage allocations.",
            },
            {
              icon: "🤖",
              title: "AI Ready",
              desc: "TripVault AI will soon organize and manage your trips.",
            },
          ].map((feature) => (
            <Paper
              key={feature.title}
              className="featureCard"
              sx={{
                p: { xs: 3, sm: 5 },
                borderRadius: 5,
                background: "rgba(255,255,255,.05)",
                backdropFilter: "blur(20px)",
                border: "1px solid rgba(255,255,255,.08)",
              }}
            >
              <Typography variant="h2">{feature.icon}</Typography>

              <Typography
                variant="h5"
                sx={{
                  mt: 3,
                  fontWeight: 700,
                  fontSize: { xs: "1.25rem", sm: "1.5rem" },
                }}
              >
                {feature.title}
              </Typography>

              <Typography sx={{ mt: 2, color: "#b7bfd3" }}>
                {feature.desc}
              </Typography>
            </Paper>
          ))}
        </Box>
      </Container>

      {/* How it Works Section */}
      <Container id="how-it-works" maxWidth="lg" sx={{ py: { xs: 6, md: 10 } }}>
        <Typography
          variant="h3"
          sx={{
            fontWeight: 800,
            mb: 4,
            fontSize: { xs: "1.75rem", sm: "2.5rem" },
          }}
        >
          How TripVault Works
        </Typography>

        <Stack spacing={3}>
          {[
            "Create your Trip",
            "Connect Google Drives",
            "Upload Files",
            "Invite Members",
            "AI manages everything",
          ].map((step, index) => (
            <Box key={step} className="timeline">
              <Box className="circle">{index + 1}</Box>
              <Typography sx={{ fontWeight: 700, fontSize: "1.1rem" }}>
                {step}
              </Typography>
            </Box>
          ))}
        </Stack>
      </Container>

      {/* AI Feature Block */}
      <Container id="ai" maxWidth="xl" sx={{ py: { xs: 6, md: 10 } }}>
        <Paper
          sx={{
            p: { xs: 4, sm: 8 },
            borderRadius: { xs: 4, sm: 8 },
            overflow: "hidden",
            position: "relative",
            background: "linear-gradient(135deg,#111827,#1e1b4b,#0f172a)",
            border: "1px solid rgba(255,255,255,.08)",
          }}
        >
          <Box
            sx={{
              position: "absolute",
              width: 500,
              height: 500,
              background: "#2563eb",
              opacity: 0.18,
              borderRadius: "50%",
              filter: "blur(120px)",
              top: -200,
              right: -150,
            }}
          />

          <Typography
            variant="h3"
            sx={{
              fontWeight: 800,
              mb: 2,
              fontSize: { xs: "1.75rem", sm: "2.5rem" },
            }}
          >
            Meet TripVault AI
          </Typography>

          <Typography
            variant="h6"
            sx={{
              color: "#cbd5e1",
              maxWidth: 700,
              mb: 5,
              fontSize: { xs: "0.95rem", sm: "1.25rem" },
            }}
          >
            TripVault is evolving into an AI-powered travel workspace. Soon
            you'll manage trips, search files, rename folders, answer storage
            questions and automate workflows simply by chatting.
          </Typography>

          <Box
            sx={{
              display: "grid",
              gridTemplateColumns: {
                xs: "1fr",
                md: "repeat(2,1fr)",
              },
              gap: 3,
            }}
          >
            {[
              "Search trip files instantly",
              "Find free cloud storage",
              "Rename trips using AI",
              "Summarize trip memories",
              "Manage invitations",
              "AI Tool Calling",
            ].map((item) => (
              <Paper
                key={item}
                className="featureCard"
                sx={{
                  p: 2.5,
                  background: "rgba(255,255,255,.05)",
                }}
              >
                <Typography variant="body1" sx={{ fontWeight: 600 }}>
                  ✓ {item}
                </Typography>
              </Paper>
            ))}
          </Box>
        </Paper>
      </Container>

      {/* Call To Action Banner */}
      <Container maxWidth="lg" sx={{ py: { xs: 8, md: 14 } }}>
        <Paper
          sx={{
            textAlign: "center",
            borderRadius: { xs: 4, sm: 8 },
            p: { xs: 4, sm: 8 },
            background: "linear-gradient(135deg,#2563eb,#7c3aed)",
          }}
        >
          <Typography
            variant="h3"
            sx={{
              fontWeight: 800,
              fontSize: { xs: "1.75rem", sm: "2.75rem" },
            }}
          >
            Ready to Organize Your Next Trip?
          </Typography>

          <Typography
            variant="h6"
            sx={{
              mt: 2,
              mb: 4,
              fontSize: { xs: "1rem", sm: "1.25rem" },
            }}
          >
            Start using distributed cloud storage today.
          </Typography>

          <Button
            size="large"
            variant="contained"
            onClick={() => navigate("/register")}
            sx={{
              background: "white",
              color: "#111",
              px: { xs: 4, sm: 6 },
              py: 1.5,
              fontWeight: 700,
              width: { xs: "100%", sm: "auto" },
              "&:hover": {
                background: "#f1f5f9",
              },
            }}
          >
            Get Started
          </Button>
        </Paper>
      </Container>

      {/* Footer */}
      <Box
        sx={{
          py: 6,
          borderTop: "1px solid rgba(255,255,255,.08)",
        }}
      >
        <Container maxWidth="xl">
          <Stack
            direction={{ xs: "column", sm: "row" }}
            spacing={{ xs: 3, sm: 0 }}
            sx={{
              alignItems: "center",
              justifyContent: "space-between",
            }}
          >
            <Box sx={{ textAlign: { xs: "center", sm: "left" } }}>
              <Typography sx={{ fontWeight: 700 }}>TripVault</Typography>
              <Typography variant="body2" color="#94a3b8">
                Distributed Cloud Storage Platform
              </Typography>
            </Box>

            <Stack direction="row" spacing={2}>
              <Button color="inherit" size="small">
                GitHub
              </Button>
              <Button color="inherit" size="small">
                LinkedIn
              </Button>
              <Button color="inherit" size="small">
                Docs
              </Button>
            </Stack>
          </Stack>
        </Container>
      </Box>
    </Box>
  );
}
