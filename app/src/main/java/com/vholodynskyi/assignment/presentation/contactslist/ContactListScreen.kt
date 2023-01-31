package com.vholodynskyi.assignment.presentation.contactslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vholodynskyi.assignment.R
import com.vholodynskyi.assignment.domain.model.ContactModel
import com.vholodynskyi.assignment.util.noRippleClickable

@Preview(showBackground = true)
@Composable
fun ContactListScreen(
    contacts: List<ContactModel> = listOf(ContactModel.MOCK),
    isLoading:Boolean = false,
    navigateToContact: (String) -> Unit = { println("Navigate to $it")},
    refreshDBContacts: () -> Unit= { println("Refresh")}
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    
    SwipeRefresh(state = swipeRefreshState, onRefresh = { refreshDBContacts()}) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(contacts) { contact ->
                ContactLine(contact, navigateToContact)
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
                        .padding(start = 4.dp)
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
