import { Box, Container } from "@mui/material";
import type { ReactNode } from "react";
import Navbar from "./Navbar";

interface Props {
  children: ReactNode;
}

const PageLayout = ({ children }: Props) => {
  return (
    <>
      <Navbar />

      <Container
        maxWidth="xl"
        sx={{
          py: 5,
        }}
      >
        {children}
      </Container>
    </>
  );
};

export default PageLayout;
