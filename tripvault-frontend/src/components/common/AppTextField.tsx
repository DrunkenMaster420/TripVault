// AppTextField.tsx
import { TextField } from "@mui/material";
import type { TextFieldProps } from "@mui/material";

const AppTextField = (props: TextFieldProps) => {
  return (
    <TextField
      fullWidth
      margin="normal"
      variant="outlined"
      slotProps={{
        htmlInput: {
          style: { fontSize: "16px" },
        },
      }}
      {...props}
    />
  );
};

export default AppTextField;
