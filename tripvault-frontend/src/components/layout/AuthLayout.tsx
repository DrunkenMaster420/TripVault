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
        p: 2,
      }}
    >
      <Paper
        elevation={6}
        sx={{
          width: 430,
          p: 5,
          borderRadius: 4,
        }}
      >
        <Typography
          variant="h4"
          gutterBottom
          align="center"
          sx={{ fontWeight: "bold" }}
        >
          TripVault
        </Typography>

        <Typography variant="h6" align="center">
          {title}
        </Typography>

        <Typography
          variant="body2"
          color="text.secondary"
          align="center"
          sx={{ mb: 4 }}
        >
          {subtitle}
        </Typography>

        {children}
      </Paper>
    </Box>
  );
};

export default AuthLayout;
