package com.vholodynskyi.assignment.presentation.details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter.State
import coil.compose.rememberAsyncImagePainter
import com.vholodynskyi.assignment.R
import com.vholodynskyi.assignment.domain.model.ContactModel
import com.vholodynskyi.assignment.util.noRippleClickable

@Preview(showBackground = true)
@Composable
fun DetailsScreen(
    state: ContactModel = ContactModel.MOCK,
    onDeleteClick: () -> Unit = { println("delete") },
    onBackClick: () -> Unit = { println("back") }
) {
    Screen(state, onBackClick, onDeleteClick)
}

@Composable
fun Screen(
    state: ContactModel,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        BackButton(onBackClick)
        UserCard(state, onDeleteClick)
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
fun UserCard(state: ContactModel, onDeleteClick: () -> Unit) {

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
                    model = state.picture
                )
            val transition by animateFloatAsState(
                targetValue = if (painter.state is State.Success) 1f else 0f
            )

            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {

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

                Text(
                    text = state.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = state.email,
                    modifier = Modifier
                        .fillMaxWidth().padding(start = 5.dp)
                )
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
                Spacer(modifier = Modifier.height(30.dp))

            }


        }


    }

}

@Composable
fun LoadingAnimation(modifier:Modifier) {
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
