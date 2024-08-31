package com.example.spotify.presentation.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spotify.R
import com.example.spotify.presentation.ui.components.ParallaxUserImage
import com.example.spotify.presentation.ui.components.ProgressIndicator
import com.example.spotify.presentation.viewmodels.AuthSuccessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthSuccess(
    token: String,
    onClick: () -> Unit,
) {
    val viewModel: AuthSuccessViewModel = hiltViewModel()
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(token) {
        viewModel.loadUserProfile(token)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userProfile?.let { profile ->
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.arror),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary.copy(0.5f)
                )
            )
            ParallaxUserImage(image = profile.images, name = profile.displayName)
            Text(
                modifier = Modifier.padding(top = 30.dp),
                fontSize = 46.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                text = profile.displayName
            )
        } ?: ProgressIndicator()
    }
}