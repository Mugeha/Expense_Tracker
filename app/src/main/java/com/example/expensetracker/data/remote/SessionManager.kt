package com.example.expensetracker.data.remote

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "user_token"
        private const val KEY_USERNAME = "user_name"
        private const val KEY_PROFILE_IMAGE = "profile_image"
        private const val KEY_EMAIL = "user_email" // 👈🏽 new
        private const val KEY_PHOTO_STEP_DONE = "photo_step_done"

    }

    // ✅ Save user session data (token, username, profile image)
    fun saveUserSession(token: String, username: String, profileImage: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USERNAME, username)
            .putString(KEY_PROFILE_IMAGE, profileImage)
            .putBoolean(KEY_PHOTO_STEP_DONE, false) // Reset when new session starts
            .apply()
    }


    // ✅ Save profile image separately
    fun saveProfileImage(uri: String) {
        prefs.edit()
            .putString(KEY_PROFILE_IMAGE, uri)
            .apply()
    }

    fun saveEmail(email: String) {
        prefs.edit()
            .putString(KEY_EMAIL, email)
            .apply()
    }


    // ✅ Get user token
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    // ✅ Get username
    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    // ✅ Get email (if stored separately)
    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

    // ✅ Get saved profile image URL
    fun getProfileImage(): String? = prefs.getString(KEY_PROFILE_IMAGE, null)

    // ✅ Clear session (for logout)
    fun clearSession() {
        prefs.edit().clear().apply()
    }

    // ✅ Check if user is logged in
    fun isLoggedIn(): Boolean = getToken() != null
    fun markPhotoStepDone() {
        prefs.edit()
            .putBoolean(KEY_PHOTO_STEP_DONE, true)
            .apply()
    }

    fun isPhotoStepDone(): Boolean {
        return prefs.getBoolean(KEY_PHOTO_STEP_DONE, false)
    }

}
