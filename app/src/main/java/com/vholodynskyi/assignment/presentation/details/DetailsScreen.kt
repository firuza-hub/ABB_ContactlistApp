package com.vholodynskyi.assignment.presentation.details

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State
import coil.compose.rememberAsyncImagePainter
import com.vholodynskyi.assignment.R
import com.vholodynskyi.assignment.util.Event
import com.vholodynskyi.assignment.util.noRippleClickable
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    id: String,
    navController: NavController,
    detailsViewModel: DetailsViewModel = koinViewModel()
) {
    val onBackClick: () -> Unit = { navController.navigateUp() }
    val onDeleteClick: () -> Unit = {
        detailsViewModel.delete()
        navController.navigateUp()
    }
    val onSaveClick: () -> Unit = { detailsViewModel.save() }

    val context = LocalContext.current
    LaunchedEffect(key1 = true)
    {

        detailsViewModel.id = id
        detailsViewModel.getData()
        detailsViewModel.eventFlow.collect { event ->
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
    val state by detailsViewModel.state.collectAsState()

    Screen(state, onBackClick, onDeleteClick, onSaveClick)
}

@Composable
fun Screen(
    state: ContactDetailsState,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        BackButton(onBackClick)
        if (!state.isLoading) UserCard(state, onDeleteClick, onSaveClick)
    }
}

@Composable
fun BackButton(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
    ) {
        Image(
            painterResource(R.drawable.ic_prev),
            contentDescription = "Go Back",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 24.dp, top = 12.dp)
                .noRippleClickable { onBackClick() },
        )

    }
}


@Composable
fun UserCard(
    state: ContactDetailsState,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp, top = 12.dp, bottom = 80.dp),
            backgroundColor = colorResource(R.color.cardview_light_background),
            shape = RoundedCornerShape(20.dp),
            elevation = 4.dp
        ) {
            val painter =
                rememberAsyncImagePainter(
                    model = state.contact.picture
                )
            val transition by animateFloatAsState(
                targetValue = if (painter.state is State.Success) 1f else 0f
            )

            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                if (!state.isEdit) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Image(
                            painterResource(R.drawable.ic_edit),
                            contentDescription = "Edit",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .noRippleClickable {
                                    state.isEdit = true
                                },
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                Box(modifier = Modifier.fillMaxWidth()) {

                    if (painter.state is State.Loading) {
                        LoadingAnimation(modifier = Modifier.align(Alignment.Center))
                    }
                    Image(
                        painter = painter,
                        contentDescription = "profile Image",
                        modifier = Modifier
                            .size(280.dp)
                            .padding(top = 10.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .alpha(transition),
                        contentScale = ContentScale.Crop
                    )
                }

                if (state.isEdit) {
                    var name by rememberSaveable {
                        mutableStateOf(state.contact.name)
                    }
                    var email by rememberSaveable {
                        mutableStateOf(state.contact.email)
                    }
                    println("GET_CONTACT - ${state.contact.name} ($name)")
                    UserCardData_Edit(name, email, { name = it }, { email = it })
                    Spacer(modifier = Modifier.height(100.dp))

                    Button(
                        onClick = {
                            state.isEdit = false
                            state.contact.name = name
                            state.contact.email = email
                            onSaveClick()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.colorPrimary)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(10.dp))
                    ) {
                        Text(text = "Save", fontSize = 16.sp)
                    }
                } else {
                    UserCardData_Preview(state)
                    Spacer(modifier = Modifier.height(100.dp))

                    Button(
                        onClick = onDeleteClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.colorAccent)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(10.dp))
                    ) {
                        Text(text = "Delete", fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))

            }


        }


    }

}

@Composable
fun LoadingAnimation(modifier: Modifier) {
    val strokeWidth = 5.dp

    CircularProgressIndicator(
        modifier = modifier.drawBehind {
            drawCircle(
                Color.Red,
                radius = size.width / 2 - strokeWidth.toPx() / 2,
                style = Stroke(strokeWidth.toPx())
            )
        },
        color = Color.LightGray,
        strokeWidth = strokeWidth,
    )
}

@Composable
fun UserCardData_Preview(state: ContactDetailsState) {
    Text(
        text = state.contact.name,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = state.contact.email,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun UserCardData_Edit(
    name: String, email: String, onNameChanged: (newName: String) -> Unit,
    onEmailChanged: (newEmail: String) -> Unit
) {


    BasicTextField(
        value = name,
        onValueChange = { onNameChanged(it) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        textStyle = TextStyle.Default.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold)
    )

    BasicTextField(
        value = email,
        onValueChange = { onEmailChanged(it) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
    )

}
