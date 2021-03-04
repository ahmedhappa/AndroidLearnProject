package com.example.androidtestproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class Paging3LibraryActivity : AppCompatActivity() {
    private val viewModel: Paging3LibraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagging3_library)

        val movieRecyclerView: RecyclerView = findViewById(R.id.rv_paging3)

        //this is the simple use of paging3 library
        //this observe works only one time
        val movieAdapter = MovieAdapter()
        movieRecyclerView.adapter = movieAdapter
            //to add a loader to list in the footer and header (optional)
            .withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter { movieAdapter.retry() },
                footer = PagingLoadStateAdapter { movieAdapter.retry() }
            )
        viewModel.movies.observe(this, { pagingData ->
            movieAdapter.submitData(lifecycle, pagingData)
        })
        //add error message
        movieAdapter.addLoadStateListener { loadState ->
            when {
                loadState.refresh is LoadState.Loading -> {
                    // show progress par or do some refresh ui ##(works only one time when the list is empty)
                    Log.i("Paging", "Refreshing")
                }
                loadState.append is LoadState.Loading -> {
                    // works every there is a new data that will be added to the list
                    Log.i("Paging", "appending to the list")
                }
                else -> {
                    //stop refresh
                    Log.i("Paging", "Stop Refreshing")
                    val errorState = when {
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }
                    errorState?.let {
                        Toast.makeText(this, it.error.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        //if you want to use it as coroutine
        /*lifecycleScope.launch {
            viewModel.movies.collectLatest {
                movieAdapter.submitData(it)
            }
        }*/

        //to apply separator to paging3
//        val movieSeparatorAdapter = MovieWithSeparatorAdapter()
//        movieRecyclerView.adapter = movieSeparatorAdapter
//        viewModel.moviesWithSeparator.observe(this, { pagingData ->
//            movieSeparatorAdapter.submitData(lifecycle, pagingData)
//        })

    }
}

//for paging3 library
//this is the data source part
/*to use paging we need to create data source (Paging source or remote mediator).
* Paging source is used if you use one type of source to fetch from like network only or database only
* if you use combination of network and local data base you need to use paging source class for database and remote mediator for network
* */
// her in our case we are using network only so we will use paging source class only
/*The PagingSource takes two parameters a Key and a Value.
The Key parameter is the identifier of the data to be loaded such as page number and the Value is the type of the data itself.*/
class MoviePagingSource(
    private val movieApi: MovieDatabaseApi
) : PagingSource<Int, PopularMoviesResponse.MovieDetails>() {
    //Notice the overridden load() function is a suspend function so we can make api requests here to get data from a network or a room database easily.
    //The LoadParams object holds the information about the load operation such as key and page size.
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularMoviesResponse.MovieDetails> {
        return try {
            //current page to load
            val currentPage = params.key ?: 1
            Log.i("Paging", "current Page $currentPage")
            val response = movieApi.getPopularMovies(page = currentPage)
            //If the api request is successful, then we will return the response data wrapped in a LoadResult.Page object along with the previous and next keys.
            LoadResult.Page(
                response.movies,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = response.page + 1
            )
        } catch (e: Exception) {
            //If the api request is unsuccessful then we will return the occurred exception wrapped in a LoadResult.Error object.
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PopularMoviesResponse.MovieDetails>): Int? {
        //******i didn't understand this method.
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.minus(1) ?: anchorPage?.nextKey?.plus(1)
        }
    }
}

class Paging3LibraryViewModel : ViewModel() {
    //.liveData for live data
    //.flow for kotlin coroutines
    //.flowable for rxJava2
    /*
    * The Pager object calls the load() method from the MoviePagingSource object, providing it with the LoadParams object and receiving the LoadResult object in return.
    * We also have to provide configurations such as pageSize with the PagingConfig object.
    * */
    val movies: LiveData<PagingData<PopularMoviesResponse.MovieDetails>> = Pager(
        config = PagingConfig(pageSize = 20)
    ) {
        MoviePagingSource(MovieRetrofit.getApiForMovie())
    }.liveData.cachedIn(viewModelScope) //cache data for better performance

    //if you want to use it as coroutine same result
    /*val movies: Flow<PagingData<PopularMoviesResponse.MovieDetails>> = Pager(
        config = PagingConfig(pageSize = 20)
    ) {
        MoviePagingSource(MovieRetrofit.getApiForMovie())
    }.flow*/


    // to add separator to the list
    sealed class UiModel() {
        data class Movie(val movie: PopularMoviesResponse.MovieDetails) : UiModel()
        data class Separator(val value: String) : UiModel()
    }

    // to add separator to the list
    val moviesWithSeparator: LiveData<PagingData<UiModel>> = Pager(
        config = PagingConfig(pageSize = 20)
    ) {
        MoviePagingSource(MovieRetrofit.getApiForMovie())
    }.liveData.cachedIn(viewModelScope).map { pagingData ->
        pagingData.map { movie ->
            UiModel.Movie(movie)
        }
    }.map { pagingData ->
        pagingData.insertSeparators { before: UiModel.Movie?, after: UiModel.Movie? ->
            if (before?.movie?.original_language != after?.movie?.original_language) {
                UiModel.Separator("New Language:${after?.movie?.original_language}")
            } else {
                null
            }
        }
    }
}

/*PagingDataAdapter takes two parameters, the first one is the type of the data(which in our case is the Movie object),
and the second one is a RecyclerView.ViewHolder*/
class MovieAdapter :
    PagingDataAdapter<PopularMoviesResponse.MovieDetails, MovieAdapter.MovieViewHolder>(
        MovieDiffUtils()
    ) {
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Log.i("Paging", "list count $itemCount")
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler_paging3, parent, false)
        )
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val idTextView: TextView = itemView.findViewById(R.id.tv_movie_id)
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_movie_title)
        private val adultTextView: TextView = itemView.findViewById(R.id.tv_movie_adult)
        private val popularityTextView: TextView = itemView.findViewById(R.id.tv_movie_popularity)
        private val languageTextView: TextView = itemView.findViewById(R.id.tv_movie_Language)

        fun bind(movie: PopularMoviesResponse.MovieDetails?) {
            movie?.apply {
                idTextView.text = id.toString()
                titleTextView.text = original_title
                adultTextView.text = adult.toString()
                popularityTextView.text = popularity.toString()
                languageTextView.text = original_language
            }
        }
    }

    class MovieDiffUtils : DiffUtil.ItemCallback<PopularMoviesResponse.MovieDetails>() {
        override fun areItemsTheSame(
            oldItem: PopularMoviesResponse.MovieDetails,
            newItem: PopularMoviesResponse.MovieDetails
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PopularMoviesResponse.MovieDetails,
            newItem: PopularMoviesResponse.MovieDetails
        ): Boolean {
            return oldItem == newItem
        }

    }
}

//adapter for the pager data with separator
class MovieWithSeparatorAdapter :
    PagingDataAdapter<Paging3LibraryViewModel.UiModel, RecyclerView.ViewHolder>(
        MovieDiffUtils()
    ) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Paging3LibraryViewModel.UiModel.Movie -> R.layout.item_recycler_paging3
            is Paging3LibraryViewModel.UiModel.Separator -> R.layout.item_recycler_paging3_language
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("Paging", "list count $itemCount")
        when (val uiModel = getItem(position)) {
            is Paging3LibraryViewModel.UiModel.Movie -> (holder as MovieViewHolder).bind(uiModel.movie)
            is Paging3LibraryViewModel.UiModel.Separator -> (holder as LanguageViewHolder).bind(
                uiModel.value
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == R.layout.item_recycler_paging3) {
            MovieViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recycler_paging3, parent, false)
            )
        } else {
            LanguageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recycler_paging3_language, parent, false)
            )
        }
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val idTextView: TextView = itemView.findViewById(R.id.tv_movie_id)
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_movie_title)
        private val adultTextView: TextView = itemView.findViewById(R.id.tv_movie_adult)
        private val popularityTextView: TextView = itemView.findViewById(R.id.tv_movie_popularity)
        private val languageTextView: TextView = itemView.findViewById(R.id.tv_movie_Language)

        fun bind(movie: PopularMoviesResponse.MovieDetails?) {
            movie?.apply {
                idTextView.text = id.toString()
                titleTextView.text = original_title
                adultTextView.text = adult.toString()
                popularityTextView.text = popularity.toString()
                languageTextView.text = original_language
            }
        }
    }

    class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val languageTextView: TextView = itemView.findViewById(R.id.tv_movie_Language)

        fun bind(lang: String) {
            languageTextView.text = lang
        }
    }

    class MovieDiffUtils : DiffUtil.ItemCallback<Paging3LibraryViewModel.UiModel>() {
        override fun areItemsTheSame(
            oldItem: Paging3LibraryViewModel.UiModel,
            newItem: Paging3LibraryViewModel.UiModel
        ): Boolean {
            return (oldItem is Paging3LibraryViewModel.UiModel.Movie && newItem is Paging3LibraryViewModel.UiModel.Movie &&
                    oldItem.movie.id == newItem.movie.id) ||
                    (oldItem is Paging3LibraryViewModel.UiModel.Separator && newItem is Paging3LibraryViewModel.UiModel.Separator &&
                            oldItem.value == newItem.value)
        }

        override fun areContentsTheSame(
            oldItem: Paging3LibraryViewModel.UiModel,
            newItem: Paging3LibraryViewModel.UiModel
        ): Boolean {
            return oldItem == newItem
        }

    }
}

//adapter for the loading state in footer
class PagingLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PagingLoadStateAdapter.PagingLoadStateViewHolder>() {


    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadStateViewHolder {
        return PagingLoadStateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler_paging3_load_state_footer, parent, false),
            retry
        )
    }

    class PagingLoadStateViewHolder(itemView: View, retry: () -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val retryBtn: Button = itemView.findViewById(R.id.btn_retry)
        private val errorText: TextView = itemView.findViewById(R.id.tv_error_msg)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)

        init {
            retryBtn.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                errorText.text = loadState.error.localizedMessage
            }

            progressBar.isVisible = loadState is LoadState.Loading
            retryBtn.isVisible = loadState !is LoadState.Loading
            errorText.isVisible = loadState !is LoadState.Loading
        }
    }

}

// for retrofit
data class PopularMoviesResponse(
    val page: Int,
    @SerializedName("results")
    val movies: List<MovieDetails>
) {
    data class MovieDetails(
        val id: Int,
        val original_title: String,
        val adult: Boolean,
        val popularity: Double,
        val original_language: String
    )
}

interface MovieDatabaseApi {
    @GET("popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "36d3944bbcad89f305ec7b255a7939b7",
        @Query("page") page: Int
    ): PopularMoviesResponse
}

object MovieRetrofit {
    fun getApiForMovie(): MovieDatabaseApi {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5L, TimeUnit.SECONDS)
            .readTimeout(5L, TimeUnit.SECONDS)
            .writeTimeout(5L, TimeUnit.SECONDS)

        val httpLogInterceptor = HttpLoggingInterceptor()
        httpLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClient.addInterceptor(httpLogInterceptor)

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .client(okHttpClient.build())
            .build()


        return retrofit.create(MovieDatabaseApi::class.java)
    }
}