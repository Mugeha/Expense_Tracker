EXPENSE TRACKER
Overview

Check out the design here: https://www.figma.com/proto/7B3yPUzrSUYTO7Jq2WSbL7/Expense-tracker?node-id=2-2&p=f&t=UTJCX5tf8sJp1IWU-1&scaling=scale-down&content-scaling=fixed&page-id=0%3A1&starting-point-node-id=2%3A2

🔐 AUTH FLOW (Full Breakdown)
✅ 1. Sign Up / Register
User enters name, email, password

This triggers a signUp() function in your AuthViewModel

It calls the repository which:

Makes a network request to register the user (backend API)

On success, stores user details (possibly token too)

✅ 2. Upload Profile Photo
After signing up, user can upload a photo via:

Gallery → using galleryLauncher (GetContent contract)

Camera → using cameraLauncher (TakePicturePreview contract)

The selected Uri or Bitmap is:

Converted to File (via uriToFile)

Sent to the backend via uploadProfilePhoto(email, file) in AuthViewModel or PhotoViewModel

On success, photo is saved locally (using SessionManager)

Also photoViewModel.saveProfileImage() stores it in LiveData

✅ 3. Home Screen
When navigating to HomeScreen, you:

Load saved image from local cache or backend (via SessionManager)

Fetch username from SessionManager

Load balance, dashboard, etc.
