package com.example.knowthisdog.auth

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.auth.AuthNavDestinations.LoginScreenDestination
import com.example.knowthisdog.auth.AuthNavDestinations.SignUpScreenDestination
import com.example.knowthisdog.auth.model.User
import com.example.knowthisdog.composables.ErrorDialog
import com.example.knowthisdog.composables.LoadingWheel

@Composable
fun AuthScreen(
    onUserLoggedIn: (User) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val user = authViewModel.user

    val userValue = user.value
    if (userValue != null) {
        onUserLoggedIn(userValue)
    }

    val navController = rememberNavController()
    val status = authViewModel.status.value

    AuthNavHost(
        navController = navController,
        onLoginButtonClick = { email, password -> authViewModel.login(email, password) },
        onSignUpButtonClick = { email, password, confirmPassword ->
            authViewModel.signUp(email, password, confirmPassword) },
        authViewModel = authViewModel,
    )

    if (status is ApiResponseStatus.Loading) {
        LoadingWheel()
    } else if (status is ApiResponseStatus.Error) {
        ErrorDialog(status.messageId) { authViewModel.resetApiResponseStatus() }
    }
}

@Composable
private fun AuthNavHost(
    navController: NavHostController,
    onLoginButtonClick: (String, String) -> Unit,
    onSignUpButtonClick: (email: String, password: String, passwordConfirmation: String) -> Unit,
    authViewModel: AuthViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = LoginScreenDestination
    ) {
        composable(route = LoginScreenDestination) {
            LoginScreen(
                onLoginButtonClick = onLoginButtonClick,
                onRegisterButtonClick = {
                    navController.navigate(route = SignUpScreenDestination)
                },
                authViewModel = authViewModel,
            )
        }

        composable(route = SignUpScreenDestination) {
            SignUpScreen(
                onSignUpButtonClick = onSignUpButtonClick,
                onNavigationIconClick = { navController.navigateUp() },
                authViewModel = authViewModel,
            )
        }
    }
}