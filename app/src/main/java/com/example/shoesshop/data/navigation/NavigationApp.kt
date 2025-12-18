package com.example.shoesshop.navigation
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shoesshop.data.view.CreateNewPasswordScreen
import com.example.shoesshop.data.view.ForgotPasswordScreen
import com.example.shoesshop.data.view.HomeScreen
import com.example.shoesshop.data.view.OnboardingScreen
import com.example.shoesshop.data.view.RegisterAccount
import com.example.shoesshop.data.view.ResetPasswordOTPScreen
import com.example.shoesshop.data.view.SignInScreen
import com.example.shoesshop.data.view.VerificationScreen
import com.example.shoesshop.data.viewModel.RegisterAccountViewModel

@Composable
fun NavigationApp(
    navController: NavHostController
) {
    val registerAccountViewModel: RegisterAccountViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "onboard"
    ) {
        composable("register_account") {
            RegisterAccount(
                viewModel = registerAccountViewModel,
                onBackClick = { navController.popBackStack() },
                onNavigateToSignIn = {  navController.navigate("sign_in") },
                onSendOTP = {  navController.navigate("send_otp") },
            )
        }
        composable("sign_in") {
            SignInScreen(
                onRegisterClick =  {  navController.navigate("register_account") },
                onBackClick = { navController.popBackStack() },
                onSignInClick = {  navController.navigate("home") },
                resetPassword = {  navController.navigate("forgot_passwd") }
            )
        }
        composable("send_otp") {
            VerificationScreen(
                onBackClick = { navController.popBackStack() },
                onVerificationSuccess = {  navController.navigate("home") }
            )
        }
        composable("onboard") {
            OnboardingScreen(
                onFinish ={  navController.navigate("register_account") }
            )
        }
        composable("home") {
            HomeScreen(
                {},
                {},
                {}
            )
        }

        composable("forgot_passwd") {
            ForgotPasswordScreen(
                onEmailSaved = { navController.navigate("reset_otp") }
            )
        }

        composable("reset_otp") {
            ResetPasswordOTPScreen(
                onBackClick = { navController.popBackStack() },
                onVerificationSuccess = { navController.navigate("new_password") }
            )
        }
        composable("new_password") {
            CreateNewPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onPasswordChanged = { navController.navigate("sign_in") }
            )
        }
    }
}