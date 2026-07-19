import { Container } from "@mui/material";
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
          py: 4,
          minHeight: "calc(100vh - 64px)",
        }}
      >
        {children}
      </Container>
    </>
  );
};

export default PageLayout;
