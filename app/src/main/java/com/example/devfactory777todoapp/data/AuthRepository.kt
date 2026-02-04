package com.example.devfactory777todoapp.data

import com.example.devfactory777todoapp.data.SupabaseModule.client
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepository {
    private val auth = client.auth

    val currentUserId: String?
        get() = auth.currentUserOrNull()?.id

    suspend fun signUp(emailInput: String, passwordInput: String) {
        auth.signUpWith(Email) {
            email = emailInput
            password = passwordInput
        }
    }

    suspend fun signIn(emailInput: String, passwordInput: String) {
        auth.signInWith(Email) {
            email = emailInput
            password = passwordInput
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }
}
