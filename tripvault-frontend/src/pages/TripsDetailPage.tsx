import {
  Alert,
  Avatar,
  Box,
  Button,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Grid,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  Paper,
  Snackbar,
  Stack,
  TextField,
  Typography,
} from "@mui/material";

import ArrowBackRoundedIcon from "@mui/icons-material/ArrowBackRounded";
import CloudUploadRoundedIcon from "@mui/icons-material/CloudUploadRounded";
import GroupRoundedIcon from "@mui/icons-material/GroupRounded";
import FolderRoundedIcon from "@mui/icons-material/FolderRounded";
import DownloadRoundedIcon from "@mui/icons-material/DownloadRounded";
import DeleteRoundedIcon from "@mui/icons-material/DeleteRounded";
import AddRoundedIcon from "@mui/icons-material/AddRounded";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getTripById } from "../services/TripService";
import { getMembers } from "../services/MemberService";
import { inviteMember } from "../services/InvitationService";
import type { Trip } from "../types/Trip";
import type { TripMember } from "../types/TripMember";
import type { FileResponse } from "../types/FileResponse";
import {
  getFiles,
  downloadFile,
  uploadFile,
  deleteFile,
} from "../services/FileService";
import { formatFileSize } from "../utils/FileUtils";
import PageLayout from "../components/layout/PageLayout";

const TripsDetailPage = () => {
  const navigate = useNavigate();
  const { tripId } = useParams();

  const [trip, setTrip] = useState<Trip | null>(null);
  const [members, setMembers] = useState<TripMember[]>([]);

  const [open, setOpen] = useState(false);
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
  const [selectedFileId, setSelectedFileId] = useState<number | null>(null);
  const [files, setFiles] = useState<FileResponse[]>([]);

  const [username, setUsername] = useState("");
  const [allocatedStorage, setAllocatedStorage] = useState("");
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  const [snackbarSeverity, setSnackbarSeverity] = useState<"success" | "error">(
    "success",
  );

  const fetchMembers = async () => {
    const filesData = await getFiles(Number(tripId));
    setFiles(filesData);
    const membersData = await getMembers(Number(tripId));
    setMembers(membersData);
  };

  useEffect(() => {
    const fetchTrip = async () => {
      try {
        const tripData = await getTripById(Number(tripId));
        setTrip(tripData);

        await fetchMembers();
      } catch (error) {
        console.error(error);
      }
    };

    fetchTrip();
  }, [tripId]);

  const handleAddMember = async () => {
    try {
      await inviteMember(Number(tripId), username, Number(allocatedStorage));

      setOpen(false);
      setUsername("");
      setAllocatedStorage("");

      setSnackbarMessage("Invitation sent successfully!");
      setSnackbarSeverity("success");
      setSnackbarOpen(true);
    } catch (error: any) {
      setSnackbarMessage(error.response?.data || "Failed to send invitation.");
      setSnackbarSeverity("error");
      setSnackbarOpen(true);
    }
  };

  const handleDownload = async (fileId: number, fileName: string) => {
    try {
      const response = await downloadFile(fileId);
      const url = window.URL.createObjectURL(response.data);
      const link = document.createElement("a");
      link.href = url;
      link.download = fileName;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error(error);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile) return;

    try {
      await uploadFile(Number(tripId), selectedFile);
      await fetchMembers();
      setSelectedFile(null);

      setSnackbarMessage("File uploaded successfully!");
      setSnackbarSeverity("success");
      setSnackbarOpen(true);
    } catch (error) {
      console.error(error);
      setSnackbarMessage("Failed to upload file.");
      setSnackbarSeverity("error");
      setSnackbarOpen(true);
    }
  };

  const confirmDelete = async () => {
    if (selectedFileId == null) return;

    try {
      await deleteFile(selectedFileId);
      await fetchMembers();

      setSnackbarMessage("File deleted successfully!");
      setSnackbarSeverity("success");
      setSnackbarOpen(true);
    } catch (error) {
      console.error(error);
      setSnackbarMessage("Failed to delete file.");
      setSnackbarSeverity("error");
      setSnackbarOpen(true);
    } finally {
      setOpenDeleteDialog(false);
      setSelectedFileId(null);
    }
  };

  if (!trip) {
    return <div>Loading...</div>;
  }

  return (
    <PageLayout>
      <Stack spacing={4}>
        {/* Banner Hero Card */}
        <Paper
          elevation={0}
          sx={{
            p: { xs: 3, sm: 5 },
            borderRadius: { xs: 3, sm: 5 },
            background:
              "linear-gradient(135deg,#2563eb 0%,#4f46e5 45%,#7c3aed 100%)",
            color: "white",
            overflow: "hidden",
            position: "relative",
          }}
        >
          <Typography
            variant="h3"
            sx={{
              fontWeight: 700,
              fontSize: { xs: "1.75rem", sm: "2.5rem" },
            }}
          >
            {trip.name}
          </Typography>

          <Typography
            sx={{
              mt: 1,
              mb: 3,
              opacity: 0.9,
              maxWidth: 700,
              fontSize: { xs: "0.9rem", sm: "1rem" },
            }}
          >
            {trip.description}
          </Typography>

          <Button
            startIcon={<ArrowBackRoundedIcon />}
            variant="contained"
            onClick={() => navigate("/dashboard")}
            sx={{
              bgcolor: "rgba(255,255,255,0.2)",
              "&:hover": { bgcolor: "rgba(255,255,255,0.3)" },
            }}
          >
            Dashboard
          </Button>
        </Paper>

        <Grid container spacing={3}>
          {/* Members Column */}
          <Grid size={{ xs: 12, md: 4 }}>
            <Paper
              elevation={0}
              sx={{
                p: { xs: 2.5, sm: 3 },
                borderRadius: { xs: 3, sm: 5 },
                bgcolor: "background.paper",
                border: "1px solid rgba(255,255,255,.08)",
              }}
            >
              <Stack
                direction="row"
                sx={{
                  mb: 3,
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <Typography
                  variant="h5"
                  sx={{
                    fontWeight: 700,
                    fontSize: { xs: "1.2rem", sm: "1.5rem" },
                  }}
                >
                  <GroupRoundedIcon sx={{ mr: 1, verticalAlign: "middle" }} />
                  Members
                </Typography>

                <Button
                  startIcon={<AddRoundedIcon />}
                  variant="contained"
                  size="small"
                  onClick={() => setOpen(true)}
                >
                  Add Member
                </Button>
              </Stack>

              <List disablePadding>
                {members.map((member) => (
                  <ListItem
                    key={member.userId}
                    sx={{
                      borderRadius: 3,
                      mb: 1,
                      px: 1,
                      transition: ".25s",
                      "&:hover": {
                        bgcolor: "action.hover",
                      },
                    }}
                    secondaryAction={
                      <Chip
                        label={member.role}
                        size="small"
                        color={
                          member.role === "OWNER" ? "primary" : "secondary"
                        }
                      />
                    }
                  >
                    <ListItemAvatar>
                      <Avatar
                        sx={{
                          bgcolor:
                            member.role === "OWNER" ? "#2563eb" : "#7c3aed",
                          fontWeight: 700,
                        }}
                      >
                        {member.name.charAt(0).toUpperCase()}
                      </Avatar>
                    </ListItemAvatar>

                    <ListItemText
                      primary={
                        <Typography
                          sx={{ fontWeight: 600, fontSize: "0.95rem" }}
                        >
                          {member.name}
                        </Typography>
                      }
                      secondary={member.username}
                    />
                  </ListItem>
                ))}
              </List>
            </Paper>
          </Grid>

          {/* Files Column */}
          <Grid size={{ xs: 12, md: 8 }}>
            <Paper
              elevation={0}
              sx={{
                p: { xs: 2.5, sm: 3 },
                borderRadius: { xs: 3, sm: 5 },
                border: "1px solid rgba(255,255,255,.08)",
              }}
            >
              <Stack
                direction={{ xs: "column", sm: "row" }}
                spacing={2}
                sx={{
                  mb: 3,
                  justifyContent: "space-between",
                  alignItems: { xs: "flex-start", sm: "center" },
                }}
              >
                <Typography
                  variant="h5"
                  sx={{
                    fontWeight: 700,
                    fontSize: { xs: "1.2rem", sm: "1.5rem" },
                  }}
                >
                  <FolderRoundedIcon sx={{ mr: 1, verticalAlign: "middle" }} />
                  Files
                </Typography>

                <Stack
                  direction="row"
                  spacing={1.5}
                  sx={{ width: { xs: "100%", sm: "auto" } }}
                >
                  <Button
                    variant="outlined"
                    component="label"
                    fullWidth
                    sx={{ textOverflow: "ellipsis" }}
                  >
                    Choose File
                    <input
                      hidden
                      type="file"
                      onChange={(e) => {
                        if (e.target.files) {
                          setSelectedFile(e.target.files[0]);
                        }
                      }}
                    />
                  </Button>

                  <Button
                    startIcon={<CloudUploadRoundedIcon />}
                    variant="contained"
                    onClick={handleUpload}
                    disabled={!selectedFile}
                    sx={{ whitespace: "nowrap" }}
                  >
                    Upload
                  </Button>
                </Stack>
              </Stack>

              {selectedFile && (
                <Typography
                  sx={{
                    mb: 3,
                    color: "text.secondary",
                    fontSize: "0.9rem",
                    wordBreak: "break-all",
                  }}
                >
                  Selected: <strong>{selectedFile.name}</strong>
                </Typography>
              )}

              <List disablePadding>
                {files.map((file) => (
                  <Paper
                    key={file.id}
                    elevation={0}
                    sx={{
                      mb: 2,
                      p: 2,
                      borderRadius: 4,
                      border: "1px solid rgba(255,255,255,.08)",
                      transition: ".25s",
                      "&:hover": {
                        transform: "translateY(-2px)",
                        boxShadow: 4,
                      },
                    }}
                  >
                    <Stack
                      direction={{ xs: "column", sm: "row" }}
                      spacing={2}
                      sx={{
                        justifyContent: "space-between",
                        alignItems: { xs: "flex-start", sm: "center" },
                      }}
                    >
                      <Box sx={{ minWidth: 0 }}>
                        <Typography
                          sx={{
                            mb: 0.5,
                            fontWeight: 700,
                            wordBreak: "break-word",
                          }}
                        >
                          {file.fileName}
                        </Typography>

                        <Typography color="text.secondary" variant="body2">
                          {formatFileSize(file.fileSize)}
                        </Typography>
                      </Box>

                      <Stack
                        direction="row"
                        spacing={1.5}
                        sx={{
                          width: { xs: "100%", sm: "auto" },
                          justifyContent: "flex-end",
                        }}
                      >
                        <Button
                          size="small"
                          startIcon={<DownloadRoundedIcon />}
                          onClick={() => handleDownload(file.id, file.fileName)}
                        >
                          Download
                        </Button>

                        <Button
                          size="small"
                          startIcon={<DeleteRoundedIcon />}
                          color="error"
                          variant="contained"
                          onClick={() => {
                            setSelectedFileId(file.id);
                            setOpenDeleteDialog(true);
                          }}
                        >
                          Delete
                        </Button>
                      </Stack>
                    </Stack>
                  </Paper>
                ))}

                {files.length === 0 && (
                  <Paper
                    elevation={0}
                    sx={{
                      p: { xs: 4, sm: 6 },
                      textAlign: "center",
                      borderRadius: 4,
                    }}
                  >
                    <Typography variant="h6">No files uploaded yet</Typography>
                    <Typography color="text.secondary">
                      Upload your first trip memory.
                    </Typography>
                  </Paper>
                )}
              </List>
            </Paper>
          </Grid>
        </Grid>
      </Stack>

      {/* Invite Modal */}
      <Dialog
        open={open}
        onClose={() => setOpen(false)}
        maxWidth="sm"
        fullWidth
        slotProps={{
          paper: {
            sx: {
              borderRadius: 4,
              p: { xs: 1, sm: 2 },
            },
          },
        }}
      >
        <DialogTitle sx={{ fontWeight: 700 }}>Add New Member</DialogTitle>

        <DialogContent>
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
            onChange={(e) => setUsername(e.target.value)}
          />

          <TextField
            label="Allocated Storage (GB)"
            fullWidth
            margin="normal"
            value={allocatedStorage}
            slotProps={{
              htmlInput: {
                style: { fontSize: "16px" },
              },
            }}
            onChange={(e) => setAllocatedStorage(e.target.value)}
          />
        </DialogContent>

        <DialogActions sx={{ p: 2 }}>
          <Button onClick={() => setOpen(false)}>Cancel</Button>

          <Button
            variant="contained"
            onClick={handleAddMember}
            sx={{ px: 3, borderRadius: 3 }}
          >
            Send Invite
          </Button>
        </DialogActions>
      </Dialog>

      {/* Delete Confirmation Modal */}
      <Dialog
        open={openDeleteDialog}
        onClose={() => setOpenDeleteDialog(false)}
        slotProps={{
          paper: {
            sx: { borderRadius: 4 },
          },
        }}
      >
        <DialogTitle sx={{ fontWeight: 700 }}>Delete File?</DialogTitle>

        <DialogContent>
          <DialogContentText>
            This action cannot be undone. The file and all its distributed
            chunks will be permanently deleted.
          </DialogContentText>
        </DialogContent>

        <DialogActions sx={{ p: 2 }}>
          <Button onClick={() => setOpenDeleteDialog(false)}>Cancel</Button>

          <Button variant="contained" color="error" onClick={confirmDelete}>
            Delete
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={3000}
        onClose={() => setSnackbarOpen(false)}
        anchorOrigin={{
          vertical: "bottom",
          horizontal: "center",
        }}
      >
        <Alert
          severity={snackbarSeverity}
          variant="filled"
          sx={{ width: "100%", borderRadius: 3 }}
          onClose={() => setSnackbarOpen(false)}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </PageLayout>
  );
};

export default TripsDetailPage;
