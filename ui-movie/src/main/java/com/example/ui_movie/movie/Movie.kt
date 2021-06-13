package com.example.ui_movie.movie

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.ui_common_compose.layout.Scaffold
import com.example.ui_common_compose.ratingbar.RatingBar
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

val listImageView = listOf(
    "https://www.film.ru/sites/default/files/movies/posters/3563896-816272.jpg",
    "https://www.film.ru/sites/default/files/movies/posters/3563896-816272.jpg",
    "https://www.film.ru/sites/default/files/movies/posters/3563896-816272.jpg",
    "https://www.film.ru/sites/default/files/movies/posters/3563896-816272.jpg",
    "https://www.film.ru/sites/default/files/movies/posters/3563896-816272.jpg",
    "https://www.film.ru/sites/default/files/movies/posters/3563896-816272.jpg",
)

data class MovieModel(val listMovie: List<String>, val title: String)

val listMovie = listOf(
    MovieModel(listImageView, "Приключене"),
    MovieModel(listImageView, "Фантастика"),
    MovieModel(listImageView, "Драма"),
    MovieModel(listImageView, "Боевик"),
    MovieModel(listImageView, "Юмор"),
    MovieModel(listImageView, "Мелодрама")
)

@Preview
@Composable
fun Movie() {
    val lazyListState = rememberLazyListState()
    var scrolledY = 0f
    var previousOffset = 0

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            reverseLayout = false,
            state = lazyListState,
        ) {
            item {
                CarouselWithHeader(
                    listImageView,
                    modifier = Modifier
                        .graphicsLayer {
                            scrolledY += lazyListState.firstVisibleItemScrollOffset - previousOffset
                            translationY = scrolledY * -0.2F
                            previousOffset = lazyListState.firstVisibleItemScrollOffset
                        }
                )
            }

            items(listMovie) {
                Genres(movie = it)
            }
        }
    }
}

@Composable
internal fun Genres(
    movie: MovieModel
) {
    val contaxt = LocalContext.current
    Column(
        modifier = Modifier.background(color = Color.White, shape = RectangleShape),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = movie.title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 10.dp)
                .wrapContentWidth(Alignment.End)
                .clickable {
                    Toast
                        .makeText(contaxt, "${movie.title}", Toast.LENGTH_SHORT)
                        .show()
                }
        )

        val halfSpacing = 14.dp / 2
        val spacingContent = PaddingValues(halfSpacing, 0.dp, halfSpacing, 10.dp)
        val layoutDir = LocalLayoutDirection.current
        val contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 8.dp, bottom = 10.dp)

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = PaddingValues(
                start = (contentPadding.calculateStartPadding(layoutDir) - halfSpacing).coerceAtLeast(
                    0.dp
                ),
                top = contentPadding.calculateTopPadding(),
                end = (contentPadding.calculateEndPadding(layoutDir) - halfSpacing).coerceAtLeast(0.dp),
                bottom = contentPadding.calculateBottomPadding(),
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(movie.listMovie) {
                Card(
                    modifier = Modifier
                        .padding(spacingContent)
                        .height(150.dp)
                        .fillMaxWidth()
                        .aspectRatio(2 / 3f)
                ) {
                    Box {
                        Image(
                            painter = rememberCoilPainter(request = it),
                            contentDescription = " ",
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class) // HorizontalPager is experimental
@Composable
internal fun CarouselWithHeader(
    items: List<String>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        pageCount = items.size,
        initialOffscreenLimit = 1,
    )
    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 24.dp)
    ) { pager ->
        val item = items[pager]
        PosterCard(
            poster = item,
            modifier = modifier
        )
    }
}

@Composable
internal fun PosterCard(
    modifier: Modifier = Modifier,
    poster: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberCoilPainter(request = poster),
            contentDescription = "",
            modifier = Modifier.drawWithCache {
                val gradient = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = size.height / 2,
                    endY = size.height
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient, blendMode = BlendMode.SrcOver)
                }
            },
            contentScale = ContentScale.FillWidth,
        )

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )

        Text(text = "7.5", fontSize = 30.sp, fontWeight = FontWeight.Light)

        Spacer(
            modifier = Modifier
                .height(7.dp)
        )

        RatingBar(
            rating = 7.5F / 2f,
            modifier = Modifier
                .height(20.dp)
        )

        Spacer(
            modifier = Modifier
                .height(14.dp)
        )

        Text(text = "приключения | фантастика | драма | боевик")
    }
}