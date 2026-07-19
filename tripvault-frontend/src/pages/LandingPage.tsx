import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Stack,
  Container,
  Paper,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import "../styles/landing.css";
import { Navigate } from "react-router-dom";
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
    if (getToken()) {
      return <Navigate to="/dashboard" replace />;
    }

  return (
    <Box className="landing">
      {/* Aurora */}

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
        <Toolbar>
          <Typography
            variant="h5"
            sx={{
              fontWeight: 700,
              flexGrow: 1,
              cursor: "pointer",
            }}
            onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
          >
            TripVault
          </Typography>

          <Stack
            direction="row"
            spacing={2}
            sx={{
              alignItems: "center",
            }}
          >
            <Button
              color="inherit"
              onClick={() =>
                document
                  .getElementById("features")
                  ?.scrollIntoView({ behavior: "smooth" })
              }
            >
              Features
            </Button>

            <Button
              color="inherit"
              onClick={() =>
                document
                  .getElementById("how-it-works")
                  ?.scrollIntoView({ behavior: "smooth" })
              }
            >
              How it Works
            </Button>

            <Button
              color="inherit"
              onClick={() =>
                document
                  .getElementById("ai")
                  ?.scrollIntoView({ behavior: "smooth" })
              }
            >
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
        </Toolbar>
      </AppBar>
      {/* Hero */}

      <Container
        maxWidth="xl"
        sx={{
          minHeight: "100vh",
          display: "flex",
          alignItems: "center",
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
            gap: 10,
            alignItems: "center",
          }}
        >
          {/* Left */}

          <Box>
            <Typography
              variant="h2"
              className="fadeUp"
              sx={{
                fontWeight: 800,
                lineHeight: 1.1,
                mb: 4,
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
                mb: 5,
              }}
            >
              TripVault intelligently distributes files across multiple Google
              Drive accounts, giving your travel group virtually unlimited
              storage with one simple dashboard.
            </Typography>

            <Stack direction="row" spacing={3}>
              <Button
                size="large"
                variant="contained"
                sx={{
                  px: 5,
                  py: 1.5,
                  borderRadius: 3,
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
                }}
              >
                Watch Demo
              </Button>
            </Stack>
          </Box>

          {/* Right */}

          <Box
            sx={{
              position: "relative",
              height: 500,
            }}
          >
            {drives.map((drive, index) => (
              <Paper
                key={index}
                className={`floating floating${index}`}
                sx={{
                  position: "absolute",
                  width: 280,
                  p: 3,
                  borderRadius: 4,
                  background: "rgba(255,255,255,.07)",
                  backdropFilter: "blur(20px)",
                  border: "1px solid rgba(255,255,255,.12)",

                  top: index * 120,
                  left: index * 25,
                }}
              >
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: 700,
                  }}
                >
                  {drive.name}
                </Typography>

                <Typography
                  sx={{
                    mt: 1,
                    color: "#7dd3fc",
                  }}
                >
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
      <Container maxWidth="lg" sx={{ py: 12 }}>
        <Box
          sx={{
            display: "grid",
            gridTemplateColumns: {
              xs: "1fr 1fr",
              md: "repeat(4,1fr)",
            },
            gap: 4,
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
                p: 5,
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
                }}
              >
                {value}
              </Typography>

              <Typography
                sx={{
                  mt: 2,
                  color: "#b8c0d0",
                }}
              >
                {label}
              </Typography>
            </Paper>
          ))}
        </Box>
      </Container>

      <Container maxWidth="xl" sx={{ py: 12 }}>
        <Typography
          sx={{
            fontWeight: 700,
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

            gap: 5,
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
                p: 5,

                borderRadius: 5,

                background: "rgba(255,255,255,.05)",

                backdropFilter: "blur(20px)",

                border: "1px solid rgba(255,255,255,.08)",
              }}
            >
              <Typography variant="h1">{feature.icon}</Typography>

              <Typography
                variant="h5"
                sx={{
                  mt: 3,

                  fontWeight: 700,
                }}
              >
                {feature.title}
              </Typography>

              <Typography
                sx={{
                  mt: 2,

                  color: "#b7bfd3",
                }}
              >
                {feature.desc}
              </Typography>
            </Paper>
          ))}
        </Box>
      </Container>
      <Container
        maxWidth="lg"
        sx={{
          py: 14,
        }}
      >
        <Typography
          sx={{
            fontWeight: 800,
          }}
        >
          How TripVault Works
        </Typography>

        <Stack spacing={5}>
          {[
            "Create your Trip",

            "Connect Google Drives",

            "Upload Files",

            "Invite Members",

            "AI manages everything",
          ].map((step, index) => (
            <Box key={step} className="timeline">
              <Box className="circle">{index + 1}</Box>

              <Typography
                sx={{
                  fontWeight: 700,
                }}
              >
                {step}
              </Typography>
            </Box>
          ))}
        </Stack>
      </Container>

      <Container maxWidth="xl" sx={{ py: 18 }}>
        <Paper
          sx={{
            p: 8,
            borderRadius: 8,
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
            sx={{
              fontWeight: 700,
            }}
          >
            Meet TripVault AI
          </Typography>

          <Typography
            variant="h6"
            sx={{
              color: "#cbd5e1",
              maxWidth: 700,
              mb: 6,
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
              gap: 4,
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
                  p: 3,
                  background: "rgba(255,255,255,.05)",
                }}
              >
                <Typography variant="h6">✓ {item}</Typography>
              </Paper>
            ))}
          </Box>
        </Paper>
      </Container>

      {/* <Container maxWidth="xl" sx={{ py: 16 }}>
        <Typography
          sx={{
            fontWeight: 700,
          }}
        >
          Everything in One Dashboard
        </Typography>

        <Paper
          className="dashboardPreview"
          sx={{
            height: 600,

            borderRadius: 8,

            overflow: "hidden",

            background: "linear-gradient(135deg,#111827,#1f2937)",

            border: "1px solid rgba(255,255,255,.08)",
          }}
        >
          <Box className="browserBar" />

          <Box
            sx={{
              display: "flex",

              justifyContent: "center",

              alignItems: "center",

              height: "100%",
            }}
          >
            <Typography variant="h4" color="#94a3b8">
              Dashboard Screenshot Here
            </Typography>
          </Box>
        </Paper>
      </Container> */}

      <Container
        maxWidth="lg"
        sx={{
          py: 20,
        }}
      >
        <Paper
          sx={{
            textAlign: "center",

            borderRadius: 8,

            p: 8,

            background: "linear-gradient(135deg,#2563eb,#7c3aed)",
          }}
        >
          <Typography
            sx={{
              fontWeight: 700,
            }}
          >
            Ready to Organize Your Next Trip?
          </Typography>

          <Typography
            variant="h6"
            sx={{
              mt: 3,

              mb: 5,
            }}
          >
            Start using distributed cloud storage today.
          </Typography>

          <Button
            size="large"
            variant="contained"
            sx={{
              background: "white",

              color: "#111",

              px: 6,

              py: 2,

              fontWeight: 700,
            }}
          >
            Get Started
          </Button>
        </Paper>
      </Container>

      <Box
        sx={{
          py: 8,
          borderTop: "1px solid rgba(255,255,255,.08)",
        }}
      >
        <Container maxWidth="xl">
          <Stack
            sx={{
              alignItems: "center",
              justifyContent: "space-between",
            }}
          >
            <Box>
              <Typography
                sx={{
                  fontWeight: 700,
                }}
              >
                TripVault
              </Typography>

              <Typography color="#94a3b8">
                Distributed Cloud Storage Platform
              </Typography>
            </Box>

            <Stack direction="row" spacing={4}>
              <Button color="inherit">GitHub</Button>

              <Button color="inherit">LinkedIn</Button>

              <Button color="inherit">Documentation</Button>
            </Stack>
          </Stack>
        </Container>
      </Box>

      {/* <Container
        maxWidth="lg"
        sx={{
          py: 15,
        }}
      >
        <Typography
          sx={{
            fontWeight: 700,
          }}
        >
          One Upload. Unlimited Storage.
        </Typography>

        <Box className="architecture">
          <Box className="upload">⬆️ Upload</Box>

          <Box className="vault">TripVault</Box>

          <Box className="drives">
            <Paper className="drive">Google Drive A</Paper>

            <Paper className="drive">Google Drive B</Paper>

            <Paper className="drive">Google Drive C</Paper>

            <Paper className="drive">Google Drive D</Paper>
          </Box>
        </Box>
      </Container> */}
    </Box>
  );
}
