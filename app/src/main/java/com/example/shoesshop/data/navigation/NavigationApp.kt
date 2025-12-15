package com.example.shoesshop.navigation
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shoesshop.data.view.RegisterAccount
import com.example.shoesshop.data.viewModel.RegisterAccountViewModel

@Composable
fun NavigationApp(
    navController: NavHostController
) {
    val registerAccountViewModel: RegisterAccountViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "register_account"
    ) {
        composable("register_account") {
            RegisterAccount(
                viewModel = registerAccountViewModel,
                onNavigateToSignIn = {  }
            )
        }
    }
}
