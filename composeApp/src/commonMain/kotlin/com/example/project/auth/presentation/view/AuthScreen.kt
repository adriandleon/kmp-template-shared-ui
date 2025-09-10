package com.example.project.auth.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.project.auth.domain.entity.AuthError.*
import com.example.project.auth.presentation.component.AuthComponent
import com.example.project.auth.presentation.store.AuthStore

/**
 * Authentication screen for login and signup functionality.
 *
 * This screen provides a simple UI for authentication operations including email/password login,
 * signup, and password reset.
 */
@Composable
fun AuthScreen(component: AuthComponent, modifier: Modifier = Modifier) {
    val state by component.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle authentication state changes
    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            // Navigate to home screen or handle successful authentication
        }
    }

    // Handle errors
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(
                message =
                    when (error) {
                        is NetworkError -> "Network error: ${error.message}"
                        is InvalidCredentials -> "Invalid credentials: ${error.message}"
                        is UserNotFound -> "User not found: ${error.message}"
                        is EmailAlreadyExists -> "Email already exists: ${error.message}"
                        is PhoneAlreadyExists -> "Phone already exists: ${error.message}"
                        is EmailNotVerified -> "Email not verified: ${error.message}"
                        is PhoneNotVerified -> "Phone not verified: ${error.message}"
                        is WeakPassword -> "Weak password: ${error.message}"
                        is InvalidEmail -> "Invalid email: ${error.message}"
                        is InvalidPhone -> "Invalid phone: ${error.message}"
                        is InvalidOtp -> "Invalid OTP: ${error.message}"
                        is OtpExpired -> "OTP expired: ${error.message}"
                        is TooManyAttempts -> "Too many attempts: ${error.message}"
                        is UserDisabled -> "User disabled: ${error.message}"
                        is GenericError -> "Error: ${error.message}"
                        is UnknownError -> "Unknown error: ${error.message}"
                    }
            )
            component.clearError()
        }
    }

    Scaffold(modifier = modifier, snackbarHost = { SnackbarHost(snackbarHostState) }) {
        paddingValues ->
        AuthContent(
            state = state,
            onSignIn = { email, password -> component.signInWithEmail(email, password) },
            onSignUp = { email, password, displayName ->
                component.signUpWithEmail(email, password, displayName)
            },
            onResetPassword = { email -> component.resetPassword(email) },
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Composable
private fun AuthContent(
    state: AuthStore.State,
    onSignIn: (String, String) -> Unit,
    onSignUp: (String, String, String?) -> Unit,
    onResetPassword: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var isSignUp by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = if (isSignUp) "Sign Up" else "Sign In",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isSignUp) {
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display Name") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (isSignUp) {
                    onSignUp(email, password, displayName.takeIf { it.isNotBlank() })
                } else {
                    onSignIn(email, password)
                }
            },
            enabled = !state.isLoading && email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.height(16.dp))
            } else {
                Text(if (isSignUp) "Sign Up" else "Sign In")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { isSignUp = !isSignUp }) {
            Text(
                if (isSignUp) "Already have an account? Sign In"
                else "Don't have an account? Sign Up"
            )
        }

        if (!isSignUp) {
            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { onResetPassword(email) }) { Text("Forgot Password?") }
        }
    }
}
