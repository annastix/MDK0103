package com.example.shoesshop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shoesshop.data.view.CatalogScreen
import com.example.shoesshop.data.view.CheckoutScreen
import com.example.shoesshop.data.view.CreateNewPasswordScreen
import com.example.shoesshop.data.view.DetailsScreen
import com.example.shoesshop.data.view.ForgotPasswordScreen
import com.example.shoesshop.data.view.HomeScreen
import com.example.shoesshop.data.view.MyCartScreen
import com.example.shoesshop.data.view.OnboardingScreen
import com.example.shoesshop.data.view.RegisterAccount
import com.example.shoesshop.data.view.ResetPasswordOTPScreen
import com.example.shoesshop.data.view.SignInScreen
import com.example.shoesshop.data.view.VerificationScreen
import com.example.shoesshop.data.viewModel.CartViewModel
import com.example.shoesshop.data.viewModel.FavouriteViewModel
import com.example.shoesshop.data.viewModel.RegisterAccountViewModel

@Composable
fun NavigationApp(
    navController: NavHostController
) {
    val registerAccountViewModel: RegisterAccountViewModel = viewModel()
    val favouriteViewModel: FavouriteViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()

    val favouriteUiState = favouriteViewModel.uiState.collectAsStateWithLifecycle()
    val favouriteIds = favouriteUiState.value.products.map { it.id }.toSet()

    NavHost(
        navController = navController,
        startDestination = "onboard"
    ) {
        composable("register_account") {
            RegisterAccount(
                viewModel = registerAccountViewModel,
                onBackClick = { navController.popBackStack() },
                onNavigateToSignIn = { navController.navigate("sign_in") },
                onSendOTP = { navController.navigate("send_otp") },
            )
        }
        composable("sign_in") {
            SignInScreen(
                onRegisterClick = { navController.navigate("register_account") },
                onBackClick = { navController.popBackStack() },
                onSignInClick = { navController.navigate("home") },
                resetPassword = { navController.navigate("forgot_passwd") }
            )
        }
        composable("send_otp") {
            VerificationScreen(
                onBackClick = { navController.popBackStack() },
                onVerificationSuccess = { navController.navigate("home") }
            )
        }
        composable("onboard") {
            OnboardingScreen(
                onFinish = { navController.navigate("register_account") }
            )
        }
        composable("home") {
            HomeScreen(
                onProductClick = { product ->
                    navController.navigate("details/${product.id}")
                },
                onCartClick = {
                    navController.navigate("my_cart")   // ← переход в корзину
                },
                onSearchClick = {},
                onSettingsClick = {},
                onCategoryClick = { id, title ->
                    navController.navigate("catalog/$id/$title")
                },
                favouriteViewModel = favouriteViewModel
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

        composable(
            route = "catalog/{categoryId}/{categoryName}"
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""

            CatalogScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                onBackClick = { navController.popBackStack() },
                onProductClick = { product ->
                    navController.navigate("details/${product.id}")
                },
                favouriteViewModel = favouriteViewModel,
                onCartClick = {
                    navController.navigate("my_cart")   // переход в корзину
                }
            )
        }

        composable(
            route = "details/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productId")!!
            DetailsScreen(
                productId = id,
                onBackClick = { navController.popBackStack() },
                favouriteViewModel = favouriteViewModel
            )
        }

        composable("my_cart") {
            MyCartScreen(
                onBackClick = { navController.navigate("home") },
                viewModel = cartViewModel,
                onCheckoutClick = { subtotal, delivery ->
                    navController.navigate("checkout/$subtotal/$delivery")
                }
            )
        }

        composable(
            route = "checkout/{subtotal}/{delivery}",
            arguments = listOf(
                navArgument("subtotal") { type = NavType.FloatType },
                navArgument("delivery") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val context = LocalContext.current
            val userId = cartViewModel.getSavedUserId(context)

            val subtotal = backStackEntry.arguments!!.getFloat("subtotal").toDouble()
            val delivery = backStackEntry.arguments!!.getFloat("delivery").toDouble()

            CheckoutScreen(
                onBackClick = { navController.popBackStack() },
                onBackToHome = { navController.navigate("home") },
                userId = userId,
                paymentId = null,
                totalAmount = subtotal,
                deliveryPrice = delivery,
                cartViewModel = cartViewModel
            )
        }
    }
}
