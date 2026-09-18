package red.line.callino

import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.request.Options
import okio.buffer
import okio.source

/**
 * مدل داده سبک برای تصاویر assets.
 * مسیر خام فایل داخل پوشه assets را نگه می‌دارد تا اسم فایل‌هایی با
 * ایموجی، فاصله، # و کاراکترهای خاص هم بدون URL-encode کار کنند.
 */
data class AssetImage(val path: String)

/**
 * Fetcher سفارشی Coil برای خواندن مستقیم از AssetManager.
 * مسیر raw (بدون encode) را تحویل context.assets.open() می‌دهد.
 */
class AssetImageFetcher(
    private val data: AssetImage,
    private val options: Options,
    private val context: Context
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val stream = context.assets.open(data.path)
        val source = ImageSource(stream.source().buffer(), context)
        return SourceResult(
            source = source,
            mimeType = null,
            dataSource = DataSource.DISK
        )
    }

    class Factory(private val context: Context) : Fetcher.Factory<AssetImage> {
        override fun create(
            data: AssetImage,
            options: Options,
            imageLoader: ImageLoader
        ): Fetcher = AssetImageFetcher(data, options, context)
    }
}

/**
 * Application کلاس تماسینو.
 * ImageLoader سراسری Coil را با Fetcher سفارشی assets می‌سازد.
 */
class CallinoApp : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(AssetImageFetcher.Factory(applicationContext))
            }
            .crossfade(true)
            .build()
    }
}