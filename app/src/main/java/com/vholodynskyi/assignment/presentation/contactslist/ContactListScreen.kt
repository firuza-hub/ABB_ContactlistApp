package com.vholodynskyi.assignment.presentation.contactslist

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.vholodynskyi.assignment.R
import com.vholodynskyi.assignment.domain.model.ContactModel
import com.vholodynskyi.assignment.presentation.details.BackButton
import com.vholodynskyi.assignment.presentation.main.MainViewModel
import com.vholodynskyi.assignment.presentation.navigation.Screen
import com.vholodynskyi.assignment.util.Event
import com.vholodynskyi.assignment.util.noRippleClickable
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ContactListScreen(
    navController: NavController,
    contactListViewModel: ContactsListViewModel = koinViewModel(),
    mainViewModel: MainViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val loginLauncher = rememberLauncherForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result != null) {
            mainViewModel.onSignInResult(result = result)
        }
    }

    val isAuthorized by mainViewModel.isAuthorized.collectAsState()

    LaunchedEffect(key1 = true) {
        mainViewModel.isAuthorized.collect {
            Log.i("FLOW_TEST", "collect triggered")
            if (!it) {
                Log.i("FLOW_TEST", "user is null")
                loginLauncher.launch(mainViewModel.buildLoginIntent())
            }
        }
    }

    LaunchedEffect(key1 = true) {
        contactListViewModel.eventFlow.collect { event ->
            when (event) {
                is Event.ShowToaster -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    val contacts by contactListViewModel.contacts.collectAsState()
    val isLoading by contactListViewModel.isLoading.collectAsState()

    val navigateToContact: (id: String) -> Unit = { id ->
        navController.navigate(
            Screen.DetailsScreen.route + "?contactId=$id"
        )
    }


    val x = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = {
            contactListViewModel.refreshDbContacts()
        })

    Column {

        TopAppBar {
            Button(onClick = { mainViewModel.crashTheApp()}) {
                Text(text = "CRASH :)")
            }
            BackButton {
                mainViewModel.signOut(context)
            }
        }

        if (isAuthorized) {
            Box(
                Modifier
                    .fillMaxSize()
                    .pullRefresh(x)
            ) {
                LazyColumn(
                    Modifier.fillMaxSize()
                )
                {
                    items(contacts) { contact ->
                        ContactLine(contact, navigateToContact)
                    }
                }
                PullRefreshIndicator(refreshing = isLoading, state = x, Modifier.align(TopCenter))
            }
        }
    }

}


@Composable
fun ContactLine(contact: ContactModel, navigateToContact: (String) -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .noRippleClickable { navigateToContact(contact.id) }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = contact.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = contact.email,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }

            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(contact.picture)
                    .build(),
                placeholder = painterResource(id = R.drawable.avatar_placeholder),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painter,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(60.dp)
                    .clip(CircleShape),
                contentDescription = "User avatar"
            )

        }

    }


}
