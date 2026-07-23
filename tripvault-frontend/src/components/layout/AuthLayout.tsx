import { Box, Paper, Typography } from "@mui/material";
import type { ReactNode } from "react";

interface Props {
  title: string;
  subtitle: string;
  children: ReactNode;
}

const AuthLayout = ({ title, subtitle, children }: Props) => {
  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        bgcolor: "#f5f7fb",
        // Scaled padding: 16px on mobile, 24px on desktop
        p: { xs: 2, sm: 3 },
      }}
    >
      <Paper
        elevation={6}
        sx={{
          width: "100%",
          // Caps the width on larger screens while letting mobile scale naturally
          maxWidth: 430,
          // Reduced padding on mobile so content has breathing room
          p: { xs: 3, sm: 5 },
          // Slightly smaller border radius on tight mobile viewports
          borderRadius: { xs: 3, sm: 4 },
        }}
      >
        <Typography
          variant="h4"
          gutterBottom
          align="center"
          sx={{
            fontWeight: "bold",
            // Scales font down slightly on small phones
            fontSize: { xs: "1.75rem", sm: "2.125rem" },
          }}
        >
          TripVault
        </Typography>

        <Typography
          variant="h6"
          align="center"
          sx={{
            fontSize: { xs: "1rem", sm: "1.25rem" },
          }}
        >
          {title}
        </Typography>

        <Typography
          variant="body2"
          color="text.secondary"
          align="center"
          sx={{ mb: { xs: 3, sm: 4 } }}
        >
          {subtitle}
        </Typography>

        {children}
      </Paper>
    </Box>
  );
};

export default AuthLayout;
