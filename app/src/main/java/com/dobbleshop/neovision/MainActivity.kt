package com.dobbleshop.neovision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dobbleshop.neovision.ui.DobbleShopApp
import com.dobbleshop.neovision.ui.theme.DobbleShopTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DobbleShopTheme {
                DobbleShopApp()
            }
        }
    }
}
